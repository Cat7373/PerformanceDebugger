package org.cat73.performancedebugger.utils;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_9_R1.CraftServer;

import com.earth2me.essentials.Essentials;

import net.minecraft.server.v1_9_R1.MinecraftServer;

// TODO bug 如果 Essentials 根本不存在，这个类就没法用
// TODO bug 直接 import MC 内部代码，版本更新后会失效
/**
 * 获取 TPS 的工具类
 *
 * @author Cat73
 */
public class GetTPS {
    private static Essentials ess = null;
    private static final MinecraftServer minecraftServer = ((CraftServer) Bukkit.getServer()).getServer();

    /**
     * 设置 Essentials 实例
     *
     * @param ess Essentials 实例
     */
    public static void setEssentials(final Essentials ess) {
        GetTPS.ess = ess;
    }

    /**
     * 获取当前的 TPS
     *
     * @return 当前的 TPS
     */
    public static double getTPS() {
        return GetTPS.ess == null ? GetTPS.minecraftServer.recentTps[0] : GetTPS.ess.getTimer().getAverageTPS();
    }
}
