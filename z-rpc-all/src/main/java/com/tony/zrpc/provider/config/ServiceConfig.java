package com.tony.zrpc.provider.config;

/**
 * @Auther: ljh
 * @Date: 2021/7/16 14:57
 * @Description: com.tony.zrpc.provider.config
 * @version: 1.0
 */
public class ServiceConfig {
    private Class service; // 接口
    private Object reference; // 实例

    @Override
    public String toString() {
        return "ServiceConfig{" +
                "service=" + service +
                ", reference=" + reference +
                '}';
    }

    public Class getService() {
        return service;
    }

    public void setService(Class service) {
        this.service = service;
    }

    public Object getReference() {
        return reference;
    }

    public void setReference(Object reference) {
        this.reference = reference;
    }
}
