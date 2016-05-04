package com.ctrl.android.yinfeng.ui.adressbook.tree;

/**
 * 树结构  实体类
 * Created by Eric on 2016/3/2.
 */
public class TreeItem {

    private String fatherId = "";
    private String id;
    private int category = 1;
    private String categoryOneId = "";
    private String categoryTwoId = "";
    private String categoryThreeId = "";
    private String categoryFourId = "";
    private boolean show = false;
    private boolean showNext = false;
    private String name;
    private String contactorGrade;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private String phone;

    public String getContactorGrade() {
        return contactorGrade;
    }

    public void setContactorGrade(String contactorGrade) {
        this.contactorGrade = contactorGrade;
    }

    private String contactorName;



    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
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

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public boolean isShowNext() {
        return showNext;
    }

    public void setShowNext(boolean showNext) {
        this.showNext = showNext;
    }

    public String getFatherId() {
        return fatherId;
    }

    public void setFatherId(String fatherId) {
        this.fatherId = fatherId;
    }

    public String getCategoryFourId() {
        return categoryFourId;
    }

    public void setCategoryFourId(String categoryFourId) {
        this.categoryFourId = categoryFourId;
    }

    public String getCategoryOneId() {
        return categoryOneId;
    }

    public void setCategoryOneId(String categoryOneId) {
        this.categoryOneId = categoryOneId;
    }

    public String getCategoryThreeId() {
        return categoryThreeId;
    }

    public void setCategoryThreeId(String categoryThreeId) {
        this.categoryThreeId = categoryThreeId;
    }

    public String getCategoryTwoId() {
        return categoryTwoId;
    }

    public void setCategoryTwoId(String categoryTwoId) {
        this.categoryTwoId = categoryTwoId;
    }
}
