package org.cat73.performancedebugger.task.tasks;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.cat73.performancedebugger.task.ITask;

/**
 * 计算 TPS 的 Task
 *
 * @author cat73
 */
public class CalculationTPSTask implements ITask {
    /** 上次 Task 被主线程执行的时间 */
    private static long lastPoll;
    /** 最后一次统计到的 TPS */
    private static double lastTPS = 20.0D;
    /** Task ID */
    private int taskID = -1;

    /**
     * 获取最近 100 个 tick 的平均 TPS
     *
     * @return 最近 100 个 tick 的平均 TPS
     */
    public static double getLastTPS() {
        return CalculationTPSTask.lastTPS;
    }

    @Override
    public void start(final BukkitScheduler scheduler, final JavaPlugin javaPlugin) {
        // 启动 Task
        CalculationTPSTask.lastPoll = System.nanoTime();
        this.taskID = scheduler.scheduleSyncRepeatingTask(javaPlugin, this, 100, 100);
    }

    @Override
    public void cancel(final BukkitScheduler scheduler) {
        if (this.taskID != -1) {
            // 取消 Task
            scheduler.cancelTask(this.taskID);
        }
    }

    @Override
    public void run() {
        // 获取当前时间
        final long startTime = System.nanoTime();

        // 计算时间间隔
        final long timeSpent = startTime - CalculationTPSTask.lastPoll;

        // 保存本次 Task 被执行的时间
        CalculationTPSTask.lastPoll = startTime;

        // 防止除 0 错误(虽然基本不可能出现)
        if (timeSpent == 0L) {
            return;
        }

        // 计算并保存 TPS
        CalculationTPSTask.lastTPS = 1.0E11D / timeSpent;
    }
}
