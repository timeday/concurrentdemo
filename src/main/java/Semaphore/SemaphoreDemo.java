package Semaphore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreDemo {
    /**
     * 线程数量
     */
    private static final int THREAD_COUNT = 20;

    private volatile static int count=0;

    /**
     * 创建20个线程池
     */
    private static ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

    //每次通过只有10个线程可以并发执行重要的代码
    private static Semaphore semaphore = new Semaphore(10);

    public static void main(String[] args) {
        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.execute(new Runnable() {
                public void run() {
                    try {
                        // 获取一个"许可证"
                        semaphore.acquire();
                        // 模拟数据保存
                        TimeUnit.SECONDS.sleep(2);
                        System.out.println("执行业务逻辑");
                        // 执行完后,归还"许可证"
                        semaphore.release();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        executor.shutdown();
    }
}

