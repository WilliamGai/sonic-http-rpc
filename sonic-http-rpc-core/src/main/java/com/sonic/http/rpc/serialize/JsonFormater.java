package com.sonic.http.rpc.serialize;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JsonFormater implements Formater
{
    public static final Formater formater = new JsonFormater();

    public String requestFormat(Class<?> clazz, String method, Object param)
    {
        Request request = new Request();
        request.setParam(param);
        request.setClazz(clazz);
        request.setMethod(method);
        return JSON.toJSONString(request, SerializerFeature.WriteClassName);
    }

    public String responseFormat(Object param)
    {
        return JSON.toJSONString(param, SerializerFeature.WriteClassName);
    }

}
