package com.tony.zrpc.consumer.client;

import com.tony.zrpc.provider.server.NettyCodec;
import com.tony.zrpc.provider.server.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: ljh
 * @Date: 2021/7/10 23:22
 * @Description: com.tony.zrpc.consumer.client
 * @version: 1.0
 */
public class NettyConsumerClient {
    // 记录 连接的信息 -- 同一个提供者 同一个消费者服务器 之间 创建一个链接
    public static ConcurrentHashMap<InetSocketAddress, RpcConnection> connectionInfo = new ConcurrentHashMap<InetSocketAddress, RpcConnection>();

    public static RpcConnection connect(String host,int port) throws InterruptedException {
        InetSocketAddress providerAddress = new InetSocketAddress(host, port);
        // 如果已经存在连接则直接返回
        RpcConnection existRpcConnection = connectionInfo.get(providerAddress);
        if(existRpcConnection != null) {
            return existRpcConnection;
        }
        NioEventLoopGroup eventExecutors=new NioEventLoopGroup();
        Bootstrap bootstrap=new Bootstrap();
        bootstrap.group(eventExecutors);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new NettyCodec(RpcResponse.class));
            }
        });
        Channel channel = bootstrap.connect(providerAddress).await().channel();
        RpcConnection rpcConnection = new RpcConnection(channel);
        // 再次判断是否有存在的连接， putifabsent 已经存在则返回已存在的， 关闭 刚刚重复创建的
        // putIfAbsent在放入数据时，如果存在重复的key，那么putIfAbsent不会放入值。防止多线程情况下出现问题
        if(connectionInfo.putIfAbsent(providerAddress, rpcConnection) != null) {
            channel.closeFuture();
            return  connectionInfo.get(providerAddress);
        }
        return rpcConnection;


    }
}
