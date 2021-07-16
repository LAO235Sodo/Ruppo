package com.tony.zrpc.consumer.client;

import com.tony.zrpc.provider.server.RpcRequest;
import io.netty.channel.Channel;

/**
 * @Auther: ljh
 * @Date: 2021/7/10 23:23
 * @Description: com.tony.zrpc.consumer.client
 * @version: 1.0
 */
public class RpcConnection {
    private Channel channel;

    public RpcConnection(Channel channel) {
        this.channel = channel;
    }

    public void call(RpcRequest rpcRequest){
        channel.writeAndFlush(rpcRequest);
    }

}
