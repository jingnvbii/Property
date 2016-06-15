package com.ctrl.forum.entity;

import java.io.Serializable;

/**
 * 导航栏实体
 * Created by jason on 2016/4/19.
 */
public class NavigationBar implements Serializable {
    private String kindName;//类别名称
    private String kindIcon;//类别图标url
    private String sort;//排序
    private String commentCode;//备注码（0：广场、1：商城、2：小区、3：周边、4：我）

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getKindName() {
        return kindName;
    }

    public void setKindName(String kindName) {
        this.kindName = kindName;
    }

    public String getKindIcon() {
        return kindIcon;
    }

    public void setKindIcon(String kindIcon) {
        this.kindIcon = kindIcon;
    }

    public String getCommentCode() {
        return commentCode;
    }

    public void setCommentCode(String commentCode) {
        this.commentCode = commentCode;
    }

}
