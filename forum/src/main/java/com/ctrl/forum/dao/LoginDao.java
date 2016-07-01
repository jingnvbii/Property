package com.ctrl.forum.dao;

import android.util.Log;

import com.beanu.arad.http.IDao;
import com.beanu.arad.http.INetResult;
import com.beanu.arad.utils.JsonUtil;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.entity.MemberInfo;
import com.ctrl.forum.entity.NavigationBar;
import com.ctrl.forum.entity.ReceiveAddress;
import com.fasterxml.jackson.databind.JsonNode;

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

    public LoginDao(INetResult activity){
        super(activity);
    }

    /**
     * 登录
     * @param userName 用户名
     * @param password 密码
     * @param lastLoginType （1：Android、2：IOS、3：WEB）
     *
     * */
    public void requestLogin(String userName,String password,String lastLoginType){
        String url="member/login";
        Map<String,String> map = new HashMap<String,String>();
        map.put("userName",userName);
        map.put("password",password);
        map.put("lastLoginType",lastLoginType);
        postRequest(Constant.RAW_URL+url, mapToRP(map), 0);
    }

    /**
     * 登录 修改密码
     * @param memberId 会员ID
     * @param userName 用户名
     * @param oldPassword 原登录密码
     * @param password 登录密码
     * @param obtainType 修改类型（0：登录后修改、1：忘记密码时修改）
     * */
   /* public void requestChangePassword(String memberId,String userName,String oldPassword,
                                      String password,String obtainType){
        String url="member/login";
        Map<String,String> map = new HashMap<String,String>();
        map.put("memberId",memberId);
        map.put("userName",userName);
        map.put("oldPassword",oldPassword);
        map.put("password",password);
        map.put("obtainType",obtainType);
        postRequest(Constant.RAW_URL+url, mapToRP(map), 1);
    }*/

    /**
     * 登录 修改密码
     * @param userName 用户名/手机号
     * @param password 登录密码
     * */
    public void requestChangePassword(String userName, String password){
        String url="member/login";
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
}
