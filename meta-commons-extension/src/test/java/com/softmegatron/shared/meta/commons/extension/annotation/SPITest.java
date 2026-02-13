package com.softmegatron.shared.meta.commons.extension.annotation;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * SPI 测试类
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">yw</a>
 * @description 测试 SPI 注解
 * @date 2026/2/6 16:00
 * @since 1.0.0
 */
public class SpiTest {

    @Spi("testExtension")
    private static class TestExtension {
    }

    @Spi
    private static class DefaultExtension {
    }

    @Spi(value = "customValue", path = "custom/path")
    private static class CustomExtension {
    }

    @Before
    public void setUp() {
    }

    @Test
    public void testSPIAnnotationExists() {
        assertNotNull("SPI注解应该存在", TestExtension.class.getAnnotation(Spi.class));
    }

    @Test
    public void testSPIAnnotationValue() {
        Spi annotation = TestExtension.class.getAnnotation(Spi.class);
        String value = annotation.value();
        
        assertEquals("value应该正确", "testExtension", value);
        System.out.println("TestExtension SPI value: " + value);
    }

    @Test
    public void testSPIAnnotationDefaultPath() {
        Spi annotation = TestExtension.class.getAnnotation(Spi.class);
        String path = annotation.path();
        
        assertEquals("path默认值应该正确", "META-INF/extensions", path);
        System.out.println("TestExtension SPI path: " + path);
    }

    @Test
    public void testSPIAnnotationEmptyValue() {
        Spi annotation = DefaultExtension.class.getAnnotation(Spi.class);
        String value = annotation.value();
        
        assertEquals("value默认值应该为空字符串", "", value);
        System.out.println("DefaultExtension SPI value: '" + value + "'");
    }

    @Test
    public void testSPIAnnotationCustomPath() {
        Spi annotation = CustomExtension.class.getAnnotation(Spi.class);
        String path = annotation.path();
        
        assertEquals("自定义path应该正确", "custom/path", path);
        System.out.println("CustomExtension SPI path: " + path);
    }

    @Test
    public void testSPIAnnotationRetention() {
        Spi annotation = TestExtension.class.getAnnotation(Spi.class);
        
        assertNotNull("注解应该在运行时可访问", annotation);
    }

    @Test
    public void testSPIAnnotationCustomValue() {
        Spi annotation = CustomExtension.class.getAnnotation(Spi.class);
        String value = annotation.value();
        
        assertEquals("自定义value应该正确", "customValue", value);
        System.out.println("CustomExtension SPI value: " + value);
    }

    @Test
    public void testSPIAnnotationInterface() {
        assertTrue("SPI应该是注解接口", Spi.class.isAnnotation());
    }

    @Test
    public void testSPIAnnotationTypeUsage() {
        Spi annotation = TestExtension.class.getAnnotation(Spi.class);
        
        assertNotNull("SPI注解应该能应用于类上", annotation);
    }

    @Test
    public void testSPIAnnotationMultipleClasses() {
        Spi testAnnotation = TestExtension.class.getAnnotation(Spi.class);
        Spi defaultAnnotation = DefaultExtension.class.getAnnotation(Spi.class);
        Spi customAnnotation = CustomExtension.class.getAnnotation(Spi.class);
        
        assertNotNull("TestExtension应该有SPI注解", testAnnotation);
        assertNotNull("DefaultExtension应该有SPI注解", defaultAnnotation);
        assertNotNull("CustomExtension应该有SPI注解", customAnnotation);
        
        assertEquals("TestExtension的value应该正确", "testExtension", testAnnotation.value());
        assertEquals("DefaultExtension的value应该为空", "", defaultAnnotation.value());
        assertEquals("CustomExtension的value应该正确", "customValue", customAnnotation.value());
    }
}
