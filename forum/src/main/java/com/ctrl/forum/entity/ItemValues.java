package com.ctrl.forum.entity;

/**
 * 字典值
 * Created by Administrator on 2016/5/26.
 */
public class ItemValues {
    private String id; //主键id
    private String itemValue; //值

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
