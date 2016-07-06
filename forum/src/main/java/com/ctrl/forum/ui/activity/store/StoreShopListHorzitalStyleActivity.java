package com.ctrl.forum.ui.activity.store;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarActivity;
import com.beanu.arad.utils.AnimUtil;
import com.ctrl.forum.R;
import com.ctrl.forum.base.MyApplication;
import com.ctrl.forum.cart.animutils.GoodsAnimUtil;
import com.ctrl.forum.cart.datasave.OperateGoodsDataBase;
import com.ctrl.forum.dao.MallDao;
import com.ctrl.forum.entity.Company;
import com.ctrl.forum.entity.ProductCategroy;
import com.ctrl.forum.ui.adapter.JasonViewPagerAdapter;
import com.ctrl.forum.ui.fragment.StoreShopListHorzitalStyleFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/*
* 商城定位 activity
* */

public class StoreShopListHorzitalStyleActivity extends ToolBarActivity implements View.OnClickListener{

    private RadioGroup myRadioGroup;
    @InjectView(R.id.lay_shop_horzital_style)
    LinearLayout layout;
    @InjectView(R.id.horizontalScrollView_shop_horzital_style)
    HorizontalScrollView mHorizontalScrollView;

    @InjectView(R.id.m_list_car_lay)//购物车布局
    RelativeLayout m_list_car_lay;
    @InjectView(R.id.m_list_car)//购物车图片
    ImageView m_list_car;
    @InjectView(R.id.m_list_num)//商品总数
    TextView m_list_num;
    @InjectView(R.id.m_list_all_price)//商品总价格
    TextView m_list_all_price;
    @InjectView(R.id.m_list_submit)//马上结算按钮
    Button m_list_submit;

    @InjectView(R.id.viewpager_shop)
    ViewPager mViewpager;

    @InjectView(R.id.ll_horzital_style)//公告布局
            LinearLayout ll_horzital_style;
    @InjectView(R.id.tv_horzital_style_information)//公告内容
            TextView tv_horzital_style_information;
    @InjectView(R.id.iv_horzital_style_information_close)//关闭公告
            ImageView iv_horzital_style_information_close;
    @InjectView(R.id.iv_style_img)//店铺图片
           ImageView iv_style_img;
    @InjectView(R.id.tv_shop_name)//店铺名称
            TextView tv_shop_name;
    @InjectView(R.id.tv_time)//营业时间
            TextView tv_time;
    @InjectView(R.id.ratingBar)//评价等级
    RatingBar ratingBar;

    @InjectView(R.id.et_horzital_style_search)//搜索输入
    EditText et_horzital_style_search;
    @InjectView(R.id.tv_horzital_style_search)//搜索
    TextView tv_horzital_style_search;

    public static int SELECTPOSITION = 0;//一级列表下标值

  //  SparseArray<Fragment> fragments = new SparseArray<Fragment>();
  List<Fragment> fragments=new ArrayList<>();

    private int width;
    private float mCurrentCheckedRadioLeft;//当前被选中的RadioButton距离左侧的距离
    private MallDao mdao;
    private Company company;
    private List<ProductCategroy> listProductCategroy;
    private OperateGoodsDataBase mGoodsDataBaseInterface;
    private StoreShopListHorzitalStyleActivity mContext;
    private JasonViewPagerAdapter viewPagerAdapter;



    //手指上下滑动时的最小速度
    private static final int YSPEED_MIN = 1000;

    //手指向右滑动时的最小距离
    private static final int XDISTANCE_MIN = 50;

    //手指向上滑或下滑时的最小距离
    private static final int YDISTANCE_MIN = 100;

    //记录手指按下时的横坐标。
    private float xDown;

    //记录手指按下时的纵坐标。
    private float yDown;

    //记录手指移动时的横坐标。
    private float xMove;

    //记录手指移动时的纵坐标。
    private float yMove;

    //用于计算手指滑动的速度。
    private VelocityTracker mVelocityTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_shop_list_horzital_style);
        ButterKnife.inject(this);
        mContext=this;
        // 隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initData();
        MyApplication.getInstance().addActivity(this);

    }




    private void initData() {
        mGoodsDataBaseInterface = OperateGoodsDataBase.getInstance();
        //清空数据库缓存
        mGoodsDataBaseInterface.deleteAll(mContext);

        Arad.imageLoader.load(getIntent().getStringExtra("url")).placeholder(R.mipmap.default_error).into(iv_style_img);

        mdao = new MallDao(this);
        showProgress(true);
        mdao.requestProductCategroy(getIntent().getStringExtra("id"),"1");
        mdao.requestCompanysDetails(Arad.preferences.getString("memberId"), getIntent().getStringExtra("id"));
    }





    private void initView() {
        tv_horzital_style_search.setOnClickListener(this);
        et_horzital_style_search.addTextChangedListener(watcher);
        iv_horzital_style_information_close.setOnClickListener(this);
     //   m_list_car_lay.setOnClickListener(this);
     //  m_list_submit.setOnClickListener(this);
        width = getResources().getDisplayMetrics().widthPixels;
        myRadioGroup = new RadioGroup(this);
        myRadioGroup.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        myRadioGroup.setOrientation(LinearLayout.HORIZONTAL);
        layout.addView(myRadioGroup);
        for (int i = 0; i <listProductCategroy.size(); i++) {
            RadioButton radio = new RadioButton(this);
            radio.setBackgroundResource(R.drawable.top_category_selector);
            LinearLayout.LayoutParams l;
            if(listProductCategroy.size()==1){
                l = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
            }else if(listProductCategroy.size()==2){
                l = new LinearLayout.LayoutParams(width/2, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
            }else if(listProductCategroy.size()==3){
                l = new LinearLayout.LayoutParams(width/3, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
            }else if(listProductCategroy.size()==4){
                l = new LinearLayout.LayoutParams(width/4, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
            }else {
            l = new LinearLayout.LayoutParams(width/5, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
             }
            radio.setLayoutParams(l);
            radio.setGravity(Gravity.CENTER);
            //  radio.setPadding(20, 20, 20, 20);
            radio.setId(i);
            radio.setButtonDrawable(getResources().getDrawable(R.color.white));
            radio.setTextSize(14.0f);
            radio.setTextColor(getResources().getColor(R.color.text_gray));//选择器
            radio.setText(listProductCategroy.get(i).getName());//动态设置
            radio.setSingleLine(true);
            radio.setEllipsize(TextUtils.TruncateAt.END);
            radio.setTag(i);
            if (i == 0) {
                radio.setChecked(true);
                radio.setTextColor(getResources().getColor(R.color.text_red));
            }
            radio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        buttonView.setTextColor(getResources().getColor(R.color.text_red));
                    }else {
                        buttonView.setTextColor(getResources().getColor(R.color.text_gray));
                    }
                }
            });

            View view = new View(this);
            LinearLayout.LayoutParams v = new LinearLayout.LayoutParams(1, ViewGroup.LayoutParams.MATCH_PARENT);
            view.setLayoutParams(v);
              view.setBackgroundColor(Color.parseColor("#cccccc"));
            myRadioGroup.addView(radio);
             myRadioGroup.addView(view);
            StoreShopListHorzitalStyleFragment fragment = StoreShopListHorzitalStyleFragment.newInstance(listProductCategroy,i,
                    m_list_car,m_list_all_price,m_list_submit,m_list_num);
            fragments.add(fragment);
        }
        myRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //Map<String, Object> map = (Map<String, Object>) group.getChildAt(checkedId).getTag();
                int radioButtonId = group.getCheckedRadioButtonId();
                //根据ID获取RadioButton的实例
                RadioButton rb = (RadioButton) findViewById(radioButtonId);
                mViewpager.setCurrentItem(radioButtonId);//让下方ViewPager跟随上面的HorizontalScrollView切换
                mCurrentCheckedRadioLeft = rb.getLeft();
                mHorizontalScrollView.smoothScrollTo((int) mCurrentCheckedRadioLeft - (width * 2) / 5, 0);
            }
        });
        viewPagerAdapter = new JasonViewPagerAdapter(getSupportFragmentManager(), fragments);
        mViewpager.setAdapter(viewPagerAdapter);
        mViewpager.setOnPageChangeListener(new FragmentOnPageChangeListener());
        mViewpager.setCurrentItem(0);

            viewPagerAdapter.setOnReloadListener(new JasonViewPagerAdapter.OnReloadListener() {
                @Override
                public void onReload() {
                    fragments = null;
                    List<Fragment> list = new ArrayList<Fragment>();
                    for (int i = 0; i < listProductCategroy.size(); i++) {
                        list.add(StoreShopListHorzitalStyleFragment.newInstance(listProductCategroy, i,
                                m_list_car, m_list_all_price, m_list_submit, m_list_num));
                    }
                    viewPagerAdapter.setPagerItems(list);
                }
            });
            mViewpager.setOffscreenPageLimit(listProductCategroy.size());
    }

    private TextWatcher watcher = new TextWatcher() {
        //文字变化时
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            if(s.length()==0){
                tv_horzital_style_search.setVisibility(View.GONE);
            }else {
                tv_horzital_style_search.setVisibility(View.VISIBLE);
            }


        }
        //文字变化前

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
            if(s.length()==0){
                tv_horzital_style_search.setVisibility(View.GONE);
            }else {
                tv_horzital_style_search.setVisibility(View.VISIBLE);
            }

        }

        //文字变化后
        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            if (s.length() == 0&&tv_horzital_style_search.getVisibility()==View.VISIBLE) {
                tv_horzital_style_search.setVisibility(View.GONE);
            }
        }
    };

    public JasonViewPagerAdapter getAdapter(){
        return viewPagerAdapter;
    }
    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if (requestCode == 002) {
            //  MessageUtils.showShortToast(this, "获取店铺详情成功");
            showProgress(false);
            company = mdao.getCompany();
            tv_horzital_style_information.setText(company.getNotice());
            tv_horzital_style_information.requestFocus();
            tv_shop_name.setText(company.getName());
            if(company.getWorkStartTime()!=null&&company.getWorkEndTime()!=null) {
                tv_time.setText("营业时间 " + company.getWorkStartTime()+ "-" + company.getWorkEndTime());
            }else {
                tv_time.setVisibility(View.GONE);
            }
            if(company.getEvaluatLevel()!=null) {
                ratingBar.setRating(Float.parseFloat(company.getEvaluatLevel()) );
            }else {
                ratingBar.setRating(Float.parseFloat("0"));
            }
        }

       if(requestCode==9){
           listProductCategroy=mdao.getListProductCategroy();
           initView();
       }
    }

    public RelativeLayout getRel(){
        return m_list_car_lay;
    }
    public void setAnim(){
        GoodsAnimUtil.setOnEndAnimListener(new onEndAnim());
    }

    /**
     * ViewPager的PageChangeListener(页面改变的监听器)
     */
    private class FragmentOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub
        }
        /**
         * 滑动ViewPager的时候,让上方的HorizontalScrollView自动切换
         */
        @Override
        public void onPageSelected(int position) {
            // TODO Auto-generated method stub
            RadioButton radioButton = (RadioButton) findViewById(position);
            radioButton.performClick();

        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(!et_horzital_style_search.getText().toString().equals("")){
            et_horzital_style_search.setText("");
            tv_horzital_style_search.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_horzital_style_search:
                Intent intent=new Intent(this,StoreSearchCommodityActivity.class);
                intent.putExtra("keyword", et_horzital_style_search.getText().toString().trim());
                intent.putExtra("companyId", getIntent().getStringExtra("id"));
                intent.addFlags(11);
                startActivity(intent);
                AnimUtil.intentSlidIn(this);
                break;
            case R.id.iv_horzital_style_information_close:
                ll_horzital_style.setVisibility(View.GONE);
                break;
        }
    }




    /**
     * 动画结束后，更新所有数量和所有价格
     */
    class onEndAnim implements GoodsAnimUtil.OnEndAnimListener {
        @Override
        public void onEndAnim() {
            setAll();
        }
    }

    /**
     * 点击加号和减号的时候设置总数和总价格
     */
    public void setAll() {

        //设置所有购物数量
        if (mGoodsDataBaseInterface.getSecondGoodsNumberAll(mContext, SELECTPOSITION) == 0) {
            m_list_car.setImageResource(R.mipmap.cart_car_gray);
            m_list_submit.setBackgroundResource(R.color.text_gray);
            m_list_num.setVisibility(View.GONE);
            m_list_all_price.setText("共￥0 元");
            m_list_num.setText("0");
        } else {
            m_list_car.setImageResource(R.mipmap.cart_car_orange);
            m_list_submit.setBackgroundResource(R.color.text_red);
            m_list_all_price.setText("共￥" + String.valueOf(mGoodsDataBaseInterface.getSecondGoodsPriceAll(mContext, SELECTPOSITION))+" 元");
            m_list_num.setText(mGoodsDataBaseInterface.getSecondGoodsNumberAll(mContext, SELECTPOSITION) + "");
            m_list_num.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        createVelocityTracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = event.getRawX();
                yDown = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if(mViewpager.getCurrentItem()==0) {
                    xMove = event.getRawX();
                    yMove = event.getRawY();
                    //滑动的距离
                    int distanceX = (int) (xMove - xDown);
                    int distanceY = (int) (yMove - yDown);
                    //获取顺时速度
                    int ySpeed = getScrollVelocity();
                    //关闭Activity需满足以下条件：
                    //1.x轴滑动的距离>XDISTANCE_MIN
                    //2.y轴滑动的距离在YDISTANCE_MIN范围内
                    //3.y轴上（即上下滑动的速度）<XSPEED_MIN，如果大于，则认为用户意图是在上下滑动而非左滑结束Activity
                    if (distanceX > XDISTANCE_MIN && (distanceY < YDISTANCE_MIN && distanceY > -YDISTANCE_MIN) && ySpeed < YSPEED_MIN) {
                        finish();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                recycleVelocityTracker();
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * 创建VelocityTracker对象，并将触摸界面的滑动事件加入到VelocityTracker当中。
     *
     * @param event
     *
     */
    private void createVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 回收VelocityTracker对象。
     */
    private void recycleVelocityTracker() {
        mVelocityTracker.recycle();
        mVelocityTracker = null;
    }

    /**
     *
     * @return 滑动速度，以每秒钟移动了多少像素值为单位。
     */
    private int getScrollVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getYVelocity();
        return Math.abs(velocity);
    }



    @Override
    public String setupToolBarTitle() {
        return getIntent().getStringExtra("name");
    }

    @Override
    public boolean setupToolBarLeftButton(ImageView leftButton) {
        leftButton.setImageResource(R.mipmap.jiantou_left_white);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        return true;
    }

    @Override
    public boolean setupToolBarRightText(TextView mRightText) {
        mRightText.setText("店铺详情");
        mRightText.setTextColor(Color.WHITE);
        mRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StoreShopListHorzitalStyleActivity.this,StoreShopDetailActivity.class);
                intent.putExtra("id",getIntent().getStringExtra("id"));
                intent.putExtra("name",getIntent().getStringExtra("name"));
                startActivity(intent);
                AnimUtil.intentSlidIn(StoreShopListHorzitalStyleActivity.this);
            }
        });
        return true;
    }
}
