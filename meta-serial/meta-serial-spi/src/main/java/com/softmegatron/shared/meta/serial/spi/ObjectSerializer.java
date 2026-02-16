package com.softmegatron.shared.meta.serial.spi;

/**
 * ObjectSerializer
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">yw</a>
 * @description
 * @date 2026/2/5 12:23
 * @since 1.0.0
 */
public interface ObjectSerializer {

    /**
     * 序列化
     * @param obj
     * @return
     */
    String serialize(Object obj);

    /**
     * 序列化方案名称
     * @return
     */
    String getName();

    /**
     * 检查是否支持该对象的序列化
     * 
     * @param obj 待检查的对象
     * @return true 如果支持该对象的序列化，false otherwise
     * @see #serialize(Object)
     */
    boolean checkSupport(Object obj);
}
