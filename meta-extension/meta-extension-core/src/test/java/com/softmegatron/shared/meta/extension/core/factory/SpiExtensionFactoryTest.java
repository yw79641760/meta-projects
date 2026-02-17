package com.softmegatron.shared.meta.extension.core.factory;

import com.softmegatron.shared.meta.extension.core.TestService;
import com.softmegatron.shared.meta.extension.core.loader.ExtensionLoader;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * SpiExtensionFactory 测试类
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @since 1.0.0
 */
public class SpiExtensionFactoryTest {

    @Test
    public void testGetExtension() {
        SpiExtensionFactory factory = new SpiExtensionFactory();
        TestService service = factory.getExtension(TestService.class, "default");
        assertNotNull(service);
    }

    @Test
    public void testGetExtensionWithCustomKey() {
        SpiExtensionFactory factory = new SpiExtensionFactory();
        TestService service = factory.getExtension(TestService.class, "custom");
        assertNotNull(service);
    }

    @Test
    public void testGetExtensionWithInvalidKey() {
        SpiExtensionFactory factory = new SpiExtensionFactory();
        TestService service = factory.getExtension(TestService.class, "invalid");
        assertNull(service);
    }

    @Test
    public void testGetExtensionWithNullType() {
        SpiExtensionFactory factory = new SpiExtensionFactory();
        Object service = factory.getExtension(null, "default");
        assertNull(service);
    }

    @Test
    public void testGetExtensionWithNullKey() {
        SpiExtensionFactory factory = new SpiExtensionFactory();
        TestService service = factory.getExtension(TestService.class, null);
        assertNull(service);
    }

    @Test
    public void testGetExtensionWithEmptyKey() {
        SpiExtensionFactory factory = new SpiExtensionFactory();
        TestService service = factory.getExtension(TestService.class, "");
        assertNull(service);
    }

    @Test
    public void testGetExtensionWithNonSpiType() {
        SpiExtensionFactory factory = new SpiExtensionFactory();
        Object service = factory.getExtension(Runnable.class, "test");
        assertNull(service);
    }

    @Test
    public void testGetExtensionWithNonInterfaceType() {
        SpiExtensionFactory factory = new SpiExtensionFactory();
        Object service = factory.getExtension(String.class, "test");
        assertNull(service);
    }

    @Test
    public void testGetOrder() {
        SpiExtensionFactory factory = new SpiExtensionFactory();
        assertEquals(100, factory.getOrder());
    }

    @Test
    public void testSingletonBehavior() {
        SpiExtensionFactory factory = new SpiExtensionFactory();
        TestService service1 = factory.getExtension(TestService.class, "default");
        TestService service2 = factory.getExtension(TestService.class, "default");
        assertSame("SPI扩展默认为单例", service1, service2);
    }

    @Test
    public void testFactoryImplementsExtensionFactory() {
        SpiExtensionFactory factory = new SpiExtensionFactory();
        assertTrue(factory instanceof ExtensionFactory);
    }
}
