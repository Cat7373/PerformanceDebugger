package org.cat73.performancedebugger.command.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.cat73.performancedebugger.command.SubCommandInfo;
import org.cat73.performancedebugger.command.ISubCommand;
import org.cat73.performancedebugger.task.tasks.CalculationTPSTask;

/**
 * 获取最后一次统计到的 TPS
 *
 * @author cat73
 */
@SubCommandInfo(name = "TPS", description = "获取当前的 TPS", help = {"每 20 tick 统计一次最近 20 个 tick 的平均 TPS", "本命令返回上五次统计的平均值"})
public class TPS implements ISubCommand {
    @Override
    public boolean handle(final CommandSender sender, final String[] args) throws Exception {
        sender.sendMessage(String.format("%sLast TPS: %f", ChatColor.GREEN, CalculationTPSTask.getLastTPS()));
        return true;
    }
}
