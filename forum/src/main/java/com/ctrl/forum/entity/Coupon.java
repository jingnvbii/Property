package com.ctrl.forum.entity;

/**
 * 现金劵
 * Created by Administrator on 2016/5/3.
 */
public class Coupon {
    private String id;//现金劵id
    private String companyId;//商铺id
    private String name;//现金劵名称
    private String remark;//现金劵标注
    private String validityStartTime;//使用起始时间
    private String validityEndTime;//使用终止时间
    private String amount;//现金劵金额
    private String useType;//使用方式（0：使用核销、1：分享核销）
    private String useState;//使用状态（0：未使用、1：已使用、2：已过期）


    public Coupon() {
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUseState() {
        return useState;
    }

    public void setUseState(String useState) {
        this.useState = useState;
    }

    public String getUseType() {
        return useType;
    }

    public void setUseType(String useType) {
        this.useType = useType;
    }

    public String getValidityEndTime() {
        return validityEndTime;
    }

    public void setValidityEndTime(String validityEndTime) {
        this.validityEndTime = validityEndTime;
    }

    public String getValidityStartTime() {
        return validityStartTime;
    }

    public void setValidityStartTime(String validityStartTime) {
        this.validityStartTime = validityStartTime;
    }

}
