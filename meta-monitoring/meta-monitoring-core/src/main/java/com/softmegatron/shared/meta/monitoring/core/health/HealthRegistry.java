package com.softmegatron.shared.meta.monitoring.core.health;

import java.util.List;
import java.util.Map;

/**
 * 健康检查注册表接口
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @since 1.0.0
 */
public interface HealthRegistry {

    /**
     * 注册健康检查指示器
     *
     * @param indicator 健康检查指示器
     */
    void register(HealthIndicator indicator);

    /**
     * 注册健康检查指示器
     *
     * @param name      名称
     * @param indicator 健康检查指示器
     */
    void register(String name, HealthIndicator indicator);

    /**
     * 注销健康检查指示器
     *
     * @param name 名称
     */
    void unregister(String name);

    /**
     * 获取所有已注册的健康检查名称
     *
     * @return 名称列表
     */
    List<String> getNames();

    /**
     * 获取健康检查指示器
     *
     * @param name 名称
     * @return 健康检查指示器
     */
    HealthIndicator getIndicator(String name);

    /**
     * 执行指定健康检查
     *
     * @param name 名称
     * @return 健康检查结果
     */
    Health check(String name);

    /**
     * 执行所有健康检查
     *
     * @return 健康检查结果映射
     */
    Map<String, Health> checkAll();

    /**
     * 获取整体健康状态
     *
     * @return 整体健康状态
     */
    Health getOverallHealth();

    /**
     * 清空所有注册
     */
    void clear();
}
