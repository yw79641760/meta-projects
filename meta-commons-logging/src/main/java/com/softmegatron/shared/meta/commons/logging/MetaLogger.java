package com.softmegatron.shared.meta.commons.logging;

import com.softmegatron.shared.meta.commons.logging.listener.LogEventManager;
import com.softmegatron.shared.meta.commons.logging.utils.LoggingUtils;

import org.slf4j.Logger;
import org.slf4j.event.Level;

/**
 * 日志工具类
 * 提供统一的日志接口，集成MDC上下文支持
 * 
 * <p>使用示例:</p>
 * <pre>
 * // 基本使用
 * MetaLogger.info(logger, "User {} logged in", userId);
 * 
 * // 结合MDC使用
 * try (Closeable ignored = MdcUtils.putCloseable("requestId", requestId)) {
 *     MetaLogger.info(logger, "Processing request");
 *     // 所有日志都会自动包含requestId上下文
 * }
 * </pre>
 * 
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0
 * @date 2026/02/12 01:41
 * @since 1.0
 * @see com.softmegatron.shared.meta.commons.logging.utils.MdcUtils
 */
public final class MetaLogger {

    /**
     * 输出DEBUG级别日志
     *
     * @param logger  SLF4J Logger实例
     * @param message 日志消息模板
     * @param args    参数数组
     */
    public static void debug(Logger logger, String message, Object... args) {
        logger.debug(LoggingUtils.build(message, args));
    }

    /**
     * 当DEBUG级别日志开启时输出DEBUG级别日志
     * @param logger
     * @param message
     * @param args
     */
    public static void debugWhenEnabled(Logger logger, String message, Object... args) {
        if (LoggingUtils.isDebugEnabled(logger)) {
            logger.debug(LoggingUtils.build(message, args));
        }
    }

    /**
     * 输出TRACE级别日志
     *
     * @param logger  SLF4J Logger实例
     * @param message 日志消息模板
     * @param args    参数数组
     */
    public static void trace(Logger logger, String message, Object... args) {
        logger.trace(LoggingUtils.build(message, args));
    }

    /**
     * 输出INFO级别日志
     *
     * @param logger  SLF4J Logger实例
     * @param message 日志消息模板
     * @param args    参数数组
     */
    public static void info(Logger logger, String message, Object... args) {
        String formatted = LoggingUtils.build(message, args);
        logger.info(formatted);
        LogEventManager.fireEvent(logger, Level.INFO, logger.getName(), formatted, null);
    }

    /**
     * 输出WARN级别日志
     *
     * @param logger  SLF4J Logger实例
     * @param message 日志消息模板
     * @param args    参数数组
     */
    public static void warn(Logger logger, String message, Object... args) {
        String formatted = LoggingUtils.build(message, args);
        logger.warn(formatted);
        LogEventManager.fireEvent(logger, Level.WARN, logger.getName(), formatted, null);
    }

    /**
     * 输出ERROR级别日志
     *
     * @param logger  SLF4J Logger实例
     * @param message 日志消息模板
     * @param args    参数数组
     */
    public static void error(Logger logger, String message, Object... args) {
        String formatted = LoggingUtils.build(message, args);
        logger.error(formatted);
        LogEventManager.fireEvent(logger, Level.ERROR, logger.getName(), formatted, null);
    }

    /**
     * 输出ERROR级别日志并附带异常信息
     *
     * @param logger    SLF4J Logger实例
     * @param throwable 异常对象
     * @param message   日志消息模板
     * @param args      参数数组
     */
    public static void error(Logger logger, Throwable throwable, String message, Object... args) {
        String formatted = LoggingUtils.build(message, args);
        logger.error(formatted, throwable);
        LogEventManager.fireEvent(logger, Level.ERROR, logger.getName(), formatted, throwable);
    }
}