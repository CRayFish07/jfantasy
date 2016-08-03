package org.jfantasy.framework.dao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.ibatis.type.Alias;
import org.jfantasy.framework.util.common.StringUtil;

import java.io.Serializable;
import java.util.List;

@ApiModel("通用分页对象")
@Alias("Pager")
@JsonIgnoreProperties(value = {"orders", "first", "order_by_setted"})
public class Pager<T> implements Serializable {
    /**
     * 排序 - 升序
     */
    public final static String SORT_ASC = "asc";
    /**
     * 排序 - 降序
     */
    public final static String SORT_DESC = "desc";

    private static final long serialVersionUID = -2343309063338998483L;
    /**
     * 最大数据条数
     */
    @ApiModelProperty(value = "最大数据条数", name = "count")
    @JsonProperty("count")
    private int totalCount = 0;
    /**
     * 每页显示的数据条数
     */
    @ApiModelProperty(value = "每页显示的数据条数", name = "per_page")
    @JsonProperty("per_page")
    private int pageSize = 0;
    /**
     * 总页数
     */
    @ApiModelProperty(value = "总页数", name = "total")
    @JsonProperty("total")
    private int totalPage = 1;
    /**
     * 当前页码
     */
    @ApiModelProperty(value = "当前页码", name = "page")
    @JsonProperty("page")
    private int currentPage = 1;
    /**
     * 开始数据索引
     */
    @ApiModelProperty(hidden = true)
    private int first = 0;
    /**
     * 排序字段
     */
    @ApiModelProperty(value = "排序字段", name = "sort")
    @JsonProperty("sort")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String orderBy;
    @ApiModelProperty(value = "排序方向", name = "order")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String order;
    @ApiModelProperty(value = "返回的数据集", name = "items")
    @JsonProperty("items")
    private List<T> pageItems;

    public Pager() {
        this.pageSize = 15;
    }

    public Pager(int pageSize) {
        this.pageSize = pageSize;
    }

    public Pager(Pager pager) {
        this.currentPage = pager.currentPage;
        this.pageSize = pager.pageSize;
        this.totalCount = pager.totalCount;
        this.totalPage = pager.totalPage;
        this.orderBy = pager.orderBy;
        this.order = pager.order;
    }

    /**
     * 获取总页码
     *
     * @return 总页数
     */
    public int getTotalPage() {
        return totalPage;
    }

    /**
     * 设置总页数
     *
     * @param totalPage 总页数
     */
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage == 0 ? 1 : totalPage;
    }

    /**
     * 获取每页显示的条数
     *
     * @return 每页显示条数
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 设置显示的页码 注意是页码
     *
     * @param currentPage 当前页码
     */
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    /**
     * 设置总数据条数
     *
     * @param totalCount 总数据条数
     */
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        int totalPages = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
        this.setTotalPage(totalPages);
        if (currentPage >= totalPages) {
            setCurrentPage(totalPages);
            setFirst((totalPages - 1) * pageSize);
        } else if (currentPage <= 0) {
            setCurrentPage(1);
            setFirst(first);
        } else {
            setFirst((currentPage - 1) * pageSize);
        }
    }

    /**
     * 返回翻页开始位置
     *
     * @param first 数据开始位置
     */
    public void setFirst(int first) {
        this.first = first;
    }

    public int getFirst() {
        return first;
    }

    /**
     * 设置每页显示数据的条数
     *
     * @param pageSize 每页显示数据条数
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 获取当前显示的页码
     *
     * @return currentPage
     */
    public int getCurrentPage() {
        return currentPage <= 0 ? 1 : currentPage;
    }

    /**
     * 获取数据总条数
     *
     * @return totalCount
     */
    public int getTotalCount() {
        return totalCount;
    }

    public List<T> getPageItems() {
        return pageItems;
    }

    public void setPageItems(List<T> pageItems) {
        this.pageItems = pageItems;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrder() {
        if (StringUtil.isNotBlank(this.getOrderBy()) && StringUtil.isBlank(this.order)) {
            this.setOrder("asc");
        }
        return this.order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    @ApiModelProperty("是否启用排序")
    public boolean isOrderBySetted() {
        return StringUtil.isNotBlank(this.getOrderBy()) && StringUtil.isNotBlank(this.getOrder());
    }

    @Override
    public String toString() {
        return "Pager [totalCount=" + totalCount + ", first=" + first + ", pageSize=" + pageSize + ", totalPage=" + totalPage + ", currentPage=" + currentPage + ", orderBy=" + orderBy + ", order=" + order + "]";
    }

}