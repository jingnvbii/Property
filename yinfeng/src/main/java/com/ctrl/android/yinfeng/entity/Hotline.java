package com.ctrl.android.yinfeng.entity;


/**
 * 通讯录
 * Created by Jason on 2015/12/28.
 */
public class Hotline {
    private String id;//分组ID
    private String grade;//节点级别
    private String name;//分组名称
    private String contactName;//联系人姓名
    private String imgUrl;//联系人头像Url
    private String gender;//联系人性别（0：男、1：女）
    private String contactGrade;//联系人职位级别（0：经理、1：主管、2：普通员工）
    private String jobType;//岗位类别（0：本部、1：客服、2：工程维修、3：保洁、4：保安）
    private String mobile;//手机号码/固话号码
    private String address;//现住址（详细）
    private String email;//电子邮件地址
    private String staffCount;//当前分组下联系人数量

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getContactGrade() {
        return contactGrade;
    }

    public void setContactGrade(String contactGrade) {
        this.contactGrade = contactGrade;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStaffCount() {
        return staffCount;
    }

    public void setStaffCount(String staffCount) {
        this.staffCount = staffCount;
    }
}
