package com.softmegatron.shared.meta.monitoring.micrometer;

import com.softmegatron.shared.meta.monitoring.core.metric.Counter;
import com.softmegatron.shared.meta.monitoring.core.metric.Gauge;
import com.softmegatron.shared.meta.monitoring.core.metric.Histogram;
import com.softmegatron.shared.meta.monitoring.core.metric.Meter;
import com.softmegatron.shared.meta.monitoring.core.metric.MeterId;
import com.softmegatron.shared.meta.monitoring.core.metric.MeterRegistry;
import com.softmegatron.shared.meta.monitoring.core.metric.Tag;
import com.softmegatron.shared.meta.monitoring.core.metric.Timer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Micrometer 适配的 MeterRegistry
 * <p>
 * 将 Micrometer 的 MeterRegistry 适配到我们的接口
 * </p>
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @since 1.0.0
 */
public class MicrometerMeterRegistry implements MeterRegistry {

    private final io.micrometer.core.instrument.MeterRegistry delegate;
    private final Map<MeterId, Meter> meters = new ConcurrentHashMap<>();

    public MicrometerMeterRegistry(io.micrometer.core.instrument.MeterRegistry delegate) {
        this.delegate = delegate;
    }

    @Override
    public Counter counter(String name, Tag... tags) {
        MeterId id = new MeterId(name, tags);
        return (Counter) meters.computeIfAbsent(id, this::createCounter);
    }

    @Override
    public Counter counter(Counter.Builder builder) {
        return counter(builder.getName(), builder.getTags().toArray(new Tag[0]));
    }

    private Counter createCounter(MeterId id) {
        io.micrometer.core.instrument.Counter counter = io.micrometer.core.instrument.Counter.builder(id.getName())
                .tags(convertTags(id.getTags()))
                .register(delegate);
        return new MicrometerCounter(id, counter);
    }

    @Override
    public Gauge gauge(String name, Supplier<Number> valueSupplier, Tag... tags) {
        MeterId id = new MeterId(name, tags);
        return (Gauge) meters.computeIfAbsent(id, k -> createGauge(k, valueSupplier));
    }

    @Override
    public Gauge gauge(Gauge.Builder builder) {
        return gauge(builder.getName(), builder.getValueSupplier(), builder.getTags().toArray(new Tag[0]));
    }

    private Gauge createGauge(MeterId id, Supplier<Number> valueSupplier) {
        io.micrometer.core.instrument.Gauge gauge = io.micrometer.core.instrument.Gauge
                .builder(id.getName(), valueSupplier, s -> s.get().doubleValue())
                .tags(convertTags(id.getTags()))
                .register(delegate);
        return new MicrometerGauge(id, gauge, valueSupplier);
    }

    @Override
    public Timer timer(String name, Tag... tags) {
        MeterId id = new MeterId(name, tags);
        return (Timer) meters.computeIfAbsent(id, this::createTimer);
    }

    @Override
    public Timer timer(Timer.Builder builder) {
        return timer(builder.getName(), builder.getTags().toArray(new Tag[0]));
    }

    private Timer createTimer(MeterId id) {
        io.micrometer.core.instrument.Timer timer = io.micrometer.core.instrument.Timer.builder(id.getName())
                .tags(convertTags(id.getTags()))
                .register(delegate);
        return new MicrometerTimer(id, timer);
    }

    @Override
    public Histogram histogram(String name, Tag... tags) {
        MeterId id = new MeterId(name, tags);
        return (Histogram) meters.computeIfAbsent(id, this::createHistogram);
    }

    @Override
    public Histogram histogram(Histogram.Builder builder) {
        return histogram(builder.getName(), builder.getTags().toArray(new Tag[0]));
    }

    private Histogram createHistogram(MeterId id) {
        io.micrometer.core.instrument.DistributionSummary summary = io.micrometer.core.instrument.DistributionSummary.builder(id.getName())
                .tags(convertTags(id.getTags()))
                .register(delegate);
        return new MicrometerHistogram(id, summary);
    }

    @Override
    public List<Meter> getMeters() {
        return new ArrayList<>(meters.values());
    }

    @Override
    public List<Meter> getMeters(String name) {
        List<Meter> result = new ArrayList<>();
        for (Map.Entry<MeterId, Meter> entry : meters.entrySet()) {
            if (entry.getKey().getName().equals(name)) {
                result.add(entry.getValue());
            }
        }
        return result;
    }

    @Override
    public void remove(MeterId meterId) {
        Meter meter = meters.remove(meterId);
        if (meter != null) {
            delegate.find(meterId.getName()).tags(convertTags(meterId.getTags())).meter();
        }
    }

    @Override
    public void clear() {
        meters.clear();
    }

    @Override
    public void close() {
        MeterRegistry.super.close();
        delegate.close();
    }

    private Iterable<io.micrometer.core.instrument.Tag> convertTags(List<Tag> tags) {
        return () -> tags.stream()
                .map(t -> io.micrometer.core.instrument.Tag.of(t.getKey(), t.getValue()))
                .iterator();
    }

    public io.micrometer.core.instrument.MeterRegistry getDelegate() {
        return delegate;
    }
}
