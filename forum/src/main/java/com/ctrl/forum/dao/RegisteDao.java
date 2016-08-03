package com.ctrl.forum.dao;

import android.util.Log;

import com.beanu.arad.http.IDao;
import com.beanu.arad.http.INetResult;
import com.ctrl.forum.base.Constant;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 注册 dao
 * Created by jason on 2016/4/19.
 */
public class RegisteDao extends IDao {

    private String code;

    public RegisteDao(INetResult activity){
        super(activity);
    }

    /**
     * 注册 接口
     * @param userName 用户名
     * @param password 密码
     * @param registIp 注册ip
     * */
    public void requestRegiste(String userName,String password,String registIp){
        String url="member/regist";
        Map<String,String> map = new HashMap<String,String>();
        map.put("userName", userName);
        map.put("password", password);
        map.put("registIp", registIp);
        postRequest(Constant.RAW_URL+url, mapToRP(map), 0);
    }
    /**
     * 短信验证码接口
     * @param mobile 用户名/手机号
     * */
    public void requestAuthCode(String mobile){
        String url="member/obtainMsgCode";
        Map<String,String> map = new HashMap<String,String>();
        map.put("mobile", mobile);
        postRequest(Constant.RAW_URL+url,mapToRP(map),1);
    }

    @Override
    public void onRequestSuccess(JsonNode result, int requestCode) throws IOException {
        if(requestCode == 0){
            Log.d("demo","dao中结果集(注册): " + result);
        }
        if(requestCode == 1){
           code = result.findValue("authCode").asText();
            Log.d("demo","dao中结果集(注册): " + result);
        }

    }

    @Override
    public void onRequestFails(String result, int requestCode, String errorNo) {

    }

    public String getCode(){return code;}
}
