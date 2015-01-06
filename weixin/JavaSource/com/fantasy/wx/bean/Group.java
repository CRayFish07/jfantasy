package com.fantasy.wx.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 微信用用户组
 * Created by zzzhong on 2014/6/19.
 */
@Entity
@Table(name = "WX_GROUP")
public class Group {
    public Group() {
    }
    public Group(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    public Group(Long id, String name,Long count) {
        this.id = id;
        this.name = name;
        this.count=count;
    }

    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = false, precision = 22, scale = 0)
    private Long id=-1L;
    @Column(name = "NAME")
    private String name;
    @Column(name = "COUNT")
    private Long count;

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

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}