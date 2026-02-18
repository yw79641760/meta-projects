package com.softmegatron.shared.meta.monitoring.core.metric;

/**
 * 指标基础接口
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @since 1.0.0
 */
public interface Meter {

    /**
     * 获取指标ID
     *
     * @return 指标ID
     */
    MeterId getId();

    /**
     * 获取指标类型
     *
     * @return 指标类型
     */
    MeterType getType();

    /**
     * 关闭指标
     */
    default void close() {
    }
}
