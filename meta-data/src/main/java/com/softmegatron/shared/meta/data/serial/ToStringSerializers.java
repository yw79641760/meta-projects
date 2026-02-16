package com.softmegatron.shared.meta.data.serial;

import java.util.ServiceLoader;

import com.softmegatron.shared.meta.data.constants.MetaConstants;
import com.softmegatron.shared.meta.serial.spi.ObjectSerializer;

/**
 * 序列化器工厂
 * <p>
 * 通过 JDK SPI 机制加载 {@link ObjectSerializer} 实现，
 * 支持通过系统属性指定首选实现。
 * </p>
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">yw</a>
 * @since 1.0.0
 */
public final class ToStringSerializers {

    private static final ObjectSerializer DEFAULT_SERIALIZER = new DefaultObjectSerializer();

    private static volatile ObjectSerializer cachedSerializer;

    private ToStringSerializers() {
    }

    /**
     * 获取序列化器实现
     * <p>
     * 选择优先级：
     * <ol>
     *   <li>通过系统属性 {@code meta.data.serializer.preferred} 指定的首选实现</li>
     *   <li>SPI 配置的第一个实现</li>
     *   <li>默认实现（反射 toString）</li>
     * </ol>
     * </p>
     *
     * @return 序列化器实例
     */
    public static ObjectSerializer getDefault() {
        if (cachedSerializer != null) {
            return cachedSerializer;
        }
        synchronized (ToStringSerializers.class) {
            if (cachedSerializer != null) {
                return cachedSerializer;
            }
            cachedSerializer = loadSerializer();
            return cachedSerializer;
        }
    }

    /**
     * 重置缓存（用于测试或动态切换实现）
     */
    public static synchronized void resetCache() {
        cachedSerializer = null;
    }

    private static ObjectSerializer loadSerializer() {
        ServiceLoader<ObjectSerializer> loader = ServiceLoader.load(ObjectSerializer.class);
        String preferred = System.getProperty(MetaConstants.SERIALIZER_PREFERRED);

        if (preferred != null && !preferred.trim().isEmpty()) {
            return loader.stream()
                    .map(ServiceLoader.Provider::get)
                    .filter(e -> preferred.equals(e.getName()))
                    .findFirst()
                    .orElse(DEFAULT_SERIALIZER);
        }

        return loader.stream()
                .map(ServiceLoader.Provider::get)
                .findFirst()
                .orElse(DEFAULT_SERIALIZER);
    }
}
