package com.tony.zrpc.provider.server;

import java.util.Arrays;

/**
 * @Auther: ljh
 * @Date: 2021/7/1 10:45
 * @Description: com.tony.zrpc.provider.server
 * @version: 1.0
 */
public class RpcRequest {

    private String className ;
    private String methodName;
    private Class[] paramterType;
    private Object[] arguments;

    public RpcRequest() {
    }

    public RpcRequest(String className, String methodName, Class[] paramterType, Object[] arguments) {
        this.className = className;
        this.methodName = methodName;
        this.paramterType = paramterType;
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        return "RpcRequest{" +
                "className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", paramterType=" + Arrays.toString(paramterType) +
                ", arguments=" + Arrays.toString(arguments) +
                '}';
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class[] getParamterType() {
        return paramterType;
    }

    public void setParamterType(Class[] paramterType) {
        this.paramterType = paramterType;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    public Class[] getParameterTypes() {
        return paramterType;
    }
}
