package com.ctrl.forum.entity;

/**
 * 周边评论的图片集合
 * Created by Administrator on 2016/6/14.
 */
public class CompanyEvaluationPic {
    private String id; //图片id
    private String img; //评价原图
    private String thumbImg; //评价缩略图
    private String remark; //图片说明

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getThumbImg() {
        return thumbImg;
    }

    public void setThumbImg(String thumbImg) {
        this.thumbImg = thumbImg;
    }
}
