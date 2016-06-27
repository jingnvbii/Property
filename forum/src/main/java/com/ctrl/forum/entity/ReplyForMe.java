package com.ctrl.forum.entity;

/**
 * 我的评论
 * Created by Administrator on 2016/4/22.
 */
public class ReplyForMe {
    private String id; //评论的id
    private String postId; //帖子id
    private String reporterId; //帖子发布者id
    private String memberId;  //评论者id
    private String memberName; //评论者名称
    private String imgUrl; //评论者头像Url
    private String memberLevel; //评论者等级
    private String replyType; //评论类型（0：对帖子的评论、1：对评论的回复）
    private String replyContent;//评论内容
    private String soundUrl;//语音Url
    private long createTime;//评论时间
    private String myReplyContent;//我的回复内容
    private String contentType; //评论内容类型（0：文字或者表情、1：图片、2：语音）
    private String myContentType; //回复内容类型（0：文字或者表情、1：图片、2：语音）
    private String isReplied; //是否有回复（0：无、1：有）

    public String getIsReplied() {
        return isReplied;
    }

    public void setIsReplied(String isReplied) {
        this.isReplied = isReplied;
    }

    public String getReplyType() {
        return replyType;
    }

    public void setReplyType(String replyType) {
        this.replyType = replyType;
    }

    public String getMyContentType() {
        return myContentType;
    }

    public void setMyContentType(String myContentType) {
        this.myContentType = myContentType;
    }

    public ReplyForMe() {}

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

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

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberLevel() {
        return memberLevel;
    }

    public void setMemberLevel(String memberLevel) {
        this.memberLevel = memberLevel;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMyReplyContent() {
        return myReplyContent;
    }

    public void setMyReplyContent(String myReplyContent) {
        this.myReplyContent = myReplyContent;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public String getReporterId() {
        return reporterId;
    }

    public void setReporterId(String reporterId) {
        this.reporterId = reporterId;
    }

    public String getSoundUrl() {
        return soundUrl;
    }

    public void setSoundUrl(String soundUrl) {
        this.soundUrl = soundUrl;
    }
}
