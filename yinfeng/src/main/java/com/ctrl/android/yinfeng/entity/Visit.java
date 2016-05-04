package com.ctrl.android.yinfeng.entity;

import java.util.List;

/**
 * 到访实体
 * Created by jasom on 2015/11/26
 */
public class Visit {
    private String id;//社区id
    private String proprietorId;//业主id
    private String addressId;//业主住址id
    private String visitorName;//到访人姓名
    private String arriveTime;//到访时间
    private String peopleNum;//到访人数
    private String numberPlates;//车牌号码
    private String residenceTime;//预计停留时间（注：单位已输入为准）
    private String visitNum;//到访编号
    private String createTime;//创建时间
    private String visitState;//到访状态（0：已预约、1：已结束（由系统自动处理））


    //获取社区到访信息时用
    private String memberName;//拜访人姓名
    private String visitorMobile;//拜访人电话
    private String visitorHeadImg;//拜访人照片
    private String qrImgUrl;//二维码url
    private String proprietorName;//业主姓名
    private String droveVisit;//是否驾车来访（0：否、1：是）
    private String gender;//性别（0：男、1：女）

    private String building;//到访楼号
    private String unit;//到访单元号
    private String room;//到访房号
    private String communityName;//到访社区名称

    private List<Img2> communityVisitPictureList;//到访图片


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProprietorId() {
        return proprietorId;
    }

    public void setProprietorId(String proprietorId) {
        this.proprietorId = proprietorId;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }


    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public String getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
    }

    public String getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(String  peopleNum) {
        this.peopleNum = peopleNum;
    }

    public String getNumberPlates() {
        return numberPlates;
    }

    public void setNumberPlates(String numberPlates) {
        this.numberPlates = numberPlates;
    }

    public String getResidenceTime() {
        return residenceTime;
    }

    public void setResidenceTime(String residenceTime) {
        this.residenceTime = residenceTime;
    }

    public String getVisitNum() {
        return visitNum;
    }

    public void setVisitNum(String visitNum) {
        this.visitNum = visitNum;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getVisitState() {
        return visitState;
    }

    public void setVisitState(String visitState) {
        this.visitState = visitState;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getVisitorMobile() {
        return visitorMobile;
    }

    public void setVisitorMobile(String visitorMobile) {
        this.visitorMobile = visitorMobile;
    }

    public String getVisitorHeadImg() {
        return visitorHeadImg;
    }

    public void setVisitorHeadImg(String visitorHeadImg) {
        this.visitorHeadImg = visitorHeadImg;
    }

    public String getQrImgUrl() {
        return qrImgUrl;
    }

    public void setQrImgUrl(String qrImgUrl) {
        this.qrImgUrl = qrImgUrl;
    }

    public String getProprietorName() {
        return proprietorName;
    }

    public void setProprietorName(String proprietorName) {
        this.proprietorName = proprietorName;
    }

    public String getDroveVisit() {
        return droveVisit;
    }

    public void setDroveVisit(String droveVisit) {
        this.droveVisit = droveVisit;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public List<Img2> getCommunityVisitPictureList() {
        return communityVisitPictureList;
    }

    public void setCommunityVisitPictureList(List<Img2> communityVisitPictureList) {
        this.communityVisitPictureList = communityVisitPictureList;
    }
}