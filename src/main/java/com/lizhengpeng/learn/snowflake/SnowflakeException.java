package com.lizhengpeng.learn.snowflake;

/**
 * 雪花算法相关异常
 * @author idealist
 */
public class SnowflakeException extends RuntimeException{

    public SnowflakeException(String msg){
        super(msg);
    }
}
