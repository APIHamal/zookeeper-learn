package com.lizhengpeng.learn.snowflake;

import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * 雪花算法的一种实现
 * @author idealist
 */
public class EasySnowflake {

    /**
     * 时间戳占用的比特位数
     */
    private static final int TIMESTAMP_BITS = 41;

    /**
     * 工作线程ID占用的比特数
     */
    private static final int WORK_ID_BITS = 10;

    /**
     * 序列号占用的比特数
     */
    private static final int SEQUENCE_BITS = 12;

    /**
     * 时间戳可表示的最大值
     */
    private static final int TIMESTAMP_MAX = ~(-1 << TIMESTAMP_BITS);

    /**
     * 工作线程ID最大值
     */
    private static final int WORK_ID_MAX = ~(-1 << WORK_ID_BITS);

    /**
     * 序列号比特可表示的最大值
     */
    private static final int SEQUENCE_MAX = ~(-1 << SEQUENCE_BITS);

    /**
     * 起始的时间戳
     */
    private static final long TIMESTAMP_START = 1632210057977l;

    /**
     * 最后一次获取到的时间戳
     */
    private long lastTimestamp = -1;

    /**
     * 记录当前的工作线程Id
     */
    private int workId = 10;

    /**
     * 当前时间戳内的序列号
     */
    private int sequence = 0;

    /**
     * 使用雪花算法生成唯一ID数据
     * @return
     */
    public synchronized long nextId(){
        long currentTimestamp = System.currentTimeMillis();
        if(currentTimestamp < lastTimestamp){
            throw new SnowflakeException("timestamp dial back");
        }
        if(currentTimestamp == lastTimestamp){
            sequence = (++sequence) & SEQUENCE_MAX;
            if(sequence == 0){
                currentTimestamp = nextMS(currentTimestamp);
                sequence = 0;
            }
        }else{
            sequence = 0;
        }
        lastTimestamp = currentTimestamp;
        return ((lastTimestamp-TIMESTAMP_START) << (WORK_ID_BITS+SEQUENCE_BITS)) | (workId << SEQUENCE_BITS) | sequence;
    }

    /**
     * 延迟到指定的毫秒
     * @param nextTimestamp
     */
    public long nextMS(long nextTimestamp){
        long currentTimestamp = 0;
        while (currentTimestamp <= nextTimestamp){
            currentTimestamp = System.currentTimeMillis();
        }
        return currentTimestamp;
    }


    public static void main(String[] args) throws InterruptedException {
        int thread_num = 20;
        int preThreadCreateNum = 10000;
        EasySnowflake ins = new EasySnowflake();
        CountDownLatch startLatch = new CountDownLatch(thread_num);
        CountDownLatch finishLatch = new CountDownLatch(thread_num);
        List<SnowflakeRun> runList = new ArrayList<>();
        for(int index = 1;index <= thread_num;index++){
            SnowflakeRun run = new SnowflakeRun(ins,startLatch,finishLatch,preThreadCreateNum);
            runList.add(run);
            new Thread(run).start();
            startLatch.countDown();
        }
        finishLatch.await();
        Set<Long> statSet = new HashSet<>();
        for(SnowflakeRun run : runList){
            statSet.addAll(run.getResult());
        }
        System.out.println("期望长度--->"+(thread_num*preThreadCreateNum));
        System.out.println("生成长度--->"+statSet.size());
        for(Long id : statSet){
            System.out.println("id--->"+id);
        }
    }

}
