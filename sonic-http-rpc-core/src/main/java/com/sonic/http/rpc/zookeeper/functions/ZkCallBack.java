package com.sonic.http.rpc.zookeeper.functions;

import org.I0Itec.zkclient.ZkClient;

public interface ZkCallBack<T> {
    T doInZk(ZkClient zk) throws Exception;
}
