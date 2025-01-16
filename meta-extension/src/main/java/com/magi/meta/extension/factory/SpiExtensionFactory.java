package com.magi.meta.extension.factory;

import com.magi.meta.extension.registry.ExtensionLoader;
import com.magi.meta.extension.registry.ExtensionRegistry;

/**
 * SpiExtensionFactory
 *
 * @author <a href="mailto:akagi@magi.com">akagi</a>
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
