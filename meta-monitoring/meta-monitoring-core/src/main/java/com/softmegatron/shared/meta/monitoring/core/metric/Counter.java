package com.softmegatron.shared.meta.monitoring.core.metric;

/**
 * 计数器接口
 * <p>
 * 单调递增的计数器，用于记录累计值（如请求数、错误数）
 * </p>
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @since 1.0.0
 */
public interface Counter extends Meter {

    @Override
    default MeterType getType() {
        return MeterType.COUNTER;
    }

    /**
     * 增加计数
     */
    default void increment() {
        increment(1);
    }

    /**
     * 增加指定数值
     *
     * @param amount 增加数值（必须为正数）
     */
    void increment(double amount);

    /**
     * 获取当前计数值
     *
     * @return 当前计数
     */
    double count();

    /**
     * 创建 Counter Builder
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

        public String getName() {
            return name;
        }

        public java.util.List<Tag> getTags() {
            return tags;
        }
    }
}
