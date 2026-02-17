package com.softmegatron.shared.meta.logging;

import com.softmegatron.shared.meta.logging.listener.LogEventListener;
import com.softmegatron.shared.meta.logging.listener.LogEventManager;
import com.softmegatron.shared.meta.logging.model.LogEvent;
import com.softmegatron.shared.meta.logging.utils.MdcUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * MetaLogger 测试类
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @since 1.0.0
 */
public class MetaLoggerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MetaLoggerTest.class);

    private TestLogEventListener testListener;
    private boolean mdcSupported;

    @Before
    public void setUp() {
        LogEventManager.cleanup();
        MdcUtils.clear();
        testListener = new TestLogEventListener();
        LogEventManager.register(testListener);
        mdcSupported = checkMdcSupport();
    }

    @After
    public void tearDown() {
        LogEventManager.cleanup();
        MdcUtils.clear();
    }

    private boolean checkMdcSupport() {
        try {
            MdcUtils.put("test", "value");
            String result = MdcUtils.get("test");
            MdcUtils.remove("test");
            return "value".equals(result);
        } catch (Exception e) {
            return false;
        }
    }

    @Test
    public void testDebug() {
        MetaLogger.debug(LOGGER, "Debug message");
        // debug 级别不触发事件
        assertTrue(testListener.getReceivedEvents().isEmpty());
    }

    @Test
    public void testDebugWithArgs() {
        MetaLogger.debug(LOGGER, "Debug message: {}", "test");
        assertTrue(testListener.getReceivedEvents().isEmpty());
    }

    @Test
    public void testDebugWithMultipleArgs() {
        MetaLogger.debug(LOGGER, "Debug: {} {}", "arg1", "arg2");
        assertTrue(testListener.getReceivedEvents().isEmpty());
    }

    @Test
    public void testDebugWithNullArgs() {
        MetaLogger.debug(LOGGER, "Debug message", (Object[]) null);
        assertTrue(testListener.getReceivedEvents().isEmpty());
    }

    @Test
    public void testDebugWhenEnabled() {
        MetaLogger.debugWhenEnabled(LOGGER, "Debug when enabled: {}", "test");
        assertTrue(testListener.getReceivedEvents().isEmpty());
    }

    @Test
    public void testDebugWhenEnabledWithNullArgs() {
        MetaLogger.debugWhenEnabled(LOGGER, "Debug message", (Object[]) null);
        assertTrue(testListener.getReceivedEvents().isEmpty());
    }

    @Test
    public void testTrace() {
        MetaLogger.trace(LOGGER, "Trace message");
        // trace 级别不触发事件
        assertTrue(testListener.getReceivedEvents().isEmpty());
    }

    @Test
    public void testTraceWithArgs() {
        MetaLogger.trace(LOGGER, "Trace message: {}", "test");
        assertTrue(testListener.getReceivedEvents().isEmpty());
    }

    @Test
    public void testTraceWithNullArgs() {
        MetaLogger.trace(LOGGER, "Trace message", (Object[]) null);
        assertTrue(testListener.getReceivedEvents().isEmpty());
    }

    @Test
    public void testInfo() {
        MetaLogger.info(LOGGER, "Info message");

        assertEquals(1, testListener.getReceivedEvents().size());
        LogEvent event = testListener.getReceivedEvents().get(0);
        assertEquals(Level.INFO, event.getLevel());
        assertEquals("Info message", event.getMessage());
    }

    @Test
    public void testInfoWithArgs() {
        MetaLogger.info(LOGGER, "Info message: {}", "test");

        assertEquals(1, testListener.getReceivedEvents().size());
        LogEvent event = testListener.getReceivedEvents().get(0);
        assertEquals(Level.INFO, event.getLevel());
        assertEquals("Info message: test", event.getMessage());
    }

    @Test
    public void testInfoWithMultipleArgs() {
        MetaLogger.info(LOGGER, "User {} logged in at {}", "Alice", "2026-02-18");

        assertEquals(1, testListener.getReceivedEvents().size());
        LogEvent event = testListener.getReceivedEvents().get(0);
        assertEquals("User Alice logged in at 2026-02-18", event.getMessage());
    }

    @Test
    public void testInfoWithNullArgs() {
        MetaLogger.info(LOGGER, "Info message", (Object[]) null);

        assertEquals(1, testListener.getReceivedEvents().size());
        assertEquals("Info message", testListener.getReceivedEvents().get(0).getMessage());
    }

    @Test
    public void testWarn() {
        MetaLogger.warn(LOGGER, "Warn message");

        assertEquals(1, testListener.getReceivedEvents().size());
        LogEvent event = testListener.getReceivedEvents().get(0);
        assertEquals(Level.WARN, event.getLevel());
        assertEquals("Warn message", event.getMessage());
    }

    @Test
    public void testWarnWithArgs() {
        MetaLogger.warn(LOGGER, "Warn message: {}", "test");

        assertEquals(1, testListener.getReceivedEvents().size());
        LogEvent event = testListener.getReceivedEvents().get(0);
        assertEquals(Level.WARN, event.getLevel());
        assertEquals("Warn message: test", event.getMessage());
    }

    @Test
    public void testWarnWithNullArgs() {
        MetaLogger.warn(LOGGER, "Warn message", (Object[]) null);

        assertEquals(1, testListener.getReceivedEvents().size());
        assertEquals("Warn message", testListener.getReceivedEvents().get(0).getMessage());
    }

    @Test
    public void testError() {
        MetaLogger.error(LOGGER, "Error message");

        assertEquals(1, testListener.getReceivedEvents().size());
        LogEvent event = testListener.getReceivedEvents().get(0);
        assertEquals(Level.ERROR, event.getLevel());
        assertEquals("Error message", event.getMessage());
        assertNull(event.getThrowable());
    }

    @Test
    public void testErrorWithArgs() {
        MetaLogger.error(LOGGER, "Error message: {}", "test");

        assertEquals(1, testListener.getReceivedEvents().size());
        LogEvent event = testListener.getReceivedEvents().get(0);
        assertEquals(Level.ERROR, event.getLevel());
        assertEquals("Error message: test", event.getMessage());
    }

    @Test
    public void testErrorWithThrowable() {
        Exception testException = new RuntimeException("Test exception");
        MetaLogger.error(LOGGER, testException, "Error with exception: {}", "test");

        assertEquals(1, testListener.getReceivedEvents().size());
        LogEvent event = testListener.getReceivedEvents().get(0);
        assertEquals(Level.ERROR, event.getLevel());
        assertEquals("Error with exception: test", event.getMessage());
        assertEquals(testException, event.getThrowable());
    }

    @Test
    public void testErrorWithThrowableAndNullArgs() {
        Exception testException = new RuntimeException("Test exception");
        MetaLogger.error(LOGGER, testException, "Error message", (Object[]) null);

        assertEquals(1, testListener.getReceivedEvents().size());
        LogEvent event = testListener.getReceivedEvents().get(0);
        assertEquals("Error message", event.getMessage());
        assertEquals(testException, event.getThrowable());
    }

    @Test
    public void testMultipleLogMessages() {
        MetaLogger.info(LOGGER, "Info 1");
        MetaLogger.warn(LOGGER, "Warn 1");
        MetaLogger.error(LOGGER, "Error 1");

        assertEquals(3, testListener.getReceivedEvents().size());
        assertEquals(Level.INFO, testListener.getReceivedEvents().get(0).getLevel());
        assertEquals(Level.WARN, testListener.getReceivedEvents().get(1).getLevel());
        assertEquals(Level.ERROR, testListener.getReceivedEvents().get(2).getLevel());
    }

    @Test
    public void testLoggerNameCorrectness() {
        MetaLogger.info(LOGGER, "Test message");

        LogEvent event = testListener.getReceivedEvents().get(0);
        assertNotNull(event.getLoggerName());
        assertTrue(event.getLoggerName().contains("MetaLoggerTest"));
    }

    @Test
    public void testTimestampSet() {
        long before = System.currentTimeMillis();
        MetaLogger.info(LOGGER, "Test message");
        long after = System.currentTimeMillis();

        LogEvent event = testListener.getReceivedEvents().get(0);
        assertNotNull(event.getTimestamp());
        assertTrue(event.getTimestamp() >= before && event.getTimestamp() <= after);
    }

    @Test
    public void testMdcContextIncluded() {
        if (!mdcSupported) {
            return;
        }

        MdcUtils.put("requestId", "req-123");
        MdcUtils.put("userId", "user-456");

        MetaLogger.info(LOGGER, "Test message with MDC");

        LogEvent event = testListener.getReceivedEvents().get(0);
        assertNotNull(event.getMdcContext());
        assertEquals("req-123", event.getMdcContext().get("requestId"));
        assertEquals("user-456", event.getMdcContext().get("userId"));
    }

    @Test
    public void testMetaLoggerIsFinalClass() {
        assertTrue(java.lang.reflect.Modifier.isFinal(MetaLogger.class.getModifiers()));
    }

    @Test
    public void testMetaLoggerIsPublicClass() {
        assertTrue(java.lang.reflect.Modifier.isPublic(MetaLogger.class.getModifiers()));
    }

    @Test
    public void testAllMethodsAreStatic() throws NoSuchMethodException {
        assertTrue(java.lang.reflect.Modifier.isStatic(
                MetaLogger.class.getMethod("debug", Logger.class, String.class, Object[].class).getModifiers()));
        assertTrue(java.lang.reflect.Modifier.isStatic(
                MetaLogger.class.getMethod("debugWhenEnabled", Logger.class, String.class, Object[].class).getModifiers()));
        assertTrue(java.lang.reflect.Modifier.isStatic(
                MetaLogger.class.getMethod("trace", Logger.class, String.class, Object[].class).getModifiers()));
        assertTrue(java.lang.reflect.Modifier.isStatic(
                MetaLogger.class.getMethod("info", Logger.class, String.class, Object[].class).getModifiers()));
        assertTrue(java.lang.reflect.Modifier.isStatic(
                MetaLogger.class.getMethod("warn", Logger.class, String.class, Object[].class).getModifiers()));
        assertTrue(java.lang.reflect.Modifier.isStatic(
                MetaLogger.class.getMethod("error", Logger.class, String.class, Object[].class).getModifiers()));
        assertTrue(java.lang.reflect.Modifier.isStatic(
                MetaLogger.class.getMethod("error", Logger.class, Throwable.class, String.class, Object[].class).getModifiers()));
    }

    @Test
    public void testEmptyMessage() {
        MetaLogger.info(LOGGER, "");

        assertEquals(1, testListener.getReceivedEvents().size());
        assertEquals("", testListener.getReceivedEvents().get(0).getMessage());
    }

    @Test
    public void testUnicodeMessage() {
        MetaLogger.info(LOGGER, "中文消息测试: {}", "测试值");

        assertEquals(1, testListener.getReceivedEvents().size());
        assertEquals("中文消息测试: 测试值", testListener.getReceivedEvents().get(0).getMessage());
    }

    @Test
    public void testSpecialCharactersInMessage() {
        MetaLogger.info(LOGGER, "Special chars: {} {} {}", "$100", "50%", "true");

        assertEquals(1, testListener.getReceivedEvents().size());
        assertEquals("Special chars: $100 50% true", testListener.getReceivedEvents().get(0).getMessage());
    }

    private static class TestLogEventListener implements LogEventListener {
        private final List<LogEvent> receivedEvents = new ArrayList<>();

        @Override
        public void onEvent(LogEvent logEvent) {
            receivedEvents.add(logEvent);
        }

        @Override
        public void onEvent(Level level, String loggerName, String message, Throwable throwable) {
            // 空实现
        }

        public List<LogEvent> getReceivedEvents() {
            return new ArrayList<>(receivedEvents);
        }
    }
}
