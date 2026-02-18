package com.softmegatron.shared.meta.monitoring.core.metric;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 计时器接口
 * <p>
 * 用于测量时间间隔和速率的指标
 * </p>
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @since 1.0.0
 */
public interface Timer extends Meter {

    @Override
    default MeterType getType() {
        return MeterType.TIMER;
    }

    /**
     * 记录一次耗时
     *
     * @param amount 耗时数值
     * @param unit   时间单位
     */
    void record(long amount, TimeUnit unit);

    /**
     * 记录 Runnable 执行耗时
     *
     * @param runnable 要执行的任务
     */
    default void record(Runnable runnable) {
        long start = System.nanoTime();
        try {
            runnable.run();
        } finally {
            record(System.nanoTime() - start, TimeUnit.NANOSECONDS);
        }
    }

    /**
     * 记录 Callable 执行耗时并返回结果
     *
     * @param callable 要执行的任务
     * @param <T>      返回类型
     * @return 任务结果
     * @throws Exception 任务执行异常
     */
    default <T> T recordCallable(Callable<T> callable) throws Exception {
        long start = System.nanoTime();
        try {
            return callable.call();
        } finally {
            record(System.nanoTime() - start, TimeUnit.NANOSECONDS);
        }
    }

    /**
     * 创建计时样本
     *
     * @return 计时样本
     */
    default Sample start() {
        return new Sample(this, System.nanoTime());
    }

    /**
     * 获取总调用次数
     *
     * @return 调用次数
     */
    long count();

    /**
     * 获取总耗时（毫秒）
     *
     * @return 总耗时
     */
    double totalTime(TimeUnit unit);

    /**
     * 获取平均耗时（毫秒）
     *
     * @return 平均耗时
     */
    default double mean(TimeUnit unit) {
        long c = count();
        return c == 0 ? 0 : totalTime(unit) / c;
    }

    /**
     * 计时样本
     */
    class Sample {
        private final Timer timer;
        private final long startTime;

        private Sample(Timer timer, long startTime) {
            this.timer = timer;
            this.startTime = startTime;
        }

        /**
         * 停止计时并记录
         *
         * @return 耗时（纳秒）
         */
        public long stop() {
            long duration = System.nanoTime() - startTime;
            timer.record(duration, TimeUnit.NANOSECONDS);
            return duration;
        }
    }

    /**
     * 创建 Timer Builder
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
