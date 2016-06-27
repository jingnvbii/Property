package com.ctrl.forum.entity;

/**
 * 店铺详情
 * Created by Administrator on 2016/6/15.
 */
public class CompanyInfo {
    private String companyId; //店铺id
    private String name; //店铺名称
    private String workStartTime; //店铺营业时间
    private String workEndTime; //店铺结业时间
    private String packetEnable; //是否使用优惠券（0-否 1-是）
    private String couponEnable; //是否使用现金券（0-否 1-是）
    private String mobile; //店铺电话
    private String img;

    public String getWorkEndTime() {
        return workEndTime;
    }

    public void setWorkEndTime(String workEndTime) {
        this.workEndTime = workEndTime;
    }

    public String getWorkStartTime() {
        return workStartTime;
    }

    public void setWorkStartTime(String workStartTime) {
        this.workStartTime = workStartTime;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }


    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getCouponEnable() {
        return couponEnable;
    }

    public void setCouponEnable(String couponEnable) {
        this.couponEnable = couponEnable;
    }

    public String getPacketEnable() {
        return packetEnable;
    }

    public void setPacketEnable(String packetEnable) {
        this.packetEnable = packetEnable;
    }
}
