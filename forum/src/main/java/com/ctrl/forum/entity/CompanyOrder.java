package com.ctrl.forum.entity;

/**
 * 商家获取订单
 * Created by Administrator on 2016/5/16.
 */
public class CompanyOrder {
    private long createTime; //下单时间
    private long acceptTime; //商家接单时间
    private long sellCancelTime; //商家取消订单时间
    private long cancelTime; //买家取消订单时间
    private long arriveTime; //送达时间
    private long signTime; //签收时间
    private long payTime; //支付时间
    private String contact; //如果快递显示单号，商家显示手机号
    private String companyname; //店铺名称
    private String orderNum; //订单编号
    private String receiverName; //收件人
    private String address; //送货地址
    private String payMode; //支付方式（1：在线支付、2：货到付款）
    private String state; //状态state 1-订单被用户取消 2-订单被系统取消 3-未付款 4-商家未发货 5-用户未签收 6-用户已签收
    private String receiverMobile; //收件人电话
    private String id; //订单id
    private String evaluationState;//String	评价状态（0：未评价、1：已评价）
    private String deliveryNo;//快递单号
    private String expressName;//快递公司名称

    public String getDeliveryNo() {
        return deliveryNo;
    }

    public void setDeliveryNo(String deliveryNo) {
        this.deliveryNo = deliveryNo;
    }

    public String getEvaluationState() {
        return evaluationState;
    }

    public void setEvaluationState(String evaluationState) {
        this.evaluationState = evaluationState;
    }

    public String getExpressName() {
        return expressName;
    }

    public void setExpressName(String expressName) {
        this.expressName = expressName;
    }

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

    public long getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(long acceptTime) {
        this.acceptTime = acceptTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(long arriveTime) {
        this.arriveTime = arriveTime;
    }

    public long getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(long cancelTime) {
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

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
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

    public long getPayTime() {
        return payTime;
    }

    public void setPayTime(long payTime) {
        this.payTime = payTime;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public long getSellCancelTime() {
        return sellCancelTime;
    }

    public void setSellCancelTime(long sellCancelTime) {
        this.sellCancelTime = sellCancelTime;
    }

    public long getSignTime() {
        return signTime;
    }

    public void setSignTime(long signTime) {
        this.signTime = signTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
