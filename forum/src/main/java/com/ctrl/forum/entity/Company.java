package com.ctrl.forum.entity;

/**
 * 店铺实体
 * Created by Administrator on 2016/4/8.
 */
public class Company {
    private String id;//店铺id
    private String name;//店铺名称
    private String img;//店铺图片
    private String address;//店铺地址
    private String mobile;//店铺电话
    private String collectState;//用户是否已收藏
    private String workStartTime;//营业起始时间
    private String workEndTime;//营业终止时间
    private String notice;//店铺介绍
    private String information;//店铺公告
    private String evaluatLevel;//店铺等级 值/2=等级

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCollectState() {
        return collectState;
    }

    public void setCollectState(String collectState) {
        this.collectState = collectState;
    }

    public String getWorkStartTime() {
        return workStartTime;
    }

    public void setWorkStartTime(String workStartTime) {
        this.workStartTime = workStartTime;
    }

    public String getWorkEndTime() {
        return workEndTime;
    }

    public void setWorkEndTime(String workEndTime) {
        this.workEndTime = workEndTime;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getEvaluatLevel() {
        return evaluatLevel;
    }

    public void setEvaluatLevel(String evaluatLevel) {
        this.evaluatLevel = evaluatLevel;
    }
}

