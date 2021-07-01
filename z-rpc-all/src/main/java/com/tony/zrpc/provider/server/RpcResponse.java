package com.tony.zrpc.provider.server;

/**
 * @Auther: ljh
 * @Date: 2021/7/1 9:57
 * @Description: com.tony.zrpc.provider.server
 * @version: 1.0
 */
public class RpcResponse {
    //200 OK,400 Exception
    private int status;
    private Object context;

    public RpcResponse() {

    }

    public RpcResponse(int status, Object context) {
        this.status = status;
        this.context = context;
    }

    @Override
    public String toString() {
        return "RpcResponse{" +
                "status=" + status +
                ", context=" + context +
                '}';
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setContext(Object context) {
        this.context = context;
    }

    public int getStatus() {
        return status;
    }

    public Object getContext() {
        return context;
    }
}
