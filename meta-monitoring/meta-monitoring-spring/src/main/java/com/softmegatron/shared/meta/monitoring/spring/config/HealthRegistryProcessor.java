package com.softmegatron.shared.meta.monitoring.spring.config;

import com.softmegatron.shared.meta.monitoring.core.health.HealthIndicator;
import com.softmegatron.shared.meta.monitoring.core.health.HealthRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Health Registry Processor
 * <p>
 * 自动注册所有 HealthIndicator Bean
 * </p>
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @since 1.0.0
 */
public class HealthRegistryProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(HealthRegistryProcessor.class);

    private final HealthRegistry healthRegistry;

    public HealthRegistryProcessor(HealthRegistry healthRegistry, List<HealthIndicator> indicators) {
        this.healthRegistry = healthRegistry;
        if (indicators != null && !indicators.isEmpty()) {
            for (HealthIndicator indicator : indicators) {
                healthRegistry.register(indicator);
                LOGGER.info("Registered HealthIndicator: {}", indicator.getName());
            }
        }
    }

    public HealthRegistry getHealthRegistry() {
        return healthRegistry;
    }
}
