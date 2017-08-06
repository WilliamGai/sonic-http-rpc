package com.sonic.http.rpc.invoke;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.sincetimes.website.core.common.extension.AtomicPositiveInteger;
import com.sincetimes.website.core.common.support.Util;
import com.sonic.http.rpc.RpcUtil;
import com.sonic.http.rpc.exception.RpcException;
import com.sonic.http.rpc.zookeeper.ZookeeperClient;

public class ConsumerConfig {
    private String url;
    private ZookeeperClient client;
    /** 调用的接口，调用接口的次数 */
    private final ConcurrentHashMap<Class<?>, AtomicPositiveInteger> INVOKE_COUNT_MAP = new ConcurrentHashMap<>();

    public void setUrl(String url) {
	this.url = url;
	if (url.toLowerCase().startsWith("zookeeper://")) {
	    client = new ZookeeperClient(url.toLowerCase().replaceFirst("zookeeper://", ""));
	}
    }

    public String getUrl(Class<?> clazz) throws RpcException {
	if (client == null) {
	    return url;
	}

	List<String> urlList = getRpcUrls(clazz);
	return getCurrentUrl(clazz, urlList);
    }



    public List<String> getRpcUrls(Class<?> clazz) throws RpcException {
	String rootPath = RpcUtil.getZkRootPath(clazz);
	List<String> childrenList = client.getChildren(rootPath);
	if (Util.isEmpty(childrenList)) {
	    return new ArrayList<String>(0);
	}
	return childrenList.stream().filter(Util::notEmpty).map(ph -> client.getData(rootPath + "/" + ph))
		.filter(Util::notEmpty).collect(Collectors.toList());

    }

    public String getCurrentUrl(Class<?> clazz, List<String> urlList) throws RpcException {
	final int _count = INVOKE_COUNT_MAP.computeIfAbsent(clazz, k -> new AtomicPositiveInteger()).getAndIncrement();
	return urlList.get(_count % urlList.size());
    }

}
