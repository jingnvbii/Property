package com.ctrl.forum.entity;

import java.util.List;

/**
 * 商品分类实体
 * Created by Administrator on 2016/4/8.
 */
public class ProductCategroy {
    private String id;//商品分类主键id
    private String name;//分类名称
   private String icon;//分类图标
    private List<Product2> productList ;//商品list

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<Product2> getProductList() {
        return productList;
    }

    public void setProductList(List<Product2> productList) {
        this.productList = productList;
    }
}
