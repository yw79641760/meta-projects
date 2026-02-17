package com.softmegatron.shared.meta.extension.loader;

import com.softmegatron.shared.meta.extension.annotation.Spi;
import com.softmegatron.shared.meta.extension.exception.ExtensionException;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 扩展管理器
 * <p>
 * 统一的扩展点入口，提供静态方法获取扩展实例。
 * </p>
 *
 * <pre>
 * 使用示例：
 * // 获取指定扩展
 * MyService service = ExtensionManager.getExtension(MyService.class, "http");
 *
 * // 获取默认扩展
 * MyService defaultService = ExtensionManager.getDefaultExtension(MyService.class);
 *
 * // 安全获取（优先指定 key，fallback 到默认）
 * MyService service = ExtensionManager.getExtensionOrDefault(MyService.class, "dubbo");
 * </pre>
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public final class ExtensionManager {

    /** 扩展点类型 -> ExtensionLoader 缓存 */
    private static final ConcurrentMap<Class<?>, ExtensionLoader<?>> LOADER_CACHE = new ConcurrentHashMap<>();

    private ExtensionManager() {
    }

    /**
     * 获取指定扩展实现
     *
     * @param type 扩展点接口类型
     * @param key  扩展键值
     * @param <T>  扩展点类型
     * @return 扩展实例，不存在则返回 null
     * @throws ExtensionException 如果扩展点配置无效
     */
    public static <T> T getExtension(Class<T> type, String key) {
        return getLoader(type).getExtension(key);
    }

    /**
     * 获取默认扩展实现
     *
     * @param type 扩展点接口类型
     * @param <T>  扩展点类型
     * @return 默认扩展实例，不存在则返回 null
     * @throws ExtensionException 如果扩展点配置无效
     */
    public static <T> T getDefaultExtension(Class<T> type) {
        return getLoader(type).getDefaultExtension();
    }

    /**
     * 获取指定扩展，不存在则返回默认扩展
     *
     * @param type 扩展点接口类型
     * @param key  扩展键值
     * @param <T>  扩展点类型
     * @return 扩展实例，都不存在则返回 null
     * @throws ExtensionException 如果扩展点配置无效
     */
    public static <T> T getExtensionOrDefault(Class<T> type, String key) {
        return getLoader(type).getExtensionOrDefault(key);
    }

    /**
     * 检查扩展是否存在
     *
     * @param type 扩展点接口类型
     * @param key  扩展键值
     * @param <T>  扩展点类型
     * @return 是否存在
     * @throws ExtensionException 如果扩展点配置无效
     */
    public static <T> boolean hasExtension(Class<T> type, String key) {
        return getLoader(type).hasExtension(key);
    }

    /**
     * 获取所有扩展键值
     *
     * @param type 扩展点接口类型
     * @param <T>  扩展点类型
     * @return 扩展键值集合
     * @throws ExtensionException 如果扩展点配置无效
     */
    public static <T> Set<String> getExtensionKeys(Class<T> type) {
        return getLoader(type).getExtensionKeys();
    }

    /**
     * 获取或创建 ExtensionLoader
     */
    @SuppressWarnings("unchecked")
    private static <T> ExtensionLoader<T> getLoader(Class<T> type) {
        validateType(type);
        return (ExtensionLoader<T>) LOADER_CACHE.computeIfAbsent(type, ExtensionLoader::new);
    }

    /**
     * 校验扩展点类型
     */
    private static <T> void validateType(Class<T> type) {
        if (type == null) {
            throw new ExtensionException("Extension type cannot be null");
        }
        if (!type.isInterface()) {
            throw new ExtensionException("Extension type must be an interface: " + type.getName());
        }
        if (!type.isAnnotationPresent(Spi.class)) {
            throw new ExtensionException("Extension type must be annotated with @Spi: " + type.getName());
        }
    }
}
