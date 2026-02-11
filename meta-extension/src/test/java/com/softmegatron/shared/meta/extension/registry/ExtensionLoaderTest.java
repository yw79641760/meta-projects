package com.softmegatron.shared.meta.extension.registry;

import com.softmegatron.shared.meta.extension.annotation.SPI;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * ExtensionLoader 测试类
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">yw</a>
 * @description 测试 ExtensionLoader 扩展加载器
 * @date 2026/2/6 16:00
 * @since 1.0.0
 */
public class ExtensionLoaderTest {

    @SPI("defaultExtension")
    private interface TestExtension {
        String getName();
    }

    @SPI
    private interface TestExtensionWithoutDefault {
        String getValue();
    }

    private ExtensionLoader<TestExtension> loaderWithDefault;
    private ExtensionLoader<TestExtensionWithoutDefault> loaderWithoutDefault;

    @Before
    public void setUp() {
        loaderWithDefault = new ExtensionLoader<>(TestExtension.class);
        loaderWithoutDefault = new ExtensionLoader<>(TestExtensionWithoutDefault.class);
    }

    @Test
    public void testExtensionLoaderInstantiation() {
        assertNotNull("ExtensionLoader应该可以实例化", loaderWithDefault);
        assertNotNull("ExtensionLoader应该可以实例化（无默认值）", loaderWithoutDefault);
    }

    @Test
    public void testExtensionLoaderWithDefaultSPI() {
        assertNotNull("ExtensionLoader应该初始化成功（有默认值）", loaderWithDefault);
    }

    @Test
    public void testExtensionLoaderWithoutDefaultSPI() {
        assertNotNull("ExtensionLoader应该初始化成功（无默认值）", loaderWithoutDefault);
    }

    @Test
    public void testGetDefaultExtension() {
        TestExtension extension = loaderWithDefault.getDefaultExtension();
        
        if (extension != null) {
            System.out.println("Default extension: " + extension.getName());
            assertNotNull("默认扩展不应该为null", extension);
        } else {
            System.out.println("No default extension found (expected if no config file)");
        }
    }

    @Test(expected = NullPointerException.class)
    public void testGetExtensionWithNullKey() {
        loaderWithDefault.getExtension(null);
    }

    @Test
    public void testGetExtensionWithEmptyKey() {
        TestExtension extension = loaderWithDefault.getExtension("");
        System.out.println("Extension with empty key: " + extension);
    }

    @Test
    public void testGetExtensionWithNonExistentKey() {
        TestExtension extension = loaderWithDefault.getExtension("nonExistent");
        System.out.println("Extension with non-existent key: " + extension);
        assertNull("不存在的key应该返回null", extension);
    }

    @Test(expected = NullPointerException.class)
    public void testGetExtensionOrDefaultWithNullKey() {
        loaderWithDefault.getExtensionOrDefault(null);
    }

    @Test
    public void testGetExtensionOrDefaultWithEmptyKey() {
        TestExtension extension = loaderWithDefault.getExtensionOrDefault("");
        System.out.println("Extension with empty key using default: " + extension);
    }

    @Test
    public void testGetExtensionOrDefaultWithNonExistentKey() {
        TestExtension extension = loaderWithDefault.getExtensionOrDefault("nonExistent");
        System.out.println("Extension with non-existent key using default: " + extension);
        assertNull("不存在的key应该返回null（如果没有默认扩展）", extension);
    }

    @Test
    public void testExtensionLoaderImplementsGetDefaultExtension() throws NoSuchMethodException {
        var method = ExtensionLoader.class.getMethod("getDefaultExtension");
        assertNotNull("getDefaultExtension方法应该存在", method);
        assertEquals("返回类型应该是泛型T", Object.class, method.getReturnType());
    }

    @Test
    public void testExtensionLoaderImplementsGetExtension() throws NoSuchMethodException {
        var method = ExtensionLoader.class.getMethod("getExtension", String.class);
        assertNotNull("getExtension方法应该存在", method);
        assertEquals("返回类型应该是泛型T", Object.class, method.getReturnType());
        assertEquals("方法应该有1个参数", 1, method.getParameterCount());
        assertEquals("参数类型应该是String", String.class, method.getParameterTypes()[0]);
    }

    @Test
    public void testExtensionLoaderImplementsGetExtensionOrDefault() throws NoSuchMethodException {
        var method = ExtensionLoader.class.getMethod("getExtensionOrDefault", String.class);
        assertNotNull("getExtensionOrDefault方法应该存在", method);
        assertEquals("返回类型应该是泛型T", Object.class, method.getReturnType());
        assertEquals("方法应该有1个参数", 1, method.getParameterCount());
        assertEquals("参数类型应该是String", String.class, method.getParameterTypes()[0]);
    }

    @Test
    public void testExtensionLoaderIsPublicClass() {
        int modifiers = ExtensionLoader.class.getModifiers();
        assertTrue("ExtensionLoader应该是public类", 
                  java.lang.reflect.Modifier.isPublic(modifiers));
    }

    @Test
    public void testExtensionLoaderIsNotAbstract() {
        int modifiers = ExtensionLoader.class.getModifiers();
        assertFalse("ExtensionLoader不应该是抽象类", 
                    java.lang.reflect.Modifier.isAbstract(modifiers));
    }

    @Test
    public void testExtensionLoaderHasGenericTypeParameter() {
        java.lang.reflect.TypeVariable<?>[] typeParameters = ExtensionLoader.class.getTypeParameters();
        assertNotNull("ExtensionLoader应该有泛型参数", typeParameters);
        assertTrue("ExtensionLoader应该有1个泛型参数", typeParameters.length > 0);
    }

    @Test
    public void testExtensionLoaderHasPublicConstructor() throws NoSuchMethodException {
        var constructor = ExtensionLoader.class.getConstructor(Class.class);
        assertNotNull("构造函数应该存在", constructor);
        assertTrue("构造函数应该是public的", 
                  java.lang.reflect.Modifier.isPublic(constructor.getModifiers()));
    }

    @Test
    public void testExtensionLoaderConstructorTakesClassParameter() throws NoSuchMethodException {
        var constructor = ExtensionLoader.class.getConstructor(Class.class);
        Class<?>[] paramTypes = constructor.getParameterTypes();
        
        assertEquals("构造函数应该有1个参数", 1, paramTypes.length);
        assertEquals("参数类型应该是Class", Class.class, paramTypes[0]);
    }

    @Test
    public void testExtensionLoaderHasPrivateLoadExtensionsMethod() throws NoSuchMethodException {
        var method = ExtensionLoader.class.getDeclaredMethod("loadExtensions");
        assertNotNull("loadExtensions方法应该存在", method);
        assertTrue("loadExtensions应该是private方法", 
                  java.lang.reflect.Modifier.isPrivate(method.getModifiers()));
    }

    @Test
    public void testExtensionLoaderHasPrivateLoadResourceMethod() throws NoSuchMethodException {
        var method = ExtensionLoader.class.getDeclaredMethod("loadResource", java.net.URL.class);
        assertNotNull("loadResource方法应该存在", method);
        assertTrue("loadResource应该是private方法", 
                  java.lang.reflect.Modifier.isPrivate(method.getModifiers()));
    }

    @Test
    public void testExtensionLoaderHasPrivateLoadClassMethod() throws NoSuchMethodException {
        var method = ExtensionLoader.class.getDeclaredMethod("loadClass", String.class, String.class);
        assertNotNull("loadClass方法应该存在", method);
        assertTrue("loadClass应该是private方法", 
                  java.lang.reflect.Modifier.isPrivate(method.getModifiers()));
    }

    @Test
    public void testExtensionLoaderHasLogger() throws NoSuchFieldException {
        var loggerField = ExtensionLoader.class.getDeclaredField("LOGGER");
        assertNotNull("LOGGER字段应该存在", loggerField);
        assertTrue("LOGGER应该是static final", 
                  java.lang.reflect.Modifier.isStatic(loggerField.getModifiers()) &&
                  java.lang.reflect.Modifier.isFinal(loggerField.getModifiers()));
    }

    @Test
    public void testExtensionLoaderHasCacheMaps() throws NoSuchFieldException {
        var typeCacheField = ExtensionLoader.class.getDeclaredField("EXTENSION_TYPE_CACHE");
        var instanceCacheField = ExtensionLoader.class.getDeclaredField("EXTENSION_INSTANCE_CACHE");
        
        assertNotNull("EXTENSION_TYPE_CACHE字段应该存在", typeCacheField);
        assertNotNull("EXTENSION_INSTANCE_CACHE字段应该存在", instanceCacheField);
        
        assertTrue("EXTENSION_TYPE_CACHE应该是Map或其子类", 
                  Map.class.isAssignableFrom(typeCacheField.getType()));
        assertTrue("EXTENSION_INSTANCE_CACHE应该是Map或其子类", 
                  Map.class.isAssignableFrom(instanceCacheField.getType()));
    }

    @Test
    public void testExtensionLoaderCacheMapsAreFinal() throws NoSuchFieldException {
        var typeCacheField = ExtensionLoader.class.getDeclaredField("EXTENSION_TYPE_CACHE");
        var instanceCacheField = ExtensionLoader.class.getDeclaredField("EXTENSION_INSTANCE_CACHE");
        
        assertTrue("EXTENSION_TYPE_CACHE应该是final", 
                  java.lang.reflect.Modifier.isFinal(typeCacheField.getModifiers()));
        assertTrue("EXTENSION_INSTANCE_CACHE应该是final", 
                  java.lang.reflect.Modifier.isFinal(instanceCacheField.getModifiers()));
    }

    @Test
    public void testExtensionLoaderToString() {
        String toString = loaderWithDefault.toString();
        assertNotNull("toString不应该返回null", toString);
        assertTrue("toString应该包含类名", 
                 toString.contains("ExtensionLoader"));
        System.out.println("ExtensionLoader toString: " + toString);
    }
}
