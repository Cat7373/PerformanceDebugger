package org.cat73.performancedebugger.task;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.cat73.performancedebugger.IModule;
import org.cat73.performancedebugger.task.tasks.CalculationTPSTask;
import org.cat73.performancedebugger.task.tasks.LogPerformanceDataTask;

/**
 * Task 管理器
 *
 * @author Cat73
 */
public class TaskManager implements IModule {
    private BukkitScheduler scheduler;
    private int logPerformanceDataTaskId;
    private int calculationTPSTaskId;

    @Override
    public void onEnable(final JavaPlugin javaPlugin) {
        this.scheduler = javaPlugin.getServer().getScheduler();

        // 读取配置
        final int interval = javaPlugin.getConfig().getInt("interval", 5000);

        // 记录性能日志, 在 20 个 tick 后启动, 每 20 个 tick 执行一次
        final LogPerformanceDataTask logPerformanceDataTask = new LogPerformanceDataTask(javaPlugin, interval);
        this.logPerformanceDataTaskId = this.scheduler.runTaskTimer(javaPlugin, logPerformanceDataTask, 20, 20).getTaskId();

        // 计算 TPS，异步执行, 在 100 个 tick 后启动, 每 100 个 tick 执行一次
        final CalculationTPSTask calculationTPSTask = new CalculationTPSTask();
        this.calculationTPSTaskId = this.scheduler.scheduleSyncRepeatingTask(javaPlugin, calculationTPSTask, 100, 100);
    }

    @Override
    public void onDisable(final JavaPlugin javaPlugin) {
        // 取消所有 Task
        this.scheduler.cancelTask(this.logPerformanceDataTaskId);
        this.scheduler.cancelTask(this.calculationTPSTaskId);
    }
}
