package com.ctrl.forum.entity;

/**
 * 商家获取订单
 * Created by Administrator on 2016/5/16.
 */
public class CompanyOrder {
    private String createTime; //下单时间
    private String acceptTime; //商家接单时间
    private String sellCancelTime; //商家取消订单时间
    private String cancelTime; //买家取消订单时间
    private String arriveTime; //送达时间
    private String signTime; //签收时间
    private String payTime; //支付时间
    private String contact; //如果快递显示单号，商家显示手机号
    private String companyname; //店铺名称
    private String orderNum; //订单编号
    private String receiverName; //收件人
    private String address; //送货地址
    private String payMode; //支付方式（1：在线支付、2：货到付款）
    private String state; //状态state 1-订单被用户取消 2-订单被系统取消 3-未付款 4-商家未发货 5-用户未签收 6-用户已签收
    private String receiverMobile; //收件人电话
    private String id; //订单id

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReceiverMobile() {
        return receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    public String getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(String acceptTime) {
        this.acceptTime = acceptTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
    }

    public String getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(String cancelTime) {
        this.cancelTime = cancelTime;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getSellCancelTime() {
        return sellCancelTime;
    }

    public void setSellCancelTime(String sellCancelTime) {
        this.sellCancelTime = sellCancelTime;
    }

    public String getSignTime() {
        return signTime;
    }

    public void setSignTime(String signTime) {
        this.signTime = signTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
