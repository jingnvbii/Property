package com.ctrl.forum.entity;

/**
 * 积分兑换商品实体
 * Created by Administrator on 2016/4/8.
 */
public class TRedeemHistory {
    private String id;//兑换ID
    private String createTime;//兑换时间
    private String productName;//兑换商品名称
    private String point;//兑换时花费积分

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }
}
