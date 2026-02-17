package com.softmegatron.shared.meta.extension.core.loader;

import com.softmegatron.shared.meta.extension.core.annotation.Spi;
import com.softmegatron.shared.meta.extension.core.exception.ExtensionException;
import com.softmegatron.shared.meta.extension.core.factory.ExtensionFactory;
import com.softmegatron.shared.meta.extension.core.factory.SpiExtensionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;

/**
 * 扩展管理器
 * <p>
 * 统一的扩展点入口，通过多个 {@link ExtensionFactory} 获取扩展实例。
 * 支持从 SPI 配置、Spring 容器等多种来源获取扩展。
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

    private static final Logger LOGGER = LoggerFactory.getLogger(ExtensionManager.class);

    /** 工厂列表（按优先级排序） */
    private static final List<ExtensionFactory> FACTORIES = new ArrayList<>();

    static {
        loadFactories();
    }

    private ExtensionManager() {
    }

    /**
     * 加载所有 ExtensionFactory 实现
     */
    private static void loadFactories() {
        ServiceLoader<ExtensionFactory> loader = ServiceLoader.load(ExtensionFactory.class);

        List<ExtensionFactory> factories = new ArrayList<>();
        loader.forEach(factories::add);

        factories.sort(Comparator.comparingInt(ExtensionFactory::getOrder));

        if (factories.isEmpty()) {
            factories.add(new SpiExtensionFactory());
        }

        FACTORIES.addAll(factories);

        LOGGER.info("Loaded {} extension factories", FACTORIES.size());
    }

    /**
     * 获取指定扩展实现
     * <p>
     * 依次尝试每个工厂，找到即返回。
     * </p>
     *
     * @param type 扩展点接口类型
     * @param key  扩展键值
     * @param <T>  扩展点类型
     * @return 扩展实例，不存在则返回 null
     * @throws ExtensionException 如果扩展点配置无效
     */
    public static <T> T getExtension(Class<T> type, String key) {
        validateType(type);
        for (ExtensionFactory factory : FACTORIES) {
            try {
                T extension = factory.getExtension(type, key);
                if (extension != null) {
                    return extension;
                }
            } catch (Exception e) {
                LOGGER.warn("ExtensionFactory [{}] failed to get extension: {}",
                        factory.getClass().getSimpleName(), e.getMessage());
            }
        }
        return null;
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
        validateType(type);
        Spi spi = type.getAnnotation(Spi.class);
        return getExtension(type, spi.value());
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
        T extension = getExtension(type, key);
        if (extension != null) {
            return extension;
        }
        return getDefaultExtension(type);
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
        return getExtension(type, key) != null;
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
        validateType(type);
        return ExtensionLoader.getLoader(type).getExtensionKeys();
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
