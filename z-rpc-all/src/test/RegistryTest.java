
import com.tony.zrpc.registry.NotifyListener;
import com.tony.zrpc.registry.redis.RedisRegistry;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

/**
 * @Auther: ljh
 * @Date: 2021/7/14 22:16
 * @Description: PACKAGE_NAME
 * @version: 1.0
 */
public class RegistryTest {
    public static void main(String[] args) throws URISyntaxException, MalformedURLException {
        RedisRegistry redisRegistry=new RedisRegistry();
        redisRegistry.init(new URI("redis://127.0.0.1:6379"));

        redisRegistry.register(new URI("//127.0.0.1:10088/com.tony.edu.sms.api.SmsSercive"));

        // 消费者启动的时候
        redisRegistry.subscriber("com.tony.edu.sms.api.SmsService", new NotifyListener() {
            @Override
            public void notify(Set<URI> uris) {
                System.out.println("服务信息有变化："+uris);
            }
        });
    }
}
