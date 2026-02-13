package com.softmegatron.shared.meta.commons.extension.loader;

import com.softmegatron.shared.meta.commons.extension.annotation.Spi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ExtensionManager
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 5/4/20 2:22 PM
 */
public class ExtensionManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExtensionManager.class);
    /**
     * spi -> extensionLoader
     */
    private static final Map<Class<?>, ExtensionLoader<?>> EXTENSION_LOADER_CACHE = new ConcurrentHashMap<>();

    private ExtensionManager() {
    }

    /**
     * 获取指定类型的扩展加载器
     *
     * @param clazz
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> ExtensionLoader<T> getExtensionLoader(final Class<T> clazz) {
        if (clazz == null) {
            LOGGER.error("Empty class found in getExtensionLoader. [clazz={}]", clazz);
            throw new IllegalArgumentException("Empty class found in getExtensionLoader.");
        }
        if (!clazz.isInterface()) {
            LOGGER.error("Invalid class found in getExtensionLoader. [class={}]", clazz);
            throw new IllegalArgumentException("Invalid clazz found in getExtensionLoader.");
        }
        if (!clazz.isAnnotationPresent(Spi.class)) {
            LOGGER.error("Empty required annotation found in getExtensionLoader. [class={}]", clazz);
            throw new IllegalArgumentException("Empty required annotation found in getExtensionLoader.");
        }
        ExtensionLoader<T> loader = (ExtensionLoader<T>) EXTENSION_LOADER_CACHE.computeIfAbsent(clazz, cls -> new ExtensionLoader<>(cls));
        // 同步初始化，防止重复
        loader.initialize();
        return loader;
    }
}
