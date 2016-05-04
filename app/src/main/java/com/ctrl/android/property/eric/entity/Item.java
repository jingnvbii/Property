package com.ctrl.android.property.eric.entity;

/**
 *
 * Created by Eric on 2015/11/13.
 */
public class Item {

    public Item(int flg,String name,boolean check){

        this.flg = flg;
        this.name = name;
        this.check = check;

    }

    private int flg;
    private String name;
    private boolean check;

    public int getFlg() {
        return flg;
    }

    public void setFlg(int flg) {
        this.flg = flg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
