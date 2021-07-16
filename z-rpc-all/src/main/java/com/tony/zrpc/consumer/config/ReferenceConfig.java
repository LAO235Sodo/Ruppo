package com.tony.zrpc.consumer.config;

/**
 * @Auther: ljh
 * @Date: 2021/7/16 16:40
 * @Description: com.tony.zrpc.consumer.config
 * @version: 1.0
 */
public class ReferenceConfig {
    private Class service;

    public Class getService() {
        return service;
    }

    public void setService(Class service) {
        this.service = service;
    }
}
