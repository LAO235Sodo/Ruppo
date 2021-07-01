package com.tony.zrpc.common.serialize.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


import java.text.SimpleDateFormat;

/**
 * @Auther: ljh
 * @Date: 2021/7/1 10:03
 * @Description: json序列化工具类
 * @version: 1.0
 */
public class JsonSerialize {
    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    }

    //将对象转成json的字节数组
    public byte[] serialize(Object output) throws Exception {
        byte[] bytes = objectMapper.writeValueAsBytes(output);
        return bytes;
    }

    //将json字节数组转换成对象
    public Object deserialize(byte[] input, Class clazz) throws Exception {
        Object parse = objectMapper.readValue(input,clazz);
        return parse;
    }
}
