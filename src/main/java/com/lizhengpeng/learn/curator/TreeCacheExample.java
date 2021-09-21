package com.lizhengpeng.learn.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;

/**
 * treeCache使用示例
 * @author idealist
 */
public class TreeCacheExample {
    public static void main(String[] args) throws Exception {
        /**
         * 创建curator客户端程序
         */
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString("192.168.168.168:2181")
                .retryPolicy(new ExponentialBackoffRetry(1000,3))
                .sessionTimeoutMs(1000*5)
                .build();
        /**
         * 启动curator客户端程序
         */
        curatorFramework.start();
        /**
         * 创建TreeCache监听器程序
         */
        TreeCache treeCache = new TreeCache(curatorFramework,"/hello");
        treeCache.getListenable().addListener(new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                /**
                 * 初始化完成后监听到[增删改]事件时会触发
                 */
                switch (event.getType()){
                    case NODE_ADDED:
                        System.out.println("节点被新增");
                        break;
                    case NODE_UPDATED:
                        System.out.println("节点被更新");
                        break;
                    case NODE_REMOVED:
                        System.out.println("节点被删除");
                        break;
                    default:return;
                }
                /**
                 * 获取当前的节点的数据
                 */
                ChildData data = event.getData();
                if(data != null){
                    System.out.println("节点的路径--->"+data.getPath());
                    System.out.println("节点的内容--->"+new String(data.getData()));
                    Stat nodeStat = data.getStat();
                    System.out.println("节点的状态--->"+nodeStat);
                }
            }
        });
        /**
         * 启动程序
         */
        treeCache.start();
        Thread.sleep(Long.MAX_VALUE);
        treeCache.close();
        /**
         * 停止curator客户端程序
         */
        curatorFramework.close();
    }
}
