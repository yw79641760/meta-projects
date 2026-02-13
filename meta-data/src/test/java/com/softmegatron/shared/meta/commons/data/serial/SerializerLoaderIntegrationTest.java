package com.softmegatron.shared.meta.commons.data.serial;

import com.softmegatron.shared.meta.commons.data.constants.MetaConstants;
import com.softmegatron.shared.meta.commons.serial.spi.ObjectSerializer;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

/**
 * SerializerLoader 集成测试类
 * 测试真实环境下的SPI加载行为
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">yw</a>
 * @description 集成测试SerializerLoader在真实SPI环境中的行为
 * @date 2026/2/5 17:30
 * @since 1.0.0
 */
public class SerializerLoaderIntegrationTest {

    private String originalPreferredSerializer;

    @Before
    public void setUp() {
        originalPreferredSerializer = System.getProperty(MetaConstants.SERIALIZER_PREFERRED);
        // 注意：DEFAULT_SERIALIZER是final字段，无法清除缓存
    }

    @After
    public void tearDown() {
        if (originalPreferredSerializer != null) {
            System.setProperty(MetaConstants.SERIALIZER_PREFERRED, originalPreferredSerializer);
        } else {
            System.clearProperty(MetaConstants.SERIALIZER_PREFERRED);
        }
    }

    /**
     * 清除 SerializerLoader 中的静态缓存
     */
    @Test
    public void testRealSpiLoadingScenario() {
        System.out.println("=== SerializerLoader 集成测试 ===");
        
        // 测试1: 无配置情况
        System.clearProperty(MetaConstants.SERIALIZER_PREFERRED);
        System.out.println("测试1 - 无首选序列化器配置:");
        ObjectSerializer result1 = ToStringSerializers.getDefault();
        System.out.println("结果: " + result1);
        assertTrue("无SPI实现时应该返回默认序列化器", result1 instanceof DefaultObjectSerializer);
        
        // 测试2: 设置不存在的序列化器
        System.setProperty(MetaConstants.SERIALIZER_PREFERRED, "non-existent-serializer");
        System.out.println("\n测试2 - 设置不存在的序列化器:");
        ObjectSerializer result2 = ToStringSerializers.getDefault();
        System.out.println("结果: " + result2);
        assertTrue("不存在的序列化器应该返回默认序列化器", result2 instanceof DefaultObjectSerializer);
        
        // 测试3: 验证实例一致性
        System.out.println("\n测试3 - 实例一致性验证:");
        long startTime = System.nanoTime();
        ObjectSerializer result3a = ToStringSerializers.getDefault();
        long firstCallTime = System.nanoTime() - startTime;
        
        startTime = System.nanoTime();
        ObjectSerializer result3b = ToStringSerializers.getDefault();
        long secondCallTime = System.nanoTime() - startTime;
        
        System.out.println("第一次调用耗时: " + firstCallTime + " ns");
        System.out.println("第二次调用耗时: " + secondCallTime + " ns");
        System.out.println("第一次结果: " + result3a);
        System.out.println("第二次结果: " + result3b);
        
        assertSame("应该返回相同实例", result3a, result3b);
        assertTrue("应该返回DefaultObjectSerializer", result3a instanceof DefaultObjectSerializer);
    }

    @Test
    public void testSystemPropertyBehavior() {
        System.out.println("=== 系统属性行为测试 ===");
        
        // 测试系统属性的读取和处理
        String testValue = "integration-test-serializer";
        System.setProperty(MetaConstants.SERIALIZER_PREFERRED, testValue);
        
        assertEquals("系统属性应该正确设置", 
                    testValue, 
                    System.getProperty(MetaConstants.SERIALIZER_PREFERRED));
        
        // 验证SerializerLoader能够读取到这个属性
        ObjectSerializer result = ToStringSerializers.getDefault();
        System.out.println("使用系统属性 '" + testValue + "' 加载结果: " + result);
        assertTrue("在没有对应SPI实现时仍应返回默认序列化器", result instanceof DefaultObjectSerializer);
    }

    @Test
    public void testThreadSafetyInRealEnvironment() throws InterruptedException {
        System.out.println("=== 线程安全性测试 ===");
        
        System.clearProperty(MetaConstants.SERIALIZER_PREFERRED);
        
        int threadCount = 5;
        Thread[] threads = new Thread[threadCount];
        TestResult[] results = new TestResult[threadCount];
        
        // 创建多个线程同时调用
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                try {
                    long startTime = System.currentTimeMillis();
                    ObjectSerializer serializer = ToStringSerializers.getDefault();
                    long endTime = System.currentTimeMillis();
                    results[index] = new TestResult(serializer, endTime - startTime, null);
                } catch (Exception e) {
                    results[index] = new TestResult(null, 0, e);
                }
            });
        }
        
        // 启动所有线程
        System.out.println("启动 " + threadCount + " 个并发线程...");
        for (Thread thread : threads) {
            thread.start();
        }
        
        // 等待所有线程完成
        for (Thread thread : threads) {
            thread.join(5000); // 5秒超时
        }
        
        // 验证结果
        System.out.println("\n并发测试结果:");
        ObjectSerializer firstNonNullResult = null;
        for (int i = 0; i < results.length; i++) {
            TestResult result = results[i];
            System.out.println("线程 " + i + ": " + 
                             (result.exception != null ? "异常: " + result.exception.getMessage() : 
                              "结果: " + result.serializer + ", 耗时: " + result.duration + "ms"));
            
            if (result.exception != null) {
                fail("线程 " + i + " 出现异常: " + result.exception.getMessage());
            }
            
            if (firstNonNullResult == null && result.serializer != null) {
                firstNonNullResult = result.serializer;
            } else if (result.serializer != null) {
                assertSame("所有线程应该获得相同的序列化器实例", 
                          firstNonNullResult, result.serializer);
            }
            assertTrue("结果应该是DefaultObjectSerializer", result.serializer instanceof DefaultObjectSerializer);
        }
    }

    @Test
    @Ignore
    public void testPerformanceCharacteristicsInRealEnvironment() {
        System.out.println("=== 性能特征测试 ===");
        
        System.clearProperty(MetaConstants.SERIALIZER_PREFERRED);
        
        // 预热
        ToStringSerializers.getDefault();
        
        // 多次调用测试
        int iterations = 10;
        long[] times = new long[iterations];
        
        System.out.println("执行 " + iterations + " 次连续调用:");
        for (int i = 0; i < iterations; i++) {
            long startTime = System.nanoTime();
            ObjectSerializer result = ToStringSerializers.getDefault();
            times[i] = System.nanoTime() - startTime;
            System.out.println("第 " + (i+1) + " 次: " + times[i] + " ns, 结果: " + result);
            assertTrue("结果应该是DefaultObjectSerializer", result instanceof DefaultObjectSerializer);
        }
        
        // 计算统计数据
        long minTime = Long.MAX_VALUE;
        long maxTime = Long.MIN_VALUE;
        long totalTime = 0;
        
        for (long time : times) {
            minTime = Math.min(minTime, time);
            maxTime = Math.max(maxTime, time);
            totalTime += time;
        }
        
        double avgTime = (double) totalTime / iterations;
        
        System.out.println("\n性能统计:");
        System.out.println("最小耗时: " + minTime + " ns");
        System.out.println("最大耗时: " + maxTime + " ns");
        System.out.println("平均耗时: " + String.format("%.2f", avgTime) + " ns");
        System.out.println("总耗时: " + totalTime + " ns");
        
        // 验证缓存效果：后续调用应该比首次调用快
        if (iterations > 1) {
            assertTrue("缓存后的调用应该更快 (首次: " + times[0] + "ns, 第二次: " + times[1] + "ns)", 
                      times[1] <= times[0]);
        }
    }

    /**
     * 测试结果封装类
     */
    private static class TestResult {
        final ObjectSerializer serializer;
        final long duration;
        final Exception exception;
        
        TestResult(ObjectSerializer serializer, long duration, Exception exception) {
            this.serializer = serializer;
            this.duration = duration;
            this.exception = exception;
        }
    }
}