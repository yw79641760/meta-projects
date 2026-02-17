package com.softmegatron.shared.meta.extension.core.loader;

import com.softmegatron.shared.meta.core.utils.ClassUtils;
import com.softmegatron.shared.meta.extension.core.annotation.Spi;
import com.softmegatron.shared.meta.extension.core.enums.ExtensionScope;
import com.softmegatron.shared.meta.extension.core.exception.ExtensionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 扩展加载器
 * <p>
 * 负责加载指定扩展点接口的所有实现，支持单例和多例模式。
 * </p>
 *
 * @param <T> 扩展点接口类型
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public class ExtensionLoader<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExtensionLoader.class);

    /** ExtensionLoader 缓存 */
    private static final ConcurrentMap<Class<?>, ExtensionLoader<?>> LOADER_CACHE = new ConcurrentHashMap<>();

    /** 扩展点接口类型 */
    private final Class<T> type;

    /** Spi 注解配置 */
    private final Spi spi;

    /** 单例缓存：key -> 实例 */
    private final Map<String, T> singletonCache = new ConcurrentHashMap<>();

    /** 类定义缓存：key -> Class */
    private final Map<String, Class<? extends T>> classCache = new ConcurrentHashMap<>();

    /** 加载状态 */
    private volatile boolean loaded = false;

    /** 加载锁 */
    private final Object loadLock = new Object();

    /**
     * 构造函数
     *
     * @param type 扩展点接口类型
     */
    ExtensionLoader(Class<T> type) {
        this.type = type;
        this.spi = type.getAnnotation(Spi.class);
    }

    /**
     * 获取指定类型的 ExtensionLoader
     *
     * @param type 扩展点接口类型
     * @param <T>  扩展点类型
     * @return ExtensionLoader 实例
     * @throws ExtensionException 如果类型无效
     */
    @SuppressWarnings("unchecked")
    public static <T> ExtensionLoader<T> getLoader(Class<T> type) {
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

    /**
     * 获取指定扩展实现
     *
     * @param key 扩展键值
     * @return 扩展实例，不存在则返回 null
     */
    public T getExtension(String key) {
        if (key == null || key.isEmpty()) {
            return null;
        }
        loadExtensions();
        return getOrCreateInstance(key);
    }

    /**
     * 获取默认扩展实现
     *
     * @return 默认扩展实例，不存在则返回 null
     */
    public T getDefaultExtension() {
        return getExtension(spi.value());
    }

    /**
     * 获取指定扩展，不存在则返回默认扩展
     *
     * @param key 扩展键值
     * @return 扩展实例，都不存在则返回 null
     */
    public T getExtensionOrDefault(String key) {
        T extension = getExtension(key);
        if (extension != null) {
            return extension;
        }
        return getDefaultExtension();
    }

    /**
     * 检查扩展是否存在
     *
     * @param key 扩展键值
     * @return 是否存在
     */
    public boolean hasExtension(String key) {
        if (key == null || key.isEmpty()) {
            return false;
        }
        loadExtensions();
        return classCache.containsKey(key);
    }

    /**
     * 获取所有扩展键值
     *
     * @return 扩展键值集合
     */
    public Set<String> getExtensionKeys() {
        loadExtensions();
        return new HashSet<>(classCache.keySet());
    }

    /**
     * 加载扩展配置
     */
    private void loadExtensions() {
        if (loaded) {
            return;
        }
        synchronized (loadLock) {
            if (loaded) {
                return;
            }
            doLoadExtensions();
            loaded = true;
        }
    }

    /**
     * 执行扩展加载
     */
    private void doLoadExtensions() {
        String fileName = spi.path() + "/" + type.getName();
        ClassLoader classLoader = ClassUtils.getDefaultClassLoader();

        try {
            Enumeration<URL> urls = classLoader.getResources(fileName);
            if (!urls.hasMoreElements()) {
                LOGGER.debug("No extension configuration found for [{}]", type.getName());
                return;
            }

            Map<String, Class<? extends T>> tempClassCache = new HashMap<>();

            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                loadResource(url, tempClassCache);
            }

            if (!tempClassCache.isEmpty()) {
                classCache.putAll(tempClassCache);
                LOGGER.info("Loaded {} extensions for [{}]", tempClassCache.size(), type.getName());
            }

        } catch (IOException e) {
            throw new ExtensionException("Failed to load extensions for " + type.getName(), e);
        }
    }

    /**
     * 加载单个配置文件
     */
    private void loadResource(URL url, Map<String, Class<? extends T>> tempClassCache) {
        try (InputStream is = url.openStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                parseLine(line, url, lineNumber, tempClassCache);
            }

        } catch (IOException e) {
            LOGGER.warn("Failed to read extension config from [{}]", url, e);
        }
    }

    /**
     * 解析配置行
     */
    @SuppressWarnings("unchecked")
    private void parseLine(String line, URL url, int lineNumber, Map<String, Class<? extends T>> tempClassCache) {
        int eqIndex = line.indexOf('=');
        if (eqIndex <= 0) {
            LOGGER.warn("Invalid extension config at [{}:{}] : {}", url, lineNumber, line);
            return;
        }

        String key = line.substring(0, eqIndex).trim();
        String className = line.substring(eqIndex + 1).trim();

        if (key.isEmpty() || className.isEmpty()) {
            LOGGER.warn("Invalid extension config at [{}:{}] : {}", url, lineNumber, line);
            return;
        }

        if (tempClassCache.containsKey(key)) {
            LOGGER.warn("Duplicate extension key [{}] at [{}:{}]", key, url, lineNumber);
            return;
        }

        try {
            Class<?> clazz = Class.forName(className, false, ClassUtils.getDefaultClassLoader());

            if (!type.isAssignableFrom(clazz)) {
                LOGGER.warn("Extension [{}] does not implement [{}] at [{}:{}]",
                        className, type.getName(), url, lineNumber);
                return;
            }

            tempClassCache.put(key, (Class<? extends T>) clazz);

        } catch (ClassNotFoundException e) {
            LOGGER.warn("Extension class not found [{}] at [{}:{}]", className, url, lineNumber);
        }
    }

    /**
     * 获取或创建实例
     */
    private T getOrCreateInstance(String key) {
        ExtensionScope scope = spi.scope();

        if (scope == ExtensionScope.SINGLETON) {
            return singletonCache.computeIfAbsent(key, this::createInstance);
        }

        return createInstance(key);
    }

    /**
     * 创建扩展实例
     */
    private T createInstance(String key) {
        Class<? extends T> clazz = classCache.get(key);
        if (clazz == null) {
            return null;
        }
        try {
            return ClassUtils.newInstance(clazz);
        } catch (Exception e) {
            throw new ExtensionException("Failed to create extension instance: " + key, e);
        }
    }
}
