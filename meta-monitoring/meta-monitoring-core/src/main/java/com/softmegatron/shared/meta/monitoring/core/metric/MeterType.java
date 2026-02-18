package com.softmegatron.shared.meta.monitoring.core.metric;

/**
 * 指标类型
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @since 1.0.0
 */
public enum MeterType {

    COUNTER("counter", "计数器"),
    GAUGE("gauge", "仪表盘"),
    TIMER("timer", "计时器"),
    HISTOGRAM("histogram", "直方图");

    private final String code;
    private final String desc;

    MeterType(String code, String desc) {
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
