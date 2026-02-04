package com.megatron.shared.meta.commons.data.base;

/**
 * PageRequest
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 5/3/20 10:28 PM
 */
public class PageRequest extends BaseRequest{

    private static final long serialVersionUID = 8943673630842445243L;
    /**
     * 当前页
     */
    private Integer currentPage = 1;
    /**
     * 每页数据量
     */
    private Integer pageSize = 10;

    public PageRequest() {
        super();
    }

    public PageRequest(Integer currentPage, Integer pageSize) {
        super();
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
