package org.cat73.performancedebugger.command.commands;

import java.util.Collection;
import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.cat73.performancedebugger.command.CommandHandler;
import org.cat73.performancedebugger.command.CommandInfo;
import org.cat73.performancedebugger.command.ICommand;

/**
 * 帮助类
 *
 * @author cat73
 */
@CommandInfo(name = "help", usage = "[page] [commandName]", description = "打印帮助信息")
public class Help implements ICommand {
    /** 每页输出多少条帮助 */
    private static final int pageCommandCount = 8;

    @Override
    public boolean execute(final CommandSender sender, final Command command, final String commandLabel, final String[] args) throws Exception {
        // 首先来判断是不是有参数 没参数就打印第一页
        if (args.length >= 1) {
            // 判断是不是请求某个已存在命令的帮助
            final ICommand commandExecer = CommandHandler.getCommand(args[0]);
            if (commandExecer != null) {
                // 如果是则打印该命令的帮助信息
                Help.sendCommandHelp(sender, commandExecer);
            } else {
                // 如果不是则视为页码并打印
                int page = 1;

                // 尝试将参数转为整数
                try {
                    page = Integer.parseInt(args[0]);
                } catch (final Exception e) {
                }

                // 根据页码输出帮助
                Help.sendHelpPage(sender, page);
            }
        } else {
            // 如果没有参数则打印第一页帮助
            Help.sendHelpPage(sender, 1);
        }

        return true;
    }

    /**
     * 打印某一页帮助信息
     *
     * @param sender
     * @param page 要打印的页码
     */
    private static void sendHelpPage(final CommandSender sender, int page) {
        // 帮助列表的Set对象
        final Collection<ICommand> commands = CommandHandler.getCommands();
        // 帮助的总量
        final int helpCommandCount = commands.size();
        // 计算总页数
        final int maxPage = helpCommandCount / Help.pageCommandCount + (helpCommandCount % Help.pageCommandCount == 0 ? 0 : 1);
        // 防止超出总数
        page = page > maxPage || page < 1 ? 1 : page;

        sender.sendMessage(String.format("%s%s------- 命令列表 (" + page + "/" + maxPage + ") ----------------", ChatColor.AQUA, ChatColor.BOLD));

        final Iterator<ICommand> it = commands.iterator();
        for (int i = 0; i < (page - 1) * Help.pageCommandCount; i++) {
            it.next();
        }
        for (int i = 0; i < Help.pageCommandCount && it.hasNext(); i++) {
            final ICommand commandExecer = it.next();
            final CommandInfo info = CommandHandler.getCommandInfo(commandExecer);
            sender.sendMessage(ChatColor.GREEN + String.format("%s -- %s", info.name(), info.description()));
        }
    }

    /**
     * 打印某个命令的帮助信息
     *
     * @param sender
     * @param command 命令的执行器
     */
    public static void sendCommandHelp(final CommandSender sender, final ICommand command) {
        final CommandInfo info = CommandHandler.getCommandInfo(command);
        sender.sendMessage(String.format("%s%s------- help %s ----------------", ChatColor.AQUA, ChatColor.BOLD, info.name()));
        sender.sendMessage(ChatColor.GREEN + String.format("/%s %s %s", CommandHandler.BASE_COMMAND, info.name(), info.usage()));
        sender.sendMessage(ChatColor.GREEN + info.description());
        for (final String line : info.help()) {
            if (!line.equals("")) {
                sender.sendMessage(ChatColor.GREEN + line);
            }
        }
    }
}
