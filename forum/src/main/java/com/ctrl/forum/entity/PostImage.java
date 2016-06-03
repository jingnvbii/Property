package com.ctrl.forum.entity;

/**
 * 帖子图片实体
 * Created by Administrator on 2016/4/8.
 */
public class PostImage {
    private String id;//图片id
    private String img ;//原图url
    private String thumbImg ;//缩略图url
    private String targetId ;//关联帖子id
    private String remark ;//图片说明或者备注
    private String pageNum ;//关联帖子id
    private String pageSize ;//关联帖子id
    private String type ;//关联帖子id
    private String createTime ;//关联帖子id

    public String getPageNum() {
        return pageNum;
    }

    public void setPageNum(String pageNum) {
        this.pageNum = pageNum;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

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
