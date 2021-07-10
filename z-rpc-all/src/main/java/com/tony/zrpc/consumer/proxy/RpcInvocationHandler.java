package com.tony.zrpc.consumer.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Auther: ljh
 * @Date: 2021/7/10 21:35
 * @Description: com.tony.zrpc.consumer.proxy
 * @version: 1.0
 */
public class RpcInvocationHandler implements InvocationHandler {
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return "动态代理成功";
    }
}
