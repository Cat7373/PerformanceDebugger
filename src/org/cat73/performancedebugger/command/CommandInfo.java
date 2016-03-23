package org.cat73.performancedebugger.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 命令的信息
 *
 * @author Cat73
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface CommandInfo {
    /** 命令的名称 */
    String name();

    /** 命令的使用方法 */
    String usage() default "";

    /** 命令的帮助信息 */
    String help() default "";
}
