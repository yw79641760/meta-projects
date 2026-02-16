package com.softmegatron.shared.meta.logging.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

import java.io.Closeable;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * MdcUtils 测试类
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 2026/02/12
 */
public class MdcUtilsTest {

    private boolean mdcSupported;

    @Before
    public void setUp() {
        // 测试前清空MDC
        MdcUtils.clear();
        
        // 检查当前环境是否支持MDC
        mdcSupported = checkMdcSupport();
        if (!mdcSupported) {
            System.out.println("警告: 当前SLF4J实现不支持MDC功能，部分测试将跳过");
        }
    }

    /**
     * 检查当前环境是否支持MDC功能
     */
    private boolean checkMdcSupport() {
        try {
            MDC.put("test", "value");
            String result = MDC.get("test");
            MDC.remove("test");
            return "value".equals(result);
        } catch (Exception e) {
            return false;
        }
    }

    @After
    public void tearDown() {
        // 测试后清空MDC
        MdcUtils.clear();
    }

    @Test
    public void testPutAndGet() {
        if (!mdcSupported) {
            // 基本的null检查仍然要测试
            try {
                MdcUtils.put(null, "value");
                fail("应该抛出IllegalArgumentException");
            } catch (IllegalArgumentException e) {
                assertEquals("MDC key cannot be null", e.getMessage());
            }
            
            try {
                MdcUtils.put("key", null);
                fail("应该抛出IllegalArgumentException");
            } catch (IllegalArgumentException e) {
                assertEquals("MDC value cannot be null", e.getMessage());
            }
            return;
        }

        // 测试正常put/get操作
        MdcUtils.put("userId", "12345");
        assertEquals("应该能获取到设置的值", "12345", MdcUtils.get("userId"));
    }

    @Test
    public void testRemove() {
        if (!mdcSupported) {
            // 测试null处理
            MdcUtils.remove(null); // 不应该抛异常
            return;
        }

        // 设置值后移除
        MdcUtils.put("tempKey", "tempValue");
        assertEquals("设置后应该能获取到值", "tempValue", MdcUtils.get("tempKey"));
        
        MdcUtils.remove("tempKey");
        assertNull("移除后应该获取不到值", MdcUtils.get("tempKey"));
        
        // 测试移除不存在的键
        MdcUtils.remove("nonExistentKey");
        // 不应该抛异常
    }

    @Test
    public void testClear() {
        if (!mdcSupported) {
            MdcUtils.clear(); // 不应该抛异常
            return;
        }

        // 设置多个值
        MdcUtils.put("key1", "value1");
        MdcUtils.put("key2", "value2");
        MdcUtils.put("key3", "value3");
        
        assertFalse("清空前MDC不应该为空", MdcUtils.isEmpty());
        assertEquals("清空前大小应该是3", 3, MdcUtils.size());
        
        // 清空
        MdcUtils.clear();
        
        assertTrue("清空后MDC应该为空", MdcUtils.isEmpty());
        assertEquals("清空后大小应该是0", 0, MdcUtils.size());
        assertNull("清空后获取任何键都应该返回null", MdcUtils.get("key1"));
    }

    @Test
    public void testGetContextMap() {
        Map<String, String> map = MdcUtils.getContextMap();
        assertNotNull("应该返回非null的map", map);
        
        if (!mdcSupported) {
            assertTrue("不支持MDC时应该返回空map", map.isEmpty());
            return;
        }

        // 设置值后获取
        MdcUtils.put("testKey", "testValue");
        map = MdcUtils.getContextMap();
        assertNotNull("应该返回非null的map", map);
        assertEquals("map大小应该是1", 1, map.size());
        assertEquals("应该包含设置的键值对", "testValue", map.get("testKey"));
        
        // 验证返回的是副本
        map.put("newKey", "newValue");
        assertNull("修改返回的map不应该影响MDC", MdcUtils.get("newKey"));
    }

    @Test
    public void testSetContextMap() {
        if (!mdcSupported) {
            MdcUtils.setContextMap(null); // 不应该抛异常
            return;
        }

        // 准备测试数据
        Map<String, String> testData = new HashMap<>();
        testData.put("key1", "value1");
        testData.put("key2", "value2");
        
        // 设置上下文
        MdcUtils.setContextMap(testData);
        
        // 验证设置结果
        assertEquals("应该能获取到设置的值1", "value1", MdcUtils.get("key1"));
        assertEquals("应该能获取到设置的值2", "value2", MdcUtils.get("key2"));
        assertEquals("大小应该是2", 2, MdcUtils.size());
        
        // 测试null输入
        MdcUtils.setContextMap(null);
        assertTrue("null输入应该清空MDC", MdcUtils.isEmpty());
    }

    @Test
    public void testPutCloseable() throws Exception {
        if (!mdcSupported) {
            // 测试异常处理
            try {
                MdcUtils.putCloseable(null, "value");
                fail("应该抛出IllegalArgumentException");
            } catch (IllegalArgumentException e) {
                assertEquals("MDC key cannot be null", e.getMessage());
            }
            return;
        }

        // 使用try-with-resources测试自动关闭
        try (Closeable closeable = MdcUtils.putCloseable("requestId", "req-123")) {
            assertEquals("在try块内应该能获取到值", "req-123", MdcUtils.get("requestId"));
            assertFalse("MDC不应该为空", MdcUtils.isEmpty());
        }
        
        // 出try块后应该自动清理
        assertNull("出try块后应该获取不到值", MdcUtils.get("requestId"));
        assertTrue("出try块后MDC应该为空", MdcUtils.isEmpty());
    }

    @Test
    public void testPutAll() {
        if (!mdcSupported) {
            // 测试异常处理
            try {
                MdcUtils.putAll(null);
                fail("应该抛出IllegalArgumentException");
            } catch (IllegalArgumentException e) {
                assertEquals("Context map cannot be null", e.getMessage());
            }
            return;
        }

        // 准备批量数据
        Map<String, String> batchData = new HashMap<>();
        batchData.put("userId", "user123");
        batchData.put("sessionId", "session456");
        batchData.put("traceId", "trace789");
        
        // 批量设置
        MdcUtils.putAll(batchData);
        
        // 验证结果
        assertEquals("应该能获取到userId", "user123", MdcUtils.get("userId"));
        assertEquals("应该能获取到sessionId", "session456", MdcUtils.get("sessionId"));
        assertEquals("应该能获取到traceId", "trace789", MdcUtils.get("traceId"));
        assertEquals("大小应该是3", 3, MdcUtils.size());
    }

    @Test
    public void testContainsKey() {
        if (!mdcSupported) {
            assertFalse("不支持MDC时应该返回false", MdcUtils.containsKey("anyKey"));
            assertFalse("null键应该返回false", MdcUtils.containsKey(null));
            return;
        }

        // 测试不存在的键
        assertFalse("不存在的键应该返回false", MdcUtils.containsKey("nonExistent"));
        
        // 设置值后测试
        MdcUtils.put("existingKey", "someValue");
        assertTrue("存在的键应该返回true", MdcUtils.containsKey("existingKey"));
    }

    @Test
    public void testSizeAndIsEmpty() {
        if (!mdcSupported) {
            assertTrue("不支持MDC时应该为空", MdcUtils.isEmpty());
            assertEquals("不支持MDC时大小应该是0", 0, MdcUtils.size());
            return;
        }

        // 空MDC测试
        assertTrue("初始MDC应该为空", MdcUtils.isEmpty());
        assertEquals("初始MDC大小应该是0", 0, MdcUtils.size());
        
        // 添加一个元素
        MdcUtils.put("key1", "value1");
        assertFalse("添加元素后MDC不应该为空", MdcUtils.isEmpty());
        assertEquals("添加一个元素后大小应该是1", 1, MdcUtils.size());
    }

    @Test
    public void testThreadSafety() throws InterruptedException {
        if (!mdcSupported) {
            // 基本的线程安全检查
            MdcUtils.put("mainKey", "mainValue");
            MdcUtils.clear();
            return;
        }

        // 测试多线程环境下的隔离性
        MdcUtils.put("mainThreadKey", "mainThreadValue");
        
        Thread thread = new Thread(() -> {
            MdcUtils.put("childThreadKey", "childThreadValue");
            assertEquals("子线程应该只能看到自己的值", "childThreadValue", MdcUtils.get("childThreadKey"));
            assertNull("子线程不应该看到主线程的值", MdcUtils.get("mainThreadKey"));
        });
        
        thread.start();
        thread.join();
        
        // 验证主线程的值未受影响
        assertEquals("主线程应该能看到自己的值", "mainThreadValue", MdcUtils.get("mainThreadKey"));
        assertNull("主线程不应该看到子线程的值", MdcUtils.get("childThreadKey"));
    }

    @Test
    public void testSpecialCharacters() {
        if (!mdcSupported) {
            MdcUtils.put("test.key", "test.value");
            MdcUtils.clear();
            return;
        }

        // 测试特殊字符
        MdcUtils.put("special.key", "value.with.dots");
        MdcUtils.put("unicode-key", "中文值");
        MdcUtils.put("empty-value", "");
        
        assertEquals("应该正确处理带点的键", "value.with.dots", MdcUtils.get("special.key"));
        assertEquals("应该正确处理unicode值", "中文值", MdcUtils.get("unicode-key"));
        assertEquals("应该正确处理空值", "", MdcUtils.get("empty-value"));
    }
}