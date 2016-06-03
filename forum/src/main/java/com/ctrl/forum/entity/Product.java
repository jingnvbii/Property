package com.ctrl.forum.entity;

/**
 * 商品实体类
 * Created by Administrator on 2016/4/8.
 */
public class Product {
    private String id; //商品主键id
    private String name;//商品名称
    private String nums;//数量
    private String amounts;//单个商品合计

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNums() {
        return nums;
    }

    public void setNums(String nums) {
        this.nums = nums;
    }

    public String getAmounts() {
        return amounts;
    }

    public void setAmounts(String amounts) {
        this.amounts = amounts;
    }
}
