package com.softmegatron.shared.meta.monitoring.micrometer;

import com.softmegatron.shared.meta.monitoring.core.metric.Counter;
import com.softmegatron.shared.meta.monitoring.core.metric.MeterId;

/**
 * Micrometer Counter 适配
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @since 1.0.0
 */
public class MicrometerCounter implements Counter {

    private final MeterId id;
    private final io.micrometer.core.instrument.Counter delegate;

    public MicrometerCounter(MeterId id, io.micrometer.core.instrument.Counter delegate) {
        this.id = id;
        this.delegate = delegate;
    }

    @Override
    public MeterId getId() {
        return id;
    }

    @Override
    public void increment(double amount) {
        delegate.increment(amount);
    }

    @Override
    public double count() {
        return delegate.count();
    }
}
