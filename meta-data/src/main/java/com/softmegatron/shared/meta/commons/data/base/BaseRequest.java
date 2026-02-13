package com.softmegatron.shared.meta.commons.data.base;

import java.util.UUID;

/**
 * BaseRequest
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 2019-04-22 23:29
 */
public class BaseRequest extends BaseModel{

    private static final long serialVersionUID = -8180724287110492064L;

    private String requestId;

    public BaseRequest() {
        this.requestId = UUID.randomUUID().toString();
    }

    public BaseRequest(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
