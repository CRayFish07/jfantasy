package com.fantasy.swp.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

/**
 * 页面静态化时，对某个页面的配置
 */
@Entity
@Table(name = "SWP_PAGE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "datas"})
public class Page extends BaseBusEntity {

    private static final long serialVersionUID = 8032849785819496211L;

    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = false, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;

    /**
     * 名称
     */
    @Column(name = "NAME")
    private String name;

    /**
     * 对应模板
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEMPLATE_ID")
    private Template template;
    /**
     * 对应的数据
     */
    @ManyToMany(targetEntity = Data.class, fetch = FetchType.LAZY)
    @JoinTable(name = "SWP_PAGE_DATA", joinColumns = @JoinColumn(name = "PAGE_ID"), inverseJoinColumns = @JoinColumn(name = "DATA_ID"),foreignKey = @ForeignKey(name = "FK_PAGE_DATA"))
    private List<Data> datas;

    @ManyToOne
    @JoinColumn(name = "PAGE_ANALYZER_ID")
    private PageAnalyzer pageAnalyzer;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public Template getTemplate() {
        return template;
    }

    public List<Data> getDatas() {
        return datas;
    }

    public void setDatas(List<Data> datas) {
        this.datas = datas;
    }

    public PageAnalyzer getPageAnalyzer() {
        return pageAnalyzer;
    }

    public void setPageAnalyzer(PageAnalyzer pageAnalyzer) {
        this.pageAnalyzer = pageAnalyzer;
    }
}