package com.ctrl.forum.entity;

/**
 * 商城推荐实体类
 * Created by Administrator on 2016/4/8.
 */
public class MallRecommend {
    private String id;//指向路径
    private String targetId;//指向路径
    private String type;//指向路径
    private String sort;//指向路径
    private String linkUrl;//指向路径
    private String imgUrl;//推荐图片Url

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }


    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
