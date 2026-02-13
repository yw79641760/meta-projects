package com.softmegatron.shared.meta.commons.data.base;

import java.io.Serializable;

/**
 * BaseEnum
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 2019-04-22 23:27
 */
public interface BaseEnum extends Serializable {

    /**
     * 获取枚举类型说明信息
     * 
     * @return
     */
    String getDesc();
}
