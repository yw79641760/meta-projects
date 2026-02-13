package com.softmegatron.shared.meta.commons.extension.factory;

import org.junit.Before;
import org.junit.Test;

import java.lang.annotation.Annotation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * ExtensionFactory 测试类
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">yw</a>
 * @description 测试 ExtensionFactory 接口
 * @date 2026/2/6 16:00
 * @since 1.0.0
 */
public class ExtensionFactoryTest {

    @Before
    public void setUp() {
    }

    @Test
    public void testExtensionFactoryIsInterface() {
        assertTrue("ExtensionFactory应该是接口", ExtensionFactory.class.isInterface());
    }

    @Test
    public void testExtensionFactoryHasSPIAnnotation() {
        assertNotNull("ExtensionFactory应该有SPI注解", 
                     ExtensionFactory.class.getAnnotation(com.softmegatron.shared.meta.commons.extension.annotation.Spi.class));
    }

    @Test
    public void testSPIAnnotationValue() {
        com.softmegatron.shared.meta.commons.extension.annotation.Spi annotation = 
            ExtensionFactory.class.getAnnotation(com.softmegatron.shared.meta.commons.extension.annotation.Spi.class);
        
        assertEquals("SPI注解的value应该正确", "spi", annotation.value());
        System.out.println("ExtensionFactory SPI value: " + annotation.value());
    }

    @Test
    public void testExtensionFactoryHasGetMethod() throws NoSuchMethodException {
        assertNotNull("ExtensionFactory应该有getExtension方法", 
                     ExtensionFactory.class.getMethod("getExtension", String.class, Class.class));
    }

    @Test
    public void testExtensionFactoryMethodSignature() throws NoSuchMethodException {
        var method = ExtensionFactory.class.getMethod("getExtension", String.class, Class.class);
        
        assertEquals("getExtension方法应该返回泛型类型", Object.class, method.getReturnType());
        assertTrue("getExtension方法应该是泛型方法", 
                 method.getTypeParameters().length > 0);
    }

    @Test
    public void testExtensionFactoryMethodParameterTypes() throws NoSuchMethodException {
        var method = ExtensionFactory.class.getMethod("getExtension", String.class, Class.class);
        Class<?>[] parameterTypes = method.getParameterTypes();
        
        assertEquals("getExtension方法应该有2个参数", 2, parameterTypes.length);
        assertEquals("第一个参数应该是String", String.class, parameterTypes[0]);
        assertEquals("第二个参数应该是Class", Class.class, parameterTypes[1]);
    }

    @Test
    public void testSpiExtensionFactoryImplementsExtensionFactory() {
        assertTrue("SpiExtensionFactory应该实现ExtensionFactory接口", 
                   SpiExtensionFactory.class.getInterfaces() != null);
        
        boolean implementsInterface = false;
        for (Class<?> iface : SpiExtensionFactory.class.getInterfaces()) {
            if (iface == ExtensionFactory.class) {
                implementsInterface = true;
                break;
            }
        }
        
        assertTrue("SpiExtensionFactory应该实现ExtensionFactory", implementsInterface);
    }

    @Test
    public void testSpiExtensionFactoryIsPublic() {
        assertTrue("SpiExtensionFactory应该是public类", 
                   java.lang.reflect.Modifier.isPublic(SpiExtensionFactory.class.getModifiers()));
    }

    @Test
    public void testSpiExtensionFactoryNotAbstract() {
        assertFalse("SpiExtensionFactory不应该是抽象类", 
                    java.lang.reflect.Modifier.isAbstract(SpiExtensionFactory.class.getModifiers()));
    }

    @Test
    public void testSpiExtensionFactoryHasGetMethod() throws NoSuchMethodException {
        assertNotNull("SpiExtensionFactory应该有getExtension方法", 
                     SpiExtensionFactory.class.getMethod("getExtension", String.class, Class.class));
    }

    @Test
    public void testSpiExtensionFactoryGetMethodSignature() throws NoSuchMethodException {
        var method = SpiExtensionFactory.class.getMethod("getExtension", String.class, Class.class);
        
        assertEquals("getExtension方法应该返回泛型类型", Object.class, method.getReturnType());
        assertTrue("getExtension方法应该是public方法", 
                 java.lang.reflect.Modifier.isPublic(method.getModifiers()));
    }

    @Test
    public void testSpiExtensionFactoryGetAnnotation() throws NoSuchMethodException {
        var method = SpiExtensionFactory.class.getMethod("getExtension", String.class, Class.class);
        
        assertNotNull("方法注解不应该为null", method.getAnnotations());
        System.out.println("SpiExtensionFactory.getExtension annotations: " + 
                        java.util.Arrays.toString(method.getAnnotations()));
    }

    @SuppressWarnings("unused")
    private boolean hasAnnotation(java.lang.reflect.AnnotatedElement element, 
                                 Class<? extends Annotation> annotationClass) {
        return element.getAnnotation(annotationClass) != null;
    }
}
