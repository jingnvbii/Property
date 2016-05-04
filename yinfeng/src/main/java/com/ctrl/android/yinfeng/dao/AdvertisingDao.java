package com.ctrl.android.yinfeng.dao;

import android.util.Log;

import com.beanu.arad.http.IDao;
import com.beanu.arad.http.INetResult;
import com.beanu.arad.utils.JsonUtil;
import com.ctrl.android.yinfeng.base.Constant;
import com.ctrl.android.yinfeng.entity.Advertising;
import com.ctrl.android.yinfeng.http.AopUtils;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 图片 dao
 * Created by jason on 2016/1/22
 */
public class AdvertisingDao extends IDao {


    private List<Advertising> advertisingList = new ArrayList<>();


    public AdvertisingDao(INetResult activity){
        super(activity);
    }

    /**
     * 广告位图片 接口
     * @param location 广告位编号（广告位编号请和Web开发人员商定）
     * */
    public void requestAdvertising(String location){
        Log.d("demo", "上传图片 dao 1 ");
        Map<String,String> map = new HashMap<String,String>();
        map.put(Constant.APPKEY,Constant.APPKEY_VALUE);
        map.put(Constant.SECRET,Constant.SECRET_VALUE);
        map.put(Constant.VERSION,Constant.VERSION_VALUE);
        map.put(Constant.FORMAT,Constant.FORMAT_VALUE);
        map.put(Constant.TYPE, Constant.TYPE_VALUE);
        map.put(Constant.METHOD,"pm.ppt.advertisement.list");//方法名称

        map.put("location",location);

        String sign = AopUtils.sign(map, Constant.SECRET_VALUE);
        map.put("sign",sign);
        postRequest(Constant.RAW_URL, mapToRP(map), 666);
    }


    @Override
    public void onRequestSuccess(JsonNode result, int requestCode) throws IOException {

        if(requestCode == 666){
            Log.d("demo","dao中结果集(上传图片): " + result);
            List<Advertising> advertising = JsonUtil.node2pojoList(result.findValue("advertisementList"), Advertising.class);
            advertisingList.addAll(advertising);
        }
    }
    public List<Advertising> getAdvertisingList() {
        return advertisingList;
    }
}
