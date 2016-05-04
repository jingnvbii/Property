package com.ctrl.android.yinfeng.entity;

/**
 * 员工信息
 * Created by Eric on 2015/11/25.
 */
public class StaffInfo {

    private String staffId;//员工Id
    private String userName;//员工登录名
    private String name;//姓名
    private int gender;//性别（0：男、 1：女）
    private String imgUrl;//头像Url
    private String communityId;//社区ID（工作社区）
    private String communityName;//工作社区名称
    private String provinceName;//所在省（名称）
    private String cityName;//所在市（名称）
    private String areaName;//所在区（名称）


    private String grade;//职位级别（0：经理、1：主管、2：普通员工）
    private String jobType;//岗位类别（0：总部、1：客服、2：工程维修、3：保洁、4：保安）


    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
