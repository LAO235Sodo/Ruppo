package com.tony.zrpc.provider.spring;

import com.tony.zrpc.provider.config.RegistryConfig;
import com.tony.zrpc.provider.config.ServerConfig;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.reflect.Field;

/**
 * @Auther: ljh
 * @Date: 2021/7/15 22:32
 * @Description: 把自己创建的对象放进Spring容器中，接下来是获取Spring上下文，
 *                 把rpc.properties的配置信息值注入到相应的java对象中去
 * @version: 1.0
 */

public class ZrpcConfiguration implements ImportBeanDefinitionRegistrar {
    StandardEnvironment environment;

    //Spring会自动把上下文环境加载进来
    public ZrpcConfiguration(Environment environment) {
        this.environment = (StandardEnvironment) environment;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
                                        BeanDefinitionRegistry registry,
                                        BeanNameGenerator importBeanNameGenerator) {
        // 告诉spring 让它完成配置对象的--,动态注册bean
        BeanDefinitionBuilder beanDefinitionBuilder = null;
        //根据一个对象的信息构建一个BeanDefinition，后面把这个bean注入到容器中
        beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(ServerConfig.class);

        //遍历ServerConfig的属性
        for (Field field : ServerConfig.class.getDeclaredFields()) {
            //从上下文环境中获取读取的rpc.properties属性值
            String value = environment.getProperty("zrpc.server." + field.getName());
            // 把属性值注入到ean中，目的完成。
            beanDefinitionBuilder.addPropertyValue(field.getName(), value);
        }
        //把该bean注册到容器上下文中
        registry.registerBeanDefinition("serverConfig", beanDefinitionBuilder.getBeanDefinition());


        // 过程理解和上述的一样
        beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(RegistryConfig.class);
        for (Field field : RegistryConfig.class.getDeclaredFields()) {
            String value = environment.getProperty("zrpc.registry." + field.getName());
            // 告诉spring， 这个bean里面每一个属性的值是什么
            beanDefinitionBuilder.addPropertyValue(field.getName(), value);
        }
        registry.registerBeanDefinition("registryConfig", beanDefinitionBuilder.getBeanDefinition());
    }


}
