package com.softmegatron.shared.meta.extension.core.enums;

import com.softmegatron.shared.meta.data.base.BaseEnum;
/**
 * ExtensionScope
 * 
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @date 2026/02/13 19:46
 * @since 1.0.0
 */
public enum ExtensionScope implements BaseEnum{

    SINGLETON("SINGLETON", "单例模式"),
    PROTOTYPE("PROTOTYPE", "多例模式"),
    ;

    private final String code;

    private final String desc;

    private ExtensionScope(String code, String desc)  {
        this.code = code;
        this.desc = desc;
    }    

    public String getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }

}
