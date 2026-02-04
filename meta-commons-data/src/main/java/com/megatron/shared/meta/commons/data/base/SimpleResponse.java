package com.megatron.shared.meta.commons.data.base;

import com.megatron.shared.meta.commons.data.enums.MessageCode;

import java.util.Collections;
import java.util.Map;

/**
 * SimpleResponse
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 2019-04-22 23:31
 */
public class SimpleResponse extends BaseModel {

    private static final long serialVersionUID = -7655934556447442696L;
    /**
     * 请求ID
     */
    private String requestId;
    /**
     * 是否成功
     */
    private boolean success;
    /**
     * 返回码
     */
    private String code;
    /**
     * 返回消息提示
     */
    private String message;
    /**
     * 特征变量
     */
    private Map feature;

    public SimpleResponse() {
        super();
        this.success = true;
        this.code = MessageCode.SUCCESSFUL.getCode();
        this.message = MessageCode.SUCCESSFUL.getDesc();
        this.feature = Collections.EMPTY_MAP;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map getFeature() {
        return feature;
    }

    public void setFeature(Map feature) {
        this.feature = feature;
    }

    public SimpleResponse(String requestId) {
        this();
        this.requestId = requestId;
    }

    public SimpleResponse(String requestId, boolean success, String code, String message, Map feature) {
        super();
        this.requestId = requestId;
        this.success = success;
        this.code = code;
        this.message = message;
        this.feature = feature;
    }

    public SimpleResponse(Builder builder) {
        super();
        this.requestId = builder.getRequestId();
        this.success = builder.isSuccess();
        this.code = builder.getCode();
        this.message = builder.getMessage();
        this.feature = builder.getFeature();
    }

    public SimpleResponse(ReturnCode returnCode) {
        this.success = returnCode instanceof MessageCode;
        this.code = returnCode.getCode();
        this.message = returnCode.getDesc();
    }

    public static class Builder extends BaseModel {

        private static final long serialVersionUID = 517740983797022733L;
        /**
         * 请求ID
         */
        private String requestId;
        /**
         * 是否成功
         */
        private boolean success;
        /**
         * 返回码
         */
        private String code;
        /**
         * 返回消息提示
         */
        private String message;
        /**
         * 特征变量
         */
        private Map feature;

        public Builder() {
            super();
        }

        public String getRequestId() {
            return requestId;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public Map getFeature() {
            return feature;
        }

        public Builder requestId(String requestId) {
            this.requestId = requestId;
            return this;
        }

        public Builder success(boolean success) {
            this.success = success;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder message(String message, Object... args) {
            this.message = String.format(message, args);
            return this;
        }

        public Builder success() {
            this.success = true;
            this.code = MessageCode.SUCCESSFUL.getCode();
            this.message = MessageCode.SUCCESSFUL.getDesc();
            return this;
        }

        public Builder failure(ReturnCode returnCode, Object... args) {
            this.success = returnCode instanceof MessageCode;
            this.code = returnCode.getCode();
            this.message = String.format(returnCode.getDesc(), args);
            return this;
        }

        public Builder feature(Map feature) {
            this.feature = feature;
            return this;
        }

        public SimpleResponse build() {
            return new SimpleResponse(this);
        }
    }
}
