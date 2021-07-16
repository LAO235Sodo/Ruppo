package com.tony.zrpc.registry;



import java.net.URI;
import java.util.Set;

/**
 * @Auther: ljh
 * @Date: 2021/7/14 22:10
 * @Description: com.tony.zrpc.registry
 * @version: 1.0
 */
public interface NotifyListener {
    /**
     * 订阅数据发生改变时，则通知所有注册的监听者
     * @param uris
     */
    void notify(Set<URI> uris);
}
