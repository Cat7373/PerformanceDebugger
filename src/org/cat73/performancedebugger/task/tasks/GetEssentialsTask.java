package org.cat73.performancedebugger.task.tasks;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.cat73.performancedebugger.utils.GetTPS;
import org.cat73.performancedebugger.utils.Log;

import com.earth2me.essentials.Essentials;

// TODO 如果 Essentials 根本不存在，就会抛异常
/**
 * 获取 Essentials 实例的 Task
 *
 * @author Cat73
 */
public class GetEssentialsTask extends BukkitRunnable {
    @Override
    public void run() {
        // 尝试获取 Essentials 的实例
        final Essentials ess = JavaPlugin.getPlugin(Essentials.class);

        if (ess == null) {
            Log.warn("获取 Essentials 接口失败，改用 Bukkit 的接口获取 TPS 信息.");
        } else {
            Log.info("获取 Essentials 接口成功，将使用 Essentials 的接口获取 TPS 信息.");
            GetTPS.setEssentials(ess);
        }
    }
}
