package com.magi.meta.remoting.http;

import com.magi.meta.remoting.common.RemoteService;
import com.magi.meta.remoting.http.model.HttpRemoteInvocation;
import com.magi.meta.remoting.http.model.HttpRemoteResponse;
import com.magi.meta.remoting.http.utils.EasyX509TrustManager;
import com.magi.meta.remoting.http.utils.HttpRemoteUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.magi.meta.remoting.http.constants.HttpRemoteConstants.CONN_FAILURE_RETRY;
import static com.magi.meta.remoting.http.constants.HttpRemoteConstants.DEFAULT_CONN_TIMEOUT;
import static com.magi.meta.remoting.http.constants.HttpRemoteConstants.DEFAULT_READ_TIMEOUT;
import static com.magi.meta.remoting.http.constants.HttpRemoteConstants.DEFAULT_WRITE_TIMEOUT;
import static com.magi.meta.remoting.http.utils.HttpRemoteUtils.createEasySSLContext;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * HttpRemoteServiceImpl
 *
 * @author <a href="mailto:akagi@magi.com">akagi</a>
 * @version 1.0.0
 * @since 5/4/20 5:01 PM
 */
public class HttpRemoteServiceImpl implements RemoteService<HttpRemoteInvocation, HttpRemoteResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRemoteServiceImpl.class);

    private static OkHttpClient client;

    static {
        try {
            client = new OkHttpClient.Builder()
                    .connectTimeout(DEFAULT_CONN_TIMEOUT, MILLISECONDS)
                    .readTimeout(DEFAULT_READ_TIMEOUT, MILLISECONDS)
                    .writeTimeout(DEFAULT_WRITE_TIMEOUT, MILLISECONDS)
                    .retryOnConnectionFailure(CONN_FAILURE_RETRY)
                    // 设置https配置，此处忽略所有证书
                    .sslSocketFactory(createEasySSLContext().getSocketFactory(),
                                      new EasyX509TrustManager(null))
                    .build();
        } catch (Exception e) {
            LOGGER.error("Failed to init okhttp client.", e);
        }
    }
    @Override
    public HttpRemoteResponse invoke(HttpRemoteInvocation invocation) {
        checkNotNull(invocation, "Empty invocation found in http invoke.");
        checkArgument(StringUtils.isNotEmpty(invocation.getUrl()), "Empty url found in http invoke.");
        checkNotNull(invocation.getMethod(), "Empty method found in http invoke.");
        long current = System.currentTimeMillis();
        HttpRemoteResponse remoteResponse = null;
        Response httpResponse = null;
        Request request = null;
        try {
           request = HttpRemoteUtils.buildRequest(invocation);
           httpResponse = client.newCall(request).execute();
           if (httpResponse.isSuccessful()) {

           }
        } catch (Exception e) {

        } finally {

        }
        return null;
    }
}
