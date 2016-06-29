package com.ctrl.forum.ui.activity.store;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.AnimUtil;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.customview.ListViewForScrollView;
import com.ctrl.forum.dao.CollectDao;
import com.ctrl.forum.dao.MallDao;
import com.ctrl.forum.entity.CashCoupons;
import com.ctrl.forum.entity.Company;
import com.ctrl.forum.entity.CompanyUnion;
import com.ctrl.forum.entity.Qualification2;
import com.ctrl.forum.entity.ShopReply;
import com.ctrl.forum.ui.adapter.StoreLianMengGridViewAdapter;
import com.ctrl.forum.ui.adapter.StoreShopDetailPingLunListViewAdapter;
import com.ctrl.forum.ui.adapter.StoreXianjinQuanListViewAdapter;
import com.ctrl.forum.ui.adapter.StoreZiZhiGridViewAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/*
* 商城店铺详情 activity
* */

public class StoreShopDetailActivity extends AppToolBarActivity implements View.OnClickListener {
    @InjectView(R.id.lv_xianjinquan)//现金券
            ListViewForScrollView lv_xianjinquan;
    @InjectView(R.id.gridview_lianmeng)//联盟商家
            GridView gridview_lianmeng;
    @InjectView(R.id.gridview_zizhi)//资质
            GridView gridview_zizhi;
    @InjectView(R.id.lv_pinglun)//评论列表
            ListViewForScrollView lv_pinglun;
    @InjectView(R.id.iv_style_img)//店铺图片
            ImageView iv_style_img;
    @InjectView(R.id.tv_shop_name)//店铺名称
            TextView tv_shop_name;
    @InjectView(R.id.tv_time)//店铺营业时间
            TextView tv_time;
    @InjectView(R.id.tv_shop_detail_address)//店铺地址
            TextView tv_shop_detail_address;
    @InjectView(R.id.tv_shop_tel)//店铺电话
            TextView tv_shop_tel;
    @InjectView(R.id.tv_shop_notifcation)//店铺公告
            TextView tv_shop_notifcation;
    @InjectView(R.id.tv_shop_introduce)//店铺介绍
            TextView tv_shop_introduce;
    @InjectView(R.id.tv_xianjinquan_remark)//店铺现金券说明
            TextView tv_xianjinquan_remark;
    @InjectView(R.id.tv_reply_detail)//评价详情
            TextView tv_reply_detail;
    @InjectView(R.id.ratingBar)//店铺等级
            RatingBar ratingBar;

    private MallDao mdao;
    private List<Qualification2> listQualification;
    private List<CompanyUnion> listCompanyUnion;
    private List<ShopReply> listShopReply;
    private List<CashCoupons> listCashCoupons;
    private Company company;
    private String remark;
    private StoreXianjinQuanListViewAdapter mStoreXianjinQuanListViewAdapter;
    private StoreLianMengGridViewAdapter mStoreLianMengGridViewAdapter;
    private StoreShopDetailPingLunListViewAdapter mStoreShopDetailPingLunListViewAdapter;
    private StoreZiZhiGridViewAdapter mStoreZiZhiGridViewAdapter;

    private int count=0;//点击计数器
    private ImageView mRightButton;

    private Handler handler= new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    if(company.getCollectState().equals("0")){
                        mRightButton.setImageResource(R.mipmap.zan_red);
                    }
                    if(company.getCollectState().equals("1")){
                        mRightButton.setImageResource(R.mipmap.zan_red_shixin);
                    }
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_shop_detail);
        ButterKnife.inject(this);
        // 隐藏输入法
        // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
        initData();
    }

    private void initView() {
        count=0;
        tv_reply_detail.setOnClickListener(this);
    }

    private void initData() {
        mdao = new MallDao(this);
        showProgress(true);
        mdao.requestCompanysDetails(Arad.preferences.getString("memberId"), getIntent().getStringExtra("id"));
        mStoreXianjinQuanListViewAdapter = new StoreXianjinQuanListViewAdapter(this);
        mStoreLianMengGridViewAdapter = new StoreLianMengGridViewAdapter(this);
        mStoreZiZhiGridViewAdapter = new StoreZiZhiGridViewAdapter(this);
        mStoreShopDetailPingLunListViewAdapter = new StoreShopDetailPingLunListViewAdapter(this);
        lv_xianjinquan.setAdapter(mStoreXianjinQuanListViewAdapter);
        gridview_lianmeng.setAdapter(mStoreLianMengGridViewAdapter);
        gridview_zizhi.setAdapter(mStoreZiZhiGridViewAdapter);
        lv_pinglun.setAdapter(mStoreShopDetailPingLunListViewAdapter);
        lv_pinglun.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(StoreShopDetailActivity.this, StorePingjiaDetailActivity.class);
                intent.putExtra("id", company.getId());
                startActivity(intent);
                AnimUtil.intentSlidIn(StoreShopDetailActivity.this);
            }
        });

        gridview_lianmeng.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(StoreShopDetailActivity.this, StoreShopListVerticalStyleActivity.class);
                intent.putExtra("id", listCompanyUnion.get(position).getUnionId());
                startActivity(intent);
                AnimUtil.intentSlidIn(StoreShopDetailActivity.this);
            }
        });
    }

      @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
         if(requestCode==666){
             if (count % 2 == 0) {//奇数次点击
                 if (company.getCollectState().equals("0")) {
                     mRightButton.setImageResource(R.mipmap.zan_red_shixin);
                 }

                 if (company.getCollectState().equals("1")) {
                     mRightButton.setImageResource(R.mipmap.zan_red);
                 }
             }
             if(count%2==1){//偶数次点击
                 if (company.getCollectState().equals("0")) {
                     mRightButton.setImageResource(R.mipmap.zan_red);
                 }

                 if (company.getCollectState().equals("1")) {
                     mRightButton.setImageResource(R.mipmap.zan_red_shixin);

                 }
             }
             count++;
         }
        if (requestCode == 002) {
          //  MessageUtils.showShortToast(this, "获取店铺详情成功");
            showProgress(false);
            listQualification = mdao.getListQualification();
            listCompanyUnion = mdao.getListCompanyUnion();
            listShopReply = mdao.getListShopReply();
            listCashCoupons = mdao.getListCashCoupons();
            company = mdao.getCompany();
            remark = mdao.getRemark();
            initCompanyUnion();
            initQualification();
            mStoreZiZhiGridViewAdapter.setList(listQualification);
              mStoreXianjinQuanListViewAdapter.setList(listCashCoupons);
            mStoreLianMengGridViewAdapter.setList(listCompanyUnion);
            mStoreShopDetailPingLunListViewAdapter.setList(listShopReply);

            Arad.imageLoader.load(company.getImg()).placeholder(R.mipmap.default_error).into(iv_style_img);
            tv_shop_name.setText(company.getName());
            tv_time.setText("营业时间  " + company.getWorkStartTime() + "-" + company.getWorkEndTime());
            if(company.getEvaluatLevel()!=null) {
                ratingBar.setRating(Float.parseFloat(company.getEvaluatLevel()));
            }else {
                ratingBar.setRating(Float.parseFloat("0"));

            }
            tv_shop_detail_address.setText(company.getAddress());
            tv_shop_tel.setText(company.getMobile());
            tv_shop_notifcation.setText(company.getNotice());
            tv_shop_introduce.setText(company.getInformation());
            tv_xianjinquan_remark.setText(remark);
            handler.sendEmptyMessage(1);
        }
    }

    private void initQualification() {
        int size = listQualification.size();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int allWidth = (int) (110 * size * density);
        int itemWidth = (int) (90 * density);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                allWidth, LinearLayout.LayoutParams.FILL_PARENT);
        gridview_zizhi.setLayoutParams(params);
        gridview_zizhi.setColumnWidth(itemWidth);
        gridview_zizhi.setHorizontalSpacing(10);
        gridview_zizhi.setStretchMode(GridView.NO_STRETCH);
        gridview_zizhi.setNumColumns(size);
    }

    private void initCompanyUnion() {
        int size = listCompanyUnion.size();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int allWidth = (int) (110 * size * density);
        int itemWidth = (int) (90 * density);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                allWidth, LinearLayout.LayoutParams.FILL_PARENT);
        gridview_lianmeng.setLayoutParams(params);
        gridview_lianmeng.setColumnWidth(itemWidth);
        gridview_lianmeng.setHorizontalSpacing(10);
        gridview_lianmeng.setStretchMode(GridView.NO_STRETCH);
        gridview_lianmeng.setNumColumns(size);
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        showProgress(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_reply_detail:
                Intent intent = new Intent(StoreShopDetailActivity.this, StorePingjiaDetailActivity.class);
                intent.putExtra("id",company.getId());
                startActivity(intent);
                AnimUtil.intentSlidIn(StoreShopDetailActivity.this);
                break;
        }


    }


    @Override
    public String setupToolBarTitle() {
        TextView mTitle = getmTitle();
        mTitle.setTextColor(Color.BLACK);
        return getIntent().getStringExtra("name");
    }

    @Override
    public boolean setupToolBarLeftButton(ImageView leftButton) {
        leftButton.setImageResource(R.mipmap.jiantou_red);
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
       mRightButton=getmRightButton();
       // Button.setImageResource(R.mipmap.zan_red);
        mRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollectDao cdao = new CollectDao(StoreShopDetailActivity.this);
                if (count % 2 == 0) {//奇数次点击
                    if (company.getCollectState().equals("0")) {
                        cdao.requestMemberCollect(Arad.preferences.getString("memberId"),
                                "1",
                                "",
                                company.getId(),
                                "0"
                        );

                    }

                    if (company.getCollectState().equals("1")) {
                        cdao.requestMemberCollect(Arad.preferences.getString("memberId"),
                                "1",
                                "",
                                company.getId(),
                                "1"
                        );
                    }
                }
                if(count%2==1){//偶数次点击

                    if (company.getCollectState().equals("0")) {
                        cdao.requestMemberCollect(Arad.preferences.getString("memberId"),
                                "1",
                                "",
                                company.getId(),
                                "1"
                        );

                    }

                    if (company.getCollectState().equals("1")) {
                        cdao.requestMemberCollect(Arad.preferences.getString("memberId"),
                                "1",
                                "",
                                company.getId(),
                                "0"
                        );
                    }

                }



            }
        });
        return true;
}
}
