package com.ctrl.forum.entity;

/**
 * Created by Administrator on 2016/4/8.
 */
public class Image {
    private String imgUrl  ;
    private String thumbImgUrl;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getThumbImgUrl() {
        return thumbImgUrl;
    }

    public void setThumbImgUrl(String thumbImgUrl) {
        this.thumbImgUrl = thumbImgUrl;
    }

    @Override
    public String toString() {
        return "Image{" +
                "imgUrl='" + imgUrl + '\'' +
                ", thumbImgUrl='" + thumbImgUrl + '\'' +
                '}';
    }
}
