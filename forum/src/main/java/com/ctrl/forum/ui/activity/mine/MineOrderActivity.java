package com.ctrl.forum.ui.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.dao.MineStoreDao;
import com.ctrl.forum.dao.OrderDao;
import com.ctrl.forum.entity.MemeberOrder;
import com.ctrl.forum.ui.activity.store.StoreOrderStatusActivity;
import com.ctrl.forum.ui.adapter.MineOrderListAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

/**
 * 我的订单
 */
public class MineOrderActivity extends AppToolBarActivity implements View.OnClickListener{
    private PullToRefreshListView lv_order;
    private List<MemeberOrder> orders;
    private MineOrderListAdapter orderListviewAdapter;
    private MineStoreDao orderDao;
    private OrderDao odao;
    private int PAGE_NUM=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_order);
        lv_order = (PullToRefreshListView) findViewById(R.id.lv_order);

        initData();
        orderListviewAdapter = new MineOrderListAdapter(getApplicationContext());
        lv_order.setAdapter(orderListviewAdapter);

        lv_order.setMode(PullToRefreshBase.Mode.BOTH);
        lv_order.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (orders != null) {
                    orders.clear();
                }
                PAGE_NUM = 1;
                orderDao.getMemeberOrder(Arad.preferences.getString("memberId"), PAGE_NUM + "", Constant.PAGE_SIZE + "");
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (orders != null) {
                    PAGE_NUM += 1;
                    orderDao.getMemeberOrder(Arad.preferences.getString("memberId"), PAGE_NUM + "", Constant.PAGE_SIZE + "");
                } else {
                    lv_order.onRefreshComplete();
                }
            }
        });

        lv_order.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), StoreOrderStatusActivity.class);
                intent.putExtra("id",orders.get(position-1).getId());
                startActivity(intent);
            }
        });

        orderListviewAdapter.setOnDelete(this);
        orderListviewAdapter.setOnCancle(this);
        orderListviewAdapter.setOnGoods(this);
    }

    private void initData() {
        orderDao = new MineStoreDao(this);
        orderDao.getMemeberOrder(Arad.preferences.getString("memberId"),PAGE_NUM+"", Constant.PAGE_SIZE+"");

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
        lv_order.onRefreshComplete();
        if (requestCode==0){
            orders = orderDao.getMemeberOrders();
            if (orders!=null){
                orderListviewAdapter.setOrders(orders);
            }
        }
        if (requestCode==3){
            if (orders!=null) {
                orders.clear();
            }
            PAGE_NUM=1;
            orderDao.getMemeberOrder(Arad.preferences.getString("memberId"),PAGE_NUM+"", Constant.PAGE_SIZE+"");
        }
        if (requestCode==1){
            MessageUtils.showShortToast(this,"删除订单成功");
            if (orders!=null) {
                orders.clear();
            }
            PAGE_NUM=1;
            orderDao.getMemeberOrder(Arad.preferences.getString("memberId"),PAGE_NUM+"", Constant.PAGE_SIZE+"");
        }
        if (requestCode==2){
            MessageUtils.showShortToast(this,"签收成功");
        }
    }

    @Override
    public void onClick(View v) {
        Object position = v.getTag();
        String id="";
        switch (v.getId()){
            case R.id.iv_delete:
                id = (String)position;
                odao.requestDeleteOrder(id);
                break;
            case R.id.bt_left: //取消订单
                id = (String)position;
                odao.requestCancelOrder(id);
                break;
            case R.id.bt_right:
                String signId = (String)position;
                odao.requestSignOrder(signId);
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (orders!=null){
            orders.clear();
        }
        PAGE_NUM=1;
        orderDao.getMemeberOrder(Arad.preferences.getString("memberId"),PAGE_NUM+"",Constant.PAGE_SIZE+"");
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        lv_order.onRefreshComplete();
    }
}
