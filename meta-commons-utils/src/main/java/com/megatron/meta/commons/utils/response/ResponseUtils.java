package com.softmegatron.meta.commons.utils.response;

import com.softmegatron.meta.commons.data.base.ListResponse;
import com.softmegatron.meta.commons.data.base.PageData;
import com.softmegatron.meta.commons.data.base.PageInfo;
import com.softmegatron.meta.commons.data.base.PageRequest;
import com.softmegatron.meta.commons.data.base.PageResponse;
import com.softmegatron.meta.commons.utils.request.RequestUtils;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

/**
 * ResponseUtils
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 5/4/20 9:59 AM
 */
public class ResponseUtils {
    /**
     * 空列表结果
     *
     * @param <T>
     * @return
     */
    public static <T> ListResponse<T> emptyListResponse() {
        return new ListResponse.Builder<T>().success()
                                            .data(Collections.emptyList())
                                            .build();
    }

    /**
     * 空分页结果
     *
     * @param <T>
     * @return
     */
    public static <T> PageResponse<T> emptyPageResponse() {
        return new PageResponse.Builder<T>().success()
                                            .data(PageData.EMPTY)
                                            .build();
    }

    /**
     * 类型转换
     *
     * @param item
     * @param mapper
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> Optional<R> convertItem(T item, Function<T, R> mapper) {
        return Optional.ofNullable(item).map(mapper);
    }

    /**
     * 列表数据类型转换
     *
     * @param listInfo
     * @param mapper
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> List<R> convertList(@Nonnull List<T> listInfo, Function<T, R> mapper) {
        return listInfo.stream().map(mapper)
                       .collect(toList());
    }

    /**
     * 列表数据类型转换
     *
     * @param listInfo
     * @param preFilter
     * @param mapper
     * @param postFilter
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> List<R> convertList(@Nonnull List<T> listInfo,
                                             Predicate<T> preFilter,
                                             Function<T, R> mapper,
                                             Predicate<R> postFilter) {
        return listInfo.stream().filter(preFilter).map(mapper).filter(postFilter).collect(toList());
    }


    /**
     * 列表页类型转换
     *
     * @param listInfo
     * @param mapper
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> ListResponse<R> toListResponse(@Nonnull List<T> listInfo, Function<T, R> mapper) {
        return new ListResponse.Builder<R>().success()
                                            .data(convertList(listInfo, mapper))
                                            .build();
    }

    /**
     * 列表页类型转换
     *
     * @param listInfo
     * @param preFilter
     * @param mapper
     * @param postFilter
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> ListResponse<R> toListResponse(@Nonnull List<T> listInfo,
                                                        Predicate<T> preFilter,
                                                        Function<T, R> mapper,
                                                        Predicate<R> postFilter) {
        return new ListResponse.Builder<R>().success()
                                            .data(convertList(listInfo, preFilter, mapper, postFilter))
                                            .build();
    }

    /**
     * 分页类型转换
     *
     * @param listInfo
     * @param mapper
     * @param pageInfo
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> PageResponse<R> toPageResponse(@Nonnull List<T> listInfo,
                                                        Function<T, R> mapper,
                                                        PageInfo pageInfo) {
        return new PageResponse.Builder<R>().success()
                                            .data(PageData.ofData(convertList(listInfo, mapper), pageInfo))
                                            .build();
    }

    /**
     * 分页响应类型转换
     *
     * @param listInfo
     * @param preFilter
     * @param mapper
     * @param postFilter
     * @param pageInfo
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> PageResponse<R> toPageResponse(@Nonnull List<T> listInfo,
                                                        Predicate<T> preFilter,
                                                        Function<T, R> mapper,
                                                        Predicate<R> postFilter,
                                                        PageInfo pageInfo) {
        return new PageResponse.Builder<R>().success()
                                            .data(PageData.ofData(convertList(listInfo, preFilter, mapper, postFilter),
                                                                  pageInfo))
                                            .build();
    }

    /**
     * 分页响应类型转换
     *
     * @param listInfo
     * @param mapper
     * @param totalCount
     * @param pageRequest
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> PageResponse<R> toPageResponse(@Nonnull List<T> listInfo,
                                                        Function<T, R> mapper,
                                                        Integer totalCount,
                                                        PageRequest pageRequest) {
        PageInfo pageInfo = PageInfo.ofData(RequestUtils.getCurrentPage(pageRequest),
                                            RequestUtils.getPageSize(pageRequest),
                                            totalCount,
                                            listInfo.size());
        return new PageResponse.Builder<R>().success()
                                            .data(PageData.ofData(convertList(listInfo, mapper), pageInfo))
                                            .build();
    }

    /**
     * 分页响应类型转换
     *
     * @param listInfo
     * @param preFilter
     * @param mapper
     * @param postFilter
     * @param totalCount
     * @param pageRequest
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> PageResponse<R> toPageResponse(@Nonnull List<T> listInfo,
                                                        Predicate<T> preFilter,
                                                        Function<T, R> mapper,
                                                        Predicate<R> postFilter,
                                                        Integer totalCount,
                                                        PageRequest pageRequest) {
        PageInfo pageInfo = PageInfo.ofData(RequestUtils.getCurrentPage(pageRequest),
                                            RequestUtils.getPageSize(pageRequest),
                                            totalCount,
                                            listInfo.size());
        return new PageResponse.Builder<R>().success()
                                            .data(PageData.ofData(convertList(listInfo, preFilter, mapper, postFilter),
                                                                  pageInfo))
                                            .build();
    }
}
