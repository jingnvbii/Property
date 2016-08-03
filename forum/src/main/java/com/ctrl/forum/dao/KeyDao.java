package com.ctrl.forum.dao;

import com.beanu.arad.http.IDao;
import com.beanu.arad.http.INetResult;
import com.beanu.arad.utils.JsonUtil;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.entity.ItemValues;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 根据字典KEY获取内容
 * Created by Administrator on 2016/5/26.
 */
public class KeyDao extends IDao {
   private ItemValues itemValues = new ItemValues();

    public KeyDao(INetResult activity) {
        super(activity);
    }

    /**
     * 根据字典KEY获取内容
     * @param itemKey 字典KEY
     */
    public void ueryDictionary(String itemKey){
        String url="dictionary/queryDictionary";
        Map<String,String> map = new HashMap<>();
        map.put("itemKey",itemKey);
        postRequest(Constant.RAW_URL + url, mapToRP(map), 66);
    }

    @Override
    public void onRequestSuccess(JsonNode result, int requestCode) throws IOException {
       if (requestCode==66){
           itemValues = JsonUtil.node2pojo(result.findValue("itemValue"), ItemValues.class);
       }
    }

    @Override
    public void onRequestFails(String result, int requestCode, String errorNo) {

    }

    public ItemValues getItemValues() {
        return itemValues;
    }
}
