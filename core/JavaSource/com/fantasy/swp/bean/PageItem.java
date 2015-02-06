package com.fantasy.swp.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

/**
 * 页面实例
 */
@Entity
@Table(name = "SWP_PAGE_ITEM")
public class PageItem extends BaseBusEntity {

    private static final long serialVersionUID = 1157087271787049968L;

    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = false, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAGE_ID",foreignKey = @ForeignKey(name = "FK_SWP_PAGE_ITEM_PAGE") )
    private Page page;

    @Lob
    @Column(name = "CONTENT")
    private String content;

    @OneToMany(mappedBy = "pageItem", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    @OrderBy("createTime asc")
    private List<PageItemData> pageItemDatas;
    /**
     * 页面路径
     */
    @Column(name = "FILE")
    private String file;



    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<PageItemData> getPageItemDatas() {
        return pageItemDatas;
    }

    public void setPageItemDatas(List<PageItemData> pageItemDatas) {
        this.pageItemDatas = pageItemDatas;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
