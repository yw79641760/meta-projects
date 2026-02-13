package com.softmegatron.shared.meta.commons.remoting.http.enums;

import com.softmegatron.shared.meta.commons.data.base.BaseEnum;

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

    private final String code;

    private final String desc;

    private final String since;

    HttpMethod(String code, String desc, String since) {
        this.code = code;
        this.desc = desc;
        this.since = since;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    public String getSince() {
        return since;
    }
}
