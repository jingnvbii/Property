package com.ctrl.forum.ui.activity.store;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.ui.adapter.CommdityImageViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/*
* 商城商品详情 activity
* */

public class StoreCommodityDetailActivity extends AppToolBarActivity implements View.OnClickListener{
    @InjectView(R.id.viewPager_commdity)//图片viewpager
    ViewPager viewPager_commdity;

    @InjectView(R.id.container)
    RelativeLayout container;

    @InjectView(R.id.tv_image_number)
    TextView tv_image_number;

    @InjectView(R.id.iv_back)//返回键
    ImageView iv_back;


    public static  String[] imageUrls = {"http://img.taodiantong.cn/v55183/infoimg/2013-07/130720115322ky.jpg",
            "http://pic30.nipic.com/20130626/8174275_085522448172_2.jpg",
            "http://pic18.nipic.com/20111215/577405_080531548148_2.jpg",
            "http://pic15.nipic.com/20110722/2912365_092519919000_2.jpg",
            "http://pic.58pic.com/58pic/12/64/27/55U58PICrdX.jpg"};

    private List<View> views = new ArrayList<View>();
    private LayoutInflater inflater;

    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    int position =(int) msg.obj+1;
                    Log.i("tag", "pisition----" + position);
                    tv_image_number.setText(position+"/"+imageUrls.length);
                    break;
            }

            return false;
        }
    });



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_commodity_detail);
        ButterKnife.inject(this);
        // 隐藏输入法
       // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
    }

    private void initView() {
        iv_back.setOnClickListener(this);
        inflater = getLayoutInflater();
        tv_image_number.setText(1+"/"+imageUrls.length);

        for(int i=0;i<imageUrls.length;i++){
            View view = inflater.inflate(R.layout.commdity_image_item, null);
            views.add(view);
        }

        /////////////////////主要配置//////////////////////////////////////

        // 1.设置幕后item的缓存数目
        viewPager_commdity.setOffscreenPageLimit(3);
        // 2.设置页与页之间的间距
        viewPager_commdity.setPageMargin(10);
        // 3.将父类的touch事件分发至viewPgaer，否则只能滑动中间的一个view对象


            ////////////////////////////////////////////////////////////////
        viewPager_commdity.setAdapter(new CommdityImageViewPagerAdapter(views)); // 为viewpager设置adapter


        container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return viewPager_commdity.dispatchTouchEvent(event);
            }
        });


        viewPager_commdity.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (viewPager_commdity != null) {
                    viewPager_commdity.invalidate();
                }
            }

            @Override
            public void onPageSelected(int position) {
                Message message=handler.obtainMessage();
                message.obj=position;
                message.what=1;
                handler.sendMessage(message);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });// 设置监听器
    }







    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                onBackPressed();
                break;
        }


    }


}
