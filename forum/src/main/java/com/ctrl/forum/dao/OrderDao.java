package com.ctrl.forum.dao;

import android.util.Log;

import com.beanu.arad.http.IDao;
import com.beanu.arad.http.INetResult;
import com.beanu.arad.utils.JsonUtil;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.entity.Address;
import com.ctrl.forum.entity.OrderEvaluation;
import com.ctrl.forum.entity.OrderItem;
import com.ctrl.forum.entity.OrderState;
import com.ctrl.forum.entity.Product;
import com.ctrl.forum.entity.Redenvelope;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单 dao
 * Created by jason on 2015/10/28.
 */
public class OrderDao extends IDao {

    /*
     * 订单评价列表
     * */
    private List<OrderEvaluation>listOrderEvaluation=new ArrayList<>();
    private List<OrderItem>listOrderItem=new ArrayList<>();//订单商品列表
    private List<OrderState>listOrderState=new ArrayList<>();//订单状态列表
    private List<Product>listProduct=new ArrayList<>();//商品列表
    private List<Redenvelope>listRedenvelope=new ArrayList<>();//我的优惠券列表
    private List<Address>listAddress=new ArrayList<>();//收货地址列表
    private String productsTotal;//商品总价
    private String orderId = "";//订单id
    private String orderNum = "";//订单总价
    private OrderState orderState = new OrderState();


    public OrderDao(INetResult activity){
        super(activity);
    }

    /**
     * 生成订单接口
     * @param memberId 会员id
     * @param companyId 商家id
     * @param productStr 商品信息,格式[{"id":1,"nums":1,"amounts":1},{"id":2,"nums":2,"amounts":1}]
     * @param couponId 已使用优惠券id
     * @param couponMoney 优惠券抵扣金额
     * @param receiverName 收货人姓名
     * @param receiverMobile 收货人电话
     * @param provinceName 所在省（名称）
     * @param cityName 市（名称）
     * @param areaName 区（名称）
     * @param address 详细地址
     * @param sourceType 订单来源（0：Web、1：Android、2：IOS、3：电话下单、4：其他）
     * @param remark 备注说明
     *
     * */
    public void requestGenetateOrder(String memberId,
                                     String companyId,
                                     String productStr,
                                     String couponId,
                                     String couponMoney,
                                     String receiverName,
                                     String receiverMobile,
                                     String provinceName,
                                     String cityName,
                                     String areaName,
                                     String address,
                                     String sourceType,
                                     String remark,
                                     Double totalCost){
        String url="order/generateOrder";
        Map<String,String> map = new HashMap<String,String>();
        map.put("memberId",memberId);
        map.put("companyId",companyId);
        map.put("productStr",productStr);
        map.put("couponId",couponId);
        map.put("couponMoney",couponMoney);
        map.put("receiverName",receiverName);
        map.put("receiverMobile",receiverMobile);
        map.put("provinceName",provinceName);
        map.put("cityName",cityName);
        map.put("areaName",areaName);
        map.put("address",address);
        map.put("sourceType",sourceType);
        map.put("remark",remark);
        map.put("totalCost",totalCost+"");
        postRequest(Constant.RAW_URL+url, mapToRP(map),0001);
    }
    /**
     * 删除订单接口
     * @param id 订单id
     * */
    public void requestDeleteOrder(String id){
        String url="order/deleteOrder";
        Map<String,String> map = new HashMap<String,String>();
        map.put("id",id);
        postRequest(Constant.RAW_URL+url, mapToRP(map),1);
    }
    /**
     * 用户签收订单接口
     * @param id 订单id
     *
     * */
    public void requestSignOrder(String id){
        String url="order/signOrder";
        Map<String,String> map = new HashMap<String,String>();
        map.put("id",id);
        postRequest(Constant.RAW_URL+url, mapToRP(map),2);
    }

    /**
     * 用户取消订单接口
     * @param id 订单id
     *
     * */
    public void requestCancelOrder(String id){
        String url="order/cancelOrder";
        Map<String,String> map = new HashMap<String,String>();
        map.put("id",id);
        postRequest(Constant.RAW_URL+url, mapToRP(map),3);
    }
    /**
     * 商家获取订单列表接口
     * @param companyId 商家id
     * @param type 查询类型//0-查询全部  1-查询待发货 2-查询待收货 3-查询已评价
     * @param pageSize 每页几条
     * @param pageNum 第几页
     *
     * */
    public void requestCompanyOrderList(String companyId,String type,String pageSize,String pageNum){
        String url="order/companyOrderList";
        Map<String,String> map = new HashMap<String,String>();
        map.put("companyId",companyId);
        map.put("type",type);
        map.put("pageSize",pageSize);
        map.put("pageNum",pageNum);
        postRequest(Constant.RAW_URL+url, mapToRP(map),4);
    }
    /**
     * 获取待评价或已评价列表接口
     * @param memberId 会员id
     * @param evaluationState 评价状态（0：未评价、1：已评价）
     * */
    public void requesQueryOrderEvaluationList(String memberId,String evaluationState){
        String url="order/queryOrderEvaluationList";
        Map<String,String> map = new HashMap<String,String>();
        map.put("memberId",memberId);
        map.put("evaluationState",evaluationState);
        postRequest(Constant.RAW_URL + url, mapToRP(map), 5);
    }

    /**
     * 获取订单详情接口
     * @param id 订单id
     * */
    public void requesOrderDetail(String id){
        String url="order/getOrderState";
        Map<String,String> map = new HashMap<String,String>();
        map.put("id",id);
        postRequest(Constant.RAW_URL+url, mapToRP(map),66);
    }


    /**
     * 评价订单接口
     * @param memberId 用户id
     * @param orderId 订单id
     * @param level 评价星级
     * @param content 评价内容
     * @param companyId 店铺id
     * */
    public void requestEvalutionOrder(String memberId,
                                     String orderId,
                                     String level,
                                     String content,
                                     String companyId){
        String url="tOrderEvaluation/evaluationOrder";
        Map<String,String> map = new HashMap<String,String>();
        map.put("memberId",memberId);
        map.put("orderId",orderId);
        map.put("level",level);
        map.put("content",content);
        map.put("companyId",companyId);
        postRequest(Constant.RAW_URL+url, mapToRP(map),7);
    }

    /**
     * 获取下单前结算信息接口（点击购物车获取下单前结算信息）
     * @param memberId 用户id
     * @param productStrs 商品信息,格式[{"id":1,"nums":1},{"id":2,"nums":2}]
     * @param discountAmount 优惠券金额
     * */
    public void requestOrderDetails(String memberId,
                                     String productStrs,
                                     String discountAmount){
        String url="order/queryOrderDetails";
        Map<String,String> map = new HashMap<String,String>();
        map.put("memberId",memberId);
        map.put("productStrs",productStrs);
        map.put("discountAmount",discountAmount);
        postRequest(Constant.RAW_URL+url, mapToRP(map),8);
    }


    @Override
    public void onRequestSuccess(JsonNode result, int requestCode) throws IOException {
        if(requestCode==0001){
            Log.d("demo","dao中结果集(生成订单返回): " + result);
            if (result.findValue("orderId")!=null && result.findValue("orderNum")!=null) {
                orderId = result.findValue("orderId").asText();
                orderNum = result.findValue("orderNum").asText();
            }
        }
        if(requestCode == 5){
            Log.d("demo","dao中结果集(订单评价列表返回): " + result);
            listOrderEvaluation = JsonUtil.node2pojoList(result.findValue("orderEvaluationList"), OrderEvaluation.class);
        }

        if(requestCode == 66){
            Log.d("demo","dao中结果集(订单详情返回): " + result);
            listOrderItem = JsonUtil.node2pojoList(result.findValue("tOrderItem"), OrderItem.class);
            //listOrderState = JsonUtil.node2pojoList(result.findValue("orderstate"), OrderState.class);
            orderState = JsonUtil.node2pojo(result.findValue("orderstate"), OrderState.class);
        }
        if(requestCode == 8){
            Log.d("demo","dao中结果集(购物车下单前结算信息返回): " + result);
            listProduct = JsonUtil.node2pojoList(result.findValue("productList"), Product.class);
            listRedenvelope = JsonUtil.node2pojoList(result.findValue("myRedenvelopeList"), Redenvelope.class);
            listAddress = JsonUtil.node2pojoList(result.findValue("receiveAddressList"), Address.class);
          productsTotal=result.findValue("productsTotal").asText();
        }

    }


    public List<OrderEvaluation> getListOrderEvaluation() {return listOrderEvaluation;}
    public List<OrderItem> getListOrderItem() {
        return listOrderItem;
    }
    public List<OrderState> getListOrderState() {
        return listOrderState;
    }
    public List<Product> getListProduct() {
        return listProduct;
    }
    public List<Redenvelope> getListRedenvelope() {
        return listRedenvelope;
    }
    public List<Address> getListAddress() {
        return listAddress;
    }
    public String getProductsTotal() {
        return productsTotal;
    }

    public OrderState getOrderState() {
        return orderState;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getOrderNum() {
        return orderNum;
    }
}
