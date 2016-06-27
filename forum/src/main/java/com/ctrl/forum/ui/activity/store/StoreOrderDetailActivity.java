package com.ctrl.forum.ui.activity.store;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.AnimUtil;
import com.beanu.arad.utils.JsonUtil;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.cart.datasave.GoodsBean;
import com.ctrl.forum.cart.datasave.OperateGoodsDataBase;
import com.ctrl.forum.cart.datasave.OperateGoodsDataBaseStatic;
import com.ctrl.forum.dao.AddressDao;
import com.ctrl.forum.dao.CouponsDao;
import com.ctrl.forum.dao.OrderDao;
import com.ctrl.forum.entity.Address;
import com.ctrl.forum.entity.OrderGoods;
import com.ctrl.forum.entity.OrderGoods2;
import com.ctrl.forum.entity.Product;
import com.ctrl.forum.entity.Redenvelope;
import com.ctrl.forum.ui.activity.mine.MineYouJuanActivity;
import com.ctrl.forum.ui.adapter.StoreOrderDetailAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/*
* 商城订单详情 activity
* */

public class StoreOrderDetailActivity extends AppToolBarActivity implements View.OnClickListener {
    @InjectView(R.id.rl_address)//收货地址
            RelativeLayout rl_address;
    @InjectView(R.id.btn_jiesuan)//马上结算
            Button btn_jiesuan;
    @InjectView(R.id.tv_order_name)//收货人姓名和电话
            TextView tv_order_name;
    @InjectView(R.id.tv_order_address)//收货人地址
            TextView tv_order_address;
    @InjectView(R.id.tv_remark)//订单备注
            TextView tv_remark;
    @InjectView(R.id.tv_order_detail_all_price)//金额共计
            TextView tv_order_detail_all_price;
    @InjectView(R.id.lv_order_detail)//商品列表
            ListView lv_order_detail;
    @InjectView(R.id.tv_address_none)//暂无地址
            TextView tv_address_none;
    @InjectView(R.id.rl_order_detail_youhuiquan)//优惠券布局
            RelativeLayout rl_order_detail_youhuiquan;
    @InjectView(R.id.tv_youhuiquaqn_money)//优惠券金额
            TextView tv_youhuiquaqn_money;
    @InjectView(R.id.iv_youhuiquaqn_money)//优惠券图片
            ImageView iv_youhuiquaqn_money;


    private OperateGoodsDataBase mGoodsDataBaseInterface;
    private StoreOrderDetailActivity mcontext;
    public static int SELECTPOSITION = 0;//一级列表下标值
    private ArrayList<GoodsBean> listGoodsBean;
    private float allPrice;
    private StoreOrderDetailAdapter mStoreOrderDetailAdapter;
    private AddressDao adao;
    private List<Address> listAddress;
    private String province;
    private String city;
    private String area;
    private CouponsDao cdao;
    private String companyId;
    private OrderDao odao;
    private String name;
    private String tel;
    private String address;
    private List<Redenvelope> redenvelopes;
    private String productStr;
    private List<Redenvelope> listRedenvelope;
    private List<Product> listProduct;
    private double productsTotal;
    private String orderId;
    private String orderNum;
    private String productStr2;
    private String amounts;
    private String couponId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_order_detail);
        ButterKnife.inject(this);
        mcontext = this;
        // 隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
        initData();
    }

    private void initData() {

        // adao = new AddressDao(this);
       // cdao = new CouponsDao(this);
        odao = new OrderDao(this);
        //adao.requestGetAddressList(Arad.preferences.getString("memberId"));
       // cdao.getMemberRedenvelope("1", "0", Arad.preferences.getString("memberId"), "", "");
        companyId = getIntent().getStringExtra("companyId");
        mGoodsDataBaseInterface = OperateGoodsDataBase.getInstance();
        listGoodsBean = OperateGoodsDataBaseStatic.getSecondGoodsTypeList(mcontext);
      //  allPrice = mGoodsDataBaseInterface.getSecondGoodsPriceAll(mcontext, SELECTPOSITION);
        mStoreOrderDetailAdapter = new StoreOrderDetailAdapter(this);
       // mStoreOrderDetailAdapter.setList(listGoodsBean);
        lv_order_detail.setAdapter(mStoreOrderDetailAdapter);
      //  tv_order_detail_all_price.setText("共￥" + allPrice + "元");

        try {
            List<OrderGoods2> list = new ArrayList<>();
            List<OrderGoods> list2 = new ArrayList<>();
            for (int i = 0; i < listGoodsBean.size(); i++) {
                OrderGoods2 goods = new OrderGoods2();
                OrderGoods goods2 = new OrderGoods();
                if(Integer.parseInt(listGoodsBean.get(i).getGoodsnum())>0) {
                    goods.setId(listGoodsBean.get(i).getGoodsid());
                    goods.setNums(listGoodsBean.get(i).getGoodsnum());
                    goods2.setId(listGoodsBean.get(i).getGoodsid());
                    goods2.setNums(listGoodsBean.get(i).getGoodsnum());
                    goods2.setAmounts((listGoodsBean.get(i).getGoodsprice())*Integer.parseInt(listGoodsBean.get(i).getGoodsnum()) + "");
                    list.add(goods);
                    list2.add(goods2);
                }
            }
            productStr= JsonUtil.pojo2json(list);
            productStr2= JsonUtil.pojo2json(list2);
           odao.requestOrderDetails(Arad.preferences.getString("memberId"), productStr, "");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initView() {
        rl_address.setOnClickListener(this);
        btn_jiesuan.setOnClickListener(this);
        rl_order_detail_youhuiquan.setOnClickListener(this);

    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if(requestCode==8){
            //  MessageUtils.showShortToast(this,"获取下单前信息接口成功");
           listAddress= odao.getListAddress();
            listRedenvelope=odao.getListRedenvelope();
            listProduct=odao.getListProduct();
            productsTotal=Double.parseDouble(odao.getProductsTotal());

            if(listAddress.size()>0) {
                tv_address_none.setVisibility(View.GONE);
            }


            tv_order_address.setText(listAddress.get(0).getAddressBase() + listAddress.get(0).getAddressDetail());
            tv_order_name.setText(listAddress.get(0).getReceiveName() + "     " + listAddress.get(0).getMobile());
            tv_order_detail_all_price.setText("共￥" + productsTotal + "元");
            mStoreOrderDetailAdapter.setList(listProduct);

            if(listRedenvelope!=null&&listRedenvelope.size()>0){
                tv_youhuiquaqn_money.setText(listRedenvelope.get(0).getAmount()+"元优惠券可用");
                couponId=listRedenvelope.get(0).getId();
                amounts=listRedenvelope.get(0).getAmount();
            }else {
               // iv_youhuiquaqn_money.setVisibility(View.GONE);
               // tv_youhuiquaqn_money.setBackgroundColor(Color.WHITE);
             //   tv_youhuiquaqn_money.setTextColor(Color.GRAY);
                tv_youhuiquaqn_money.setText("0元优惠券可用");
              //  tv_youhuiquaqn_money.setText("暂无优惠券可用");
            }

            name = listAddress.get(0).getReceiveName();
            tel = listAddress.get(0).getMobile();
            address = listAddress.get(0).getAddressBase()+listAddress.get(0).getAddressDetail();
            province = listAddress.get(0).getProvince();
            city = listAddress.get(0).getCity();
            area =listAddress.get(0).getArea();

            if(amounts!=null) {
                productsTotal = productsTotal + Double.parseDouble(amounts);
            }



        }

        if (requestCode == 0001) {
            //   MessageUtils.showShortToast(this,"生成订单成功");
            orderId=odao.getOrderId();
            orderNum=odao.getOrderNum();
            Intent intent =new Intent(this,StorePaymentOrderActivity.class);
            intent.putExtra("orderId",orderId);
            intent.putExtra("productsTotal",productsTotal);
            intent.putExtra("orderNum",orderNum);
            intent.putExtra("redenvelope",amounts);
            startActivity(intent);
            AnimUtil.intentSlidIn(this);
            finish();
        }
        if (requestCode == 1) {
          //  MessageUtils.showShortToast(this, "获取优惠券列表成功");
        }
        if (requestCode == 0) {
            listAddress = adao.getListAddress();
            if(listAddress.size()>0) {
                tv_address_none.setVisibility(View.GONE);
            }
            for (int i = 0; i < listAddress.size(); i++) {
                if (listAddress.get(i).getIsDefault().equals("1")) {
                    tv_order_address.setText(listAddress.get(i).getAddressBase() + listAddress.get(i).getAddressDetail());
                    tv_order_name.setText(listAddress.get(i).getReceiveName() + "     " + listAddress.get(i).getMobile());
                }
            }

        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.rl_order_detail_youhuiquan:
                if(listRedenvelope.size()>0) {
                    intent = new Intent(this, MineYouJuanActivity.class);
                    intent.putExtra("amount",productsTotal+listRedenvelope.get(0).getAmount());
                    startActivityForResult(intent, 660);
                    AnimUtil.intentSlidIn(this);
                }
                break;
            case R.id.rl_address:
                intent = new Intent(this, StoreManageAddressActivity.class);
                startActivityForResult(intent, 300);
                AnimUtil.intentSlidIn(this);
                break;
            case R.id.btn_jiesuan:
                   /* odao.requestGenetateOrder(Arad.preferences.getString("memberId"), companyId, productStr, "4", "1", name, tel, province,
                            city, area, address, "1", "",Double.parseDouble(productsTotal));*/
                   odao.requestGenetateOrder(Arad.preferences.getString("memberId"), companyId, productStr2, couponId, amounts, name, tel, province,
                            city, area, address, "1", tv_remark.getText().toString().trim(),productsTotal);

                break;
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 300) {
            name = data.getStringExtra("name");
            tel = data.getStringExtra("tel");
            address = data.getStringExtra("address");
            province = data.getStringExtra("province");
            city = data.getStringExtra("city");
            area = data.getStringExtra("area");
            tv_order_name.setText(name + "     " + tel);
            tv_order_address.setText(address);
        }
        if (resultCode == RESULT_OK && requestCode == 660) {
            amounts=data.getStringExtra("amount");
            couponId=data.getStringExtra("id");
            tv_youhuiquaqn_money.setText(amounts);
            productsTotal = productsTotal + Double.parseDouble(listRedenvelope.get(0).getAmount()) - Double.parseDouble(amounts);
            tv_order_detail_all_price.setText(productsTotal+"");
        }
    }

    @Override
    public String setupToolBarTitle() {
        return "订单详情";
    }

    @Override
    public boolean setupToolBarLeftButton(ImageView leftButton) {
        leftButton.setImageResource(R.mipmap.jiantou_left_white);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        return true;
    }

}
