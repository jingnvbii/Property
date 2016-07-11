package com.ctrl.forum.entity;

/**
 * 共通分类实体
 * Created by Administrator on 2016/4/8.
 */
public class Advertising {
    private String id;//主键id
    private String imgUrl;//广告图片url

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
