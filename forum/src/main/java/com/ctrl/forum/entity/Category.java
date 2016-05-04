package com.ctrl.forum.entity;

import java.util.List;

/**
 * 帖子分类实体类
 * Created by Administrator on 2016/4/8.
 */
public class Category {
    private String id;//分类id
    private String name;//分类名称
    private String category_icon;//分类图标
    private String styleType;//样式类型（1：列表样式、2：方块样式、3：瀑布流样式、4：朋友圈样式）
    private String canPublish;//是否允许发帖（0：禁止、1：允许）
    private String canComment;//是否允许评论（0：禁止、1：允许）
    private String canCollect;//是否允许收藏（0：禁止、1：允许）

    private List<Category2>categorylist;//三级帖子列表

    public List<Category2> getCategorylist() {
        return categorylist;
    }

    public void setCategorylist(List<Category2> categorylist) {
        this.categorylist = categorylist;
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

    public String getCategory_icon() {
        return category_icon;
    }

    public void setCategory_icon(String category_icon) {
        this.category_icon = category_icon;
    }

    public String getStyleType() {
        return styleType;
    }

    public void setStyleType(String styleType) {
        this.styleType = styleType;
    }

    public String getCanPublish() {
        return canPublish;
    }

    public void setCanPublish(String canPublish) {
        this.canPublish = canPublish;
    }

    public String getCanComment() {
        return canComment;
    }

    public void setCanComment(String canComment) {
        this.canComment = canComment;
    }

    public String getCanCollect() {
        return canCollect;
    }

    public void setCanCollect(String canCollect) {
        this.canCollect = canCollect;
    }

    public String getCanShare() {
        return canShare;
    }

    public void setCanShare(String canShare) {
        this.canShare = canShare;
    }

    private String canShare;//是否允许分享（0：禁止、1：允许）


}
