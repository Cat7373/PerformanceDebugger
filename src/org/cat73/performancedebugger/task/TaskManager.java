package org.cat73.performancedebugger.task;

import java.util.ArrayList;

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
    /** 所有 Task */
    private final ArrayList<ITask> tasks = new ArrayList<>();

    public TaskManager() {
        // 记录性能日志, 在 20 个 tick 后启动, 每 20 个 tick 执行一次
        this.tasks.add(new LogPerformanceDataTask());
        // 计算 TPS，异步执行, 在 100 个 tick 后启动, 每 100 个 tick 执行一次
        this.tasks.add(new CalculationTPSTask());
    }

    @Override
    public void onEnable(final JavaPlugin javaPlugin) {
        final BukkitScheduler scheduler = javaPlugin.getServer().getScheduler();

        // 启动所有 Task
        for (final ITask task : this.tasks) {
            task.start(scheduler, javaPlugin);
        }
    }

    @Override
    public void onDisable(final JavaPlugin javaPlugin) {
        final BukkitScheduler scheduler = javaPlugin.getServer().getScheduler();

        // 取消所有 Task
        for (final ITask task : this.tasks) {
            task.cancel(scheduler);
        }
    }
}
