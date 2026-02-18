package com.softmegatron.shared.meta.monitoring.core.metric;

import java.util.function.Supplier;

/**
 * 仪表盘接口
 * <p>
 * 表示瞬时值的指标，用于记录当前值（如当前连接数、队列长度）
 * </p>
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @since 1.0.0
 */
public interface Gauge extends Meter {

    @Override
    default MeterType getType() {
        return MeterType.GAUGE;
    }

    /**
     * 获取当前值
     *
     * @return 当前值
     */
    double value();

    /**
     * 创建 Gauge Builder
     *
     * @param name 指标名称
     * @return Builder
     */
    static Builder builder(String name) {
        return new Builder(name);
    }

    class Builder {
        private final String name;
        private final java.util.List<Tag> tags = new java.util.ArrayList<>();
        private Supplier<Number> valueSupplier;

        public Builder(String name) {
            this.name = name;
        }

        public Builder tag(String key, String value) {
            tags.add(new Tag(key, value));
            return this;
        }

        public Builder tags(Tag... tags) {
            this.tags.addAll(java.util.Arrays.asList(tags));
            return this;
        }

        public Builder value(Supplier<Number> valueSupplier) {
            this.valueSupplier = valueSupplier;
            return this;
        }

        public String getName() {
            return name;
        }

        public java.util.List<Tag> getTags() {
            return tags;
        }

        public Supplier<Number> getValueSupplier() {
            return valueSupplier;
        }
    }
}
