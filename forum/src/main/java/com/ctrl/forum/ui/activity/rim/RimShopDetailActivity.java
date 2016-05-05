package com.ctrl.forum.ui.activity.rim;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.arad.widget.SlidingUpPanelLayout;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 店铺详情
 */
public class RimShopDetailActivity extends AppToolBarActivity implements View.OnClickListener{
    @InjectView(R.id.tv_name)
    TextView tv_name;     //店铺名
    @InjectView(R.id.rl_address)
    RelativeLayout rl_address;  //地址
    @InjectView(R.id.iv_phone)
    ImageView iv_phone;     //电话
    @InjectView(R.id.hot_pl)
    RelativeLayout hot_pl;  //热门评论
    @InjectView(R.id.tv_pl)
    TextView tv_pl;     //评论的总数
    @InjectView(R.id.tv_collect)
    TextView tv_collect;  //收藏按钮
    private View view;
    private PopupWindow popupWindow;
    private TextView bo_hao,call_up,cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rim_shop_detail);
        ButterKnife.inject(this);
        initView();
        initPop();
    }

    //����
    private void initPop() {
        view = LayoutInflater.from(this).inflate(R.layout.call_phone,null);
        popupWindow = new PopupWindow(view, SlidingUpPanelLayout.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.pop_bg));
        colorDrawable.setAlpha(40);
        popupWindow.setBackgroundDrawable(colorDrawable);
        popupWindow.setOutsideTouchable(true);

        bo_hao = (TextView) view.findViewById(R.id.bo_hao);
        call_up = (TextView) view.findViewById(R.id.call_up);
        cancel = (TextView) view.findViewById(R.id.cancel);
        bo_hao.setOnClickListener(this);
        call_up.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    private void initView() {
        tv_name.setOnClickListener(this);
        rl_address.setOnClickListener(this);
        iv_phone.setOnClickListener(this);
        hot_pl.setOnClickListener(this);
        tv_pl.setOnClickListener(this);
        tv_collect.setOnClickListener(this);
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
    public String setupToolBarTitle() {return getResources().getString(R.string.shop_name);}


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_address://地址
                break;
            case R.id.iv_phone://电话
                popupWindow.showAtLocation(this.view, Gravity.BOTTOM, 0, 0);  //��������
                popupWindow.update();
                break;
            case R.id.hot_pl://店铺评论
                startActivity(new Intent(this,RimStoreCommentActivity.class));
                break;
            case R.id.tv_collect://收藏
                
                break;
            case R.id.call_up: //打电话
                //����
                break;
            case R.id.cancel: //取消
                popupWindow.dismiss();
                break;
        }
    }
}
