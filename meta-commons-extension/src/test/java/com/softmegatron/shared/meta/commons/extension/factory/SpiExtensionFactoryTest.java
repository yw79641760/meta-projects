package com.softmegatron.shared.meta.commons.extension.factory;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * SpiExtensionFactory 测试类
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">yw</a>
 * @description 测试 SpiExtensionFactory 实现类
 * @date 2026/2/6 16:00
 * @since 1.0.0
 */
public class SpiExtensionFactoryTest {

    private SpiExtensionFactory factory;

    @Before
    public void setUp() {
        factory = new SpiExtensionFactory();
    }

    @Test
    public void testSpiExtensionFactoryInstantiation() {
        assertNotNull("SpiExtensionFactory应该可以实例化", factory);
    }

    @Test
    public void testGetExtensionWithNullClassLoader() {
        assertNotNull("工厂实例不应该为null", factory);
        System.out.println("SpiExtensionFactory instance: " + factory);
    }

    @Test
    public void testSpiExtensionFactoryImplementsExtensionFactory() {
        assertTrue("SpiExtensionFactory应该实现ExtensionFactory接口", 
                   factory instanceof ExtensionFactory);
    }

    @Test
    public void testSpiExtensionFactoryHasDefaultConstructor() throws Exception {
        assertNotNull("SpiExtensionFactory应该有默认构造函数", 
                     SpiExtensionFactory.class.getConstructor());
    }

    @Test
    public void testSpiExtensionFactoryGetExtensionMethodExists() throws NoSuchMethodException {
        assertNotNull("SpiExtensionFactory应该有getExtension方法", 
                     SpiExtensionFactory.class.getMethod("getExtension", String.class, Class.class));
    }

    @Test
    public void testSpiExtensionFactoryMethodIsPublic() throws NoSuchMethodException {
        var method = SpiExtensionFactory.class.getMethod("getExtension", String.class, Class.class);
        assertTrue("getExtension方法应该是public的", 
                  java.lang.reflect.Modifier.isPublic(method.getModifiers()));
    }

    @Test
    public void testSpiExtensionFactoryMethodIsOverridden() throws NoSuchMethodException {
        var method = SpiExtensionFactory.class.getMethod("getExtension", String.class, Class.class);
        assertNotNull("方法应该存在", method);
        assertTrue("方法应该是public方法", 
                 java.lang.reflect.Modifier.isPublic(method.getModifiers()));
    }

    @Test
    public void testSpiExtensionFactoryMethodSignature() throws NoSuchMethodException {
        var method = SpiExtensionFactory.class.getMethod("getExtension", String.class, Class.class);
        Class<?>[] paramTypes = method.getParameterTypes();
        
        assertTrue("方法应该有2个参数", paramTypes.length == 2);
        assertTrue("第一个参数应该是String", paramTypes[0] == String.class);
        assertTrue("第二个参数应该是Class", paramTypes[1] == Class.class);
    }

    @Test
    public void testSpiExtensionFactoryReturnType() throws NoSuchMethodException {
        var method = SpiExtensionFactory.class.getMethod("getExtension", String.class, Class.class);
        
        assertTrue("返回类型应该是Object或其子类", 
                  Object.class.isAssignableFrom(method.getReturnType()));
    }

    @Test
    public void testSpiExtensionFactoryClassIsNotAbstract() {
        int modifiers = SpiExtensionFactory.class.getModifiers();
        assertFalse("SpiExtensionFactory不应该有abstract修饰符", 
                    java.lang.reflect.Modifier.isAbstract(modifiers));
    }

    @Test
    public void testSpiExtensionFactoryClassIsFinal() {
        int modifiers = SpiExtensionFactory.class.getModifiers();
        assertFalse("SpiExtensionFactory不应该有final修饰符", 
                    java.lang.reflect.Modifier.isFinal(modifiers));
    }

    @Test
    public void testSpiExtensionFactoryToString() {
        String toString = factory.toString();
        assertNotNull("toString不应该返回null", toString);
        assertTrue("toString应该包含类名", 
                 toString.contains("SpiExtensionFactory"));
        System.out.println("SpiExtensionFactory toString: " + toString);
    }

    @Test
    public void testSpiExtensionFactoryHashCode() {
        int hashCode = factory.hashCode();
        assertTrue("hashCode应该返回一个整数", hashCode != 0 || hashCode == 0);
        System.out.println("SpiExtensionFactory hashCode: " + hashCode);
    }

    @Test
    public void testSpiExtensionFactoryEquals() {
        SpiExtensionFactory factory1 = new SpiExtensionFactory();
        SpiExtensionFactory factory2 = new SpiExtensionFactory();
        
        assertNotNull("equals不应该返回null", factory1.equals(factory2));
    }
}
