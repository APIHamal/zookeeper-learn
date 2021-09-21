package com.lizhengpeng.learn.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * PathCache示例代码
 * @author idealist
 */
public class PathCacheExample {
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
         * 创建PathCache监听器
         * 其只能监听子节点的(不能进行递归的监听即[无法监听孙子节点的变化])
         * true表示接收到节点列表变化的同时获取节点的内容
         */
        PathChildrenCache pathChildrenCache = new PathChildrenCache(curatorFramework,"/hello", true);
        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                /**
                 * 如果是初始化的事件类型则直接返回
                 */
                if(event.getType() == PathChildrenCacheEvent.Type.INITIALIZED){
                    return;
                }
                /**
                 * 获取当前触发的事件的类型
                 */
                PathChildrenCacheEvent.Type eventType = event.getType();
                switch (eventType){
                    case CHILD_ADDED:
                        System.out.println("节点被创建");
                        break;
                    case CHILD_REMOVED:
                        System.out.println("节点被删除");
                        break;
                    case CHILD_UPDATED:
                        System.out.println("节点被更新");
                        break;
                    default:return;
                }
                /**
                 * 获取当前的数据节点
                 */
                ChildData childData = event.getData();
                System.out.println("数据内容--->"+childData);
                if(childData != null){
                    System.out.println("节点的路径--->"+childData.getPath());
                    System.out.println("节点的内容--->"+new String(childData.getData()));
                }
            }
        });
        /**
         * 同样需要调用start方法进行监听器的启动
         */
        pathChildrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
        Thread.sleep(Long.MAX_VALUE);
        pathChildrenCache.close();
        /**
         * 关闭curator客户端程序
         */
        curatorFramework.close();
    }
}
