package com.softmegatron.shared.meta.core.pattern.chain;

import com.softmegatron.shared.meta.core.pattern.chain.ChainContext;

/**
 * 责任链处理接口
 * 
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @date 2026/02/15 13:42
 * @version 1.0.0
 * @since 1.0.0
 */
public interface ChainHandler<T, R> {

    /**
     * 处理方法
     *
     * @param request 请求参数
     * @param context 上下文参数
     * @return 处理结果
     */
    R handle(T request, ChainContext context);

    /**
     * 设置下一个处理者
     *
     * @param next 下一个处理者
     */
    void setNext(ChainHandler<T, R> next);

    /**
     * 获取下一个处理者
     *
     * @return 下一个处理者
     */
    ChainHandler<T, R> getNext();

    /**
     * 获取处理者顺序(越小优先级越高)
     *
     * @return 处理者顺序
     */
    default int getOrder() {
        return 0;
    }
}
