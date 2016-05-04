package com.ctrl.forum.entity;

/**
 * 帖子一级分类实体
 * Created by Administrator on 2016/4/8.
 */
public class PostKind {
    private String id;//帖子一级分类id
    private String name;//分类名称（频道名称）
    private String categoryIcon;//分类图标（频道图标）

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

    public String getCategoryIcon() {
        return categoryIcon;
    }

    public void setCategoryIcon(String categoryIcon) {
        this.categoryIcon = categoryIcon;
    }



}
