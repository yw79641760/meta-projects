package com.softmegatron.shared.meta.extension.core.loader;

import com.softmegatron.shared.meta.extension.core.TestService;
import com.softmegatron.shared.meta.extension.core.exception.ExtensionException;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * ExtensionLoader 测试类
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @since 1.0.0
 */
public class ExtensionLoaderTest {

    @Test
    public void testGetLoader() {
        ExtensionLoader<TestService> loader = ExtensionLoader.getLoader(TestService.class);
        assertNotNull(loader);
    }

    @Test
    public void testGetLoaderReturnsSameInstance() {
        ExtensionLoader<TestService> loader1 = ExtensionLoader.getLoader(TestService.class);
        ExtensionLoader<TestService> loader2 = ExtensionLoader.getLoader(TestService.class);
        assertSame("相同类型应返回相同loader", loader1, loader2);
    }

    @Test
    public void testGetExtension() {
        ExtensionLoader<TestService> loader = ExtensionLoader.getLoader(TestService.class);
        TestService service = loader.getExtension("default");
        assertNotNull(service);
        assertEquals("Hello, World (default)", service.sayHello("World"));
    }

    @Test
    public void testGetExtensionWithCustomKey() {
        ExtensionLoader<TestService> loader = ExtensionLoader.getLoader(TestService.class);
        TestService service = loader.getExtension("custom");
        assertNotNull(service);
        assertEquals("Hello, Test (custom)", service.sayHello("Test"));
    }

    @Test
    public void testGetExtensionWithInvalidKey() {
        ExtensionLoader<TestService> loader = ExtensionLoader.getLoader(TestService.class);
        TestService service = loader.getExtension("invalid");
        assertNull(service);
    }

    @Test
    public void testGetExtensionWithNullKey() {
        ExtensionLoader<TestService> loader = ExtensionLoader.getLoader(TestService.class);
        TestService service = loader.getExtension(null);
        assertNull(service);
    }

    @Test
    public void testGetExtensionWithEmptyKey() {
        ExtensionLoader<TestService> loader = ExtensionLoader.getLoader(TestService.class);
        TestService service = loader.getExtension("");
        assertNull(service);
    }

    @Test
    public void testGetDefaultExtension() {
        ExtensionLoader<TestService> loader = ExtensionLoader.getLoader(TestService.class);
        TestService service = loader.getDefaultExtension();
        assertNotNull(service);
    }

    @Test
    public void testGetExtensionOrDefault() {
        ExtensionLoader<TestService> loader = ExtensionLoader.getLoader(TestService.class);
        TestService service = loader.getExtensionOrDefault("invalid");
        assertNotNull(service);
    }

    @Test
    public void testGetExtensionOrDefaultWithValidKey() {
        ExtensionLoader<TestService> loader = ExtensionLoader.getLoader(TestService.class);
        TestService service = loader.getExtensionOrDefault("custom");
        assertNotNull(service);
        assertEquals("Hello, Test (custom)", service.sayHello("Test"));
    }

    @Test
    public void testHasExtension() {
        ExtensionLoader<TestService> loader = ExtensionLoader.getLoader(TestService.class);
        assertTrue(loader.hasExtension("default"));
        assertTrue(loader.hasExtension("custom"));
        assertFalse(loader.hasExtension("invalid"));
    }

    @Test
    public void testHasExtensionWithNullKey() {
        ExtensionLoader<TestService> loader = ExtensionLoader.getLoader(TestService.class);
        assertFalse(loader.hasExtension(null));
    }

    @Test
    public void testHasExtensionWithEmptyKey() {
        ExtensionLoader<TestService> loader = ExtensionLoader.getLoader(TestService.class);
        assertFalse(loader.hasExtension(""));
    }

    @Test
    public void testGetExtensionKeys() {
        ExtensionLoader<TestService> loader = ExtensionLoader.getLoader(TestService.class);
        Set<String> keys = loader.getExtensionKeys();
        assertNotNull(keys);
        assertEquals(2, keys.size());
        assertTrue(keys.contains("default"));
        assertTrue(keys.contains("custom"));
    }

    @Test
    public void testSingletonScope() {
        ExtensionLoader<TestService> loader = ExtensionLoader.getLoader(TestService.class);
        TestService service1 = loader.getExtension("default");
        TestService service2 = loader.getExtension("default");
        assertSame(service1, service2);
    }

    @Test(expected = ExtensionException.class)
    public void testGetLoaderWithNullType() {
        ExtensionLoader.getLoader(null);
    }

    @Test(expected = ExtensionException.class)
    public void testGetLoaderWithNonInterface() {
        ExtensionLoader.getLoader(String.class);
    }

    @Test(expected = ExtensionException.class)
    public void testGetLoaderWithoutSpiAnnotation() {
        ExtensionLoader.getLoader(Runnable.class);
    }
}
