package com.softmegatron.shared.meta.monitoring.prometheus;

import com.softmegatron.shared.meta.monitoring.core.metric.MeterId;
import com.softmegatron.shared.meta.monitoring.core.metric.Tag;

/**
 * Prometheus Counter 适配
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @since 1.0.0
 */
public class PrometheusCounter implements com.softmegatron.shared.meta.monitoring.core.metric.Counter {

    private final MeterId id;
    private final io.prometheus.client.Counter delegate;
    private final String[] labelValues;

    public PrometheusCounter(MeterId id, io.prometheus.client.Counter delegate) {
        this.id = id;
        this.delegate = delegate;
        this.labelValues = id.getTags().stream().map(Tag::getValue).toArray(String[]::new);
    }

    @Override
    public MeterId getId() {
        return id;
    }

    @Override
    public void increment(double amount) {
        delegate.labels(labelValues).inc(amount);
    }

    @Override
    public double count() {
        return delegate.labels(labelValues).get();
    }
}
