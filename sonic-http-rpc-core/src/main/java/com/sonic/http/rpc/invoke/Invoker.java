package com.sonic.http.rpc.invoke;

import java.io.OutputStream;

import com.sonic.http.rpc.exception.RpcException;

public interface Invoker {
    /** 调用请求 */
    String request(String request, String url) throws RpcException;

    /** 请求应答 */
    void response(String response, OutputStream outputStream) throws RpcException;
}
