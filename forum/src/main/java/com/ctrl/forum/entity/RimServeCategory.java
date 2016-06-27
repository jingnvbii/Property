package com.ctrl.forum.entity;

import java.util.List;

/**
 * 周边服务一级分类
 * Created by Administrator on 2016/5/20.
 */
public class RimServeCategory {
    private String id;//分类id
    private String name; //分类名称
    private String categoryIcon; //分类图标
    private List<RimServeCategorySecond> aroundservicecategorylist; //二级分类的列表集合

    public List<RimServeCategorySecond> getAroundservicecategorylist() {
        return aroundservicecategorylist;
    }

    public void setAroundservicecategorylist(List<RimServeCategorySecond> aroundservicecategorylist) {
        this.aroundservicecategorylist = aroundservicecategorylist;
    }

    public String getCategoryIcon() {
        return categoryIcon;
    }

    public void setCategoryIcon(String categoryIcon) {
        this.categoryIcon = categoryIcon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
