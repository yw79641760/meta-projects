package com.softmegatron.shared.meta.monitoring.core.health;

/**
 * 健康状态枚举
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @since 1.0.0
 */
public enum HealthStatus {

    UP("UP", "健康"),
    DOWN("DOWN", "不健康"),
    UNKNOWN("UNKNOWN", "未知"),
    OUT_OF_SERVICE("OUT_OF_SERVICE", "服务不可用");

    private final String code;
    private final String desc;

    HealthStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
