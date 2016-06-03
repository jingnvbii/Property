package com.ctrl.forum.entity;

/**
 * 优惠券实体类
 * Created by Administrator on 2016/4/8.
 */
public class Redenvelope {
    private String id; //优惠券主键id
    private String amount;//优惠券金额
    private String useRule;//使用规则说明

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUseRule() {
        return useRule;
    }

    public void setUseRule(String useRule) {
        this.useRule = useRule;
    }
}
