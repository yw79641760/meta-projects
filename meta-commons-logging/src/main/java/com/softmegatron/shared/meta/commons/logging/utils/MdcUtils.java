package com.softmegatron.shared.meta.commons.logging.utils;

import org.slf4j.MDC;

import java.io.Closeable;
import java.util.Collections;
import java.util.Map;

/**
 * MDC (Mapped Diagnostic Context) 工具类
 * 提供线程安全的日志上下文管理功能
 * 
 * <p><strong>重要说明：</strong>此工具类依赖SLF4J的具体实现来提供MDC功能。
 * 常见的支持MDC的实现包括：
 * <ul>
 *   <li>Logback (推荐)</li>
 *   <li>Log4j 2.x</li>
 *   <li>slf4j-log4j12 + log4j 1.2</li>
 * </ul>
 * 如果使用slf4j-simple或其他不支持MDC的绑定，则MDC操作将无效。
 * </p>
 *
 * <p>使用示例:</p>
 * <pre>
 * // 基本使用
 * MdcUtils.put("userId", "12345");
 * String userId = MdcUtils.get("userId");
 * 
 * // 自动清理的MDC上下文
 * try (Closeable ignored = MdcUtils.putCloseable("requestId", "req-abc")) {
 *     // 所有在此块内的日志都会包含requestId上下文
 *     MetaLogger.info(logger, "Processing request");
 * }
 * // 出块后自动清理
 * 
 * // 批量设置
 * Map&lt;String, String&gt; context = Map.of("traceId", "trace-123", "spanId", "span-456");
 * MdcUtils.putAll(context);
 * </pre>
 * 
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 2026/02/12
 * @see org.slf4j.MDC
 */
public class MdcUtils {

    private MdcUtils() {
        // 私有构造函数，防止实例化
    }

    /**
     * 设置MDC键值对
     *
     * @param key   键
     * @param value 值
     * @throws IllegalArgumentException 如果key或value为null
     */
    public static void put(String key, String value) {
        if (key == null) {
            throw new IllegalArgumentException("MDC key cannot be null");
        }
        if (value == null) {
            throw new IllegalArgumentException("MDC value cannot be null");
        }
        MDC.put(key, value);
    }

    /**
     * 获取MDC中的值
     *
     * @param key 键
     * @return 对应的值，如果不存在返回null
     */
    public static String get(String key) {
        return key != null ? MDC.get(key) : null;
    }

    /**
     * 移除MDC中的键值对
     *
     * @param key 键
     */
    public static void remove(String key) {
        if (key != null) {
            MDC.remove(key);
        }
    }

    /**
     * 清空当前线程的所有MDC上下文
     */
    public static void clear() {
        MDC.clear();
    }

    /**
     * 获取当前线程的MDC上下文映射
     *
     * @return MDC上下文映射的副本，如果MDC不可用返回空map
     */
    public static Map<String, String> getContextMap() {
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        return contextMap != null ? contextMap : Collections.emptyMap();
    }

    /**
     * 设置MDC上下文映射
     *
     * @param contextMap 上下文映射
     */
    public static void setContextMap(Map<String, String> contextMap) {
        if (contextMap != null) {
            MDC.setContextMap(contextMap);
        }
    }

    /**
     * 创建可自动关闭的MDC条目
     * 使用try-with-resources语法可以自动清理MDC上下文
     *
     * @param key   键
     * @param value 值
     * @return 可关闭的MDC条目
     * @throws IllegalArgumentException 如果key或value为null
     */
    public static Closeable putCloseable(String key, String value) {
        put(key, value);
        return () -> remove(key);
    }

    /**
     * 批量设置MDC上下文
     *
     * @param contextMap 上下文映射
     * @throws IllegalArgumentException 如果contextMap为null
     */
    public static void putAll(Map<String, String> contextMap) {
        if (contextMap == null) {
            throw new IllegalArgumentException("Context map cannot be null");
        }
        contextMap.forEach(MdcUtils::put);
    }

    /**
     * 检查MDC中是否存在指定键
     *
     * @param key 键
     * @return 如果存在返回true，否则返回false
     */
    public static boolean containsKey(String key) {
        return key != null && MDC.get(key) != null;
    }

    /**
     * 获取MDC大小
     *
     * @return MDC中键值对的数量
     */
    public static int size() {
        Map<String, String> contextMap = getContextMap();
        return contextMap.size();
    }

    /**
     * 检查MDC是否为空
     *
     * @return 如果为空返回true，否则返回false
     */
    public static boolean isEmpty() {
        return size() == 0;
    }
}