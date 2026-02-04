package com.megatron.shared.meta.extension.factory;

import com.megatron.shared.meta.extension.registry.ExtensionLoader;
import com.megatron.shared.meta.extension.registry.ExtensionRegistry;

/**
 * SpiExtensionFactory
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 5/4/20 2:21 PM
 */
public class SpiExtensionFactory implements ExtensionFactory{

    @Override
    public <T> T getExtension(String key, Class<T> clazz) {
        ExtensionLoader<T> extensionLoader = ExtensionRegistry.getExtensionLoader(clazz);
        if (extensionLoader == null) {
            return null;
        }
        return extensionLoader.getExtensionOrDefault(key);
    }
}
