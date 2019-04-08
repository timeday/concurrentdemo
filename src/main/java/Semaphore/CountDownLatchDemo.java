package Semaphore;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.*;

public class CountDownLatchDemo {
    /**
     * 等待4个线程 进行统计结果
     */
    private static CountDownLatch countDownLatch = new CountDownLatch(4);

    /**
     * 线程池
     */
    private static ExecutorService executor = Executors.newFixedThreadPool(4);

    /**
     * 开启的线程数
     */
    private static int THREAD_COUNT = 4;


    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.execute(new Runnable() {
                public void run() {
                    try {
                        // 模拟业务逻辑的耗时
                        int timer = new Random().nextInt(5);
                        TimeUnit.SECONDS.sleep(timer);
                        System.out.printf("分步骤计算各个子体的数据");
                        // 业务处理完成之后,计数器减一
                        countDownLatch.countDown();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        // 主线程一直被阻塞,直到countDownLatch的值为0
        countDownLatch.await();
        System.out.printf("合并统计最终结果");
        executor.shutdown();
    }
}
