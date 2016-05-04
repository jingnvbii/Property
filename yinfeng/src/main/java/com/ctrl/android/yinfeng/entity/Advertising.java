package com.ctrl.android.yinfeng.entity;

/**
 * 广告图片
 * Created by jason on 2016/1/22.
 */
public class Advertising  {

    private String id;//广告id
    private String imgUrl = "aa";//广告图片URL
    private String targetUrl;//广告图片超链接URL

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
