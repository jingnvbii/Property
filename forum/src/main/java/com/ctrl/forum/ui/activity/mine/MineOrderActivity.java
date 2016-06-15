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

import java.util.List;

/**
 * 我的订单
 */
public class MineOrderActivity extends AppToolBarActivity implements View.OnClickListener{
    private ListView lv_order;
    private List<MemeberOrder> orders;
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
            orders = orderDao.getMemeberOrders();
            if (orders!=null){
                orderListviewAdapter.setOrders(orders);
            }
        }
        if (requestCode==3){
            orders.clear();
            orderListviewAdapter = new MineOrderListAdapter(getApplicationContext());
            lv_order.setAdapter(orderListviewAdapter);
            orderDao.getMemeberOrder(Arad.preferences.getString("memberId"));
        }
        if (requestCode==1){
            orders.clear();
            orderListviewAdapter = new MineOrderListAdapter(getApplicationContext());
            lv_order.setAdapter(orderListviewAdapter);
            orderDao.getMemeberOrder(Arad.preferences.getString("memberId"));
        }
    }

    @Override
    public void onClick(View v) {
        Object position = v.getTag();
        String id="";
        switch (v.getId()){
            case R.id.payment:  //付款
                id = (String)position;
                MessageUtils.showShortToast(this,"付款");
                break;
            case R.id.iv_delete:
                id = (String)position;
                odao.requestDeleteOrder(id);
                break;
            case R.id.bt_right: //再次购买
                id = (String)position;
                MessageUtils.showShortToast(this,"再次购买");
                break;
            case R.id.bt_left: //评价
                id = (String)position;
                MessageUtils.showShortToast(this,"评价");
                break;
            case R.id.cancle: //取消订单
                id = (String)position;
                odao.requestCancelOrder(id);
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        orderDao.getMemeberOrder(Arad.preferences.getString("memberId"));
    }
}
