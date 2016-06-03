package com.ctrl.forum.entity;

import java.util.List;

/**
 * 帖子回复实体
 * Created by Administrator on 2016/4/8.
 */
public class PostReply2 {
    private String id;//评论id
    private String memberId;//回复者id
    private String imgUrl;//回复者头像Url
    private String memberLevel;//回复者等级（会员等级）


    private String contentType;//内容类型（0：文字或者表情、1：图片、2：语音）
    private String replyContent;//回复内容
    private String soundUrl;//语音文件Url
    private String createTime;//回复时间
    private String memberFloor;//回复者所在楼层
    private String receiverFloor;//被回复者所在楼层
    private String memberName;//回复者昵称
    private String receiverName;//被回复者昵称
    private String replyType;//回复类型 "0"回复帖子 "1"回复帖子

    public String getReplyType() {
        return replyType;
    }

    public void setReplyType(String replyType) {
        this.replyType = replyType;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    private String receiverId;//被回复者id
    private String preContentType;//原原内容类型（0：文字或者表情、1：图片、2：语音）
    private String preContent;//原回复内容

    public List<PostImage> getPostReplyImgList() {
        return postReplyImgList;
    }

    public void setPostReplyImgList(List<PostImage> postReplyImgList) {
        this.postReplyImgList = postReplyImgList;
    }

    public String getPreContentType() {
        return preContentType;
    }

    public void setPreContentType(String preContentType) {
        this.preContentType = preContentType;
    }

    public String getPreContent() {
        return preContent;
    }

    public void setPreContent(String preContent) {
        this.preContent = preContent;
    }

    private List<PostImage> postReplyImgList;//评论图片集合



    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getMemberLevel() {
        return memberLevel;
    }

    public void setMemberLevel(String memberLevel) {
        this.memberLevel = memberLevel;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public String getSoundUrl() {
        return soundUrl;
    }

    public void setSoundUrl(String soundUrl) {
        this.soundUrl = soundUrl;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getMemberFloor() {
        return memberFloor;
    }

    public void setMemberFloor(String memberFloor) {
        this.memberFloor = memberFloor;
    }

    public String getReceiverFloor() {
        return receiverFloor;
    }

    public void setReceiverFloor(String receiverFloor) {
        this.receiverFloor = receiverFloor;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }
}
