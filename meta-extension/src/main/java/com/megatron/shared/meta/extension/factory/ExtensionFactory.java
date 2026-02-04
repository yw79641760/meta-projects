package com.megatron.shared.meta.extension.factory;

import com.megatron.shared.meta.extension.annotation.SPI;

/**
 * ExtensionFactory
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
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
