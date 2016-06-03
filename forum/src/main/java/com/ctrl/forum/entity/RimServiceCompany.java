package com.ctrl.forum.entity;

/**
 * 周边项目--根据id或者关键字获取周边服务
 * Created by Administrator on 2016/5/23.
 */
public class RimServiceCompany {
    private String id; //Id
    private String name; //商家用户名
    private String telephone; //电话
    private String callTimes; //拨打次数
    private String province; //省代号
    private String provinceName; //省名称
    private String city; //市代号
    private String cityName; //市名称
    private String area; //区代号
    private String areaName; //区名称
    private String latitude; //纬度
    private String longitude; //经度
    private String distance; //距离我当前位置距离
    private String address; //详细地址

    private String aroundServiceId; //周边服务的主键id
    private String memberId; //会员id

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getAroundServiceId() {
        return aroundServiceId;
    }

    public void setAroundServiceId(String aroundServiceId) {
        this.aroundServiceId = aroundServiceId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getCallTimes() {
        return callTimes;
    }

    public void setCallTimes(String callTimes) {
        this.callTimes = callTimes;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
