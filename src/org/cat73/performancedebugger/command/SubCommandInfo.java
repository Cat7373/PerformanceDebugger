package org.cat73.performancedebugger.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 子命令的信息
 *
 * @author Cat73
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface SubCommandInfo {
    /** 子命令的名称 */
    String name();

    /** 子命令的使用方法 */
    String usage() default "";

    /** 子命令的说明 */
    String description() default "";

    /** 子命令的帮助信息 */
    String[] help() default "";

    /** 子命令的简写列表 */
    String[] aliases() default "";
}
