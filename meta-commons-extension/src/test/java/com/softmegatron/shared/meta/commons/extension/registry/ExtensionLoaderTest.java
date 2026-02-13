package com.softmegatron.shared.meta.commons.extension.registry;

import com.softmegatron.shared.meta.commons.extension.annotation.Spi;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * ExtensionLoader测试类
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 2026/2/13
 */
public class ExtensionLoaderTest {

    @Spi("defaultKey")
    public interface TestExtension {
        String getName();
    }

    @Spi(value = "", path = "META-INF/extensions")
    public interface TestExtensionWithoutDefault {
        String getName();
    }

    private ExtensionLoader<TestExtension> loaderWithDefault;
    private ExtensionLoader<TestExtensionWithoutDefault> loaderWithoutDefault;

    @Before
    public void setUp() {
        loaderWithDefault = new ExtensionLoader<>(TestExtension.class);
        loaderWithoutDefault = new ExtensionLoader<>(TestExtensionWithoutDefault.class);
    }

    @Test
    public void testGetExtensionWithInvalidKey() {
        // 测试传入无效key的情况
        TestExtension extension = loaderWithDefault.getExtension("");
        assertNull("空key应该返回null", extension);
        
        extension = loaderWithDefault.getExtension(null);
        assertNull("null key应该返回null", extension);
    }
    
    @Test
    public void testGetDefaultExtension() {
        TestExtension extension = loaderWithDefault.getDefaultExtension();
        System.out.println("Default extension: " + extension);
        // 在没有配置文件的情况下应该返回null
        assertNull("默认扩展应该为null", extension);
    }
}
