package com.softmegatron.meta.remoting.common.enums;

import com.softmegatron.meta.commons.data.base.BaseEnum;

/**
 * RemotingProtocol
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 5/4/20 2:45 PM
 */
public enum RemoteProtocol implements BaseEnum {
    /**
     * okhttp3
     */
    HTTP("http", "HTTP"),
    ;

    private String code;

    private String desc;

    RemoteProtocol(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
