package com.tony.zrpc.provider.server;

import org.apache.log4j.Logger;
import org.apache.log4j.net.SocketServer;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Auther: ljh
 * @Date: 2021/6/30 21:28
 * @Description: com.tony.zrpc.provider.server
 * @version: 1.0
 */
@Component
public class ProviderServer implements SmartApplicationListener {
    private static Logger logger = Logger.getLogger(ProviderServer.class);

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
                    logger.info(new String(request));
                    String response="接受成功";
                    socket.getOutputStream().write(response.getBytes());
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
