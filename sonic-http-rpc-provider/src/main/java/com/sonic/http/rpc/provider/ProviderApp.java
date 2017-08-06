package com.sonic.http.rpc.provider;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sincetimes.website.core.common.support.LogCore;

/**
 * 加载Spring
 */
public class ProviderApp {
    public static void main(String[] args) throws Exception {
	LogCore.BASE.info("服务提供者上线了");
	ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring-*.xml");
	context.start();
//	ProviderConfig obj = context.getBean(ProviderConfig.class);
//	LogCore.BASE.info("obj is {}", obj.getTarget());

	LogCore.BASE.info("ClassPathXmlApplicationContext.appName is {}", context.getApplicationName());
	context.close();
    }
}
