package com.ctrl.forum.entity;

/**
 * 用户插件
 * Created by Administrator on 2016/6/8.
 */
public class Plugin {
    private String name;//插件名称
    private String linkUrl; //指向链接
    private String iconUrl; //图片路径
    private String remark; //备注

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "Plugin{" +
                "iconUrl='" + iconUrl + '\'' +
                ", name='" + name + '\'' +
                ", linkUrl='" + linkUrl + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
