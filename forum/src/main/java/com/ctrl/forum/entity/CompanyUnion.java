package com.ctrl.forum.entity;

import java.util.List;

/**
 * 联合商家 实体
 * Created by Administrator on 2016/4/8.
 */
public class CompanyUnion {
    private String id;//店铺id
    private String name;//店铺名称
    private String img;//店铺图片
    private List<Company>tCompanysLis;

    public List<Company> gettCompanysLis() {
        return tCompanysLis;
    }

    public void settCompanysLis(List<Company> tCompanysLis) {
        this.tCompanysLis = tCompanysLis;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
