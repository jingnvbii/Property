package com.ctrl.forum.ui.activity.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.entity.Order;
import com.ctrl.forum.ui.adapter.MineOrderListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的订单
 */
public class MineOrderActivity extends AppToolBarActivity {
    private ListView lv_order;
    private List<Order> orders;
    private MineOrderListAdapter orderListviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_order);
        lv_order = (ListView) findViewById(R.id.lv_order);

        initData();
        orderListviewAdapter = new MineOrderListAdapter(getApplicationContext(),orders);
        lv_order.setAdapter(orderListviewAdapter);
        orderListviewAdapter.setType(0);
    }

    private void initData() {
        orders = new ArrayList<>();
        for (int i =0;i<4;i++){
            Order order = new Order();
            order.setTotal("1366.2");
            orders.add(order);
        }
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

}
