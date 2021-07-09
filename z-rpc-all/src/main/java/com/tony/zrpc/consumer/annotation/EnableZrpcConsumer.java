package com.tony.zrpc.consumer.annotation;

import com.tony.zrpc.provider.server.NettyProviderServer;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


// 启用 zrpc消费者调用的功能
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(NettyProviderServer.class)
public @interface EnableZrpcConsumer {
}
