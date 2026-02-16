package com.softmegatron.shared.meta.data.enums;

import com.softmegatron.shared.meta.data.base.ReturnCode;

/**
 * MessageCode
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 2019-04-22 23:32
 */
public enum MessageCode implements ReturnCode {
    /**
     * 成功
     */
    SUCCESSFUL("successful", "成功"),
    ;

    private final String code;

    private final String desc;

    MessageCode(String code, String desc) {
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
