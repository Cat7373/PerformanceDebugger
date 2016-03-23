package org.cat73.performancedebugger.task.tasks;

public class CalculationTPSTask implements Runnable {
    /** 上次 Task 被主线程执行的时间 */
    private static long lastPoll = System.nanoTime();
    /** 最后一次统计到的 TPS */
    private static double lastTPS = 20.0D;

    @Override
    public void run() {
        // 获取当前时间
        final long startTime = System.nanoTime();

        // 计算时间间隔
        long timeSpent = (startTime - CalculationTPSTask.lastPoll) / 1000L;
        if (timeSpent == 0L) {
            timeSpent = 1L;
        }

        // 计算 TPS
        final double tps = 1.0E8D / timeSpent;
        if (tps <= 21.0D) {
            // 保存本次统计到的 TPS
            CalculationTPSTask.lastTPS = tps;
        }

        // 保存本次 Task 被执行的时间
        CalculationTPSTask.lastPoll = startTime;
    }

    /**
     * 获取最近 100 个 tick 的平均 TPS
     *
     * @return 最近 100 个 tick 的平均 TPS
     */
    public static double getLastTPS() {
        return CalculationTPSTask.lastTPS;
    }
}
