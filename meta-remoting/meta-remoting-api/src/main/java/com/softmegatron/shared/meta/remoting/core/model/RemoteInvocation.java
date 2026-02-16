package com.softmegatron.shared.meta.remoting.core.model;

import com.softmegatron.shared.meta.data.base.BaseModel;

import java.util.List;

import static com.softmegatron.shared.meta.remoting.core.constants.RemoteConstants.DEFAULT_CLIENT_TIMEOUT;

/**
 * RemoteInvocation
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 5/4/20 2:51 PM
 */
public class RemoteInvocation<P extends RemoteParam> extends BaseModel {

    private static final long serialVersionUID = -8013268272421466220L;
    /**
     * 入参列表
     */
    private List<P> params;
    /**
     * 客户端超时
     */
    private Integer clientTimeout = DEFAULT_CLIENT_TIMEOUT;

    public RemoteInvocation() {
        super();
    }

    public RemoteInvocation(List<P> params, Integer clientTimeout) {
        this.params = params;
        this.clientTimeout = clientTimeout;
    }

    public List<P> getParams() {
        return params;
    }

    public void setParams(List<P> params) {
        this.params = params;
    }

    public Integer getClientTimeout() {
        return clientTimeout;
    }

    public void setClientTimeout(Integer clientTimeout) {
        this.clientTimeout = clientTimeout;
    }
}
