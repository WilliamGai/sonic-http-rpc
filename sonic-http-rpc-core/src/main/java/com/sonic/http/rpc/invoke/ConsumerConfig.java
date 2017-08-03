package com.sonic.http.rpc.invoke;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.sonic.http.rpc.exception.RpcException;
import com.sonic.http.rpc.zookeeper.ZookeeperClient;

public class ConsumerConfig {
    private String url;
    private ZookeeperClient client;
    /** 调用的接口，调用接口的次数 */
    private final ConcurrentHashMap<Class<?>, AtomicInteger> invokeCount = new ConcurrentHashMap<>();

    public String getUrl(Class<?> clazz) throws RpcException {
	if (client != null) {
	    String childrenPath = "/rpc/" + clazz.getName().replaceAll("\\.", "/");
	    List<String> urlList = new ArrayList<String>();
	    List<String> pathList = client.getChildren(childrenPath);
	    for (String path : pathList) {
		String getPath = "/rpc/" + clazz.getName().replaceAll("\\.", "/") + "/" + path;
		String httpUrl = client.getData(getPath);
		if (httpUrl != null) {
		    urlList.add(httpUrl);
		}
	    }
	    return getCurrentUrl(clazz, urlList);
	}
	return url;
    }

    public String getCurrentUrl(Class<?> clazz, List<String> urlList) throws RpcException {
	if (invokeCount.get(clazz) == null)// 初次调用
	{
	    invokeCount.putIfAbsent(clazz, new AtomicInteger(1));
	    return urlList.get(0);
	} else {
	    int i = invokeCount.get(clazz).incrementAndGet();
	    return urlList.get(i % urlList.size());
	}
    }

    public void setUrl(String url) {
	this.url = url;
	if (url.toLowerCase().startsWith("zookeeper://")) {
	    client = new ZookeeperClient(url.toLowerCase().replaceFirst("zookeeper://", ""));
	}
    }
}
