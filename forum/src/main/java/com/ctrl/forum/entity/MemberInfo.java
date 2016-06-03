package com.ctrl.forum.entity;

/**
 * 用户实体
 * Created by jason on 2016/4/19
 */
public class MemberInfo {

    private String id;//会员Id
    private String nickName;//昵称
    private String mobile;//手机号
    private String point;//积分
    private String remark;//个人介绍
    private String memberLevel;//会员等级
    private String imgUrl;//头像url地址
    private String isAdmin;//是否管理员（0：否  1：是）
    private String address;//会员地址
    private String companyState;//是否有店铺 0-没有, 如果是checkState:0：待审核、1：已通过
    private String companyId;//店铺id
    private String isShielded;//是否被屏蔽（0：否、1：是）
    private String couponsNum;//现金券数量
    private String redenvelopeNum;//优惠券数量
    private String signTimes;//连续签到次数
    private String communityId; //小区的id
    private String communityName; //小区名称

    private String userName;//用户名（登录名）
    private String password;//用户密码
    private String trueName;//真实姓名
    private String registIp;//注册ip
    private String registTime;//注册时间
    private String messageCount;  //通知消息数量
    private String signState; //是否签到; 0:没签到   1:已签到

    private String memberName;//用户名称

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(String messageCount) {
        this.messageCount = messageCount;
    }

    public String getSignState() {
        return signState;
    }

    public void setSignState(String signState) {
        this.signState = signState;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getRegistIp() {
        return registIp;
    }

    public void setRegistIp(String registIp) {
        this.registIp = registIp;
    }

    public String getRegistTime() {
        return registTime;
    }

    public void setRegistTime(String registTime) {
        this.registTime = registTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getMemberLevel() {
        return memberLevel;
    }

    public void setMemberLevel(String memberLevel) {
        this.memberLevel = memberLevel;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCouponsNum() {
        return couponsNum;
    }

    public void setCouponsNum(String couponsNum) {
        this.couponsNum = couponsNum;
    }

    public String getIsShielded() {
        return isShielded;
    }

    public void setIsShielded(String isShielded) {
        this.isShielded = isShielded;
    }

    public String getRedenvelopeNum() {
        return redenvelopeNum;
    }

    public void setRedenvelopeNum(String redenvelopeNum) {
        this.redenvelopeNum = redenvelopeNum;
    }

    public String getSignTimes() {
        return signTimes;
    }

    public void setSignTimes(String signTimes) {
        this.signTimes = signTimes;
    }

    public String getCompanyState() {
        return companyState;
    }

    public void setCompanyState(String companyState) {
        this.companyState = companyState;
    }
}
