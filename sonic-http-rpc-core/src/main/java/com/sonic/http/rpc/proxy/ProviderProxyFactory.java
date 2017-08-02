package com.sonic.http.rpc.proxy;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mortbay.jetty.handler.AbstractHandler;

import com.sincetimes.website.core.common.support.LogCore;
import com.sonic.http.rpc.container.Container;
import com.sonic.http.rpc.container.HttpContainer;
import com.sonic.http.rpc.exception.RpcException;
import com.sonic.http.rpc.exception.RpcExceptionCodeEnum;
import com.sonic.http.rpc.invoke.HttpInvoker;
import com.sonic.http.rpc.invoke.Invoker;
import com.sonic.http.rpc.invoke.ProviderConfig;
import com.sonic.http.rpc.serialize.Formater;
import com.sonic.http.rpc.serialize.RPCFormater;
import com.sonic.http.rpc.serialize.RPCParser;
import com.sonic.http.rpc.serialize.Parser;
import com.sonic.http.rpc.serialize.Request;

/***
 * 服务提供者核心是反射
 * 继承 org.mortbay.jetty.handler.AbstractHandler Spring<br>
 * 将"com.sonic.http.rpc.api.SpeakInterface"<br>
 * 和"com.sonic.http.rpc.invoke.ProviderConfig"注入providers
 */

public class ProviderProxyFactory extends AbstractHandler {

    private Map<Class<?>, Object> providers = new ConcurrentHashMap<>();// spring注入

    private Parser parser = RPCParser.parser;

    private Formater formater = RPCFormater.formater;

    private Invoker invoker = HttpInvoker.invoker;

    public ProviderProxyFactory(Map<Class<?>, Object> providers) {
	if (Container.container == null) {
	    new HttpContainer(this).start();
	}
	providers.forEach(this::register);
    }

    public ProviderProxyFactory(Map<Class<?>, Object> providers, ProviderConfig providerConfig) {
	if (Container.container == null) {
	    new HttpContainer(this, providerConfig).start();
	}
	providers.forEach(this::register);
    }

    public void register(Class<?> clazz, Object object) {
	providers.put(clazz, object);
	LogCore.BASE.info("{} 已经发布", clazz.getSimpleName());
    }

    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, int dispatch)
	    throws IOException, ServletException {
	String reqStr = request.getParameter("data");
	LogCore.BASE.info("get the param value={}", reqStr);
	try {
	    // 将请求参数解析
	    Request rpcRequest = parser.requestParse(reqStr);
	    // 反射请求
	    // Object result = rpcRequest.invoke(ProviderProxyFactory.getInstance().getBeanByClass(rpcRequest.getClazz()));
	    Object result = rpcRequest.invoke(getBeanByClass(rpcRequest.getClazz()));
	    // 相应请求
	    invoker.response(formater.responseFormat(result), response.getOutputStream());
	} catch (Exception e) {
	    LogCore.RPC.error("providerProxyFactory handle", e);
	}
    }

    public Object getBeanByClass(Class<?> clazz) throws RpcException {
	Object bean = providers.get(clazz);
	if (bean != null) {
	    return bean;
	}
	throw new RpcException(RpcExceptionCodeEnum.NO_BEAN_FOUND.getCode(), clazz);
    }
}
