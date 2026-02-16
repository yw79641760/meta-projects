package com.softmegatron.shared.meta.extension;

import com.softmegatron.shared.meta.extension.exception.ExtensionException;
import com.softmegatron.shared.meta.extension.loader.ExtensionManager;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * ExtensionManager 测试类
 */
public class ExtensionManagerTest {

    @Test
    public void testGetExtension() {
        TestService service = ExtensionManager.getExtension(TestService.class, "default");
        assertNotNull(service);
        assertEquals("Hello, World (default)", service.sayHello("World"));
    }

    @Test
    public void testGetExtensionWithCustomKey() {
        TestService service = ExtensionManager.getExtension(TestService.class, "custom");
        assertNotNull(service);
        assertEquals("Hello, Test (custom)", service.sayHello("Test"));
    }

    @Test
    public void testGetExtensionWithInvalidKey() {
        TestService service = ExtensionManager.getExtension(TestService.class, "invalid");
        assertNull(service);
    }

    @Test
    public void testGetDefaultExtension() {
        TestService service = ExtensionManager.getDefaultExtension(TestService.class);
        assertNotNull(service);
        assertTrue(service instanceof DefaultTestServiceImpl);
    }

    @Test
    public void testGetExtensionOrDefault() {
        TestService service = ExtensionManager.getExtensionOrDefault(TestService.class, "invalid");
        assertNotNull(service);
        assertTrue(service instanceof DefaultTestServiceImpl);
    }

    @Test
    public void testHasExtension() {
        assertTrue(ExtensionManager.hasExtension(TestService.class, "default"));
        assertTrue(ExtensionManager.hasExtension(TestService.class, "custom"));
        assertFalse(ExtensionManager.hasExtension(TestService.class, "invalid"));
    }

    @Test
    public void testGetExtensionKeys() {
        Set<String> keys = ExtensionManager.getExtensionKeys(TestService.class);
        assertNotNull(keys);
        assertEquals(2, keys.size());
        assertTrue(keys.contains("default"));
        assertTrue(keys.contains("custom"));
    }

    @Test
    public void testSingletonScope() {
        TestService service1 = ExtensionManager.getExtension(TestService.class, "default");
        TestService service2 = ExtensionManager.getExtension(TestService.class, "default");
        assertSame(service1, service2);
    }

    @Test
    public void testPrototypeScope() {
        PrototypeService service1 = ExtensionManager.getExtension(PrototypeService.class, "prototype");
        PrototypeService service2 = ExtensionManager.getExtension(PrototypeService.class, "prototype");
        assertNotNull(service1);
        assertNotNull(service2);
        assertNotSame(service1, service2);
        assertNotEquals(service1.getId(), service2.getId());
    }

    @Test(expected = ExtensionException.class)
    public void testGetExtensionWithNullType() {
        ExtensionManager.getExtension(null, "default");
    }

    @Test(expected = ExtensionException.class)
    public void testGetExtensionWithNonInterface() {
        ExtensionManager.getExtension(DefaultTestServiceImpl.class, "default");
    }

    @Test
    public void testGetExtensionWithNullKey() {
        TestService service = ExtensionManager.getExtension(TestService.class, null);
        assertNull(service);
    }

    @Test
    public void testGetExtensionWithEmptyKey() {
        TestService service = ExtensionManager.getExtension(TestService.class, "");
        assertNull(service);
    }
}
