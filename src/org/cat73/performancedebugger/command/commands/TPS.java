package org.cat73.performancedebugger.command.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.cat73.performancedebugger.command.CommandInfo;
import org.cat73.performancedebugger.command.ICommand;
import org.cat73.performancedebugger.task.tasks.CalculationTPSTask;

/**
 * 获取最后一次统计到的 TPS
 *
 * @author cat73
 */
@CommandInfo(name = "TPS", description = "获取最后一次统计到的 TPS", help = {"每 20 tick 统计一次最近 20 个 tick 的平均 TPS", "本命令返回上五次统计的平均值"})
public class TPS implements ICommand {
    @Override
    public boolean execute(final CommandSender sender, final Command command, final String commandLabel, final String[] args) throws Exception {
        sender.sendMessage(String.format("%sLast TPS: %f", ChatColor.GREEN, CalculationTPSTask.getLastTPS()));
        return true;
    }
}
