package com.zj.web.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class LockTest {
    private ReentrantLock lock;

    public LockTest() {}

    public LockTest(boolean isFair) {
        super();
        lock = new ReentrantLock(isFair);
    }

    public void ser() {
        try {
            lock.lock();
            System.out.println("ThreadName= " + Thread.currentThread().getName() + " have lock");
        } catch (Exception e) {
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        final LockTest lockTest = new LockTest(true);

        Thread t = new Thread(() -> {
            System.out.println("coming: " + Thread.currentThread().getName());
            lockTest.ser();
        });
        ExecutorService es = Executors.newCachedThreadPool();
        for (int i = 0; i < 3; i++) {
            es.execute(t);
        }
        es.shutdown();
    }
}
