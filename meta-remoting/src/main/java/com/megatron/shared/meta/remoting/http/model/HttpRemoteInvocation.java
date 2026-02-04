package com.megatron.shared.meta.remoting.http.model;

import com.megatron.shared.meta.remoting.common.model.RemoteInvocation;
import com.megatron.shared.meta.remoting.http.enums.HttpMethod;

import java.util.List;
import java.util.Map;

/**
 * HttpRemoteInvocation
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 5/4/20 3:11 PM
 */
public class HttpRemoteInvocation extends RemoteInvocation<HttpRemoteParam> {

    private static final long serialVersionUID = 2706173426670115179L;
    /**
     * 请求URL
     */
    private String url;
    /**
     * 请求办法
     */
    private HttpMethod method;
    /**
     * header参数列表
     */
    private Map<String, String> headers;

    public HttpRemoteInvocation() {
        super();
    }

    public HttpRemoteInvocation(Builder builder) {
        super(builder.getParams(), builder.getClientTimeout());
        this.url = builder.getUrl();
        this.method = builder.getMethod();
        this.headers = builder.getHeaders();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public static class Builder {
        /**
         * 入参列表
         */
        private List<HttpRemoteParam> params;
        /**
         * 客户端超时
         */
        private Integer clientTimeout;
        /**
         * 请求URL
         */
        private String url;
        /**
         * 请求方法
         */
        private HttpMethod method;
        /**
         * header参数
         */
        private Map<String, String> headers;

        public Builder(String url, HttpMethod method, List<HttpRemoteParam> params) {
            this.url = url;
            this.method = method;
            this.params = params;
        }

        public List<HttpRemoteParam> getParams() {
            return params;
        }

        public Integer getClientTimeout() {
            return clientTimeout;
        }

        public String getUrl() {
            return url;
        }

        public HttpMethod getMethod() {
            return method;
        }

        public Map<String, String> getHeaders() {
            return headers;
        }

        public Builder clientTimeout(Integer clientTimeout) {
            this.clientTimeout = clientTimeout;
            return this;
        }

        public Builder headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public HttpRemoteInvocation build() {
            return new HttpRemoteInvocation(this);
        }
    }
}
