package com.sonic.http.rpc.serialize;

import com.sonic.http.rpc.exception.RpcException;

public interface Parser {
    /**
     * @param param 请求参数
     * @return
     */
    Request reqParse(String param) throws RpcException;

    public <T> T rspParse(String result);
}
