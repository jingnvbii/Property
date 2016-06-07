package com.ctrl.forum.ui.activity.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.dao.MineStoreDao;
import com.ctrl.forum.dao.OrderDao;
import com.ctrl.forum.entity.MemeberOrder;
import com.ctrl.forum.ui.adapter.MineOrderListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的订单
 */
public class MineOrderActivity extends AppToolBarActivity implements View.OnClickListener{
    private ListView lv_order;
    private List<MemeberOrder> orders;
    private List<Integer> types; //类型
    private MineOrderListAdapter orderListviewAdapter;
    private MineStoreDao orderDao;
    private OrderDao odao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_order);
        lv_order = (ListView) findViewById(R.id.lv_order);

        initData();
        orderListviewAdapter = new MineOrderListAdapter(getApplicationContext());
        lv_order.setAdapter(orderListviewAdapter);
        orderListviewAdapter.setOnBuy(this);
        orderListviewAdapter.setOnDelete(this);
        orderListviewAdapter.setOnPay(this);
        orderListviewAdapter.setOnPingJia(this);
        orderListviewAdapter.setOnCancle(this);
    }

    private void initData() {
        orderDao = new MineStoreDao(this);
        orderDao.getMemeberOrder(Arad.preferences.getString("memberId"));

        odao = new OrderDao(this);
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
    public String setupToolBarTitle() {return getResources().getString(R.string.my_order);}

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if (requestCode==0){
            MessageUtils.showShortToast(this,"获取个人订单成功!");
            orders = orderDao.getMemeberOrders();
            putData();
        }
    }

    public void putData(){
        if (orders!=null){
            types = new ArrayList<>();
            orderListviewAdapter.setOrders(orders);
            for (int i=0;i<orders.size();i++){
                String state = orders.get(i).getState();
                if (state.equals("3")){
                    types.add(i,3);
                }else{
                    types.add(i,0);
                }
            }
            orderListviewAdapter.setTypes(types);
        }
    }

    @Override
    public void onClick(View v) {
        Object position = v.getTag();
        String id="";
        switch (v.getId()){
            case R.id.payment:  //付款
                id = (String)position;
                break;
            case R.id.iv_delete:
                id = (String)position;
                odao.requestDeleteOrder(id);
                break;
            case R.id.buy_again: //再次购买
                id = (String)position;

                break;
            case R.id.button2: //评价
                id = (String)position;

                break;
            case R.id.cancle: //取消订单
                id = (String)position;
                odao.requestCancelOrder(id);
                break;
        }
    }
}
