package com.tony.zrpc.provider.server;

import com.tony.zrpc.common.serialize.json.JsonSerialize;
import org.apache.log4j.Logger;
import org.apache.log4j.net.SocketServer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.serviceloader.ServiceFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Auther: ljh
 * @Date: 2021/6/30 21:28
 * @Description: com.tony.zrpc.provider.server
 * @version: 1.0
 */
@Component
public class ProviderServer implements SmartApplicationListener , ApplicationContextAware {
    private static Logger logger = Logger.getLogger(ProviderServer.class);
    private ApplicationContext applicationContext;

    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return eventType== ContextClosedEvent.class || eventType== ContextStartedEvent.class;
    }



    public int getOrder() {
        return 9999;
    }

    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if (applicationEvent instanceof ContextStartedEvent){
            logger.info("Spring 上下文启动");

            try {
                ServerSocket serverSocket = new ServerSocket(8080);
                while (true){
                    Socket socket = serverSocket.accept();
                    //定义一个字节数组用来接收网络传输过来的数据
                    byte[] request=new byte[1024];
                    socket.getInputStream().read(request);

                    RpcRequest deserialize = (RpcRequest) new JsonSerialize().deserialize(request, RpcRequest.class);

                    //返回与给定的字符串名称相关联类或接口的Class对象
                    //获取到Smsservice接口的Class对象，用去下面的按类型获取对应的Bean
                    Class<?> serviceClass=Class.forName(deserialize.getClassName());
                    //按类型检索bean
                    Object serviceBean=applicationContext.getBean(serviceClass);
                    Method method=serviceBean.getClass().getMethod(deserialize.getMethodName(),deserialize.getParamterType());
                    Object result=method.invoke(serviceBean,deserialize.getArguments());

                    //包装
                    RpcResponse rpcResponse = new RpcResponse();
                    rpcResponse.setStatus(200);
                    rpcResponse.setContext(result);

                    //序列化
                    byte[] serialize = new JsonSerialize().serialize(rpcResponse);
                    socket.getOutputStream().write(serialize);

                    socket.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }
}
