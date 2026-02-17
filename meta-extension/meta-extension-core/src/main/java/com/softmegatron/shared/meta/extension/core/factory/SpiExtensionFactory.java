package com.softmegatron.shared.meta.extension.core.factory;

import com.softmegatron.shared.meta.extension.core.annotation.Spi;
import com.softmegatron.shared.meta.extension.core.loader.ExtensionLoader;

/**
 * SPI 扩展工厂
 * <p>
 * 从 SPI 配置文件加载扩展实现。
 * </p>
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public class SpiExtensionFactory implements ExtensionFactory {

    @Override
    public <T> T getExtension(Class<T> type, String name) {
        if (type == null || !type.isInterface() || !type.isAnnotationPresent(Spi.class)) {
            return null;
        }
        if (name == null || name.isEmpty()) {
            return null;
        }
        ExtensionLoader<T> loader = ExtensionLoader.getLoader(type);
        return loader.getExtension(name);
    }

    @Override
    public int getOrder() {
        return 100;
    }
}
