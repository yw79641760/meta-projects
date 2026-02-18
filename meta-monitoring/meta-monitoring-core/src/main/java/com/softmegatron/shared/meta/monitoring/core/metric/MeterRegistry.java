package com.softmegatron.shared.meta.monitoring.core.metric;

import java.util.List;
import java.util.function.Supplier;

/**
 * 指标注册表接口
 * <p>
 * 指标管理的核心接口，用于创建和管理各类指标
 * </p>
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @since 1.0.0
 */
public interface MeterRegistry {

    /**
     * 创建或获取计数器
     *
     * @param name 指标名称
     * @return 计数器
     */
    default Counter counter(String name) {
        return counter(name, new Tag[0]);
    }

    /**
     * 创建或获取计数器
     *
     * @param name 指标名称
     * @param tags 标签
     * @return 计数器
     */
    Counter counter(String name, Tag... tags);

    /**
     * 创建或获取计数器
     *
     * @param builder Builder
     * @return 计数器
     */
    Counter counter(Counter.Builder builder);

    /**
     * 创建或获取仪表盘
     *
     * @param name         指标名称
     * @param valueSupplier 值提供器
     * @return 仪表盘
     */
    default Gauge gauge(String name, Supplier<Number> valueSupplier) {
        return gauge(name, valueSupplier, new Tag[0]);
    }

    /**
     * 创建或获取仪表盘
     *
     * @param name         指标名称
     * @param valueSupplier 值提供器
     * @param tags         标签
     * @return 仪表盘
     */
    Gauge gauge(String name, Supplier<Number> valueSupplier, Tag... tags);

    /**
     * 创建或获取仪表盘
     *
     * @param builder Builder
     * @return 仪表盘
     */
    Gauge gauge(Gauge.Builder builder);

    /**
     * 创建或获取计时器
     *
     * @param name 指标名称
     * @return 计时器
     */
    default Timer timer(String name) {
        return timer(name, new Tag[0]);
    }

    /**
     * 创建或获取计时器
     *
     * @param name 指标名称
     * @param tags 标签
     * @return 计时器
     */
    Timer timer(String name, Tag... tags);

    /**
     * 创建或获取计时器
     *
     * @param builder Builder
     * @return 计时器
     */
    Timer timer(Timer.Builder builder);

    /**
     * 创建或获取直方图
     *
     * @param name 指标名称
     * @return 直方图
     */
    default Histogram histogram(String name) {
        return histogram(name, new Tag[0]);
    }

    /**
     * 创建或获取直方图
     *
     * @param name 指标名称
     * @param tags 标签
     * @return 直方图
     */
    Histogram histogram(String name, Tag... tags);

    /**
     * 创建或获取直方图
     *
     * @param builder Builder
     * @return 直方图
     */
    Histogram histogram(Histogram.Builder builder);

    /**
     * 获取所有已注册的指标
     *
     * @return 指标列表
     */
    List<Meter> getMeters();

    /**
     * 获取指定名称的指标
     *
     * @param name 指标名称
     * @return 指标列表
     */
    List<Meter> getMeters(String name);

    /**
     * 移除指定指标
     *
     * @param meterId 指标ID
     */
    void remove(MeterId meterId);

    /**
     * 清空所有指标
     */
    void clear();

    /**
     * 关闭注册表
     */
    default void close() {
        clear();
    }
}
