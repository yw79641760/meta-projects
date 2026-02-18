package com.softmegatron.shared.meta.monitoring.prometheus.exporter;

import com.softmegatron.shared.meta.monitoring.prometheus.PrometheusMeterRegistry;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

/**
 * Prometheus HTTP 导出器
 * <p>
 * 提供简单的 HTTP 端点暴露 Prometheus 格式的指标
 * </p>
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @since 1.0.0
 */
public class PrometheusExporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrometheusExporter.class);

    private static final String CONTENT_TYPE = "text/plain; version=0.0.4; charset=utf-8";
    private static final String METRICS_PATH = "/metrics";

    private final PrometheusMeterRegistry registry;
    private final int port;
    private HttpServer server;

    public PrometheusExporter(PrometheusMeterRegistry registry) {
        this(registry, 9090);
    }

    public PrometheusExporter(PrometheusMeterRegistry registry, int port) {
        this.registry = registry;
        this.port = port;
    }

    /**
     * 启动 HTTP 服务器
     *
     * @throws IOException 启动失败
     */
    public synchronized void start() throws IOException {
        if (server != null) {
            LOGGER.warn("Prometheus exporter is already running");
            return;
        }

        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext(METRICS_PATH, createMetricsHandler());
        server.setExecutor(null);
        server.start();

        LOGGER.info("Prometheus exporter started on port {} at {}", port, METRICS_PATH);
    }

    /**
     * 停止 HTTP 服务器
     */
    public synchronized void stop() {
        if (server != null) {
            server.stop(0);
            server = null;
            LOGGER.info("Prometheus exporter stopped");
        }
    }

    /**
     * 获取端口号
     *
     * @return 端口号
     */
    public int getPort() {
        return port;
    }

    /**
     * 检查是否运行中
     *
     * @return 是否运行中
     */
    public boolean isRunning() {
        return server != null;
    }

    private HttpHandler createMetricsHandler() {
        return exchange -> {
            String response;
            int statusCode;

            try {
                response = registry.scrape();
                statusCode = 200;
            } catch (Exception e) {
                LOGGER.error("Failed to scrape metrics", e);
                response = "Error: " + e.getMessage();
                statusCode = 500;
            }

            exchange.getResponseHeaders().set("Content-Type", CONTENT_TYPE);
            exchange.sendResponseHeaders(statusCode, response.getBytes().length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        };
    }

    /**
     * 创建并启动导出器
     *
     * @param registry Prometheus 注册表
     * @param port     端口号
     * @return 导出器实例
     * @throws IOException 启动失败
     */
    public static PrometheusExporter createAndStart(PrometheusMeterRegistry registry, int port) throws IOException {
        PrometheusExporter exporter = new PrometheusExporter(registry, port);
        exporter.start();
        return exporter;
    }
}
