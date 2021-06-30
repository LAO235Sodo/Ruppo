package com.tony.zrpc.consumer.annotation;

import java.lang.annotation.*;


// 启用 zrpc消费者调用的功能
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableZrpcConsumer {
}
