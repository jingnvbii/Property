package com.ctrl.forum.entity;

/**
 *公告图片实体
 * Created by Administrator on 2016/4/8.
 */
public class NoticeImage {
    private String id;//分类id
    private String imgUrl;//类别名称
    private String type;//类别图标Url
    private String targetId;//类别图标Url
    private String targetUrl;//类别图标Url
    private String sortNum;//类别图标Url
    private String delflag;//类别图标Url

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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

    public String getSortNum() {
        return sortNum;
    }

    public void setSortNum(String sortNum) {
        this.sortNum = sortNum;
    }

    public String getDelflag() {
        return delflag;
    }

    public void setDelflag(String delflag) {
        this.delflag = delflag;
    }
}
