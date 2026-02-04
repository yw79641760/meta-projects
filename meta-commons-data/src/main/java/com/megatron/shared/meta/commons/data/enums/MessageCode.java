package com.megatron.shared.meta.commons.data.enums;

import com.megatron.shared.meta.commons.data.base.ReturnCode;

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

    private String code;

    private String desc;

    MessageCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
