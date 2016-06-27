package com.ctrl.forum.dao;

import android.util.Log;

import com.beanu.arad.http.IDao;
import com.beanu.arad.http.INetResult;
import com.beanu.arad.utils.JsonUtil;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.entity.ExchaneProduct;
import com.ctrl.forum.entity.IntegralProduct;
import com.ctrl.forum.entity.RedeemHistory;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 积分商品
 * Created by Administrator on 2016/5/9.
 */
public class RemarkDao extends IDao {
    private List<IntegralProduct> integralProducts = new ArrayList<>(); //获取商品积分
    private List<ExchaneProduct> tRedeemHistory= new ArrayList<>();   //兑换商品积分历史记录
    private List<RedeemHistory> redeemHistories= new ArrayList<>(); //积分历史记录
    public RemarkDao(INetResult activity) {
        super(activity);
    }

    /**
     * 获取积分商品
     * @param pageNum  每页多少
     * @param pageSize  第几页
     */
    public void getRemarkGoods(String pageNum,String pageSize){
        String url="product/getIntegralProductList";
        Map<String,String> map = new HashMap<>();
        map.put("pageNum",pageNum);
        map.put("pageSize",pageSize);
        postRequest(Constant.RAW_URL + url, mapToRP(map), 0);
    }

    /**
     * 兑换积分商品
     * @param memberId  用户id
     * @param productId  商品id
     */
    public void convertRemarkGoods(String memberId,String productId){
        String url="MemberIntegral/exchangeIntegralProduct";
        Map<String,String> map = new HashMap<>();
        map.put("memberId",memberId);
        map.put("productId",productId);
        postRequest(Constant.RAW_URL + url, mapToRP(map), 1);
    }

    /**
     * 兑换积分商品历史记录
     * @param memberId  用户id
     * @param pageNum  每页多少
     * @param pageSize 第几页
     */
    public void convertRemarkHistory(String memberId,String pageNum,String pageSize){
        String url="MemberIntegral/exchangeIntegralProductHistory";
        Map<String,String> map = new HashMap<>();
        map.put("memberId",memberId);
        map.put("pageNum",pageNum);
        map.put("pageSize",pageSize);
        postRequest(Constant.RAW_URL + url, mapToRP(map), 2);
    }

    /**
     * 积分历史记录
     * @param memberId  用户id
     * @param pageNum  每页多少
     * @param pageSize 第几页
     */
    public void getPointHistory(String memberId,String pageNum,String pageSize){
        String url="MemberIntegral/pointHistory";
        Map<String,String> map = new HashMap<>();
        map.put("memberId",memberId);
        map.put("pageNum",pageNum);
        map.put("pageSize",pageSize);
        postRequest(Constant.RAW_URL + url, mapToRP(map), 3);
    }

    @Override
    public void onRequestSuccess(JsonNode result, int requestCode) throws IOException {
      if (requestCode==0){
          Log.d("demo","dao中结果集(获取积分商品): " + result);
          List<IntegralProduct> list =  JsonUtil.node2pojoList(result.findValue("integralProductList"),IntegralProduct.class);
          integralProducts.addAll(list);
      }
        if (requestCode==1){
            Log.d("demo","dao中结果集(兑换积分商品): " + result);
        }
        if (requestCode==2){
            Log.d("demo","dao中结果集(兑换积分商品历史记录): " + result);
            List<ExchaneProduct> list =JsonUtil.node2pojoList(result.findValue("tRedeemHistoryList"),ExchaneProduct.class);
            tRedeemHistory.addAll(list);
        }
        if (requestCode==3){
            Log.d("demo","dao中结果集(积分历史记录): " + result);
            List<RedeemHistory> list = JsonUtil.node2pojoList(result.findValue("pointList"),RedeemHistory.class);
            redeemHistories.addAll(list);
        }
    }

    public List<IntegralProduct> getIntegralProducts() {
        return integralProducts;
    }

    public List<ExchaneProduct> gettRedeemHistory() {
        return tRedeemHistory;
    }

    public List<RedeemHistory> getRedeemHistories() {
        return redeemHistories;
    }
}
