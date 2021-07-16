import com.tony.zrpc.common.serialize.json.ByteUtil;
import com.tony.zrpc.common.serialize.json.JsonSerialize;
import com.tony.zrpc.provider.server.NettyProviderServer;
import com.tony.zrpc.provider.server.RpcRequest;
import com.tony.zrpc.provider.server.RpcResponse;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.apache.log4j.Logger;

import java.net.Socket;

/**
 * @Auther: ljh
 * @Date: 2021/6/30 21:46
 * @Description: PACKAGE_NAME
 * @version: 1.0
 */
public class SocketTest {
    final static byte[] MAGIC = new byte[]{(byte) 0xda, (byte) 0xbb};

    public static void main(String[] args) throws Exception {
        // 1. body

        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setClassName("com.tony.edu.rpc.sms.api.SmsService");
        rpcRequest.setMethodName("send");
        rpcRequest.setParamterType(new Class[]{String.class, String.class});
        rpcRequest.setArguments(new Object[]{"13800138000", "iamtony"});
        byte[] body = new JsonSerialize().serialize(rpcRequest);
        System.out.println(body.length + " - request-body:" + new String(body));
        // build request
        // 2. header
        ByteBuf requestBuffer = Unpooled.buffer();
        requestBuffer.writeByte(MAGIC[0]);
        requestBuffer.writeByte(MAGIC[1]);
        // 3. length
        int len = body.length;
        byte[] lenBytes = ByteUtil.int2bytes(len);
        requestBuffer.writeBytes(lenBytes);
        // 4. body
        requestBuffer.writeBytes(body);
        System.out.println("request length:" + requestBuffer.readableBytes());

        // client
        Socket client = new Socket("127.0.0.1", 8080);
        byte[] req = new byte[requestBuffer.readableBytes()];
        requestBuffer.readBytes(req);

        for (int i = 0; i < 10; i++) {
            client.getOutputStream().write(req);
        }

        System.in.read();
//        // send sned 1 a b
//        byte[] response = new byte[1024];
//        client.getInputStream().read(response);
//
//        System.out.println(new String(response));
//        // RpcResponse
//        RpcResponse rpcResponse = (RpcResponse) new JsonSerialization().deserialize(response, RpcResponse.class);
//        System.out.println(rpcResponse);
//        client.close();
    }
}
