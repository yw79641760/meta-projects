package com.softmegatron.shared.meta.data.serial;

import java.util.ServiceLoader;

import com.softmegatron.shared.meta.data.constants.MetaConstants;
import com.softmegatron.shared.meta.serial.spi.ObjectSerializer;

/**
 * ToStringSerializers
 * @description
 * @author <a href="mailto:wei.yan@softmegatron.com">yw</a>
 * @date 2026/2/5 15:21
 * @since 1.0.0
 */
public class ToStringSerializers {

    private static final ObjectSerializer DEFAULT_SERIALIZER = new DefaultObjectSerializer();

    private ToStringSerializers() {
    }

    /**
     * 获取生效的序列化实现
     * 指定首选且找到匹配，返回匹配的实现
     * 未指定首选或未找到匹配，返回第一个SPI实现
     * 否则返回默认实现
     */
    public static ObjectSerializer getDefault() {
        ServiceLoader<ObjectSerializer> loader = ServiceLoader.load(ObjectSerializer.class);
        String preferred = System.getProperty(MetaConstants.SERIALIZER_PREFERRED);
        // 查找匹配的实现
        if (preferred != null && !preferred.trim().isEmpty()) {
            return loader.stream()
                        .map(ServiceLoader.Provider::get)
                        .filter(e -> preferred.equals(e.getName()))
                        .findFirst()
                        .orElse(DEFAULT_SERIALIZER);
        }
        // 未指定首选，返回第一个SPI实现 
        return loader.stream()
                    .map(ServiceLoader.Provider::get)
                    .findFirst()
                    .orElse(DEFAULT_SERIALIZER);
    }
}
