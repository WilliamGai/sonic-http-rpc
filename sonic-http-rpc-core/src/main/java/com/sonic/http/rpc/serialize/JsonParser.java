package com.sonic.http.rpc.serialize;

import com.alibaba.fastjson.JSON;
import com.sincetimes.website.core.common.support.LogCore;
import com.sonic.http.rpc.exception.RpcException;
import com.sonic.http.rpc.exception.RpcExceptionCodeEnum;

public class JsonParser implements Parser {

    public static final Parser parser = new JsonParser();

    public Request reqParse(String param) throws RpcException {
	try {
	    LogCore.RPC.info("调用参数 {}", param);
	    return (Request) JSON.parse(param);
	} catch (Exception e) {
	    LogCore.RPC.error("转换异常 param = {}", param, e);
	    throw new RpcException("", e, RpcExceptionCodeEnum.DATA_PARSER_ERROR.getCode(), param);
	}
    }

    @SuppressWarnings("unchecked")
    public <T> T rspParse(String result) {
	return (T) JSON.parse(result);
    }
}
