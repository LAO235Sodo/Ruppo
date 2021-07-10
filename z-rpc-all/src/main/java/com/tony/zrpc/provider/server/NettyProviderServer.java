package com.tony.zrpc.provider.server;

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

/**
 * @Auther: ljh
 * @Date: 2021/6/30 21:28
 * @Description: com.tony.zrpc.provider.server
 * @version: 1.0
 */
@Component
public class NettyProviderServer implements SmartApplicationListener , ApplicationContextAware {

    private static Logger logger = Logger.getLogger(NettyProviderServer.class);
    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }


    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return eventType== ContextClosedEvent.class || eventType== ContextStartedEvent.class;
    }



    public int getOrder() {
        return 9999;
    }

    public void onApplicationEvent(ApplicationEvent applicationEvent) {

        if (applicationEvent instanceof ContextStartedEvent){
            logger.info("Spring 上下文启动");
            try {
            //创建两个线程组，默认是8个线程
            EventLoopGroup boss = new NioEventLoopGroup();
            EventLoopGroup worker = new NioEventLoopGroup();

            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap
                    .group(boss, worker)
                    //指定的NIO
                    .channel(NioServerSocketChannel.class)
                    //指定监听的端口
                    .localAddress(new InetSocketAddress("127.0.0.1", 8080));

            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {



                    // 定义具体的handler处理顺序
                    socketChannel.pipeline().addLast(new NettyProviderHandler(applicationContext));

                }
            });
                //绑定端口
                serverBootstrap.bind().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }

    }


}
