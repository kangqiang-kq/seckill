package com.xxxx.seckill.config;

import org.apache.tomcat.util.threads.ThreadPoolExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Treadtest {
    static Object lock = new Object();
    static volatile int count = 100;

    public static void main(String[] args) {
        // 交替打印 A B
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(new ThreadA());
        executorService.execute(new ThreadB());
        executorService.shutdown();
    }

    static class ThreadA implements Runnable {
        @Override
        public void run() {
            while (count > 0) {
                synchronized (lock) {
                    while (count % 2 == 1) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    if(count == 0) return ; // 防止多打印
                    System.out.println(Thread.currentThread().getName() + ":" + "A" + count);
                    lock.notifyAll();
                    count--;
                }
            }
        }
    }

    static class ThreadB implements Runnable {
        @Override
        public void run() {
            while (count > 0) {
                synchronized (lock) {
                    while (count % 2 == 0) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    if(count == 0) return ;
                    System.out.println(Thread.currentThread().getName() + ":" + "B" + count);
                    lock.notifyAll();
                    count--;
                }
            }
        }
    }
}
