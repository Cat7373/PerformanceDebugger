package org.cat73.performancedebugger.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * 命令执行器接口
 *
 * @author Cat73
 */
public interface ICommand {
    /**
     * 执行一条命令
     *
     * @param args 修剪后的 args (不包含子命令名)
     * @return 成功返回 true，如果返回 false 则会打印该命令的帮助信息
     * @throws Exception 如果抛出任何异常，则会提示用户执行命令时出现了未处理的错误
     */
    boolean execute(CommandSender sender, Command command, String commandLabel, String[] args) throws Exception;
}
