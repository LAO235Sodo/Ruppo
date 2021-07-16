package com.tony.zrpc.registry.redis;

import com.tony.zrpc.registry.NotifyListener;
import com.tony.zrpc.registry.RegistryService;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: ljh
 * @Date: 2021/7/14 22:13
 * @Description: com.tony.zrpc.registry.redis
 * @version: 1.0
 */
public class RedisRegistry implements RegistryService {

    URI address;//redis端口
    private static final int TIME_OUT=15;//默认过期时间
    ScheduledExecutorService scheduledExecutorService=new ScheduledThreadPoolExecutor(5);

    // 服务提供者的信息
    ArrayList<URI> servicesHeartBeat = new ArrayList<>();

    // 服务消费者 -- 本地副本
    //因为 一个服务，对应可能有多个服务实例，
    // 比如zrpc://127.0.0.1:10088/com.tony.edu.sms.api.SmsService/，
    // zrpc://127.0.0.2:10088/com.tony.edu.sms.api.SmsService/，
    // zrpc://127.0.0.3:10088/com.tony.edu.sms.api.SmsService/
    Map<String, Set<URI>> localCache = new ConcurrentHashMap<>();
    JedisPubSub jedisPubSub;
    Map<String, NotifyListener> listenerMap = new ConcurrentHashMap<>();


    /**
     * 注册服务，添加key
     * @param uri
     */
    @Override
    public void register(URI uri) {
        String Key="zrpc:"+uri.toString();//类似于https://196.168.1.2/****
        Jedis jedis=new Jedis(address.getHost(), address.getPort());

        jedis.setex(Key,TIME_OUT,String.valueOf(System.currentTimeMillis()));//主要注册的是key，key的值可以不管
        jedis.close();//关闭连接

        //uri是服务提供者将自己的服务暴露出来，消费者调用，做一个本地缓存，用来发送心跳
        servicesHeartBeat.add(uri);

    }

    @Override
    public void subscriber(String service, NotifyListener notifyListener) throws URISyntaxException {
        if(localCache.get(service) == null) { // 代表第一次初始化 订阅
            localCache.putIfAbsent(service, new HashSet<>());
            // 第一次应该主动的去redis
            Jedis jedis= new Jedis(address.getHost(), address.getPort());  // 默认连接 127.0.0.1 6379
            //根据service去匹配所有的服务实例
            Set<String> serviceInstances = jedis.keys("zrpc:*" + service + "/*");
            //把所有的服务实例添加到该服务名的集合中
            for (String serviceUri : serviceInstances) {
                localCache.get(service).add(new URI(serviceUri));
            }
            // 调用回调通知
            notifyListener.notify(localCache.get(service));
            listenerMap.putIfAbsent(service, notifyListener);
            jedis.close();
        }
    }

    @Override
    public void init(URI address) {
        this.address=address;

        // 定时任务
        scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                // 每隔一段时间，往Redis上面 发送心跳，延长有效期
                Jedis jedis= new Jedis(address.getHost(), address.getPort());  // 默认连接 127.0.0.1 6379
                // 具体哪些服务需要延长
                for (URI uri : servicesHeartBeat) {
                    String key = "zrpc:" + uri.toString();
                    jedis.expire(key, TIME_OUT);
                }
                jedis.close();
            }
        }, 3000, 5000, TimeUnit.MILLISECONDS);



        // 专门启动一个线程，用于 监听 redis里面的数据变化,这里要先在redis客户端开启相关监听相关配置。CONFIG SET notify-keyspace-events KE$xg
        scheduledExecutorService.execute(new Runnable() {
            @Override
            public void run() {

                jedisPubSub = new JedisPubSub() {
                    // 此方法 在收到通知后被触发执行
                    @Override
                    public void onPMessage(String pattern, String channel, String message) {
                        try {

                            // __keyspace@0__:zrpc://127.0.0.1:10088/com.tony.edu.sms.api.SmsService/
                            URI serviceURI = new URI(channel.replace("__keyspace@0__:", ""));
                            String serviceName = serviceURI.getPath().replace("/","");
                            if("set".equals(message)) {
                                // 本地增加 一个服务实例信息
                                Set<URI> uris = localCache.get(serviceName);
                                // 收到的服务实例变动，可能与此消费者无关，此处null判断
                                if(uris != null) {
                                    uris.add(serviceURI);
                                }
                            }
                            // http://wwww.xxx.com:80/path
                            if("expired".equals(message)) {
                                // 过期
                                Set<URI> uris = localCache.get(serviceName);
                                // 收到的服务实例变动，可能与此消费者无关，此处null判断
                                if(uris != null) {
                                    uris.remove(serviceURI);
                                }
                            }
                            // 既然本地数据 localCache 发生了变化， 此时就要通知
                            if("expired".equals(message) || "set".equals(message)) {
                                NotifyListener notifyListener = listenerMap.get(serviceName);
                                if(notifyListener != null) {
                                    notifyListener.notify(localCache.get(serviceName));
                                }
                            }

                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                Jedis jedis= new Jedis(address.getHost(), address.getPort());  // 默认连接 127.0.0.1 6379
                jedis.psubscribe(jedisPubSub, "__keyspace@0__:zrpc:*");
                jedis.close();
            }
        });

    }
}
