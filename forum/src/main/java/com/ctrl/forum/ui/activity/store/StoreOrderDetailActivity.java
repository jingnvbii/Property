package com.ctrl.forum.ui.activity.store;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.beanu.arad.utils.MessageUtils;
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
import com.ctrl.forum.ui.adapter.StoreOrderDetailAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/*
* 商城订单详情 activity
* */

public class StoreOrderDetailActivity extends AppToolBarActivity implements View.OnClickListener{
    @InjectView(R.id.rl_address)//收货地址
    RelativeLayout rl_address;
    @InjectView(R.id.btn_jiesuan)//马上结算
    Button btn_jiesuan;
    @InjectView(R.id.tv_order_name)//收货人姓名和电话
    TextView tv_order_name;
    @InjectView(R.id.tv_order_address)//收货人地址
    TextView tv_order_address;
    @InjectView(R.id.tv_order_detail_all_price)//金额共计
    TextView tv_order_detail_all_price;
    @InjectView(R.id.lv_order_detail)//商品列表
    ListView lv_order_detail;



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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_order_detail);
        ButterKnife.inject(this);
        mcontext=this;
        // 隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
        initData();
    }

    private void initData() {
        adao=new AddressDao(this);
        cdao=new CouponsDao(this);
        odao=new OrderDao(this);
        adao.requestGetAddressList(Arad.preferences.getString("memberId"));
        cdao.getMemberRedenvelope("1", "0", Arad.preferences.getString("memberId"), "", "");
        companyId=getIntent().getStringExtra("companyId");
        mGoodsDataBaseInterface = OperateGoodsDataBase.getInstance();
        listGoodsBean= OperateGoodsDataBaseStatic.getSecondGoodsTypeList(mcontext);
        allPrice=mGoodsDataBaseInterface.getSecondGoodsPriceAll(mcontext, SELECTPOSITION);
        mStoreOrderDetailAdapter =new StoreOrderDetailAdapter(this);
        mStoreOrderDetailAdapter.setList(listGoodsBean);
        lv_order_detail.setAdapter(mStoreOrderDetailAdapter);
        tv_order_detail_all_price.setText("共￥"+allPrice+"元");
    }

    private void initView() {
        rl_address.setOnClickListener(this);
        btn_jiesuan.setOnClickListener(this);

    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if(requestCode==0001){
            MessageUtils.showShortToast(this,"生成订单成功");
        }
        if(requestCode==1){
            MessageUtils.showShortToast(this,"获取优惠券列表成功");

        }


        if(requestCode==0){
          listAddress = adao.getListAddress();

         for(int i=0;i<listAddress.size();i++){
             if(listAddress.get(i).getIsDefault().equals("1")){
                 tv_order_address.setText(listAddress.get(i).getAddressBase()+listAddress.get(i).getAddressDetail());
                 tv_order_name.setText(listAddress.get(i).getReceiveName()+"     "+listAddress.get(i).getMobile());
             }
         }

        }
    }

    @Override
    public void onClick(View v) {
        Intent intent=null;
        switch (v.getId()){
            case R.id.rl_address:
                intent =new Intent(this,StoreManageAddressActivity.class);
                startActivityForResult(intent, 300);
                AnimUtil.intentSlidIn(this);
                break;
            case R.id.btn_jiesuan:
                try {
                    OrderGoods goods=new OrderGoods();
                    List<OrderGoods>list=new ArrayList<>();
                    for(int i=0;i<listGoodsBean.size();i++){
                        goods.setId(listGoodsBean.get(i).getGoodsid());
                        goods.setNums(listGoodsBean.get(i).getGoodsnum());
                        goods.setAmounts(listGoodsBean.get(i).getGoodsprice() + "");
                        list.add(goods);
                    }
                    String productStr= JsonUtil.pojo2json(list);
                    Log.i("tag", "productStr======" + productStr);
                    odao.requestGenetateOrder(Arad.preferences.getString("memberId"),companyId,productStr,"4","1",name,tel,province,
                            city,area,address,"1","");


                } catch (IOException e) {
                    e.printStackTrace();
                }


               /* intent =new Intent(this,StorePaymentOrderActivity.class);
                startActivity(intent);
                AnimUtil.intentSlidIn(this);*/
                break;
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK&&requestCode==300){
            tv_order_name.setText(name+"     "+tel);
            tv_order_address.setText(address);
            name=data.getStringExtra("name");
            tel=data.getStringExtra("tel");
            address=data.getStringExtra("address");
            province=data.getStringExtra("province");
            city=data.getStringExtra("city");
            area=data.getStringExtra("area");
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
