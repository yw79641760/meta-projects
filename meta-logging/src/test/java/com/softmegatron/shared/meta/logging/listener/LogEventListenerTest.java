package com.softmegatron.shared.meta.logging.listener;

import com.softmegatron.shared.meta.logging.model.LogEvent;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * LogEventListener 测试类
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 2026/02/12
 */
public class LogEventListenerTest {

    private static final Logger TEST_LOGGER = LoggerFactory.getLogger("LogEventListenerTest");

    @Test
    public void testDefaultShouldHandle() {
        LogEventListener listener = new TestLogEventListener();
        
        // 默认实现应该处理所有级别的日志
        assertTrue("默认shouldHandle应该返回true", listener.shouldHandle(Level.TRACE));
        assertTrue("默认shouldHandle应该返回true", listener.shouldHandle(Level.DEBUG));
        assertTrue("默认shouldHandle应该返回true", listener.shouldHandle(Level.INFO));
        assertTrue("默认shouldHandle应该返回true", listener.shouldHandle(Level.WARN));
        assertTrue("默认shouldHandle应该返回true", listener.shouldHandle(Level.ERROR));
    }

    @Test
    public void testOnErrorDefaultMethod() {
        TestLogEventListener listener = new TestLogEventListener();
        
        // 测试null事件
        listener.onError(null);
        assertEquals("onError处理null事件不应该添加事件", 0, listener.getReceivedEvents().size());
        
        // 测试非ERROR级别的事件
        LogEvent warnEvent = createLogEvent(Level.WARN, "Warning message");
        listener.onError(warnEvent);
        assertEquals("onError不应该处理WARN级别的事件", 0, listener.getReceivedEvents().size());
        
        // 测试ERROR级别的事件
        LogEvent errorEvent = createLogEvent(Level.ERROR, "Error message");
        listener.onError(errorEvent);
        assertEquals("onError应该处理ERROR级别的事件", 1, listener.getReceivedEvents().size());
        assertSame("应该收到相同的事件对象", errorEvent, listener.getReceivedEvents().get(0));
    }

    @Test
    public void testOnWarnDefaultMethod() {
        TestLogEventListener listener = new TestLogEventListener();
        
        // 测试null事件
        listener.onWarn(null);
        assertEquals("onWarn处理null事件不应该添加事件", 0, listener.getReceivedEvents().size());
        
        // 测试非WARN级别的事件
        LogEvent infoEvent = createLogEvent(Level.INFO, "Info message");
        listener.onWarn(infoEvent);
        assertEquals("onWarn不应该处理INFO级别的事件", 0, listener.getReceivedEvents().size());
        
        // 测试WARN级别的事件
        LogEvent warnEvent = createLogEvent(Level.WARN, "Warning message");
        listener.onWarn(warnEvent);
        assertEquals("onWarn应该处理WARN级别的事件", 1, listener.getReceivedEvents().size());
        assertSame("应该收到相同的事件对象", warnEvent, listener.getReceivedEvents().get(0));
    }

    @Test
    public void testOnInfoDefaultMethod() {
        TestLogEventListener listener = new TestLogEventListener();
        
        // 测试null事件
        listener.onInfo(null);
        assertEquals("onInfo处理null事件不应该添加事件", 0, listener.getReceivedEvents().size());
        
        // 测试非INFO级别的事件
        LogEvent debugEvent = createLogEvent(Level.DEBUG, "Debug message");
        listener.onInfo(debugEvent);
        assertEquals("onInfo不应该处理DEBUG级别的事件", 0, listener.getReceivedEvents().size());
        
        // 测试INFO级别的事件
        LogEvent infoEvent = createLogEvent(Level.INFO, "Info message");
        listener.onInfo(infoEvent);
        assertEquals("onInfo应该处理INFO级别的事件", 1, listener.getReceivedEvents().size());
        assertSame("应该收到相同的事件对象", infoEvent, listener.getReceivedEvents().get(0));
    }

    @Test
    public void testCustomShouldHandleImplementation() {
        // 创建自定义shouldHandle实现的监听器
        LogEventListener listener = new LogEventListener() {
            @Override
            public void onEvent(LogEvent logEvent) {
                // 空实现
            }
            
            @Override
            public void onEvent(Level level, String loggerName, String message, Throwable throwable) {
                // 空实现
            }
            
            @Override
            public boolean shouldHandle(Level level) {
                // 只处理ERROR和WARN级别
                return Level.ERROR.equals(level) || Level.WARN.equals(level);
            }
        };
        
        assertTrue("应该处理ERROR级别", listener.shouldHandle(Level.ERROR));
        assertTrue("应该处理WARN级别", listener.shouldHandle(Level.WARN));
        assertFalse("不应该处理INFO级别", listener.shouldHandle(Level.INFO));
        assertFalse("不应该处理DEBUG级别", listener.shouldHandle(Level.DEBUG));
        assertFalse("不应该处理TRACE级别", listener.shouldHandle(Level.TRACE));
    }

    @Test
    public void testMultipleLevelHandling() {
        MultiLevelTestListener listener = new MultiLevelTestListener();
        
        LogEvent traceEvent = createLogEvent(Level.TRACE, "Trace message");
        LogEvent debugEvent = createLogEvent(Level.DEBUG, "Debug message");
        LogEvent infoEvent = createLogEvent(Level.INFO, "Info message");
        LogEvent warnEvent = createLogEvent(Level.WARN, "Warn message");
        LogEvent errorEvent = createLogEvent(Level.ERROR, "Error message");
        
        listener.onEvent(traceEvent);
        listener.onEvent(debugEvent);
        listener.onEvent(infoEvent);
        listener.onEvent(warnEvent);
        listener.onEvent(errorEvent);
        
        assertEquals("应该收到5个事件", 5, listener.getAllEvents().size());
        assertEquals("应该收到1个TRACE事件", 1, listener.getTraceEvents().size());
        assertEquals("应该收到1个DEBUG事件", 1, listener.getDebugEvents().size());
        assertEquals("应该收到1个INFO事件", 1, listener.getInfoEvents().size());
        assertEquals("应该收到1个WARN事件", 1, listener.getWarnEvents().size());
        assertEquals("应该收到1个ERROR事件", 1, listener.getErrorEvents().size());
    }


    private LogEvent createLogEvent(Level level, String message) {
        return new LogEvent(TEST_LOGGER, level, message, null, null);
    }

    /**
     * 测试用的监听器实现
     */
    private static class TestLogEventListener implements LogEventListener {
        private final List<LogEvent> receivedEvents = new ArrayList<>();
        
        @Override
        public void onEvent(LogEvent logEvent) {
            receivedEvents.add(logEvent);
        }
        
        @Override
        public void onEvent(Level level, String loggerName, String message, Throwable throwable) {
            // 不实现，只测试onEvent(LogEvent)方法
        }
        
        public List<LogEvent> getReceivedEvents() {
            return new ArrayList<>(receivedEvents);
        }
    }

    /**
     * 多级别处理测试监听器
     */
    private static class MultiLevelTestListener implements LogEventListener {
        private final List<LogEvent> allEvents = new ArrayList<>();
        private final List<LogEvent> traceEvents = new ArrayList<>();
        private final List<LogEvent> debugEvents = new ArrayList<>();
        private final List<LogEvent> infoEvents = new ArrayList<>();
        private final List<LogEvent> warnEvents = new ArrayList<>();
        private final List<LogEvent> errorEvents = new ArrayList<>();
        
        @Override
        public void onEvent(LogEvent logEvent) {
            allEvents.add(logEvent);
            
            switch (logEvent.getLevel()) {
                case TRACE:
                    traceEvents.add(logEvent);
                    break;
                case DEBUG:
                    debugEvents.add(logEvent);
                    break;
                case INFO:
                    infoEvents.add(logEvent);
                    break;
                case WARN:
                    warnEvents.add(logEvent);
                    break;
                case ERROR:
                    errorEvents.add(logEvent);
                    break;
            }
        }
        
        @Override
        public void onEvent(Level level, String loggerName, String message, Throwable throwable) {
            // 不实现
        }
        
        public List<LogEvent> getAllEvents() {
            return new ArrayList<>(allEvents);
        }
        
        public List<LogEvent> getTraceEvents() {
            return new ArrayList<>(traceEvents);
        }
        
        public List<LogEvent> getDebugEvents() {
            return new ArrayList<>(debugEvents);
        }
        
        public List<LogEvent> getInfoEvents() {
            return new ArrayList<>(infoEvents);
        }
        
        public List<LogEvent> getWarnEvents() {
            return new ArrayList<>(warnEvents);
        }
        
        public List<LogEvent> getErrorEvents() {
            return new ArrayList<>(errorEvents);
        }
    }
}