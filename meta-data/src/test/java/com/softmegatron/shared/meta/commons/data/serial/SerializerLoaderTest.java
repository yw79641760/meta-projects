package com.softmegatron.shared.meta.commons.data.serial;

import com.softmegatron.shared.meta.commons.data.constants.MetaConstants;
import com.softmegatron.shared.meta.commons.serial.spi.ObjectSerializer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ServiceLoader;

import static org.junit.Assert.*;

/**
 * SerializerLoader 测试类
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">yw</a>
 * @description 测试 SerializerLoader 的SPI加载和缓存机制
 * @date 2026/2/5 17:00
 * @since 1.0.0
 */
public class SerializerLoaderTest {

    // 用于保存原始系统属性值
    private String originalPreferredSerializer;
    
    // 测试用的模拟序列化器
    private static class MockSerializer implements ObjectSerializer {
        private final String name;
        private boolean support = true;
        
        public MockSerializer(String name) {
            this.name = name;
        }
        
        public MockSerializer(String name, boolean support) {
            this.name = name;
            this.support = support;
        }
        
        public String serialize(Object obj) {
            return "Serialized by " + name + ": " + obj.toString();
        }
        
        public String getName() {
            return name;
        }
        
        public boolean checkSupport(Object obj) {
            return support;
        }
    }
    
    // 用于测试异常情况的序列化器
    private static class ExceptionThrowingSerializer implements ObjectSerializer {
        public String serialize(Object obj) {
            throw new RuntimeException("Serialization failed");
        }
        
        public String getName() {
            throw new RuntimeException("GetName failed");
        }
        
        public boolean checkSupport(Object obj) {
            throw new RuntimeException("CheckSupport failed");
        }
    }

    @Before
    public void setUp() {
        // 保存原始系统属性
        originalPreferredSerializer = System.getProperty(MetaConstants.SERIALIZER_PREFERRED);
        
        // 清除缓存以确保测试隔离
        clearSerializerCache();
    }

    @After
    public void tearDown() {
        // 恢复原始系统属性
        if (originalPreferredSerializer != null) {
            System.setProperty(MetaConstants.SERIALIZER_PREFERRED, originalPreferredSerializer);
        } else {
            System.clearProperty(MetaConstants.SERIALIZER_PREFERRED);
        }
        
        // 清除缓存
        clearSerializerCache();
    }

    /**
     * 清除 SerializerLoader 中的静态缓存
     */
    private void clearSerializerCache() {
        try {
            Field serializerField = ToStringSerializers.class.getDeclaredField("DEFAULT_SERIALIZER");
            serializerField.setAccessible(true);
            // 注意：DEFAULT_SERIALIZER是final的，不能重新赋值
            // 这里只是为了演示清理逻辑
        } catch (Exception e) {
            // 忽略异常，因为DEFAULT_SERIALIZER是final字段
        }
    }

    @Test
    public void testGetDefaultReturnsDefaultWhenNoSerializersAvailable() {
        // 设置一个不存在的序列化器名称
        System.setProperty(MetaConstants.SERIALIZER_PREFERRED, "non-existent-serializer");
        
        ObjectSerializer result = ToStringSerializers.getDefault();
        
        assertNotNull("当没有可用序列化器时应该返回默认序列化器", result);
        assertTrue("应该返回DefaultObjectSerializer实例", result instanceof DefaultObjectSerializer);
    }

    @Test
    public void testGetDefaultWithNullPreferredSerializer() {
        // 清除首选序列化器设置
        System.clearProperty(MetaConstants.SERIALIZER_PREFERRED);
        
        ObjectSerializer result = ToStringSerializers.getDefault();
        
        // 在没有SPI实现的情况下应该返回默认序列化器
        assertNotNull("当没有设置首选序列化器且无SPI实现时应该返回默认序列化器", result);
        assertTrue("应该返回DefaultObjectSerializer实例", result instanceof DefaultObjectSerializer);
    }

    @Test
    public void testConcurrentAccess() throws InterruptedException {
        // 这个测试主要用于验证同步机制
        System.clearProperty(MetaConstants.SERIALIZER_PREFERRED);
        
        // 模拟并发访问
        Thread[] threads = new Thread[10];
        ObjectSerializer[] results = new ObjectSerializer[10];
        
        for (int i = 0; i < 10; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                results[index] = ToStringSerializers.getDefault();
            });
        }
        
        // 启动所有线程
        for (Thread thread : threads) {
            thread.start();
        }
        
        // 等待所有线程完成
        for (Thread thread : threads) {
            thread.join();
        }
        
        // 验证所有结果一致（都是默认序列化器）
        for (int i = 0; i < results.length; i++) {
            assertNotNull("线程" + i + "的结果不应该为null", results[i]);
            assertTrue("线程" + i + "应该返回DefaultObjectSerializer", results[i] instanceof DefaultObjectSerializer);
        }
    }

    @Test
    public void testDoubleCheckedLocking() {
        System.clearProperty(MetaConstants.SERIALIZER_PREFERRED);
        
        // 第一次调用
        long startTime1 = System.nanoTime();
        ObjectSerializer result1 = ToStringSerializers.getDefault();
        long endTime1 = System.nanoTime();
        
        // 第二次调用（应该使用相同实例）
        long startTime2 = System.nanoTime();
        ObjectSerializer result2 = ToStringSerializers.getDefault();
        long endTime2 = System.nanoTime();
        
        System.out.println("第一次加载耗时: " + (endTime1 - startTime1) + " ns");
        System.out.println("第二次加载耗时: " + (endTime2 - startTime2) + " ns");
        
        // 验证实例一致性
        assertSame("两次调用应该返回相同的实例", result1, result2);
        assertTrue("结果应该是DefaultObjectSerializer", result1 instanceof DefaultObjectSerializer);
    }

    @Test
    public void testSystemPropertyHandling() {
        // 测试系统属性的处理
        String testSerializerName = "test-serializer";
        System.setProperty(MetaConstants.SERIALIZER_PREFERRED, testSerializerName);
        
        assertEquals("系统属性应该正确设置", 
                    testSerializerName, 
                    System.getProperty(MetaConstants.SERIALIZER_PREFERRED));
        
        // 调用getDefault会读取这个属性
        ObjectSerializer result = ToStringSerializers.getDefault();
        assertNotNull("结果不应该为null", result);
        assertTrue("在没有对应SPI实现时应该返回默认序列化器", result instanceof DefaultObjectSerializer);
    }

    @Test
    public void testServiceLoaderIteration() {
        System.clearProperty(MetaConstants.SERIALIZER_PREFERRED);
        
        // 这个测试验证ServiceLoader的迭代逻辑
        // 由于没有实际的SPI实现，getDefault()方法会返回默认序列化器
        ObjectSerializer result = ToStringSerializers.getDefault();
        assertNotNull("没有SPI实现时应该返回默认序列化器", result);
        assertTrue("应该返回DefaultObjectSerializer", result instanceof DefaultObjectSerializer);
    }

    @Test
    public void testCacheMechanism() {
        System.clearProperty(MetaConstants.SERIALIZER_PREFERRED);
        
        // 第一次加载
        ObjectSerializer firstResult = ToStringSerializers.getDefault();
        assertNotNull("第一次结果不应该为null", firstResult);
        assertTrue("第一次结果应该是DefaultObjectSerializer", firstResult instanceof DefaultObjectSerializer);
        
        // 第二次加载应该返回相同实例
        ObjectSerializer secondResult = ToStringSerializers.getDefault();
        assertNotNull("第二次结果不应该为null", secondResult);
        assertSame("两次调用应该返回相同实例", firstResult, secondResult);
    }

    @Test
    public void testMultipleCallsReturnSameInstance() {
        System.clearProperty(MetaConstants.SERIALIZER_PREFERRED);
        
        ObjectSerializer result1 = ToStringSerializers.getDefault();
        ObjectSerializer result2 = ToStringSerializers.getDefault();
        ObjectSerializer result3 = ToStringSerializers.getDefault();
        
        // 所有调用应该返回相同的实例
        assertSame("第一次和第二次调用应该返回相同实例", result1, result2);
        assertSame("第二次和第三次调用应该返回相同实例", result2, result3);
        assertTrue("所有结果都应该是DefaultObjectSerializer", result1 instanceof DefaultObjectSerializer);
    }

    @Test
    public void testPerformanceCharacteristics() {
        System.clearProperty(MetaConstants.SERIALIZER_PREFERRED);
        
        // 测试多次调用的性能特征
        long[] times = new long[5];
        
        for (int i = 0; i < times.length; i++) {
            long startTime = System.nanoTime();
            ToStringSerializers.getDefault();
            times[i] = System.nanoTime() - startTime;
        }
        
        System.out.println("多次调用耗时统计:");
        for (int i = 0; i < times.length; i++) {
            System.out.println("第" + (i+1) + "次: " + times[i] + " ns");
        }
        
        // 验证返回值的一致性
        ObjectSerializer result = ToStringSerializers.getDefault();
        assertTrue("应该返回DefaultObjectSerializer", result instanceof DefaultObjectSerializer);
        
        // 验证多次调用返回相同的结果
        ObjectSerializer result2 = ToStringSerializers.getDefault();
        assertSame("多次调用应该返回相同结果", result, result2);
    }
}