package com.softmegatron.shared.meta.monitoring.prometheus;

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
 * Prometheus 适配的 MeterRegistry
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @since 1.0.0
 */
public class PrometheusMeterRegistry implements MeterRegistry {

    private final io.prometheus.client.CollectorRegistry delegate;
    private final Map<MeterId, Meter> meters = new ConcurrentHashMap<>();

    public PrometheusMeterRegistry() {
        this(new io.prometheus.client.CollectorRegistry());
    }

    public PrometheusMeterRegistry(io.prometheus.client.CollectorRegistry delegate) {
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
        String[] labelNames = id.getTags().stream().map(Tag::getKey).toArray(String[]::new);
        io.prometheus.client.Counter counter = io.prometheus.client.Counter.build()
                .name(sanitizeName(id.getName()))
                .help("Counter: " + id.getName())
                .labelNames(labelNames)
                .register(delegate);
        return new PrometheusCounter(id, counter);
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
        String[] labelNames = id.getTags().stream().map(Tag::getKey).toArray(String[]::new);
        io.prometheus.client.Gauge gauge = io.prometheus.client.Gauge.build()
                .name(sanitizeName(id.getName()))
                .help("Gauge: " + id.getName())
                .labelNames(labelNames)
                .register(delegate);
        return new PrometheusGauge(id, gauge, valueSupplier);
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
        String[] labelNames = id.getTags().stream().map(Tag::getKey).toArray(String[]::new);
        io.prometheus.client.Summary summary = io.prometheus.client.Summary.build()
                .name(sanitizeName(id.getName()))
                .help("Timer: " + id.getName())
                .labelNames(labelNames)
                .register(delegate);
        return new PrometheusTimer(id, summary);
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
        String[] labelNames = id.getTags().stream().map(Tag::getKey).toArray(String[]::new);
        io.prometheus.client.Summary summary = io.prometheus.client.Summary.build()
                .name(sanitizeName(id.getName()))
                .help("Histogram: " + id.getName())
                .labelNames(labelNames)
                .register(delegate);
        return new PrometheusHistogram(id, summary);
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
        // Prometheus doesn't support removing collectors directly
    }

    @Override
    public void clear() {
        meters.clear();
    }

    /**
     * 获取 Prometheus CollectorRegistry
     *
     * @return CollectorRegistry
     */
    public io.prometheus.client.CollectorRegistry getDelegate() {
        return delegate;
    }

    /**
     * 导出 Prometheus 格式的指标
     *
     * @return Prometheus 格式文本
     */
    public String scrape() {
        java.io.StringWriter writer = new java.io.StringWriter();
        try {
            io.prometheus.client.exporter.common.TextFormat.write004(writer, delegate.metricFamilySamples());
        } catch (java.io.IOException e) {
            throw new RuntimeException("Failed to scrape metrics", e);
        }
        return writer.toString();
    }

    /**
     * 清理指标名称，符合 Prometheus 命名规范
     */
    private String sanitizeName(String name) {
        return name.replaceAll("[^a-zA-Z0-9_:]", "_");
    }
}
