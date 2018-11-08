package com.zj;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * kill -15 pid 优雅停机
 * 关闭 socket 链接
 * 清理临时文件
 * 发送消息通知给订阅方，告知自己下线
 * 将自己将要被销毁的消息通知给子进程
 * 各种资源的释放 ...
 */
public class ShutdownGracefulTest {
    public static ExecutorService executorService = Executors.newCachedThreadPool();

    public static void main(String[] args) {
//假设有5个线程需要执行任务
        for (int i = 0; i < 5; i++) {
            final int id = i;
            Thread taski = new Thread(() -> {
                System.out.println(System.currentTimeMillis() + " : thread_" + id + " start...");
                try {
                    TimeUnit.SECONDS.sleep(id);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(System.currentTimeMillis() + " : thread_" + id + " finish!");
            });
            taski.setDaemon(
                    true
            );
            executorService.submit(taski);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println(System.currentTimeMillis() + " : " + Thread.currentThread().getName() +
                    " No1 shutdown hooking...");
            boolean shutdown = true;
            try {
                executorService.shutdown();
                System.out.println(System.currentTimeMillis() + " : " + Thread.currentThread().getName() +
                        " all thread's done.");
                executorService.awaitTermination(1500, TimeUnit.SECONDS);
                boolean done = false;
                System.out.println(System.currentTimeMillis() + " : " + Thread.currentThread().getName() +
                        " shutdown signal got, wait threadPool finish.");
            } catch (InterruptedException e) {
                e.printStackTrace();
                if (!executorService.isTerminated()) {
                    executorService.shutdownNow();
                }
            }
            System.out.println(System.currentTimeMillis() + " : " + Thread.currentThread().getName());
        }));


        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                System.out.println(System.currentTimeMillis() + " : " + Thread.currentThread().getName() +
                        " No2 shutdown hooking...");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(System.currentTimeMillis() + " : " + Thread.currentThread().getName() +
                    " No2 shutdown done...");
        }));

        System.out.println("main method exit...");
        System.exit(0);
    }
}
