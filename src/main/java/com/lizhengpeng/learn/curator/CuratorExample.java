package com.lizhengpeng.learn.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

/**
 * Curator客户端连接Zookeeper示例
 * @author lzp
 */
public class CuratorExample {

    @Test
    public void createPath() throws Exception {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .retryPolicy(new ExponentialBackoffRetry(1000,3))
                .connectString("192.168.168.168:2181")
                .sessionTimeoutMs(5000)
                .build();
        curatorFramework.start();
        if(curatorFramework.checkExists().forPath("/lizhengpeng") == null){
            System.out.println("路径不存在创建路径");
            curatorFramework.create().forPath("/lizhengpeng");
        }else{
            System.out.println("路径已经存在");
        }
        curatorFramework.close();
    }

    @Test
    public void asyncListener() throws Exception {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString("192.168.168.168:2181")
                .connectionTimeoutMs(2000)
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(500,3))
                .build();
        //添加节点增加、检查、删除、获取等操作的异步监听器
        curatorFramework.getCuratorListenable().addListener(new CuratorListener() {
            @Override
            public void eventReceived(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                System.out.println("异步检测到路径创建成功");
                System.out.println("异步操作的路径--->"+curatorEvent.getPath());
            }
        });
        //添加异步的操作
        curatorFramework.start();
        curatorFramework.create().inBackground().forPath("/hello_world");
        curatorFramework.getData().inBackground().forPath("/first_node");
        curatorFramework.delete().inBackground().forPath("/second_node");
    }

    @Test
    public void distributionLock() throws Exception {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString("192.168.168.168:2181")
                .retryPolicy(new ExponentialBackoffRetry(1000,3))
                .build();
        curatorFramework.start();
        InterProcessMutex lock = new InterProcessMutex(curatorFramework, "/lizhengpeng");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    lock.acquire();
                    System.out.println("线程1拿到分布式锁");
                    Thread.sleep(1000*5);
                    lock.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    lock.acquire();
                    System.out.println("线程2拿到分布式锁");
                    Thread.sleep(1000*5);
                    lock.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        Thread.sleep(Long.MAX_VALUE);
    }

}
