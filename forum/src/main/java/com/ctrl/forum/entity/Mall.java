package com.ctrl.forum.entity;

/**
 * 商家实体类
 * Created by Administrator on 2016/4/8.
 */
public class Mall {
    private String id; //商家 id
    private String name;//商家名称
    private String img;//商家LOG地址
    private String state;//是否营业中（0：休息中、1：营业中）
    private String packetEnable;//是否可以使用优惠券（0：不可以、1：可以）
    private String couponEnable;//是否可以使用现金券（0：不可以、1：可以）
    private String evaluatLevel;//评价等级
    private String dis;//距离
    private String cashName;//现金券名称
    private String workStartTime;//营业起始时间
    private String workEndTime;//营业终止时间

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPacketEnable() {
        return packetEnable;
    }

    public void setPacketEnable(String packetEnable) {
        this.packetEnable = packetEnable;
    }

    public String getCouponEnable() {
        return couponEnable;
    }

    public void setCouponEnable(String couponEnable) {
        this.couponEnable = couponEnable;
    }

    public String getEvaluatLevel() {
        return evaluatLevel;
    }

    public void setEvaluatLevel(String evaluatLevel) {
        this.evaluatLevel = evaluatLevel;
    }

    public String getDis() {
        return dis;
    }

    public void setDis(String dis) {
        this.dis = dis;
    }

    public String getCashName() {
        return cashName;
    }

    public void setCashName(String cashName) {
        this.cashName = cashName;
    }
}
