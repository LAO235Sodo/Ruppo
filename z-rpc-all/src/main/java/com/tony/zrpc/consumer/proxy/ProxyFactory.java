package com.tony.zrpc.consumer.proxy;

import java.lang.reflect.Proxy;
import java.net.InterfaceAddress;

/**
 * @Auther: ljh
 * @Date: 2021/7/10 21:32
 * @Description: com.tony.zrpc.consumer.proxy
 * @version: 1.0
 */
public class ProxyFactory {
    public static Object getProxy(Class<?>[] interfaces){
        return Proxy.newProxyInstance(ProxyFactory.class.getClassLoader(), interfaces,new RpcInvocationHandler() );
    }
}
