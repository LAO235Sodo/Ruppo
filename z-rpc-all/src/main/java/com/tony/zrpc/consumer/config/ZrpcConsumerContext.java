package com.tony.zrpc.consumer.config;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: ljh
 * @Date: 2021/7/16 16:25
 * @Description: 上下文，保存系统里面所有的依赖，服务信息
 * @version: 1.0
 */
public class ZrpcConsumerContext {
    public static ConcurrentHashMap<String, ReferenceConfig> serviceMap = new ConcurrentHashMap<>();

    public static void saveReferenceConfig(ReferenceConfig referenceConfig) {
        serviceMap.putIfAbsent(referenceConfig.getService().getName(),referenceConfig);
    }
}
