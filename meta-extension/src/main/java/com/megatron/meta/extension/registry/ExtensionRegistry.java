package com.softmegatron.meta.extension.registry;

import com.softmegatron.meta.extension.annotation.SPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ExtensionRegistry
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 5/4/20 2:22 PM
 */
public class ExtensionRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExtensionRegistry.class);
    /**
     * spi -> extensionLoader
     */
    private static final Map<Class<?>, ExtensionLoader<?>> EXTENSION_LOADER_CACHE = new ConcurrentHashMap<>();

    /**
     * 获取指定类型的扩展加载器
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> ExtensionLoader<T> getExtensionLoader(final Class<T> clazz) {
        if (clazz == null) {
            LOGGER.error("Empty class found in getExtensionLoader. [clazz={}]", clazz);
            throw new IllegalArgumentException("Empty class found in getExtensionLoader.");
        }
        if (!clazz.isInterface()) {
            LOGGER.error("Invalid class found in getExtensionLoader. [class={}]", clazz);
            throw new IllegalArgumentException("Invalid clazz found in getExtensionLoader.");
        }
        if (!clazz.isAnnotationPresent(SPI.class)) {
            LOGGER.error("Empty required annotation found in getExtensionLoader. [class={}]", clazz);
            throw new IllegalArgumentException("Empty required annotation found in getExtensionLoader.");
        }
        ExtensionLoader<T> extensionLoader = (ExtensionLoader<T>) EXTENSION_LOADER_CACHE.get(clazz);
        if (extensionLoader != null) {
            return extensionLoader;
        }
        EXTENSION_LOADER_CACHE.putIfAbsent(clazz, new ExtensionLoader<>(clazz));
        return (ExtensionLoader<T>) EXTENSION_LOADER_CACHE.get(clazz);
    }
}
