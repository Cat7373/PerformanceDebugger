package org.cat73.performancedebugger.command.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.cat73.performancedebugger.command.CommandInfo;
import org.cat73.performancedebugger.command.ICommand;

/**
 * 将玩家 TP 到一个 Chunk 的中心，方便找到问题 Chunk 后前往查看
 *
 * @author cat73
 */
@CommandInfo(name = "TPChunk", usage = "<x> <z> [world]", description = "TP 到目标 Chunk 的中心", aliases = "tp")
public class TPChunk implements ICommand {
    /** Bukkit 的 Server 接口 */
    private final static Server server = Bukkit.getServer();

    @Override
    public boolean execute(final CommandSender sender, final Command command, final String commandLabel, final String[] args) throws Exception {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(ChatColor.RED + "该命令只能由玩家执行!");
            return true;
        } else if (args.length < 2) {
            return false;
        }

        // 获取目标玩家
        final Player player = TPChunk.server.getPlayer(sender.getName());

        // 获取目标世界
        World world = null;
        if (args.length > 2) {
            final String worldName = args[2];
            world = TPChunk.server.getWorld(worldName);
        }
        world = world != null ? world : player.getWorld();

        // 计算目标坐标
        int posX = Integer.parseInt(args[0]);
        int posZ = Integer.parseInt(args[1]);
        posX = (posX << 4) + 8;
        posZ = (posZ << 4) + 8;
        final int posY = world.getHighestBlockAt(posX, posZ).getY();

        // TP 玩家
        player.teleport(new Location(world, posX, posY, posZ));

        return true;
    }
}
