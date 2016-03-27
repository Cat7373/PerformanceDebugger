package org.cat73.performancedebugger.command;

import java.util.Collection;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.cat73.performancedebugger.IModule;
import org.cat73.performancedebugger.command.subcommands.DumpChunks;
import org.cat73.performancedebugger.command.subcommands.Help;
import org.cat73.performancedebugger.command.subcommands.PlayersInfo;
import org.cat73.performancedebugger.command.subcommands.TOP;
import org.cat73.performancedebugger.command.subcommands.TPChunk;
import org.cat73.performancedebugger.command.subcommands.TPS;

// TODO 增加 reload
// TODO 增加统计所有玩家周围某种实体数量的功能
// TODO 实现 TabExecutor
/**
 * 命令的执行器
 *
 * @author cat73
 */
public class CommandHandler implements CommandExecutor, IModule {
    /** 基础命令名 */
    public static final String BASE_COMMAND = "performancedebugger";

    /* 存储的子命令列表 */
    private static final HashMap<String, ISubCommand> commandList = new HashMap<String, ISubCommand>();

    /* 子命令的简写缓存 */
    private static final HashMap<String, ISubCommand> aliaseCache = new HashMap<String, ISubCommand>();

    static {
        // 注册所有子命令
        CommandHandler.registerCommand(new Help());
        CommandHandler.registerCommand(new DumpChunks());
        CommandHandler.registerCommand(new PlayersInfo());
        CommandHandler.registerCommand(new TPChunk());
        CommandHandler.registerCommand(new TPS());
        CommandHandler.registerCommand(new TOP());
    }

    /**
     * 注册一个子命令
     *
     * @param command 子命令的执行器
     */
    private static void registerCommand(final ISubCommand command) {
        final SubCommandInfo info = CommandHandler.getCommandInfo(command);
        final String name = info.name().toLowerCase();

        CommandHandler.commandList.put(name, command);
        for (final String aliase : info.aliases()) {
            CommandHandler.aliaseCache.put(aliase.toLowerCase(), command);
        }
    }

    /**
     * 获取子命令列表
     *
     * @return 子命令列表
     */
    public static Collection<ISubCommand> getCommands() {
        return CommandHandler.commandList.values();
    }

    /**
     * 获取一个子命令的信息
     *
     * @param command 子命令的执行器
     * @return 该子命令的信息
     */
    public static SubCommandInfo getCommandInfo(final ISubCommand command) {
        return command.getClass().getAnnotation(SubCommandInfo.class);
    }

    /**
     * 根据名称或简写获取一个子命令的执行器
     *
     * @param nameOrAliase 子命令的名称或者简写
     * @return 子命令的执行器, 如果未找到则返回 null
     */
    public static ISubCommand getCommandByNameOrAliase(final String nameOrAliase) {
        final ISubCommand command = CommandHandler.commandList.get(nameOrAliase);
        return command != null ? command : CommandHandler.aliaseCache.get(nameOrAliase);
    }

    /**
     * 判断命令执行者有没有一个子命令的执行权限
     *
     * @param command 子命令的执行器
     * @param sender 执行者
     * @return 该执行者有无权限执行这条子命令
     */
    public static boolean hasPermission(final ISubCommand command, final CommandSender sender) {
        // 获取子命令的信息
        final SubCommandInfo info = CommandHandler.getCommandInfo(command);

        // 判断有无权限执行这个子命令
        if (info.permission().isEmpty()) {
            return true;
        } else {
            if (sender.hasPermission(info.permission())) {
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public void onEnable(final JavaPlugin javaPlugin) {
        javaPlugin.getCommand(CommandHandler.BASE_COMMAND).setExecutor(this);
    }

    @Override
    public void onDisable(final JavaPlugin javaPlugin) {}

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String commandLabel, String[] args) {
        // 查找被执行的子命令
        switch (command.getName()) {
            case BASE_COMMAND:
                // 如果没有参数则执行帮助
                if (args == null || args.length < 1) {
                    args = new String[] {"help"};
                }

                // 获取目标子命令的执行器
                ISubCommand commandExecer = CommandHandler.getCommandByNameOrAliase(args[0].toLowerCase());

                // 获取失败则执行帮助
                if (commandExecer == null) {
                    commandExecer = CommandHandler.getCommandByNameOrAliase("help");
                }

                // 判断有无权限执行这个子命令
                if (!CommandHandler.hasPermission(commandExecer, sender)) {
                    final SubCommandInfo info = CommandHandler.getCommandInfo(commandExecer);
                    sender.sendMessage(String.format("%s%s你需要 %s 权限才能执行 %s 命令.", ChatColor.RED, ChatColor.BOLD, info.permission(), info.name()));
                    return true;
                }

                // 修剪参数 (删除子命令名)
                final String[] tmp = new String[args.length - 1];
                for (int i = 1; i < args.length; i++) {
                    tmp[i - 1] = args[i];
                }

                try {
                    // 执行子命令
                    if (!commandExecer.handle(sender, tmp)) {
                        // 如果返回 false 则打印该子命令的帮助
                        Help.sendCommandHelp(sender, commandExecer);
                    }
                } catch (final Exception e) {
                    // 如果出现任何未捕获的异常则打印提示
                    sender.sendMessage(String.format("%s%s执行命令的过程中出现了一个未处理的错误.", ChatColor.RED, ChatColor.BOLD));
                    e.printStackTrace();
                }

                return true;
            default:
                return false;
        }
    }
}
