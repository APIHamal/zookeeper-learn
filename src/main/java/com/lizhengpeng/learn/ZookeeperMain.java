package com.lizhengpeng.learn;

import com.github.zkclient.ZkClient;
import com.lizhengpeng.learn.util.ZooKeeperFactory;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ZookeeperMain {

    public static void main(String[] args) throws Exception {
//        ZooKeeper zooKeeper = ZooKeeperFactory.getZookeeper();
//        String path = zooKeeper.create("/test","-1".getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
        ZkClient zkClient = new ZkClient("192.168.168.168:2181");
        System.out.println(zkClient.exists("/hello/main"));
    }
}
