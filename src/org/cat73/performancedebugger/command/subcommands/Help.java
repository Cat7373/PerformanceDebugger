package org.cat73.performancedebugger.command.subcommands;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.cat73.performancedebugger.command.CommandHandler;
import org.cat73.performancedebugger.command.ISubCommand;
import org.cat73.performancedebugger.command.SubCommandInfo;

/**
 * 帮助类
 *
 * @author cat73
 */
@SubCommandInfo(name = "help", permission = "performancedebugger.help", usage = "[page] [commandName]", description = "打印帮助信息", aliases = "h")
public class Help implements ISubCommand {
    /** 每页输出多少条帮助 */
    private static final int pageCommandCount = 8;

    @Override
    public boolean handle(final CommandSender sender, final String[] args) throws Exception {
        // 首先来判断是不是有参数 没参数就打印第一页
        if (args.length >= 1) {
            // 判断是不是请求某个已存在命令的帮助
            final ISubCommand commandExecer = CommandHandler.getCommandByNameOrAliase(args[0]);
            if (commandExecer != null && CommandHandler.hasPermission(commandExecer, sender)) {
                // 如果是且有权限执行则打印该命令的帮助信息
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
        // 所有子命令
        final Collection<ISubCommand> commands = CommandHandler.getCommands();

        // 获取有权执行的子命令
        final Set<ISubCommand> hasPermissionCommands = new HashSet<>();
        Iterator<ISubCommand> it = commands.iterator();
        while (it.hasNext()) {
            final ISubCommand command = it.next();
            if (CommandHandler.hasPermission(command, sender)) {
                hasPermissionCommands.add(command);
            }
        }

        // 命令的总量
        final int helpCommandCount = hasPermissionCommands.size();
        // 计算总页数
        final int maxPage = helpCommandCount / Help.pageCommandCount + (helpCommandCount % Help.pageCommandCount == 0 ? 0 : 1);
        // 防止超出总数
        page = page > maxPage || page < 1 ? 1 : page;

        sender.sendMessage(String.format("%s%s------- 命令列表 (" + page + "/" + maxPage + ") ----------------", ChatColor.AQUA, ChatColor.BOLD));

        // 获取命令列表的迭代器
        it = hasPermissionCommands.iterator();
        // 跳过前几页的内容
        for (int i = 0; i < (page - 1) * Help.pageCommandCount; i++) {
            it.next();
        }
        // 输出目标页的内容
        for (int i = 0; i < Help.pageCommandCount && it.hasNext(); i++) {
            final ISubCommand commandExecer = it.next();
            final SubCommandInfo info = CommandHandler.getCommandInfo(commandExecer);
            sender.sendMessage(ChatColor.GREEN + String.format("%s -- %s", info.name(), info.description()));
        }
    }

    /**
     * 打印某个命令的帮助信息
     *
     * @param sender
     * @param command 命令的执行器
     */
    public static void sendCommandHelp(final CommandSender sender, final ISubCommand command) {
        final SubCommandInfo info = CommandHandler.getCommandInfo(command);
        sender.sendMessage(String.format("%s%s------- help %s ----------------", ChatColor.AQUA, ChatColor.BOLD, info.name()));
        // 命令的用法 / 参数
        sender.sendMessage(ChatColor.GREEN + String.format("/%s %s %s", CommandHandler.BASE_COMMAND, info.name(), info.usage()));
        // 命令的说明
        sender.sendMessage(ChatColor.GREEN + info.description());
        // 命令的帮助信息
        for (final String line : info.help()) {
            if (!line.isEmpty()) {
                sender.sendMessage(ChatColor.GREEN + line);
            }
        }

        // 命令的简写列表
        final StringBuilder aliases = new StringBuilder("aliases: ");
        int aliaseCount = 0;
        for (final String aliase : info.aliases()) {
            if (!aliase.isEmpty()) {
                aliaseCount++;
                aliases.append(aliase);
                aliases.append(" ");
            }
        }
        if (aliaseCount > 0) {
            sender.sendMessage(ChatColor.GREEN + aliases.toString());
        }
    }
}
