package com.lizhengpeng.learn.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 分布式可重入锁的示例代码
 * @author idealist
 */
public class InterProcessMutexExample {
    public static void main(String[] args) throws Exception {
        /**
         * 创建curator客户端代码
         */
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString("192.168.168.168:2181")
                .retryPolicy(new ExponentialBackoffRetry(1000,3))
                .namespace("lock")
                .sessionTimeoutMs(1000*5)
                .build();
        /**
         * 启动curator客户端程序
         */
        curatorFramework.start();
        /**
         * 创建分布式锁的实现
         */
        InterProcessMutex mutex = new InterProcessMutex(curatorFramework,"/dis_lock");
        /**
         * 尝试进行加锁操作
         */
        mutex.acquire();
        Thread.sleep(5000);
        /**
         * 释放锁的操作
         */
        mutex.release();
        /**
         * 关闭客户端程序
         */
        curatorFramework.close();
    }
}
