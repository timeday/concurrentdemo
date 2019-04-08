package Semaphore;

import java.util.Map;
import java.util.concurrent.*;

/**
 * 用一个Excel保存了用户所有的银行流水，每个sheet保存一个账户近一年的每笔交易流水，现在需要统计用户的日均交易流水，
 * 先用多线程处理每个sheet里的交易流水，
 * 都处理完后，得到每个sheet的日均交易流水，最后再用barrierAction用这些线程的计算结果，计算出整个Excel的日均银行流水
 */
public class CyclicBarrierDemo implements Runnable {
    /**
     * 创建4个屏障,处理完之后,执行当前类的run方法
     */
    private CyclicBarrier cyclicBarrier = new CyclicBarrier(4, this);

    /**
     * 启动4个线程
     */
    private Executor executor = Executors.newFixedThreadPool(4);

    /**
     * 保存每个sheet计算出来的银行交易流水结果
     */
    private ConcurrentHashMap<String, Integer> sheetBankWaterCount = new ConcurrentHashMap<String, Integer>();


    /**
     * 交易流水统计
     */
    private void count() {
        for (int i = 0; i < 4; i++) {
            executor.execute(new Runnable() {
                public void run() {
                    // 模拟计算当前sheet的银行交易流水数据的业务处理
                    sheetBankWaterCount.put(Thread.currentThread().getName(), 1);
                    // 银行交易流水计算完成后,插入一个屏障
                    try {
                        cyclicBarrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    /**
     * 汇总计算结果
     */
    public void run() {
        int result = 0;
        for (Map.Entry<String, Integer> sheet : sheetBankWaterCount.entrySet()) {
            result += sheet.getValue();
        }
        // 设置计算结果,并输出
        sheetBankWaterCount.put("result", result);
        System.out.println(result);
    }

    public static void main(String[] args) {
        CyclicBarrierDemo service = new CyclicBarrierDemo();
        service.count();
    }
}
