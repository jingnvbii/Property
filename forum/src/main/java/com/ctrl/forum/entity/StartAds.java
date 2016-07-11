package com.ctrl.forum.entity;

/**
 * 共通分类实体
 * Created by Administrator on 2016/4/8.
 */
public class StartAds {
    private String id;//主键id
    private String kindIcon;//广告图片url

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKindIcon() {
        return kindIcon;
    }

    public void setKindIcon(String kindIcon) {
        this.kindIcon = kindIcon;
    }
}
