package com.ctrl.android.yinfeng.entity;

import java.util.List;

/**
 * 报修实体类
 * Created by jason on 2015/11/30.
 */
public class Job {
    private String id;//我的工单id
    private String repairNum;//报修编号
    private String repairKindName;//报修类型名称
    private String createTime;//报修创建时间
    private String handleStatus;//处理状态
    private String maintenanceCosts;//维修费用
    private String feedbackTime;//评价反馈时间
    private String payState;//支付状态（0：未付款、1：已付款）
    private String isFeedback;//物业是否已反馈(0：未反馈、1：已反馈)
    private String requestState;//报修状态（0：正常、1：已取消）注：用于在已完成页面判断是正常结束的工单还是取消的工单
    private String orderType;//报修是否分配（0:未分配、1：已分配）注：未分配时显示“抢单”按钮，已分配时显示“接单”按钮
    private String repairKindId;//报修类型id 报修类型id串 关联cxh_tb_kind的id（多个id“，”分隔存放）
    private List<RepairKind>repairKindList;

    public List<RepairKind> getrepairKindList() {
        return repairKindList;
    }

    public void setList(List<RepairKind> repairKindList) {
        this.repairKindList = repairKindList;
    }

    //报修详情使用
    private String communityName;//社区名称
    private String building;//楼号
    private String unit;//单元号
    private String room;//房间号
    private String content;//报修内容
    private String evaluateLevel;//评价等级
    private String evaluateContent;//评价内容
    private String hasEvaluate;//是否评价过（0：未评价1：已评价）
    private String result;//物业处理结果
    private String staffName;//处理人员
    private String staffMobile;//联系电话
    private String communityId;//社区ID
    private String proprietorId;//业主id
    private String addressId;//地区ID
    private String title;//报修标题
    private String acceptState;//任务接收状态


    public String getPayState() {
        return payState;
    }

    public void setPayState(String payState) {
        this.payState = payState;
    }

    public String getIsFeedback() {
        return isFeedback;
    }

    public void setIsFeedback(String isFeedback) {
        this.isFeedback = isFeedback;
    }

    public String getRequestState() {
        return requestState;
    }

    public void setRequestState(String requestState) {
        this.requestState = requestState;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }


    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffMobile() {
        return staffMobile;
    }

    public void setStaffMobile(String staffMobile) {
        this.staffMobile = staffMobile;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaintenanceCosts() {
        return maintenanceCosts;
    }

    public void setMaintenanceCosts(String maintenanceCosts) {
        this.maintenanceCosts = maintenanceCosts;
    }

    public String getFeedbackTime() {
        return feedbackTime;
    }

    public void setFeedbackTime(String feedbackTime) {
        this.feedbackTime = feedbackTime;
    }

    public String getRepairKindId() {
        return repairKindId;
    }

    public void setRepairKindId(String repairKindId) {
        this.repairKindId = repairKindId;
    }
    public String getRepairNum() {
        return repairNum;
    }

    public void setRepairNum(String repairNum) {
        this.repairNum = repairNum;
    }

    public String getRepairKindName() {
        return repairKindName;
    }

    public void setRepairKindName(String repairKindName) {
        this.repairKindName = repairKindName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(String handleStatus) {
        this.handleStatus = handleStatus;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEvaluateLevel() {
        return evaluateLevel;
    }

    public void setEvaluateLevel(String evaluateLevel) {
        this.evaluateLevel = evaluateLevel;
    }

    public String getEvaluateContent() {
        return evaluateContent;
    }

    public void setEvaluateContent(String evaluateContent) {
        this.evaluateContent = evaluateContent;
    }

    public String getHasEvaluate() {
        return hasEvaluate;
    }

    public void setHasEvaluate(String hasEvaluate) {
        this.hasEvaluate = hasEvaluate;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }
    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
    public String getProprietorId() {
        return proprietorId;
    }

    public void setProprietorId(String proprietorId) {
        this.proprietorId = proprietorId;
    }
    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getAcceptState() {
        return acceptState;
    }

    public void setAcceptState(String acceptState) {
        this.acceptState = acceptState;
    }


}
