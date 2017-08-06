package com.sonic.http.rpc.consumer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sincetimes.website.core.common.core.StepMillisWatch;
import com.sincetimes.website.core.common.support.DrawTool;
import com.sincetimes.website.core.common.support.LogCore;

/**
 * Hello world!
 */
public class ConsumerApp {
    private static final int _COUNT = 1;
    public static void main(String[] args) throws Exception {
	
	LogCore.BASE.info("消费者者上线了");
	ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring-*.xml");
	context.start();
	PeopleController peopleController = context.getBean(PeopleController.class);
	
	final ExecutorService exec = Executors.newFixedThreadPool(50);
	
	AtomicInteger count = new AtomicInteger(0);
	final CountDownLatch countDownLatch = new CountDownLatch(_COUNT);

	StepMillisWatch sw = new StepMillisWatch();
	sw.reset();
	while (count.getAndIncrement() < _COUNT) {
	    final int _reqId = count.get();
	    exec.submit(() -> {
		LogCore.BASE.info("reqId={},rpc={}",_reqId,
			peopleController.getSpeak(DrawTool.RAND.nextInt(100), DrawTool.RAND.nextInt(2)));
		countDownLatch.countDown();
	    });
	}
	countDownLatch.await();
	LogCore.BASE.info("req num={}, used time={}", _COUNT, sw.interval());
	exec.shutdown(); 
	context.close();
    }
}
