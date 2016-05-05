package com.ctrl.forum.entity;


public class Member {
    private String name;
    private String path;

    public Member(String name) {this.name = name;}
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString() {return "Type{" + "name='" + name + '\'' + ", path='" + path + '\'' + '}';}
}
