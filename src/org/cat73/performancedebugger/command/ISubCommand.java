package org.cat73.performancedebugger.command;

import org.bukkit.command.CommandSender;

/**
 * 子命令执行器接口
 *
 * @author Cat73
 */
public interface ISubCommand {
    /**
     * 执行一条子命令
     *
     * @param sender 命令的执行者
     * @param args 修剪后的 args (不包含子命令名)
     * @return 成功返回 true，如果返回 false 则会打印该子命令的帮助信息
     * @throws Exception 如果抛出任何异常，则会提示用户执行命令时出现了未处理的错误
     */
    boolean handle(CommandSender sender, String[] args) throws Exception;
}
