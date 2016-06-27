package com.ctrl.forum.entity;

/**
 * 我的订单
 * Created by Administrator on 2016/5/16.
 */
public class MemeberOrder {
    private String id;//订单id
    private String totalCost;//订单总价格
    private String Img;//商家logo地址
    private long createTime;//订单创建时间
    private String companyname;//商家名称
    private String companyId;//商家id
    private String evaluationState;//评价状态（0：未评价、1：已评价）
    private String state;//订单状态 1-订单被用户取消 2-订单被系统取消 3-未付款 4-商家未发货 5-用户未签收 6-用户已签收
    private String couponMoney;//优惠劵金额
    private String orderNum;//订单编号

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getCouponMoney() {
        return couponMoney;
    }

    public void setCouponMoney(String couponMoney) {
        this.couponMoney = couponMoney;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getEvaluationState() {
        return evaluationState;
    }

    public void setEvaluationState(String evaluationState) {
        this.evaluationState = evaluationState;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return Img;
    }

    public void setImg(String img) {
        Img = img;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }
}
