package com.ctrl.android.yinfeng.entity;

/**
 * 公告实体
 * Created by Administrator on 2015/10/28.
 */
public class Notice {
    private String noticeTitle;//公告标题

    //公告列表使用
    private String level;//公告级别（0：重要、1：一般）
    private String status;//状态（0：未读、1：已读）
    private int propertyNoticeNum;//未读公告数量

    public int getPropertyNoticeNum() {
        return propertyNoticeNum;
    }

    public void setPropertyNoticeNum(int propertyNoticeNum) {
        this.propertyNoticeNum = propertyNoticeNum;
    }

    //公告详情使用
    private String content;//公告内容
    private String createTime;//公告发布时间
    private String userName;//用户名
    private String originalImg;//公告图片
    private String id;//通知公告id
    private String isTop;//是否置顶（0：置顶、1：普通）

    public String getIsTop() {
        return isTop;
    }

    public void setIsTop(String isTop) {
        this.isTop = isTop;
    }

    public String getId(){return id;}
    public void setId(String id){this.id=id;}

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOriginalImg() {
        return originalImg;
    }

    public void setOriginalImg(String originalImg) {
        this.originalImg = originalImg;
    }
}
