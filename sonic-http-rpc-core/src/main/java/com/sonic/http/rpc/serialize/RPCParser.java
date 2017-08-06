package com.sonic.http.rpc.serialize;

import com.sincetimes.website.core.common.serialize.interfaces.JSONUtil;
import com.sincetimes.website.core.common.support.LogCore;
import com.sonic.http.rpc.exception.RpcException;
import com.sonic.http.rpc.exception.RpcExceptionCodeEnum;

public class RPCParser implements Parser {

    public static final Parser parser = new RPCParser();

    public Request requestParse(String param) throws RpcException {
	LogCore.RPC.info("调用参数 {}", param);
	try {
	    return JSONUtil.deserialize(param);
	} catch (Exception e) {
	    LogCore.RPC.error("转换异常 param = {}", param, e);
	    throw new RpcException("", e, RpcExceptionCodeEnum.DATA_PARSER_ERROR.getCode(), param);
	}
    }

    public <T> T rsponseParse(String result) {
	return JSONUtil.deserialize(result);
    }
}
