package com.tony.edu.rpc;

import com.tony.edu.rpc.order.api.OrderService;
import com.tony.zrpc.consumer.annotation.EnableZrpcConsumer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("com.tony.edu.rpc")
@PropertySource("classpath:/rpc.properties")
@EnableZrpcConsumer
public class OrderApplication {

    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(OrderApplication.class);
        context.start();
        // 测试..模拟调用接口 -- 一定是远程，因为当前的系统没有具体实现类
        OrderService orderService = context.getBean(OrderService.class);
        for (int i = 0; i < 20; i++) {
            orderService.create("买一瓶水");
        }


        // 阻塞不退出
        System.in.read();
        context.close();
    }
}