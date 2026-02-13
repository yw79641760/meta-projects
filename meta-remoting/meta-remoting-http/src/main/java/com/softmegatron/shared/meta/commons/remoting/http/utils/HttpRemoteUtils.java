package com.softmegatron.shared.meta.commons.remoting.http.utils;

import com.softmegatron.shared.meta.commons.remoting.http.model.FileRemoteParam;
import com.softmegatron.shared.meta.commons.remoting.http.model.HttpRemoteInvocation;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.MultipartBody.Part;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.Closeable;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;

import static java.util.stream.Collectors.toMap;

/**
 * HttpRemoteUtils
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 5/4/20 3:32 PM
 */
public class HttpRemoteUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRemoteUtils.class);

    private HttpRemoteUtils() {
    }

    /**
     * 绕过SSL验证
     *
     * @return
     * @throws IOException
     */
    public static SSLContext createEasySSLContext() throws IOException {
        try {
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, null, null);
            return context;
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    public static Request buildRequest(HttpRemoteInvocation invocation) {
        // 添加headers
        Request.Builder builder = enrichHeaders(new Request.Builder(), invocation);
        switch (invocation.getMethod()) {
            case GET:
                return buildGetRequest(builder, invocation);
            case POST:
                return buildPostRequest(builder, invocation);
            case HEAD:
                return buildHeadRequest(builder, invocation);
            case PUT:
                return buildPutRequest(builder, invocation);
            case DELETE:
                return buildDeleteRequest(builder, invocation);
            case PATCH:
                return buildPatchRequest(builder, invocation);
            case OPTIONS:
            case TRACE:
            case CONNECT:
            default:
                throw new UnsupportedOperationException("Unsupported http method found in buildRequest.");
        }
    }

    /**
     * 添加HTTP headers参数
     *
     * @param builder
     * @param invocation
     * @return
     */
    private static Request.Builder enrichHeaders(Request.Builder builder,
                                                 HttpRemoteInvocation invocation) {
        if (invocation == null
                || invocation.getHeaders() == null
                || invocation.getHeaders().isEmpty()) {
            return builder;
        }
        invocation.getHeaders().entrySet().stream()
                .filter(e -> StringUtils.isNotEmpty(e.getKey()) && StringUtils.isNotEmpty(e.getValue()))
                .collect(toMap(Entry::getKey, Entry::getValue))
                .forEach(builder::addHeader);
        return builder;
    }

    /**
     * 构造GET请求
     *
     * @param builder
     * @param invocation
     * @return
     */
    private static Request buildGetRequest(Request.Builder builder, HttpRemoteInvocation invocation) {
        return builder.url(appendQueryParameters(invocation)).get().build();
    }

    /**
     * 拼接GET请求带参数的URL
     *
     * @param invocation
     * @return
     */
    private static String appendQueryParameters(HttpRemoteInvocation invocation) {
        if (invocation.getParams() == null || invocation.getParams().isEmpty()) {
            return invocation.getUrl();
        }
        // 适配OkHttp 5.x：HttpUrl.get()改为HttpUrl.parse()（若原方法失效）
        HttpUrl baseUrl = HttpUrl.parse(invocation.getUrl());
        if (baseUrl == null) {
            throw new IllegalArgumentException("Invalid URL: " + invocation.getUrl());
        }
        HttpUrl.Builder urlBuilder = baseUrl.newBuilder();
        invocation.getParams().stream()
                .filter(e -> StringUtils.isNotEmpty(e.getName())
                        && e.getArgument() != null
                        && StringUtils.isNotEmpty(String.valueOf(e.getArgument())))
                .forEach(e -> urlBuilder.addQueryParameter(e.getName(), String.valueOf(e.getArgument())));
        return urlBuilder.build().toString();
    }

    /**
     * 构造POST请求
     *
     * @param builder
     * @param invocation
     * @return
     */
    private static Request buildPostRequest(Request.Builder builder, HttpRemoteInvocation invocation) {
        RequestBody body = isMultipartRequest(invocation) ? buildMultipartBody(invocation) : buildFormBody(invocation);
        return builder.url(invocation.getUrl()).post(body).build();
    }

    /**
     * 是否为上传文件请求
     *
     * @param invocation
     * @return
     */
    private static boolean isMultipartRequest(HttpRemoteInvocation invocation) {
        return invocation.getParams().stream()
                .anyMatch(e -> e.getArgument() != null
                        && InputStream.class.isAssignableFrom(e.getArgument().getClass()));
    }

    /**
     * 构造表单请求
     *
     * @param invocation
     * @return
     */
    private static RequestBody buildFormBody(HttpRemoteInvocation invocation) {
        FormBody.Builder builder = new FormBody.Builder();
        invocation.getParams().stream()
                .filter(e -> StringUtils.isNotEmpty(e.getName())
                        && e.getArgument() != null
                        && StringUtils.isNotEmpty(String.valueOf(e.getArgument())))
                .forEach(e -> builder.add(e.getName(), String.valueOf(e.getArgument())));
        return builder.build();
    }

    /**
     * 构造文件上传请求
     *
     * @param invocation
     * @return
     */
    private static RequestBody buildMultipartBody(HttpRemoteInvocation invocation) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        invocation.getParams().stream()
                .filter(e -> StringUtils.isNotEmpty(e.getName()) && e.getArgument() != null)
                .forEach(e -> {
                    boolean isFileParam = e.getClass().equals(FileRemoteParam.class);
                    if (InputStream.class.isAssignableFrom(e.getArgument().getClass())) {
                        String fileName = isFileParam ? ((FileRemoteParam) e).getFileName() : e.getName();
                        String mimeType = isFileParam ? ((FileRemoteParam) e).getMimeType() : null;
                        MediaType mediaType = mimeType != null ? MediaType.parse(mimeType) : null;
                        builder.addPart(Part.createFormData(e.getName(),
                                fileName,
                                create(mediaType, (InputStream) e.getArgument())));
                    } else if (List.class.isAssignableFrom(e.getArgument().getClass())) {
                        List<?> argList = (List) e.getArgument();
                        argList.stream()
                                .filter(Objects::nonNull)
                                .forEach(o -> builder.addPart(Part.createFormData(e.getName(), String.valueOf(o))));
                    } else if (e.getArgument().getClass().isArray()) {
                        // 补充数组参数处理（原代码空实现，可选）
                        Object[] array = (Object[]) e.getArgument();
                        for (Object o : array) {
                            if (o != null) {
                                builder.addPart(Part.createFormData(e.getName(), String.valueOf(o)));
                            }
                        }
                    } else {
                        // 普通参数处理
                        builder.addPart(Part.createFormData(e.getName(), String.valueOf(e.getArgument())));
                    }
                });
        return builder.build();
    }

    /**
     * 创建文件上传请求体
     * 适配OkHttp 5.x：移除内部Util类，替换为原生关闭逻辑
     *
     * @param mediaType    媒体类型
     * @param inputStream  文件输入流
     * @return RequestBody
     */
    private static RequestBody create(final MediaType mediaType, final InputStream inputStream) {
        return new RequestBody() {
            @Nullable
            @Override
            public MediaType contentType() {
                return mediaType;
            }

            @Override
            public long contentLength() throws IOException {
                // 注意：InputStream.available() 不一定返回真实文件大小，若需精准长度建议传入文件元信息
                // OkHttp 5.x 若返回-1会自动处理为chunked编码
                try {
                    long length = inputStream.available();
                    return length > 0 ? length : -1;
                } catch (IOException e) {
                    return -1;
                }
            }

            @Override
            public void writeTo(@NotNull BufferedSink bufferedSink) throws IOException {
                Source source = null;
                try {
                    source = Okio.source(inputStream);
                    bufferedSink.writeAll(source);
                } finally {
                    // 替换 Util.closeQuietly() 为原生关闭逻辑
                    closeQuietly(source);
                    closeQuietly(inputStream); // 可选：根据业务决定是否关闭输入流
                }
            }
        };
    }

    /**
     * 静默关闭Closeable资源（替换okhttp3.internal.Util.closeQuietly）
     *
     * @param closeable 可关闭资源
     */
    private static void closeQuietly(@Nullable Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                LOGGER.warn("Failed to close resource quietly", e);
            }
        }
    }

    /**
     * 适配Okio Source的静默关闭（Source不是Closeable，单独处理）
     *
     * @param source Okio Source
     */
    private static void closeQuietly(@Nullable Source source) {
        if (source != null) {
            try {
                source.close();
            } catch (IOException e) {
                LOGGER.warn("Failed to close Okio Source quietly", e);
            }
        }
    }

    /**
     * 构造HEAD请求
     *
     * @param builder
     * @param invocation
     * @return
     */
    private static Request buildHeadRequest(Builder builder, HttpRemoteInvocation invocation) {
        return builder.url(appendQueryParameters(invocation)).head().build();
    }

    /**
     * 构造PUT请求
     *
     * @param builder
     * @param invocation
     * @return
     */
    private static Request buildPutRequest(Builder builder, HttpRemoteInvocation invocation) {
        RequestBody body = isMultipartRequest(invocation) ? buildMultipartBody(invocation) : buildFormBody(invocation);
        return builder.url(invocation.getUrl()).put(body).build();
    }

    /**
     * 构造DELETE请求
     *
     * @param builder
     * @param invocation
     * @return
     */
    private static Request buildDeleteRequest(Builder builder, HttpRemoteInvocation invocation) {
        return builder.url(appendQueryParameters(invocation)).delete().build();
    }

    /**
     * 构造PATCH请求
     *
     * @param builder
     * @param invocation
     * @return
     */
    private static Request buildPatchRequest(Builder builder, HttpRemoteInvocation invocation) {
        RequestBody body = isMultipartRequest(invocation) ? buildMultipartBody(invocation) : buildFormBody(invocation);
        return builder.url(invocation.getUrl()).patch(body).build();
    }
}