package com.softmegatron.shared.meta.data.enums;

import com.softmegatron.shared.meta.data.base.ReturnCode;

/**
 * CommonErrorCode
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 2019-05-12 00:31
 */
public enum CommonErrorCode implements ReturnCode {
    ;

    private final String code;

    private final String desc;

    CommonErrorCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
