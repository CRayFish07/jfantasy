package com.fantasy.swp.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.swp.bean.PageItem;
import com.fantasy.swp.bean.PageItemData;
import com.fantasy.swp.dao.PageItemDao;
import com.fantasy.swp.dao.PageItemDataDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class PageItemDataService {

    @Resource
    private PageItemDataDao pageItemDataDao;

    public Pager<PageItemData> findPager(Pager<PageItemData> pager, List<PropertyFilter> filters) {
        return this.pageItemDataDao.findPager(pager, filters);
    }

    public void save(PageItemData data) {
        this.pageItemDataDao.save(data);
    }

    public PageItemData getData(Long id) {
        return this.pageItemDataDao.get(id);
    }

    public void delete(Long[] ids) {
        for (Long id : ids) {
            this.pageItemDataDao.delete(id);
        }
    }

    public List<PageItemData> find(List<PropertyFilter> filters) {
        return this.pageItemDataDao.find(filters);
    }
}

