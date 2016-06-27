package com.ctrl.forum.entity;

/**
 * 订单状态
 * Created by Administrator on 2016/4/25.
 */
public class OrderState {
    private long createTime;//下单时间
    private String acceptTime;//商家接单时间
    private String sellCancelTime;//商家取消订单时间
    private String cancelTime;//买家取消订单时间
    private String arriveTime;//送达时间
    private String signTime;//签收时间
    private String payTime;//支付时间
    private Double couponMoney;//优惠券金额
    private Double totalCost;//总价（已减去优惠金额）
    private Double deliveryNo;//快递单号
    private Double companyPhone;//商家联系电话
    private String contact;//如果快递显示单号为空，则显示商家显示联系电话
    private String companyname;//店铺名称
    private String orderNum;//订单编号
    private String receiverName; //收件人
    private String address; //送货地址
    private String state; //状态
    private String content; //评价内容
    private String level; //评价等级

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(String acceptTime) {
        this.acceptTime = acceptTime;
    }

    public String getSellCancelTime() {
        return sellCancelTime;
    }

    public void setSellCancelTime(String sellCancelTime) {
        this.sellCancelTime = sellCancelTime;
    }

    public String getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(String cancelTime) {
        this.cancelTime = cancelTime;
    }

    public String getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
    }

    public String getSignTime() {
        return signTime;
    }

    public void setSignTime(String signTime) {
        this.signTime = signTime;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public Double getCouponMoney() {
        return couponMoney;
    }

    public void setCouponMoney(Double couponMoney) {
        this.couponMoney = couponMoney;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public Double getDeliveryNo() {
        return deliveryNo;
    }

    public void setDeliveryNo(Double deliveryNo) {
        this.deliveryNo = deliveryNo;
    }

    public Double getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(Double companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }
}
