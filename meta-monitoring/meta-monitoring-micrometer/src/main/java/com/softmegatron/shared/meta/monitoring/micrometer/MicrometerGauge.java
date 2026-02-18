package com.softmegatron.shared.meta.monitoring.micrometer;

import com.softmegatron.shared.meta.monitoring.core.metric.Gauge;
import com.softmegatron.shared.meta.monitoring.core.metric.MeterId;

import java.util.function.Supplier;

/**
 * Micrometer Gauge 适配
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @since 1.0.0
 */
public class MicrometerGauge implements Gauge {

    private final MeterId id;
    private final io.micrometer.core.instrument.Gauge delegate;
    private final Supplier<Number> valueSupplier;

    public MicrometerGauge(MeterId id, io.micrometer.core.instrument.Gauge delegate, Supplier<Number> valueSupplier) {
        this.id = id;
        this.delegate = delegate;
        this.valueSupplier = valueSupplier;
    }

    @Override
    public MeterId getId() {
        return id;
    }

    @Override
    public double value() {
        return delegate.value();
    }
}
