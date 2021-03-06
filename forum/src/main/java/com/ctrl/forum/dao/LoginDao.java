package com.ctrl.forum.dao;

import android.util.Log;

import com.beanu.arad.http.IDao;
import com.beanu.arad.http.INetResult;
import com.beanu.arad.utils.JsonUtil;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.entity.Advertising;
import com.ctrl.forum.entity.MemberInfo;
import com.ctrl.forum.entity.NavigationBar;
import com.ctrl.forum.entity.ReceiveAddress;
import com.ctrl.forum.entity.StartAds;
import com.fasterxml.jackson.databind.JsonNode;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录 dao
 * Created by jason on 2015/10/28.
 */
public class LoginDao extends IDao {

    /**
     * 用户信息
     * */
    private MemberInfo memberInfo=new MemberInfo();

    /**
     * 收货地址列表
     * */
    private List<ReceiveAddress> listReceiveAddress = new ArrayList<>();
    private List<NavigationBar> listNavigationBar=new ArrayList<>();
    private List<Advertising> listAdvertising=new ArrayList<>();
    private List<StartAds> listAds=new ArrayList<>();
    private StartAds startAds;
    private String handleDay = "";

    public LoginDao(INetResult activity){
        super(activity);
    }

    /**
     * 登录
     * @param loginType //登录类型 (0是第三方登录 , 1是账号密码登录)
     * @param userName 用户名
     * @param password 密码
     * @param lastLoginType （1：Android、2：IOS、3：WEB）
     * @param deviceImei 设备串码
     * @param openId 第三方的openId
     * @param thirdloginType 第三方类型 (qq,微信,微博)  分别传(qq  wx  wb )
     * */

    public void requestLogin(String loginType,String userName,String password,String deviceImei,String lastLoginType,String openId,String thirdloginType){
        String url="member/login";
        Map<String,String> map = new HashMap<String,String>();
        map.put("loginType",loginType);
        map.put("userName",userName);
        map.put("password",password);
        map.put("deviceImei",deviceImei);
        map.put("lastLoginType",lastLoginType);
        map.put("openId",openId);
        map.put("thirdloginType",thirdloginType);
        postRequest(Constant.RAW_URL+url, mapToRP(map), 0);
    }

    /**
     * 修改/找回登录密码
     * @param userName 用户名/手机号
     * @param password 登录密码
     * */
    public void requestChangePassword(String userName, String password){
        String url="member/modifyPwd";
        Map<String,String> map = new HashMap<>();
        map.put("userName",userName);
        map.put("password",password);
        postRequest(Constant.RAW_URL+url, mapToRP(map), 1);
    }
    /**
     * 获取导航栏
     * */
    public void requestQueryNavigationBar(){
        String url="kind/queryNavigationBar";
        Map<String,String> map = new HashMap<>();
        postRequest(Constant.RAW_URL+url, mapToRP(map), 2);
    }

    @Override
    public void onRequestSuccess(JsonNode result, int requestCode) throws IOException {
        if(requestCode == 0){
            Log.d("demo","dao中结果集(登录返回): " + result);
            memberInfo = JsonUtil.node2pojo(result.findValue("memberInfo"), MemberInfo.class);
            listReceiveAddress = JsonUtil.node2pojoList(result.findValue("receiveAddressList"), ReceiveAddress.class);
            listNavigationBar = JsonUtil.node2pojoList(result.findValue("navigationBar"), NavigationBar.class);
        }
        if(requestCode == 1){
            Log.d("demo","dao中结果集(修改密码): " + result);
            //memberInfo = JsonUtil.node2pojo(result.findValue("memberInfo"), MemberInfo.class);
            //listReceiveAddress = JsonUtil.node2pojoList(result.findValue("receiveAddressList"), ReceiveAddress.class);
        }
        if(requestCode == 2){
            Log.d("demo","dao中结果集(修改密码): " + result);
            listNavigationBar = JsonUtil.node2pojoList(result.findValue("navigationBar"), NavigationBar.class);
            listAdvertising = JsonUtil.node2pojoList(result.findValue("advertisingList"), Advertising.class);
            startAds = JsonUtil.node2pojo(result.findValue("startADS"), StartAds.class);
        }
    }

    @Override
    public void onRequestFails(String result, int requestCode, String errorNo) {
        if(requestCode == 0) {
            if (errorNo.equals("033")) {
                Log.d("demo", "dao中结果集(033设备拉黑): " + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject data = jsonObject.optJSONObject("data");
                    handleDay = data.optString("handleDay");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public MemberInfo getMemberInfo() {
        return memberInfo;
    }

    public List<ReceiveAddress> getListReceiveAddress() {
        return listReceiveAddress;
    }

    public List<NavigationBar> getListNavigationBar() {
        return listNavigationBar;
    }

    public List<Advertising> getListAdvertising() {
        return listAdvertising;
    }

    public List<StartAds> getListAds() {
        return listAds;
    }

    public StartAds getStartAds() {
        return startAds;
    }

    public String getHandleDay() {
        return handleDay;
    }
}
