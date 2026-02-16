package com.softmegatron.shared.meta.core.pattern.holder;
/**
 * 单例模式Holder
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @date 2026/02/15 10:23
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class SingletonHolder<T> {

    private static class Holder<T> {

        private final T instance;

        Holder(T instance) {
            this.instance = instance;
        }
    }

    private volatile Holder<T> holder;

    protected SingletonHolder() {
    }

    @SuppressWarnings("unchecked")
    public T getInstance() {
        if (holder == null) {
            synchronized (this) {
                if (holder == null) {
                    holder = new Holder<>(newInstance());
                }
            }
        }
        return holder.instance;
    }

    protected abstract T newInstance();
}
