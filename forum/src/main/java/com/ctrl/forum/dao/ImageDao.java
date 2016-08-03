package com.ctrl.forum.dao;

import android.util.Log;

import com.beanu.arad.http.IDao;
import com.beanu.arad.http.INetResult;
import com.beanu.arad.utils.JsonUtil;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.entity.Image;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 图片处理 dao
 * Created by jason on 2015/10/28.
 */
public class ImageDao extends IDao {

    /**
     * 用户信息
     * */
    private Image image=new Image();


    public ImageDao(INetResult activity){
        super(activity);
    }

    /**
     * 上传图片接口
     * @param imgData 图片数据
     *
     * */
    public void requestUploadImage(String imgData){
        String url="sysImg/uploadImg";
        Map<String,String> map = new HashMap<String,String>();
        map.put("imgData",imgData);
        postRequest(Constant.RAW_URL + url, mapToRP(map), 888);
    }
    /**
     * 删除图片接口
     * @param id 图片id
     *
     * */
    public void requestDeleteImage(String id){
        String url="sysImg/deleteImg";
        Map<String,String> map = new HashMap<String,String>();
        map.put("id",id);
        postRequest(Constant.RAW_URL+url, mapToRP(map),889);
    }


    @Override
    public void onRequestSuccess(JsonNode result, int requestCode) throws IOException {
        if(requestCode == 888){
            Log.d("demo","dao中结果集(登录返回): " + result);
            image = JsonUtil.node2pojo(result.findValue("data"), Image.class);
        }




    }

    @Override
    public void onRequestFails(String result, int requestCode, String errorNo) {

    }

    public Image getImage() {
        return image;
    }


}
