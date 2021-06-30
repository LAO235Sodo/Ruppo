package com.tony.edu.rpc.sms;

import com.tony.edu.rpc.sms.api.SmsService;
import com.tony.zrpc.provider.annotation.ZRpcService;

// 面向java接口 远程调用
@ZRpcService // 告诉zrpc框架 此服务需要开放 -- 服务导出
public class SmsServiceImpl implements SmsService {
    public Object send(String phone, String content) {
        System.out.println("移动--发送短信：" + phone + ":" + content);
        return "短信发送成功";
    }
}
