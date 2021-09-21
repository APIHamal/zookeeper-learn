package com.lizhengpeng.learn.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * Curator客户端工程
 * @author idealist
 */
public class CuratorFactory {

    /**
     * 单例对象
     */
    private static CuratorFramework curatorFramework;

    /**
     * 静态块中初始化curatorFramework对象
     */
    static {
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString("192.168.168.168:2181")
                .retryPolicy(new ExponentialBackoffRetry(1000,3))
                .sessionTimeoutMs(1000*5)
                .build();
        curatorFramework.start();
    }

    /**
     * 获取当前已经被初始化的curatorFramework对象
     * @return
     */
    public static CuratorFramework getCuratorFramework(){
        return curatorFramework;
    }

}
