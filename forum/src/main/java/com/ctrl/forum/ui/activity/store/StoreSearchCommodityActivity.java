package com.ctrl.forum.ui.activity.store;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.entity.Merchant;
import com.ctrl.forum.ui.adapter.StoreSearchGridViewAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/*
* 商城商品搜索 activity
* */

public class StoreSearchCommodityActivity extends AppToolBarActivity implements View.OnClickListener{
    @InjectView(R.id.iv_back)//返回按钮
    ImageView iv_back;
   /* @InjectView(R.id.spinner)//下拉列表
            Spinner spinner;*/
    @InjectView(R.id.et_search)//搜索输入框
    EditText et_search;
    @InjectView(R.id.tv_search)//搜索按钮
    TextView tv_search;

    @InjectView(R.id.tv_choose)//切换按钮
    TextView tv_choose;

    @InjectView(R.id.tv_xiaoliang)//销量优先
    TextView tv_xiaoliang;
    @InjectView(R.id.tv_pinjia)//评价优先
    TextView tv_pinjia;
    @InjectView(R.id.gridview)//网格列表
    PullToRefreshGridView gridView;
    private List<Merchant> list;
    private StoreSearchGridViewAdapter adapter;
    private PopupWindow popupWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_commodity_search);
        ButterKnife.inject(this);
        // 隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
        initData();
        adapter=new StoreSearchGridViewAdapter(this);
        adapter.setList(list);
        gridView.getRefreshableView().setNumColumns(2);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              /*  Intent intent=new Intent(StoreSearchCommodityActivity.this,StoreShopListVerticalStyleActivity.class);
                startActivity(intent);
                AnimUtil.intentSlidIn(StoreSearchCommodityActivity.this);*/
            }
        });
    }

    private void initData() {
        list=new ArrayList<>();

        for(int i=0;i<20;i++){
            Merchant manchant = new Merchant();
            manchant.setName("章子怡"+i+"便利店");
            list.add(manchant);
        }
    }

    private void initView() {
       tv_choose.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_choose:
                showPopupWidow();
                break;
        }


    }

    private void showPopupWidow() {
        View contentView= LayoutInflater.from(this).inflate(R.layout.popupwindow_store_choose,null);
        TextView tv_dianpu_pup = (TextView) contentView.findViewById(R.id.tv_dianpu_pup);
        final TextView tv_shangpin_pup = (TextView) contentView.findViewById(R.id.tv_shangpin_pup);

        tv_dianpu_pup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_choose.setText("店铺");
                popupWindow.dismiss();
            }
        });
        tv_shangpin_pup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_choose.setText("商品");
                popupWindow.dismiss();

            }
        });

        popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT, true);
     /*   // 设置SelectPicPopupWindow的View
        popupWindow.setContentView(contentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);*/
        // 设置SelectPicPopupWindow弹出窗体可点击
        popupWindow.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        popupWindow.setAnimationStyle(R.style.AnimBottom);
        // 实例化一个ColorDrawable颜色为半透明
      //  ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.mipmap.choose_bg));

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

        // 设置好参数之后再show
        popupWindow.showAsDropDown(tv_choose,0,20);

    }


}
