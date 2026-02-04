package com.megatron.shared.meta.remoting.http.utils;

import com.megatron.shared.meta.remoting.http.model.FileRemoteParam;
import com.megatron.shared.meta.remoting.http.model.HttpRemoteInvocation;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.MultipartBody.Part;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
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
        HttpUrl.Builder urlBuilder = HttpUrl.get(invocation.getUrl()).newBuilder();
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
                          if (mimeType != null) {
                              builder.addPart(Part.createFormData(e.getName(),
                                                                  fileName,
                                                                  create(MediaType.parse(mimeType),
                                                                         (InputStream) e.getArgument())));
                          } else {
                              builder.addPart(Part.createFormData(e.getName(),
                                                                  fileName,
                                                                  create(null, (InputStream) e.getArgument())));
                          }
                      } else if (List.class.isAssignableFrom(e.getArgument().getClass())) {
                          List<?> argList = (List) e.getArgument();
                          argList.stream()
                                 .filter(Objects::nonNull)
                                 .forEach(o -> builder.addPart(Part.createFormData(e.getName(), String.valueOf(o))));
                      } else if (e.getArgument().getClass().isArray()) {

                      } else {

                      }
                  });
        return builder.build();
    }

    /**
     * 创建文件上传请求
     *
     * @param mediaType
     * @param inputStream
     * @return
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
                try {
                    return inputStream.available();
                } catch (IOException e) {
                    return 0;
                }
            }

            @Override
            public void writeTo(@NotNull BufferedSink bufferedSink) throws IOException {
                Source source = null;
                try {
                    source = Okio.source(inputStream);
                    bufferedSink.writeAll(source);
                } finally {
                    Util.closeQuietly(source);
                }
            }
        };
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
