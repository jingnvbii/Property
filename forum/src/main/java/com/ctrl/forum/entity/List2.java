package com.ctrl.forum.entity;

/**
 * Created by Administrator on 2016/6/13.
 */
public class List2 {
    private String id; //二级菜单id;
    private String pid; //二级菜单父id;
    private String pTree; //二级菜单树形结构
    private String name; //二级菜单名称

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

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getpTree() {
        return pTree;
    }

    public void setpTree(String pTree) {
        this.pTree = pTree;
    }
}
