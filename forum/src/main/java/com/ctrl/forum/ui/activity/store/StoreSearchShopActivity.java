package com.ctrl.forum.ui.activity.store;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.beanu.arad.Arad;
import com.beanu.arad.utils.AnimUtil;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.dao.MallDao;
import com.ctrl.forum.entity.Merchant;
import com.ctrl.forum.ui.adapter.StoreFragmentAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/*
* 商城店铺搜索 activity
* */

public class StoreSearchShopActivity extends AppToolBarActivity implements View.OnClickListener{
    @InjectView(R.id.listview_shop)//下拉列表
    PullToRefreshListView listview_shop;
    @InjectView(R.id.tv_xiaoliang)//销量优先
    TextView tv_xiaoliang;
    @InjectView(R.id.tv_pinjia)//评价优先
    TextView tv_pingjia;
    @InjectView(R.id.tv_choose)//切换
    TextView tv_choose;
    @InjectView(R.id.et_search)//搜索输入内容
    EditText et_search;
    @InjectView(R.id.tv_search)//搜索
    TextView tv_search;
    @InjectView(R.id.iv_back)//返回
    ImageView iv_back;

    private StoreFragmentAdapter listviewAdapter;
    private List<Merchant> list;
    private MallDao mdao;
    private int PAGE_NUM=1;
    private PopupWindow popupWindow;
    private int choose=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_search_shop);
        ButterKnife.inject(this);
        // 隐藏输入法
       getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
        initData();
    }

    private void initView() {
        tv_pingjia.setOnClickListener(this);
        tv_xiaoliang.setOnClickListener(this);
        tv_choose.setOnClickListener(this);
        tv_search.setOnClickListener(this);
        iv_back.setOnClickListener(this);

    }

    private void initData() {
        tv_choose.setText("店铺");
        mdao=new MallDao(this);
        mdao.requestSearchCompanysOrProductByKeyword("0", Arad.preferences.getString("latitude"),Arad.preferences.getString("longitude"),
                "0",getIntent().getStringExtra("keyword"),Arad.preferences.getString("memberId"),String.valueOf(PAGE_NUM),String.valueOf(Constant.PAGE_SIZE)
                );
        list=new ArrayList<>();

        for(int i=0;i<20;i++){
            Merchant manchant = new Merchant();
            manchant.setName("章子怡"+i+"便利店");
            list.add(manchant);
        }

        listviewAdapter=new StoreFragmentAdapter(this);
        listview_shop.setAdapter(listviewAdapter);
        listview_shop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent_shop = new Intent(StoreSearchShopActivity.this, StoreShopListHorzitalStyleActivity.class);
                startActivity(intent_shop);
                AnimUtil.intentSlidIn(StoreSearchShopActivity.this);
            }
        });

        
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if(requestCode==6){
            MessageUtils.showShortToast(this,"根据关键字获取商铺成功");
            listviewAdapter.setList(mdao.getListMall());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_xiaoliang:
                mdao.requestSearchCompanysOrProductByKeyword("0", Arad.preferences.getString("latitude"),Arad.preferences.getString("longitude"),
                        "0",getIntent().getStringExtra("keyword"),Arad.preferences.getString("memberId"),String.valueOf(PAGE_NUM),String.valueOf(Constant.PAGE_SIZE)
                );
                break;
            case R.id.tv_pingjia:
                mdao.requestSearchCompanysOrProductByKeyword("1", Arad.preferences.getString("latitude"),Arad.preferences.getString("longitude"),
                        "0",getIntent().getStringExtra("keyword"),Arad.preferences.getString("memberId"),String.valueOf(PAGE_NUM),String.valueOf(Constant.PAGE_SIZE)
                );
                break;
            case R.id.tv_choose:
                showPopupWidow();
                break;
            case R.id.tv_search:
                if(TextUtils.isEmpty(et_search.getText().toString())){
                    MessageUtils.showShortToast(this,"搜索内容不能为空");
                    return;
                }
                if(choose==2){
                    mdao.requestSearchCompanysOrProductByKeyword("0", Arad.preferences.getString("latitude"), Arad.preferences.getString("longitude"),
                            "0", getIntent().getStringExtra("keyword"), Arad.preferences.getString("memberId"), String.valueOf(PAGE_NUM), String.valueOf(Constant.PAGE_SIZE));
                }
                if(choose==1){
                    Intent intent=new Intent(StoreSearchShopActivity.this,StoreSearchCommodityActivity.class);
                    intent.putExtra("keyword",et_search.getText().toString().trim());
                    startActivity(intent);
                    AnimUtil.intentSlidIn(StoreSearchShopActivity.this);
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
                choose=2;
                popupWindow.dismiss();
            }
        });
        tv_shangpin_pup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_choose.setText("商品");
                choose=1;
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
        //  popupWindow.setAnimationStyle(R.style.AnimBottom);
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
