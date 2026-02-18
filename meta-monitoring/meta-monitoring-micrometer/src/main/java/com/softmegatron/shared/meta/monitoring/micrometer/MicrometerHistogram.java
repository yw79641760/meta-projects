package com.softmegatron.shared.meta.monitoring.micrometer;

import com.softmegatron.shared.meta.monitoring.core.metric.Histogram;
import com.softmegatron.shared.meta.monitoring.core.metric.MeterId;

/**
 * Micrometer Histogram 适配
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @since 1.0.0
 */
public class MicrometerHistogram implements Histogram {

    private final MeterId id;
    private final io.micrometer.core.instrument.DistributionSummary delegate;

    public MicrometerHistogram(MeterId id, io.micrometer.core.instrument.DistributionSummary delegate) {
        this.id = id;
        this.delegate = delegate;
    }

    @Override
    public MeterId getId() {
        return id;
    }

    @Override
    public void record(double amount) {
        delegate.record(amount);
    }

    @Override
    public long count() {
        return delegate.count();
    }

    @Override
    public double sum() {
        return delegate.totalAmount();
    }

    @Override
    public double max() {
        return delegate.max();
    }

    @Override
    public double percentile(double percentile) {
        return delegate.percentile(percentile);
    }
}
