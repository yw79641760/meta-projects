package com.megatron.shared.meta.remoting.common.model;

import com.megatron.shared.meta.commons.data.base.BaseModel;

/**
 * RemoteResponse
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 5/4/20 2:53 PM
 */
public class RemoteResponse extends BaseModel {

    private static final long serialVersionUID = 1173027738259664646L;
    /**
     * 是否成功
     */
    private boolean success;
    /**
     * 结果
     */
    private Object value;
    /**
     * 异常
     */
    private Throwable exception;
    /**
     * 耗时
     */
    private Long elapsedTime;

    public RemoteResponse() {
        super();
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public Long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(Long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }
}
