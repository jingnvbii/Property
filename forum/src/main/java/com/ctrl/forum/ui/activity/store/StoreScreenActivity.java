package com.ctrl.forum.ui.activity.store;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.arad.utils.AnimUtil;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.entity.Merchant;
import com.ctrl.forum.ui.adapter.StoreFragmentAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/*
* 商城店铺列表筛选 activity
* */

public class StoreScreenActivity extends AppToolBarActivity implements View.OnClickListener{
    @InjectView(R.id.listview)//下拉列表
    PullToRefreshListView listView;
    @InjectView(R.id.rl_all)//全部
    RelativeLayout rl_all;
    @InjectView(R.id.rl_sort)//排序
    RelativeLayout rl_sort;
    private StoreFragmentAdapter listviewAdapter;
    private List<Merchant> list;
    private PopupWindow popupWindow;
    private  List<String> sortList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_screen);
        ButterKnife.inject(this);
        // 隐藏输入法
     //   getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initData();
        initView();

        listviewAdapter=new StoreFragmentAdapter(this);
        listviewAdapter.setList(list);
        listView.setAdapter(listviewAdapter);
    }

    private void initData() {
        list=new ArrayList<>();

        for(int i=0;i<20;i++){
            Merchant manchant = new Merchant();
            manchant.setName("章子怡"+i+"便利店");
            list.add(manchant);
        }

        sortList = new ArrayList<>();
        sortList.add("评价最高");
        sortList.add("销量最高");
        sortList.add("距离最近");


        
    }

    private void initView() {
       rl_all.setOnClickListener(this);
       rl_sort.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_all:
                showAllPopupWindow(sortList);
                break;
            case R.id.rl_sort:
                showAllPopupWindow(sortList);
                break;
        }


    }

    private void showAllPopupWindow(List<String>list) {
        View contentView= LayoutInflater.from(this).inflate(R.layout.popupwindow_store_screen,null);
        ListView lv = (ListView)contentView.findViewById(R.id.popup_listview);
        ArrayAdapter adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,list);
        Log.i("tag", "list" + list.size());


        popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT, true);
        // 设置SelectPicPopupWindow的View
        popupWindow.setContentView(contentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        popupWindow.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        popupWindow.setAnimationStyle(R.style.AnimBottom);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(dw);

        contentView.setFocusable(true);
        contentView.setFocusableInTouchMode(true);
        popupWindow.setTouchable(true);
         /*
        * 设置popupwindow 点击自身消失
        * */
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });

        popupWindow.setOutsideTouchable(true);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.i("mengdd", "onTouch : ");

                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        lv.setAdapter(adapter);
        // 设置好参数之后再show
        popupWindow.showAsDropDown(rl_all);

    }


    @Override
    public String setupToolBarTitle() {
        TextView tv = getmTitle();
        Drawable drawable = getResources().getDrawable(R.mipmap.locate_img);
        drawable.setBounds(0,0,32,32);
        tv.setCompoundDrawables(drawable, null, null, null);
        tv.setCompoundDrawablePadding(10);
        tv.setTextColor(Color.WHITE);
        tv.setText("中润世纪广场");
        return tv.getText().toString();
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

    @Override
    public boolean setupToolBarRightButton(ImageView rightButton) {
        rightButton.setImageResource(R.mipmap.search_white);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StoreScreenActivity.this,StoreSearchActivity.class);
                startActivity(intent);
                AnimUtil.intentSlidIn(StoreScreenActivity.this);
            }
        });
        return true;
    }
}
