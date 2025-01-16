package com.magi.meta.remoting.http.constants;

/**
 * HttpRemoteConstants
 *
 * @author <a href="mailto:akagi@magi.com">akagi</a>
 * @version 1.0.0
 * @since 5/4/20 2:57 PM
 */
public interface HttpRemoteConstants {
    /**
     * 链接超时，单位：毫秒
     */
    Long DEFAULT_CONN_TIMEOUT = 10000L;
    /**
     * 读超时，单位：毫秒
     */
    Long DEFAULT_READ_TIMEOUT = 10000L;
    /**
     * 写超时，单位：毫秒
     */
    Long DEFAULT_WRITE_TIMEOUT = 10000L;
    /**
     * 断连自动重试
     */
    Boolean CONN_FAILURE_RETRY = Boolean.TRUE;
}
