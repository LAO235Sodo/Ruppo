package com.tony.zrpc.provider.register;

import com.tony.zrpc.provider.config.RegistryConfig;
import com.tony.zrpc.provider.config.ServerConfig;
import com.tony.zrpc.provider.config.ZrpcProviderContext;
import com.tony.zrpc.provider.server.NettyCodec;
import com.tony.zrpc.provider.server.NettyProviderHandler;
import com.tony.zrpc.provider.server.NettyProviderServer;
import com.tony.zrpc.provider.server.RpcRequest;
import com.tony.zrpc.registry.redis.RedisRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @Auther: ljh
 * @Date: 2021/6/30 21:28
 * @Description: 当Spring启动后,把服务注册到注册中心去，完成服务的暴露
 * @version: 1.0
 */
@Component
public class ProviderRegistStart  implements SmartApplicationListener , ApplicationContextAware {

    private static Logger logger = Logger.getLogger(NettyProviderServer.class);
    private ApplicationContext applicationContext;

    private static RedisRegistry  redisRegistry=new RedisRegistry();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }


    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return eventType== ContextStartedEvent.class;
    }

    @Override
    public int getOrder() {
        return 9999;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        try {
            // 获取所有 service 信息， 挨个注册到 注册中心
            RegistryConfig registryConfig = applicationContext.getBean(RegistryConfig.class);
            ServerConfig serverConfig = applicationContext.getBean(ServerConfig.class);
            redisRegistry.init(new URI(registryConfig.getAddress()));
            // 遍历 - service这么多》哪些需要对外暴露？
            for (String serviceName : ZrpcProviderContext.serviceMap.keySet()) {
                URI serviceURI = new URI("//" + serverConfig.getHost() + ":" + serverConfig.getPort()
                        +"/" + serviceName + "/");
                redisRegistry.register(serviceURI);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }


}
