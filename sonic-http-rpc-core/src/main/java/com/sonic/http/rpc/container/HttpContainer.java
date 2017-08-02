package com.sonic.http.rpc.container;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.AbstractHandler;
import org.mortbay.jetty.nio.SelectChannelConnector;

import com.sincetimes.website.core.common.support.LogCore;
import com.sonic.http.rpc.invoke.ProviderConfig;
/**
 * ProviderProxyFactory使用
 * @author bao
 * @date 2017年8月1日 下午10:46:49
 */
public class HttpContainer extends Container {

    private AbstractHandler httpHandler;
    private ProviderConfig providerConfig;

    public HttpContainer(AbstractHandler httpHandler) {
	this(httpHandler, new ProviderConfig("/invoke", 8080));
    }

    public HttpContainer(AbstractHandler httpHandler, ProviderConfig providerConfig) {
	this.httpHandler = httpHandler;
	this.providerConfig = providerConfig;
	Container.container = this;
    }

    public void start() {
	Server server = new Server();
	try {
	    SelectChannelConnector connector = new SelectChannelConnector();
	    connector.setPort(providerConfig.getPort());
	    server.setConnectors(new Connector[] { connector });
	    server.setHandler(httpHandler);
	    server.start();
	} catch (Throwable e) {
	    LogCore.BASE.error("容器异常", e);
	}
    }

}
