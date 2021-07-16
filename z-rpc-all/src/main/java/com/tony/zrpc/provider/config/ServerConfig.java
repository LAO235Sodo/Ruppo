package com.tony.zrpc.provider.config;

/**
 * @Auther: ljh
 * @Date: 2021/7/15 22:22
 * @Description: 服务提供者的配置信息，例如ip和端口
 * @version: 1.0
 */
public class ServerConfig {
    private int port;
    private String host;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String toString() {
        return "ServerConfig{" +
                "port=" + port +
                ", host='" + host + '\'' +
                '}';
    }
}
