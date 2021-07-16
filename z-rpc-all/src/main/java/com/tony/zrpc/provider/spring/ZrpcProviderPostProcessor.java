package com.tony.zrpc.provider.spring;

import com.tony.zrpc.provider.annotation.ZRpcService;
import com.tony.zrpc.provider.config.ServiceConfig;
import com.tony.zrpc.provider.config.ZrpcProviderContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @Auther: ljh
 * @Date: 2021/7/16 15:06
 * @Description: com.tony.zrpc.provider.spring
 * @version: 1.0
 */
// 在spring启动的过程中， 找到用到了ZRpcService注解
public class ZrpcProviderPostProcessor implements ApplicationContextAware, InstantiationAwareBeanPostProcessor {
    ApplicationContext applicationContext;

    // spring invoker
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    // 每一个 bean 在spring初始化工作之后，交给这个方法，进行再次的处理[拓展]
    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        if(bean.getClass().isAnnotationPresent(ZRpcService.class)) {
            ServiceConfig serviceConfig = new ServiceConfig();
            serviceConfig.setReference(bean);
            // todo 此处只写了 单接口实现
            serviceConfig.setService(bean.getClass().getInterfaces()[0]);
            System.out.println("spring扫描到一个需要开放网络访问的service 实现类：" + serviceConfig);
            ZrpcProviderContext.saveServiceConfig(serviceConfig);
        }
        return true;
    }
}
