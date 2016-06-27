package com.ctrl.forum.dao;

import android.util.Log;

import com.beanu.arad.http.IDao;
import com.beanu.arad.http.INetResult;
import com.beanu.arad.utils.JsonUtil;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.entity.CProduct;
import com.ctrl.forum.entity.CProductCategory;
import com.ctrl.forum.entity.CompanyOrder;
import com.ctrl.forum.entity.CouponsPackag;
import com.ctrl.forum.entity.MemberInfo;
import com.ctrl.forum.entity.MemeberOrder;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单
 * Created by Administrator on 2016/5/16.
 */
public class MineStoreDao extends IDao {
    private List<MemeberOrder> memeberOrders = new ArrayList<>();
    private List<CompanyOrder> companyOrders = new ArrayList<>();
    private List<CProductCategory> productCategories = new ArrayList<>();
    private List<CProduct> products = new ArrayList<>();
    private MemberInfo memberInfo = new MemberInfo();
    private List<CouponsPackag> couponsPackags = new ArrayList<>(); //现金劵包

    public MineStoreDao(INetResult activity) {
        super(activity);
    }

    /**
     * 获取会员订单列表接口
     * @param memberId  会员id
     */
    public void getMemeberOrder(String memberId,String pageNum,String pageSize){
       String url = "member/getMemeberOrderList";
        Map<String,String> map = new HashMap<>();
        map.put("memberId",memberId);
        map.put("pageNum",pageNum);
        map.put("pageSize",pageSize);
        postRequest(Constant.RAW_URL + url, mapToRP(map), 0);
    }

    /**
     * 商家获取订单列表
     * @param companyId 商家id
     * @param type 查询类型//0-查询全部  1-查询待发货 2-查询待收货 3-查询已评价
     * @param pageSize 每页几条
     * @param pageNum 第几页
     */
    public void companyOrderList(String companyId,String type,String pageSize,String pageNum){
        String url = "order/companyOrderList";
        Map<String,String> map = new HashMap<>();
        map.put("companyId",companyId);
        map.put("type",type);
        map.put("pageSize",pageSize);
        map.put("pageNum",pageNum);
        postRequest(Constant.RAW_URL+url,mapToRP(map),1);
    }

    /**
     * 获取店铺商品分类以及分类下的商品列表接口
     * @param companyId  商家id
    // * @param type 商家管理:不传  用户界面: 传type=1
     */
    public void getProductCategory(String companyId){
        String url = "productCategory/getProductCategory";
        Map<String,String> map = new HashMap<>();
        map.put("companyId",companyId);
        //map.put("type",type);
        postRequest(Constant.RAW_URL + url, mapToRP(map), 2);
    }

    /**
     * 用户商品上下架
     * @param id 商品id
     * @param isAdded 是否上下架（0：下架、1：上架）
     */
    public void UpdateProduct(String id,String isAdded){
        String url = "product/updateProduct";
        Map<String,String> map = new HashMap<>();
        map.put("id",id);
        map.put("isAdded",isAdded);
        postRequest(Constant.RAW_URL+url,mapToRP(map),3);
    }

    /**
     * 删除订单
     * @param id 订单id
     */
    public void deleteOrder(String id){
        String url = "order/deleteOrder";
        Map<String,String> map = new HashMap<>();
        map.put("id",id);
        postRequest(Constant.RAW_URL+url,mapToRP(map),4);
    }

    /**
     * 根据手机号获取用户名称
     * @param mobile 手机号
     */
    public void getMemberName(String mobile){
        String url = "member/getMemberName";
        Map<String,String> map = new HashMap<>();
        map.put("mobile",mobile);
        postRequest(Constant.RAW_URL+url,mapToRP(map),5);
    }

    /**
     * 根据商家id查询现金包
     * @param companyId 商家id
     */
    public void queryCouponsPackageByCompanyId(String companyId){
        String url = "companyCouponsPackage/queryCouponsPackageByCompanyId";
        Map<String,String> map = new HashMap<>();
        map.put("companyId",companyId);
        postRequest(Constant.RAW_URL+url,mapToRP(map),6);
    }

    /**
     * 商家派送现金券
     * @param companyId 店铺id
     * @param packageId 现金包id
     * @param mobile 会员手机号码
     */
    public void sendGiftVoucher(String companyId,String packageId,String mobile){
        String url = "companys/sendGiftVoucher";
        Map<String,String> map = new HashMap<>();
        map.put("companyId",companyId);
        map.put("packageId",packageId);
        map.put("mobile",mobile);
        postRequest(Constant.RAW_URL+url,mapToRP(map),7);
    }

    /**
     * 商家发货
     * @param id 订单主键id
     * @param memberId 会员id
     * @param orderNum 订单编号
     */
    public void deliverGoods(String id,String memberId,String orderNum){
        String url = "order/deliverGoods";
        Map<String,String> map = new HashMap<>();
        map.put("id",id);
        map.put("memberId",memberId);
        map.put("orderNum",orderNum);
        postRequest(Constant.RAW_URL+url,mapToRP(map),8);
    }

    @Override
    public void onRequestSuccess(JsonNode result, int requestCode) throws IOException {
      if (requestCode==0){
          Log.d("demo", "dao中结果集(获取我的订单列表): " + result);
          List<MemeberOrder> list = JsonUtil.node2pojoList(result.findValue("list"), MemeberOrder.class);
          memeberOrders.addAll(list);
      }
        if (requestCode==1){
            Log.d("demo", "dao中结果集(商家获取订单列表): " + result);
            List<CompanyOrder> list = JsonUtil.node2pojoList(result.findValue("orderList"), CompanyOrder.class);
            companyOrders.addAll(list);
        }
        if (requestCode==2){

            Log.d("demo", "dao中结果集(获取店铺商品分类以及分类下的商品): " + result);
            productCategories = JsonUtil.node2pojoList(result.findValue("productCategoryList"), CProductCategory.class);
            products = JsonUtil.node2pojoList(result.findValue("productList"),CProduct.class);
            Log.e("products============", productCategories.get(0).getcProducts().toString());

        }
        if (requestCode==3){
            Log.d("demo", "dao中结果集(用户商品上下架): " + result);
        }
        if (requestCode==5){
            Log.d("demo", "dao中结果集(手机获取用户名): " + result);
            memberInfo = JsonUtil.node2pojo(result.findValue("memberInfo"), MemberInfo.class);
        }
        if (requestCode==6){
            Log.d("demo", "dao中结果集(根据商家id查询现金包): " + result);
            couponsPackags = JsonUtil.node2pojoList(result.findValue("couponsPackageList"), CouponsPackag.class);
        }
    }

    public List<MemeberOrder> getMemeberOrders() {
        return memeberOrders;
    }
    public List<CompanyOrder> getCompanyOrders() {
        return companyOrders;
    }
    public List<CProductCategory> getProductCategories() {
        return productCategories;
    }
    public MemberInfo getMemberInfo() {
        return memberInfo;
    }
    public List<CouponsPackag> getCouponsPackags() {
        return couponsPackags;
    }
}
