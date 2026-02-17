package com.softmegatron.shared.meta.core.pattern.chain;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * ChainContext 测试类
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @since 1.0.0
 */
public class ChainContextTest {

    @Test
    public void testPutAndGet() {
        ChainContext context = new ChainContext();
        context.put("key", "value");
        assertEquals("value", context.get("key"));
    }

    @Test
    public void testGetNonExistentKey() {
        ChainContext context = new ChainContext();
        assertNull(context.get("nonexistent"));
    }

    @Test
    public void testPutNullValue() {
        ChainContext context = new ChainContext();
        context.put("key", null);
        assertNull(context.get("key"));
    }

    @Test
    public void testPutOverridesExistingValue() {
        ChainContext context = new ChainContext();
        context.put("key", "value1");
        context.put("key", "value2");
        assertEquals("value2", context.get("key"));
    }

    @Test
    public void testGenericGet() {
        ChainContext context = new ChainContext();
        context.put("int", 123);
        context.put("string", "hello");
        context.put("double", 3.14);

        Integer intValue = context.get("int");
        String stringValue = context.get("string");
        Double doubleValue = context.get("double");

        assertEquals(Integer.valueOf(123), intValue);
        assertEquals("hello", stringValue);
        assertEquals(Double.valueOf(3.14), doubleValue);
    }

    @Test
    public void testMultipleKeys() {
        ChainContext context = new ChainContext();
        context.put("key1", "value1");
        context.put("key2", "value2");
        context.put("key3", "value3");

        assertEquals("value1", context.get("key1"));
        assertEquals("value2", context.get("key2"));
        assertEquals("value3", context.get("key3"));
    }

    @Test
    public void testComplexObject() {
        ChainContext context = new ChainContext();
        TestObject obj = new TestObject("test", 100);
        context.put("object", obj);

        TestObject retrieved = context.get("object");
        assertNotNull(retrieved);
        assertEquals("test", retrieved.name);
        assertEquals(100, retrieved.value);
    }

    private static class TestObject {
        String name;
        int value;

        TestObject(String name, int value) {
            this.name = name;
            this.value = value;
        }
    }
}
