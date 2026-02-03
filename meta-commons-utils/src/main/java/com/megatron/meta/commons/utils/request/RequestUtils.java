package com.softmegatron.meta.commons.utils.request;

import com.softmegatron.meta.commons.data.base.PageRequest;

/**
 * RequestUtils
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 5/4/20 9:59 AM
 */
public class RequestUtils {

    private static final Integer MAX_PAGE_SIZE = 200;

    /**
     * 获取分页大小
     *
     * @param pageRequest
     * @return
     */
    public static int getPageSize(PageRequest pageRequest) {
        if (pageRequest.getPageSize() == null
            || pageRequest.getPageSize() < 0
            || pageRequest.getPageSize() > MAX_PAGE_SIZE) {
            return MAX_PAGE_SIZE;
        }
        return pageRequest.getPageSize();
    }

    /**
     * 获取当前页数
     *
     * @param pageRequest
     * @return
     */
    public static int getCurrentPage(PageRequest pageRequest) {
        if (pageRequest.getCurrentPage() == null
            || pageRequest.getCurrentPage() <= 0) {
            return 1;
        }
        return pageRequest.getCurrentPage();
    }

    /**
     * 获取位移offset
     *
     * @param pageRequest
     * @return
     */
    public static int getOffset(PageRequest pageRequest) {
        int currentPage = getCurrentPage(pageRequest);
        return (currentPage - 1) * getPageSize(pageRequest);
    }

    /**
     * 获取数据大小
     *
     * @param pageRequest
     * @return
     */
    public static int getLimit(PageRequest pageRequest) {
        return getPageSize(pageRequest);
    }
}
