package com.softmegatron.shared.meta.monitoring.spring.health;

import com.softmegatron.shared.meta.monitoring.core.health.Health;
import com.softmegatron.shared.meta.monitoring.core.health.HealthIndicator;
import com.softmegatron.shared.meta.monitoring.core.health.HealthRegistry;
import com.softmegatron.shared.meta.monitoring.core.health.HealthStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认健康检查注册表实现
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @since 1.0.0
 */
public class DefaultHealthRegistry implements HealthRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultHealthRegistry.class);

    private final Map<String, HealthIndicator> indicators = new ConcurrentHashMap<>();

    @Override
    public void register(HealthIndicator indicator) {
        register(indicator.getName(), indicator);
    }

    @Override
    public void register(String name, HealthIndicator indicator) {
        indicators.put(name, indicator);
        LOGGER.debug("Registered health indicator: {}", name);
    }

    @Override
    public void unregister(String name) {
        indicators.remove(name);
        LOGGER.debug("Unregistered health indicator: {}", name);
    }

    @Override
    public List<String> getNames() {
        return List.copyOf(indicators.keySet());
    }

    @Override
    public HealthIndicator getIndicator(String name) {
        return indicators.get(name);
    }

    @Override
    public Health check(String name) {
        HealthIndicator indicator = indicators.get(name);
        if (indicator == null) {
            return Health.unknown().withDetail("error", "Indicator not found: " + name).build();
        }
        try {
            return indicator.check();
        } catch (Exception e) {
            LOGGER.error("Health check failed for: {}", name, e);
            return Health.down(e).build();
        }
    }

    @Override
    public Map<String, Health> checkAll() {
        Map<String, Health> results = new ConcurrentHashMap<>();
        indicators.forEach((name, indicator) -> {
            try {
                results.put(name, indicator.check());
            } catch (Exception e) {
                LOGGER.error("Health check failed for: {}", name, e);
                results.put(name, Health.down(e).build());
            }
        });
        return results;
    }

    @Override
    public Health getOverallHealth() {
        Map<String, Health> results = checkAll();
        
        if (results.isEmpty()) {
            return Health.unknown().withDetail("reason", "No health indicators registered").build();
        }

        HealthStatus overallStatus = HealthStatus.UP;
        for (Health health : results.values()) {
            if (health.getStatus() == HealthStatus.DOWN) {
                overallStatus = HealthStatus.DOWN;
                break;
            }
            if (health.getStatus() == HealthStatus.OUT_OF_SERVICE && overallStatus != HealthStatus.DOWN) {
                overallStatus = HealthStatus.OUT_OF_SERVICE;
            }
            if (health.getStatus() == HealthStatus.UNKNOWN && overallStatus == HealthStatus.UP) {
                overallStatus = HealthStatus.UNKNOWN;
            }
        }

        return new Health.Builder(overallStatus)
                .withDetails(Map.of("checks", results))
                .build();
    }

    @Override
    public void clear() {
        indicators.clear();
    }
}
