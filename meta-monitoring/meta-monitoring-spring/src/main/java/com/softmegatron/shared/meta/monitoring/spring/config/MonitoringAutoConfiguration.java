package com.softmegatron.shared.meta.monitoring.spring.config;

import com.softmegatron.shared.meta.monitoring.core.health.HealthRegistry;
import com.softmegatron.shared.meta.monitoring.core.metric.MeterRegistry;
import com.softmegatron.shared.meta.monitoring.micrometer.MicrometerMeterRegistry;
import com.softmegatron.shared.meta.monitoring.spring.health.DefaultHealthRegistry;
import io.micrometer.core.instrument.Metrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * Meta Monitoring 自动配置
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @since 1.0.0
 */
@AutoConfiguration
@ConditionalOnClass(name = "io.micrometer.core.instrument.MeterRegistry")
public class MonitoringAutoConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonitoringAutoConfiguration.class);

    @Bean
    @ConditionalOnMissingBean(MeterRegistry.class)
    public MeterRegistry meterRegistry(io.micrometer.core.instrument.MeterRegistry micrometerRegistry) {
        LOGGER.info("Creating MicrometerMeterRegistry");
        return new MicrometerMeterRegistry(micrometerRegistry);
    }

    @Bean
    @ConditionalOnMissingBean(MeterRegistry.class)
    public MeterRegistry defaultMeterRegistry() {
        LOGGER.info("Creating default MicrometerMeterRegistry with global registry");
        return new MicrometerMeterRegistry(Metrics.globalRegistry);
    }

    @Bean
    @ConditionalOnMissingBean(HealthRegistry.class)
    public HealthRegistry healthRegistry() {
        LOGGER.info("Creating DefaultHealthRegistry");
        return new DefaultHealthRegistry();
    }

    @Bean
    public HealthRegistryProcessor healthRegistryProcessor(HealthRegistry healthRegistry,
                                                           @Autowired(required = false) List<com.softmegatron.shared.meta.monitoring.core.health.HealthIndicator> indicators) {
        return new HealthRegistryProcessor(healthRegistry, indicators);
    }
}
