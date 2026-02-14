package com.softmegatron.shared.meta.remoting.core.enums;

import com.softmegatron.shared.meta.data.base.BaseEnum;

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

    private final String code;

    private final String desc;

    RemoteProtocol(String code, String desc) {
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
