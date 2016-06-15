package com.ctrl.forum.entity;

/**
 * 帖子一级分类实体
 * Created by Administrator on 2016/4/8.
 */
public class MallKind {
    private String id;//帖子一级分类id
    private String kindName;//分类名称（频道名称）
    private String kindIcon;//分类图标（频道图标）

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKindName() {
        return kindName;
    }

    public void setKindName(String kindName) {
        this.kindName = kindName;
    }

    public String getKindIcon() {
        return kindIcon;
    }

    public void setKindIcon(String kindIcon) {
        this.kindIcon = kindIcon;
    }
}
