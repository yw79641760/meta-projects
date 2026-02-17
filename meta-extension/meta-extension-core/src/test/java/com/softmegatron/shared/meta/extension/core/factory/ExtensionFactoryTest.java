package com.softmegatron.shared.meta.extension.core.factory;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * ExtensionFactory 接口测试类
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @since 1.0.0
 */
public class ExtensionFactoryTest {

    @Test
    public void testDefaultGetOrder() {
        ExtensionFactory factory = new ExtensionFactory() {
            @Override
            public <T> T getExtension(Class<T> type, String name) {
                return null;
            }
        };
        assertEquals(100, factory.getOrder());
    }

    @Test
    public void testCustomGetOrder() {
        ExtensionFactory factory = new ExtensionFactory() {
            @Override
            public <T> T getExtension(Class<T> type, String name) {
                return null;
            }
            
            @Override
            public int getOrder() {
                return 50;
            }
        };
        assertEquals(50, factory.getOrder());
    }

    @Test
    public void testIsInterface() {
        assertTrue(ExtensionFactory.class.isInterface());
    }

    @Test
    public void testIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(ExtensionFactory.class.getModifiers()));
    }

    @Test
    public void testGetExtensionMethodExists() throws NoSuchMethodException {
        var method = ExtensionFactory.class.getMethod("getExtension", Class.class, String.class);
        assertNotNull(method);
    }

    @Test
    public void testGetOrderMethodExists() throws NoSuchMethodException {
        var method = ExtensionFactory.class.getMethod("getOrder");
        assertNotNull(method);
        assertEquals(int.class, method.getReturnType());
    }

    @Test
    public void testGetExtensionMethodReturnType() throws NoSuchMethodException {
        var method = ExtensionFactory.class.getMethod("getExtension", Class.class, String.class);
        assertEquals(Object.class, method.getReturnType());
    }

    @Test
    public void testGetExtensionMethodParameterTypes() throws NoSuchMethodException {
        var method = ExtensionFactory.class.getMethod("getExtension", Class.class, String.class);
        var paramTypes = method.getParameterTypes();
        assertEquals(2, paramTypes.length);
        assertEquals(Class.class, paramTypes[0]);
        assertEquals(String.class, paramTypes[1]);
    }

    @Test
    public void testOrderMethodIsDefault() throws NoSuchMethodException {
        var method = ExtensionFactory.class.getMethod("getOrder");
        assertTrue(method.isDefault());
    }

    @Test
    public void testCustomFactoryImplementation() {
        ExtensionFactory customFactory = new ExtensionFactory() {
            @Override
            public <T> T getExtension(Class<T> type, String name) {
                if (type == String.class && "test".equals(name)) {
                    return type.cast("test-value");
                }
                return null;
            }
            
            @Override
            public int getOrder() {
                return 0;
            }
        };
        
        assertEquals(0, customFactory.getOrder());
        assertEquals("test-value", customFactory.getExtension(String.class, "test"));
        assertNull(customFactory.getExtension(Integer.class, "test"));
    }
}
