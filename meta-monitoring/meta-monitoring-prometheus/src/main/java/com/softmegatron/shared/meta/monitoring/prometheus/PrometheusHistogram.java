package com.softmegatron.shared.meta.monitoring.prometheus;

import com.softmegatron.shared.meta.monitoring.core.metric.MeterId;
import com.softmegatron.shared.meta.monitoring.core.metric.Tag;

/**
 * Prometheus Histogram 适配
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @since 1.0.0
 */
public class PrometheusHistogram implements com.softmegatron.shared.meta.monitoring.core.metric.Histogram {

    private final MeterId id;
    private final io.prometheus.client.Summary delegate;
    private final String[] labelValues;

    public PrometheusHistogram(MeterId id, io.prometheus.client.Summary delegate) {
        this.id = id;
        this.delegate = delegate;
        this.labelValues = id.getTags().stream().map(Tag::getValue).toArray(String[]::new);
    }

    @Override
    public MeterId getId() {
        return id;
    }

    @Override
    public void record(double amount) {
        delegate.labels(labelValues).observe(amount);
    }

    @Override
    public long count() {
        return (long) delegate.labels(labelValues).get().count;
    }

    @Override
    public double sum() {
        return delegate.labels(labelValues).get().sum;
    }

    @Override
    public double max() {
        // Prometheus Summary doesn't provide max directly
        return 0;
    }

    @Override
    public double percentile(double percentile) {
        // Prometheus Summary doesn't provide percentile directly
        return 0;
    }
}
