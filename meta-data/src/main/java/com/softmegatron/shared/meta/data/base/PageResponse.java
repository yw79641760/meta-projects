package com.softmegatron.shared.meta.data.base;

import com.softmegatron.shared.meta.data.enums.MessageCode;

import java.util.List;
import java.util.Map;

/**
 * PageResponse
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 5/3/20 9:59 PM
 */
public class PageResponse<T> extends BaseResponse<PageData<T>>{

    private static final long serialVersionUID = -8209295883024586248L;

    public PageResponse() {
        super();
    }

    public PageResponse(String requestId, List<T> data, PageInfo pageInfo) {
        super(requestId, new PageData<T>(data, pageInfo));
    }

    public PageResponse(String requestId, PageData<T> data) {
        super(requestId, data);
    }

    public PageResponse(ReturnCode returnCode) {
        super(returnCode);
    }

    public PageResponse(String requestId, boolean success, String code, String message, Map<?, ?> feature, PageData<T> data) {
        super(requestId, success, code, message, feature, data);
    }

    public PageResponse(Builder<T> builder) {
        super(builder.getRequestId(), builder.isSuccess(),
              builder.getCode(), builder.getMessage(),
              builder.getFeature(), builder.getData());
    }

    public static class Builder<T> extends BaseModel {

        private static final long serialVersionUID = -4635556527593098143L;
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
        private PageData<T> data;

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

        public PageData<T> getData() {
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

        public Builder<T> data(PageData<T> data) {
            this.data = data;
            return this;
        }

        public PageResponse<T> build() {
            return new PageResponse<>(this);
        }
    }
}
