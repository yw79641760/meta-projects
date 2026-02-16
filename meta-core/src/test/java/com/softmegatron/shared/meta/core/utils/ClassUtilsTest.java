package com.softmegatron.shared.meta.core.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * ClassUtils 测试类
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">yw</a>
 * @description 测试 ClassUtils 的各种功能
 * @date 2026/2/6 14:50
 * @since 1.0.0
 */
public class ClassUtilsTest {

    private static class SimpleClass {
        public String name = "test";

        public SimpleClass() {
        }
    }

    private static class NoArgConstructorClass {
        private int value;

        public NoArgConstructorClass() {
            this.value = 100;
        }

        public int getValue() {
            return value;
        }
    }

    private static class NoPublicConstructorClass {
        private NoPublicConstructorClass() {
        }
    }

    private static class ThrowingConstructorClass {
        public ThrowingConstructorClass() {
            throw new RuntimeException("Constructor throws exception");
        }
    }

    @Test
    public void testGetDefaultClassLoader() {
        ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
        assertNotNull("ClassLoader不应为null", classLoader);
    }

    @Test
    public void testNewInstanceWithSimpleClass() {
        SimpleClass instance = ClassUtils.newInstance(SimpleClass.class);
        assertNotNull("实例不应为null", instance);
        assertTrue("实例类型应正确", instance instanceof SimpleClass);
    }

    @Test
    public void testNewInstanceWithNoArgConstructorClass() {
        NoArgConstructorClass instance = ClassUtils.newInstance(NoArgConstructorClass.class);
        assertNotNull("实例不应为null", instance);
        assertEquals("实例应正确初始化", 100, instance.getValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testNewInstanceWithNoPublicConstructorClass() {
        ClassUtils.newInstance(NoPublicConstructorClass.class);
    }

    @Test(expected = IllegalStateException.class)
    public void testNewInstanceWithThrowingConstructorClass() {
        ClassUtils.newInstance(ThrowingConstructorClass.class);
    }

    @Test(expected = IllegalStateException.class)
    public void testNewInstanceWithInterface() {
        ClassUtils.newInstance(Runnable.class);
    }

    @Test(expected = IllegalStateException.class)
    public void testNewInstanceWithAbstractClass() {
        ClassUtils.newInstance(AbstractClass.class);
    }

    @Test(expected = IllegalStateException.class)
    public void testNewInstanceWithPrimitiveType() {
        ClassUtils.newInstance(int.class);
    }

    @Test(expected = IllegalStateException.class)
    public void testNewInstanceWithArrayType() {
        ClassUtils.newInstance(String[].class);
    }

    private static abstract class AbstractClass {
    }
}
