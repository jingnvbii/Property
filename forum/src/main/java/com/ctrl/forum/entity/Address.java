package com.ctrl.forum.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/8.
 */
public class Address implements Serializable{
    private String id  ;//地址id
    private String memberId;//用户id
    private String addressDetail;//详细地址
    private String isDefault;//是否默认地址 '0 '1'（0不是，1是）
    private String delFlag;//删除标记（0：启用、1：删除）
    private String mobile;//电话
    private String zip;//邮编
    private String province;//省
    private String city;//市
    private String area;//区
    private String latitude;//经度
    private String longitude;//经度
    private String receiveName;//收货人
    private String addressBase;//收货人

    public String getAddressBase() {
        return addressBase;
    }

    public void setAddressBase(String addressBase) {
        this.addressBase = addressBase;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }
}
