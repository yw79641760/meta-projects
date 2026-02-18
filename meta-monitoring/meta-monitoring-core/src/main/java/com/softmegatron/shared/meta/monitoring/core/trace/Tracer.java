package com.softmegatron.shared.meta.monitoring.core.trace;

/**
 * 追踪器接口
 * <p>
 * 提供 Span 的创建和管理功能
 * </p>
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @since 1.0.0
 */
public interface Tracer {

    /**
     * 创建新的 Span
     *
     * @param name Span 名称
     * @return Span
     */
    Span newSpan(String name);

    /**
     * 创建并启动新的 Span
     *
     * @param name Span 名称
     * @return 已启动的 Span
     */
    default Span startSpan(String name) {
        return newSpan(name).start();
    }

    /**
     * 获取当前 Span
     *
     * @return 当前 Span，如果没有则返回 null
     */
    Span currentSpan();

    /**
     * 获取当前 Span，如果没有则创建
     *
     * @param name Span 名称
     * @return Span
     */
    default Span currentSpanOrCreate(String name) {
        Span span = currentSpan();
        return span != null ? span : startSpan(name);
    }

    /**
     * 将 Span 设置为当前 Span
     *
     * @param span Span
     * @return 之前的 Span，可能为 null
     */
    Span withSpan(Span span);

    /**
     * 在指定 Span 上下文中执行操作
     *
     * @param span   Span
     * @param runnable 操作
     */
    default void runWithSpan(Span span, Runnable runnable) {
        Span old = withSpan(span);
        try {
            runnable.run();
        } finally {
            withSpan(old);
        }
    }

    /**
     * 获取当前 Trace ID
     *
     * @return Trace ID，如果没有则返回 null
     */
    default String currentTraceId() {
        Span span = currentSpan();
        return span != null ? span.getTraceId() : null;
    }

    /**
     * 获取当前 Span ID
     *
     * @return Span ID，如果没有则返回 null
     */
    default String currentSpanId() {
        Span span = currentSpan();
        return span != null ? span.getSpanId() : null;
    }

    /**
     * 在 Runnable 中传递追踪上下文
     *
     * @param runnable Runnable
     * @return 包装后的 Runnable
     */
    Runnable wrap(Runnable runnable);

    /**
     * 是否启用追踪
     *
     * @return 是否启用
     */
    boolean isEnabled();

    /**
     * 设置是否启用追踪
     *
     * @param enabled 是否启用
     */
    void setEnabled(boolean enabled);
}
