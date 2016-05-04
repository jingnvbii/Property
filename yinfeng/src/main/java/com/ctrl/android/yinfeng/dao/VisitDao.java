package com.ctrl.android.yinfeng.dao;

import android.util.Log;

import com.beanu.arad.http.IDao;
import com.beanu.arad.http.INetResult;
import com.beanu.arad.utils.JsonUtil;
import com.ctrl.android.yinfeng.base.Constant;
import com.ctrl.android.yinfeng.entity.Img;
import com.ctrl.android.yinfeng.entity.Visit;
import com.ctrl.android.yinfeng.http.AopUtils;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 到访 dao
 * Created by jason on 2015/11/2
 */
public class VisitDao extends IDao {

    /**
     * 访问列表
     * */
    private List<Visit> listVisit = new ArrayList<>();
    private List<Img> listImg = new ArrayList<>();

    private Visit visitDetail;

    /**
     * 房屋列表
     * */
    private List<String> listBuilding = new ArrayList<>();

    /**
     * 单元列表
     * */
    private List<String> listUnit = new ArrayList<>();

    /**
     * 房号列表
     * */
    private List<String> listRoom = new ArrayList<>();


    public VisitDao(INetResult activity){
        super(activity);
    }

    /**
     * 到访列表
     * @param communityId 社区id
     * @param currentPage 当前页码
     * @param rowCountPerPage 每页条数
     * */
    public void requestVisitList(String communityId,String currentPage,String rowCountPerPage){
        Map<String,String> map = new HashMap<String,String>();
        map.put(Constant.APPKEY,Constant.APPKEY_VALUE);
        map.put(Constant.SECRET,Constant.SECRET_VALUE);
        map.put(Constant.VERSION,Constant.VERSION_VALUE);
        map.put(Constant.FORMAT,Constant.FORMAT_VALUE);
        map.put(Constant.TYPE, Constant.TYPE_VALUE);
        map.put(Constant.METHOD,"pm.stf.communityVisit.queryCommunityVisitList");//方法名称

        map.put("communityId",communityId);
        map.put("currentPage",currentPage);
        map.put("rowCountPerPage",rowCountPerPage);

        String sign = AopUtils.sign(map, Constant.SECRET_VALUE);
        map.put("sign",sign);
        postRequest(Constant.RAW_URL, mapToRP(map), 0);
    }

    /**
     * 到访信息详情
     * @param qrImgUrl 到访类型（0：预约到访、1：突发到访）
     *
     * */
    public void requestVisitDetail(String qrImgUrl ){
        Map<String,String> map = new HashMap<String,String>();
        map.put(Constant.APPKEY,Constant.APPKEY_VALUE);
        map.put(Constant.SECRET,Constant.SECRET_VALUE);
        map.put(Constant.VERSION,Constant.VERSION_VALUE);
        map.put(Constant.FORMAT,Constant.FORMAT_VALUE);
        map.put(Constant.TYPE, Constant.TYPE_VALUE);
        map.put(Constant.METHOD,"pm.stf.communityVisit.queryQrImgInfo");//方法名称

        map.put("qrImgUrl",qrImgUrl);

        String sign = AopUtils.sign(map, Constant.SECRET_VALUE);
        map.put("sign",sign);
        postRequest(Constant.RAW_URL, mapToRP(map), 1);
    }

    /**
     * 添加到访信息
     * @param communityId 社区id
     * @param building 楼号
     * @param unit  单元号
     * @param room 房间号
     * @param visitorName 到访人姓名
     * @param memberName 拜访人姓名
     * @param numberPlates 车牌号码
     * @param residenceTime 预计停留时间（注：单位已输入为准）
     * @param peopleNum 到访人数
     * @param droveVisit 是否驾车
     * @param gender 性别
     * @param communityVisitPicStr1 突发到访图片urlString串1（原图、缩略图，url间用逗号分开）
     * @param communityVisitPicStr2 突发到访图片urlString串2（原图、缩略图，url间用逗号分开）
     * @param communityVisitPicStr3 突发到访图片urlString串3（原图、缩略图，url间用逗号分开））
     * */
    public void requestAddVisit(String communityId,String building,
                                String unit,String room,
                                String visitorName,String memberName,
                                String numberPlates,
                                String residenceTime ,
                                String peopleNum,
                                String droveVisit,
                                String gender,
                                String communityVisitPicStr1,
                                String communityVisitPicStr2,String communityVisitPicStr3){
        Map<String,String> map = new HashMap<String,String>();
        map.put(Constant.APPKEY,Constant.APPKEY_VALUE);
        map.put(Constant.SECRET,Constant.SECRET_VALUE);
        map.put(Constant.VERSION,Constant.VERSION_VALUE);
        map.put(Constant.FORMAT,Constant.FORMAT_VALUE);
        map.put(Constant.TYPE, Constant.TYPE_VALUE);
        map.put(Constant.METHOD,"pm.stf.communityVisit.addCommunityVisit");//方法名称

        map.put("communityId",communityId);
        map.put("building",building);
        map.put("unit",unit);
        map.put("room",room);
        map.put("visitorName",visitorName);
        map.put("memberName",memberName);
        map.put("numberPlates",numberPlates);
        map.put("residenceTime",residenceTime);
        map.put("peopleNum",peopleNum);
        map.put("droveVisit",droveVisit);
        map.put("gender",gender);
        map.put("communityVisitPicStr1",communityVisitPicStr1);
        map.put("communityVisitPicStr2",communityVisitPicStr2);
        map.put("communityVisitPicStr3",communityVisitPicStr3);

        String sign = AopUtils.sign(map, Constant.SECRET_VALUE);
        map.put("sign",sign);
        postRequest(Constant.RAW_URL, mapToRP(map), 2);
    }

    /**
     * 处理到访信息
     * @param communityVisitId 社区到访id
     * @param handleStatus 处理状态（1：同意到访、2：拒绝到访、3：我不在家）
     * */
    public void requestHandleVisit(String communityVisitId,String handleStatus){
        Map<String,String> map = new HashMap<String,String>();
        map.put(Constant.APPKEY,Constant.APPKEY_VALUE);
        map.put(Constant.SECRET,Constant.SECRET_VALUE);
        map.put(Constant.VERSION,Constant.VERSION_VALUE);
        map.put(Constant.FORMAT,Constant.FORMAT_VALUE);
        map.put(Constant.TYPE, Constant.TYPE_VALUE);
        map.put(Constant.METHOD,"pm.ppt.communityVisit.handleCommunityVisit");//方法名称

        map.put("communityVisitId",communityVisitId);
        map.put("handleStatus",handleStatus);

        String sign = AopUtils.sign(map, Constant.SECRET_VALUE);
        map.put("sign",sign);
        postRequest(Constant.RAW_URL, mapToRP(map), 3);
    }

    /**
     * 更改预约到访状态
     * @param communityVisitId 到访id
     * */
    public void requestOrderVisitStatus(String communityVisitId){
        Map<String,String> map = new HashMap<String,String>();
        map.put(Constant.APPKEY,Constant.APPKEY_VALUE);
        map.put(Constant.SECRET,Constant.SECRET_VALUE);
        map.put(Constant.VERSION,Constant.VERSION_VALUE);
        map.put(Constant.FORMAT,Constant.FORMAT_VALUE);
        map.put(Constant.TYPE, Constant.TYPE_VALUE);
        map.put(Constant.METHOD,"pm.stf.communityVisit.updateCommunityVisitStatus");//方法名称

        map.put("communityVisitId",communityVisitId);
        String sign = AopUtils.sign(map, Constant.SECRET_VALUE);
        map.put("sign",sign);
        postRequest(Constant.RAW_URL, mapToRP(map), 4);
    }


    /**
     * 获取社区楼号 列表
     * @param communityId 社区ID
     * */
    public void requestBuildingList(String communityId){
        Map<String,String> map = new HashMap<String,String>();
        map.put(Constant.APPKEY,Constant.APPKEY_VALUE);
        map.put(Constant.SECRET,Constant.SECRET_VALUE);
        map.put(Constant.VERSION,Constant.VERSION_VALUE);
        map.put(Constant.FORMAT,Constant.FORMAT_VALUE);
        map.put(Constant.TYPE, Constant.TYPE_VALUE);
        map.put(Constant.METHOD,"pm.ppt.communityAddress.list");//方法名称

        map.put("communityId",communityId);
        map.put("building","");
        map.put("unit","");
        map.put("room","");

        String sign = AopUtils.sign(map, Constant.SECRET_VALUE);
        map.put("sign",sign);
        postRequest(Constant.RAW_URL, mapToRP(map), 10);
    }

    /**
     * 获取社区单元号 列表
     * @param communityId 社区ID
     * @param building 楼号
     * */
    public void requestUnitList(String communityId,String building){
        Map<String,String> map = new HashMap<String,String>();
        map.put(Constant.APPKEY,Constant.APPKEY_VALUE);
        map.put(Constant.SECRET,Constant.SECRET_VALUE);
        map.put(Constant.VERSION,Constant.VERSION_VALUE);
        map.put(Constant.FORMAT,Constant.FORMAT_VALUE);
        map.put(Constant.TYPE, Constant.TYPE_VALUE);
        map.put(Constant.METHOD,"pm.ppt.communityAddress.list");//方法名称

        map.put("communityId",communityId);
        map.put("building",building);
        map.put("unit","");
        map.put("room","");

        String sign = AopUtils.sign(map, Constant.SECRET_VALUE);
        map.put("sign",sign);
        postRequest(Constant.RAW_URL, mapToRP(map), 11);
    }

    /**
     * 获取社区房号 列表
     * @param communityId 社区ID
     * @param building 楼号
     * @param unit 单元号
     * */
    public void requestRoomList(String communityId,String building,String unit){
        Map<String,String> map = new HashMap<String,String>();
        map.put(Constant.APPKEY,Constant.APPKEY_VALUE);
        map.put(Constant.SECRET,Constant.SECRET_VALUE);
        map.put(Constant.VERSION,Constant.VERSION_VALUE);
        map.put(Constant.FORMAT,Constant.FORMAT_VALUE);
        map.put(Constant.TYPE, Constant.TYPE_VALUE);
        map.put(Constant.METHOD,"pm.ppt.communityAddress.list");//方法名称

        map.put("communityId",communityId);
        map.put("building",building);
        map.put("unit",unit);
        map.put("room","");

        String sign = AopUtils.sign(map, Constant.SECRET_VALUE);
        map.put("sign",sign);
        postRequest(Constant.RAW_URL, mapToRP(map), 12);
    }



    @Override
    public void onRequestSuccess(JsonNode result, int requestCode) throws IOException {
        if(requestCode == 0){
            Log.d("demo","dao中结果集(访问列表): " + result);
            List<Visit> list= new ArrayList<>();
            list = JsonUtil.node2pojoList(result.findValue("communityVisitList"), Visit.class);
           if(null!=list) {
               listVisit.addAll(list);
           }
        }

        if(requestCode == 1){
            Log.d("demo","dao中结果集(访问详情): " + result);
            visitDetail = JsonUtil.node2pojo(result.findValue("QrImgInfo"), Visit.class);
        }

        if(requestCode == 2){
            Log.d("demo","dao中结果集(添加访问): " + result);
        }

        if(requestCode == 3){
            Log.d("demo","dao中结果集(处理访问): " + result);
        }


        if(requestCode == 10){
            Log.d("demo","dao中结果集(楼号列表): " + result);
            //memberInfo = JsonUtil.node2pojo(result.findValue("memberInfo"), MemberInfo.class);
            listBuilding = JsonUtil.node2pojoList(result.findValue("buildList"), String.class);
            //listUnit = JsonUtil.node2pojoList(result.findValue("unitList"), String.class);
            //listRoom = JsonUtil.node2pojoList(result.findValue("roomList"), String.class);
        }

        if(requestCode == 11){
            Log.d("demo","dao中结果集(单元列表): " + result);
            //memberInfo = JsonUtil.node2pojo(result.findValue("memberInfo"), MemberInfo.class);
            //listBuilding = JsonUtil.node2pojoList(result.findValue("buildList"), String.class);
            listUnit = JsonUtil.node2pojoList(result.findValue("unitList"), String.class);
            //listRoom = JsonUtil.node2pojoList(result.findValue("roomList"), String.class);
        }

        if(requestCode == 12){
            Log.d("demo","dao中结果集(房号列表): " + result);
            //memberInfo = JsonUtil.node2pojo(result.findValue("memberInfo"), MemberInfo.class);
            //listBuilding = JsonUtil.node2pojoList(result.findValue("buildList"), String.class);
            //listUnit = JsonUtil.node2pojoList(result.findValue("unitList"), String.class);
            listRoom = JsonUtil.node2pojoList(result.findValue("roomList"), String.class);
        }




    }

    public List<Visit> getListVisit() {
        return listVisit;
    }
    public List<Img> getListImg() {
        return listImg;
    }

    public Visit getVisitDetail() {
        return visitDetail;
    }

    public List<String> getListBuilding() {
        return listBuilding;
    }

    public List<String> getListUnit() {
        return listUnit;
    }

    public List<String> getListRoom() {
        return listRoom;
    }
}
