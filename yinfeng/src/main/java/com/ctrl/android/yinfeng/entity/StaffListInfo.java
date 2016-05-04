package com.ctrl.android.yinfeng.entity;

/**
 * 报修实体类
 * Created by jason on 2015/11/30.
 */
public class StaffListInfo {
   private String id;//员工id
   private String communityId;//社区id
   private String name;//员工姓名
   private String specialty;//擅长
   private String mobile;//手机号
   private String imgUrl;//头像

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getCountOnRepairing() {
        return countOnRepairing;
    }

    public void setCountOnRepairing(int countOnRepairing) {
        this.countOnRepairing = countOnRepairing;
    }

    private int countOnRepairing;//现有的维修工单

}
