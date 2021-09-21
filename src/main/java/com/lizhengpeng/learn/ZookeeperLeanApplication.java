package com.lizhengpeng.learn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * zookeeper启动类
 * @author idealist
 */
@EnableAutoConfiguration
@ComponentScan
@Configuration
public class ZookeeperLeanApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZookeeperLeanApplication.class, args);
    }
}
