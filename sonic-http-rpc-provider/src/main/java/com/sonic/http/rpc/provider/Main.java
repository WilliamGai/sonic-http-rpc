package com.sonic.http.rpc.provider;

import java.util.concurrent.CountDownLatch;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sincetimes.website.core.common.support.LogCore;

/**
 * 加载Spring
 */
public class Main {
    public static void main(String[] args) throws Exception {
	LogCore.BASE.info("服务提供者上线了");
	@SuppressWarnings("resource")
	ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring-*.xml");
	context.start();
	LogCore.BASE.info("ClassPathXmlApplicationContext.appName is {}", context.getApplicationName());
	CountDownLatch countDownLatch = new CountDownLatch(1);
	countDownLatch.await();
    }
}
