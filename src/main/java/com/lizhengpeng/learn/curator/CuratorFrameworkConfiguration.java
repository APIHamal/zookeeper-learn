package com.lizhengpeng.learn.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * curator框架的配置类
 * @author idealist
 */
@Configuration
@ConditionalOnProperty(prefix = "zookeeper", name = "address")
public class CuratorFrameworkConfiguration {

    @Value("${zookeeper.address}")
    private String zookeeperAddress;

    @Value("${zookeeper.sessionTimeout:5000}")
    private Integer sessionTimeOut;

    @Bean(initMethod = "start", destroyMethod = "close")
    public CuratorFramework curatorFramework(){
        System.out.println("进入框架");
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(zookeeperAddress)
                .retryPolicy(new ExponentialBackoffRetry(1000,5))
                .sessionTimeoutMs(sessionTimeOut)
                .build();
        return curatorFramework;
    }

}
