package com.lizhengpeng.learn.util;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Zookeeper连接工厂
 * @author lzp
 */
public class ZooKeeperFactory {

    /**
     * Zookeeper集群地址
     */
    private static final String ipAddress = "192.168.168.168:2181";

    /**
     * 创建Zookeeper连接
     * @return
     */
    public static ZooKeeper getZookeeper() throws InterruptedException, IOException {
        CountDownLatch latch = new CountDownLatch(1);
        ZooKeeper zooKeeper = new ZooKeeper(ipAddress,1000,(watchedEvent -> {
            if(watchedEvent.getState() == Watcher.Event.KeeperState.SyncConnected){
                latch.countDown();
            }
        }));
        latch.await();
        return zooKeeper;
    }

}
