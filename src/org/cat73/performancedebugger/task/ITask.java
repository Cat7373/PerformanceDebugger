package org.cat73.performancedebugger.task;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

/**
 * Task 接口
 *
 * @author cat73
 */
public interface ITask extends Runnable {
    /**
     * 启用该 Task
     * 
     * @param scheduler
     * @param javaPlugin 插件主类
     */
    void start(BukkitScheduler scheduler, JavaPlugin javaPlugin);

    /**
     * 停用该 Task
     * 
     * @param scheduler
     */
    void cancel(BukkitScheduler scheduler);
}
