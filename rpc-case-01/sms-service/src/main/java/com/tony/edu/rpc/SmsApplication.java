package com.tony.edu.rpc;

import com.tony.zrpc.provider.annotation.EnableZrpcProvider;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("com.tony.edu.rpc") // spring注解扫描
@PropertySource("classpath:/rpc.properties") // spring
@EnableZrpcProvider
public class SmsApplication {
    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SmsApplication.class);
        context.start();

        // 阻塞不退出，保持Spring容器存活，
        // 当在控制台输入一个数字，则会执行close方法，关闭容器。
        System.in.read();
        context.close();
    }
}