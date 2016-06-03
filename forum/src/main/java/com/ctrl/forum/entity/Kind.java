package com.ctrl.forum.entity;

/**
 * 共通分类实体
 * Created by Administrator on 2016/4/8.
 */
public class Kind {
    private String id;//分类id
    private String kindName;//类别名称
    private String kindIcon;//类别图标Url

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
