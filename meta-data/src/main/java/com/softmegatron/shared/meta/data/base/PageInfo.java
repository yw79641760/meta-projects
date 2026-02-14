package com.softmegatron.shared.meta.data.base;

/**
 * PageInfo
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 2019-04-23 00:04
 */
public class PageInfo extends BaseModel{

    private static final long serialVersionUID = 7031767677144237389L;
    /**
     * 空分页结果
     */
    public static final PageInfo EMPTY = new PageInfo(1, 10, 0L, 0);
    /**
     * 当前页
     */
    private int currentPage;
    /**
     * 每页数据条数
     */
    private int pageSize;
    /**
     * 数据总条数
     */
    private long totalCount;
    /**
     * 当前页数据条数
     * count <= pageSize
     */
    private int count;
    /**
     * 总页数
     */
    private int pageCount;
    /**
     * 是否有更多数据
     */
    private boolean hasMore;

    public PageInfo() {
        super();
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public PageInfo(int currentPage, int pageSize, long totalCount, int count) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        this.count = count;
        if (pageSize > 0) {
            this.pageCount = (int) (totalCount % pageSize > 0 ? (totalCount / pageSize) + 1 : totalCount / pageSize);
            this.hasMore = pageCount > currentPage;
        }
    }

    public PageInfo(int currentPage, int pageSize, long totalCount, int count, boolean hasMore) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        this.count = count;
        if (pageSize > 0) {
            this.pageCount = (int) (totalCount % pageSize > 0 ? (totalCount / pageSize) + 1 : totalCount / pageSize);
        }
        this.hasMore = hasMore;
    }

    public static PageInfo ofData(int currentPage, int pageSize, boolean hasMore) {
        return new PageInfo(currentPage, pageSize, 0, 0, hasMore);
    }

    public static PageInfo ofData(int currentPage, int pageSize, int totalCount, int count) {
        return new PageInfo(currentPage, pageSize, totalCount, count);
    }

    public static PageInfo ofData(int currentPage, int pageSize, int totalCount, int count, boolean hasMore) {
        return new PageInfo(currentPage, pageSize, totalCount, count, hasMore);
    }
}
