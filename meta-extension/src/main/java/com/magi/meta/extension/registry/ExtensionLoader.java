package com.magi.meta.extension.registry;

import com.magi.meta.commons.utils.bean.ClassUtils;
import com.magi.meta.extension.annotation.SPI;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import static com.magi.meta.extension.enums.ExtensionProtocol.SUPPORTED_PROTOCOL;

/**
 * ExtensionLoader
 *
 * @author <a href="mailto:akagi@magi.com">akagi</a>
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
    private SPI spi;

    public ExtensionLoader(Class<T> clazz) {
        this.clazz = clazz;
        this.spi = clazz.getDeclaredAnnotation(SPI.class);
        // 初始化loader
        loadExtensions();
    }

    /**
     * 加载扩展的所有实现
     * <p>失败情况暂时仅打日志，不阻断</p>
     */
    private void loadExtensions() {
        // classpath下的路径
        String fileName = spi.path() + clazz.getName();
        try {
            ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
            Enumeration<URL> urls = classLoader.getResources(fileName);
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                loadResource(url);
            }
        } catch (IOException ioe) {
            LOGGER.error("Failed to load extension file. [fileName={}]", fileName, ioe);
        } catch (Exception e) {
            LOGGER.error("Failed to load extensions. [fileName={}]", fileName, e);

        }
    }

    /**
     * 加载配置文件
     *
     * @param url
     */
    private void loadResource(URL url) {
        if (url == null
                || StringUtils.isEmpty(url.getPath())
                || !SUPPORTED_PROTOCOL.contains(url.getProtocol())) {
            return;
        }
        //  解析配置
        Properties properties = new Properties();
        try (InputStream inputStream = url.openStream()){
            properties.load(inputStream);
            properties.forEach((k, v) -> {
                String key = (String) k;
                String classPath = (String) v;
                if (StringUtils.isNotBlank(key)
                    && StringUtils.isNotBlank(classPath)) {
                    try {
                        loadClass(key, classPath);
                    } catch (Exception e) {
                        LOGGER.error("Failed to load extension class. [url={}][key={}][classPath={}]",
                                     url, key, classPath, e);
                        throw new IllegalStateException("Failed to load extension class.", e);
                    }
                }
            });
        } catch (IOException ioe) {
            LOGGER.error("Failed to load extension file. [url={}]", url, ioe);
            throw new IllegalStateException("Failed to load extension file. ", ioe);
        }
    }

    /**
     * 加载扩展类型及实现类
     *
     * @param key
     * @param classPath
     * @throws ClassNotFoundException
     */
    private void loadClass(final String key, final String classPath) throws ClassNotFoundException {
        Class<?> subClass = Class.forName(classPath);
        if (!clazz.isAssignableFrom(subClass)) {
            LOGGER.error("Extension type and subClass mismatch found. [clazz={}][subClass={}]", clazz, subClass);
            throw new IllegalStateException("Extension type and subClass mismatch found.");
        }
        Class<?> oldClass = EXTENSION_TYPE_CACHE.get(key);
        if (oldClass == null) {
            EXTENSION_TYPE_CACHE.put(key, subClass);
            EXTENSION_INSTANCE_CACHE.put(key, ClassUtils.newInstance(subClass));
        } else if (oldClass != subClass) {
            LOGGER.error("Duplicate extension subClass found. [clazz={}][oldClass={}][subClass={}]",
                         clazz, oldClass, subClass);
            throw new IllegalStateException("Duplicate extension subClass found.");
        }
    }

    /**
     * 获取默认的扩展实现
     *
     * @return
     */
    public T getDefaultExtension() {
        return getExtension(spi.value());
    }

    /**
     * 获取指定键值的扩展实现
     *
     * @param key
     * @return
     */
    public T getExtension(String key) {
        if (key != null && !EXTENSION_INSTANCE_CACHE.containsKey(key)) {
            loadExtensions();
        }
        return (T) EXTENSION_INSTANCE_CACHE.get(key);
    }

    /**
     * 获取指定键值的扩展实现，否则获取默认的扩展实现
     * 
     * @param key
     * @return
     */
    public T getExtensionOrDefault(String key) {
        if (key != null && !EXTENSION_INSTANCE_CACHE.containsKey(key)) {
            loadExtensions();
        }
        return (T) EXTENSION_INSTANCE_CACHE.getOrDefault(key, getDefaultExtension());
    }
}
