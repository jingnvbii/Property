package com.ctrl.forum.entity;

/**
 * 个人中心---我的评论列表
 * Created by Administrator on 2016/5/31.
 */
public class ObtainMyReply {
    private String id; //评论的id
    private String postId; //帖子id
    private String contentType; //内容类型（0：文字或者表情、1：图片、2：语音）
    private String replyContent;//回复内容
    private String soundUrl;//语音文件Url
    private long createTime;//回复时间
    private String receiverName; //被回复者昵称
    private String replyType; //评论类型（0：对帖子的评论、1：对评论的回复）
    private String sourceType;//发布来源类型（0：平台、1：App）

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

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

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public String getReplyType() {
        return replyType;
    }

    public void setReplyType(String replyType) {
        this.replyType = replyType;
    }

    public String getSoundUrl() {
        return soundUrl;
    }

    public void setSoundUrl(String soundUrl) {
        this.soundUrl = soundUrl;
    }
}
