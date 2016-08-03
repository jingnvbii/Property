package com.ctrl.forum.dao;

import android.util.Log;

import com.beanu.arad.http.IDao;
import com.beanu.arad.http.INetResult;
import com.beanu.arad.utils.JsonUtil;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.entity.Count;
import com.ctrl.forum.entity.Coupon;
import com.ctrl.forum.entity.CouponImg;
import com.ctrl.forum.entity.Redenvelope;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的劵
 * Created by Administrator on 2016/5/10.
 */
public class CouponsDao extends IDao {
    private List<Coupon> coupons = new ArrayList<>();  //现金劵
    private List<Redenvelope> redenvelopes= new ArrayList<>();  //优惠劵
    private Count count = new Count();
    private Count count1 = new Count();
    private CouponImg couponImg = new CouponImg();

    public CouponsDao(INetResult activity) {
        super(activity);
    }

    /**
     * 获取我的现金券列表
     * @param type  查询状态 0-查询状态条数  1-查询状态下优惠券
     * @param state  查询状态 0-未使用 1-未使用 2-已过期 在type=1下配合使用
     * @param memberId  用户id
     * @param pageNum  第几页
     * @param pageSize  每页几条
     */
    public void getMemberCoupons(String type,String state,String memberId,String pageNum,String pageSize){
        String url="member/getMemberCoupons";
        Map<String,String> map = new HashMap<>();
        map.put("type",type);
        map.put("state",state);
        map.put("memberId",memberId);
        map.put("pageNum",pageNum);
        map.put("pageSize",pageSize);
        postRequest(Constant.RAW_URL + url, mapToRP(map), 0);
    }

    /**
     * 获取我的优惠券列表
     * @param type  查询状态 0-查询状态条数  1-查询状态下优惠券
     * @param state  查询状态 0-未使用 1-未使用 2-已过期
     * @param memberId  用户id
     * @param pageNum  第几页
     * @param pageSize  每页几条
     */
    public void getMemberRedenvelope(String type,String state,String memberId,String pageNum,String pageSize){
        String url="member/getMemberRedenvelope";
        Map<String,String> map = new HashMap<>();
        map.put("type",type);
        map.put("state",state);
        map.put("memberId",memberId);
        map.put("pageNum",pageNum);
        map.put("pageSize",pageSize);
        postRequest(Constant.RAW_URL + url, mapToRP(map), 1);
    }


    /**
     * 用户使用现金券
     * @param id  现金券id
     */
    public void useCashCoupons(String id){
        String url="memberCoupons/useCashCoupons";
        Map<String,String> map = new HashMap<>();
        map.put("id",id);
        postRequest(Constant.RAW_URL + url, mapToRP(map), 2);
    }

    /**
     * 获取优惠券图片
     * @param imgKey  现金券id
     */
    public void queryCouponImg(String imgKey){
        String url="otherImg/queryCouponImg";
        Map<String,String> map = new HashMap<>();
        map.put("imgKey",imgKey);
        postRequest(Constant.RAW_URL + url, mapToRP(map), 3);
    }

    @Override
    public void onRequestSuccess(JsonNode result, int requestCode) throws IOException {
     if (requestCode==0){
         Log.d("demo", "dao中结果集(获取现金劵): " + result);
         coupons = JsonUtil.node2pojoList(result.findValue("memberCouponsList"), Coupon.class);
         count = JsonUtil.node2pojo(result.findValue("count"), Count.class);//获取总数
     }
        if (requestCode==1){
            Log.d("demo", "dao中结果集(获取优惠劵): " + result);
            redenvelopes = JsonUtil.node2pojoList(result.findValue("memberCouponsList"), Redenvelope.class);
            count1 = JsonUtil.node2pojo(result.findValue("count"), Count.class);//获取总数
        }
        if (requestCode==3){
           couponImg = JsonUtil.node2pojo(result.findValue("couponImg"), CouponImg.class);
        }
    }

    @Override
    public void onRequestFails(String result, int requestCode, String errorNo) {

    }

    public List<Coupon> getCoupons() {
        return coupons;
    }

    public List<Redenvelope> getRedenvelopes() {
        return redenvelopes;
    }

    public Count getCount() {
        return count;
    }

    public Count getCount1() {
        return count1;
    }

    public CouponImg getCouponImg() {
        return couponImg;
    }
}
