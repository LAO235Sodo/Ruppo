package com.tony.zrpc.consumer.annotation;

import com.tony.zrpc.consumer.discovery.ConsumerDiscoveryStart;
import com.tony.zrpc.consumer.spring.ZrpcConsumerPostProcessor;
import com.tony.zrpc.provider.server.NettyProviderServer;
import com.tony.zrpc.provider.spring.ZrpcConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


// 启用 zrpc消费者调用的功能
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({ZrpcConsumerPostProcessor.class,
        ZrpcConfiguration.class,
        ConsumerDiscoveryStart.class})
public @interface EnableZrpcConsumer {
}
