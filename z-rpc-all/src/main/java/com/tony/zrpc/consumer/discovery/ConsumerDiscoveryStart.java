package com.tony.zrpc.consumer.discovery;

import com.tony.zrpc.consumer.config.ZrpcConsumerContext;
import com.tony.zrpc.provider.config.RegistryConfig;
import com.tony.zrpc.provider.config.ServerConfig;
import com.tony.zrpc.registry.NotifyListener;
import com.tony.zrpc.registry.redis.RedisRegistry;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: ljh
 * @Date: 2021/7/16 16:43
 * @Description: com.tony.zrpc.consumer.discovery
 * @version: 1.0
 */
@Component
public class ConsumerDiscoveryStart implements SmartApplicationListener, ApplicationContextAware {
    // 指定  哪些事件下会被触发
    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return  eventType == ContextStartedEvent.class;
    }

    private  static RedisRegistry redisRegistry = new RedisRegistry();

    /***
     * 消费者 本地 保存的 服务实例 列表
     */
    public static Map<String, ArrayList<URI>> serverList = new ConcurrentHashMap<>();

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        try {
            // 订阅 服务信息，并且保存起来， 提供给 代理去使用
            RegistryConfig registryConfig = applicationContext.getBean(RegistryConfig.class);
            ServerConfig serverConfig = applicationContext.getBean(ServerConfig.class);
            redisRegistry.init(new URI(registryConfig.getAddress()));

            // 需要订阅哪些服务？
            for (String serviceName : ZrpcConsumerContext.serviceMap.keySet()) {
                // 1. 初始化一个 空的  实例列表
                serverList.putIfAbsent(serviceName, new ArrayList<>());
                // 2. 订阅
                redisRegistry.subscriber(serviceName, new NotifyListener() {
                    // 当服务列表发生了变动 新增、剔除
                    @Override
                    public void notify(Set<URI> remoteInfo) {
                        ArrayList<URI> localInfo = serverList.get(serviceName);
                        System.out.println("更新前的服务实例信息：" + localInfo );
                        // 剔除 - 本地存在的实例列表，但是 注册中心不存在 ，剔除本地的
                        for (URI uri : localInfo) {
                            if(!remoteInfo.contains(uri)) {
                                localInfo.remove(uri);
                            }
                        }

                        // 新增 - 远程注册中心存在，而本地没有， 添加进去
                        for (URI uri : remoteInfo) {
                            if(!localInfo.contains(uri)) {
                                localInfo.add(uri);
                            }
                        }
                        System.out.println("更新后的服务实例信息：" + localInfo );
                    }
                });
            }

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getOrder() {
        return 9999;
    }

    ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
