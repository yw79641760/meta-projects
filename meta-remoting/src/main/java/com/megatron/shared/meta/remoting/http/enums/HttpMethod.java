package com.megatron.shared.meta.remoting.http.enums;

import com.megatron.shared.meta.commons.data.base.BaseEnum;

/**
 * HttpMethod
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 5/4/20 3:00 PM
 */
public enum HttpMethod implements BaseEnum {
    /**
     *
     */
    GET("GET", "", "1.0"),
    POST("POST", "", "1.0"),
    HEAD("HEAD", "", "1.0"),
    PUT("PUT", "", "1.1"),
    DELETE("DELETE", "", "1.1"),
    CONNECT("CONNECT", "", "1.1"),
    OPTIONS("OPTIONS", "", "1.1"),
    TRACE("TRACE", "", "1.1"),
    PATCH("PATCH", "", "1.1"),
    ;

    private String code;

    private String desc;

    private String since;

    HttpMethod(String code, String desc, String since) {
        this.code = code;
        this.desc = desc;
        this.since = since;
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

    public String getSince() {
        return since;
    }

    public void setSince(String since) {
        this.since = since;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
