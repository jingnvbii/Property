package com.beanu.arad.http;

import android.util.Log;

import com.beanu.arad.error.AradException;
import com.beanu.arad.utils.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 默认的HTTP 处理,统一处理接口的错误信息
 * 接口每个人的处理方式不一样，表达的方式也不一样。所以这个类最好是复写
 * Created by beanu on 13-11-29.
 */
public class HttpConfig {

//    private String errorKey = "error_code";
    private String errorKey = "code";

    private List<String> errorCodes = new ArrayList<String>();

    public HttpConfig() {
        handleInErrorCode(errorCodes);
    }

    public HttpConfig(String errorKey) {
        this.errorKey = errorKey;
        handleInErrorCode(errorCodes);
    }

    public JsonNode handleResult(String result) throws AradException {

        //Log.d("demo","result: " + result);
        try {
            JsonNode node = JsonUtil.json2node(result);
            //Log.d("demo","node: " + node);
            //String statue = node.get(errorKey).asText();
//            String statue = node.get("succeed").asText();//1.5版
            String statue = node.get("code").asText();//2.0版
            Log.d("demo","statue: " + statue);
            if (statue != null && statue.equals("000")) {
                return node;
            } else {
                //Log.d("demo","异常1 ");
                AradException e = new AradException();
                e.setError_code(statue);
                throw e;
            }
        } catch (JsonProcessingException e1) {
            e1.printStackTrace();
            AradException exception = new AradException(e1.getMessage());
            throw exception;
        } catch (IOException e1) {
            e1.printStackTrace();
            AradException exception = new AradException(e1.getMessage());
            throw exception;
        }
    }

    /**
     * 返回的状态值中 不是错误信息的，需要也去去处理数据的，单独提出来
     */
    public void handleInErrorCode(List<String> errorCodes) {
    }

    public List<String> getErrorCodes() {
        return errorCodes;
    }

    public String getErrorKey() {
        return errorKey;
    }
}
