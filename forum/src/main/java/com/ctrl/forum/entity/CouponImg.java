package com.ctrl.forum.entity;

/**
 * 优惠卷图片
 * Created by jingjing on 2016/6/13.
 */
public class CouponImg {
    private String id;//图片id
    private String img;//原图地址
    private String thumbImg;      //缩略图
    private String remark;      //图片说明

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getThumbImg() {
        return thumbImg;
    }

    public void setThumbImg(String thumbImg) {
        this.thumbImg = thumbImg;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
