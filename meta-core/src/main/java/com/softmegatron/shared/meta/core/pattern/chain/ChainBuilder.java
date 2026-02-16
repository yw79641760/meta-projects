package com.softmegatron.shared.meta.core.pattern.chain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.softmegatron.shared.meta.core.pattern.chain.ChainHandler;

/**
 * ChainBuilder
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @date 2026-02-15 14:05:36
 * @version 1.0.0
 * @since 1.0.0
 */
public class ChainBuilder<T, R> {

    private final List<ChainHandler<T, R>> handlers = new ArrayList<>(); 

    public ChainBuilder<T, R> addHandler(ChainHandler<T, R> handler) {
        handlers.add(handler);
        return this;
    }
    
    /**
     * 构建链
     * @return
     */
    public ChainHandler<T, R> build() { 
        if (handlers.isEmpty()) {
            throw new IllegalStateException("Empty handler added to the chain.");
        }
        // 排序
        handlers.sort(Comparator.comparingInt(ChainHandler::getOrder));
        // 构建链
        for (int i = 0; i < handlers.size() - 1; i++) {
            ChainHandler<T, R> current = handlers.get(i);
            ChainHandler<T, R> next = handlers.get(i + 1);
            current.setNext(next);
        }
        return handlers.get(0);
    }
}
