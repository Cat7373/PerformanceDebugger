package org.cat73.performancedebugger;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import org.bukkit.plugin.java.JavaPlugin;
import org.cat73.performancedebugger.command.CommandHandler;
import org.cat73.performancedebugger.task.TaskManager;
import org.cat73.performancedebugger.utils.Log;

/**
 * 插件主类
 *
 * @author Cat73
 */
public class PerformanceDebugger extends JavaPlugin {
    /** 插件的实例 */
    private static PerformanceDebugger instance;

    /** 所有管理器 */
    private final ArrayList<IModule> modules = new ArrayList<>();

    /**
     * 获取插件的实例
     *
     * @return 插件的实例
     */
    public static PerformanceDebugger instance() {
        return PerformanceDebugger.instance;
    }

    public PerformanceDebugger() {
        PerformanceDebugger.instance = this;

        this.modules.add(new CommandHandler());
        this.modules.add(new TaskManager());
    }

    @Override
    public void onEnable() {
        // 初始化 Log
        Log.setLogger(this.getLogger());

        // 保存默认配置
        this.saveDefaultConfig();

        // 启动所有模块
        for (final IModule manager : this.modules) {
            manager.onEnable(this);
        }

        Log.info("启动成功");
    }

    @Override
    public void onDisable() {
        // 停用所有模块
        for (final IModule manager : this.modules) {
            manager.onDisable(this);
        }

        Log.info("已关闭");
    }

    /**
     * 根据当前时间获取文件
     *
     * @param format 文件名格式
     * @return 文件对象
     */
    public static File getFile(final String format) {
        final Calendar time = Calendar.getInstance();
        final String timeStr = String.format("%d-%d-%d_%d-%d-%d", time.get(Calendar.YEAR), time.get(Calendar.MONTH) + 1, time.get(Calendar.DATE), time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.SECOND));
        final File file = new File(PerformanceDebugger.instance().getDataFolder(), String.format(format, timeStr));

        return file;
    }
}
