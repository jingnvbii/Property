package com.ctrl.forum.entity;

/**
 * 现金劵包
 * Created by Administrator on 2016/6/3.
 */
public class CouponsPackag {
    private String packageId; //现金包id
    private String nums; //现金包总数量
    private String overNums; //剩余数量
    private String name; //现金包名称

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNums() {
        return nums;
    }

    public void setNums(String nums) {
        this.nums = nums;
    }

    public String getOverNums() {
        return overNums;
    }

    public void setOverNums(String overNums) {
        this.overNums = overNums;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }
}
