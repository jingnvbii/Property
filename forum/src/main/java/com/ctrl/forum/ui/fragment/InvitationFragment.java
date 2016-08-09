package com.ctrl.forum.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarFragment;
import com.beanu.arad.utils.AndroidUtil;
import com.beanu.arad.utils.AnimUtil;
import com.ctrl.forum.HorzitalGridView.adapter.AppAdapter;
import com.ctrl.forum.HorzitalGridView.adapter.MyViewPagerAdapter;
import com.ctrl.forum.HorzitalGridView.control.PageControl;
import com.ctrl.forum.R;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.customview.GridViewForScrollView;
import com.ctrl.forum.dao.InvitationDao;
import com.ctrl.forum.entity.Banner;
import com.ctrl.forum.entity.Invitation_listview;
import com.ctrl.forum.entity.Notice;
import com.ctrl.forum.entity.Post;
import com.ctrl.forum.entity.PostKind;
import com.ctrl.forum.entity.Recommend;
import com.ctrl.forum.loopview.HomeAutoSwitchPicHolder;
import com.ctrl.forum.ui.activity.Invitation.InvitationDetailFromPlatformActivity;
import com.ctrl.forum.ui.activity.Invitation.InvitationPinterestDetailActivity;
import com.ctrl.forum.ui.activity.Invitation.InvitationPullDownActivity;
import com.ctrl.forum.ui.activity.store.StoreCommodityDetailActivity;
import com.ctrl.forum.ui.activity.store.StoreShopListVerticalStyleActivity;
import com.ctrl.forum.ui.adapter.InvitationGridViewAdapter;
import com.ctrl.forum.ui.adapter.InvitationListViewAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 帖子 fragment
 * Created by jason on 2016/4/7.
 */
public class InvitationFragment extends ToolBarFragment implements View.OnClickListener{
    @InjectView(R.id.ll_linear_layout)//父布局
            LinearLayout ll_linear_layout;

    DisplayMetrics dm;
    private int NUM = 4; // 每行显示个数
    private int hSpacing = 20;// 水平间距
  //  private List<Kind> kindList;
    private InvitationListViewAdapter invitationListViewAdapter;
    private List<Invitation_listview> list;
    private InvitationDao idao;

    private List<Banner> listBanner;
    private List<Notice> listNotice;
    private List<Recommend> listRecommend;
    private List<PostKind> listPostKind;

    private List<Post> listPost;
   // private List<PostImage> listPostImage;
    private ImageView iv_recommend_1;
    private ImageView iv_recommend_2;
    private ImageView iv_recommend_3;
    private ImageView iv_recommend_4;

   private PullToRefreshListView lv_invitation_fragment_home;

    private TextView tv_change;
    private ListView lv01;
//    private List<Merchant> listMerchant;
    private FrameLayout framelayout;
    private GridViewForScrollView gridView1;
    private int PAGE_NUM=1;


    /*
    * 实现文字上下轮播
    * */
    private boolean isloop = true;
    private List<String> listNoticeString = new ArrayList<>();
    private int item = 0;
    private AnimationSet set = new AnimationSet(true);
    private AlphaAnimation animation = new AlphaAnimation(0.1f, 1.0f);
    private AlphaAnimation animation2 = new AlphaAnimation(1.0f, 1.0f);
    private TranslateAnimation ta = new TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF,
            0, Animation.RELATIVE_TO_SELF, 2.0f,
            Animation.RELATIVE_TO_SELF, 0);
    private TranslateAnimation ta2 = new TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF,
            0, Animation.RELATIVE_TO_SELF,0,
            Animation.RELATIVE_TO_SELF,-2.0f);

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    if (isloop) {
                        if (listNoticeString.size()!=0)
                        tv_change.setText(listNoticeString.get(item % listNoticeString.size()));
                        tv_change.setAnimation(set);
                        tv_change.startAnimation(set);
                      //  item += 1;
                    }
            }
        }

        ;
    };
    private HomeAutoSwitchPicHolder mAutoSwitchPicHolder;
    private ArrayList<String> mData;
    private ImageView iv_invitation_notice_image;
    private ViewPager myViewPager;
    private static final float APP_PAGE_SIZE = 10.0f;
    private MyViewPagerAdapter viewpagerAdapter;
    LayoutInflater inflater;

    private PageControl pageControl;

    private Map<Integer, GridView> map;
    private LinearLayout viewGroup;
    private LinearLayout ll_tuijian;
    private LinearLayout ll_notice;
    private View loadNoneView;
    private RelativeLayout rl_footer;
    private TextView tv_footer;
    private ProgressBar progressBar;
 //   private int bol=1;


    public static InvitationFragment newInstance() {
        InvitationFragment fragment = new InvitationFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  showProgress(true);
        //bol=1;
        idao = new InvitationDao(this);
        invitationListViewAdapter = new InvitationListViewAdapter(getActivity());
        inflater = getActivity().getLayoutInflater();
        ViewGroup main = (ViewGroup) inflater.inflate(R.layout.fragment_invitation_home_header,
                null);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }




    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invitation, container, false);
        ButterKnife.inject(this, view);
        lv_invitation_fragment_home=(PullToRefreshListView)view.findViewById(R.id.lv_invitation_fragment_home);
       // scrollView.setHorizontalScrollBarEnabled(false);// 隐藏滚动条11
        initView();
        getScreenDen();
        //公告轮播控件初始化
        initNoticeView();
        initData();
        return view;
    }

    private void initNoticeView() {
        ta.setDuration(800);
     //   animation.setDuration(500);
     //   animation2.setDuration(500);
        ta2.setDuration(800);
        ta.setStartOffset(4000);
        set.addAnimation(animation);
        set.addAnimation(ta);
        set.addAnimation(ta2);
        set.addAnimation(animation2);
        set.setRepeatMode(Animation.REVERSE);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                item += 1;
                if (listNotice.size() > 1)
                    handler.sendEmptyMessage(1);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private void initView() {
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        View headview = getActivity().getLayoutInflater().inflate(R.layout.fragment_invitation_home_header, lv_invitation_fragment_home, false);
        headview.setLayoutParams(layoutParams);
         lv01 = lv_invitation_fragment_home.getRefreshableView();
        tv_change=(TextView)headview.findViewById(R.id.tv_change);
        iv_invitation_notice_image=(ImageView)headview.findViewById(R.id.iv_invitation_notice_image);
        framelayout=(FrameLayout)headview.findViewById(R.id.framelayout);
        ll_tuijian=(LinearLayout)headview.findViewById(R.id.ll_tuijian);
        ll_notice=(LinearLayout)headview.findViewById(R.id.ll_notice);
        iv_recommend_1=(ImageView)headview.findViewById(R.id.iv_recommend_1);
        iv_recommend_2=(ImageView)headview.findViewById(R.id.iv_recommend_2);
        iv_recommend_3=(ImageView)headview.findViewById(R.id.iv_recommend_3);
        iv_recommend_4=(ImageView)headview.findViewById(R.id.iv_recommend_4);
        gridView1=(GridViewForScrollView)headview.findViewById(R.id.gridView1);
        myViewPager=(ViewPager)headview.findViewById(R.id.myviewpager);
       viewGroup=(LinearLayout)headview.findViewById(R.id.viewGroup);
        lv01.addHeaderView(headview);
        tv_change.setOnClickListener(this);
        loadNoneView = getActivity().getLayoutInflater().inflate(R.layout.load_more, null);
        rl_footer=(RelativeLayout)loadNoneView.findViewById(R.id.rl_footer);
        tv_footer=(TextView)loadNoneView.findViewById(R.id.tv_load_more);
        progressBar=(ProgressBar)loadNoneView.findViewById(R.id.secondBar);
        lv01.addFooterView(loadNoneView);
        setLoopViewHeight();
        lv_invitation_fragment_home.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                rl_footer.setVisibility(View.VISIBLE);
                rl_footer.setPadding(0, 0, 0, 0);
                tv_footer.setText("加载更多。。。");
                progressBar.setVisibility(View.VISIBLE);
                if(listPost==null||listPost.size()==0) return;
                PAGE_NUM+=1;
                //  showProgress(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //idao.requestInitPostHomePage();
                        idao.requestPostListByCategory(Arad.preferences.getString("memberId"), "", "0", "", "", PAGE_NUM, Constant.PAGE_SIZE);
                    }
                }, 500);
            }
        });

    }

    /*
* 设置轮播图高度
* */
    private void setLoopViewHeight() {
        framelayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                (int) (AndroidUtil.getDeviceWidth(getActivity()) * Constant.SCALE_LOOP)));

    }

    /*
 * 轮播图
 * */
    private void setLoopView() {
        framelayout.setVisibility(View.VISIBLE);
        // 1.创建轮播的holder
        mAutoSwitchPicHolder = new HomeAutoSwitchPicHolder(getActivity());
        // 2.得到轮播图的视图view
        View autoPlayPicView = mAutoSwitchPicHolder.getRootView();
        // 把轮播图的视图添加到主界面中
        framelayout.addView(autoPlayPicView);
        //4. 为轮播图设置数据1
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

    private void initData() {
       // showProgress(true);
        idao.requestPostListByCategory(Arad.preferences.getString("memberId"), "", "0", "", "", PAGE_NUM, Constant.PAGE_SIZE);
        idao.requestInitPostHomePage();
        lv_invitation_fragment_home.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
       /* lv_invitation_fragment_home.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                PAGE_NUM += 1;
                //  showProgress(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        idao.requestInitPostHomePage();
                        idao.requestPostListByCategory(Arad.preferences.getString("memberId"), "", "0", "", "", PAGE_NUM, Constant.PAGE_SIZE);
                    }
                }, 500);
            }
        });*/

        lv_invitation_fragment_home.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                rl_footer.setVisibility(View.GONE);
                if (listPost!=null)
                    listPost.clear();
                if (listNoticeString!=null){
                    listNoticeString.clear();
                }
                PAGE_NUM = 1;
                //  showProgress(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        idao.requestPostListByCategory(Arad.preferences.getString("memberId"), "", "0", "", "", PAGE_NUM, Constant.PAGE_SIZE);
                        idao.requestInitPostHomePage();
                    }
                }, 500);
            }
        });
      /*  lv_invitation_fragment_home.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (listPost!=null)
                listPost.clear();
                if (listNoticeString!=null){
                    listNoticeString.clear();
                }
                PAGE_NUM = 1;
                //  showProgress(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        idao.requestInitPostHomePage();
                        idao.requestPostListByCategory(Arad.preferences.getString("memberId"), "", "0", "", "", PAGE_NUM, Constant.PAGE_SIZE);
                    }
                }, 500);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM += 1;
                //  showProgress(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        idao.requestInitPostHomePage();
                        idao.requestPostListByCategory(Arad.preferences.getString("memberId"), "", "0", "", "", PAGE_NUM, Constant.PAGE_SIZE);
                    }
                }, 500);
            }
        });*/
        lv01.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position==listPost.size()+lv01.getHeaderViewsCount()){
                    return;
                }
                Intent intent = null;
                String type = listPost.get(position - lv01.getHeaderViewsCount()).getSourceType();
                String contentType = listPost.get(position - lv01.getHeaderViewsCount()).getContentType();
                switch (contentType) {
                    case "0":
                        switch (type) {
                            case "0"://平台
                                intent = new Intent(getActivity(), InvitationDetailFromPlatformActivity.class);
                                intent.putExtra("id", listPost.get(position - lv01.getHeaderViewsCount()).getId());
                                startActivity(intent);
                                AnimUtil.intentSlidIn(getActivity());

                                break;
                            case "1"://app
                                intent = new Intent(getActivity(), InvitationPinterestDetailActivity.class);
                                intent.putExtra("id", listPost.get(position - lv01.getHeaderViewsCount()).getId());
                                startActivityForResult(intent, 666);
                                AnimUtil.intentSlidIn(getActivity());
                                break;
                        }
                        break;
                    case "1":
                        Uri uri = Uri.parse(listPost.get(position - lv01.getHeaderViewsCount()).getArticleLink());
                        intent = new Intent(Intent.ACTION_VIEW, uri);
                        getActivity().startActivity(intent);
                        AnimUtil.intentSlidIn(getActivity());
                        break;
                    case "2":
                        intent = new Intent(getActivity(), StoreCommodityDetailActivity.class);
                        intent.putExtra("id", listPost.get(position - lv01.getHeaderViewsCount()).getLinkItemId());
                        getActivity().startActivity(intent);
                        AnimUtil.intentSlidIn(getActivity());
                        break;
                    case "3":
                        intent = new Intent(getActivity(), StoreShopListVerticalStyleActivity.class);
                        intent.putExtra("id", listPost.get(position - lv01.getHeaderViewsCount()).getLinkItemId());
                        getActivity().startActivity(intent);
                        AnimUtil.intentSlidIn(getActivity());
                        break;
                }

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==666&&resultCode==667){
            listPost.clear();
            PAGE_NUM=1;
            idao.requestPostListByCategory(Arad.preferences.getString("memberId"), "", "0", "", "",PAGE_NUM, Constant.PAGE_SIZE);
        }
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
       // showProgress(false);
        lv_invitation_fragment_home.onRefreshComplete();
        if(errorNo.equals("006")){
            if(listPost!=null&&listPost.size()>0){
                //   MessageUtils.showShortToast(getActivity(),"fsdfdsf");
                rl_footer.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                tv_footer.setText("已经到底了，请到别处看看");
            }
            if(listPost==null||listPost.size()==0){
                rl_footer.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        lv_invitation_fragment_home.onRefreshComplete();
        if (requestCode == 0) {
            listBanner = idao.getListBanner();
            listPostKind = idao.getListPostKind();
            listNotice = idao.getListNotice();
            listRecommend = idao.getListRecommend();
           // MessageUtils.showShortToast(getActivity(), "初始化成功");
            setValue();
            if(listRecommend!=null&&listRecommend.size()>0) {
                initRecommend();//推荐列表初始化
            }
            if(listNotice!=null&&listNotice.size()>0) {
                initNotice();//公告栏数据初始化
            }
            if(listBanner!=null&&listBanner.size()>0) {
                //调用轮播图
                setLoopView();
            }
            initViewPager();
            viewpagerAdapter = new MyViewPagerAdapter(getActivity(), map);
            myViewPager.setOnPageChangeListener(new MyListener());
            myViewPager.setAdapter(viewpagerAdapter);
            showProgress(false);
        }

        if (requestCode == 1) {
            //bol=0;
          //  MessageUtils.showShortToast(getActivity(), "获取帖子列表成功");
            listPost=idao.getListPost();
            if(listPost!=null) {
               invitationListViewAdapter.setList(listPost);
               showProgress(false);
            }
        }


    }

    private void initViewPager() {
        final int PageCount = (int) Math.ceil(listPostKind.size() / APP_PAGE_SIZE);
        map = new HashMap<Integer, GridView>();
        for (int i = 0; i < PageCount; i++) {
            GridView appPage = new GridView(getActivity());
            final AppAdapter adapter =new AppAdapter(getActivity(), listPostKind, i);
            appPage.setAdapter(adapter);
            appPage.setNumColumns(5);
            appPage.setOnItemClickListener(adapter);
            map.put(i, appPage);

        }

       /* ViewGroup main = (ViewGroup) inflater.inflate(R.layout.fragment_invitation_home_header,
                null);
        // group是R.layou.main中的负责包裹小圆点的LinearLayout.
        ViewGroup group = (ViewGroup) main.findViewById(R.id.viewGroup);*/
        pageControl = new PageControl(getActivity(), viewGroup, PageCount);
      //  getActivity().setContentView(main);

    }

    private void initNotice() {
        ll_notice.setVisibility(View.VISIBLE);
        if(idao.getListNoticeImage()!=null&&idao.getListNoticeImage().size()>0) {
            Arad.imageLoader.load(idao.getListNoticeImage().get(0).getImgUrl()).placeholder(R.mipmap.jinrigonggao).into(iv_invitation_notice_image);
        }
        for (int i = 0; i < listNotice.size(); i++) {
            listNoticeString.add(listNotice.get(i).getContent());
        }
       // tv_change.setText(listNoticeString.get(item));
        //实现自动切换界面
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(1);
               /* while (isloop) {
                    //系统时钟的睡眠方法---->电量的消耗很少。
                 //   SystemClock.sleep(5000);

                }*/
            }
        }).start();

    }

    private void initRecommend() {
        ll_tuijian.setVisibility(View.VISIBLE);
        if (listRecommend.size() > 0&&listRecommend.get(0).getImgUrl()!=null&&!listRecommend.get(0).getImgUrl().equals("")) {
            Arad.imageLoader.load(listRecommend.get(0).getImgUrl()).placeholder(R.mipmap.default_error).resize(400,400).into(iv_recommend_1);
            iv_recommend_1.setOnClickListener(this);
        }
        if (listRecommend.size() > 1&&listRecommend.get(1).getImgUrl()!=null&&!listRecommend.get(1).getImgUrl().equals("")) {
            Arad.imageLoader.load(listRecommend.get(1).getImgUrl()).placeholder(R.mipmap.default_error).resize(400, 400).into(iv_recommend_2);
            iv_recommend_2.setOnClickListener(this);
        }
        if (listRecommend.size() > 2&&listRecommend.get(2).getImgUrl()!=null&&!listRecommend.get(2).getImgUrl().equals("")) {
            Arad.imageLoader.load(listRecommend.get(2).getImgUrl()).placeholder(R.mipmap.default_error).resize(400, 400).into(iv_recommend_3);
            iv_recommend_3.setOnClickListener(this);
        }
        if (listRecommend.size() > 3&&listRecommend.get(3).getImgUrl()!=null&&!listRecommend.get(3).getImgUrl().equals("")) {
            Arad.imageLoader.load(listRecommend.get(3).getImgUrl()).placeholder(R.mipmap.default_error).resize(400,400).into(iv_recommend_4);
            iv_recommend_4.setOnClickListener(this);
        }
    }

    private void setValue() {
        InvitationGridViewAdapter adapter = new InvitationGridViewAdapter(getActivity());
        adapter.setList(listPostKind);
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
            gridView1.setNumColumns(count);
        } else {
            gridView1.setNumColumns(columns);
        }

        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), InvitationPullDownActivity.class);
                intent.putExtra("channelId", listPostKind.get(position).getId());
                getActivity().startActivity(intent);
            }
        });

    }

    private void getScreenDen() {
        dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
            lv_invitation_fragment_home.setAdapter(invitationListViewAdapter);
    }

    @Override
    public void onClick(View v) {
        String type=null;
        Intent intent=null;
        switch (v.getId()){
            case R.id.tv_change:
                gotoDetail(item % listNoticeString.size());
                break;
            case R.id.iv_recommend_1:
                type=listRecommend.get(0).getType();
                switch (type){
                    case "0"://跳商家
                        intent=new Intent(getActivity(), StoreShopListVerticalStyleActivity.class);
                        intent.putExtra("id",listRecommend.get(0).getTargetId());
                        getActivity().startActivity(intent);
                        AnimUtil.intentSlidIn(getActivity());
                        break;
                    case "1"://跳商品详情
                        intent=new Intent(getActivity(), StoreCommodityDetailActivity.class);
                        intent.putExtra("id",listRecommend.get(0).getTargetId());
                        getActivity().startActivity(intent);
                        AnimUtil.intentSlidIn(getActivity());
                        break;
                    case "2"://跳帖子详情
                        intent=new Intent(getActivity(), InvitationPinterestDetailActivity.class);
                        intent.putExtra("id",listRecommend.get(0).getTargetId());
                        getActivity().startActivity(intent);
                        AnimUtil.intentSlidIn(getActivity());
                        break;
                    case "3"://外部链接
                        Uri uri = Uri.parse(listRecommend.get(0).getTargetUrl());
                        intent = new Intent(Intent.ACTION_VIEW, uri);
                        getActivity().startActivity(intent);
                        AnimUtil.intentSlidIn(getActivity());
                        break;

                }
                break;
            case R.id.iv_recommend_2:
                type=listRecommend.get(1).getType();
                switch (type){
                    case "0"://跳商家
                        intent=new Intent(getActivity(), StoreShopListVerticalStyleActivity.class);
                        intent.putExtra("id",listRecommend.get(1).getTargetId());
                        getActivity().startActivity(intent);
                        AnimUtil.intentSlidIn(getActivity());
                        break;
                    case "1"://跳商品详情
                        intent=new Intent(getActivity(), StoreCommodityDetailActivity.class);
                        intent.putExtra("id",listRecommend.get(1).getTargetId());
                        getActivity().startActivity(intent);
                        AnimUtil.intentSlidIn(getActivity());
                        break;
                    case "2"://跳帖子详情
                        intent=new Intent(getActivity(), InvitationPinterestDetailActivity.class);
                        intent.putExtra("id",listRecommend.get(1).getTargetId());
                        getActivity().startActivity(intent);
                        AnimUtil.intentSlidIn(getActivity());
                        break;
                    case "3"://外部链接
                        Uri uri = Uri.parse(listRecommend.get(1).getTargetUrl());
                        intent = new Intent(Intent.ACTION_VIEW, uri);
                        getActivity().startActivity(intent);
                        AnimUtil.intentSlidIn(getActivity());
                        break;

                }

                break;
            case R.id.iv_recommend_3:
                type=listRecommend.get(2).getType();
                switch (type){
                    case "0"://跳商家
                        intent=new Intent(getActivity(), StoreShopListVerticalStyleActivity.class);
                        intent.putExtra("id",listRecommend.get(2).getTargetId());
                        getActivity().startActivity(intent);
                        AnimUtil.intentSlidIn(getActivity());
                        break;
                    case "1"://跳商品详情
                        intent=new Intent(getActivity(), StoreCommodityDetailActivity.class);
                        intent.putExtra("id",listRecommend.get(2).getTargetId());
                        getActivity().startActivity(intent);
                        AnimUtil.intentSlidIn(getActivity());
                        break;
                    case "2"://跳帖子详情
                        intent=new Intent(getActivity(), InvitationPinterestDetailActivity.class);
                        intent.putExtra("id",listRecommend.get(2).getTargetId());
                        getActivity().startActivity(intent);
                        AnimUtil.intentSlidIn(getActivity());
                        break;
                    case "3"://外部链接
                        Uri uri = Uri.parse(listRecommend.get(2).getTargetUrl());
                        intent = new Intent(Intent.ACTION_VIEW, uri);
                        getActivity().startActivity(intent);
                        AnimUtil.intentSlidIn(getActivity());
                        break;

                }

                break;
            case R.id.iv_recommend_4:
                type=listRecommend.get(3).getType();
                switch (type){
                    case "0"://跳商家
                        intent=new Intent(getActivity(), StoreShopListVerticalStyleActivity.class);
                        intent.putExtra("id",listRecommend.get(3).getTargetId());
                        getActivity().startActivity(intent);
                        AnimUtil.intentSlidIn(getActivity());
                        break;
                    case "1"://跳商品详情
                        intent=new Intent(getActivity(), StoreCommodityDetailActivity.class);
                        intent.putExtra("id",listRecommend.get(3).getTargetId());
                        getActivity().startActivity(intent);
                        AnimUtil.intentSlidIn(getActivity());
                        break;
                    case "2"://跳帖子详情
                        intent=new Intent(getActivity(), InvitationPinterestDetailActivity.class);
                        intent.putExtra("id",listRecommend.get(3).getTargetId());
                        getActivity().startActivity(intent);
                        AnimUtil.intentSlidIn(getActivity());
                        break;
                    case "3"://外部链接
                        Uri uri = Uri.parse(listRecommend.get(3).getTargetUrl());
                        intent = new Intent(Intent.ACTION_VIEW, uri);
                        getActivity().startActivity(intent);
                        AnimUtil.intentSlidIn(getActivity());
                        break;
                }
                break;
        }

    }

    private void gotoDetail(int pos) {
        if (pos>=listNotice.size())return;
        String type = listNotice.get(pos).getType();
        if(listNotice.get(pos).getTargetId()==null)return;
        Intent intent=null;
        switch (type){
            case "0"://跳商家
               intent=new Intent(getActivity(), StoreShopListVerticalStyleActivity.class);
                intent.putExtra("id",listNotice.get(pos).getTargetId());
                getActivity().startActivity(intent);
                AnimUtil.intentSlidIn(getActivity());
                break;
            case "1"://跳商品
                 intent=new Intent(getActivity(), StoreCommodityDetailActivity.class);
                intent.putExtra("id",listNotice.get(pos).getTargetId());
                getActivity().startActivity(intent);
                AnimUtil.intentSlidIn(getActivity());
                break;
            case "2"://跳帖子
                intent=new Intent(getActivity(), InvitationPinterestDetailActivity.class);
                intent.putExtra("id",listNotice.get(pos).getTargetId());
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
