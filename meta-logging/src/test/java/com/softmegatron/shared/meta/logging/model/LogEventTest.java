package com.softmegatron.shared.meta.logging.model;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * LogEvent 测试类
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 2026/02/12
 */
public class LogEventTest {

    private static final Logger TEST_LOGGER = LoggerFactory.getLogger("LogEventTest");

    @Test
    public void testDefaultConstructor() {
        LogEvent event = new LogEvent();
        
        assertNull("默认构造函数应创建空事件", event.getLogger());
        assertNull("默认构造函数应创建空事件", event.getLevel());
        assertNull("默认构造函数应创建空事件", event.getLoggerName());
        assertNull("默认构造函数应创建空事件", event.getMessage());
        assertNull("默认构造函数应创建空事件", event.getThrowable());
        assertNull("默认构造函数应创建空事件", event.getMdcContext());
        assertNull("默认构造函数应创建空事件", event.getTimestamp());
    }

    @Test
    public void testFullConstructor() {
        String message = "Test message";
        Exception throwable = new RuntimeException("Test exception");
        Map<String, String> mdcContext = new HashMap<>();
        mdcContext.put("key1", "value1");
        mdcContext.put("key2", "value2");

        LogEvent event = new LogEvent(TEST_LOGGER, Level.INFO, message, throwable, mdcContext);

        assertEquals("Logger应该正确设置", TEST_LOGGER, event.getLogger());
        assertEquals("Logger名称应该正确设置", "LogEventTest", event.getLoggerName());
        assertEquals("Level应该正确设置", Level.INFO, event.getLevel());
        assertEquals("Message应该正确设置", message, event.getMessage());
        assertEquals("Throwable应该正确设置", throwable, event.getThrowable());
        assertEquals("MDC上下文应该正确设置", mdcContext, event.getMdcContext());
        assertNotNull("时间戳应该被设置", event.getTimestamp());
    }

    @Test
    public void testConstructorWithNullLogger() {
        LogEvent event = new LogEvent(null, Level.ERROR, "test message", new Exception(), null);

        assertNull("Logger应该为null", event.getLogger());
        assertNull("Logger名称应该为null", event.getLoggerName());
        assertEquals("Level应该正确设置", Level.ERROR, event.getLevel());
        assertEquals("Message应该正确设置", "test message", event.getMessage());
    }

    @Test
    public void testSetterAndGetter() {
        LogEvent event = new LogEvent();
        
        // 测试Logger setter/getter
        event.setLogger(TEST_LOGGER);
        assertEquals("Logger getter应该返回设置的值", TEST_LOGGER, event.getLogger());
        
        // 测试Level setter/getter
        event.setLevel(Level.WARN);
        assertEquals("Level getter应该返回设置的值", Level.WARN, event.getLevel());
        
        // 测试LoggerName setter/getter
        event.setLoggerName("CustomLogger");
        assertEquals("LoggerName getter应该返回设置的值", "CustomLogger", event.getLoggerName());
        
        // 测试Message setter/getter
        event.setMessage("Custom message");
        assertEquals("Message getter应该返回设置的值", "Custom message", event.getMessage());
        
        // 测试Throwable setter/getter
        Exception customException = new Exception("Custom exception");
        event.setThrowable(customException);
        assertEquals("Throwable getter应该返回设置的值", customException, event.getThrowable());
        
        // 测试MDC上下文setter/getter
        Map<String, String> customContext = new HashMap<>();
        customContext.put("customKey", "customValue");
        event.setMdcContext(customContext);
        assertEquals("MDC上下文getter应该返回设置的值", customContext, event.getMdcContext());
        
        // 测试时间戳setter/getter
        Long customTimestamp = System.currentTimeMillis();
        event.setTimestamp(customTimestamp);
        assertEquals("时间戳getter应该返回设置的值", customTimestamp, event.getTimestamp());
    }

    @Test
    public void testSerialization() {
        // 测试LogEvent是否实现了Serializable接口
        assertTrue("LogEvent应该实现Serializable接口", 
                  java.io.Serializable.class.isAssignableFrom(LogEvent.class));
        
        // 测试serialVersionUID
        try {
            java.lang.reflect.Field serialVersionUIDField = LogEvent.class.getDeclaredField("serialVersionUID");
            serialVersionUIDField.setAccessible(true);
            Long serialVersionUID = (Long) serialVersionUIDField.get(null);
            assertEquals("serialVersionUID应该正确设置", Long.valueOf(9223372036854775807L), serialVersionUID);
        } catch (Exception e) {
            fail("应该能够访问serialVersionUID字段: " + e.getMessage());
        }
    }

    @Test
    public void testTimestampAutoGeneration() {
        long before = System.currentTimeMillis();
        LogEvent event = new LogEvent(TEST_LOGGER, Level.INFO, "test", null, null);
        long after = System.currentTimeMillis();
        
        assertNotNull("时间戳应该自动生成", event.getTimestamp());
        assertTrue("时间戳应该在合理范围内", 
                  event.getTimestamp() >= before && event.getTimestamp() <= after);
    }

    @Test
    public void testEqualsAndHashCode() {
        // 基本相等性测试
        LogEvent event1 = new LogEvent(TEST_LOGGER, Level.INFO, "same message", null, null);
        LogEvent event2 = new LogEvent(TEST_LOGGER, Level.INFO, "same message", null, null);
        
        // 虽然内容相同，但由于时间戳不同，它们不相等
        assertNotEquals("不同实例即使内容相同也应该不相等", event1, event2);
        
        // 测试同一个实例
        assertEquals("同一实例应该相等", event1, event1);
        assertEquals("同一实例的hashCode应该相等", event1.hashCode(), event1.hashCode());
    }
}