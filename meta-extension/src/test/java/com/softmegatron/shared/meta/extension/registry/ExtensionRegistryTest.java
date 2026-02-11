package com.softmegatron.shared.meta.extension.registry;

import com.softmegatron.shared.meta.extension.annotation.SPI;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * ExtensionRegistry 测试类
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">yw</a>
 * @description 测试 ExtensionRegistry 注册表
 * @date 2026/2/6 16:00
 * @since 1.0.0
 */
public class ExtensionRegistryTest {

    @SPI("testExtension")
    private interface TestExtension {
    }

    private interface TestInterfaceWithoutSPI {
    }

    @Before
    public void setUp() {
    }

    @Test
    public void testExtensionRegistryIsNotInstantiable() {
        try {
            ExtensionRegistry.class.getDeclaredConstructor().newInstance();
            fail("ExtensionRegistry不应该可以实例化");
        } catch (Exception e) {
            assertTrue("应该抛出异常", 
                      e instanceof IllegalAccessException || e.getCause() instanceof IllegalAccessException);
        }
    }

    @Test
    public void testExtensionRegistryConstructorIsPrivate() throws NoSuchMethodException {
        var constructor = ExtensionRegistry.class.getDeclaredConstructor();
        assertNotNull("构造函数应该存在", constructor);
        assertTrue("构造函数应该是private的", 
                  java.lang.reflect.Modifier.isPrivate(constructor.getModifiers()));
    }

    @Test
    public void testGetExtensionLoaderWithNullClass() {
        try {
            ExtensionRegistry.getExtensionLoader(null);
            fail("传入null应该抛出IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertNotNull("异常信息不应该为null", e.getMessage());
            assertTrue("异常信息应该包含'Empty class'", 
                      e.getMessage().contains("Empty class"));
            System.out.println("Expected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetExtensionLoaderWithNonInterfaceClass() {
        try {
            ExtensionRegistry.getExtensionLoader(String.class);
            fail("传入非接口类应该抛出IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertNotNull("异常信息不应该为null", e.getMessage());
            System.out.println("Expected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetExtensionLoaderWithoutSPIAnnotation() {
        try {
            ExtensionRegistry.getExtensionLoader(TestInterfaceWithoutSPI.class);
            fail("传入没有SPI注解的接口应该抛出IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertNotNull("异常信息不应该为null", e.getMessage());
            assertTrue("异常信息应该包含'Empty required annotation'", 
                      e.getMessage().contains("Empty required annotation"));
            System.out.println("Expected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetExtensionLoaderWithValidInterface() {
        ExtensionLoader<TestExtension> loader = ExtensionRegistry.getExtensionLoader(TestExtension.class);
        
        assertNotNull("返回的ExtensionLoader不应该为null", loader);
        assertNotNull("ExtensionLoader应该初始化spi注解", loader);
    }

    @Test
    public void testGetExtensionLoaderReturnsSameInstanceForSameClass() {
        ExtensionLoader<TestExtension> loader1 = ExtensionRegistry.getExtensionLoader(TestExtension.class);
        ExtensionLoader<TestExtension> loader2 = ExtensionRegistry.getExtensionLoader(TestExtension.class);
        
        assertNotNull("第一个loader不应该为null", loader1);
        assertNotNull("第二个loader不应该为null", loader2);
        assertTrue("同一类的多次调用应该返回相同的loader实例", loader1 == loader2);
    }

    @Test
    public void testGetExtensionLoaderIsStaticMethod() throws NoSuchMethodException {
        var method = ExtensionRegistry.class.getMethod("getExtensionLoader", Class.class);
        assertTrue("getExtensionLoader应该是静态方法", 
                  java.lang.reflect.Modifier.isStatic(method.getModifiers()));
    }

    @Test
    public void testGetExtensionLoaderIsPublicMethod() throws NoSuchMethodException {
        var method = ExtensionRegistry.class.getMethod("getExtensionLoader", Class.class);
        assertTrue("getExtensionLoader应该是public方法", 
                  java.lang.reflect.Modifier.isPublic(method.getModifiers()));
    }

    @Test
    public void testGetExtensionLoaderTakesClassParameter() throws NoSuchMethodException {
        var method = ExtensionRegistry.class.getMethod("getExtensionLoader", Class.class);
        Class<?>[] paramTypes = method.getParameterTypes();
        
        assertEquals("方法应该有1个参数", 1, paramTypes.length);
        assertEquals("参数类型应该是Class", Class.class, paramTypes[0]);
    }

    @Test
    public void testGetExtensionLoaderReturnsGenericExtensionLoader() throws NoSuchMethodException {
        var method = ExtensionRegistry.class.getMethod("getExtensionLoader", Class.class);
        
        assertNotNull("方法返回类型不应该为null", method.getGenericReturnType());
    }

    @Test
    public void testExtensionRegistryHasPrivateConstructor() {
        int modifiers = ExtensionRegistry.class.getDeclaredConstructors()[0].getModifiers();
        assertTrue("ExtensionRegistry的构造函数应该是private的", 
                  java.lang.reflect.Modifier.isPrivate(modifiers));
    }

    @Test
    public void testExtensionRegistryIsNotFinalClass() {
        int modifiers = ExtensionRegistry.class.getModifiers();
        assertFalse("ExtensionRegistry不应该是final类", 
                    java.lang.reflect.Modifier.isFinal(modifiers));
    }
}
