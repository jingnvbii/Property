package com.ctrl.android.yinfeng.dao;

import android.util.Log;

import com.beanu.arad.http.IDao;
import com.beanu.arad.http.INetResult;
import com.beanu.arad.utils.JsonUtil;
import com.ctrl.android.yinfeng.base.Constant;
import com.ctrl.android.yinfeng.entity.Hotline;
import com.ctrl.android.yinfeng.entity.Hotline2;
import com.ctrl.android.yinfeng.entity.Hotline3;
import com.ctrl.android.yinfeng.http.AopUtils;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 联系人列表 dao
 * Created by Eric on 2015/10/28.
 */
public class ContactDao extends IDao {

    private List<Hotline> listContactGroup;
    private List<Hotline2> listContactGroup2;
    private List<Hotline3> listContactGroup3;


    public ContactDao(INetResult activity){
        super(activity);
    }

    /**
     * 通讯录员工分组列表
     * @param categoryId 分组id
     * @param keyword 关键字
     * */
    public void requestContactGroupList(String categoryId,String keyword){
        Map<String,String> map = new HashMap<String,String>();
        map.put(Constant.APPKEY,Constant.APPKEY_VALUE);
        map.put(Constant.SECRET,Constant.SECRET_VALUE);
        map.put(Constant.VERSION,Constant.VERSION_VALUE);
        map.put(Constant.FORMAT,Constant.FORMAT_VALUE);
        map.put(Constant.TYPE, Constant.TYPE_VALUE);
        map.put(Constant.METHOD,"pm.stf.contactsCategory.list");//方法名称
        map.put("categoryId",categoryId);
        map.put("keyword",keyword);
        String sign = AopUtils.sign(map, Constant.SECRET_VALUE);
        map.put("sign",sign);
        postRequest(Constant.RAW_URL, mapToRP(map), 0);
    }
    /**
     * 通讯录员工分组列表
     * @param categoryId 分组id
     * @param keyword 关键字
     * */
    public void requestContact2GroupList(String categoryId,String keyword){
        Map<String,String> map = new HashMap<String,String>();
        map.put(Constant.APPKEY,Constant.APPKEY_VALUE);
        map.put(Constant.SECRET,Constant.SECRET_VALUE);
        map.put(Constant.VERSION,Constant.VERSION_VALUE);
        map.put(Constant.FORMAT,Constant.FORMAT_VALUE);
        map.put(Constant.TYPE, Constant.TYPE_VALUE);
        map.put(Constant.METHOD,"pm.stf.contactsCategory.list");//方法名称
        map.put("categoryId",categoryId);
        map.put("keyword",keyword);
        String sign = AopUtils.sign(map, Constant.SECRET_VALUE);
        map.put("sign",sign);
        postRequest(Constant.RAW_URL, mapToRP(map), 1);
    }
    /**
     * 通讯录员工分组列表
     * @param categoryId 分组id
     * @param keyword 关键字
     * */
    public void requestContact3GroupList(String categoryId,String keyword){
        Map<String,String> map = new HashMap<String,String>();
        map.put(Constant.APPKEY,Constant.APPKEY_VALUE);
        map.put(Constant.SECRET,Constant.SECRET_VALUE);
        map.put(Constant.VERSION,Constant.VERSION_VALUE);
        map.put(Constant.FORMAT,Constant.FORMAT_VALUE);
        map.put(Constant.TYPE, Constant.TYPE_VALUE);
        map.put(Constant.METHOD,"pm.stf.contactsCategory.list");//方法名称
        map.put("categoryId",categoryId);
        map.put("keyword",keyword);
        String sign = AopUtils.sign(map, Constant.SECRET_VALUE);
        map.put("sign",sign);
        postRequest(Constant.RAW_URL, mapToRP(map),2);
    }

 /*   public void requestContactorList(String communityId,String groupId,String keyword){
        Map<String,String> map = new HashMap<String,String>();
        map.put(Constant.APPKEY,Constant.APPKEY_VALUE);
        map.put(Constant.SECRET,Constant.SECRET_VALUE);
        map.put(Constant.VERSION,Constant.VERSION_VALUE);
        map.put(Constant.FORMAT,Constant.FORMAT_VALUE);
        map.put(Constant.TYPE, Constant.TYPE_VALUE);
        map.put(Constant.METHOD,"pm.stf.staff.list");//方法名称

        map.put("communityId",communityId);
        map.put("groupId",groupId);
        map.put("keyword",keyword);

        String sign = AopUtils.sign(map, Constant.SECRET_VALUE);
        map.put("sign",sign);
        postRequest(Constant.RAW_URL, mapToRP(map), 1);
    }*/

    @Override
    public void onRequestSuccess(JsonNode result, int requestCode) throws IOException {
        if(requestCode == 0){
            Log.d("demo","dao中结果集(联系人分组): " + result);
            listContactGroup = JsonUtil.node2pojoList(result.findValue("contactsCategoryList"), Hotline.class);
        }
        if(requestCode ==1){
            Log.d("demo","dao中结果集(联系人分组): " + result);
            listContactGroup2 = JsonUtil.node2pojoList(result.findValue("contactsCategoryList"), Hotline2.class);
        }
        if(requestCode ==2){
            Log.d("demo","dao中结果集(联系人分组): " + result);
            listContactGroup3 = JsonUtil.node2pojoList(result.findValue("contactsCategoryList"), Hotline3.class);
        }

       /* if(requestCode == 1){
            Log.d("demo","dao中结果集(联系人列表): " + result);
            listContactor = JsonUtil.node2pojoList(result.findValue("staffList"), Contactor.class);
        }
*/


    }

    public List<Hotline> getListContactGroup() {
        return listContactGroup;
    }
    public List<Hotline2> getListContactGroup2() {
        return listContactGroup2;
    }
    public List<Hotline3> getListContactGroup3() {
        return listContactGroup3;
    }
    }

   /* public List<Contactor> getListContactor() {
        return listContactor;
    }*/
