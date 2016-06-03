package com.ctrl.forum.entity;

/**
 * 收藏的店铺
 * Created by Administrator on 2016/5/13.
 */
public class CompanyCollect {
    private String id;//商家id
    private String name;//商家名称
    private String img; //商家LOG地址
    private String state; //是否营业中（0：休息中、1：营业中）
    private String packetEnable; //是否可以使用优惠券（0：不可以、1：可以）
    private String couponEnable;//是否可以使用现金券（0：不可以、1：可以）
    private String evaluatLevel;//评价等级   xxxxxxxxxxxxxxxxxxxxxxxx [如果获取不到数据,将int改为string]
    private String dis;//距离
    private String cashName; //现金劵名称

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCashName() {
        return cashName;
    }

    public void setCashName(String cashName) {
        this.cashName = cashName;
    }

    public String getCouponEnable() {
        return couponEnable;
    }

    public void setCouponEnable(String couponEnable) {
        this.couponEnable = couponEnable;
    }

    public String getDis() {
        return dis;
    }

    public void setDis(String dis) {
        this.dis = dis;
    }

    public String getEvaluatLevel() {
        return evaluatLevel;
    }

    public void setEvaluatLevel(String evaluatLevel) {
        this.evaluatLevel = evaluatLevel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPacketEnable() {
        return packetEnable;
    }

    public void setPacketEnable(String packetEnable) {
        this.packetEnable = packetEnable;
    }
}
