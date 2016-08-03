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
 * 上传语音 dao
 * Created by jason on 2015/10/28.
 */
public class SoundDao extends IDao {


    private String soundUrl;

    public SoundDao(INetResult activity){
        super(activity);
    }

    /**
     * 上传图片接口
     * @param soundData 语音的数据（Basae64编码后的字符串）
     *
     * */
    public void requestSoundUpload(String soundData){
        String url="soundUpload/upload";
        Map<String,String> map = new HashMap<String,String>();
        map.put("soundData",soundData);
        postRequest(Constant.RAW_URL+url, mapToRP(map),889);
    }


    @Override
    public void onRequestSuccess(JsonNode result, int requestCode) throws IOException {
        if(requestCode == 889){
            Log.d("demo","dao中结果集(登录返回): " + result);
           // soundUrl = JsonUtil.node2pojo(result.findValue("memberInfo"), Image.class);
            soundUrl=result.findValue("soundUrl").asText();

        }




    }

    @Override
    public void onRequestFails(String result, int requestCode, String errorNo) {

    }

    public String getSoundUrl() {
        return soundUrl;
    }


}
