package com.ctrl.forum.entity;

/**
 * 周边服务--我的收藏
 * Created by Administrator on 2016/5/24.
 */
public class RimServiceCollection {
    private String id; //收藏列表主键id
    private String roundServiceId; //周边服务的主键id
    private String name; //商家名称
    private String address; //详细地址
    private String callTimes; //拨打次数
    private String telephone; //联系电话
    private String memberId; //会员id

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCallTimes() {
        return callTimes;
    }

    public void setCallTimes(String callTimes) {
        this.callTimes = callTimes;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoundServiceId() {
        return roundServiceId;
    }

    public void setRoundServiceId(String roundServiceId) {
        this.roundServiceId = roundServiceId;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
