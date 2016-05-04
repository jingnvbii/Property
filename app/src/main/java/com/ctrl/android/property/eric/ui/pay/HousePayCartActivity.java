package com.ctrl.android.property.eric.ui.pay;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ctrl.android.property.R;
import com.ctrl.android.property.base.AppHolder;
import com.ctrl.android.property.base.AppToolBarActivity;
import com.ctrl.android.property.eric.entity.HousePay;
import com.ctrl.android.property.eric.entity.PropertyPay;
import com.ctrl.android.property.eric.ui.adapter.HousePayCartAdapter;
import com.ctrl.android.property.eric.util.StrConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 房屋缴费 (购物车) activity
 * Created by Eric on 2015/10/22
 */
public class HousePayCartActivity extends AppToolBarActivity implements View.OnClickListener{

    @InjectView(R.id.listView)
    ListView listView;

    @InjectView(R.id.amount_text)
    TextView amount_text;

    private HousePayCartAdapter housePayCartAdapter;

    private String TITLE = StrConstant.HOUSE_PAY_TITLE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 禁止横屏
        setContentView(R.layout.house_pay_cart_activity);
        ButterKnife.inject(this);
        init();
    }

    /**
     * 初始化执行的 方法
     * */
    private void init(){

        housePayCartAdapter = new HousePayCartAdapter(this);
        if((AppHolder.getInstance().getListPropertyPay()) != null && (AppHolder.getInstance().getListPropertyPay()).size() > 0){
            housePayCartAdapter.setList(AppHolder.getInstance().getListPropertyPay());
        }
        listView.setAdapter(housePayCartAdapter);

    }

    @Override
    public void onClick(View v) {

    }


    /**
     * header 左侧按钮
     * */
    @Override
    public boolean setupToolBarLeftButton(ImageView leftButton) {
        leftButton.setImageResource(R.drawable.white_arrow_left_none);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //MessageUtils.showShortToast(MallShopActivity.this, "AA");
                onBackPressed();
            }
        });
        return true;
    }

    /**
     * 页面标题
     * */
    @Override
    public String setupToolBarTitle() {
        return TITLE;
    }

    @Override
    public boolean setupToolBarRightText(TextView mRightText) {
        mRightText.setText(StrConstant.CLEAR);
        mRightText.setTextColor(getResources().getColor(R.color.text_white));
        mRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MessageUtils.showShortToast(HousePayCartActivity.this, "清空");
                AppHolder.getInstance().setListPropertyPay(new ArrayList<PropertyPay>());
                housePayCartAdapter.setList(AppHolder.getInstance().getListPropertyPay());
                listView.setAdapter(housePayCartAdapter);
                housePayCartAdapter.notifyDataSetChanged();
            }
        });
        return true;
    }

    /**
     * 测试 获取数据的方法
     * */
    private List<HousePay> getList(){
        List<HousePay> list = new ArrayList<>();
        for(int i = 0 ; i < 10 ; i ++){
            HousePay p = new HousePay();
            p.setCheck(true);
            p.setName(i + "中润世纪小区");
            p.setAddress("15楼-1单元-20" + i + "室");

            List<Map<String,String>> l = new ArrayList<>();
            for(int j = 0 ; j < 5 ; j ++){
                Map<String,String> map = new HashMap<>();
                map.put("check","" + (i%2));
                map.put("name",i + "水费");
                map.put("amount","102." + i + "654");
                l.add(map);
            }
            p.setListMap(l);
            list.add(p);
        }
        return list;
    }



}
