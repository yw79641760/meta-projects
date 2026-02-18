package com.softmegatron.shared.meta.monitoring.core.health;

/**
 * 健康检查指示器接口
 * <p>
 * 实现此接口以提供自定义的健康检查
 * </p>
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @since 1.0.0
 */
public interface HealthIndicator {

    /**
     * 获取健康检查名称
     *
     * @return 名称
     */
    default String getName() {
        return this.getClass().getSimpleName();
    }

    /**
     * 执行健康检查
     *
     * @return 健康检查结果
     */
    Health check();

    /**
     * 创建 UP 状态的健康结果
     *
     * @return Health
     */
    default Health up() {
        return Health.up().build();
    }

    /**
     * 创建带详情的 UP 状态健康结果
     *
     * @param key   详情键
     * @param value 详情值
     * @return Health
     */
    default Health up(String key, Object value) {
        return Health.up().withDetail(key, value).build();
    }

    /**
     * 创建 DOWN 状态的健康结果
     *
     * @return Health
     */
    default Health down() {
        return Health.down().build();
    }

    /**
     * 创建带异常的 DOWN 状态健康结果
     *
     * @param error 异常
     * @return Health
     */
    default Health down(Throwable error) {
        return Health.down(error).build();
    }

    /**
     * 创建 UNKNOWN 状态的健康结果
     *
     * @return Health
     */
    default Health unknown() {
        return Health.unknown().build();
    }
}
