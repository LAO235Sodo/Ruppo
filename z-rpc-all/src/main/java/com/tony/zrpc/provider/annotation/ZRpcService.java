package com.tony.zrpc.provider.annotation;

import org.springframework.stereotype.Service;

import javax.xml.ws.WebServiceClient;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识 此service需要被rpc框架对外暴露
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Service
public @interface ZRpcService {
}
