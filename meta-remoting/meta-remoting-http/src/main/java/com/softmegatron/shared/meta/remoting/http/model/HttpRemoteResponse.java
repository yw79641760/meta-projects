package com.softmegatron.shared.meta.remoting.http.model;

import com.softmegatron.shared.meta.remoting.core.model.RemoteResponse;

/**
 * HttpRemoteResult
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 5/4/20 3:08 PM
 */
public class HttpRemoteResponse extends RemoteResponse {

    private static final long serialVersionUID = -815195827582835085L;
    /**
     * HTTP状态码
     */
    private Integer status;

    public HttpRemoteResponse() {
        super();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public static HttpRemoteResponse ofData(Integer status, Object value, Long elapsedTime) {
        HttpRemoteResponse result = new HttpRemoteResponse();
        result.setSuccess(true);
        result.setStatus(status);
        result.setValue(value);
        result.setElapsedTime(elapsedTime);
        return result;
    }

    public static HttpRemoteResponse ofException(Integer status, Exception cause, Long elapsedTime) {
        HttpRemoteResponse result = new HttpRemoteResponse();
        result.setSuccess(false);
        result.setStatus(status);
        result.setException(cause);
        result.setElapsedTime(elapsedTime);
        return result;
    }
}
