package com.ctrl.forum.entity;

/**
 * 帖子分类实体类
 * Created by Administrator on 2016/4/8.
 */
public class CategoryItem {
    private String id;//分类id
    private String name;//分类名称
    private String categoryIcon;//分类图标url
    private String grade;//分类级别
    private String checkType;//审核类型（0：无需审核、1：需审核)
    private String styleType;////样式类型（1：列表样式、2：方块样式、3：瀑布流样式、4：朋友圈样式）

    public String getStyleType() {
        return styleType;
    }

    public void setStyleType(String styleType) {
        this.styleType = styleType;
    }

    public String getCheckType() {
        return checkType;
    }

    public void setCheckType(String checkType) {
        this.checkType = checkType;
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

    public String getCategoryIcon() {
        return categoryIcon;
    }

    public void setCategoryIcon(String categoryIcon) {
        this.categoryIcon = categoryIcon;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
