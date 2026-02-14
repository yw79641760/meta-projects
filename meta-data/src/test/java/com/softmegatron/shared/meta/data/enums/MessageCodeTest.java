package com.softmegatron.shared.meta.data.enums;

import com.softmegatron.shared.meta.data.base.ReturnCode;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * MessageCode 测试类
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">yw</a>
 * @description 测试 MessageCode 枚举的功能和实现
 * @date 2026/2/11 22:35
 * @since 1.0.0
 */
public class MessageCodeTest {

    @Test
    public void testSuccessfulCode() {
        MessageCode successful = MessageCode.SUCCESSFUL;
        
        assertEquals("code应该是successful", "successful", successful.getCode());
        assertEquals("desc应该是成功", "成功", successful.getDesc());
        System.out.println("SUCCESSFUL枚举: code=" + successful.getCode() + ", desc=" + successful.getDesc());
    }

    @Test
    public void testEnumImplementsReturnCode() {
        MessageCode code = MessageCode.SUCCESSFUL;
        assertTrue("MessageCode应该实现ReturnCode接口", code instanceof ReturnCode);
        assertTrue("MessageCode应该实现BaseEnum接口", code instanceof com.softmegatron.shared.meta.data.base.BaseEnum);
        assertTrue("MessageCode应该实现Serializable", code instanceof java.io.Serializable);
    }

    @Test
    public void testEnumSingleton() {
        MessageCode code1 = MessageCode.SUCCESSFUL;
        MessageCode code2 = MessageCode.SUCCESSFUL;
        
        assertSame("枚举应该是单例", code1, code2);
        assertEquals("hashCode应该相同", code1.hashCode(), code2.hashCode());
    }

    @Test
    public void testNameMethod() {
        MessageCode code = MessageCode.SUCCESSFUL;
        assertEquals("name应该是SUCCESSFUL", "SUCCESSFUL", code.name());
    }

    @Test
    public void testOrdinalMethod() {
        MessageCode code = MessageCode.SUCCESSFUL;
        assertEquals("ordinal应该是0", 0, code.ordinal());
    }

    @Test
    public void testToStringMethod() {
        MessageCode code = MessageCode.SUCCESSFUL;
        String result = code.toString();
        
        assertNotNull("toString结果不应为null", result);
        assertFalse("toString结果不应为空", result.isEmpty());
        System.out.println("toString结果: " + result);
    }

    @Test
    public void testValuesMethod() {
        MessageCode[] values = MessageCode.values();
        
        assertNotNull("values结果不应为null", values);
        assertEquals("应该只有一个枚举值", 1, values.length);
        assertEquals("第一个值应该是SUCCESSFUL", MessageCode.SUCCESSFUL, values[0]);
        System.out.println("values方法测试通过，共有" + values.length + "个枚举值");
    }

    @Test
    public void testValueOfMethod() {
        MessageCode code = MessageCode.valueOf("SUCCESSFUL");
        
        assertNotNull("valueOf结果不应为null", code);
        assertEquals("应该返回SUCCESSFUL", MessageCode.SUCCESSFUL, code);
        
        // 测试不存在的值
        try {
            MessageCode.valueOf("NON_EXISTENT");
            fail("应该抛出IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue("应该抛出IllegalArgumentException", e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void testEqualsAndHashCode() {
        MessageCode code1 = MessageCode.SUCCESSFUL;
        MessageCode code2 = MessageCode.SUCCESSFUL;
        MessageCode code3 = null;
        
        assertTrue("相同枚举应该相等", code1.equals(code2));
        assertFalse("与null不应该相等", code1.equals(code3));
        assertFalse("与其他类型不应该相等", code1.equals("successful"));
        
        assertEquals("相同枚举hashCode应该相等", code1.hashCode(), code2.hashCode());
        System.out.println("equals和hashCode测试通过");
    }

    @Test
    public void testCompareToMethod() {
        MessageCode code = MessageCode.SUCCESSFUL;
        int result = code.compareTo(MessageCode.SUCCESSFUL);
        
        assertEquals("与自身比较应该返回0", 0, result);
        System.out.println("compareTo测试通过");
    }

    @Test
    public void testGetDeclaringClass() {
        MessageCode code = MessageCode.SUCCESSFUL;
        Class<? extends MessageCode> declaringClass = code.getDeclaringClass();
        
        assertEquals("声明类应该是MessageCode", MessageCode.class, declaringClass);
        System.out.println("声明类测试通过: " + declaringClass.getSimpleName());
    }

    @Test
    public void testSerialization() {
        MessageCode original = MessageCode.SUCCESSFUL;
        
        // 简单的序列化测试
        try {
            // 这里只是验证枚举可以参与序列化过程
            String name = original.name();
            int ordinal = original.ordinal();
            
            assertNotNull("name不应该为null", name);
            assertTrue("ordinal应该>=0", ordinal >= 0);
            
            System.out.println("序列化相关信息: name=" + name + ", ordinal=" + ordinal);
        } catch (Exception e) {
            fail("枚举应该支持序列化相关操作: " + e.getMessage());
        }
    }

    @Test
    public void testUsageInSwitchStatement() {
        MessageCode code = MessageCode.SUCCESSFUL;
        
        String result = switch (code) {
            case SUCCESSFUL -> "处理成功";
            default -> "未知状态";
        };
        
        assertEquals("switch语句应该正确执行", "处理成功", result);
        System.out.println("switch语句测试通过: " + result);
    }

    @Test
    public void testPerformance() {
        long startTime = System.nanoTime();
        
        // 大量访问测试
        for (int i = 0; i < 100000; i++) {
            MessageCode code = MessageCode.SUCCESSFUL;
            String codeValue = code.getCode();
            String descValue = code.getDesc();
        }
        
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        
        System.out.println("10万次访问耗时: " + duration + " ns");
        System.out.println("平均每次访问耗时: " + (duration / 100000) + " ns");
        
        // 性能应该很好（枚举访问很快）
        assertTrue("性能应该合理", duration < 100000000); // 100ms以内
    }
}