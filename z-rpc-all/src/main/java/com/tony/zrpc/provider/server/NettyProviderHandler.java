package com.tony.zrpc.provider.server;

import com.tony.zrpc.common.serialize.json.JsonSerialize;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.log4j.Logger;

import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;


/**
 * @Auther: ljh
 * @Date: 2021/7/8 21:38
 * @Description: com.tony.zrpc.provider.server
 * @version: 1.0
 */
public class NettyProviderHandler extends SimpleChannelInboundHandler  {
    private static Logger logger = Logger.getLogger(NettyProviderHandler.class);

    //传入服务器端的容器，来获取bean
    private ApplicationContext applicationContext;

    public NettyProviderHandler(ApplicationContext applicationContext) {
        super();
        this.applicationContext = applicationContext;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

        ByteBuf msg=(ByteBuf) o;
        byte[] request=new byte[msg.readableBytes()];
        msg.readBytes(request);


        RpcRequest rpcRequest = (RpcRequest) new JsonSerialize().deserialize(request,RpcRequest.class);

        // 根据请求的参数，定义调用的目标
        String className = rpcRequest.getClassName();
        String methodName = rpcRequest.getMethodName();
        Class[] parameterTypes = rpcRequest.getParameterTypes();
        Object[] arguments = rpcRequest.getArguments();

        Class<?> serviceClass = Class.forName(className);// 获取该类型的类类型

        // 从spring的容器中获取一个bean实例
        Object serviceBean = applicationContext.getBean(serviceClass);
        Method method = serviceBean.getClass().getMethod(methodName, parameterTypes);
        Object result = method.invoke(serviceBean, arguments);

        // 执行结果 包装成RpcResponse对象返回给调用端
        RpcResponse response = new RpcResponse();
        response.setStatus(200);
        response.setContent(result);

        // response - 序列化为 json字符串 -- 字节数组
        byte[] serialize = new JsonSerialize().serialize(response);
        // 包装为 netty 识别的 bytebuf
        channelHandlerContext.writeAndFlush(Unpooled.wrappedBuffer(serialize));


    }


}
