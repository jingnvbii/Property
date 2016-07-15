package com.ctrl.forum.entity;

/**
 * 店铺回复 实体
 * Created by Administrator on 2016/4/8.
 */
public class ShopReply {
    private String id;//回复id
    private String level;//回复等级
    private String content;//回复内容
    private String createTime;//回复创建时间
    private String kfReplay;//客服回复
    private String reportName;//回复用户昵称
    private String count;//当前列表总数
    private String replyTime;//客服回复时间

    public String getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(String replyTime) {
        this.replyTime = replyTime;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
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

    public String getKfReplay() {
        return kfReplay;
    }

    public void setKfReplay(String kfReplay) {
        this.kfReplay = kfReplay;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }
}
