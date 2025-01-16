package com.magi.meta.extension.factory;

import com.magi.meta.extension.annotation.SPI;

/**
 * ExtensionFactory
 *
 * @author <a href="mailto:akagi@magi.com">akagi</a>
 * @version 1.0.0
 * @since 5/4/20 1:50 PM
 */
@SPI("spi")
public interface ExtensionFactory {
    /**
     * 根据扩展点接口类型及键值获取扩展
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T getExtension(String key, Class<T> clazz);
}
