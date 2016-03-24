package org.cat73.performancedebugger.command;

import java.util.Collection;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.cat73.performancedebugger.IModule;
import org.cat73.performancedebugger.command.commands.DumpChunks;
import org.cat73.performancedebugger.command.commands.Help;
import org.cat73.performancedebugger.command.commands.PlayersInfo;
import org.cat73.performancedebugger.command.commands.TOP;
import org.cat73.performancedebugger.command.commands.TPChunk;
import org.cat73.performancedebugger.command.commands.TPS;

// TODO 增加 reload
// TODO 增加统计所有玩家周围某种实体数量的功能
/**
 * 命令的执行器
 *
 * @author cat73
 */
public class CommandHandler implements CommandExecutor, IModule {
    /** 基础命令名 */
    public static final String BASE_COMMAND = "performancedebugger";

    /* 存储的命令列表 */
    private static final HashMap<String, ICommand> commandList = new HashMap<String, ICommand>();

    /* 命令的简写缓存 */
    private static final HashMap<String, ICommand> aliaseCache = new HashMap<String, ICommand>();

    static {
        // 注册所有命令
        CommandHandler.registerCommand(new Help());
        CommandHandler.registerCommand(new DumpChunks());
        CommandHandler.registerCommand(new PlayersInfo());
        CommandHandler.registerCommand(new TPChunk());
        CommandHandler.registerCommand(new TPS());
        CommandHandler.registerCommand(new TOP());
    }

    /**
     * 注册一个命令
     *
     * @param command 命令的执行器
     */
    private static void registerCommand(final ICommand command) {
        final CommandInfo info = CommandHandler.getCommandInfo(command);
        final String name = info.name().toLowerCase();

        CommandHandler.commandList.put(name, command);
        for (final String aliase : info.aliases()) {
            CommandHandler.aliaseCache.put(aliase.toLowerCase(), command);
        }
    }

    /**
     * 获取命令列表
     *
     * @return 命令列表
     */
    public static Collection<ICommand> getCommands() {
        return CommandHandler.commandList.values();
    }

    /**
     * 获取一个命令的信息
     *
     * @param command 命令的执行器
     * @return 该命令的信息
     */
    public static CommandInfo getCommandInfo(final ICommand command) {
        return command.getClass().getAnnotation(CommandInfo.class);
    }

    /**
     * 根据名称或简写获取一个命令的执行器
     *
     * @param nameOrAliase 命令的名称或者简写
     * @return 命令的执行器, 如果未找到则返回 null
     */
    public static ICommand getCommandByNameOrAliase(final String nameOrAliase) {
        final ICommand command = CommandHandler.commandList.get(nameOrAliase);
        return command != null ? command : CommandHandler.aliaseCache.get(nameOrAliase);
    }

    @Override
    public void onEnable(final JavaPlugin javaPlugin) {
        javaPlugin.getCommand(CommandHandler.BASE_COMMAND).setExecutor(this);
    }

    @Override
    public void onDisable(final JavaPlugin javaPlugin) {}

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String commandLabel, String[] args) {
        // 查找被执行的命令
        switch (command.getName()) {
            case BASE_COMMAND:
                // 如果没有参数则执行帮助
                if (args.length == 0) {
                    args = new String[] {"help"};
                }

                // 获取目标子命令的执行器
                ICommand commandExecer = CommandHandler.getCommandByNameOrAliase(args[0].toLowerCase());

                // 获取失败则执行帮助
                if (commandExecer == null) {
                    commandExecer = CommandHandler.getCommandByNameOrAliase("help");
                }

                // 修剪参数 (删除子命令名)
                final String[] tmp = new String[args.length - 1];
                for (int i = 1; i < args.length; i++) {
                    tmp[i - 1] = args[i];
                }

                try {
                    // 执行命令
                    if (!commandExecer.execute(sender, command, commandLabel, tmp)) {
                        // 如果返回 false 则打印该命令的帮助
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
