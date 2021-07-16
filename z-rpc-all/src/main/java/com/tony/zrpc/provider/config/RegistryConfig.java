package com.tony.zrpc.provider.config;

/**
 * @Auther: ljh
 * @Date: 2021/7/15 22:24
 * @Description: 用于保存服务注册中心的配置，比如redis的地址
 * @version: 1.0
 */
public class RegistryConfig {

    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "RegistryConfig{" +
                "address='" + address + '\'' +
                '}';
    }
}
