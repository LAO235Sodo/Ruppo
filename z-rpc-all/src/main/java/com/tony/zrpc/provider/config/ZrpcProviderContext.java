package com.tony.zrpc.provider.config;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: ljh
 * @Date: 2021/7/16 14:35
 * @Description: com.tony.zrpc.provider.config
 * @version: 1.0
 */
public class ZrpcProviderContext {
    public static ConcurrentHashMap<String, ServiceConfig> serviceMap=new ConcurrentHashMap<>();

    public static void saveServiceConfig(ServiceConfig serviceConfig) {
        serviceMap.putIfAbsent(serviceConfig.getService().getName(),serviceConfig);
    }
}
