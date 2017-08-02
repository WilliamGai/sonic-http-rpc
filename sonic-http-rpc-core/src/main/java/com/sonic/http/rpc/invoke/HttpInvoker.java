package com.sonic.http.rpc.invoke;

import java.io.OutputStream;

import com.sincetimes.website.core.common.support.HttpUtil;
import com.sonic.http.rpc.exception.RpcException;

/***
 * 为consumer提供http服务,请求远程的服务提供者
 * 
 * @author bao
 * @date 2017年8月1日 下午11:21:07
 */
public class HttpInvoker implements Invoker {

    public static final Invoker invoker = new HttpInvoker();

    public String request(String request, ConsumerConfig consumerConfig) throws RpcException {
	return HttpUtil.sendPost(consumerConfig.getUrl(), "data=" + request);
    }

    public void response(String response, OutputStream outputStream) throws RpcException {
	try {
	    outputStream.write(response.getBytes("UTF-8"));
	    outputStream.flush();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

}
