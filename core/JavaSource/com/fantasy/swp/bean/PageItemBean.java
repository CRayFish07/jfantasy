package com.fantasy.swp.bean;

import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.swp.IPageItem;
import com.fantasy.swp.exception.SwpException;
import com.fantasy.swp.service.PageBeanService;
import com.fantasy.swp.service.PageItemBeanService;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 *
 */
@Component
public class PageItemBean implements IPageItem{

    PageItemBeanService pageItemBeanService = SpringContextUtil.getBeanByType(PageItemBeanService.class);

    private PageItem pageItem;

    @Override
    public void refash() throws SwpException, IOException {
        this.pageItemBeanService.refash(this.pageItem.getId());
    }

    @Override
    public void refash(List<Data> datas) throws SwpException {

    }

    public PageItem getPageItem() {
        return pageItem;
    }

    public void setPageItem(PageItem pageItem) {
        this.pageItem = pageItem;
    }
}
