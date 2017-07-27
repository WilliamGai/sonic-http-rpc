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
import com.sonic.http.rpc.serialize.JsonFormater;
import com.sonic.http.rpc.serialize.JsonParser;
import com.sonic.http.rpc.serialize.Parser;
import com.sonic.http.rpc.serialize.Request;

/***
 * 继承
 * org.mortbay.jetty.handler.AbstractHandler
 * @author bao
 */

public class ProviderProxyFactory extends AbstractHandler {

    private Map<Class<?>, Object> providers = new ConcurrentHashMap<>();

    private static ProviderProxyFactory factory;

    private Parser parser = JsonParser.parser;

    private Formater formater = JsonFormater.formater;

    private Invoker invoker = HttpInvoker.invoker;

    public ProviderProxyFactory(Map<Class<?>, Object> providers) {
	if (Container.container == null) {
	    new HttpContainer(this).start();
	}
	for (Map.Entry<Class<?>, Object> entry : providers.entrySet()) {
	    register(entry.getKey(), entry.getValue());
	}
	factory = this;
    }

    public ProviderProxyFactory(Map<Class<?>, Object> providers, ProviderConfig providerConfig) {
	if (Container.container == null) {
	    new HttpContainer(this, providerConfig).start();
	}
	for (Map.Entry<Class<?>, Object> entry : providers.entrySet()) {
	    register(entry.getKey(), entry.getValue());
	}
	factory = this;
    }

    public void register(Class<?> clazz, Object object) {
	providers.put(clazz, object);
	LogCore.BASE.info("{} 已经发布", clazz.getSimpleName());
    }

    public void handle(String target, HttpServletRequest request, HttpServletResponse response, int dispatch)
	    throws IOException, ServletException {
	String reqStr = request.getParameter("data");
	try {
	    // 将请求参数解析
	    Request rpcRequest = parser.reqParse(reqStr);
	    // 反射请求
	    Object result = rpcRequest.invoke(ProviderProxyFactory.getInstance().getBeanByClass(rpcRequest.getClazz()));
	    // 相应请求
	    invoker.response(formater.responseFormat(result), response.getOutputStream());
	} catch (RpcException e) {
	    e.printStackTrace();
	} catch (Throwable e) {
	    e.printStackTrace();
	}
    }

    public Object getBeanByClass(Class<?> clazz) throws RpcException {
	Object bean = providers.get(clazz);
	if (bean != null) {
	    return bean;
	}
	throw new RpcException(RpcExceptionCodeEnum.NO_BEAN_FOUND.getCode(), clazz);
    }

    public static ProviderProxyFactory getInstance() {
	return factory;
    }
}
