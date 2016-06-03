package com.ctrl.forum.ui.activity.rim;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.widget.SlidingUpPanelLayout;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.dao.RimDao;
import com.ctrl.forum.entity.Data;
import com.ctrl.forum.entity.RimSeverCompanyDetail;

import java.util.List;

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
    @InjectView(R.id.tv_detail_address)
    TextView tv_detail_address;  //tv 地址
    @InjectView(R.id.iv_phone)
    ImageView iv_phone;     //电话
    @InjectView(R.id.hot_pl)
    RelativeLayout hot_pl;  //热门评论
    @InjectView(R.id.tv_pl)
    TextView tv_pl;     //评论的总数
    @InjectView(R.id.tv_collect)
    TextView tv_collect;  //收藏按钮
    @InjectView(R.id.tv_detail)
    TextView tv_detail;  //详细信息
    @InjectView(R.id.tv_phone)
    TextView tv_phone;  //电话
    @InjectView(R.id.iv_map)
    ImageView iv_map;

    private View view;
    private PopupWindow popupWindow;
    private TextView bo_hao,call_up,cancel;
    private Data data;
    private RimDao rimDao;
    private List<RimSeverCompanyDetail> rimSeverCompanyDetails;
    private String collecttionState;
    private String rimServiceCompaniesId,name,address,telephone,callTimes;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rim_shop_detail);
        ButterKnife.inject(this);
        intent = getIntent();

        initView();
        initPop();

        rimServiceCompaniesId = intent.getStringExtra("rimServiceCompaniesId");
        rimDao = new RimDao(this);
        rimDao.getAroundServiceCompany(rimServiceCompaniesId, Arad.preferences.getString("memberId"));

    }

    //初始化弹窗
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
        iv_map.setOnClickListener(this);

        //为控件赋值(未进行网络请求)
        name = intent.getStringExtra("name");
        address = intent.getStringExtra("address");
        telephone = intent.getStringExtra("telephone");
        callTimes = intent.getStringExtra("callTimes");
        tv_name.setText(name);
        tv_detail_address.setText(address);
        tv_phone.setText(intent.getStringExtra(telephone));
        callTimes = intent.getStringExtra("callTimes");
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
        Intent intent1 =null;
        switch (v.getId()){
            case R.id.rl_address://地址
                break;
            case R.id.iv_phone://电话
                if (!bo_hao.getText().equals("")){
                    popupWindow.showAtLocation(this.view, Gravity.BOTTOM, 0, 0);  //在底部
                    popupWindow.update();
                }
                break;
            case R.id.hot_pl://店铺评论
                intent1= new Intent(this,RimStoreCommentActivity.class);
                intent1.putExtra("rimServiceCompaniesId",rimServiceCompaniesId);
                startActivity(intent1);
                break;
            case R.id.tv_collect://收藏(店铺详情)
                if (collecttionState!=null){
                if(collecttionState.equals("0")){
                    rimDao.memberCollect(Arad.preferences.getString("memberId"),"3",rimServiceCompaniesId,"0");
                }else{
                    rimDao.memberCollect(Arad.preferences.getString("memberId"), "3", rimServiceCompaniesId, "1");}}
                break;
            case R.id.call_up: //打电话
                if (!bo_hao.getText().equals("")){
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + bo_hao.getText())));
                rimDao.addCallHistory(rimServiceCompaniesId, bo_hao.getText().toString(), Arad.preferences.getString("memberId"));
                popupWindow.dismiss();}
                break;
            case R.id.cancel: //取消
                popupWindow.dismiss();
                break;
            case R.id.iv_map://地图详情
                intent1 = new Intent(this,RimMapDetailActivity.class);
                if (rimSeverCompanyDetails!=null){
                    intent1.putExtra("name", name);
                    intent1.putExtra("rimServiceCompaniesId",rimServiceCompaniesId);
                    intent1.putExtra("address",address);
                    intent1.putExtra("telephone",telephone);
                    intent1.putExtra("callTimes",callTimes);
                    intent1.putExtra("latitude",rimSeverCompanyDetails.get(0).getLatitude());
                    intent1.putExtra("longitude",rimSeverCompanyDetails.get(0).getLongitude());}
                    startActivity(intent1);
                this.finish();
                break;
        }
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if (requestCode==5){
            rimSeverCompanyDetails = rimDao.getRimSeverCompanyDetails();
            data = rimDao.getData();
            Log.e("rimSeverCompanyDetails",rimSeverCompanyDetails.toString());
            if (rimSeverCompanyDetails!=null){
                name = rimSeverCompanyDetails.get(0).getName();
                address = rimSeverCompanyDetails.get(0).getAddress();
                telephone = rimSeverCompanyDetails.get(0).getTelephone();

                tv_name.setText(name);
                tv_detail_address.setText(address);
                tv_phone.setText(telephone);

                bo_hao.setText(rimSeverCompanyDetails.get(0).getTelephone());
                collecttionState = rimSeverCompanyDetails.get(0).getCollecttionState();
                collect(collecttionState);
            }
            if (data!=null){
                tv_pl.setText("("+data.getEvaluationCount()+")");
            }
        }
       /* if (requestCode==6){
            MessageUtils.showShortToast(getApplicationContext(),"收藏成功!");
                tv_collect.setText("取消收藏");
                collecttionState = "1";
        }*/
        if (requestCode==7){
            //MessageUtils.showShortToast(getApplicationContext(),"取消收藏成功!");
            rimDao.getAroundServiceCompany(rimServiceCompaniesId, Arad.preferences.getString("memberId"));
           // collect(collecttionState);
        }
    }

    //收藏--取消收藏
    public void collect(String collecttionState){
        switch (collecttionState){
            case "0":
                tv_collect.setText("收藏");
                break;
            default:
                tv_collect.setText("取消收藏");
                break;
        }
    }

}
