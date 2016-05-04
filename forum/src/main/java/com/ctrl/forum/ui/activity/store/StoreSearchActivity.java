package com.ctrl.forum.ui.activity.store;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.beanu.arad.utils.AnimUtil;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/*
* 商城搜索 activity
* */

public class StoreSearchActivity extends AppToolBarActivity implements View.OnClickListener{
    @InjectView(R.id.tv_choose)//切换按钮
    TextView tv_choose;

    @InjectView(R.id.iv_back)//返回按钮
            ImageView iv_back;
    /* @InjectView(R.id.spinner)//下拉列表
             Spinner spinner;*/
    @InjectView(R.id.et_search)//搜索输入框
            EditText et_search;
    @InjectView(R.id.tv_search)//搜索按钮
            TextView tv_search;


    private PopupWindow popupWindow;

    private int choose=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_search);
        ButterKnife.inject(this);
        // 隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
    }

    private void initView() {
        tv_choose.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        tv_search.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        Intent intent=null;
        switch (v.getId()){
            case R.id.tv_choose:
                showPopupWidow();
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_search:
                if(choose==1){
                    intent=new Intent(this,StoreSearchShopActivity.class);
                    startActivity(intent);
                    AnimUtil.intentSlidIn(this);
                }else if(choose==2){
                    intent=new Intent(this,StoreSearchCommodityActivity.class);
                    startActivity(intent);
                    AnimUtil.intentSlidIn(this);
                }

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
                choose=1;
                popupWindow.dismiss();
            }
        });
        tv_shangpin_pup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_choose.setText("商品");
                choose=2;
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
