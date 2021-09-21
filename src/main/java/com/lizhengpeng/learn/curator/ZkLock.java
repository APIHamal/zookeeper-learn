package com.lizhengpeng.learn.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

/**
 * zk实现分布式锁
 * @author idealist
 */
public class ZkLock {

    /**
     * zk连接器对象
     */
    private CuratorFramework curatorFramework;

    /**
     * 可重入锁对象
     */
    private InterProcessMutex interProcessMutex;

    /**
     * 当前尝试加锁的路径
     */
    private String lockPath;

    /**
     * 默认的构造函数
     * @param lockPath
     */
    public ZkLock(String lockPath){
        this.lockPath = lockPath;
        curatorFramework = CuratorFactory.getCuratorFramework();
        interProcessMutex = new InterProcessMutex(curatorFramework, lockPath.startsWith("/") ? lockPath : "/"+lockPath);
    }

    /**
     * 对象进行加锁操作
     */
    public void lock() throws Exception {
        interProcessMutex.acquire();
    }

    /**
     * 分布式锁释放操作
     */
    public void unlock() throws Exception {
        interProcessMutex.release();
    }

    /**
     * 使用分布式锁尝试构建
     * @param args
     */
    public static void main(String[] args) {
        new Thread(()->{
            try {
                ZkLock zkLock = new ZkLock("hello_world");
                System.out.println("线程1尝试加锁");
                zkLock.lock();
                System.out.println("线程1已经获取锁");
                Thread.sleep(1000*3);
                System.out.println("线程1释放锁");
                zkLock.unlock();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(()->{
            try {
                ZkLock zkLock = new ZkLock("hello_world");
                System.out.println("线程2尝试加锁");
                zkLock.lock();
                System.out.println("线程2已经获取锁");
                Thread.sleep(1000*3);
                System.out.println("线程2释放锁");
                zkLock.unlock();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(()->{
            try {
                ZkLock zkLock = new ZkLock("hello_world");
                System.out.println("线程3尝试加锁");
                zkLock.lock();
                System.out.println("线程3已经获取锁");
                Thread.sleep(1000*3);
                System.out.println("线程3释放锁");
                zkLock.unlock();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }

}
