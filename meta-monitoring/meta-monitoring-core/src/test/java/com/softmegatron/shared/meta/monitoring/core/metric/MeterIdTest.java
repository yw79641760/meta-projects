package com.softmegatron.shared.meta.monitoring.core.metric;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * MeterId 测试类
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @since 1.0.0
 */
public class MeterIdTest {

    @Test
    public void testCreateWithName() {
        MeterId id = new MeterId("test.metric");
        assertEquals("test.metric", id.getName());
        assertTrue(id.getTags().isEmpty());
    }

    @Test
    public void testCreateWithTags() {
        Tag tag1 = new Tag("key1", "value1");
        Tag tag2 = new Tag("key2", "value2");
        MeterId id = new MeterId("test.metric", tag1, tag2);
        
        assertEquals("test.metric", id.getName());
        assertEquals(2, id.getTags().size());
        assertEquals("value1", id.getTag("key1"));
        assertEquals("value2", id.getTag("key2"));
    }

    @Test
    public void testWithTag() {
        MeterId id = new MeterId("test.metric");
        MeterId newId = id.withTag("key", "value");
        
        assertEquals("test.metric", newId.getName());
        assertEquals(1, newId.getTags().size());
        assertEquals("value", newId.getTag("key"));
    }

    @Test
    public void testGetTagNotFound() {
        MeterId id = new MeterId("test.metric");
        assertNull(id.getTag("not-exist"));
    }

    @Test
    public void testEqualsAndHashCode() {
        MeterId id1 = new MeterId("test.metric", new Tag("key", "value"));
        MeterId id2 = new MeterId("test.metric", new Tag("key", "value"));
        MeterId id3 = new MeterId("test.metric");
        
        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
        assertNotEquals(id1, id3);
    }

    @Test
    public void testToString() {
        MeterId id = new MeterId("test.metric", new Tag("key", "value"));
        String str = id.toString();
        assertTrue(str.contains("test.metric"));
        assertTrue(str.contains("key=value"));
    }

    @Test
    public void testToStringNoTags() {
        MeterId id = new MeterId("test.metric");
        assertEquals("test.metric", id.toString());
    }
}
