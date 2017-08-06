package com.sonic.http.rpc.invoke;

import java.net.Inet4Address;
import java.net.UnknownHostException;

import com.sincetimes.website.core.common.support.LogCore;
import com.sonic.http.rpc.RpcUtil;
import com.sonic.http.rpc.zookeeper.ZookeeperClient;

public class ProviderConfig {
    private String target;
    private Integer port;// 如果target是zookeeper,则port不起作用了
    private ZookeeperClient client;

    public ProviderConfig(String target, Integer port) {
	this.target = target;
	this.port = port;
	if (target.toLowerCase().startsWith("zookeeper://")) {
	    client = new ZookeeperClient(target.toLowerCase().replaceFirst("zookeeper://", ""));
	}
    }

    public void register(Class<?> clazz) {
	if (client == null) {
	    return;
	}
	String path = RpcUtil.getZkRootPath(clazz);
	String childrenPath = path + "/node";
	client.createPersistent(path);
	client.createEphemeral(childrenPath, getNodeInfo());
    }

    public String getNodeInfo() {
	try {
	    return "http://" + Inet4Address.getLocalHost().getHostAddress() + ":" + getPort();
	} catch (UnknownHostException e) {
	    LogCore.RPC.error("getNodeInfo", e);
	}
	return null;
    }

    public String getTarget() {
	return target;
    }

    public void setTarget(String target) {
	this.target = target;
    }

    public Integer getPort() {
	return port;
    }

    public void setPort(Integer port) {
	this.port = port;
    }
}
