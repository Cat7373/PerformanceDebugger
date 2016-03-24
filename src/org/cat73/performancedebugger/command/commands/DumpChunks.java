package org.cat73.performancedebugger.command.commands;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.cat73.performancedebugger.PerformanceDebugger;
import org.cat73.performancedebugger.command.CommandInfo;
import org.cat73.performancedebugger.command.ICommand;

/**
 * 统计所有 Chunk 的信息并输出到日志文件
 *
 * @author cat73
 */
@CommandInfo(name = "DumpChunks", description = "统计 Chunk 信息并写出到日志文件里", aliases = "dc")
public class DumpChunks implements ICommand {
    /** Bukkit 的 Server 接口 */
    private final static Server server = Bukkit.getServer();

    @Override
    public boolean execute(final CommandSender sender, final Command command, final String commandLabel, final String[] args) throws Exception {
        // 输出信息头
        sender.sendMessage(String.format("%s%s------- 统计信息 ----------------", ChatColor.AQUA, ChatColor.BOLD));

        // 获取输出信息的文件名
        final File logFile = PerformanceDebugger.getFile("DumpChunks_%s.log");

        // 打开 log 文件并输出信息
        try (Writer logWriter = new FileWriter(logFile, true)) {
            // 写出数据头
            logWriter.write("Values:\n");
            logWriter.write("WorldName\tname\n");
            logWriter.write("Chunk\tx\tz\tEntityCount\tTilesCount\n");
            logWriter.write("Player\tname\tx\ty\tz\n");
            logWriter.write("World\tWorldName\tChunkCount\tEntityCount\tTilesCount\tPlayerCount\n");
            logWriter.write("Total\tChunkCount\tEntityCount\tTilesCount\tPlayerCount\n\n");
            logWriter.write("Type\tValues\n");

            // 所有世界的统计数据
            int totalChunkCount = 0;
            int totalEntityCount = 0;
            int totalTilesCount = 0;
            int totalPlayerCount = 0;

            // 统计每个世界的信息
            for (final World world : DumpChunks.server.getWorlds()) {
                // 当前世界的统计数据
                int worldChunkCount = 0;
                int worldEntityCount = 0;
                int worldTilesCount = 0;
                int worldPlayerCount = 0;
                final String worldName = world.getName();

                // 写出世界名称
                logWriter.write(String.format("WorldName\t%s\n", worldName));

                // 统计每个区块的信息
                for (final Chunk chunk : world.getLoadedChunks()) {
                    // 当前区块的统计数据
                    final int chunkEntityCount = chunk.getEntities().length;
                    final int chunkTilesCount = chunk.getTileEntities().length;

                    // 更新当前世界的数据
                    worldChunkCount++;
                    worldEntityCount += chunkEntityCount;
                    worldTilesCount += chunkTilesCount;

                    // 向日志写出数据
                    logWriter.write(String.format("Chunk\t%d\t%d\t%d\t%d\n", chunk.getX(), chunk.getZ(), chunkEntityCount, chunkTilesCount));
                }

                // 统计当前世界的玩家信息
                for (final Player player : world.getPlayers()) {
                    // 更新当前世界的数据
                    worldPlayerCount++;

                    // 向日志写出数据
                    final Location location = player.getLocation();
                    logWriter.write(String.format("Player\t%s\t%f\t%f\t%f\n", player.getName(), location.getX(), location.getY(), location.getZ()));
                }

                // 更新所有世界的统计数据
                totalChunkCount += worldChunkCount;
                totalEntityCount += worldEntityCount;
                totalTilesCount += worldTilesCount;
                totalPlayerCount += worldPlayerCount;

                // 向日志写出数据并向执行者输出信息
                logWriter.write(String.format("World\t%s\t%d\t%d\t%d\t%d\n\n", worldName, worldChunkCount, worldEntityCount, worldTilesCount, worldPlayerCount));
                sender.sendMessage(ChatColor.GREEN + String.format("World(%s): Chunk: %d, Entity: %d, Tiles: %d, Player: %d", worldName, worldChunkCount, worldEntityCount, worldTilesCount, worldPlayerCount));
            }

            // 向日志写出数据并向执行者输出信息
            logWriter.write(String.format("Total\t%d\t%d\t%d\t%d\n", totalChunkCount, totalEntityCount, totalTilesCount, totalPlayerCount));
            sender.sendMessage(ChatColor.GREEN + String.format("Total: Chunk: %d, Entity: %d, Tiles: %d, Player: %d", totalChunkCount, totalEntityCount, totalTilesCount, totalPlayerCount));
        } catch (final Exception e) {
            throw e;
        }

        // 输出日志文件路径
        sender.sendMessage(String.format("%s%s细节数据请查看: plugins/%s/%s", ChatColor.BLUE, ChatColor.BOLD, PerformanceDebugger.instance().getName(), logFile.getName()));

        return true;
    }
}
