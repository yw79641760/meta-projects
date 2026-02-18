package com.softmegatron.shared.meta.monitoring.spring.health;

import com.softmegatron.shared.meta.monitoring.core.health.Health;
import com.softmegatron.shared.meta.monitoring.core.health.HealthIndicator;
import com.softmegatron.shared.meta.monitoring.core.health.HealthStatus;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * DefaultHealthRegistry 测试类
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @since 1.0.0
 */
public class DefaultHealthRegistryTest {

    private DefaultHealthRegistry registry;

    @Before
    public void setUp() {
        registry = new DefaultHealthRegistry();
    }

    @Test
    public void testRegister() {
        HealthIndicator indicator = new TestHealthIndicator("test", Health.up().build());
        registry.register(indicator);
        
        assertEquals(1, registry.getNames().size());
        assertTrue(registry.getNames().contains("test"));
    }

    @Test
    public void testRegisterWithName() {
        HealthIndicator indicator = new TestHealthIndicator("original", Health.up().build());
        registry.register("custom", indicator);
        
        assertTrue(registry.getNames().contains("custom"));
    }

    @Test
    public void testUnregister() {
        HealthIndicator indicator = new TestHealthIndicator("test", Health.up().build());
        registry.register(indicator);
        registry.unregister("test");
        
        assertFalse(registry.getNames().contains("test"));
    }

    @Test
    public void testCheck() {
        Health expectedHealth = Health.up().withDetail("version", "1.0").build();
        registry.register(new TestHealthIndicator("test", expectedHealth));
        
        Health health = registry.check("test");
        assertEquals(HealthStatus.UP, health.getStatus());
        assertEquals("1.0", health.getDetails().get("version"));
    }

    @Test
    public void testCheckNotFound() {
        Health health = registry.check("not-exist");
        assertEquals(HealthStatus.UNKNOWN, health.getStatus());
    }

    @Test
    public void testCheckException() {
        registry.register(new ExceptionHealthIndicator("test"));
        Health health = registry.check("test");
        assertEquals(HealthStatus.DOWN, health.getStatus());
        assertNotNull(health.getError());
    }

    @Test
    public void testCheckAll() {
        registry.register(new TestHealthIndicator("up1", Health.up().build()));
        registry.register(new TestHealthIndicator("up2", Health.up().build()));
        
        var results = registry.checkAll();
        assertEquals(2, results.size());
        assertEquals(HealthStatus.UP, results.get("up1").getStatus());
        assertEquals(HealthStatus.UP, results.get("up2").getStatus());
    }

    @Test
    public void testGetOverallHealthAllUp() {
        registry.register(new TestHealthIndicator("test1", Health.up().build()));
        registry.register(new TestHealthIndicator("test2", Health.up().build()));
        
        Health overall = registry.getOverallHealth();
        assertEquals(HealthStatus.UP, overall.getStatus());
    }

    @Test
    public void testGetOverallHealthOneDown() {
        registry.register(new TestHealthIndicator("up", Health.up().build()));
        registry.register(new TestHealthIndicator("down", Health.down().build()));
        
        Health overall = registry.getOverallHealth();
        assertEquals(HealthStatus.DOWN, overall.getStatus());
    }

    @Test
    public void testGetOverallHealthEmpty() {
        Health overall = registry.getOverallHealth();
        assertEquals(HealthStatus.UNKNOWN, overall.getStatus());
    }

    @Test
    public void testClear() {
        registry.register(new TestHealthIndicator("test", Health.up().build()));
        registry.clear();
        assertTrue(registry.getNames().isEmpty());
    }

    private static class TestHealthIndicator implements HealthIndicator {
        private final String name;
        private final Health health;

        TestHealthIndicator(String name, Health health) {
            this.name = name;
            this.health = health;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Health check() {
            return health;
        }
    }

    private static class ExceptionHealthIndicator implements HealthIndicator {
        private final String name;

        ExceptionHealthIndicator(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Health check() {
            throw new RuntimeException("Test exception");
        }
    }
}
