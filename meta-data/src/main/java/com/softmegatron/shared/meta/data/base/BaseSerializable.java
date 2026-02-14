package com.softmegatron.shared.meta.data.base;

import java.io.Serializable;

import com.softmegatron.shared.meta.data.serial.ToStringSerializers;
import com.softmegatron.shared.meta.serial.spi.ObjectSerializer;

/**
 * BaseToString
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 2019-04-22 23:25
 */
public abstract class BaseSerializable implements Serializable {

    private static final long serialVersionUID = 8445222833791659584L;

    // 缓存序列化器
    private static final ObjectSerializer SERIALIZER = ToStringSerializers.getDefault();

    @Override
    public String toString() {
        return SERIALIZER.serialize(this);
    }
}
