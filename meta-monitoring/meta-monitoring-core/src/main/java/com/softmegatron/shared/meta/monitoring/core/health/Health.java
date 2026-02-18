package com.softmegatron.shared.meta.monitoring.core.health;

import java.util.HashMap;
import java.util.Map;

/**
 * 健康检查结果
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @since 1.0.0
 */
public class Health {

    private final HealthStatus status;
    private final Map<String, Object> details;
    private final Throwable error;

    private Health(HealthStatus status, Map<String, Object> details, Throwable error) {
        this.status = status;
        this.details = details != null ? new HashMap<>(details) : new HashMap<>();
        this.error = error;
    }

    public HealthStatus getStatus() {
        return status;
    }

    public Map<String, Object> getDetails() {
        return new HashMap<>(details);
    }

    public Throwable getError() {
        return error;
    }

    public boolean isUp() {
        return HealthStatus.UP.equals(status);
    }

    public boolean isDown() {
        return HealthStatus.DOWN.equals(status);
    }

    public static Builder unknown() {
        return new Builder(HealthStatus.UNKNOWN);
    }

    public static Builder up() {
        return new Builder(HealthStatus.UP);
    }

    public static Builder down() {
        return new Builder(HealthStatus.DOWN);
    }

    public static Builder down(Throwable error) {
        return new Builder(HealthStatus.DOWN).withError(error);
    }

    public static Builder outOfService() {
        return new Builder(HealthStatus.OUT_OF_SERVICE);
    }

    public static class Builder {
        private final HealthStatus status;
        private final Map<String, Object> details = new HashMap<>();
        private Throwable error;

        public Builder(HealthStatus status) {
            this.status = status;
        }

        public Builder withDetail(String key, Object value) {
            this.details.put(key, value);
            return this;
        }

        public Builder withDetails(Map<String, Object> details) {
            this.details.putAll(details);
            return this;
        }

        public Builder withError(Throwable error) {
            this.error = error;
            return this;
        }

        public Health build() {
            return new Health(status, details, error);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Health{status=");
        sb.append(status);
        if (!details.isEmpty()) {
            sb.append(", details=").append(details);
        }
        if (error != null) {
            sb.append(", error=").append(error.getMessage());
        }
        sb.append("}");
        return sb.toString();
    }
}
