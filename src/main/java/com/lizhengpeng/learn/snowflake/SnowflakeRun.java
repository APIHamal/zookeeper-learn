package com.lizhengpeng.learn.snowflake;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * 测试数据
 * @author idealist
 */
public class SnowflakeRun implements Runnable{

    private int idNums;
    private EasySnowflake easySnowflake;
    private CountDownLatch startLatch;
    private CountDownLatch finishLatch;
    private Set<Long> hashSet = new HashSet<>();

    public SnowflakeRun(EasySnowflake easySnowflake,CountDownLatch startLatch,CountDownLatch finishLatch,int idNums){
        this.easySnowflake = easySnowflake;
        this.startLatch = startLatch;
        this.finishLatch = finishLatch;
        this.idNums = idNums;
    }

    public Set<Long> getResult(){
        return hashSet;
    }

    @Override
    public void run() {
        try {
            startLatch.await();
            for(int index = 1;index <= idNums;index++){
                hashSet.add(easySnowflake.nextId());
            }
            finishLatch.countDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
