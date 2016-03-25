package org.cat73.performancedebugger.command.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.cat73.performancedebugger.command.SubCommandInfo;
import org.cat73.performancedebugger.command.ISubCommand;

@SubCommandInfo(name = "TOP", usage = "<type>", description = "统计某项指标在所有 Chunk 里的前 10 名", help = "type: entity, tiles")
public class TOP implements ISubCommand {
    /** Bukkit 的 Server 接口 */
    private final static Server server = Bukkit.getServer();

    @Override
    public boolean handle(final CommandSender sender, final String[] args) throws Exception {
        if (args.length < 1) {
            return false;
        }

        // 处理类型
        final String typeStr = args[0];
        int type;
        if (typeStr.equals("entity")) {
            type = 1;
        } else if (typeStr.equals("tiles")) {
            type = 2;
        } else {
            return false;
        }

        // 输出信息头
        sender.sendMessage(String.format("%s%s------- 统计信息(%s) ----------------", ChatColor.AQUA, ChatColor.BOLD, typeStr));

        final int[] counts = new int[10];
        final String[] infos = new String[10];
        for (int i = 0; i < counts.length; i++) {
            counts[i] = 0;
        }

        // 统计每个世界的信息
        for (final World world : TOP.server.getWorlds()) {
            final String worldName = world.getName();

            // 统计每个区块的信息
            for (final Chunk chunk : world.getLoadedChunks()) {
                // 当前区块的统计数据
                int count;
                if (type == 1) {
                    count = chunk.getEntities().length;
                } else {
                    count = chunk.getTileEntities().length;
                }

                // 寻找当前的最低值
                int lowPoint = -1;
                for (int i = 0, low = Integer.MAX_VALUE; i < counts.length; i++) {
                    if (counts[i] < low) {
                        low = counts[i];
                        lowPoint = i;
                    }
                }

                // 如果当前值比最低值低, 则直接替换最低值
                if (lowPoint != -1) {
                    if (counts[lowPoint] < count) {
                        counts[lowPoint] = count;
                        infos[lowPoint] = String.format("(%s,%d,%d): %d", worldName, chunk.getX(), chunk.getZ(), count);
                    }
                }
            }
        }

        // 对结果排序
        int temp;
        String tempStr;
        for (int i = counts.length - 1; i > 0; --i) {
            for (int j = 0; j < i; ++j) {
                if (counts[j + 1] > counts[j]) {
                    temp = counts[j];
                    counts[j] = counts[j + 1];
                    counts[j + 1] = temp;

                    tempStr = infos[j];
                    infos[j] = infos[j + 1];
                    infos[j + 1] = tempStr;
                }
            }
        }

        // 输出结果
        for (final String info : infos) {
            if (info != null) {
                sender.sendMessage(ChatColor.GREEN + info);
            }
        }

        return true;
    }
}
