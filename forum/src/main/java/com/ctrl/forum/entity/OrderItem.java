package com.ctrl.forum.entity;

/**
 * 订单商品列表
 * Created by Administrator on 2016/4/8.
 */
public class OrderItem {
    private String sellingPrice;//商品单价
    private String amount;//商品总价
    private String nums;//商品数量
    private String productname;//商品名称

    public String getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(String sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNums() {
        return nums;
    }

    public void setNums(String nums) {
        this.nums = nums;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }
}