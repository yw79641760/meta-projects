package com.softmegatron.shared.meta.monitoring.core.metric;

/**
 * 直方图接口
 * <p>
 * 用于统计值分布的指标，记录分位数
 * </p>
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @since 1.0.0
 */
public interface Histogram extends Meter {

    @Override
    default MeterType getType() {
        return MeterType.HISTOGRAM;
    }

    /**
     * 记录一个值
     *
     * @param amount 值
     */
    void record(double amount);

    /**
     * 获取总记录数
     *
     * @return 记录数
     */
    long count();

    /**
     * 获取总和
     *
     * @return 总和
     */
    double sum();

    /**
     * 获取平均值
     *
     * @return 平均值
     */
    default double mean() {
        long c = count();
        return c == 0 ? 0 : sum() / c;
    }

    /**
     * 获取最大值
     *
     * @return 最大值
     */
    double max();

    /**
     * 获取指定分位数的值
     *
     * @param percentile 分位数 (0.0 - 1.0)
     * @return 分位数值
     */
    double percentile(double percentile);

    /**
     * 创建 Histogram Builder
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
        private double[] percentiles = {0.5, 0.95, 0.99};

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

        public Builder percentiles(double... percentiles) {
            this.percentiles = percentiles;
            return this;
        }

        public String getName() {
            return name;
        }

        public java.util.List<Tag> getTags() {
            return tags;
        }

        public double[] getPercentiles() {
            return percentiles;
        }
    }
}
