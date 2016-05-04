package com.ctrl.forum.entity;

/**
 * 公告实体类
 * Created by Administrator on 2016/4/8.
 */
public class Notice {
    private String id;//公告id
    private String noticeTitle;//公告标题

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
