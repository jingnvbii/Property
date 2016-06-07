package com.ctrl.forum.ui.activity.store;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.arad.utils.AnimUtil;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.dao.KindDao;
import com.ctrl.forum.dao.MallDao;
import com.ctrl.forum.entity.Kind;
import com.ctrl.forum.entity.Mall;
import com.ctrl.forum.entity.Merchant;
import com.ctrl.forum.ui.adapter.StoreFragmentAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/*
* 商城店铺列表筛选 activity
* */

public class StoreScreenActivity extends AppToolBarActivity implements View.OnClickListener {
    @InjectView(R.id.lv_store_screen)//下拉列表
            PullToRefreshListView lv_store_screen;
    @InjectView(R.id.rl_all)//全部
            RelativeLayout rl_all;
    @InjectView(R.id.rl_sort)//排序
            RelativeLayout rl_sort;
    private StoreFragmentAdapter listviewAdapter;
    private List<Merchant> list;
    private PopupWindow popupWindow;
    private List<String> sortList;
    private String channelId;
    private String latitude;
    private String longitude;
    private MallDao mdao;
    private int PAGE_NEM = 1;
    private KindDao kdao;
    private List<Kind> listKind;
    private ArrayList<String> listKindStr;
    private ListView lv;
    private ArrayAdapter<String> adapter;
    private List<Mall> listMall;
    private boolean isFromAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_screen);
        ButterKnife.inject(this);
        // 隐藏输入法
        //   getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
        initData();

    }

    private void initData() {
        mdao = new MallDao(this);
        kdao = new KindDao(this);
        kdao.requestObtainKindList("COMPANY");
        channelId = getIntent().getStringExtra("channelId");
        latitude = getIntent().getStringExtra("latitude");
        longitude = getIntent().getStringExtra("longitude");
        if(getIntent().getFlags()==303){
            mdao.requestCompanyByKind("0", getIntent().getStringExtra("latitude"), getIntent().getStringExtra("longitude"), "", String.valueOf(PAGE_NEM), String.valueOf(Constant.PAGE_SIZE));
        }else {
            mdao.requestCompanyByKind("0", latitude, longitude, channelId, String.valueOf(PAGE_NEM), String.valueOf(Constant.PAGE_SIZE));
        }
        sortList = new ArrayList<>();
        sortList.add("默认排序");
        sortList.add("评价最高");
        sortList.add("销量最高");
        sortList.add("距离最近");

        listviewAdapter = new StoreFragmentAdapter(this);
        lv_store_screen.setAdapter(listviewAdapter);
        lv_store_screen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(StoreScreenActivity.this, StoreShopListVerticalStyleActivity.class);
                intent.putExtra("id", listMall.get(position - 1).getId());
                intent.putExtra("url", listMall.get(position - 1).getImg());
                intent.putExtra("name", listMall.get(position - 1).getName());
                intent.putExtra("startTime", listMall.get(position - 1).getWorkStartTime());
                intent.putExtra("endTime", listMall.get(position - 1).getWorkEndTime());
                intent.putExtra("levlel", listMall.get(position - 1).getEvaluatLevel());
                startActivity(intent);
                AnimUtil.intentSlidIn(StoreScreenActivity.this);
            }
        });

        lv_store_screen.setMode(PullToRefreshBase.Mode.BOTH);
        lv_store_screen.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NEM=1;
                listMall.clear();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mdao.requestCompanyByKind("0", latitude, longitude, channelId, String.valueOf(PAGE_NEM), String.valueOf(Constant.PAGE_SIZE));
                    }
                },500);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NEM+=1;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mdao.requestCompanyByKind("0", latitude, longitude, channelId, String.valueOf(PAGE_NEM), String.valueOf(Constant.PAGE_SIZE));
                    }
                },500);
            }
        });

    }

    private void initView() {
        rl_all.setOnClickListener(this);
        rl_sort.setOnClickListener(this);
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        lv_store_screen.onRefreshComplete();
        if(errorNo.equals("006")){
            if(listMall!=null)
            listMall.clear();
            if(popupWindow!=null)
                popupWindow.dismiss();

        }
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        lv_store_screen.onRefreshComplete();
        if (requestCode == 5) {
          //  MessageUtils.showShortToast(this, "获取店铺列表成功");
            listMall = mdao.getListMall();
            listviewAdapter.setList(listMall);
            if(popupWindow!=null){
                popupWindow.dismiss();
            }
        }
        if (requestCode == 777) {
          //  MessageUtils.showShortToast(this, "获取全部分类列表成功");
            listKind = kdao.getListKind();
            listKindStr = new ArrayList<String>();
            for (int i = 0; i < listKind.size(); i++) {
                listKindStr.add(listKind.get(i).getKindName());
            }


        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_all:
                showAllPopupWindow(listKindStr);
                isFromAll = true;
                break;
            case R.id.rl_sort:
                isFromAll = false;
                showAllPopupWindow(sortList);
                break;
        }


    }

    private void showAllPopupWindow(List<String> list) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.popupwindow_store_screen, null);
        lv = (ListView) contentView.findViewById(R.id.popup_listview);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isFromAll) {
                    if(listMall!=null){
                        listMall.clear();
                    }
                    mdao.requestCompanyByKind("0", latitude, longitude, listKind.get(position).getId(), String.valueOf(PAGE_NEM), String.valueOf(Constant.PAGE_SIZE));
                }else {
                    if(listMall!=null){
                        listMall.clear();
                    }
                    switch (position){

                        case 0:
                            mdao.requestCompanyByKind("0", latitude, longitude, "", String.valueOf(PAGE_NEM), String.valueOf(Constant.PAGE_SIZE));
                            break;
                        case 1:
                            mdao.requestCompanyByKind("1", latitude, longitude, "", String.valueOf(PAGE_NEM), String.valueOf(Constant.PAGE_SIZE));
                            break;
                        case 2:
                            mdao.requestCompanyByKind("2", latitude, longitude, "", String.valueOf(PAGE_NEM), String.valueOf(Constant.PAGE_SIZE));
                            break;
                        case 3:
                            mdao.requestCompanyByKind("3", latitude, longitude, "", String.valueOf(PAGE_NEM), String.valueOf(Constant.PAGE_SIZE));
                            break;
                    }

                }
            }
        });

        Log.i("tag", "list" + list.size());


        popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        // 设置SelectPicPopupWindow的View
        popupWindow.setContentView(contentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        popupWindow.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        //  popupWindow.setAnimationStyle(R.style.AnimBottom);
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
        drawable.setBounds(0, 0, 32, 32);
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
                Intent intent = new Intent(StoreScreenActivity.this, StoreSearchActivity.class);
                startActivity(intent);
                AnimUtil.intentSlidIn(StoreScreenActivity.this);
            }
        });
        return true;
    }
}
