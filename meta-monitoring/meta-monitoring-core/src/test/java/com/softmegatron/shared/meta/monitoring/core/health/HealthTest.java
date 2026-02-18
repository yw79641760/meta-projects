package com.softmegatron.shared.meta.monitoring.core.health;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Health 测试类
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @since 1.0.0
 */
public class HealthTest {

    @Test
    public void testUp() {
        Health health = Health.up().build();
        assertEquals(HealthStatus.UP, health.getStatus());
        assertTrue(health.isUp());
        assertFalse(health.isDown());
        assertNull(health.getError());
    }

    @Test
    public void testDown() {
        Health health = Health.down().build();
        assertEquals(HealthStatus.DOWN, health.getStatus());
        assertFalse(health.isUp());
        assertTrue(health.isDown());
    }

    @Test
    public void testDownWithError() {
        Exception error = new RuntimeException("Test error");
        Health health = Health.down(error).build();
        assertEquals(HealthStatus.DOWN, health.getStatus());
        assertEquals(error, health.getError());
    }

    @Test
    public void testUnknown() {
        Health health = Health.unknown().build();
        assertEquals(HealthStatus.UNKNOWN, health.getStatus());
    }

    @Test
    public void testOutOfService() {
        Health health = Health.outOfService().build();
        assertEquals(HealthStatus.OUT_OF_SERVICE, health.getStatus());
    }

    @Test
    public void testWithDetail() {
        Health health = Health.up()
                .withDetail("version", "1.0.0")
                .withDetail("connections", 10)
                .build();
        
        assertEquals(HealthStatus.UP, health.getStatus());
        assertEquals("1.0.0", health.getDetails().get("version"));
        assertEquals(10, health.getDetails().get("connections"));
    }

    @Test
    public void testWithDetails() {
        java.util.Map<String, Object> details = new java.util.HashMap<>();
        details.put("key1", "value1");
        details.put("key2", "value2");
        
        Health health = Health.up().withDetails(details).build();
        
        assertEquals("value1", health.getDetails().get("key1"));
        assertEquals("value2", health.getDetails().get("key2"));
    }

    @Test
    public void testGetDetailsReturnsCopy() {
        Health health = Health.up().withDetail("key", "value").build();
        health.getDetails().put("newKey", "newValue");
        
        assertNull(health.getDetails().get("newKey"));
    }

    @Test
    public void testToString() {
        Health health = Health.up().withDetail("key", "value").build();
        String str = health.toString();
        assertTrue(str.contains("UP"));
        assertTrue(str.contains("key"));
        assertTrue(str.contains("value"));
    }
}
