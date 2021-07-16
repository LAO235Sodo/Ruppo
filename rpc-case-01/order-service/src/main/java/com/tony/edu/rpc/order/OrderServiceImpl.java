package com.tony.edu.rpc.order;

import com.tony.edu.rpc.order.api.OrderService;
import com.tony.edu.rpc.sms.api.SmsService;
import com.tony.zrpc.consumer.annotation.ZRpcReference;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    // @Autowired // 此处注入 springbean
    @ZRpcReference// 引用一个远程的服务
    SmsService smsService; //  smsService.send 本质 RPC调用 -- 网络数据传输

    @Override
    public void create(String orderContent) {
        System.out.println("订单创建成功：" + orderContent);
        Object smsResult = smsService.send("10086", "你有新订单");
        System.out.println("smsService调用结果：" + smsResult);
    }
}
