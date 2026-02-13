package com.softmegatron.shared.meta.commons.extension.registry;

import com.softmegatron.shared.meta.commons.core.utils.ClassUtils;
import com.softmegatron.shared.meta.commons.extension.annotation.Spi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

import static com.softmegatron.shared.meta.commons.extension.enums.ExtensionProtocol.SUPPORTED_PROTOCOL;

/**
 * ExtensionLoader
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 5/4/20 1:58 PM
 */
public class ExtensionLoader<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExtensionLoader.class);
    /**
     * key -> extensionType
     */
    private final Map<String, Class<?>> EXTENSION_TYPE_CACHE = new ConcurrentHashMap<>();
    /**
     * key -> extensionInstance
     */
    private final Map<String, Object> EXTENSION_INSTANCE_CACHE = new ConcurrentHashMap<>();
    /**
     * spi类型
     */
    private Class<T> clazz;
    /**
     * spi标注
     */
    private Spi spi;
    /**
     * 是否已经初始化
     */
    private volatile boolean initialized = false;

    public ExtensionLoader(Class<T> clazz) {
        this.clazz = clazz;
        this.spi = clazz.getDeclaredAnnotation(Spi.class);
    }

    /**
     * 同步初始化
     */
    public void initialize() {
        if (initialized) {
            return;
        }
        synchronized (this) {
            if (initialized) {
                return;
            }
            loadExtensions();
            initialized = true;
        }
    }

    /**
     * 加载扩展的所有实现
     * <p>
     * 失败情况暂时仅打日志，不阻断
     * </p>
     */
    private void loadExtensions() {
        try {
            long startTime = System.currentTimeMillis();
            String fileName = spi.path() + File.separator + clazz.getName();
            ClassLoader classLoader = ClassUtils.getDefaultClassLoader();

            // 一次性获取所有资源URL
            Enumeration<URL> urls = classLoader.getResources(fileName);
            List<URL> urlList = Collections.list(urls);

            if (urlList.isEmpty()) {
                LOGGER.warn("Empty extension configuration found for extension. [className={}][fileName={}]",
                        clazz.getSimpleName(), fileName);
                return;
            }

            // 批量处理所有配置文件
            for (URL url : urlList) {
                try {
                    loadResource(url);
                } catch (Exception e) {
                    LOGGER.error("Failed to load extension resource from URL. [class={}][url={}]",
                            clazz.getSimpleName(), url, e);
                }
            }
            Long elapsedTime = System.currentTimeMillis() - startTime;
            LOGGER.info("Finished to load extensions. [class={}][size={}][elapsedTime={}]",
                    clazz.getSimpleName(), EXTENSION_INSTANCE_CACHE.size(), elapsedTime);

        } catch (IOException ioe) {
            LOGGER.error("Failed to load extension. [clazz={}]", clazz.getSimpleName(), ioe);
        } catch (Exception e) {
            LOGGER.error("Failed to load extension. [clazz={}]", clazz.getSimpleName(), e);
        }
    }

    /**
     * 加载配置文件
     *
     * @param url 配置文件URL
     */
    private void loadResource(URL url) {

        if (url == null || url.getPath() == null || url.getPath().isEmpty()) {
            LOGGER.error("Invalid extension resource path found. [url={}]", url);
            throw new IllegalArgumentException("Invalid extension resource path found.");
        }

        if (!SUPPORTED_PROTOCOL.contains(url.getProtocol())) {
            LOGGER.error("Unsupported extension protocol found. [protocol={}][url={}]",
                    url.getProtocol(), url.toString());
            throw new UnsupportedOperationException("Unsupported extension protocol found.");
        }

        // 解析配置文件
        Properties properties = new Properties();
        try (InputStream inputStream = url.openStream()) {
            properties.load(inputStream);
            processProperties(properties, url);
        } catch (IOException ioe) {
            LOGGER.error("Failed to load extension file. [url={}]", url, ioe);
            throw new IllegalStateException("Failed to load extension file. ", ioe);
        }
    }

    /**
     * 处理配置属性
     */
    private void processProperties(Properties properties, URL url) {
        properties.forEach((k, v) -> {
            String key = (String) k;
            String classPath = (String) v;
            if (key != null && !key.trim().isEmpty()
                    && classPath != null && !classPath.trim().isEmpty()) {
                try {
                    loadClass(key.trim(), classPath.trim());
                } catch (Exception e) {
                    LOGGER.error("Failed to load extension class. [key={}][classPath={}]",
                            key, classPath, e);
                    throw new IllegalStateException("Failed to load extension class.", e);
                }
            }
        });
    }

    /**
     * 加载扩展类型及实现类
     *
     * @param key       扩展键值
     * @param classPath 类路径
     */
    private void loadClass(final String key, final String classPath) {
        if (key == null || key.trim().isEmpty()
            || classPath == null || classPath.trim().isEmpty()) {
            LOGGER.error("Invalid extension class key and path found. [key={}][classPath={}]", 
                key, classPath);
            throw new IllegalArgumentException("Invalid extension class key and path found.");
        }
        try {
            Class<?> subClass = Class.forName(classPath.trim());
            // 类型检查
            if (!clazz.isAssignableFrom(subClass)) {
                LOGGER.error("Extension type and subClass mismatch found. [clazz={}][subClass={}]", clazz, subClass);
                throw new IllegalStateException("Extension type and subClass mismatch found.");
            }

            // 检查重复实现
            Class<?> oldClass = EXTENSION_TYPE_CACHE.get(key);
            if (oldClass == null) {
                EXTENSION_TYPE_CACHE.put(key, subClass);
                try {
                    Object instance = ClassUtils.newInstance(subClass);
                    EXTENSION_INSTANCE_CACHE.put(key, instance);
                } catch (Exception e) {
                    LOGGER.error("Failed to instantiate extension class. [classPath={}]", classPath, e);
                    throw new IllegalStateException("Failed to instantiate extension class.");
                }
            } else if (oldClass != subClass) {
                LOGGER.error("Duplicate extension subClass found. [clazz={}][oldClass={}][subClass={}]",
                         clazz, oldClass, subClass);
                throw new IllegalStateException("Duplicate extension subClass found.");
            }
        } catch (Exception e) {
            LOGGER.error("Failed to load extension class. [key={}][classPath={}]", key, classPath, e);
            throw new IllegalStateException("Failed to load extension class.");
        }
    }

    /**
     * 获取默认的扩展实现
     *
     * @return 默认扩展实例，如果不存在则返回null
     */
    public T getDefaultExtension() {
        return getExtension(spi.value());
    }

    /**
     * 获取指定键值的扩展实现
     *
     * @param key 扩展键值
     * @return 扩展实例，如果不存在则返回null
     */
    @SuppressWarnings("unchecked")
    public T getExtension(String key) {
        if (key == null) {
            return null;
        }
        initialize();
        return (T) EXTENSION_INSTANCE_CACHE.get(key);
    }

    /**
     * 获取指定键值的扩展实现，否则获取默认的扩展实现
     * 
     * @param key 扩展键值
     * @return 扩展实例，如果都不存在则返回null
     */
    @SuppressWarnings("unchecked")
    public T getExtensionOrDefault(String key) {
        if (key == null) {
            return null;
        }
        initialize();
        return (T) EXTENSION_INSTANCE_CACHE.getOrDefault(key, getDefaultExtension());
    }

    /**
     * 检查是否包含指定键值的扩展
     * 
     * @param key 扩展键值
     * @return 是否存在
     */
    public boolean hasExtension(String key) {
        if (key == null) {
            return false;
        }
        return EXTENSION_INSTANCE_CACHE.containsKey(key);
    }

    /**
     * 获取所有已加载的扩展键值
     * 
     * @return 扩展键值集合
     */
    public Set<String> getExtensionKeys() {
        return new HashSet<>(EXTENSION_INSTANCE_CACHE.keySet());
    }

    /**
     * 获取缓存大小
     * 
     * @return 缓存中的扩展数量
     */
    public int getCacheSize() {
        return EXTENSION_INSTANCE_CACHE.size();
    }

    /**
     * 清理缓存（谨慎使用）
     */
    public synchronized void clearCache() {
        EXTENSION_INSTANCE_CACHE.clear();
        EXTENSION_TYPE_CACHE.clear();
    }
}
