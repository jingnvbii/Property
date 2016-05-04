package com.ctrl.android.yinfeng.entity;

/**
 * 上报实体类
 * Created by jason on 2015/12/28.
 */
public class Report {

    private String id;//事件上报id
    private String communityId;//社区id
    private String staffId;//员工id
    private String title;//标题
    private String kindName;//上报类型名称
    private String createTime;//报修创建时间
    private String handleStatus;//处理状态
    private String eventKindId;//上报类型id
    private String content;//内容
    private String image;//内容


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKindName() {
        return kindName;
    }

    public void setKindName(String kindName) {
        this.kindName = kindName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(String handleStatus) {
        this.handleStatus = handleStatus;
    }

    public String getEventKindId() {
        return eventKindId;
    }

    public void setEventKindId(String eventKindId) {
        this.eventKindId = eventKindId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
