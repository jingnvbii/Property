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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.widget.SlidingUpPanelLayout;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.dao.InvitationDao;
import com.ctrl.forum.dao.RimDao;
import com.ctrl.forum.entity.Banner;
import com.ctrl.forum.entity.Data;
import com.ctrl.forum.entity.RimImage;
import com.ctrl.forum.entity.RimSeverCompanyDetail;
import com.ctrl.forum.loopview.HomeAutoSwitchPicHolder;

import java.util.ArrayList;
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
    @InjectView(R.id.iv_shop_pic)
    FrameLayout iv_shop_pic;

    private View view;
    private PopupWindow popupWindow;
    private TextView bo_hao,call_up,cancel;
    private Data data;
    private RimDao rimDao;
    private List<RimSeverCompanyDetail> rimSeverCompanyDetails;
    static String collecttionState;
    private String rimServiceCompaniesId,name,address,telephone,callTimes;
    private Intent intent;

    private HomeAutoSwitchPicHolder mAutoSwitchPicHolder;

    private ArrayList<String> mData;
    private List<Banner> listBanner = new ArrayList<>();
    private List<RimImage> rimImage;
    private InvitationDao idao;

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

    /**
     * 轮播图
     */
    private void setLoopView() {
        // 1.创建轮播的holder
        mAutoSwitchPicHolder = new HomeAutoSwitchPicHolder(this);
        // 2.得到轮播图的视图view
        View autoPlayPicView = mAutoSwitchPicHolder.getRootView();
        // 把轮播图的视图添加到主界面中
        iv_shop_pic.addView(autoPlayPicView);
        //4. 为轮播图设置数据
        mAutoSwitchPicHolder.setData(getData());
        mAutoSwitchPicHolder.setData(listBanner);
    }

    public List<String> getData() {
        mData = new ArrayList<String>();
        for(int i=0;i<rimImage.size();i++){
            mData.add(rimImage.get(i).getImg());
        }
        return mData;
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
            if (rimSeverCompanyDetails!=null){
                Log.e("rimSeverCompanyDetails",rimSeverCompanyDetails.toString());
                name = rimSeverCompanyDetails.get(0).getName();
                address = rimSeverCompanyDetails.get(0).getAddress();
                telephone = rimSeverCompanyDetails.get(0).getTelephone();

                tv_name.setText(name);
                tv_detail_address.setText(address);
                tv_phone.setText(telephone);
                tv_detail.setText(rimSeverCompanyDetails.get(0).getDetailInfo());

                //图片轮播图设置数据
                //listBanner = rimSeverCompanyDetails.get(0).getImgList();
                rimImage = rimSeverCompanyDetails.get(0).getImgList();
                if (rimImage!=null && rimImage.size()!=0) {
                    for (int i = 0; i < rimImage.size(); i++) {
                        Banner banner = new Banner();
                        banner.setId(rimImage.get(i).getId());
                        banner.setImgUrl(rimImage.get(i).getImg());
                        banner.setTargetId(rimImage.get(i).getTargetId());
                        //banner.setType(rimImage.get(i).getType());
                        listBanner.add(banner);
                    }
                    iv_shop_pic.setVisibility(View.VISIBLE);
                    setLoopView();
                }else{  //轮播图没图片状态
                   /* ImageView iv = new ImageView(this);
                    iv.setScaleType(ImageView.ScaleType.FIT_XY);
                    if (rimSeverCompanyDetails.get(0).getImg()!=null && !rimSeverCompanyDetails.get(0).getImg().equals(""))
                    Arad.imageLoader.load(rimSeverCompanyDetails.get(0).getImg()).into(iv);
                    iv_shop_pic.addView(iv);*/
                    iv_shop_pic.setVisibility(View.GONE);
                }

                bo_hao.setText(rimSeverCompanyDetails.get(0).getTelephone());
                collecttionState = rimSeverCompanyDetails.get(0).getCollecttionState();
                collect(collecttionState);
            }
            if (data!=null){
                tv_pl.setText("(" + data.getEvaluationCount() + ")");
            }
        }
        if (requestCode==7){
            rimSeverCompanyDetails.clear();
            rimDao.getAroundServiceCompany(rimServiceCompaniesId, Arad.preferences.getString("memberId"));
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

    @Override
    protected void onRestart() {
        super.onRestart();
        if (rimSeverCompanyDetails!=null){
            rimSeverCompanyDetails.clear();
        }
        rimDao.getAroundServiceCompany(rimServiceCompaniesId, Arad.preferences.getString("memberId"));
    }
}
