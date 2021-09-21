package com.lizhengpeng.learn.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * curator常规使用案例
 * @author idealist
 */
public class CuratorExample {
    public static void main(String[] args) throws Exception {
        /**
         * 创建客户端连接
         */
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString("192.168.168.168:2181,192.168.168.169:2181")
                .retryPolicy(new ExponentialBackoffRetry(1000,3))
                .sessionTimeoutMs(1000*5)
                .build();
        /**
         * curator初始化完成后必须调用start方法进行启动
         */
        curatorFramework.start();
        /**
         * 创建指定节点的示例
         * 创建节点时可以同时设置节点的数据
         * create [-s顺序节点] [-e临时节点] path data acl
         * zNode通常有4种常用的节点
         * 持久:[持久节点、持久顺序节点]
         * 临时:[临时节点、临时顺序节点]
         */
        curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/hello");
        /**
         * 更新节点的示例
         * set path data [version版本号]
         * 命令[set path data version]可以类似CAS的方式更新节点的内容
         * 传入-1表示不在意节点的数据版本从而直接更新数据
         */
        curatorFramework.setData().withVersion(-1).forPath("/hello","{text:'world'}".getBytes());
        /**
         * 删除节点的示例
         * delete path [version]
         * 命令[delete path version]表示只删除指定版本的节点
         * 如果版本不符合则不进行删除操作
         */
        curatorFramework.delete().deletingChildrenIfNeeded().forPath("/hello");
        /**
         * 获取指定节点的下级子节点
         * 获取[/hello]路径下的所有的子节点
         */
        curatorFramework.getChildren().forPath("/hello");
        /**
         * 查看数据的版本信息
         * [stat+version]可以实现CAS的功能
         * 例如更新或则删除指定版本的zNode节点数据
         */
        Stat stat = curatorFramework.checkExists().forPath("/hello");
        /**
         * 使用完成后需要调用close进行关闭
         */
        curatorFramework.close();
    }
}
