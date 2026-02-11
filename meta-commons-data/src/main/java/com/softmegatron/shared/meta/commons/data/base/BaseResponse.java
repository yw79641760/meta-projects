package com.softmegatron.shared.meta.commons.data.base;

import com.softmegatron.shared.meta.commons.data.enums.MessageCode;

import java.util.Map;

/**
 * BaseResponse
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 2019-04-22 23:45
 */
public class BaseResponse<T> extends SimpleResponse{

    private static final long serialVersionUID = -6533323665813662409L;

    private T data;

    public BaseResponse() {
        super();
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public BaseResponse(String requestId, T data) {
        super(requestId);
        this.data = data;
    }

    public BaseResponse(String requestId, boolean success, String code, String message, Map<?, ?> feature, T data) {
        super(requestId, success, code, message, feature);
        this.data = data;
    }

    public BaseResponse(ReturnCode returnCode) {
        super(returnCode);
    }

    public BaseResponse(Builder<T> builder) {
        super(builder.getRequestId(), builder.isSuccess(), builder.getCode(), builder.getMessage(), builder.getFeature());
        this.data = builder.getData();
    }

    public static class Builder<T> extends BaseModel{

        private static final long serialVersionUID = 454348829447194412L;
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
        private Map<?, ?> feature;
        /**
         * 数据
         */
        private T data;

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

        public Map<?, ?> getFeature() {
            return feature;
        }

        public T getData() {
            return data;
        }

        public Builder<T> requestId(String requestId) {
            this.requestId = requestId;
            return this;
        }

        public Builder<T> success(boolean success) {
            this.success = success;
            return this;
        }

        public Builder<T> code(String code) {
            this.code = code;
            return this;
        }

        public Builder<T> message(String message, Object... args) {
            this.message = String.format(message, args);
            return this;
        }

        public Builder<T> success() {
            this.success = true;
            this.code = MessageCode.SUCCESSFUL.getCode();
            this.message = MessageCode.SUCCESSFUL.getDesc();
            return this;
        }

        public Builder<T> failure(ReturnCode returnCode, Object... args) {
            this.success = returnCode instanceof MessageCode;
            this.code = returnCode.getCode();
            this.message = String.format(returnCode.getDesc(), args);
            return this;
        }

        public Builder<T> feature(Map<?, ?> feature) {
            this.feature = feature;
            return this;
        }

        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public BaseResponse<T> build() {
            return new BaseResponse<>(this);
        }
    }
}
