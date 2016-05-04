package com.ctrl.android.property.eric.ui.pay;

import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.beanu.arad.utils.MessageUtils;
import com.ctrl.android.property.R;
import com.ctrl.android.property.base.AppToolBarActivity;
import com.ctrl.android.property.eric.ui.adapter.HousePayHistoryAdapter;
import com.ctrl.android.property.eric.ui.adapter.ListItemAdapter;
import com.ctrl.android.property.eric.util.D;
import com.ctrl.android.property.eric.util.S;
import com.ctrl.android.property.eric.util.StrConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 房屋缴费 历史记录 activity
 * Created by Eric on 2015/10/22
 */
public class HousePayHistoryActivity extends AppToolBarActivity implements View.OnClickListener{

    @InjectView(R.id.year_btn)//年份
    TextView year_btn;
    @InjectView(R.id.month_btn)//月份
    TextView month_btn;
    @InjectView(R.id.search_btn)//搜索
    TextView search_btn;

    @InjectView(R.id.listView)
    ListView listView;

    private ArrayList<String> listYear;
    private ArrayList<String> listMonth;

    private ListItemAdapter itemAdapter;
    private HousePayHistoryAdapter housePayHistoryAdapter;

    private String TITLE = StrConstant.HOUSE_PAY_HISTORY_TITLE;

    private View mMenuView;//显示pop的view

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 禁止横屏
        setContentView(R.layout.house_pay_history_activity);
        ButterKnife.inject(this);
        init();
    }

    /**
     * 初始化执行的 方法
     * */
    private void init(){
        year_btn.setOnClickListener(this);
        month_btn.setOnClickListener(this);
        search_btn.setOnClickListener(this);

        housePayHistoryAdapter = new HousePayHistoryAdapter(this);
        housePayHistoryAdapter.setList(getListMap());
        listView.setAdapter(housePayHistoryAdapter);

    }

    @Override
    public void onClick(View v) {
        if(v == year_btn){
            showYearListPop();
        }

        if(v == month_btn){
            showMonthListPop();
        }

        if(v == search_btn){
            MessageUtils.showShortToast(this,year_btn.getText().toString() + "-" + month_btn.getText().toString());
        }
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
     * 显示年份的 popwindow
     * */
    private void showYearListPop(){

        listYear = new ArrayList<>();
        listYear = D.getRecent5Years();
        itemAdapter = new ListItemAdapter(this);
        itemAdapter.setList(listYear);

        mMenuView = LayoutInflater.from(HousePayHistoryActivity.this).inflate(R.layout.choose_list_pop, null);
        ListView listView = (ListView)mMenuView.findViewById(R.id.listView);
        listView.setAdapter(itemAdapter);

        final PopupWindow pop = new PopupWindow();

        // 设置SelectPicPopupWindow的View
        pop.setContentView(mMenuView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        pop.setWidth(year_btn.getWidth());
        // 设置SelectPicPopupWindow弹出窗体的高
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        pop.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        //Pop.setAnimationStyle(R.style.AnimBottom);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        pop.setBackgroundDrawable(dw);

        mMenuView.setFocusable(true);
        mMenuView.setFocusableInTouchMode(true);
        pop.setFocusable(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MessageUtils.showShortToast(HousePayHistoryActivity.this, listYear.get(position));
                year_btn.setText(S.getStr(listYear.get(position)));
                pop.dismiss();
            }
        });

        int[] location = new int[2];
        year_btn.getLocationOnScreen(location);
        //Pop.showAtLocation(year_btn, Gravity.NO_GRAVITY, location[0], location[1] - Pop.getHeight());
        pop.showAsDropDown(year_btn);
        //Pop.showAtLocation(mall_main_pop_layout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }


    /**
     * 显示月份的 popwindow
     * */
    private void showMonthListPop(){

        listMonth = new ArrayList<>();
        listMonth = D.getAllMonths();
        itemAdapter = new ListItemAdapter(this);
        itemAdapter.setList(listMonth);

        mMenuView = LayoutInflater.from(HousePayHistoryActivity.this).inflate(R.layout.choose_list_pop, null);
        ListView listView = (ListView)mMenuView.findViewById(R.id.listView);
        listView.setAdapter(itemAdapter);



        final PopupWindow pop = new PopupWindow();

        // 设置SelectPicPopupWindow的View
        pop.setContentView(mMenuView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        pop.setWidth(month_btn.getWidth());
        // 设置SelectPicPopupWindow弹出窗体的高
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        pop.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        //Pop.setAnimationStyle(R.style.AnimBottom);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        pop.setBackgroundDrawable(dw);

        mMenuView.setFocusable(true);
        mMenuView.setFocusableInTouchMode(true);
        pop.setFocusable(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MessageUtils.showShortToast(HousePayHistoryActivity.this, listMonth.get(position));
                month_btn.setText(S.getStr(listMonth.get(position)));
                pop.dismiss();
            }
        });

        int[] location = new int[2];
        month_btn.getLocationOnScreen(location);
        //Pop.showAtLocation(year_btn, Gravity.NO_GRAVITY, location[0], location[1] - Pop.getHeight());
        pop.showAsDropDown(month_btn);
        //Pop.showAtLocation(mall_main_pop_layout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 页面标题
     * */
    @Override
    public String setupToolBarTitle() {
        return TITLE;
    }

    /**
     * 测试 获取数据的方法
     * */
    private List<Map<String,String>> getListMap(){
        List<Map<String,String>> list = new ArrayList<>();
        for(int i = 0 ; i < 10 ; i ++){
            Map<String,String> map = new HashMap<String,String>();
            map.put("name", "条目" + i);
            map.put("time", "10月" + i + "日");
            map.put("amount","20" + i + ".5" + i + "9");
            list.add(map);
        }
        return list;
    }



}
