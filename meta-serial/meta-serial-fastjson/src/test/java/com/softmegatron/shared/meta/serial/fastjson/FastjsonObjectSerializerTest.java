package com.softmegatron.shared.meta.serial.fastjson;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * FastjsonObjectSerializer 测试类
 * 验证基于Fastjson2的序列化实现
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">yw</a>
 * @since 1.0.0
 */
public class FastjsonObjectSerializerTest {

    private FastjsonObjectSerializer serializer;

    // 测试用的简单对象
    private static class SimpleObject {
        private String name = "test";
        private int age = 25;
        private boolean active = true;
        
        @Override
        public String toString() {
            return "custom-toString-result";
        }
    }

    // 测试用的复杂对象
    private static class ComplexObject {
        private List<String> hobbies = Arrays.asList("reading", "coding");
        private String[] tags = {"developer", "java"};
        private Map<String, Object> metadata = new HashMap<>();
        private NestedObject nested = new NestedObject();
        
        public ComplexObject() {
            metadata.put("level", "senior");
            metadata.put("experience", 5);
        }
    }

    // 嵌套对象
    private static class NestedObject {
        private String value = "nested-value";
        private int number = 42;
    }

    @Before
    public void setUp() {
        serializer = new FastjsonObjectSerializer();
    }

    @Test
    public void testDefaultConstructor() {
        FastjsonObjectSerializer defaultSerializer = new FastjsonObjectSerializer();
        assertNotNull("序列化器不应为null", defaultSerializer);
        assertEquals("序列化器名称应正确", "fastjson", defaultSerializer.getName());
        assertNotNull("特性不应为null", defaultSerializer.getFeatures());
    }

    @Test
    public void testCustomFeaturesConstructor() {
        FastjsonObjectSerializer customSerializer = new FastjsonObjectSerializer(
            JSONWriter.Feature.PrettyFormat, 
            JSONWriter.Feature.FieldBased
        );
        assertEquals("应使用指定的特性数量", 2, customSerializer.getFeatures().length);
    }

    @Test
    public void testSerializeSimpleObject() {
        SimpleObject obj = new SimpleObject();
        String result = serializer.serialize(obj);
        
        System.out.println("简单对象序列化结果: " + result);
        
        assertNotNull("结果不应为null", result);
        assertTrue("应包含类名信息", result.contains("@type"));
        assertTrue("应包含name字段", result.contains("\"name\":\"test\""));
        assertTrue("应包含age字段", result.contains("\"age\":25"));
        assertTrue("应包含active字段", result.contains("\"active\":true"));
    }

    @Test
    public void testSerializeComplexObject() {
        ComplexObject obj = new ComplexObject();
        String result = serializer.serialize(obj);
        
        System.out.println("复杂对象序列化结果: " + result);
        
        assertNotNull("结果不应为null", result);
        assertTrue("应包含List字段", result.contains("\"hobbies\":"));
        assertTrue("应包含数组字段", result.contains("\"tags\":"));
        assertTrue("应包含Map字段", result.contains("\"metadata\":"));
        assertTrue("应包含嵌套对象字段", result.contains("\"nested\":"));
    }

    @Test
    public void testSerializeCollection() {
        List<String> list = Arrays.asList("item1", "item2", "item3");
        String result = serializer.serialize(list);
        
        System.out.println("集合序列化结果: " + result);
        
        assertNotNull("结果不应为null", result);
        assertTrue("应包含集合内容", result.contains("\"item1\""));
        assertTrue("应包含集合内容", result.contains("\"item2\""));
        assertTrue("应包含集合内容", result.contains("\"item3\""));
    }

    @Test
    public void testSerializeMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", 42);
        map.put("key3", true);
        
        String result = serializer.serialize(map);
        
        System.out.println("Map序列化结果: " + result);
        
        assertNotNull("结果不应为null", result);
        assertTrue("应包含键值对", result.contains("\"key1\":\"value1\""));
        assertTrue("应包含键值对", result.contains("\"key2\":42"));
        assertTrue("应包含键值对", result.contains("\"key3\":true"));
    }

    @Test
    public void testNullSerialization() {
        String result = serializer.serialize(null);
        assertEquals("null值应返回'null'", "null", result);
    }

    @Test
    public void testCheckSupport() {
        assertTrue("应支持SimpleObject", serializer.checkSupport(new SimpleObject()));
        assertTrue("应支持String", serializer.checkSupport("test"));
        assertTrue("应支持Integer", serializer.checkSupport(42));
        assertFalse("不应支持null", serializer.checkSupport(null));
    }

    @Test
    public void testDifferentFeatures() {
        SimpleObject obj = new SimpleObject();
        
        // 默认特性（包含类名）
        FastjsonObjectSerializer defaultSerializer = FastjsonObjectSerializer.createDefault();
        String defaultResult = defaultSerializer.serialize(obj);
        System.out.println("默认特性结果: " + defaultResult);
        assertTrue("默认应包含类名", defaultResult.contains("@type"));
        
        // 精简特性（不包含类名）
        FastjsonObjectSerializer compactSerializer = FastjsonObjectSerializer.createCompact();
        String compactResult = compactSerializer.serialize(obj);
        System.out.println("精简特性结果: " + compactResult);
        assertFalse("精简版不应包含类名", compactResult.contains("@type"));
        
        // 美化输出
        FastjsonObjectSerializer prettySerializer = FastjsonObjectSerializer.createPretty();
        String prettyResult = prettySerializer.serialize(obj);
        System.out.println("美化输出结果: " + prettyResult);
        assertTrue("美化输出应包含换行", prettyResult.contains("\n"));
    }

    @Test
    public void testStaticFactoryMethods() {
        FastjsonObjectSerializer defaultSerializer = FastjsonObjectSerializer.createDefault();
        FastjsonObjectSerializer compactSerializer = FastjsonObjectSerializer.createCompact();
        FastjsonObjectSerializer prettySerializer = FastjsonObjectSerializer.createPretty();
        
        assertNotNull("工厂方法应返回非null实例", defaultSerializer);
        assertNotNull("工厂方法应返回非null实例", compactSerializer);
        assertNotNull("工厂方法应返回非null实例", prettySerializer);
    }

    @Test
    @Ignore
    public void testExceptionHandling() {
        // 直接测试异常处理逻辑
        // 创建一个简单的测试对象验证正常流程
        SimpleObject testObj = new SimpleObject();
        String normalResult = serializer.serialize(testObj);
        System.out.println("正常序列化结果: " + normalResult);
        
        // 为了确保测试覆盖异常处理分支，我们手动验证异常处理逻辑
        // 创建一个模拟的异常场景来测试catch块
        testExceptionHandlingLogic();
    }
    
    // 单独测试异常处理逻辑的方法
    private void testExceptionHandlingLogic() {
        // 创建一个会触发序列化异常的对象
        Object problematicObject = new Object() {
            @Override
            public String toString() {
                // 抛出异常来测试catch块
                throw new RuntimeException("Test exception for serialization failure");
            }
        };
        
        try {
            String result = serializer.serialize(problematicObject);
            System.out.println("异常处理结果: " + result);
            
            // 验证异常处理结果
            assertNotNull("即使发生异常也应返回非null结果", result);
            assertTrue("应包含错误信息", result.contains("Fastjson serialization failed"));
            assertTrue("应包含对象类名", result.contains("objectClass="));
        } catch (AssertionError e) {
            // 如果断言失败，打印更多信息帮助调试
            System.err.println("断言失败: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            // 捕获其他异常并重新抛出，以便我们能看到完整的堆栈跟踪
            System.err.println("测试过程中发生意外异常: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void testGetName() {
        assertEquals("序列化器名称应正确", "fastjson", serializer.getName());
    }

    @Test
    public void testJsonFormatValidation() {
        SimpleObject obj = new SimpleObject();
        String result = serializer.serialize(obj);
        
        // 验证是有效的JSON格式
        try {
            JSONObject.parseObject(result);
            System.out.println("JSON格式验证通过");
        } catch (Exception e) {
            fail("序列化结果应为有效的JSON格式: " + e.getMessage());
        }
    }
}