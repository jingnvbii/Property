package com.ctrl.forum.entity;

/**
 * 公告实体类
 * Created by Administrator on 2016/4/8.
 */
public class Notice {
    private String id;//公告id
    private String noticeTitle;//公告标题
    private String targetId;//目标id（商家id、商品id或者帖子id）
    private String type;//内容类型（0：商家、1：商品、2：帖子）

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private String content;//公告内容

}
