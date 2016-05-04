package com.ctrl.forum.entity;

/**
 * 轮播图实体
 * Created by Administrator on 2016/4/8.
 */
public class Banner {
    private String id;//轮播图id
    private String imgUrl;//轮播图url
    private String type;//链接类型（0：商家、1：商品、2：帖子、3：外部链接）
    private String targetId;//目标id（商品id、店铺id或者帖子id）
    private String targetUrl;//外部链接Url

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
