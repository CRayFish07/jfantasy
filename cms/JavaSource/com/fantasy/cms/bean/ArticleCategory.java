package com.fantasy.cms.bean;

import com.fantasy.attr.bean.AttributeVersion;
import com.fantasy.framework.dao.BaseBusEntity;
import com.fantasy.framework.lucene.annotations.IndexEmbedBy;
import com.fantasy.framework.lucene.annotations.IndexProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.IOException;
import java.util.List;

/**
 * 栏目表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2012-11-4 下午05:46:57
 */
@Entity
@Table(name = "CMS_ARTICLE_CATEGORY")
@JsonIgnoreProperties({"hibernateLazyInitializer", "articles"})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ArticleCategory extends BaseBusEntity {

    private static final long serialVersionUID = -2207100604803274789L;

    public static final String PATH_SEPARATOR = ",";// 树路径分隔符

    @Id
    @Column(name = "CODE", length = 50, insertable = true, updatable = false)
    private String code;
    /**
     * 栏目名称
     */
    @Column(name = "NAME", length = 200)
    @IndexProperty(analyze = true)
    private String name;
    /**
     * 层级
     */
    @Column(name = "LAYER", nullable = false)
    private Integer layer;
    // 树路径
    @IndexEmbedBy(value = Article.class)
    @Column(name = "PATH", nullable = false, length = 3000)
    private String path;
    /**
     * 描述
     */
    @Column(name = "DESCRIPTION", length = 2000)
    private String description;
    /**
     * 排序字段
     */
    @Column(name = "SORT")
    private Integer sort;
    /**
     * 上级栏目
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PCODE", foreignKey = @ForeignKey(name = "FK_CMS_CATEGORY_PARENT"))
    @JsonManagedReference
    private ArticleCategory parent;
    /**
     * 下级栏目
     */
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    @OrderBy("sort ASC")
    @JsonBackReference
    private List<ArticleCategory> children;
    /**
     * 属性版本表
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ARTICLE_VERSION_ID", foreignKey = @ForeignKey(name = "FK_CMS_ARTICLE_CATEGORY_VERSION"))
    private AttributeVersion articleVersion;
    /**
     * 文章
     */
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    private List<Article> articles;


    // -------------------------------新增字段-------------------------------\\

    /**
     * 英文名称
     */
    @Column(name = "EGNAME")
    private String egname;

    /**
     * 栏目对应的模板地址
     */
    @Column(name = "TEMPLATE_PATH")
    private String templatePath;

    /**
     * 栏目文章对应的模板地址
     */
    @Column(name = "ARTICLE_TEMPLATE_PATH")
    private String articleTemplatePath;


    public String getEgname() {
        return egname;
    }

    public void setEgname(String egname) {
        this.egname = egname;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @JsonProperty("parentCode")
    @JsonSerialize(using = ArticleCategoryParentSerialize.class)
    public ArticleCategory getParent() {
        return parent;
    }

    public void setParent(ArticleCategory parent) {
        this.parent = parent;
    }

    public List<ArticleCategory> getChildren() {
        return children;
    }

    public void setChildren(List<ArticleCategory> children) {
        this.children = children;
    }

    public Integer getLayer() {
        return layer;
    }

    public void setLayer(Integer layer) {
        this.layer = layer;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public String getArticleTemplatePath() {
        return articleTemplatePath;
    }

    public void setArticleTemplatePath(String articleTemplatePath) {
        this.articleTemplatePath = articleTemplatePath;
    }

    public AttributeVersion getArticleVersion() {
        return articleVersion;
    }

    public void setArticleVersion(AttributeVersion articleVersion) {
        this.articleVersion = articleVersion;
    }

    public static class ArticleCategoryParentSerialize extends JsonSerializer<ArticleCategory> {

        @Override
        public void serialize(ArticleCategory category, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
            jgen.writeString(category.getCode());
        }

    }
}
