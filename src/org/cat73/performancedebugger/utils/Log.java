package org.cat73.performancedebugger.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 日志类
 *
 * @author Cat73
 */
public class Log {
    /* 日志输出流 */
    private static Logger logger;

    /**
     * 设置 logger
     *
     * @param logger
     */
    public static void setLogger(final Logger logger) {
        Log.logger = logger;
    }

    /**
     * 向日志输出流输出日志
     *
     * @param level 日志的级别
     * @param format 要输出的信息格式
     * @param args 格式化时使用的数据列表
     */
    private static void log(final Level level, final String format, final Object... args) {
        Log.logger.log(level, String.format(format, args));
    }

    /**
     * 向日志输出流输出信息
     *
     * @param format 要输出的信息格式
     * @param args 格式化时使用的数据列表
     */
    public static void info(final String format, final Object... args) {
        Log.log(Level.INFO, "[INFO]" + format, args);
    }

    /**
     * 向日志输出流输出警告
     *
     * @param format 要输出的信息格式
     * @param args 格式化时使用的数据列表
     */
    public static void warn(final String format, final Object... args) {
        Log.warning(format, args);
    }

    /**
     * 向日志输出流输出警告
     *
     * @param format 要输出的信息格式
     * @param args 格式化时使用的数据列表
     */
    public static void warning(final String format, final Object... args) {
        Log.log(Level.WARNING, "[WARN]" + format, args);
    }

    /**
     * 向日志输出流输出错误
     *
     * @param format 要输出的信息格式
     * @param args 格式化时使用的数据列表
     */
    public static void error(final String format, final Object... args) {
        Log.log(Level.SEVERE, "[ERROR]" + format, args);
    }

    /**
     * 向玩家输出格式化的调试信息
     *
     * @param format 要输出的信息格式
     * @param args 格式化时使用的数据列表
     */
    public static void debug(final String format, final Object... args) {
        Log.log(Level.INFO, "[DEBUG] " + format, args);
    }

    /**
     * 向日志输出调试信息
     *
     * @param objs 要输出的数据列表
     */
    public static void debugs(final Object... objs) {
        String message = "";
        for (final Object obj : objs) {
            message += obj + ", ";
        }
        if (!message.isEmpty()) {
            message = message.substring(0, message.length() - 2);
        }

        Log.log(Level.INFO, "[DEBUG] " + message);
    }
}
