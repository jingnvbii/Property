package com.ctrl.forum.entity;

/**
 * 优惠劵-查询总数
 * Created by Administrator on 2016/5/10.
 */
public class Count {
    private String used;//使用过总数
    private String notUsed;//未使用过总数
    private String expired;//已过期总数

    public String getExpired() {
        return expired;
    }

    public void setExpired(String expired) {
        this.expired = expired;
    }

    public String getNotUsed() {
        return notUsed;
    }

    public void setNotUsed(String notUsed) {
        this.notUsed = notUsed;
    }

    public String getUsed() {
        return used;
    }

    public void setUsed(String used) {
        this.used = used;
    }
}
