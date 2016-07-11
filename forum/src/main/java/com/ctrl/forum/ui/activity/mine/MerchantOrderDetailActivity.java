package com.ctrl.forum.ui.activity.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.base.SetMemberLevel;
import com.ctrl.forum.dao.MineStoreDao;
import com.ctrl.forum.dao.OrderDao;
import com.ctrl.forum.entity.OrderItem;
import com.ctrl.forum.entity.OrderState;
import com.ctrl.forum.ui.adapter.MineOrderDetailAdapter;
import com.ctrl.forum.utils.DateUtil;
import java.util.List;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 商家订单详情
 */
public class MerchantOrderDetailActivity extends AppToolBarActivity{
    private String id;
    private String deliveryNo,expressName,receiverMobile,evaluationState;

    @InjectView(R.id.tv_number)
    TextView tv_number;  //订单编号
    @InjectView(R.id.tv_order_time)
    TextView tv_order_time;  //下单时间
    @InjectView(R.id.tv_order_name)
    TextView tv_order_name;  //收货人
    @InjectView(R.id.tv_order_phone)
    TextView tv_order_phone;  //电话
    @InjectView(R.id.tv_state)
    TextView tv_state;    //状态
    @InjectView(R.id.tv_order_address)
    TextView tv_order_address; //地址
    @InjectView(R.id.lv_goods)
    ListView lv_goods;  //商品列表
    @InjectView(R.id.tv_money)
    TextView tv_money;  //合计
    @InjectView(R.id.tv_company)
    TextView tv_company;
    @InjectView(R.id.tv_company_hao)
    TextView tv_company_hao;
    @InjectView(R.id.tv_login)
    TextView tv_login;
    @InjectView(R.id.good)
    TextView good;
    @InjectView(R.id.ratingBar)
    RatingBar ratingBar;
    @InjectView(R.id.et_content)
    EditText et_content;
    @InjectView(R.id.pinglun)
    LinearLayout pinglun;

    private OrderDao oDao;
    private List<OrderItem> listOrderItem;//订单商品列表
    private OrderState orderState;//订单状态列表
    private MineStoreDao mdao;

    private MineOrderDetailAdapter mineOrderDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_order_detail);
        ButterKnife.inject(this);

        id = getIntent().getStringExtra("id");
        oDao = new OrderDao(this);
        oDao.requesOrderDetail(id);
        mdao = new MineStoreDao(this);

        deliveryNo = getIntent().getStringExtra("deliveryNo");//物流单号
        expressName = getIntent().getStringExtra("expressName");//物流公司名称
        receiverMobile = getIntent().getStringExtra("receiverMobile");
        evaluationState = getIntent().getStringExtra("evaluationState");

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdao.deliverGoods(id,orderState.getOrderNum());
            }
        });

        mineOrderDetailAdapter = new MineOrderDetailAdapter(this);
        lv_goods.setAdapter(mineOrderDetailAdapter);
    }

    @Override
    public boolean setupToolBarLeftButton(ImageView leftButton) {
        leftButton.setImageResource(R.mipmap.white_arrow_left_none);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        return true;
    }

    @Override
    public String setupToolBarTitle() {return "订单详情";}

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if (requestCode==66){
            listOrderItem = oDao.getListOrderItem();
            orderState = oDao.getOrderState();
            if (listOrderItem!=null){
                mineOrderDetailAdapter.setList(listOrderItem);
                for (int i=0;i<listOrderItem.size();i++){
                    if (listOrderItem.get(i).getAmount()!=null) {
                        float money = Float.valueOf(listOrderItem.get(i).getAmount());
                        money +=money ;
                        tv_money.setText(money+"");
                    }
                }
            }
            if (orderState!=null){
                setTextValue();
            }
        }
        if (requestCode==8){
            MessageUtils.showShortToast(this,"发货成功");
            this.finish();
        }
    }

    private void setTextValue() {
        tv_number.setText(orderState.getOrderNum());
        tv_order_time.setText(DateUtil.getStringByFormat(orderState.getCreateTime(), "yyyy-MM-dd   hh:mm:ss"));
        tv_order_name.setText(orderState.getReceiverName());
        tv_order_phone.setText(receiverMobile);
        tv_order_address.setText(orderState.getAddress());
        tv_company.setText(expressName);
        tv_company_hao.setText(deliveryNo);

        String type = orderState.getState();
        switch (type){
            case "1": //1-订单被用户取消
                tv_state.setText("订单被用户取消");
                pinglun.setVisibility(View.GONE);
                tv_login.setVisibility(View.GONE);
                break;
            case "2":
                tv_state.setText("订单被系统取消");
                pinglun.setVisibility(View.GONE);
                tv_login.setVisibility(View.GONE);
                break;
            case "3":
                tv_state.setText("未付款");
                pinglun.setVisibility(View.GONE);
                tv_login.setVisibility(View.GONE);
                break;
            case "4":
                tv_state.setText("已付款");
                pinglun.setVisibility(View.GONE);
                break;
            case "5":
                tv_state.setText("待签收");
                pinglun.setVisibility(View.GONE);
                tv_login.setVisibility(View.GONE);
                break;
            case "6":
                pinglun.setVisibility(View.VISIBLE);
                tv_login.setVisibility(View.GONE);
                if (evaluationState.equals("0")){
                    tv_state.setText("待评价");
                }else{
                    tv_state.setText("已评价");
                    if (orderState.getLevel()!=null && !orderState.getLevel().equals("")){
                        float level = Float.valueOf(orderState.getLevel());
                        ratingBar.setRating(level);
                        good.setText(SetMemberLevel.setLeveText(level));
                    }else{
                        ratingBar.setRating(0);
                        good.setText("");
                    }
                    et_content.setText(orderState.getContent());
                }
                break;
            default:
                break;
        }
    }

}
