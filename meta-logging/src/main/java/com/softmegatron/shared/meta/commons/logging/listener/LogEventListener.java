package com.softmegatron.shared.meta.commons.logging.listener;

import com.softmegatron.shared.meta.commons.logging.model.LogEvent;

import org.slf4j.event.Level;

/**
 * 日志事件监听器
 * 
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @date 2026/02/12 15:33
 * @version 1.0.0
 * @since 1.0.0
 */
public interface LogEventListener {

    /**
     * 日志事件处理
     * 
     * @param logEvent 日志事件
     */
    void onEvent(LogEvent logEvent);

    /**
     * 日志事件处理
     * 
     * @param level 日志级别
     * @param loggerName 日志记录器名称
     * @param message 日志信息
     */
    void onEvent(Level level, String loggerName, String message, Throwable throwable);
    
    /**
     * 错误日志处理
     * 
     * @param loggerName 日志记录器名称
     * @param message 日志信息
     * @param throwable 异常
     */
    default void onError(LogEvent event) {
        if (event != null && Level.ERROR.equals(event.getLevel())) {
            onEvent(event);
        }
    }

    /**
     * 警告日志处理
     * 
     * @param loggerName 日志记录器名称
     * @param message 日志信息
     * @param throwable 异常
     */
    default void onWarn(LogEvent event) {
        if (event != null && Level.WARN.equals(event.getLevel())) {
            onEvent(event);
        }
    }

    /**
     * 信息日志处理
     * 
     * @param loggerName 日志记录器名称
     * @param message 日志信息
     */
    default void onInfo(LogEvent event) {
        if (event != null && Level.INFO.equals(event.getLevel())) {
            onEvent(event);
        }
    }

    /**
     * 是否处理指定级别的日志
     * 
     * @param level 日志级别
     * @return 是否处理
     */
    default boolean shouldHandle(Level level) {
        return true;
    }
}
