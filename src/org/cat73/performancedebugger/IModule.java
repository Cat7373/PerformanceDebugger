package org.cat73.performancedebugger;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * 模块接口
 *
 * @author Cat73
 */
public interface IModule {
    /**
     * 插件启用时的触发
     */
    void onEnable(JavaPlugin javaPlugin);

    /**
     * 插件禁用时的触发
     */
    void onDisable(JavaPlugin javaPlugin);
}
