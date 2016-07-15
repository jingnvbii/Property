package com.ctrl.forum.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarFragment;
import com.beanu.arad.utils.AnimUtil;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.HorzitalGridView.adapter.App2Adapter;
import com.ctrl.forum.HorzitalGridView.adapter.MyViewPagerAdapter;
import com.ctrl.forum.HorzitalGridView.control.PageControl;
import com.ctrl.forum.R;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.base.MyApplication;
import com.ctrl.forum.customview.GridViewForScrollView;
import com.ctrl.forum.dao.MallDao;
import com.ctrl.forum.entity.Banner;
import com.ctrl.forum.entity.Mall;
import com.ctrl.forum.entity.MallKind;
import com.ctrl.forum.entity.MallRecommend;
import com.ctrl.forum.entity.Merchant;
import com.ctrl.forum.entity.Notice;
import com.ctrl.forum.entity.NoticeImage;
import com.ctrl.forum.loopview.HomeAutoSwitchPicHolder;
import com.ctrl.forum.service.LocationService;
import com.ctrl.forum.ui.activity.Invitation.InvitationPinterestDetailActivity;
import com.ctrl.forum.ui.activity.LoginActivity;
import com.ctrl.forum.ui.activity.store.StoreCommodityDetailActivity;
import com.ctrl.forum.ui.activity.store.StoreLocateActivity;
import com.ctrl.forum.ui.activity.store.StoreScreenActivity;
import com.ctrl.forum.ui.activity.store.StoreShopListHorzitalStyleActivity;
import com.ctrl.forum.ui.activity.store.StoreShopListVerticalStyleActivity;
import com.ctrl.forum.ui.adapter.StoreFragmentAdapter;
import com.ctrl.forum.ui.adapter.StoreGridViewAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 商城 fragment
 * Created by jason on 2016/4/7.
 */
public class StroeFragment extends ToolBarFragment implements View.OnClickListener {
    @InjectView(R.id.tv_toolbar)//定位标
            TextView tv_toolbar;
    @InjectView(R.id.ll_linear_layout)
    LinearLayout ll_linear_layout;


    DisplayMetrics dm;
    private int NUM = 4; // 每行显示个数
    private int hSpacing = 20;// 水平间距
    private StoreFragmentAdapter listviewAdapter;
    private PullToRefreshListView lv_store_home;
    private ListView lv01;
    private TextView tv_change;
    private FrameLayout framelayout;
    private GridViewForScrollView gridView1;
    private ImageView iv01_store_recomend;
    private ImageView iv02_store_recomend;
    private ImageView iv03_store_recomend;
    private ImageView iv04_store_recomend;
    private MallDao mdao;
    private int PAGE_NUM = 1;
    private List<Banner> listBanner;
    private List<MallKind> listMallKind;
    private List<Notice> listMallNotice;
    private List<MallRecommend> listMallRecommend;
    private List<Mall> listMall;
    private LocationService locationService;
    private List<Merchant> merchantList;//测试数据
    private GeoCoder mSearch;
    private HomeAutoSwitchPicHolder mAutoSwitchPicHolder;
    private ArrayList<String> mData;
    private String latitude_now;
    private String longitude_now;
    private String address_now;
    private String latitude_address;
    private String longitude_address;
    private String address_address;
    private double latitude1;
    private double longitude1;
    private String latitude_map;
    private String longitude_map;
    private String address_map;
    private String latitude_search;
    private String longitude_search;
    private String address_search;
    private TextView tv_store_home_more;
    private ImageView iv_notice_store_home;
    private List<NoticeImage> listNoticeImage;
    private HashMap<Integer, GridView> map;

    private ViewPager myViewPager;
    private static final float APP_PAGE_SIZE = 10.0f;
    private MyViewPagerAdapter viewpagerAdapter;
   // LayoutInflater inflater;
    private PageControl pageControl;
    private ViewGroup viewGroup;
    private LinearLayout ll_notice;
    private LinearLayout ll_tuijian;
    private int bol=0;
    private int PageCount;


    public static StroeFragment newInstance() {
        StroeFragment fragment = new StroeFragment();
        return fragment;
    }

    /*
   * 实现文字上下轮播
   * */
    private boolean isloop = true;
    private List<String> listNoticeString = new ArrayList<>();
    private int item = 0;
    private AnimationSet set = new AnimationSet(true);
    private AlphaAnimation animation = new AlphaAnimation(0.1f, 1.0f);
    private TranslateAnimation ta = new TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF,
            0, Animation.RELATIVE_TO_SELF, 2.0f,
            Animation.RELATIVE_TO_SELF, 0);
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:

                    if (isloop) {
                        tv_change.setText(listNoticeString.get(item % listNoticeString.size()));
                        tv_change.setAnimation(set);
                        tv_change.startAnimation(set);
                        item += 1;
                    }
                    break;
            }
        }

    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bol=0;
        mdao = new MallDao(this);
        showProgress(true);
        mdao.requestInitMall();
        mSearch = GeoCoder.newInstance();
        // -----------location config ------------
        locationService = ((MyApplication) getActivity().getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());//默认  仅定位一次
        locationService.start();
        // showProgress(true);
        OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
            public void onGetGeoCodeResult(GeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    //没有检索到结果
                    //没有找到检索结果
                    MessageUtils.showShortToast(getActivity(), "没有结果");
                }
                //获取地理编码结果
                //获取反向地理编码结果
                Log.i("tag", "地理位置--" + result.getAddress());
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    //没有找到检索结果
                    MessageUtils.showShortToast(getActivity(), "没有结果");
                }
                //获取反向地理编码结果
                tv_toolbar.setText(result.getAddress());
                latitude1 = result.getLocation().latitude;
                longitude1 = result.getLocation().longitude;

            }
        };
        mSearch.setOnGetGeoCodeResultListener(listener);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mSearch.destroy();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
       /* if(isVisibleToUser) {
            PAGE_NUM=1;
            showProgress(true);
            if(latitude_now!=null&&longitude_now!=null){
                mdao.requestInitMallRecommendCompany(latitude_now, longitude_now,
                        String.valueOf(Constant.PAGE_SIZE), String.valueOf(PAGE_NUM));
            }else if(latitude_address!=null&&longitude_address!=null){
                mdao.requestInitMallRecommendCompany(latitude_address, longitude_address,
                        String.valueOf(Constant.PAGE_SIZE), String.valueOf(PAGE_NUM));
            }else if(latitude_map!=null&&longitude_map!=null){
                mdao.requestInitMallRecommendCompany(latitude_map, longitude_map,
                        String.valueOf(Constant.PAGE_SIZE), String.valueOf(PAGE_NUM));
            }else if(latitude_search!=null&&longitude_search!=null){
                mdao.requestInitMallRecommendCompany(latitude_search, longitude_search,
                        String.valueOf(Constant.PAGE_SIZE), String.valueOf(PAGE_NUM));
            }else {
                mdao.requestInitMallRecommendCompany(latitude, longitude,
                        String.valueOf(Constant.PAGE_SIZE), String.valueOf(PAGE_NUM));
            }
        }*/
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store, container, false);
        lv_store_home = (PullToRefreshListView) view.findViewById(R.id.lv_store_home);
        ButterKnife.inject(this, view);
        // scrollView.setHorizontalScrollBarEnabled(false);// 隐藏滚动条
        getScreenDen();
        initView();
        //公告轮播控件初始化
        initNoticeView();
        initData();
        pageControl = new PageControl(getActivity(),(LinearLayout)viewGroup, 5);
        return view;
    }

    private void initData() {
        tv_toolbar.requestFocus();//跑马灯获取焦点

        lv_store_home.setMode(PullToRefreshBase.Mode.BOTH);
        lv_store_home.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (listMall != null)
                    listMall.clear();
               if(PAGE_NUM==0){
                   PAGE_NUM=1;
               }else {
                   PAGE_NUM=1;
               }
                // showProgress(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mdao.requestInitMall();
                        if(latitude_now!=null&&longitude_now!=null){
                            mdao.requestInitMallRecommendCompany(latitude_now, longitude_now,
                                    String.valueOf(Constant.PAGE_SIZE), String.valueOf(PAGE_NUM));
                        }else if(latitude_address!=null&&longitude_address!=null){
                            mdao.requestInitMallRecommendCompany(latitude_address, longitude_address,
                                    String.valueOf(Constant.PAGE_SIZE), String.valueOf(PAGE_NUM));
                        }else if(latitude_map!=null&&longitude_map!=null){
                            mdao.requestInitMallRecommendCompany(latitude_map, longitude_map,
                                    String.valueOf(Constant.PAGE_SIZE), String.valueOf(PAGE_NUM));
                        }else if(latitude_search!=null&&longitude_search!=null){
                            mdao.requestInitMallRecommendCompany(latitude_search, longitude_search,
                                    String.valueOf(Constant.PAGE_SIZE), String.valueOf(PAGE_NUM));
                        }else {
                            mdao.requestInitMallRecommendCompany(latitude, longitude,
                                    String.valueOf(Constant.PAGE_SIZE), String.valueOf(PAGE_NUM));
                        }
                    }
                }, 500);


            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if(listMall==null){
                    PAGE_NUM=1;
                }else {
                    PAGE_NUM += 1;
                }
                //  showProgress(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mdao.requestInitMall();
                        if(latitude_now!=null&&longitude_now!=null){
                            mdao.requestInitMallRecommendCompany(latitude_now, longitude_now,
                                    String.valueOf(Constant.PAGE_SIZE), String.valueOf(PAGE_NUM));
                        }else if(latitude_address!=null&&longitude_address!=null){
                            mdao.requestInitMallRecommendCompany(latitude_address, longitude_address,
                                    String.valueOf(Constant.PAGE_SIZE), String.valueOf(PAGE_NUM));
                        }else if(latitude_map!=null&&longitude_map!=null){
                            mdao.requestInitMallRecommendCompany(latitude_map, longitude_map,
                                    String.valueOf(Constant.PAGE_SIZE), String.valueOf(PAGE_NUM));
                        }else if(latitude_search!=null&&longitude_search!=null){
                            mdao.requestInitMallRecommendCompany(latitude_search, longitude_search,
                                    String.valueOf(Constant.PAGE_SIZE), String.valueOf(PAGE_NUM));
                        }else {
                            mdao.requestInitMallRecommendCompany(latitude, longitude,
                                    String.valueOf(Constant.PAGE_SIZE), String.valueOf(PAGE_NUM));
                        }
                    }
                }, 500);
            }
        });

        lv_store_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int newpositon = position - lv01.getHeaderViewsCount();
                if(listMall.get(newpositon).getCompanyStyle().equals("0")) {
                    Intent intent = new Intent(getActivity(), StoreShopListVerticalStyleActivity.class);
                    intent.putExtra("id", listMall.get(newpositon).getId());
                    intent.putExtra("url", listMall.get(newpositon).getImg());
                    intent.putExtra("name", listMall.get(newpositon).getName());
                    intent.putExtra("startTime", listMall.get(newpositon).getWorkStartTime());
                    intent.putExtra("endTime", listMall.get(newpositon).getWorkEndTime());
                    intent.putExtra("levlel", listMall.get(newpositon).getEvaluatLevel());
                    startActivity(intent);
                    AnimUtil.intentSlidIn(getActivity());
                }
                if(listMall.get(newpositon).getCompanyStyle().equals("1")) {
                    Intent intent = new Intent(getActivity(), StoreShopListHorzitalStyleActivity.class);
                    intent.putExtra("id", listMall.get(newpositon).getId());
                    intent.putExtra("url", listMall.get(newpositon).getImg());
                    intent.putExtra("name", listMall.get(newpositon).getName());
                    intent.putExtra("startTime", listMall.get(newpositon).getWorkStartTime());
                    intent.putExtra("endTime", listMall.get(newpositon).getWorkEndTime());
                    intent.putExtra("levlel", listMall.get(newpositon).getEvaluatLevel());
                    startActivity(intent);
                    AnimUtil.intentSlidIn(getActivity());
                }

            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
        // TODO Auto-generated method stub
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();


    }


    private boolean isRefresh;
    private String latitude;
    private String longitude;
    private BDLocationListener mListener = new BDLocationListener() {


        @Override
        public void onReceiveLocation(final BDLocation location) {
            showProgress(false);
            isRefresh = false;
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                latitude = String.valueOf(location.getLatitude());
                longitude = String.valueOf(location.getLongitude());
                if (latitude != null && longitude != null) {
                    Arad.preferences.putString("latitude", latitude);
                    Arad.preferences.putString("longitude", longitude);
                }
                Arad.preferences.flush();
                mdao.requestInitMallRecommendCompany(latitude, longitude,
                        String.valueOf(Constant.PAGE_SIZE), String.valueOf(PAGE_NUM));
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
            }
        }

    };


    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        showProgress(false);
        lv_store_home.onRefreshComplete();
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        lv_store_home.onRefreshComplete();
        bol=1;
        if (requestCode == 0) {
            listBanner = mdao.getListMallBanner();
            listMallKind = mdao.getListMallKind();
            listMallNotice = mdao.getListMallNotice();
            listMallRecommend = mdao.getListMallRecommend();
            listNoticeImage=mdao.getListNoticeImage();
            Arad.imageLoader.load(listNoticeImage.get(0).getImgUrl()).placeholder(R.mipmap.jinrigonggao_red).into(iv_notice_store_home);
            //    MessageUtils.showShortToast(getActivity(), "商城初始化成功");
            setValue();
            initRecommend();//推荐列表初始化
            initNotice();//公告栏数据初始化
            //调用轮播图
            setLoopView();
            initViewPager();
            viewpagerAdapter = new MyViewPagerAdapter(getActivity(), map);
            myViewPager.setAdapter(viewpagerAdapter);
            myViewPager.setOnPageChangeListener(new MyListener());
            showProgress(false);
        }

        if (requestCode == 1) {
            //   MessageUtils.showShortToast(getActivity(), "获取推荐商家列表成功");
            listMall = mdao.getListMall();
            listviewAdapter.setList(listMall);
            showProgress(false);
        }
    }


    public void  setGridViewItemClick(int arg2,String kindId,String kindName){
        for (int i = 0; i < (int)APP_PAGE_SIZE; i++) {
            if (i == arg2) {
                if(latitude_now!=null&&longitude_now!=null){
                    Intent intent = new Intent(getActivity(), StoreScreenActivity.class);
                    intent.putExtra("channelId",kindId);
                    intent.putExtra("latitude", latitude_now);
                    intent.putExtra("longitude", longitude_now);
                    intent.putExtra("address", address_now);
                    intent.putExtra("name", kindName);
                    getActivity().startActivity(intent);
                    AnimUtil.intentSlidIn(getActivity());
                }else if(latitude_address!=null&&longitude_address!=null){
                    Intent intent = new Intent(getActivity(), StoreScreenActivity.class);
                    intent.putExtra("channelId", kindId);
                    intent.putExtra("latitude", latitude_address);
                    intent.putExtra("longitude", longitude_address);
                    intent.putExtra("address", address_address);
                    intent.putExtra("name", kindName);
                    getActivity().startActivity(intent);
                    AnimUtil.intentSlidIn(getActivity());
                }else if(latitude_map!=null&&longitude_map!=null){
                    Intent intent = new Intent(getActivity(), StoreScreenActivity.class);
                    intent.putExtra("channelId", kindId);
                    intent.putExtra("latitude", latitude_map);
                    intent.putExtra("longitude", longitude_map);
                    intent.putExtra("address", address_map);
                    intent.putExtra("name", kindName);
                    getActivity().startActivity(intent);
                    AnimUtil.intentSlidIn(getActivity());
                }else if(latitude_search!=null&&longitude_search!=null){
                    Intent intent = new Intent(getActivity(), StoreScreenActivity.class);
                    intent.putExtra("channelId",kindId);
                    intent.putExtra("latitude", latitude_search);
                    intent.putExtra("longitude", longitude_search);
                    intent.putExtra("address", address_search);
                    intent.putExtra("name",  kindName);
                    getActivity().startActivity(intent);
                    AnimUtil.intentSlidIn(getActivity());
                }else {
                    Intent intent = new Intent(getActivity(), StoreScreenActivity.class);
                    intent.putExtra("channelId", kindId);
                    intent.putExtra("latitude", latitude);
                    intent.putExtra("longitude", longitude);
                    intent.putExtra("address", tv_toolbar.getText().toString().trim());
                    intent.putExtra("name",  kindName);
                    getActivity().startActivity(intent);
                    AnimUtil.intentSlidIn(getActivity());
                }

            } else {
//
            }
        }
    }

    private void initViewPager() {
        PageCount = (int) Math.ceil(listMallKind.size() / APP_PAGE_SIZE);
        map = new HashMap<Integer, GridView>();
        for (int i = 0; i < PageCount; i++) {
            GridView appPage = new GridView(getActivity());
            final App2Adapter app2adapter =new App2Adapter(getActivity(), listMallKind, i,this);
            appPage.setAdapter(app2adapter);
            appPage.setNumColumns(5);
           appPage.setOnItemClickListener(app2adapter);
            map.put(i, appPage);

        }

    //    ViewGroup main = (ViewGroup) getActivity().getLayoutInflater().inflate(R.layout.fragment_store_home_header,
        //        null);
        // group是R.layou.main中的负责包裹小圆点的LinearLayout.
       // ViewGroup group = (ViewGroup)hefindViewById(R.id.viewGroup);
    //    pageControl = new PageControl(getActivity(), viewGroup, PageCount);
     //   getActivity().setContentView(main);

    }

    private void initNotice() {
        ll_notice.setVisibility(View.VISIBLE);
        for (int i = 0; i < listMallNotice.size(); i++) {
            listNoticeString.add(listMallNotice.get(i).getContent());
        }
        //实现自动切换界面
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isloop) {
                    //系统时钟的睡眠方法---->电量的消耗很少。
                    SystemClock.sleep(4000);
                    handler.sendEmptyMessage(1);
                }
            }
        }).start();

    }

    private void initRecommend() {
        ll_tuijian.setVisibility(View.VISIBLE);
        if (listMallRecommend.size() > 0&&listMallRecommend.get(0).getImgUrl()!=null&&!listMallRecommend.get(0).getImgUrl().equals("")) {
            Arad.imageLoader.load(listMallRecommend.get(0).getImgUrl()).placeholder(R.mipmap.default_error).into(iv01_store_recomend);
            iv01_store_recomend.setOnClickListener(this);
        }
        if (listMallRecommend.size() > 1&&listMallRecommend.get(1).getImgUrl()!=null&&!listMallRecommend.get(1).getImgUrl().equals("")) {
            Arad.imageLoader.load(listMallRecommend.get(1).getImgUrl()).placeholder(R.mipmap.default_error).into(iv02_store_recomend);
            iv02_store_recomend.setOnClickListener(this);
        }
        if (listMallRecommend.size() > 2&&listMallRecommend.get(2).getImgUrl()!=null&&!listMallRecommend.get(2).getImgUrl().equals("")) {
            Arad.imageLoader.load(listMallRecommend.get(2).getImgUrl()).placeholder(R.mipmap.default_error).into(iv03_store_recomend);
            iv03_store_recomend.setOnClickListener(this);
        }
        if (listMallRecommend.size() > 3&&listMallRecommend.get(3).getImgUrl()!=null&&!listMallRecommend.get(3).getImgUrl().equals("")) {
            Arad.imageLoader.load(listMallRecommend.get(3).getImgUrl()).placeholder(R.mipmap.default_error).into(iv04_store_recomend);
            iv04_store_recomend.setOnClickListener(this);
        }
    }

    private void setValue() {
        StoreGridViewAdapter adapter = new StoreGridViewAdapter(getActivity());
        gridView1.setClickable(true);
        adapter.setList(listMallKind);
        int count = adapter.getCount();
        int columns = (count % 2 == 0) ? count / 2 : count / 2 + 1;
        gridView1.setAdapter(adapter);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(columns * dm.widthPixels / NUM,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        gridView1.setLayoutParams(params);
        gridView1.setColumnWidth(dm.widthPixels / NUM);
        gridView1.setHorizontalSpacing(hSpacing);
        gridView1.setStretchMode(GridView.NO_STRETCH);
        if (count <= 3) {
            gridView1.setNumColumns(columns);
        } else {
            gridView1.setNumColumns(columns);
        }

        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(latitude!=null&&longitude!=null) {

                    Intent intent = new Intent(getActivity(), StoreScreenActivity.class);
                    intent.putExtra("channelId", listMallKind.get(position).getId());
                    intent.putExtra("latitude", latitude);
                    intent.putExtra("longitude", longitude);
                    intent.putExtra("name", listMallKind.get(position).getKindName());
                    getActivity().startActivity(intent);
                    AnimUtil.intentSlidIn(getActivity());
                }
            }
        });

    }

    private void initNoticeView() {
        tv_change.setText("");
        set.addAnimation(animation);
        set.addAnimation(ta);
        set.setDuration(2000);
        set.setRepeatMode(Animation.REVERSE);
    }

    private void initView() {
        PAGE_NUM = 1;
        tv_toolbar.setOnClickListener(this);
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        View headview = getActivity().getLayoutInflater().inflate(R.layout.fragment_store_home_header, ll_linear_layout, false);
        headview.setLayoutParams(layoutParams);
        lv01 = lv_store_home.getRefreshableView();
        tv_change = (TextView) headview.findViewById(R.id.tv_change);
        tv_store_home_more = (TextView) headview.findViewById(R.id.tv_store_home_more);
        iv_notice_store_home = (ImageView) headview.findViewById(R.id.iv_notice_store_home);
        framelayout = (FrameLayout) headview.findViewById(R.id.framelayout_store_home);
        ll_notice = (LinearLayout) headview.findViewById(R.id.ll_notice);
        ll_tuijian = (LinearLayout) headview.findViewById(R.id.ll_tuijian);
        iv01_store_recomend = (ImageView) headview.findViewById(R.id.iv01_store_recomend);
        iv02_store_recomend = (ImageView) headview.findViewById(R.id.iv02_store_recomend);
        iv03_store_recomend = (ImageView) headview.findViewById(R.id.iv03_store_recomend);
        iv04_store_recomend = (ImageView) headview.findViewById(R.id.iv04_store_recomend);

        myViewPager=(ViewPager)headview.findViewById(R.id.myviewpager);
        viewGroup=(ViewGroup)headview.findViewById(R.id.viewGroup);
        tv_store_home_more.setOnClickListener(this);
        gridView1 = (GridViewForScrollView) headview.findViewById(R.id.gridView1_store_home);
        lv01.addHeaderView(headview);
        lv01.setFocusable(false);
        listviewAdapter = new StoreFragmentAdapter(getActivity());

        tv_change.setOnClickListener(this);
    }

    /*
  * 轮播图
  * */
    private void setLoopView() {
        framelayout.setVisibility(View.VISIBLE);
        // 1.创建轮播的holder  fdfdf
        mAutoSwitchPicHolder = new HomeAutoSwitchPicHolder(getActivity());
        // 2.得到轮播图的视图view
        View autoPlayPicView = mAutoSwitchPicHolder.getRootView();
        // 把轮播图的视图添加到主界面中
        framelayout.addView(autoPlayPicView);
        //4. 为轮播图设置数据
        mAutoSwitchPicHolder.setData(getData());
        mAutoSwitchPicHolder.setData(listBanner);
    }

    public List<String> getData() {
        mData = new ArrayList<String>();
        for(int i=0;i<listBanner.size();i++){
            mData.add(listBanner.get(i).getImgUrl());
        }
        return mData;
    }


    private void getScreenDen() {
        dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lv_store_home.setAdapter(listviewAdapter);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PAGE_NUM=1;
        if (requestCode == 555 && resultCode == 556) {
            latitude_now = data.getStringExtra("latitude");
            longitude_now = data.getStringExtra("longitude");
            address_now = data.getStringExtra("address");
            tv_toolbar.setText(address_now);
            mdao.requestInitMallRecommendCompany(latitude_now, longitude_now,
                    String.valueOf(Constant.PAGE_SIZE), String.valueOf(PAGE_NUM));
        }
        if (requestCode == 555 && resultCode == 557) {
            latitude_address = data.getStringExtra("latitude");
            longitude_address = data.getStringExtra("longitude");
            address_address = data.getStringExtra("address");
            tv_toolbar.setText(address_address);
            mdao.requestInitMallRecommendCompany(latitude_address, longitude_address,
                    String.valueOf(Constant.PAGE_SIZE), String.valueOf(PAGE_NUM));
        }
        if (requestCode == 555 && resultCode == 666) {
            latitude_map = data.getStringExtra("latitude");
            longitude_map = data.getStringExtra("longitude");
            address_map = data.getStringExtra("address");
            tv_toolbar.setText(address_map);
            mdao.requestInitMallRecommendCompany(latitude_map, longitude_map,
                    String.valueOf(Constant.PAGE_SIZE), String.valueOf(PAGE_NUM));
        }
        if (requestCode == 555 && resultCode == 888) {
            latitude_search = data.getStringExtra("latitude");
            longitude_search = data.getStringExtra("longitude");
            address_search = data.getStringExtra("address");
            tv_toolbar.setText(address_search);

            mdao.requestInitMallRecommendCompany(latitude_search, longitude_search,
                    String.valueOf(Constant.PAGE_SIZE), String.valueOf(PAGE_NUM));
        }

    }

    @Override
    public void onClick(View v) {
        String type = null;
        Intent intent = null;
        switch (v.getId()) {
            case R.id.tv_change:
                gotoDetail(item%listMallNotice.size());
                break;
            case R.id.tv_store_home_more:
                intent=new Intent(getActivity(),StoreScreenActivity.class);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                intent.putExtra("address", tv_toolbar.getText().toString().trim());
                intent.setFlags(303);
                startActivity(intent);
                AnimUtil.intentSlidIn(getActivity());
                break;
            case R.id.tv_toolbar:
                if(Arad.preferences.getString("memberId")==null||Arad.preferences.getString("memberId").equals("")){
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    AnimUtil.intentSlidOut(getActivity());
                    return;
                }
                Intent intent_toolbar = new Intent(getActivity(), StoreLocateActivity.class);
                intent_toolbar.putExtra("address", tv_toolbar.getText().toString().trim());
                intent_toolbar.putExtra("latitude1", latitude1);
                intent_toolbar.putExtra("longitude1", longitude1);
                startActivityForResult(intent_toolbar, 555);
                AnimUtil.intentSlidIn(getActivity());
                break;

            case R.id.iv01_store_recomend:
                type = listMallRecommend.get(0).getType();
                switch (type) {
                    case "0"://跳商家
                        intent = new Intent(getActivity(), StoreShopListVerticalStyleActivity.class);
                        intent.putExtra("id", listMallRecommend.get(0).getTargetId());
                        getActivity().startActivity(intent);
                        AnimUtil.intentSlidIn(getActivity());
                        break;
                    case "1"://跳商品详情
                        intent = new Intent(getActivity(), StoreCommodityDetailActivity.class);
                        intent.putExtra("id", listMallRecommend.get(0).getTargetId());
                        getActivity().startActivity(intent);
                        AnimUtil.intentSlidIn(getActivity());
                        break;
                    case "2"://跳帖子详情
                        intent = new Intent(getActivity(), InvitationPinterestDetailActivity.class);
                        intent.putExtra("id", listMallRecommend.get(0).getTargetId());
                        getActivity().startActivity(intent);
                        AnimUtil.intentSlidIn(getActivity());
                        break;
                    case "3"://外部链接
                        Uri uri = Uri.parse(listMallRecommend.get(0).getLinkUrl());
                        intent = new Intent(Intent.ACTION_VIEW, uri);
                        getActivity().startActivity(intent);
                        AnimUtil.intentSlidIn(getActivity());
                        break;

                }
                break;
            case R.id.iv02_store_recomend:
                type = listMallRecommend.get(1).getType();
                switch (type) {
                    case "0"://跳商家
                        intent = new Intent(getActivity(), StoreShopListVerticalStyleActivity.class);
                        intent.putExtra("id", listMallRecommend.get(1).getTargetId());
                        getActivity().startActivity(intent);
                        AnimUtil.intentSlidIn(getActivity());
                        break;
                    case "1"://跳商品详情
                        intent = new Intent(getActivity(), StoreCommodityDetailActivity.class);
                        intent.putExtra("id", listMallRecommend.get(1).getTargetId());
                        getActivity().startActivity(intent);
                        AnimUtil.intentSlidIn(getActivity());
                        break;
                    case "2"://跳帖子详情
                        intent = new Intent(getActivity(), InvitationPinterestDetailActivity.class);
                        intent.putExtra("id", listMallRecommend.get(1).getTargetId());
                        getActivity().startActivity(intent);
                        AnimUtil.intentSlidIn(getActivity());
                        break;
                    case "3"://外部链接
                        Uri uri = Uri.parse(listMallRecommend.get(1).getLinkUrl());
                        intent = new Intent(Intent.ACTION_VIEW, uri);
                        getActivity().startActivity(intent);
                        AnimUtil.intentSlidIn(getActivity());
                        break;

                }

                break;
            case R.id.iv03_store_recomend:
                type = listMallRecommend.get(2).getType();
                switch (type) {
                    case "0"://跳商家
                        intent = new Intent(getActivity(), StoreShopListVerticalStyleActivity.class);
                        intent.putExtra("id", listMallRecommend.get(2).getTargetId());
                        getActivity().startActivity(intent);
                        AnimUtil.intentSlidIn(getActivity());
                        break;
                    case "1"://跳商品详情
                        intent = new Intent(getActivity(), StoreCommodityDetailActivity.class);
                        intent.putExtra("id", listMallRecommend.get(2).getTargetId());
                        getActivity().startActivity(intent);
                        AnimUtil.intentSlidIn(getActivity());
                        break;
                    case "2"://跳帖子详情
                        intent = new Intent(getActivity(), InvitationPinterestDetailActivity.class);
                        intent.putExtra("id", listMallRecommend.get(2).getTargetId());
                        getActivity().startActivity(intent);
                        AnimUtil.intentSlidIn(getActivity());
                        break;
                    case "3"://外部链接
                        Uri uri = Uri.parse(listMallRecommend.get(2).getLinkUrl());
                        intent = new Intent(Intent.ACTION_VIEW, uri);
                        getActivity().startActivity(intent);
                        AnimUtil.intentSlidIn(getActivity());
                        break;

                }

                break;
            case R.id.iv04_store_recomend:
                type = listMallRecommend.get(3).getType();
                switch (type) {
                    case "0"://跳商家
                        intent = new Intent(getActivity(), StoreShopListVerticalStyleActivity.class);
                        intent.putExtra("id", listMallRecommend.get(3).getTargetId());
                        getActivity().startActivity(intent);
                        AnimUtil.intentSlidIn(getActivity());
                        break;
                    case "1"://跳商品详情
                        intent = new Intent(getActivity(), StoreCommodityDetailActivity.class);
                        intent.putExtra("id", listMallRecommend.get(3).getTargetId());
                        getActivity().startActivity(intent);
                        AnimUtil.intentSlidIn(getActivity());
                        break;
                    case "2"://跳帖子详情
                        intent = new Intent(getActivity(), InvitationPinterestDetailActivity.class);
                        intent.putExtra("id", listMallRecommend.get(3).getTargetId());
                        getActivity().startActivity(intent);
                        AnimUtil.intentSlidIn(getActivity());
                        break;
                    case "3"://外部链接
                        Uri uri = Uri.parse(listMallRecommend.get(3).getLinkUrl());
                        intent = new Intent(Intent.ACTION_VIEW, uri);
                        getActivity().startActivity(intent);
                        AnimUtil.intentSlidIn(getActivity());
                        break;
                }
                break;
        }
    }

    private void gotoDetail(int pos) {
        String type = listMallNotice.get(pos).getType();
        if(listMallNotice.get(pos).getTargetId()==null)return;
        Intent intent=null;
        switch (type){
            case "0"://跳商家

                intent=new Intent(getActivity(), StoreShopListVerticalStyleActivity.class);
                intent.putExtra("id",listMallNotice.get(pos).getTargetId());
                getActivity().startActivity(intent);
                AnimUtil.intentSlidIn(getActivity());
                break;
            case "1"://跳商品
                intent=new Intent(getActivity(), StoreCommodityDetailActivity.class);
                intent.putExtra("id",listMallNotice.get(pos).getTargetId());
                getActivity().startActivity(intent);
                AnimUtil.intentSlidIn(getActivity());
                break;
            case "2"://跳帖子
                intent=new Intent(getActivity(), InvitationPinterestDetailActivity.class);
                intent.putExtra("id",listMallNotice.get(pos).getTargetId());
                getActivity().startActivity(intent);
                AnimUtil.intentSlidIn(getActivity());
                break;
        }
    }

    class MyListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int arg0) {
            pageControl.selectPage(arg0);
        }

    }


}
