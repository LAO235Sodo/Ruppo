import com.tony.zrpc.provider.server.ProviderServer;
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
    public static void main(String[] args) throws IOException {
        Logger logger = Logger.getLogger(ProviderServer.class);
        // client
        Socket client = new Socket("127.0.0.1", 8080);
        String request = "链接成功";
        client.getOutputStream().write(request.getBytes());

        byte[] response = new byte[1024];
        client.getInputStream().read(response);
        logger.info(new String(response));
        client.close();
    }
}
