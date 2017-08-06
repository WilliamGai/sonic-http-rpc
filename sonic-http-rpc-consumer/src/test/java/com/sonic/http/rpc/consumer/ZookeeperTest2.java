package com.sonic.http.rpc.consumer;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;

import org.junit.Test;


import com.sincetimes.website.core.common.core.StepMillisWatch;
import com.sincetimes.website.core.common.support.LogCore;
/**
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath*:spring-*.xml" })
 */
public class ZookeeperTest2 {

    @Resource
    private PeopleController peopleController;

    @Test
    public void test() throws InterruptedException {
	final ExecutorService exec = Executors.newFixedThreadPool(50);
	final int _count = 5;
	AtomicInteger count = new AtomicInteger(0);
	final CountDownLatch countDownLatch = new CountDownLatch(_count);

	StepMillisWatch sw = new StepMillisWatch();
	sw.reset();
	while (count.getAndIncrement() < _count) {
	    exec.submit(() -> {
		LogCore.BASE.info("reqId={},{}",
			peopleController.getSpeak(new Random(100).nextInt(), new Random(1).nextInt()));
		countDownLatch.countDown();
	    });
	}
	countDownLatch.await();
	LogCore.BASE.info("req num={}, used time={}", _count, sw.interval());
	exec.shutdown();
    }

    // @Test
    public void test2() throws InterruptedException {
	final ExecutorService exec = Executors.newFixedThreadPool(50);
	AtomicInteger count = new AtomicInteger(0);
	StepMillisWatch sw = new StepMillisWatch();
	sw.reset();
	// Future<Integer> f1 = exec.submit(call);
	while (count.getAndIncrement() < 100) {
	    exec.submit(() -> {
		LogCore.BASE.info(peopleController.getSpeak(new Random(100).nextInt(), new Random(1).nextInt()));

	    });
	}
	LogCore.BASE.info("req num={}, used time={}", sw.interval());
	exec.shutdown();
    }
}
