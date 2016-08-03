package com.ctrl.forum.dao;

import android.util.Log;

import com.beanu.arad.http.IDao;
import com.beanu.arad.http.INetResult;
import com.beanu.arad.utils.JsonUtil;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.entity.Kind;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分类共通接口 dao
 * Created by jason on 2015/10/28.
 */
public class KindDao extends IDao {

    private List<Kind> listKind=new ArrayList<>();//分类列表


    public KindDao(INetResult activity){
        super(activity);
    }

    /**
     * 获取共通分类列表（或者下拉列表）用户获取店铺分类以及类似的下拉列表
     * @param kindKey 类别KEY（参照【烟台项目-系统参数文档.xls】的【分类表t_kind】sheet页）
     *
     * */
    public void requestObtainKindList(String kindKey){
        String url="kind/obtainKindList";
        Map<String,String> map = new HashMap<String,String>();
        map.put("kindKey",kindKey);
        postRequest(Constant.RAW_URL + url, mapToRP(map), 777);
    }


    @Override
    public void onRequestSuccess(JsonNode result, int requestCode) throws IOException {
        if(requestCode == 777){
            Log.d("demo","dao中结果集(共通分类列表返回): " + result);
            listKind = JsonUtil.node2pojoList(result.findValue("kindList"), Kind.class);
        }




    }

    @Override
    public void onRequestFails(String result, int requestCode, String errorNo) {

    }

    public List<Kind> getListKind() {
        return listKind;
    }
}
