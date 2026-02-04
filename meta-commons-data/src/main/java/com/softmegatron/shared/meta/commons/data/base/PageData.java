package com.softmegatron.shared.meta.commons.data.base;

import java.util.Collections;
import java.util.List;

/**
 * PageData
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 2019-04-23 00:04
 */
public class PageData<T> extends BaseModel{

    private static final long serialVersionUID = 5021157350064058810L;

    public static final PageData EMPTY = new PageData(Collections.emptyList(), PageInfo.EMPTY);

    private List<T> listInfo;

    private PageInfo pageInfo;

    public PageData(List<T> listInfo, PageInfo pageInfo) {
        super();
        this.listInfo = listInfo;
        this.pageInfo = pageInfo;
    }

    public List<T> getListInfo() {
        return listInfo;
    }

    public void setListInfo(List<T> listInfo) {
        this.listInfo = listInfo;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public static <T> PageData<T> ofData(List<T> listInfo, PageInfo pageInfo) {
        return new PageData<T>(listInfo, pageInfo);
    }
}
