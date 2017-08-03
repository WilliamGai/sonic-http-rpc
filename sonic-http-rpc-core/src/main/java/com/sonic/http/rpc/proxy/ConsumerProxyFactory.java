package com.sonic.http.rpc.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.sincetimes.website.core.common.support.LogCore;
import com.sonic.http.rpc.invoke.ConsumerConfig;
import com.sonic.http.rpc.invoke.HttpInvoker;
import com.sonic.http.rpc.invoke.Invoker;
import com.sonic.http.rpc.serialize.Formater;
import com.sonic.http.rpc.serialize.Parser;
import com.sonic.http.rpc.serialize.RPCFormater;
import com.sonic.http.rpc.serialize.RPCParser;

/**
 * 消费者,核心是代理
 * 
 * @author 继承 java.lang.reflect.InvocationHandler
 */
public class ConsumerProxyFactory implements InvocationHandler {
    private ConsumerConfig consumerConfig;// spring注入 consumerConfig

    private Parser parser = RPCParser.parser;

    private Formater formater = RPCFormater.formater;

    private Invoker invoker = HttpInvoker.invoker;

    private String clazz;// Spring 指定 com.sonic.http.rpc.api.SpeakInterface

    /* create()创建工厂bean speakInterface */
    public Object create() throws Exception {
	Class<?> interfaceClass = Class.forName(clazz);
	return Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[] { interfaceClass }, this);
    }

    /**
     * 实现InvocationHandler的接口<br> TODO: 需要增加失败重试机制
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
	Class<?> interfaceClass = proxy.getClass().getInterfaces()[0];
	String req = formater.requestFormat(interfaceClass, method.getName(), args[0]);
	LogCore.RPC.info("InvocationHandler.invoke, req={}", req);
	String resb = invoker.request(req, consumerConfig.getUrl(interfaceClass));
	return parser.rsponseParse(resb);
    }

    public ConsumerConfig getConsumerConfig() {
	return consumerConfig;
    }

    public void setConsumerConfig(ConsumerConfig consumerConfig) {
	this.consumerConfig = consumerConfig;
    }

    public String getClazz() {
	return clazz;
    }

    public void setClazz(String clazz) {
	this.clazz = clazz;
    }
}
