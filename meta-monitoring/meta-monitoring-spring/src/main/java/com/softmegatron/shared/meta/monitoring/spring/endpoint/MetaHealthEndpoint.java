package com.softmegatron.shared.meta.monitoring.spring.endpoint;

import com.softmegatron.shared.meta.monitoring.core.health.Health;
import com.softmegatron.shared.meta.monitoring.core.health.HealthRegistry;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 健康检查端点
 * <p>
 * 提供 /actuator/meta-health 端点
 * </p>
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @since 1.0.0
 */
@Component
@Endpoint(id = "meta-health")
public class MetaHealthEndpoint {

    private final HealthRegistry healthRegistry;

    public MetaHealthEndpoint(HealthRegistry healthRegistry) {
        this.healthRegistry = healthRegistry;
    }

    @ReadOperation
    public Map<String, Object> health() {
        Health health = healthRegistry.getOverallHealth();
        return Map.of(
                "status", health.getStatus().getCode(),
                "details", health.getDetails()
        );
    }

    @ReadOperation
    public Map<String, Object> healthForIndicator(@Selector String name) {
        Health health = healthRegistry.check(name);
        return Map.of(
                "name", name,
                "status", health.getStatus().getCode(),
                "details", health.getDetails()
        );
    }
}
