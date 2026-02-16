package com.softmegatron.shared.meta.core.pattern.chain;

import java.util.HashMap;
import java.util.Map;

/**
 * ChainContext - 责任链上下文
 * 
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @date 2026-02-15 13:52:00
 * @version 1.0.0
 * @since 1.0.0
 */
public class ChainContext {

    private final Map<String, Object> context = new HashMap<>();

    public void put(String key, Object value) {
        context.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) context.get(key);
    }
}
