package com.softmegatron.shared.meta.logging.model;

import java.io.Serializable;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.event.Level;

/**
 * 日志事件模型
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @date 2020/05/07 14:05
 * @since 1.0.0
 */
public class LogEvent implements Serializable {

    private static final long serialVersionUID = 9223372036854775807L;    
    /**
     * 日志记录器
     */
    private Logger logger;
    /**
     * 日志等级
     */
    private Level level;
    /**
     * 日志记录器名称
     */
    private String loggerName;
    /**
     * 日志内容
     */
    private String message;
    /**
     * 异常
     */
    private Throwable throwable;
    /**
     * MDC上下文
     */
    private Map<String, String> mdcContext;
    /**
     * 日志记录时间
     */
    private Long timestamp;

    public LogEvent() {  
    }

    public LogEvent(Logger logger, Level level, String message, Throwable throwable, Map<String, String> mdcContext) { 
        this.logger = logger;
        this.level = level;
        this.loggerName = logger == null ? null : logger.getName();
        this.message = message;
        this.throwable = throwable;
        this.mdcContext = mdcContext;
        this.timestamp = System.currentTimeMillis();
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public String getLoggerName() {
        return loggerName;
    }

    public void setLoggerName(String loggerName) {
        this.loggerName = loggerName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public Map<String, String> getMdcContext() {
        return mdcContext;
    }

    public void setMdcContext(Map<String, String> mdcContext) {
        this.mdcContext = mdcContext;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
