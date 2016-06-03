package com.ctrl.forum.dao;

import android.util.Log;

import com.beanu.arad.http.IDao;
import com.beanu.arad.http.INetResult;
import com.beanu.arad.utils.JsonUtil;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.entity.Address;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 地址 dao
 * Created by jason on 2016/4/19.
 */
public class AddressDao extends IDao {




    private List<Address> listAddress = new ArrayList<>();//收货地址列表

    public AddressDao(INetResult activity){
        super(activity);
    }

    /**
     * 获取收货地址列表接口
     * @param  memberId  //用户id
     * */
    public void requestGetAddressList(String memberId){
        String url="receiveAddress/getAddressList";
        Map<String,String> map = new HashMap<String,String>();
        map.put("memberId",memberId);
        postRequest(Constant.RAW_URL + url, mapToRP(map), 0);
    }
    /**
     * 添加收货地址
     * @param addressDetail //详细地址
     * @param MemberId //用户id
     * @param mobile //手机号
     * @param province //省
     * @param city //市
     * @param area //地区
     * @param latitude //纬度
     * @param longitude //经度
     * @param receiveName //收货人
     * @param addressBase //收货基本地址
     *
     * */
    public void requestAddReceiveAddress(String addressDetail,
                                         String MemberId,
                                         String mobile,
                                         String province,
                                         String city,
                                         String area,
                                         String latitude,
                                         String longitude,
                                         String receiveName,
                                         String addressBase){
        String url="receiveAddress/addReceiveAddress";
        Map<String,String> map = new HashMap<String,String>();
        map.put("addressDetail",addressDetail);
        map.put("MemberId",MemberId);
        map.put("mobile",mobile);
        map.put("province",province);
        map.put("city",city);
        map.put("area",area);
        map.put("latitude",latitude);
        map.put("longitude",longitude);
        map.put("receiveName",receiveName);
        map.put("addressBase",addressBase);
        postRequest(Constant.RAW_URL + url, mapToRP(map), 1);
    }

    /**
     * 更改收货地址
     * @param addressDetail //详细地址
     * @param id //收货地址id
     * @param mobile //手机号
     * @param province //省
     * @param city //市
     * @param area //地区
     * @param latitude //纬度
     * @param longitude //经度
     * @param receiveName //收货人
     * @param addressBase //收货基本地址
     *
     * */
    public void requesUpdateReceiveAddress(String addressDetail,
                                         String id,
                                         String mobile,
                                         String province,
                                         String city,
                                         String area,
                                         String latitude,
                                         String longitude,
                                         String receiveName,
                                         String addressBase){
        String url="receiveAddress/updateReceiveAddress";
        Map<String,String> map = new HashMap<String,String>();
        map.put("addressDetail",addressDetail);
        map.put("id",id);
        map.put("mobile",mobile);
        map.put("province",province);
        map.put("city",city);
        map.put("area",area);
        map.put("latitude",latitude);
        map.put("longitude",longitude);
        map.put("receiveName",receiveName);
        map.put("addressBase",addressBase);
        postRequest(Constant.RAW_URL + url, mapToRP(map),2);
    }

    /**
     * 删除收货地址接口
     * @param  id  //收货地址id
     * */
    public void requestDelteReceiveAddress(String id){
        String url="receiveAddress/deleteReceiveAddress";
        Map<String,String> map = new HashMap<String,String>();
        map.put("id",id);
        postRequest(Constant.RAW_URL + url, mapToRP(map),3);
    }
    /**
     * 设置默认收货地址接口
     * @param  isDefault  //是否是默认id（0不是 1是）
     * @param  id  //收货地址id
     * @param  memberId  //用户id
     * */
    public void requestUpdateDefaultReceiveAddress(String isDefault,String id,String memberId){
        String url="receiveAddress/updateDefaultReceiveAddress";
        Map<String,String> map = new HashMap<String,String>();
        map.put("isDefault",isDefault);
        map.put("id",id);
        map.put("memberId",memberId);
        postRequest(Constant.RAW_URL + url, mapToRP(map),4);
    }





    @Override
    public void onRequestSuccess(JsonNode result, int requestCode) throws IOException {
        if(requestCode == 0){
            Log.d("demo", "dao中结果集(收货地址列表返回): " + result);
            listAddress = JsonUtil.node2pojoList(result.findValue("addressList"), Address.class);
        }
        if(requestCode == 1){
            Log.d("demo", "dao中结果集(商城首页商家推荐返回): " + result);
        }

    }

    public List<Address> getListAddress() {
        return listAddress;
    }
}
