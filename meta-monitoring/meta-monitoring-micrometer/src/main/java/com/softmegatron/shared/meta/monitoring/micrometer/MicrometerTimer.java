package com.softmegatron.shared.meta.monitoring.micrometer;

import com.softmegatron.shared.meta.monitoring.core.metric.MeterId;
import com.softmegatron.shared.meta.monitoring.core.metric.Timer;

import java.util.concurrent.TimeUnit;

/**
 * Micrometer Timer 适配
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @since 1.0.0
 */
public class MicrometerTimer implements Timer {

    private final MeterId id;
    private final io.micrometer.core.instrument.Timer delegate;

    public MicrometerTimer(MeterId id, io.micrometer.core.instrument.Timer delegate) {
        this.id = id;
        this.delegate = delegate;
    }

    @Override
    public MeterId getId() {
        return id;
    }

    @Override
    public void record(long amount, TimeUnit unit) {
        delegate.record(amount, unit);
    }

    @Override
    public long count() {
        return delegate.count();
    }

    @Override
    public double totalTime(TimeUnit unit) {
        return delegate.totalTime(unit);
    }
}
