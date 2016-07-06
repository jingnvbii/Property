package com.ctrl.forum.entity;

/**
 * 积分历史记录
 * Created by Administrator on 2016/5/9.
 */
public class RedeemHistory {
    private int point;//积分（+获得-扣除）
    private String doType;//用户行为类型（1：商城消费、2：积分兑换、3：每日签到、4：连续签到30天、5：连续签到100天、6：连续签到200天、7：绑定手机号、8：完整填写个人资料、9：回别人帖、10：被管理员拉黑、11：被拉黑设备...后续逐渐追加）
    private String totalPoint;//剩余积分
    private long createTime;//消耗时间

    public String getDoType() {
        return doType;
    }

    public void setDoType(String doType) {
        this.doType = doType;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }


    public String getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(String totalPoint) {
        this.totalPoint = totalPoint;
    }
}
