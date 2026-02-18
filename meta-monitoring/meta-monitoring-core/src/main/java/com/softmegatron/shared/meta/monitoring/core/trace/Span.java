package com.softmegatron.shared.meta.monitoring.core.trace;

/**
 * 追踪片段接口
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @since 1.0.0
 */
public interface Span extends AutoCloseable {

    /**
     * 获取 Span ID
     *
     * @return Span ID
     */
    String getSpanId();

    /**
     * 获取 Trace ID
     *
     * @return Trace ID
     */
    String getTraceId();

    /**
     * 获取父 Span ID
     *
     * @return 父 Span ID
     */
    String getParentSpanId();

    /**
     * 获取 Span 名称
     *
     * @return 名称
     */
    String getName();

    /**
     * 设置 Span 名称
     *
     * @param name 名称
     * @return this
     */
    Span name(String name);

    /**
     * 设置标签
     *
     * @param key   标签键
     * @param value 标签值
     * @return this
     */
    Span tag(String key, String value);

    /**
     * 设置标签
     *
     * @param key   标签键
     * @param value 标签值
     * @return this
     */
    default Span tag(String key, Number value) {
        return tag(key, String.valueOf(value));
    }

    /**
     * 设置标签
     *
     * @param key   标签键
     * @param value 标签值
     * @return this
     */
    default Span tag(String key, boolean value) {
        return tag(key, String.valueOf(value));
    }

    /**
     * 记录事件
     *
     * @param name 事件名称
     * @return this
     */
    Span event(String name);

    /**
     * 记录事件
     *
     * @param name 事件名称
     * @param message 事件消息
     * @return this
     */
    Span event(String name, String message);

    /**
     * 记录错误
     *
     * @param throwable 异常
     * @return this
     */
    Span error(Throwable throwable);

    /**
     * 标记 Span 开始
     *
     * @return this
     */
    Span start();

    /**
     * 标记 Span 结束
     */
    void end();

    /**
     * 关闭 Span（等同于 end）
     */
    @Override
    default void close() {
        end();
    }

    /**
     * 创建子 Span
     *
     * @param name Span 名称
     * @return 子 Span
     */
    Span newChild(String name);

    /**
     * 获取开始时间（毫秒）
     *
     * @return 开始时间
     */
    long getStartTimeMillis();

    /**
     * 获取耗时（毫秒）
     *
     * @return 耗时，如果未结束返回 -1
     */
    long getDurationMillis();

    /**
     * Span 是否已结束
     *
     * @return 是否已结束
     */
    boolean isEnded();
}
