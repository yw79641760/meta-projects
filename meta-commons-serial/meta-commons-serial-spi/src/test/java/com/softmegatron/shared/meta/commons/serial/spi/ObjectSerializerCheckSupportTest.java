package com.softmegatron.shared.meta.commons.serial.spi;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * ObjectSerializer checkSupport 方法测试类
 * 验证各种场景下的支持性检查逻辑
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">yw</a>
 * @since 1.0.0
 */
public class ObjectSerializerCheckSupportTest {

    /**
     * 测试类型基础的序列化器
     */
    public static class TypeBasedSerializer implements ObjectSerializer {
        @Override
        public String serialize(Object obj) {
            return "TypeBased: " + obj.toString();
        }

        @Override
        public String getName() {
            return "type-based";
        }

        @Override
        public boolean checkSupport(Object obj) {
            // 只支持String和Number类型
            return obj instanceof String || obj instanceof Number;
        }
    }

    /**
     * 测试状态基础的序列化器
     */
    public static class StateBasedSerializer implements ObjectSerializer {
        @Override
        public String serialize(Object obj) {
            Validatable validatable = (Validatable) obj;
            return "StateBased: " + validatable.getContent();
        }

        @Override
        public String getName() {
            return "state-based";
        }

        @Override
        public boolean checkSupport(Object obj) {
            // 只支持有效的可验证对象
            return obj instanceof Validatable && ((Validatable) obj).isValid();
        }
    }

    /**
     * 测试安全基础的序列化器
     */
    public static class SecurityBasedSerializer implements ObjectSerializer {
        @Override
        public String serialize(Object obj) {
            SensitiveData data = (SensitiveData) obj;
            return "SecurityBased: " + data.getContent();
        }

        @Override
        public String getName() {
            return "security-based";
        }

        @Override
        public boolean checkSupport(Object obj) {
            // 拒绝机密数据
            if (obj instanceof SensitiveData) {
                return !((SensitiveData) obj).isConfidential();
            }
            return true;
        }
    }

    /**
     * 测试性能基础的序列化器
     */
    public static class PerformanceBasedSerializer implements ObjectSerializer {
        private static final int MAX_SIZE = 100;
        
        @Override
        public String serialize(Object obj) {
            LargeObject large = (LargeObject) obj;
            return "PerformanceBased: size=" + large.getSize();
        }

        @Override
        public String getName() {
            return "performance-based";
        }

        @Override
        public boolean checkSupport(Object obj) {
            // 只处理小对象
            if (obj instanceof LargeObject) {
                return ((LargeObject) obj).getSize() <= MAX_SIZE;
            }
            return true;
        }
    }

    // 辅助接口和类
    public interface Validatable {
        boolean isValid();
        String getContent();
    }

    public static class ValidData implements Validatable {
        private String content;
        
        public ValidData(String content) {
            this.content = content;
        }
        
        @Override
        public boolean isValid() { return true; }
        @Override
        public String getContent() { return content; }
    }

    public static class InvalidData implements Validatable {
        private String content;
        
        public InvalidData(String content) {
            this.content = content;
        }
        
        @Override
        public boolean isValid() { return false; }
        @Override
        public String getContent() { return content; }
    }

    public static class SensitiveData {
        private String content;
        private boolean confidential;
        
        public SensitiveData(String content, boolean confidential) {
            this.content = content;
            this.confidential = confidential;
        }
        
        public String getContent() { return content; }
        public boolean isConfidential() { return confidential; }
    }

    public static class LargeObject {
        private int size;
        
        public LargeObject(int size) {
            this.size = size;
        }
        
        public int getSize() { return size; }
    }

    @Test
    public void testTypeBasedSupport() {
        TypeBasedSerializer serializer = new TypeBasedSerializer();
        
        // 支持的类型
        assertTrue("应该支持String类型", serializer.checkSupport("hello"));
        assertTrue("应该支持Integer类型", serializer.checkSupport(42));
        assertTrue("应该支持Double类型", serializer.checkSupport(3.14));
        
        // 不支持的类型
        assertFalse("不应该支持Object类型", serializer.checkSupport(new Object()));
        assertFalse("不应该支持自定义类型", serializer.checkSupport(new ValidData("test")));
    }

    @Test
    public void testStateBasedSupport() {
        StateBasedSerializer serializer = new StateBasedSerializer();
        
        // 支持的有效对象
        ValidData validData = new ValidData("valid content");
        assertTrue("应该支持有效的数据", serializer.checkSupport(validData));
        
        // 不支持的无效对象
        InvalidData invalidData = new InvalidData("invalid content");
        assertFalse("不应该支持无效的数据", serializer.checkSupport(invalidData));
        
        // 不支持的其他类型
        assertFalse("不应该支持String类型", serializer.checkSupport("string"));
    }

    @Test
    public void testSecurityBasedSupport() {
        SecurityBasedSerializer serializer = new SecurityBasedSerializer();
        
        // 支持的非机密数据
        SensitiveData publicData = new SensitiveData("public info", false);
        assertTrue("应该支持非机密数据", serializer.checkSupport(publicData));
        
        // 不支持的机密数据
        SensitiveData secretData = new SensitiveData("secret info", true);
        assertFalse("不应该支持机密数据", serializer.checkSupport(secretData));
        
        // 支持的其他类型
        assertTrue("应该支持String类型", serializer.checkSupport("normal string"));
    }

    @Test
    public void testPerformanceBasedSupport() {
        PerformanceBasedSerializer serializer = new PerformanceBasedSerializer();
        
        // 支持的小对象
        LargeObject smallObject = new LargeObject(50);
        assertTrue("应该支持小对象", serializer.checkSupport(smallObject));
        
        // 不支持的大对象
        LargeObject largeObject = new LargeObject(200);
        assertFalse("不应该支持大对象", serializer.checkSupport(largeObject));
        
        // 支持的其他类型
        assertTrue("应该支持String类型", serializer.checkSupport("any string"));
    }

    @Test
    public void testCombinedScenarios() {
        System.out.println("=== 组合场景测试 ===");
        
        Object[] testObjects = {
            "字符串数据",
            123,
            new ValidData("有效数据"),
            new InvalidData("无效数据"),
            new SensitiveData("公开信息", false),
            new SensitiveData("机密信息", true),
            new LargeObject(80),
            new LargeObject(150)
        };
        
        ObjectSerializer[] serializers = {
            new TypeBasedSerializer(),
            new StateBasedSerializer(),
            new SecurityBasedSerializer(),
            new PerformanceBasedSerializer()
        };
        
        for (int i = 0; i < testObjects.length; i++) {
            Object obj = testObjects[i];
            System.out.println("\n测试对象: " + obj.getClass().getSimpleName() + " = " + obj);
            
            for (ObjectSerializer serializer : serializers) {
                boolean supported = serializer.checkSupport(obj);
                System.out.println("  " + serializer.getName() + ": " + 
                                 (supported ? "✓ 支持" : "✗ 不支持"));
            }
        }
    }

    @Test
    public void testNullHandling() {
        TypeBasedSerializer serializer = new TypeBasedSerializer();
        
        // 测试null值处理
        assertFalse("不应该支持null值", serializer.checkSupport(null));
    }

    @Test
    public void testConsistency() {
        TypeBasedSerializer serializer = new TypeBasedSerializer();
        String testData = "consistent test";
        
        // 多次调用应该返回相同结果
        boolean result1 = serializer.checkSupport(testData);
        boolean result2 = serializer.checkSupport(testData);
        boolean result3 = serializer.checkSupport(testData);
        
        assertEquals("多次调用应该返回一致结果", result1, result2);
        assertEquals("多次调用应该返回一致结果", result2, result3);
    }
}