package com.ctrl.forum.ui.activity.store;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.AnimUtil;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.cart.animutils.GoodsAnimUtil;
import com.ctrl.forum.cart.datasave.GoodsBean;
import com.ctrl.forum.cart.datasave.GoodsDataBaseInterface;
import com.ctrl.forum.cart.datasave.OperateGoodsDataBase;
import com.ctrl.forum.cart.datasave.OperateGoodsDataBaseStatic;
import com.ctrl.forum.customview.ShareDialog;
import com.ctrl.forum.dao.CollectDao;
import com.ctrl.forum.dao.MallDao;
import com.ctrl.forum.entity.Image2;
import com.ctrl.forum.entity.Product2;
import com.ctrl.forum.ui.adapter.CartPopupWindowListViewAdapter;
import com.ctrl.forum.ui.adapter.CommdityImageViewPagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.system.email.Email;
import cn.sharesdk.system.text.ShortMessage;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.weibo.TencentWeibo;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/*
* 商城商品详情 activity
* */

public class StoreCommodityDetailActivity extends AppToolBarActivity implements View.OnClickListener ,PlatformActionListener{
    @InjectView(R.id.viewPager_commdity)//图片viewpager
            ViewPager viewPager_commdity;

    @InjectView(R.id.container)
    RelativeLayout container;

    @InjectView(R.id.webview_commdity)//webview
            WebView webview_commdity;

    @InjectView(R.id.tv_image_number)
    TextView tv_image_number;
    @InjectView(R.id.tv_commdity_name)
    TextView tv_commdity_name;
    @InjectView(R.id.tv_commdity_price)
    TextView tv_commdity_price;
    @InjectView(R.id.tv_beizhu)
    TextView tv_beizhu;
    @InjectView(R.id.tv_product_number)
    TextView tv_product_number;

    @InjectView(R.id.iv_back)//返回键
            ImageView iv_back;
    @InjectView(R.id.iv_zan)//收藏商品
            ImageView iv_zan;
    @InjectView(R.id.iv_share)//分享商品
            ImageView iv_share;

    @InjectView(R.id.m_list_car_lay)//购物车布局
            RelativeLayout m_list_car_lay;
    @InjectView(R.id.m_list_all_price)//价格总和
            TextView m_list_all_price;
    @InjectView(R.id.m_list_num)//商品数量总和
            TextView m_list_num;
    @InjectView(R.id.m_list_car)//购物车图片
            ImageView m_list_car;
    @InjectView(R.id.m_list_submit)//马上结算
            Button m_list_submit;


    @InjectView(R.id.tv_add_cart)//加入购物车
            TextView tv_add_cart;
    @InjectView(R.id.rl_commodity_detail_add_cart)//加入购物车布局
            RelativeLayout rl_commodity_detail_add_cart;
    @InjectView(R.id.iv_commodity_detail_add)//加号
            ImageView iv_commodity_detail_add;
    @InjectView(R.id.iv_commodity_detail_sub)//减号
            ImageView iv_commodity_detail_sub;
    @InjectView(R.id.tv_commodity_detail_num)//数量
            TextView tv_commodity_detail_num;

    /**
     * 购物车布局
     */
   /* @InjectView(R.id.m_list_car_lay)
    RelativeLayout mCarLay;*/

   /* @InjectView(R.id.m_list_car_vertical_style)//购物车图片
            ImageView m_list_car_vertical_style;
    @InjectView(R.id.m_list_submit_vertical_style)//提交订单按钮
            Button m_list_submit_vertical_style;*/


    private List<View> views = new ArrayList<View>();
    private LayoutInflater inflater;
    private int count = 0;

    private Handler  handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    int position = (int) msg.obj + 1;
                    tv_image_number.setText(position + "/" + listProductImg.size());
                    break;
            }

            return false;
        }
    });
    private MallDao mdao;
    private Product2 product;
    private List<Image2> listProductImg;
    private WebSettings settings;
    private ArrayList<String> listNameStr;
    private ArrayList<GoodsBean> listGoodsBean;
    private PopupWindow popupWindow;
    private TextView m_list_num_popup;
    private TextView m_list_all_price_popup;
    private GoodsDataBaseInterface mGoodsDataBaseInterface;
    private StoreCommodityDetailActivity mContext;
    private int SELECTPOSITION = 0;
    private Button m_list_submit_popup;
    private PopupWindow popupWindow_share;
    private ShareDialog shareDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_commodity_detail);
        ButterKnife.inject(this);
        ShareSDK.initSDK(this);
        mContext = this;
        // 隐藏输入法
        // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
        initData();
    }

    private void initData() {
        mdao = new MallDao(this);
        showProgress(true);
        mdao.requestProduct(getIntent().getStringExtra("id"), Arad.preferences.getString("memberId"), "0");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK();
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        showProgress(false);
        if (requestCode == 666) {
           // MessageUtils.showShortToast(this, "商品藏成功");
            if (count % 2 == 0) {//奇数次点击
                if (product.getCollectState().equals("0")) {
                    iv_zan.setImageResource(R.mipmap.shoucang_white);
                }
                if (product.getCollectState().equals("1")) {
                    iv_zan.setImageResource(R.mipmap.zan_white);
                }
            }
            if (count % 2 == 1) {//偶数次点击

                if (product.getCollectState().equals("0")) {
                    iv_zan.setImageResource(R.mipmap.zan_white);
                }

                if (product.getCollectState().equals("1")) {
                    iv_zan.setImageResource(R.mipmap.shoucang_white);

                }
            }
            count++;

        }
        if (requestCode == 4) {
          //  MessageUtils.showShortToast(this, "获取商品成功");
            product = mdao.getProduct2();
            listProductImg = mdao.getListProductImg();
            tv_commdity_name.setText(product.getName());
            tv_commdity_price.setText("￥" + product.getSellingPrice());
            if (product.getDeliveryType().equals("0")) {
                tv_beizhu.setText("自己配送");
            }
            if (product.getDeliveryType().equals("1")) {
                tv_beizhu.setText("第三方配送");
            }
            if(product.getSalesVolume()==null||product.getSalesVolume().equals("")){
                tv_product_number.setText("销量 ：0 " );
            }else {
                tv_product_number.setText("销量 ： " + product.getSalesVolume());
            }

            if(product.getCollectState().equals("0")){
                iv_zan.setImageResource(R.mipmap.zan_white);
            }
            if(product.getCollectState().equals("1")){
                iv_zan.setImageResource(R.mipmap.shoucang_white);
            }


            //图片数量初始值
            tv_image_number.setText(1 + "/" + listProductImg.size());
            for (int i = 0; i < listProductImg.size(); i++) {
                View view = inflater.inflate(R.layout.commdity_image_item, null);
                views.add(view);
            }

            // 1.设置幕后item的缓存数目
            viewPager_commdity.setOffscreenPageLimit(listProductImg.size());
            // 2.设置页与页之间的间距
            viewPager_commdity.setPageMargin(10);
            // 3.将父类的touch事件分发至viewPgaer，否则只能滑动中间的一个view对象


            ////////////////////////////////////////////////////////////////
            viewPager_commdity.setAdapter(new CommdityImageViewPagerAdapter(views, listProductImg)); // 为viewpager设置adapter

            /*
            * 加载富文本
            * */
            loadRichText();

              /*
        * 购物车数据初始化
        * */
            setCartData();

        }
    }

    private void setCartData() {
        /** 获取存储的商品数量 */
        if (mGoodsDataBaseInterface.getSecondGoodsNumber(mContext, SELECTPOSITION, product.getId()) == 0) {
            tv_add_cart.setVisibility(View.VISIBLE);
            rl_commodity_detail_add_cart.setVisibility(View.GONE);
        } else {
            tv_add_cart.setVisibility(View.GONE);
            rl_commodity_detail_add_cart.setVisibility(View.VISIBLE);
            tv_commodity_detail_num.setText("" + mGoodsDataBaseInterface.getSecondGoodsNumber(mContext, SELECTPOSITION, product.getId()));
        }

        m_list_all_price.setText("共￥" + String.valueOf(mGoodsDataBaseInterface.getSecondGoodsPriceAll(mContext, SELECTPOSITION)) + " 元");
        m_list_num.setText(mGoodsDataBaseInterface.getSecondGoodsNumberAll(mContext, SELECTPOSITION) + "");
        setAll();
    }

    /*
   * 加载富文本
   * */
    private void loadRichText() {
        //自适应屏幕
        settings = webview_commdity.getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        if (mDensity == 120) {
            settings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        } else if (mDensity == 160) {
            settings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else if (mDensity == 240) {
            settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        }
        webview_commdity.getSettings().setDefaultTextEncodingName("UTF -8");
        webview_commdity.loadDataWithBaseURL(null, product.getInfomation(), "text/html", "UTF-8", null);
    }

    private void initView() {
        mGoodsDataBaseInterface = OperateGoodsDataBase.getInstance();
        m_list_car_lay.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        iv_zan.setOnClickListener(this);
        iv_share.setOnClickListener(this);
        iv_commodity_detail_add.setOnClickListener(this);
        iv_commodity_detail_sub.setOnClickListener(this);
        tv_add_cart.setOnClickListener(this);
        m_list_submit.setOnClickListener(this);

        inflater = getLayoutInflater();


        /////////////////////主要配置//////////////////////////////////////
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
                Message message = handler.obtainMessage();
                message.obj = position;
                message.what = 1;
                handler.sendMessage(message);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });// 设置监听器
    }


    private void showCartPopupWindow(View v) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.popupwindow_cart_vertical_style, null);

        popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

        final ListView lv_cart_popup = (ListView) contentView.findViewById(R.id.lv_cart_popup);
        m_list_num_popup = (TextView) contentView.findViewById(R.id.m_list_num_popup);
        m_list_all_price_popup = (TextView) contentView.findViewById(R.id.m_list_all_price_popup);
        m_list_submit_popup = (Button) contentView.findViewById(R.id.m_list_submit_popup);
        TextView tv_cart_popup_delete = (TextView) contentView.findViewById(R.id.tv_cart_popup_delete);
        // 设置SelectPicPopupWindow的View
        popupWindow.setContentView(contentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        popupWindow.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        popupWindow.setAnimationStyle(R.style.AnimBottom);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(dw);
        m_list_submit_popup.setOnClickListener(this);
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
        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
        m_list_num_popup.setText(mGoodsDataBaseInterface.getSecondGoodsNumberAll(mContext, SELECTPOSITION) + "");
        m_list_all_price_popup.setText("共￥" + mGoodsDataBaseInterface.getSecondGoodsPriceAll(mContext, SELECTPOSITION) + "元");
        listGoodsBean = OperateGoodsDataBaseStatic.getSecondGoodsTypeList(mContext);

        final CartPopupWindowListViewAdapter mCartPopupWindowListViewAdapter = new CartPopupWindowListViewAdapter(this);
        mCartPopupWindowListViewAdapter.setList(listGoodsBean);
        lv_cart_popup.setAdapter(mCartPopupWindowListViewAdapter);

        for(int i=0;i<listGoodsBean.size();i++){
            if(listGoodsBean.get(i).getGoodsnum().equals("0")){
                listGoodsBean.remove(i);
                mCartPopupWindowListViewAdapter.setList(listGoodsBean);
            }
        }


        tv_cart_popup_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog();

            }
        });

        mCartPopupWindowListViewAdapter.setOnItemClickListener(new CartPopupWindowListViewAdapter.OnItemClickListener() {
            @Override
            public void onItemJiaClick(CartPopupWindowListViewAdapter.ViewHolder v) {
                String nums = v.tv_popup_lv_number.getText().toString().trim();
                if ((Integer.parseInt(nums) + 1) > Integer.parseInt(listGoodsBean.get(v.getPosition()).getStock())) {
                    MessageUtils.showShortToast(StoreCommodityDetailActivity.this, "库存不足");
                    return;
                }
                v.tv_popup_lv_number.setText(mGoodsDataBaseInterface.saveGoodsNumber(mContext, SELECTPOSITION, listGoodsBean.get(v.getPosition()).getGoodsid(),
                        String.valueOf(Integer.parseInt(nums) + 1),
                        listGoodsBean.get(v.getPosition()).getGoodsprice(),
                        listGoodsBean.get(v.getPosition()).getGoodsname(),
                        listGoodsBean.get(v.getPosition()).getStock()) + "");
                tv_commodity_detail_num.setText(mGoodsDataBaseInterface.saveGoodsNumber(mContext, SELECTPOSITION,
                        product.getId(),
                        String.valueOf(Integer.parseInt(nums) + 1),
                        Float.parseFloat(product.getSellingPrice()),
                        product.getName(),
                        product.getStock()

                ) + "");
                setPupupAll();
            }

            @Override
            public void onItemJianClick(CartPopupWindowListViewAdapter.ViewHolder v) {
                String nums = v.tv_popup_lv_number.getText().toString().trim();
                v.tv_popup_lv_number.setText(mGoodsDataBaseInterface.saveGoodsNumber(mContext, SELECTPOSITION,
                        listGoodsBean.get(v.getPosition()).getGoodsid(),
                        String.valueOf(Integer.parseInt(nums) - 1),
                        listGoodsBean.get(v.getPosition()).getGoodsprice(),
                        listGoodsBean.get(v.getPosition()).getGoodsname(),
                        listGoodsBean.get(v.getPosition()).getStock()

                ) + "");

                tv_commodity_detail_num.setText(mGoodsDataBaseInterface.saveGoodsNumber(mContext, SELECTPOSITION,
                        product.getId(),
                        String.valueOf(Integer.parseInt(nums) - 1),
                        Float.parseFloat(product.getSellingPrice()),
                        product.getName(),
                        product.getStock()

                ) + "");
                nums = v.tv_popup_lv_number.getText().toString().trim();
                // 减完之后  数据为0
                if (nums.equals("0")) {
                    listGoodsBean.remove(v.getPosition());
                    mCartPopupWindowListViewAdapter.setList(listGoodsBean);
                }
                setPupupAll();
              //  setAll();

            }
        });

    }

    private void showDeleteDialog() {
        new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("是否清空购物车内所有商品？")
                .setNegativeButton("取消", null)
                .setPositiveButton("清空", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mGoodsDataBaseInterface.deleteAll(mContext);
                        //   foodAdapter.notifyDataSetChanged();
                        setAll();
                        popupWindow.dismiss();
                    }
                }).show();


    }

    /**
     * 点击加号和减号的时候设置总数和总价格
     */
    private void setAll() {

        //设置所有购物数量
        if (mGoodsDataBaseInterface.getSecondGoodsNumberAll(mContext, SELECTPOSITION) == 0) {
            m_list_car.setImageResource(R.mipmap.cart_car_gray);
            m_list_submit.setBackgroundResource(R.color.text_gray);
            m_list_num.setVisibility(View.GONE);
            m_list_all_price.setText("共￥0 元");
            m_list_num.setText("0");
            tv_add_cart.setVisibility(View.VISIBLE);
            rl_commodity_detail_add_cart.setVisibility(View.GONE);
        } else {
            m_list_car.setImageResource(R.mipmap.cart_car_orange);
            m_list_submit.setBackgroundResource(R.color.text_red);
            m_list_all_price.setText("共￥" + String.valueOf(mGoodsDataBaseInterface.getSecondGoodsPriceAll(mContext, SELECTPOSITION)) + " 元");
            m_list_num.setText(mGoodsDataBaseInterface.getSecondGoodsNumberAll(mContext, SELECTPOSITION) + "");
            m_list_num.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 点击弹窗加号和减号的时候设置总数和总价格
     */
    private void setPupupAll() {

        //设置所有购物数量
        if (mGoodsDataBaseInterface.getSecondGoodsNumberAll(mContext, SELECTPOSITION) == 0) {
            popupWindow.dismiss();
            mGoodsDataBaseInterface.deleteAll(mContext);
            m_list_car.setImageResource(R.mipmap.cart_car_gray);
            m_list_submit.setBackgroundResource(R.color.text_gray);
            m_list_num.setVisibility(View.GONE);
            m_list_all_price.setText("共￥0 元");
            m_list_num.setText("0");
            rl_commodity_detail_add_cart.setVisibility(View.GONE);
            tv_add_cart.setVisibility(View.VISIBLE);
        } else {
            m_list_all_price_popup.setText("共￥" + String.valueOf(mGoodsDataBaseInterface.getSecondGoodsPriceAll(mContext, SELECTPOSITION)) + " 元");
            m_list_num_popup.setText(mGoodsDataBaseInterface.getSecondGoodsNumberAll(mContext, SELECTPOSITION) + "");
            m_list_num_popup.setVisibility(View.VISIBLE);

        }
    }

    /*
    * 监听返回键
    * */
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            setResult(112);
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.m_list_submit:
                if (m_list_num.getText().toString().equals("0")) {
                    MessageUtils.showShortToast(this, "购物车还是空的！");
                } else {
                    Intent intent = new Intent(this, StoreOrderDetailActivity.class);
                    intent.putExtra("companyId", getIntent().getStringExtra("id"));
                    startActivity(intent);
                    AnimUtil.intentSlidIn(this);
                }
                break;
            case R.id.m_list_submit_popup:
                    Intent intent = new Intent(this, StoreOrderDetailActivity.class);
                    intent.putExtra("companyId", getIntent().getStringExtra("id"));
                    startActivity(intent);
                    AnimUtil.intentSlidIn(this);
                    popupWindow.dismiss();
                break;
            case R.id.iv_back:
                setResult(112);
                onBackPressed();
                break;
            case R.id.iv_zan://收藏
                CollectDao cdao = new CollectDao(StoreCommodityDetailActivity.this);
                if (count % 2 == 0) {//奇数次点击
                    if (product.getCollectState().equals("0")) {
                        cdao.requestMemberCollect(Arad.preferences.getString("memberId"),
                                "0",
                                "",
                                product.getId(),
                                "0"
                        );

                    }

                    if (product.getCollectState().equals("1")) {
                        cdao.requestMemberCollect(Arad.preferences.getString("memberId"),
                                "0",
                                "",
                                product.getId(),
                                "1"
                        );
                    }
                }
                if (count % 2 == 1) {//偶数次点击

                    if (product.getCollectState().equals("0")) {
                        cdao.requestMemberCollect(Arad.preferences.getString("memberId"),
                                "0",
                                "",
                                product.getId(),
                                "1"
                        );

                    }

                    if (product.getCollectState().equals("1")) {
                        cdao.requestMemberCollect(Arad.preferences.getString("memberId"),
                                "0",
                                "",
                                product.getId(),
                                "0"
                        );
                    }

                }

                break;
            case R.id.iv_share:
              //  showSharePopuwindow(iv_share);
                shareDialog = new ShareDialog(this);
                shareDialog.setCancelButtonOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shareDialog.dismiss();
                    }
                });
                shareDialog.setQQButtonOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Platform.ShareParams sp = new Platform.ShareParams();
                        sp.setTitle("烟台项目");
                        sp.setText("欢迎加入");

                        sp.setImageUrl(getIntent().getStringExtra("qrImgUrl"));//网络图片rul
                        sp.setTitleUrl(getIntent().getStringExtra("qrImgUrl"));  //网友点进链接后，可以看到分享的详情
                        //3、非常重要：获取平台对象
                        Platform qq = ShareSDK.getPlatform(QQ.NAME);
                        qq.setPlatformActionListener(StoreCommodityDetailActivity.this); // 设置分享事件回调
                        // 执行分享
                        qq.share(sp);
                    }

                });

                shareDialog.setWeixinButtonOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Platform.ShareParams sp = new Platform.ShareParams();
                        sp.setShareType(Platform.SHARE_WEBPAGE);//非常重要：一定要设置分享属性
                        sp.setTitle("烟台项目");  //分享标题
                        sp.setText("欢迎加入");   //分享文本
                        sp.setImageUrl(getIntent().getStringExtra("qrImgUrl"));//网络图片rul
                        sp.setUrl(getIntent().getStringExtra("qrImgUrl"));   //网友点进链接后，可以看到分享的详情

                        //3、非常重要：获取平台对象
                        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                        wechat.setPlatformActionListener(StoreCommodityDetailActivity.this); // 设置分享事件回调
                        // 执行分享
                        wechat.share(sp);
                    }
                });
                shareDialog.setSinaWeiBoButtonOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //2、设置分享内容
                        Platform.ShareParams sp = new Platform.ShareParams();
                        sp.setText("我是新浪微博分享文本，啦啦啦~http://uestcbmi.com/"); //分享文本
                        sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
                        //3、非常重要：获取平台对象
                        Platform sinaWeibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                        sinaWeibo.setPlatformActionListener(StoreCommodityDetailActivity.this); // 设置分享事件回调
                        // 执行分享
                        sinaWeibo.share(sp);
                    }
                });

                shareDialog.setPengYouQuanButtonOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //2、设置分享内容
                        Platform.ShareParams sp = new Platform.ShareParams();
                        sp.setShareType(Platform.SHARE_WEBPAGE); //非常重要：一定要设置分享属性
                        sp.setTitle("我是朋友圈分享标题");  //分享标题
                        sp.setText("我是朋友圈分享文本，啦啦啦~http://uestcbmi.com/");   //分享文本
                        sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
                        sp.setUrl("http://sharesdk.cn");   //网友点进链接后，可以看到分享的详情
                        //3、非常重要：获取平台对象
                        Platform wechatMoments = ShareSDK.getPlatform(WechatMoments.NAME);
                        wechatMoments.setPlatformActionListener(StoreCommodityDetailActivity.this); // 设置分享事件回调
                        // 执行分享
                        wechatMoments.share(sp);
                    }
                });
                shareDialog.setTecentWeiBoButtonOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //2、设置分享内容
                        Platform.ShareParams sp = new Platform.ShareParams();
                        sp.setTitle("我是腾讯微博分享标题");  //分享标题
                        sp.setText("我是腾讯微博分享文本，啦啦啦~http://uestcbmi.com/");   //分享文本
                        sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
                        sp.setUrl("http://sharesdk.cn");   //网友点进链接后，可以看到分享的详情
                        //3、非常重要：获取平台对象
                        Platform tecentWeibo = ShareSDK.getPlatform(TencentWeibo.NAME);
                        tecentWeibo.setPlatformActionListener(StoreCommodityDetailActivity.this); // 设置分享事件回调
                        // 执行分享
                        tecentWeibo.share(sp);
                    }
                });

                shareDialog.setEmailButtonOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //2、设置分享内容
                        Platform.ShareParams sp = new Platform.ShareParams();
                        sp.setTitle("我是邮件分享标题");  //分享标题
                        sp.setText("我是邮件分享文本，啦啦啦~http://uestcbmi.com/");   //分享文本
                        sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
                        sp.setUrl("http://sharesdk.cn");   //网友点进链接后，可以看到分享的详情
                        //3、非常重要：获取平台对象
                        Platform emailName = ShareSDK.getPlatform(Email.NAME);
                        emailName.setPlatformActionListener(StoreCommodityDetailActivity.this); // 设置分享事件回调
                        // 执行分享
                        emailName.share(sp);
                    }
                });
                shareDialog.setDuanXinButtonOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //2、设置分享内容
                        Platform.ShareParams sp = new Platform.ShareParams();
                        sp.setTitle("我是短信分享标题");  //分享标题
                        sp.setText("我是短信分享文本，啦啦啦~http://uestcbmi.com/");   //分享文本
                        sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
                        sp.setUrl("http://sharesdk.cn");   //网友点进链接后，可以看到分享的详情
                        //3、非常重要：获取平台对象
                        Platform shortMessage = ShareSDK.getPlatform(ShortMessage.NAME);
                        shortMessage.setPlatformActionListener(StoreCommodityDetailActivity.this); // 设置分享事件回调
                        // 执行分享
                        shortMessage.share(sp);
                    }
                });

                break;
            case R.id.m_list_car_lay:
                showCartPopupWindow(v);
                break;
            case R.id.tv_add_cart://加入购物车
                tv_add_cart.setVisibility(View.GONE);
                rl_commodity_detail_add_cart.setVisibility(View.VISIBLE);
                tv_commodity_detail_num.setVisibility(View.VISIBLE);
                iv_commodity_detail_sub.setVisibility(View.VISIBLE);
                tv_commodity_detail_num.setText(mGoodsDataBaseInterface.saveGoodsNumber(mContext, SELECTPOSITION,
                        product.getId(), "1",
                        Float.parseFloat(product.getSellingPrice()),
                        product.getName(), product.getStock()) + "");
                //动画
                GoodsAnimUtil.setAnim(StoreCommodityDetailActivity.this, tv_add_cart, m_list_car_lay);
                GoodsAnimUtil.setOnEndAnimListener(new onEndAnim());
                break;
            case R.id.iv_commodity_detail_sub://减号
                String nums = tv_commodity_detail_num.getText().toString().trim();
                //  holder.item_menu_content_number.setText(mGoodsDataBaseInterface.saveGoodsNumber(mContext, SELECTPOSITION, foodList.get(holder.getPosition()).getId(), String.valueOf(Integer.parseInt(nums) - 1), foodList.get(holder.getPosition()).getSellingPrice()) + "");
                tv_commodity_detail_num.setText(mGoodsDataBaseInterface.saveGoodsNumber(mContext, SELECTPOSITION,
                        product.getId(),
                        String.valueOf(Integer.parseInt(nums) - 1),
                        Float.parseFloat(product.getSellingPrice())
                        ,
                        product.getName(), product.getStock()) + "");
                nums = tv_commodity_detail_num.getText().toString().trim();
                // 减完之后  数据为0
                if (nums.equals("0")) {
                    tv_commodity_detail_num.setVisibility(View.GONE);
                    iv_commodity_detail_sub.setVisibility(View.GONE);
                    rl_commodity_detail_add_cart.setVisibility(View.GONE);
                    tv_add_cart.setVisibility(View.VISIBLE);
                }
                setAll();
                break;
            case R.id.iv_commodity_detail_add://加号
                String nums2 = tv_commodity_detail_num.getText().toString().trim();
                if(product.getStock()!=null) {
                    if ((Integer.parseInt(nums2) + 1) > Integer.parseInt(product.getStock())) {
                        MessageUtils.showShortToast(StoreCommodityDetailActivity.this, "库存不足");
                        return;
                    }
                }
                tv_commodity_detail_num.setText(mGoodsDataBaseInterface.saveGoodsNumber(mContext, SELECTPOSITION,
                        product.getId(), String.valueOf(Integer.parseInt(nums2) + 1),
                        Float.parseFloat(product.getSellingPrice()),
                        product.getName(), product.getStock()) + "");

                //动画
                GoodsAnimUtil.setAnim(StoreCommodityDetailActivity.this, tv_add_cart, m_list_car_lay);
                GoodsAnimUtil.setOnEndAnimListener(new onEndAnim());
                break;
        }
    }

    private void showSharePopuwindow(View v) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.share_dialog, null);

        popupWindow_share = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        // 设置SelectPicPopupWindow的View
        popupWindow_share.setContentView(contentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        popupWindow_share.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        popupWindow_share.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        popupWindow_share.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        popupWindow_share.setAnimationStyle(R.style.AnimBottom);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x90000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow_share.setBackgroundDrawable(dw);

        contentView.setFocusable(true);
        contentView.setFocusableInTouchMode(true);
        popupWindow_share.setTouchable(true);
         /*
        * 设置popupwindow 点击自身消失
        * */
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow_share.isShowing()) {
                    popupWindow_share.dismiss();
                }
            }
        });

        popupWindow_share.setOutsideTouchable(true);

        popupWindow_share.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.i("mengdd", "onTouch : ");

                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        // 设置好参数之后再show
        popupWindow_share.showAtLocation(v, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        if (platform.getName().equals(QQ.NAME)) {// 判断成功的平台是不是QQ
            mhandler.sendEmptyMessage(1);
        } else if (platform.getName().equals(Wechat.NAME)) {//判断成功的平台是不是微信
            mhandler.sendEmptyMessage(2);
        }else if(platform.getName().equals(SinaWeibo.NAME)){//判断成功的平台是不是新浪微博
            mhandler.sendEmptyMessage(3);
        }else if(platform.getName().equals(WechatMoments.NAME)){//判断成功平台是不是微信朋友圈
            mhandler.sendEmptyMessage(4);
        }else if(platform.getName().equals(TencentWeibo.NAME)){//判断成功平台是不是腾讯微博
            mhandler.sendEmptyMessage(5);
        }else if(platform.getName().equals(Email.NAME)){//判断成功平台是不是邮件
            mhandler.sendEmptyMessage(6);
        }else if(platform.getName().equals(ShortMessage.NAME)){//判断成功平台是不是短信
            mhandler.sendEmptyMessage(7);
        }else {
            //
        }

    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        throwable.printStackTrace();
        Message msg = new Message();
        msg.what = 8;
        msg.obj = throwable.getMessage();
        handler.sendMessage(msg);

    }

    @Override
    public void onCancel(Platform platform, int i) {
        handler.sendEmptyMessage(9);
    }

    Handler mhandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //  Toast.makeText(getApplicationContext(), "QQ分享成功", Toast.LENGTH_LONG).show();
                    break;

                case 2:
                    //Toast.makeText(getApplicationContext(), "微信分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    //Toast.makeText(getApplicationContext(), "微信分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 4:
                    //Toast.makeText(getApplicationContext(), "微信分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 5:
                    //Toast.makeText(getApplicationContext(), "微信分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 6:
                    //Toast.makeText(getApplicationContext(), "微信分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 7:
                    //Toast.makeText(getApplicationContext(), "微信分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 8:
                    Toast.makeText(getApplicationContext(), "分享失败啊", Toast.LENGTH_LONG).show();
                    break;
                case 9:
                    Toast.makeText(getApplicationContext(), "已取消", Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
            }
        }

    };


}
