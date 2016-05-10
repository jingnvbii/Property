package com.ctrl.forum.entity;

/**
 * 积分历史记录
 * Created by Administrator on 2016/5/9.
 */
public class RedeemHistory {
    private String point;//积分（+获得-扣除）
    private String pointType;//类型（0：会员注册、1：商城消费、2：积分兑换...后续逐渐追加）
    private String totalPoint;//剩余积分
    private String createTime;//消耗时间

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getPointType() {
        return pointType;
    }

    public void setPointType(String pointType) {
        this.pointType = pointType;
    }

    public String getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(String totalPoint) {
        this.totalPoint = totalPoint;
    }
}
