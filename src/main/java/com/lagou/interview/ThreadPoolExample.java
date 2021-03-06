package com.lagou.interview;

import java.util.concurrent.*;

/**
 * 线程池示例
 */
public class ThreadPoolExample {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 线程池扩展
        MyThreadPoolExecutor executor = new MyThreadPoolExecutor(2, 4, 10,
                TimeUnit.SECONDS, new LinkedBlockingQueue());
        for (int i = 0; i < 3; i++) {
            executor.execute(() -> {
                Thread.currentThread().getName();
            });
        }

        // 线程池溢出演示
        overflow();

        // 线程池使用
        threadPoolUse();

        // 线程池拒绝策略演示
        rejected();

        // 自定义拒绝策略
        rejectedByCustom();

    }

    /**
     * 自定义拒绝策略
     */
    private static void rejectedByCustom() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 3,
                10, TimeUnit.SECONDS, new LinkedBlockingQueue<>(2),
                new RejectedExecutionHandler() {  // 添加自定义拒绝策略
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        // 业务处理方法
                        System.out.println("执行自定义拒绝策略");
                    }
                });
        for (int i = 0; i < 6; i++) {
            executor.execute(() -> {
                System.out.println(Thread.currentThread().getName());
            });
        }
    }

    /**
     * Java 自带的拒绝策略演示
     */
    private static void rejected() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 3,
                10, TimeUnit.SECONDS, new LinkedBlockingQueue<>(2),
                new ThreadPoolExecutor.AbortPolicy()); // 添加 AbortPolicy 拒绝策略
        for (int i = 0; i < 6; i++) {
            executor.execute(() -> {
                System.out.println(Thread.currentThread().getName());
            });
        }
    }

    /**
     * 线程池使用方式对比
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static void threadPoolUse() throws ExecutionException, InterruptedException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 10, 10L,
                TimeUnit.SECONDS, new LinkedBlockingQueue(20));
        // execute 使用
        executor.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello, execute.");
            }
        });
        // submit 使用
        Future<String> future = executor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println("Hello, submit.");
                return "Success";
            }
        });
        System.out.println(future.get());
    }

    /**
     * 线程池溢出演示
     */
    private static void overflow() {
        Executor executor = new ThreadPoolExecutor(2, 4, 10L,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>(1), new ThreadPoolExecutor.AbortPolicy());
        for (int i = 0; i < 6; i++) {
            executor.execute(() -> {
                System.out.println("当前线程：" + Thread.currentThread().getName());
            });
        }
    }

    /**
     * 线程池扩展
     */
    static class MyThreadPoolExecutor extends ThreadPoolExecutor {
        private final ThreadLocal<Long> localTime = new ThreadLocal<>();

        public MyThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                                    TimeUnit unit, BlockingQueue<Runnable> workQueue) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        }

        /**
         * 开始执行之前
         * @param t 线程
         * @param r 任务
         */
        @Override
        protected void beforeExecute(Thread t, Runnable r) {
            Long sTime = System.nanoTime(); // 开始时间 (单位：纳秒)
            localTime.set(sTime);
            System.out.println(String.format("%s | before | time=%s",
                    t.getName(), sTime));
            super.beforeExecute(t, r);
        }

        /**
         * 执行完成之后
         * @param r 任务
         * @param t 抛出的异常
         */
        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            Long eTime = System.nanoTime(); // 结束时间 (单位：纳秒)
            Long totalTime = eTime - localTime.get(); // 执行总时间
            System.out.println(String.format("%s | after | time=%s | 耗时：%s 毫秒",
                    Thread.currentThread().getName(), eTime, (totalTime / 1000000.0)));
            super.afterExecute(r, t);
        }
    }

}
