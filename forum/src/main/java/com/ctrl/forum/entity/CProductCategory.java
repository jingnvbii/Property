package com.ctrl.forum.entity;

import java.util.List;

/**
 * 商品管理--分类列表
 * Created by Administrator on 2016/5/18.
 */
public class CProductCategory {
    private String id;//商品分类主键id
    private String name; //分类名称
    private String icon; //分类图标
    private List<CProduct> productList;

    public List<CProduct> getcProducts() {
        return productList;
    }

    public void setcProducts(List<CProduct> cProducts) {
        this.productList = cProducts;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

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

    @Override
    public String toString() {
        return "CProductCategory{" +
                "icon='" + icon + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", productList=" + productList +
                '}';
    }
}
