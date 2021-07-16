package com.tony.zrpc.consumer.spring;

import com.tony.zrpc.consumer.annotation.ZRpcReference;

import com.tony.zrpc.consumer.config.ReferenceConfig;
import com.tony.zrpc.consumer.config.ZrpcConsumerContext;
import com.tony.zrpc.consumer.proxy.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Field;

/**
 * @Auther: ljh
 * @Date: 2021/7/10 21:15
 * @Description: com.tony.zrpc.consumer.spring
 * @version: 1.0
 */
public class ZrpcConsumerPostProcessor implements ApplicationContextAware, InstantiationAwareBeanPostProcessor {
    private ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        for (Field field : bean.getClass().getDeclaredFields()) {
            //判断该属性是否存在该注解
            if(!field.isAnnotationPresent(ZRpcReference.class)){
                continue;
            }
            ReferenceConfig referenceConfig=new ReferenceConfig();
            referenceConfig.setService(field.getType());
            //保存
            ZrpcConsumerContext.saveReferenceConfig(referenceConfig);
            // 构建一个 代理对象
            Object refrenceBean = ProxyFactory.getProxy(new Class[]{field.getType()});
            // 赋值给bean对应的属性
            field.setAccessible(true);
            try {
                field.set(bean, refrenceBean);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }

        return true;
    }
}
