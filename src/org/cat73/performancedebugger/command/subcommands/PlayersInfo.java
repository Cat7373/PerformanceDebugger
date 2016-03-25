package org.cat73.performancedebugger.command.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.cat73.performancedebugger.command.SubCommandInfo;
import org.cat73.performancedebugger.command.ISubCommand;

/**
 * 显示所有玩家的统计信息 (目前只统计所在坐标)
 *
 * @author cat73
 */
@SubCommandInfo(name = "PlayersInfo", description = "显示所有玩家的位置", aliases = "pi")
public class PlayersInfo implements ISubCommand {
    /** Bukkit 的 Server 接口 */
    private final static Server server = Bukkit.getServer();

    @Override
    public boolean handle(final CommandSender sender, final String[] args) throws Exception {
        // 输出信息头
        sender.sendMessage(String.format("%s%s------- 玩家信息 ----------------", ChatColor.AQUA, ChatColor.BOLD));

        // 遍历所有在线玩家
        for (final Player player : PlayersInfo.server.getOnlinePlayers()) {
            // 输出当前玩家的信息
            final Location location = player.getLocation();
            sender.sendMessage(ChatColor.GREEN + String.format("%s: world: %s, x: %f, y: %f, z:%f\n", player.getName(), player.getWorld().getName(), location.getX(), location.getY(), location.getZ()));
        }

        return true;
    }
}
