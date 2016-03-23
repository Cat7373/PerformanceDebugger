package org.cat73.performancedebugger.task;

import org.bukkit.plugin.java.JavaPlugin;
import org.cat73.performancedebugger.IModule;
import org.cat73.performancedebugger.task.tasks.GetEssentialsTask;
import org.cat73.performancedebugger.task.tasks.LogPerformanceDataTask;

/**
 * Task 管理器
 *
 * @author Cat73
 */
public class TaskManager implements IModule {
    private LogPerformanceDataTask refreshTask;

    @Override
    public void onEnable(final JavaPlugin javaPlugin) {
        // 读取配置
        final int interval = javaPlugin.getConfig().getInt("interval", 5000);

        // 记录性能日志, 在 50 个 tick 后启动, 之后每 10 个 tick 触发一次
        this.refreshTask = new LogPerformanceDataTask(javaPlugin, interval);
        this.refreshTask.runTaskTimer(javaPlugin, 10, 50);

        // 一次性 Task, 尝试获取 Essentials 的接口, 在 20 个 tick 后运行一次
        new GetEssentialsTask().runTaskLaterAsynchronously(javaPlugin, 20);
    }

    @Override
    public void onDisable(final JavaPlugin javaPlugin) {
        // 取消记录性能日志的 Task
        this.refreshTask.cancel();
    }
}
