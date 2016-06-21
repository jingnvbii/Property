package com.ctrl.forum.entity;

/**
 * 店铺详情
 * Created by Administrator on 2016/6/15.
 */
public class CompanyInfo {
    private String companyId; //店铺id
    private String companyName; //店铺名称
    private long companyStartTime; //店铺营业时间
    private long companyEndTime; //店铺结业时间
    private String packetEnable; //是否使用优惠券（0-否 1-是）
    private String couponEnable; //是否使用现金券（0-否 1-是）
    private String mobile; //店铺电话
    private String companyKind; //店铺分类
    private String img;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public long getCompanyEndTime() {
        return companyEndTime;
    }

    public void setCompanyEndTime(long companyEndTime) {
        this.companyEndTime = companyEndTime;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyKind() {
        return companyKind;
    }

    public void setCompanyKind(String companyKind) {
        this.companyKind = companyKind;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public long getCompanyStartTime() {
        return companyStartTime;
    }

    public void setCompanyStartTime(long companyStartTime) {
        this.companyStartTime = companyStartTime;
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
