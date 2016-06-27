package com.ctrl.forum.entity;

/**
 * 现金券 实体
 * Created by Administrator on 2016/4/8.
 */
public class CashCoupons {
    private String id;//现金券id
    private String validityStartTime;//现金券有效期起始时间
    private String validityEndTime;//现金券有效期结束时间
    private String remark;//现金券备注
    private String name;//现金券名称
    private String useType;//现金券使用类型
    private String amount;//现金券使用金额

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUseType() {
        return useType;
    }

    public void setUseType(String useType) {
        this.useType = useType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValidityStartTime() {
        return validityStartTime;
    }

    public void setValidityStartTime(String validityStartTime) {
        this.validityStartTime = validityStartTime;
    }

    public String getValidityEndTime() {
        return validityEndTime;
    }

    public void setValidityEndTime(String validityEndTime) {
        this.validityEndTime = validityEndTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
