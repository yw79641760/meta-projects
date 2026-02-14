package com.softmegatron.shared.meta.logging.listener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.softmegatron.shared.meta.logging.model.LogEvent;
import com.softmegatron.shared.meta.logging.utils.MdcUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

/**
 * 日志事件管理器
 * @description
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @date 2026/02/12 15:37
 * @since 1.0.0
 */
public class LogEventManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogEventManager.class);

    private static final List<LogEventListener> LOG_EVENT_LISTENERS = Collections.synchronizedList(new ArrayList<>());

    /**
     * 注册监听器
     * @param listener
     */
    public static void register(LogEventListener listener) {
        synchronized (LOG_EVENT_LISTENERS) {
            if (LOG_EVENT_LISTENERS.contains(listener)) {
                return;
            }
            LOG_EVENT_LISTENERS.add(listener);
        }
    }

    /**
     * 注销监听器
     * @param listener
     */
    public static void unregister(LogEventListener listener) {
        LOG_EVENT_LISTENERS.remove(listener);
    }

    /**
     * 触发事件
     * @param level
     * @param loggerName
     * @param message
     * @param throwable
     */
    public static void fireEvent(Logger logger, Level level, String loggerName, String message, Throwable throwable) {

        if (LOG_EVENT_LISTENERS == null || LOG_EVENT_LISTENERS.isEmpty()) {
            return;
        }

        LogEvent event = new LogEvent(logger, level, message, throwable, MdcUtils.getContextMap());

        List<LogEventListener> snapshot;
        synchronized (LOG_EVENT_LISTENERS) {
            snapshot = new ArrayList<>(LOG_EVENT_LISTENERS);
        }

        for (LogEventListener listener : snapshot) {
            try {
                listener.onEvent(event);
            } catch (Exception e) {
                LOGGER.error("Failed to fire event. [listener={}]", listener, e);
            }
        }
    }

    /**
     * 获取监听器数量
     * @return
     */
    public static int getListenerCount() {
        return LOG_EVENT_LISTENERS.size();
    }

    /**
     * 清理所有监听器
     */
    public static void cleanup() {
        LOG_EVENT_LISTENERS.clear();
    }
}
