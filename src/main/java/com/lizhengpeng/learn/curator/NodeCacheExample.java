package com.lizhengpeng.learn.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.w3c.dom.Node;

/**
 * NodeCache示例代码
 * @author idealist
 */
public class NodeCacheExample {
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
         * 创建指定的节点监听器
         * NodeCache只能监听指定节点的[增删改]事件无法感知其子节点以及孙子节点的变化
         * 如果监听的节点最初不存在时后来被创建也可以被监听到
         */
        NodeCache nodeCache = new NodeCache(curatorFramework,"/hello");
        /**
         * 添加指定的监听器
         */
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                /**
                 * 获取当前监听的节点的变化事件[增删改]
                 */
                ChildData childData = nodeCache.getCurrentData();
                /**
                 * 如果值为空则表示是删除事件
                 * 否则是新增或者更新事件
                 */
                if(childData == null){
                    System.out.println("zNode节点已经被删除");
                }else{
                    System.out.println("节点路径--->"+childData.getPath());
                    System.out.println("节点内容--->"+new String(childData.getData()));
                    Stat nodeStat = childData.getStat();
                    System.out.println("节点版本--->"+nodeStat.getVersion());
                    if(nodeStat.getVersion() == 0){
                        System.out.println("节点被创建");
                    }else {
                        System.out.println("节点被更新");
                    }
                }
            }
        });
        /**
         * nodeCache同样也需要使用start方法启用监听
         * buildInitial为true表示立即拉取数据因此
         * 当nodeCache启动时如果被监听的节点已经存在则不会触发事件通知
         */
        nodeCache.start();
        Thread.sleep(Long.MAX_VALUE);
        nodeCache.close();
        /**
         * 停止curator客户端程序
         */
        curatorFramework.close();
    }
}
