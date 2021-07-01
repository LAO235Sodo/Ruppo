import com.tony.zrpc.common.serialize.json.JsonSerialize;
import com.tony.zrpc.provider.server.ProviderServer;
import com.tony.zrpc.provider.server.RpcRequest;
import com.tony.zrpc.provider.server.RpcResponse;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.Socket;

/**
 * @Auther: ljh
 * @Date: 2021/6/30 21:46
 * @Description: PACKAGE_NAME
 * @version: 1.0
 */
public class SocketTest {
    public static void main(String[] args) throws Exception {
        Logger logger = Logger.getLogger(ProviderServer.class);
        RpcRequest rpcRequest = new RpcRequest();

        rpcRequest.setClassName("com.tony.edu.rpc.sms.api.SmsService");
        rpcRequest.setMethodName("send");
        rpcRequest.setParamterType(new Class[]{String.class,String.class});
        rpcRequest.setArguments(new Object[]{"10086","你好"});

        byte[] serialize = new JsonSerialize().serialize(rpcRequest);

        // client
        Socket client = new Socket("127.0.0.1", 8080);
        String request = "链接成功";
        for (int i = 0; i < 10; i++) {
            client.getOutputStream().write(serialize);
        }


        byte[] response = new byte[1024];
        client.getInputStream().read(response);

        RpcResponse deserialize = (RpcResponse) new JsonSerialize().deserialize(response, RpcResponse.class);

        logger.info(deserialize);

        client.close();
    }
}
