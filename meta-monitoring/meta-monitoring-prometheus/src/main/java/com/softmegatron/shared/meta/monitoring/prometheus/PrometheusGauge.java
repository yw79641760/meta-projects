package com.softmegatron.shared.meta.monitoring.prometheus;

import com.softmegatron.shared.meta.monitoring.core.metric.MeterId;
import com.softmegatron.shared.meta.monitoring.core.metric.Tag;

import java.util.function.Supplier;

/**
 * Prometheus Gauge 适配
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @since 1.0.0
 */
public class PrometheusGauge implements com.softmegatron.shared.meta.monitoring.core.metric.Gauge {

    private final MeterId id;
    private final io.prometheus.client.Gauge delegate;
    private final Supplier<Number> valueSupplier;
    private final String[] labelValues;

    public PrometheusGauge(MeterId id, io.prometheus.client.Gauge delegate, Supplier<Number> valueSupplier) {
        this.id = id;
        this.delegate = delegate;
        this.valueSupplier = valueSupplier;
        this.labelValues = id.getTags().stream().map(Tag::getValue).toArray(String[]::new);
    }

    @Override
    public MeterId getId() {
        return id;
    }

    @Override
    public double value() {
        if (valueSupplier != null) {
            Number value = valueSupplier.get();
            return value != null ? value.doubleValue() : 0;
        }
        return delegate.labels(labelValues).get();
    }
}
