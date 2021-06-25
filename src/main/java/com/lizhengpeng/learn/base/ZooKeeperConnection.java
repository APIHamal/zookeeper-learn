package com.lizhengpeng.learn.base;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Zookeeper客户端连接
 * @author lzp
 */
public class ZooKeeperConnection {

    private ZooKeeper _zooKeeper;
    private String _hostAddress;
    private long _sessionTimeOut;

    /**
     * 默认的构造函数
     * @param hostAddress
     */
    public ZooKeeperConnection(String hostAddress){
        this(hostAddress, 3000);
    }

    /**
     * 默认的构造函数
     * @param hostAddress
     * @param sessionTimeOut
     */
    public ZooKeeperConnection(String hostAddress, long sessionTimeOut){
        this._hostAddress = hostAddress;
        this._sessionTimeOut = sessionTimeOut;
    }

    /**
     * 连接Zookeeper服务器
     */
    public void connection() throws IOException, InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        _zooKeeper = new ZooKeeper(_hostAddress, (int)_sessionTimeOut, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                if(event.getState() == Event.KeeperState.SyncConnected){
                    countDownLatch.countDown();
                }
            }
        });
        countDownLatch.await();
    }

}
