package com.ctrl.forum.ui.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.dao.MallDao;
import com.ctrl.forum.dao.RemarkDao;
import com.ctrl.forum.entity.Banner;
import com.ctrl.forum.entity.Image2;
import com.ctrl.forum.entity.Product2;
import com.ctrl.forum.loopview.HomeAutoSwitchPicHolder;
import com.ctrl.forum.ui.activity.store.StoreManageAddressActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 积分商品详情
 */
public class MineIntegralStoreDetailActivity extends AppToolBarActivity implements View.OnClickListener{

    @InjectView(R.id.btn_sure)
    Button btn_sure;
    @InjectView(R.id.webView)
    WebView webView;
    @InjectView(R.id.banner)//图片viewpager
    FrameLayout banner;
    @InjectView(R.id.tv_mey)
    TextView tv_mey;
    @InjectView(R.id.integral)
    TextView integral;


   private String integralProductsId;
    private RemarkDao rdao;
    private MallDao mdao;
    private Product2 product2;
    private List<Image2> image2;
    private List<Banner> listBanner = new ArrayList<>();
    private ArrayList<String> mData;
    private HomeAutoSwitchPicHolder mAutoSwitchPicHolder;
    private static int ADDRESS = 1002;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral_store_detail);
        ButterKnife.inject(this);

        integralProductsId = getIntent().getStringExtra("integralProductsId");//商品id

        initView();
        //兑换成功后刷新积分
        Arad.preferences.getString("point");
    }

    private void initView() {
        rdao = new RemarkDao(this);
        mdao = new MallDao(this);
        mdao.requestProduct(integralProductsId, Arad.preferences.getString("memberId"), "1");//获取积分商品详情

        btn_sure.setOnClickListener(this);
        //inflater = getLayoutInflater();

    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if (requestCode == 4) {
            product2 = mdao.getProduct2();
            if (product2 != null) {
                //为控件赋值
                webView.loadDataWithBaseURL(null, product2.getInfomation(), "text/html", "utf-8", null);//单纯显示文字
                tv_mey.setText(product2.getOriginalPrice()+"元");
                integral.setText(product2.getNeedPoint()+"积分");
            }
            image2 = mdao.getListProductImg();
            if (image2 != null && image2.size() != 0) {
                if (image2.size()>1) {
                    for (int i = 0; i < image2.size(); i++) {
                        Banner banner = new Banner();
                        banner.setId(image2.get(i).getId());
                        banner.setImgUrl(image2.get(i).getImg());
                        banner.setTargetId(image2.get(i).getTargetId());
                        //banner.setType(rimImage.get(i).getType());
                        listBanner.add(banner);
                    }
                    banner.setVisibility(View.VISIBLE);
                    setLoopView();
                }else{
                    LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                    ImageView imageView = new ImageView(this);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    Arad.imageLoader.load(image2.get(0).getImg()).into(imageView);
                    banner.addView(imageView,params);
                }
            }else{
                banner.setVisibility(View.GONE);
            }
            if (requestCode == 1) {
                MessageUtils.showLongToast(this, "兑换积分成功");
                this.finish();
            }
        }
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
        banner.addView(autoPlayPicView);
        //4. 为轮播图设置数据
        mAutoSwitchPicHolder.setData(getData());
        mAutoSwitchPicHolder.setData(listBanner);
    }

    public List<String> getData() {
        mData = new ArrayList<String>();
        for(int i=0;i<image2.size();i++){
            mData.add(image2.get(i).getImg());
        }
        return mData;
    }

    @Override
    public void onClick(View v){
            switch (v.getId()) {
                case R.id.btn_sure:
                    Intent intent = new Intent(this, StoreManageAddressActivity.class);
                    intent.setFlags(888);
                    startActivityForResult(intent,RESULT_OK);
                    break;
            }
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==RESULT_OK){
            if (requestCode==ADDRESS){ //兑换积分
                String id = data.getStringExtra("province");

                //rdao.convertRemarkGoods(Arad.preferences.getString("memberId"), integralProductsId);
            }
        }else{
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
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
    public String setupToolBarTitle() {return "商品详情";}
}
