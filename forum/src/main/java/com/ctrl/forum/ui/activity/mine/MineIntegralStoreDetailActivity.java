package com.ctrl.forum.ui.activity.mine;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.dao.MallDao;
import com.ctrl.forum.dao.RemarkDao;
import com.ctrl.forum.entity.Image2;
import com.ctrl.forum.entity.Product2;
import com.ctrl.forum.ui.adapter.CommdityImageViewPagerAdapter;

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
    @InjectView(R.id.tv_image_number)
    TextView tv_image_number;
    @InjectView(R.id.viewPager_commdity)//图片viewpager
    ViewPager viewPager_commdity;
    @InjectView(R.id.name)
    TextView name;
    @InjectView(R.id.tv_mey)
    TextView tv_mey;
    @InjectView(R.id.integral)
    TextView integral;


   private String integralProductsId;
    private RemarkDao rdao;
    private MallDao mdao;
    private Product2 product2;
    private List<Image2> image2;
    private LayoutInflater inflater;
    private List<View> views = new ArrayList<View>();

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    int position = (int) msg.obj + 1;
                    tv_image_number.setText(position + "/" + image2.size());
                    break;
            }

            return false;
        }
    });

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
        inflater = getLayoutInflater();

        viewPager_commdity.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (viewPager_commdity != null) {
                    viewPager_commdity.invalidate();
                }
            }

            @Override
            public void onPageSelected(int position) {
                Message message = handler.obtainMessage();
                message.obj = position;
                message.what = 1;
                handler.sendMessage(message);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if (requestCode == 4) {
            product2 = mdao.getProduct2();
            if (product2 != null) {
                //为控件赋值
                webView.loadDataWithBaseURL(null, product2.getInfomation(), "text/html", "utf-8", null);//单纯显示文字

                name.setText(product2.getName());
                tv_mey.setText(product2.getSellingPrice());
                integral.setText(product2.getNeedPoint());
            }
            image2 = mdao.getListProductImg();
            if (image2 != null && image2.size() != 0) {
                //图片数量初始值
                tv_image_number.setText(1 + "/" + image2.size());
                for (int i = 0; i < image2.size(); i++) {
                        View view = inflater.inflate(R.layout.commdity_image_item, null);
                        views.add(view);
                }
                // 1.设置幕后item的缓存数目
                viewPager_commdity.setOffscreenPageLimit(image2.size());
                // 2.设置页与页之间的间距
                viewPager_commdity.setPageMargin(10);
                // 3.将父类的touch事件分发至viewPgaer，否则只能滑动中间的一个view对象
                viewPager_commdity.setAdapter(new CommdityImageViewPagerAdapter(views, image2)); // 为viewpager设置adapter

            }
        }
        if (requestCode == 1) {
            MessageUtils.showLongToast(this, "兑换积分成功");
            this.finish();
        }
    }

    @Override
    public void onClick(View v){
            switch (v.getId()) {
                case R.id.btn_sure:
                    rdao.convertRemarkGoods(Arad.preferences.getString("memberId"), integralProductsId);
                    break;
            }
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
