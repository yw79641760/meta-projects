package com.softmegatron.shared.meta.monitoring.core.trace;

import java.util.Map;

/**
 * 追踪上下文接口
 * <p>
 * 用于跨进程/线程传递追踪信息
 * </p>
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @since 1.0.0
 */
public interface TraceContext {

    /**
     * 获取 Trace ID
     *
     * @return Trace ID
     */
    String getTraceId();

    /**
     * 获取 Span ID
     *
     * @return Span ID
     */
    String getSpanId();

    /**
     * 获取父 Span ID
     *
     * @return 父 Span ID
     */
    String getParentSpanId();

    /**
     * 获取所有标签
     *
     * @return 标签映射
     */
    Map<String, String> getTags();

    /**
     * 获取指定标签
     *
     * @param key 标签键
     * @return 标签值
     */
    String getTag(String key);

    /**
     * 设置标签
     *
     * @param key   标签键
     * @param value 标签值
     */
    void setTag(String key, String value);

    /**
     * 转换为 Map
     *
     * @return Map 表示
     */
    Map<String, String> toMap();

    /**
     * 从 Map 创建上下文
     *
     * @param map Map
     * @return TraceContext
     */
    static TraceContext fromMap(Map<String, String> map) {
        return new DefaultTraceContext(
                map.get("traceId"),
                map.get("spanId"),
                map.get("parentSpanId"),
                map
        );
    }

    /**
     * 创建新的 TraceContext
     *
     * @param traceId Trace ID
     * @param spanId  Span ID
     * @return TraceContext
     */
    static TraceContext of(String traceId, String spanId) {
        return new DefaultTraceContext(traceId, spanId, null, null);
    }

    /**
     * 创建新的 TraceContext
     *
     * @param traceId      Trace ID
     * @param spanId       Span ID
     * @param parentSpanId 父 Span ID
     * @return TraceContext
     */
    static TraceContext of(String traceId, String spanId, String parentSpanId) {
        return new DefaultTraceContext(traceId, spanId, parentSpanId, null);
    }
}
