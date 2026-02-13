package com.softmegatron.shared.meta.commons.logging.listener;

import com.softmegatron.shared.meta.commons.logging.model.LogEvent;
import com.softmegatron.shared.meta.commons.logging.utils.MdcUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * LogEventManager 测试类
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 2026/02/12
 */
public class LogEventManagerTest {

    private static final Logger TEST_LOGGER = LoggerFactory.getLogger("LogEventManagerTest");
    private boolean mdcSupported;
    
    @Before
    public void setUp() {
        // 测试前清理所有监听器
        LogEventManager.cleanup();
        MdcUtils.clear();
        
        // 检查MDC支持情况
        mdcSupported = checkMdcSupport();
    }

    @After
    public void tearDown() {
        // 测试后清理所有监听器
        LogEventManager.cleanup();
        MdcUtils.clear();
    }
    
    /**
     * 检查当前环境是否支持MDC功能
     */
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
    public void testRegisterAndUnregister() {
        TestLogEventListener listener = new TestLogEventListener();
        
        // 初始状态
        assertEquals("初始监听器数量应该为0", 0, LogEventManager.getListenerCount());
        
        // 注册监听器
        LogEventManager.register(listener);
        assertEquals("注册后监听器数量应该为1", 1, LogEventManager.getListenerCount());
        
        // 重复注册同一个监听器
        LogEventManager.register(listener);
        assertEquals("重复注册不应该增加数量", 1, LogEventManager.getListenerCount());
        
        // 注册另一个监听器
        TestLogEventListener listener2 = new TestLogEventListener();
        LogEventManager.register(listener2);
        assertEquals("注册不同监听器应该增加数量", 2, LogEventManager.getListenerCount());
        
        // 注销监听器
        LogEventManager.unregister(listener);
        assertEquals("注销后监听器数量应该为1", 1, LogEventManager.getListenerCount());
        
        // 注销不存在的监听器
        LogEventManager.unregister(listener);
        assertEquals("注销不存在的监听器不应该影响数量", 1, LogEventManager.getListenerCount());
        
        // 注销最后一个监听器
        LogEventManager.unregister(listener2);
        assertEquals("注销所有监听器后数量应该为0", 0, LogEventManager.getListenerCount());
    }

    @Test
    public void testFireEventWithNullParameters() {
        TestLogEventListener listener = new TestLogEventListener();
        LogEventManager.register(listener);
        
        // 测试各种null参数组合
        LogEventManager.fireEvent(null, Level.INFO, null, "test message", null);
        assertEquals("应该收到一个事件", 1, listener.getReceivedEvents().size());
        
        LogEventManager.fireEvent(TEST_LOGGER, null, "LogEventManagerTest", "test message", null);
        assertEquals("应该收到第二个事件", 2, listener.getReceivedEvents().size());
        
        LogEventManager.fireEvent(TEST_LOGGER, Level.INFO, "LogEventManagerTest", null, null);
        assertEquals("应该收到第三个事件", 3, listener.getReceivedEvents().size());
    }

    @Test
    public void testFireEventWithoutListeners() {
        // 不注册任何监听器，测试fireEvent是否正常执行
        LogEventManager.fireEvent(TEST_LOGGER, Level.INFO, "LogEventManagerTest", "test message", null);
        // 不应该抛出异常
    }

    @Test
    public void testFireEventWithExceptionInListener() {
        // 创建会抛异常的监听器
        LogEventListener badListener = new LogEventListener() {
            @Override
            public void onEvent(LogEvent logEvent) {
                throw new RuntimeException("Test exception from listener");
            }
            
            @Override
            public void onEvent(Level level, String loggerName, String message, Throwable throwable) {
                throw new RuntimeException("Test exception from listener");
            }
        };
        
        TestLogEventListener goodListener = new TestLogEventListener();
        
        LogEventManager.register(badListener);
        LogEventManager.register(goodListener);
        
        // 即使有监听器抛异常，fireEvent也应该正常执行
        LogEventManager.fireEvent(TEST_LOGGER, Level.INFO, "LogEventManagerTest", "test message", null);
        
        // 好的监听器应该仍然收到事件
        assertEquals("好的监听器应该收到事件", 1, goodListener.getReceivedEvents().size());
    }

    @Test
    public void testCleanup() {
        LogEventManager.register(new TestLogEventListener());
        LogEventManager.register(new TestLogEventListener());
        
        assertEquals("注册两个监听器后数量应该为2", 2, LogEventManager.getListenerCount());
        
        LogEventManager.cleanup();
        
        assertEquals("清理后监听器数量应该为0", 0, LogEventManager.getListenerCount());
    }

    @Test
    public void testConcurrentRegistration() throws InterruptedException {
        final int threadCount = 10;
        final int listenersPerThread = 100;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch finishLatch = new CountDownLatch(threadCount);
        
        // 并发注册监听器
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    startLatch.await(); // 等待所有线程就绪
                    
                    for (int j = 0; j < listenersPerThread; j++) {
                        LogEventManager.register(new NamedTestListener("Thread" + threadId + "-Listener" + j));
                    }
                    
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    finishLatch.countDown();
                }
            });
        }
        
        startLatch.countDown(); // 开始所有线程
        finishLatch.await(10, TimeUnit.SECONDS); // 等待完成
        executor.shutdown();
        
        // 验证结果
        int expectedCount = threadCount * listenersPerThread;
        assertEquals("并发注册后监听器数量应该正确", expectedCount, LogEventManager.getListenerCount());
    }

    @Test
    public void testEventContent() {
        TestLogEventListener listener = new TestLogEventListener();
        LogEventManager.register(listener);
        
        String testMessage = "Test event message";
        Exception testException = new RuntimeException("Test exception");
        
        // 准备MDC上下文（只有在支持MDC时才设置）
        Map<String, String> expectedMdcContext = new HashMap<>();
        if (mdcSupported) {
            MdcUtils.put("testKey", "testValue");
            MdcUtils.put("userId", "12345");
            expectedMdcContext.put("testKey", "testValue");
            expectedMdcContext.put("userId", "12345");
        }
        
        // 触发事件
        LogEventManager.fireEvent(TEST_LOGGER, Level.ERROR, "LogEventManagerTest", testMessage, testException);
        
        // 验证收到的事件内容
        assertEquals("应该收到一个事件", 1, listener.getReceivedEvents().size());
        LogEvent receivedEvent = listener.getReceivedEvents().get(0);
        
        assertEquals("Logger应该正确传递", TEST_LOGGER, receivedEvent.getLogger());
        assertEquals("Logger名称应该正确", "LogEventManagerTest", receivedEvent.getLoggerName());
        assertEquals("Level应该正确传递", Level.ERROR, receivedEvent.getLevel());
        assertEquals("Message应该正确传递", testMessage, receivedEvent.getMessage());
        assertEquals("Throwable应该正确传递", testException, receivedEvent.getThrowable());
        assertNotNull("时间戳应该被设置", receivedEvent.getTimestamp());
        
        // 验证MDC上下文
        if (mdcSupported) {
            assertNotNull("MDC上下文应该不为null", receivedEvent.getMdcContext());
            assertEquals("MDC上下文大小应该正确", expectedMdcContext.size(), receivedEvent.getMdcContext().size());
            expectedMdcContext.forEach((key, value) -> 
                assertEquals("MDC上下文应该包含设置的值", value, receivedEvent.getMdcContext().get(key))
            );
        } else {
            // 不支持MDC时，上下文应该为空或默认值
            assertTrue("不支持MDC时上下文应该为空或默认值", 
                      receivedEvent.getMdcContext() == null || receivedEvent.getMdcContext().isEmpty());
        }
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
     * 带名称的测试监听器
     */
    private static class NamedTestListener implements LogEventListener {
        private final String name;
        
        public NamedTestListener(String name) {
            this.name = name;
        }
        
        @Override
        public void onEvent(LogEvent logEvent) {
            // 空实现
        }
        
        @Override
        public void onEvent(Level level, String loggerName, String message, Throwable throwable) {
            // 空实现
        }
        
        @Override
        public String toString() {
            return "NamedTestListener{name='" + name + "'}";
        }
    }
}