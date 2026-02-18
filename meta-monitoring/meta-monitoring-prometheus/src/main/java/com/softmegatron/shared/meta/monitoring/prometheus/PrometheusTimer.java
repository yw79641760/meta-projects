package com.softmegatron.shared.meta.monitoring.prometheus;

import com.softmegatron.shared.meta.monitoring.core.metric.MeterId;
import com.softmegatron.shared.meta.monitoring.core.metric.Tag;

import java.util.concurrent.TimeUnit;

/**
 * Prometheus Timer 适配
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @since 1.0.0
 */
public class PrometheusTimer implements com.softmegatron.shared.meta.monitoring.core.metric.Timer {

    private final MeterId id;
    private final io.prometheus.client.Summary delegate;
    private final String[] labelValues;

    public PrometheusTimer(MeterId id, io.prometheus.client.Summary delegate) {
        this.id = id;
        this.delegate = delegate;
        this.labelValues = id.getTags().stream().map(Tag::getValue).toArray(String[]::new);
    }

    @Override
    public MeterId getId() {
        return id;
    }

    @Override
    public void record(long amount, TimeUnit unit) {
        double seconds = unit.toNanos(amount) / 1_000_000_000.0;
        delegate.labels(labelValues).observe(seconds);
    }

    @Override
    public long count() {
        return (long) delegate.labels(labelValues).get().count;
    }

    @Override
    public double totalTime(TimeUnit unit) {
        return delegate.labels(labelValues).get().sum;
    }
}
