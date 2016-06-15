package com.ctrl.forum.entity;

/**
 * Created by jingjing on 2016/6/7.
 */
public class Message {
    private String id;//消息的主键id
    private String msgType;//消息类型（1：用户下单支付成功<通知商家>、2：商家发货<通知买家>、3：买家领取优惠券<通知买家>、4：商家赠送现金券给买家<通知买家>、5：会员发布帖子<通知会员>、6：已发布帖子需要审核<通知会员>、7：帖子被赞<通知发帖人>、8：帖子收到评论、9：帖子评论收到回复......其他逐渐添加）
    private String targetId;//目标id（例如：帖子id）
    private String appendContent;//附加内容（帖子标题、帖子内容摘要）
    private String reporterId;//消息发送者id（系统消息：发送者id为admin）
    private String receiverId;//消息接收者id
    private String memberName;//会员昵称
    private String memberLevel;//会员等级
    private String imgUrl;//会员头像
    private String content; //消息内容

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getAppendContent() {
        return appendContent;
    }

    public void setAppendContent(String appendContent) {
        this.appendContent = appendContent;
    }

    public String getReporterId() {
        return reporterId;
    }

    public void setReporterId(String reporterId) {
        this.reporterId = reporterId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberLevel() {
        return memberLevel;
    }

    public void setMemberLevel(String memberLevel) {
        this.memberLevel = memberLevel;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
