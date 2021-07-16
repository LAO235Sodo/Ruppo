package com.tony.zrpc.provider.annotation;

import com.tony.zrpc.provider.spring.ZrpcConfiguration;
import com.tony.zrpc.provider.register.ProviderRegistStart;
import com.tony.zrpc.provider.server.NettyProviderServer;
import com.tony.zrpc.provider.spring.ZrpcProviderPostProcessor;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用 服务提供者相关的功能
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({NettyProviderServer.class, ZrpcConfiguration.class,
        ProviderRegistStart.class, ZrpcProviderPostProcessor.class})
public @interface EnableZrpcProvider {
}
