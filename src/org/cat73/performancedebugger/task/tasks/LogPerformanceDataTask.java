package org.cat73.performancedebugger.task.tasks;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.management.ManagementFactory;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.cat73.performancedebugger.PerformanceDebugger;
import org.cat73.performancedebugger.task.ITask;
import org.cat73.performancedebugger.utils.Log;

/**
 * 记录性能日志的 Task<br>
 * 每隔一定的 tick 运行一次，判断是否达到目标间隔，如果达到则记录一次性能日志<br>
 * 因此并不能完全保证按照指定的间隔来记录，但在服务器性能良好的情况下一般差距不会太大
 *
 * @author Cat73
 */
public class LogPerformanceDataTask implements ITask {
    /** Bukkit 的 Server 接口 */
    private Server server;
    /** 保存日志的文件 */
    private Writer logWriter = null;
    /** 目标更新间隔 */
    private int interval;
    /** 上次记录日志的时间 */
    private long lastLogTime;
    /** Task ID */
    private int taskID = -1;

    @Override
    public void start(final BukkitScheduler scheduler, final JavaPlugin javaPlugin) {
        this.server = javaPlugin.getServer();

        // 打开日志文件
        final File logFile = PerformanceDebugger.getFile("log_%s.log");
        try {
            this.logWriter = new FileWriter(logFile, true);
        } catch (final Exception e) {
            Log.warn("打开日志文件失败: %s", logFile.getName());
            e.printStackTrace();
            return;
        }

        // 读取配置
        this.interval = javaPlugin.getConfig().getInt("interval", 10000);

        // 写出数据头
        try {
            // 运行时间,TPS,玩家数,总区块数,总实体数,总tiles,剩余内存,每个世界的数据细节,玩家列表
            this.logWriter.write("RunTime(s)\tTPS(Recent 100 tick average)\tPlayerCount\tChunkCount\tEntityCount\tTilesCount\tFreeMem(MB)\tWorldDatas\tPlayers\n");
            this.logWriter.flush();
        } catch (final Exception e) {
            e.printStackTrace();
        }

        // 启动 Task
        this.lastLogTime = System.currentTimeMillis();
        this.taskID = scheduler.runTaskTimer(javaPlugin, this, 20, 20).getTaskId();
    }

    @Override
    public void cancel(final BukkitScheduler scheduler) {
        if (this.taskID != -1) {
            // 取消 Task
            scheduler.cancelTask(this.taskID);

            // 关闭文件
            try {
                this.logWriter.close();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        // 判断是否到下次写出日志的时间
        if (System.currentTimeMillis() - this.lastLogTime < this.interval) {
            return;
        }

        // TODO 异步文件读写, 不要占用主线程时间
        // 打开 log 文件并写出日志
        try {
            // 运行时间
            final int runTime = (int) ((System.currentTimeMillis() - ManagementFactory.getRuntimeMXBean().getStartTime()) / 1000L);
            // TPS
            final double tps = CalculationTPSTask.getLastTPS();
            // 剩余内存 MB
            final int freeRAM = (int) (Runtime.getRuntime().freeMemory() / 1024L / 1024L);

            // 所有世界的统计数据
            // 加载的 区块、实体、tiles 数量、玩家数，以及每个世界的数据细节、在线玩家列表
            int totalChunkCount = 0;
            int totalEntityCount = 0;
            int totalTilesCount = 0;
            int totalPlayerCount = 0;
            final StringBuilder worldData = new StringBuilder();
            final StringBuilder playerList = new StringBuilder();

            // 统计每个世界的信息
            for (final World world : this.server.getWorlds()) {
                if(world == null) {
                    continue;
                }

                // 准备数据
                final Chunk[] chunks = world.getLoadedChunks();
                final List<Player> players = world.getPlayers();

                // 当前世界的统计数据
                final int worldChunkCount = chunks.length;
                final int worldEntityCount = world.getEntities().size();
                int worldTilesCount = 0;
                final int worldPlayerCount = players.size();
                final String worldName = world.getName();

                // 统计每个区块的信息
                for (final Chunk chunk : chunks) {
                    // 更新当前世界的数据
                    if(chunk.isLoaded()) {
                        worldTilesCount += chunk.getTileEntities().length;
                    }
                }

                // 统计当前世界的玩家信息
                for (final Player player : players) {
                    // 在玩家列表中增加玩家
                    playerList.append(player.getName());
                    playerList.append(", ");
                }

                // 更新所有世界的统计数据
                totalChunkCount += worldChunkCount;
                totalEntityCount += worldEntityCount;
                totalTilesCount += worldTilesCount;
                totalPlayerCount += worldPlayerCount;

                // 增加当前世界的数据细节
                worldData.append(String.format("%s: %d,%d,%d,%d; ", worldName, worldChunkCount, worldEntityCount, worldTilesCount, worldPlayerCount));
            }

            // 删除后面多余的分隔符
            if (worldData.toString().endsWith("; ")) {
                worldData.delete(worldData.length() - 2, worldData.length());
            }
            if (playerList.toString().endsWith(", ")) {
                playerList.delete(playerList.length() - 2, playerList.length());
            }

            // 写出到日志文件
            // 运行时间,TPS,玩家数,总区块数,总实体数,总tiles,剩余内存,每个世界的数据细节,玩家列表
            this.logWriter.write(String.format("%d\t%f\t%d\t%d\t%d\t%d\t%d\t%s\t%s\n", runTime, tps, totalPlayerCount, totalChunkCount, totalEntityCount, totalTilesCount, freeRAM, worldData, playerList));
            this.logWriter.flush();
        } catch (final Exception e) {
            e.printStackTrace();
        }

        // 更新上次刷新时间
        this.lastLogTime = System.currentTimeMillis();
    }
}
