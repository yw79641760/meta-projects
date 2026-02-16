package com.softmegatron.shared.meta.data.base;

import com.softmegatron.shared.meta.data.enums.MessageCode;

import java.util.List;
import java.util.Map;

/**
 * ListResponse
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 2019-04-22 23:59
 */
public class ListResponse<T> extends BaseResponse<List<T>>{

    private static final long serialVersionUID = 6296121369376533277L;

    public ListResponse() {
    }

    public ListResponse(String requestId, List<T> data) {
        super(requestId, data);
    }

    public ListResponse(ReturnCode returnCode) {
        super(returnCode);
    }

    public ListResponse(Builder<T> builder) {
        super(builder.getRequestId(), builder.isSuccess(),
              builder.getCode(), builder.getMessage(),
              builder.getFeature(), builder.getData());
    }

    public static class Builder<T> extends BaseModel {

        private static final long serialVersionUID = -5511306069185533957L;
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
         * 数据结果
         */
        private List<T> data;

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

        public List<T> getData() {
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

        public Builder<T> data(List<T> data) {
            this.data = data;
            return this;
        }

        public ListResponse<T> build() {
            return new ListResponse<T>(this);
        }
    }
}
