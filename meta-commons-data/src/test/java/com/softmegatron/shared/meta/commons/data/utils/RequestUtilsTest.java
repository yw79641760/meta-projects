package com.softmegatron.shared.meta.commons.data.utils;

import com.softmegatron.shared.meta.commons.data.base.PageRequest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * RequestUtils 测试类
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">yw</a>
 * @description 测试 RequestUtils 的各种功能
 * @date 2026/2/6 14:55
 * @since 1.0.0
 */
public class RequestUtilsTest {

    private static final int MAX_PAGE_SIZE = 200;

    @Test
    public void testGetPageSizeWithValidValue() {
        PageRequest request = new PageRequest();
        request.setPageSize(10);
        int pageSize = RequestUtils.getPageSize(request);
        assertEquals("页大小应为10", 10, pageSize);
    }

    @Test
    public void testGetPageSizeWithNull() {
        PageRequest request = new PageRequest();
        request.setPageSize(null);
        int pageSize = RequestUtils.getPageSize(request);
        assertEquals("null页大小应返回最大值", MAX_PAGE_SIZE, pageSize);
    }

    @Test
    public void testGetPageSizeWithZero() {
        PageRequest request = new PageRequest();
        request.setPageSize(0);
        int pageSize = RequestUtils.getPageSize(request);
        assertEquals("0页大小应返回0", 0, pageSize);
    }

    @Test
    public void testGetPageSizeWithNegative() {
        PageRequest request = new PageRequest();
        request.setPageSize(-10);
        int pageSize = RequestUtils.getPageSize(request);
        assertEquals("负数页大小应返回最大值", MAX_PAGE_SIZE, pageSize);
    }

    @Test
    public void testGetPageSizeWithExceedsMax() {
        PageRequest request = new PageRequest();
        request.setPageSize(300);
        int pageSize = RequestUtils.getPageSize(request);
        assertEquals("超过最大值应返回最大值", MAX_PAGE_SIZE, pageSize);
    }

    @Test
    public void testGetPageSizeWithMaxValue() {
        PageRequest request = new PageRequest();
        request.setPageSize(MAX_PAGE_SIZE);
        int pageSize = RequestUtils.getPageSize(request);
        assertEquals("最大值应正常返回", MAX_PAGE_SIZE, pageSize);
    }

    @Test
    public void testGetCurrentPageWithValidValue() {
        PageRequest request = new PageRequest();
        request.setCurrentPage(5);
        int currentPage = RequestUtils.getCurrentPage(request);
        assertEquals("当前页应为5", 5, currentPage);
    }

    @Test
    public void testGetCurrentPageWithNull() {
        PageRequest request = new PageRequest();
        request.setCurrentPage(null);
        int currentPage = RequestUtils.getCurrentPage(request);
        assertEquals("null当前页应返回1", 1, currentPage);
    }

    @Test
    public void testGetCurrentPageWithZero() {
        PageRequest request = new PageRequest();
        request.setCurrentPage(0);
        int currentPage = RequestUtils.getCurrentPage(request);
        assertEquals("0当前页应返回1", 1, currentPage);
    }

    @Test
    public void testGetCurrentPageWithNegative() {
        PageRequest request = new PageRequest();
        request.setCurrentPage(-5);
        int currentPage = RequestUtils.getCurrentPage(request);
        assertEquals("负数当前页应返回1", 1, currentPage);
    }

    @Test
    public void testGetCurrentPageWithOne() {
        PageRequest request = new PageRequest();
        request.setCurrentPage(1);
        int currentPage = RequestUtils.getCurrentPage(request);
        assertEquals("当前页1应正常返回", 1, currentPage);
    }

    @Test
    public void testGetOffsetWithFirstPage() {
        PageRequest request = new PageRequest();
        request.setCurrentPage(1);
        request.setPageSize(10);
        int offset = RequestUtils.getOffset(request);
        assertEquals("第一页偏移量应为0", 0, offset);
    }

    @Test
    public void testGetOffsetWithSecondPage() {
        PageRequest request = new PageRequest();
        request.setCurrentPage(2);
        request.setPageSize(10);
        int offset = RequestUtils.getOffset(request);
        assertEquals("第二页偏移量应为10", 10, offset);
    }

    @Test
    public void testGetOffsetWithThirdPage() {
        PageRequest request = new PageRequest();
        request.setCurrentPage(3);
        request.setPageSize(20);
        int offset = RequestUtils.getOffset(request);
        assertEquals("第三页偏移量应为40", 40, offset);
    }

    @Test
    public void testGetOffsetWithNullCurrentPage() {
        PageRequest request = new PageRequest();
        request.setCurrentPage(null);
        request.setPageSize(10);
        int offset = RequestUtils.getOffset(request);
        assertEquals("null当前页应返回0", 0, offset);
    }

    @Test
    public void testGetOffsetWithNullPageSize() {
        PageRequest request = new PageRequest();
        request.setCurrentPage(2);
        request.setPageSize(null);
        int offset = RequestUtils.getOffset(request);
        assertEquals("null页大小第二页应返回200", MAX_PAGE_SIZE, offset);
    }

    @Test
    public void testGetLimitWithValidValue() {
        PageRequest request = new PageRequest();
        request.setPageSize(10);
        int limit = RequestUtils.getLimit(request);
        assertEquals("limit应与页大小相同", 10, limit);
    }

    @Test
    public void testGetLimitWithNull() {
        PageRequest request = new PageRequest();
        request.setPageSize(null);
        int limit = RequestUtils.getLimit(request);
        assertEquals("null页大小limit应返回最大值", MAX_PAGE_SIZE, limit);
    }

    @Test
    public void testGetLimitWithExceedsMax() {
        PageRequest request = new PageRequest();
        request.setPageSize(300);
        int limit = RequestUtils.getLimit(request);
        assertEquals("超过最大值limit应返回最大值", MAX_PAGE_SIZE, limit);
    }

    @Test
    public void testCalculatePaginationWithValidRequest() {
        PageRequest request = new PageRequest();
        request.setCurrentPage(3);
        request.setPageSize(20);

        int currentPage = RequestUtils.getCurrentPage(request);
        int pageSize = RequestUtils.getPageSize(request);
        int offset = RequestUtils.getOffset(request);
        int limit = RequestUtils.getLimit(request);

        assertEquals("当前页应为3", 3, currentPage);
        assertEquals("页大小应为20", 20, pageSize);
        assertEquals("偏移量应为40", 40, offset);
        assertEquals("limit应为20", 20, limit);
    }

    @Test
    public void testCalculatePaginationWithBoundaryValues() {
        PageRequest request = new PageRequest();
        request.setCurrentPage(1);
        request.setPageSize(1);

        int currentPage = RequestUtils.getCurrentPage(request);
        int pageSize = RequestUtils.getPageSize(request);
        int offset = RequestUtils.getOffset(request);
        int limit = RequestUtils.getLimit(request);

        assertEquals("当前页应为1", 1, currentPage);
        assertEquals("页大小应为1", 1, pageSize);
        assertEquals("偏移量应为0", 0, offset);
        assertEquals("limit应为1", 1, limit);
    }

    @Test
    public void testCalculatePaginationWithInvalidRequest() {
        PageRequest request = new PageRequest();
        request.setCurrentPage(null);
        request.setPageSize(-1);

        int currentPage = RequestUtils.getCurrentPage(request);
        int pageSize = RequestUtils.getPageSize(request);
        int offset = RequestUtils.getOffset(request);
        int limit = RequestUtils.getLimit(request);

        assertEquals("当前页应默认为1", 1, currentPage);
        assertEquals("页大小应默认为最大值", MAX_PAGE_SIZE, pageSize);
        assertEquals("偏移量应为0", 0, offset);
        assertEquals("limit应为最大值", MAX_PAGE_SIZE, limit);
    }
}
