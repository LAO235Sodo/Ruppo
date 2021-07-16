package com.tony.zrpc.consumer.proxy;

import com.tony.zrpc.consumer.client.NettyConsumerClient;
import com.tony.zrpc.consumer.client.RpcConnection;
import com.tony.zrpc.consumer.discovery.DiscoveryClient;
import com.tony.zrpc.provider.server.RpcRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.URI;

/**
 * @Auther: ljh
 * @Date: 2021/7/10 21:35
 * @Description: com.tony.zrpc.consumer.proxy
 * @version: 1.0
 */
public class RpcInvocationHandler implements InvocationHandler {
    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        if("toString".equals(method.getName())) {
            return o.toString();
        }
        // 把 远程 另一个 java服务器 上面的方法触发执行，并且拿到返回值
        // 1. 根据调用的信息，组装一个 RPCrequest对象
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setClassName(method.getDeclaringClass().getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParamterType(method.getParameterTypes());
        rpcRequest.setArguments(args);
        System.out.println("客户端准备发起一次RPC调用:" + rpcRequest);
        // 2. 获取和 服务提供者 的 网络链接
        URI target = DiscoveryClient.chose(rpcRequest.getClassName());
        RpcConnection connect = NettyConsumerClient.connect(target.getHost(), target.getPort());
        // 3. 发送数据 -- 基于网络
        connect.call(rpcRequest);

        return "动态代理成功";
    }
}
