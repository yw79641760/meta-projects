package com.softmegatron.shared.meta.monitoring.core.trace;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 默认追踪上下文实现
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @since 1.0.0
 */
public class DefaultTraceContext implements TraceContext {

    private final String traceId;
    private final String spanId;
    private final String parentSpanId;
    private final Map<String, String> tags;

    public DefaultTraceContext(String traceId, String spanId, String parentSpanId, Map<String, String> tags) {
        this.traceId = traceId;
        this.spanId = spanId;
        this.parentSpanId = parentSpanId;
        this.tags = tags != null ? new HashMap<>(tags) : new HashMap<>();
    }

    @Override
    public String getTraceId() {
        return traceId;
    }

    @Override
    public String getSpanId() {
        return spanId;
    }

    @Override
    public String getParentSpanId() {
        return parentSpanId;
    }

    @Override
    public Map<String, String> getTags() {
        return Collections.unmodifiableMap(tags);
    }

    @Override
    public String getTag(String key) {
        return tags.get(key);
    }

    @Override
    public void setTag(String key, String value) {
        tags.put(key, value);
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> result = new HashMap<>();
        if (traceId != null) {
            result.put("traceId", traceId);
        }
        if (spanId != null) {
            result.put("spanId", spanId);
        }
        if (parentSpanId != null) {
            result.put("parentSpanId", parentSpanId);
        }
        result.putAll(tags);
        return result;
    }

    @Override
    public String toString() {
        return "DefaultTraceContext{" +
                "traceId='" + traceId + '\'' +
                ", spanId='" + spanId + '\'' +
                ", parentSpanId='" + parentSpanId + '\'' +
                ", tags=" + tags +
                '}';
    }
}
