package com.ctrl.forum.entity;

/**
<<<<<<< HEAD
 * 优惠劵
 * Created by Administrator on 2016/5/10.
 */
public class Redenvelope {
    private String id;  //优惠劵id
    private String amount;//优惠劵金额
    private String leastMoney; // 使用优惠劵最低金额
    private String useRule; // 使用规则
    private String number; //优惠劵编码
    private String deadlineTime; //有效期
    private String useState;  //使用状态（0：未使用、1：已使用、2：已过期）
    private String used; //使用过总数
    private String notUsed; //未使用过总数
    private String expired; //已过期总数

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDeadlineTime() {
        return deadlineTime;
    }

    public void setDeadlineTime(String deadlineTime) {
        this.deadlineTime = deadlineTime;
    }

    public String getExpired() {
        return expired;
    }

    public void setExpired(String expired) {
        this.expired = expired;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLeastMoney() {
        return leastMoney;
    }

    public void setLeastMoney(String leastMoney) {
        this.leastMoney = leastMoney;
    }

    public String getUseRule() {
        return useRule;
    }

    public void setUseRule(String useRule) {
        this.useRule = useRule;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUseState() {
        return useState;
    }

    public void setUseState(String useState) {
        this.useState = useState;
    }

    public String getUsed() {
        return used;
    }

    public void setUsed(String used) {
        this.used = used;
    }

    public String getNotUsed() {
        return notUsed;
    }

    public void setNotUsed(String notUsed) {
        this.notUsed = notUsed;
    }
}
