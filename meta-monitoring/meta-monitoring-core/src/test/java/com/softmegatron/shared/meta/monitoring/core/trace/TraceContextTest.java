package com.softmegatron.shared.meta.monitoring.core.trace;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * TraceContext 测试类
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @since 1.0.0
 */
public class TraceContextTest {

    @Test
    public void testOfTraceIdAndSpanId() {
        TraceContext context = TraceContext.of("trace123", "span456");
        assertEquals("trace123", context.getTraceId());
        assertEquals("span456", context.getSpanId());
        assertNull(context.getParentSpanId());
    }

    @Test
    public void testOfWithParentSpanId() {
        TraceContext context = TraceContext.of("trace123", "span456", "parent789");
        assertEquals("trace123", context.getTraceId());
        assertEquals("span456", context.getSpanId());
        assertEquals("parent789", context.getParentSpanId());
    }

    @Test
    public void testFromMap() {
        Map<String, String> map = new HashMap<>();
        map.put("traceId", "trace123");
        map.put("spanId", "span456");
        map.put("parentSpanId", "parent789");
        map.put("customTag", "tagValue");
        
        TraceContext context = TraceContext.fromMap(map);
        
        assertEquals("trace123", context.getTraceId());
        assertEquals("span456", context.getSpanId());
        assertEquals("parent789", context.getParentSpanId());
        assertEquals("tagValue", context.getTag("customTag"));
    }

    @Test
    public void testToMap() {
        TraceContext context = TraceContext.of("trace123", "span456", "parent789");
        context.setTag("customTag", "tagValue");
        
        Map<String, String> map = context.toMap();
        
        assertEquals("trace123", map.get("traceId"));
        assertEquals("span456", map.get("spanId"));
        assertEquals("parent789", map.get("parentSpanId"));
        assertEquals("tagValue", map.get("customTag"));
    }

    @Test
    public void testSetAndGetTag() {
        TraceContext context = TraceContext.of("trace123", "span456");
        assertNull(context.getTag("key"));
        
        context.setTag("key", "value");
        assertEquals("value", context.getTag("key"));
    }

    @Test
    public void testGetTags() {
        TraceContext context = TraceContext.of("trace123", "span456");
        context.setTag("key1", "value1");
        context.setTag("key2", "value2");
        
        Map<String, String> tags = context.getTags();
        assertEquals(2, tags.size());
        assertEquals("value1", tags.get("key1"));
        assertEquals("value2", tags.get("key2"));
    }

    @Test
    public void testGetTagsReturnsUnmodifiable() {
        TraceContext context = TraceContext.of("trace123", "span456");
        Map<String, String> tags = context.getTags();
        
        try {
            tags.put("newKey", "newValue");
            fail("Should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testToString() {
        TraceContext context = TraceContext.of("trace123", "span456");
        String str = context.toString();
        assertTrue(str.contains("trace123"));
        assertTrue(str.contains("span456"));
    }
}
