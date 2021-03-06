package com.ctrl.forum.ui.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarFragment;
import com.beanu.arad.utils.AnimUtil;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.customview.ShareDialog;
import com.ctrl.forum.customview.XListView;
import com.ctrl.forum.dao.InvitationDao;
import com.ctrl.forum.dao.PlotDao;
import com.ctrl.forum.entity.Banner;
import com.ctrl.forum.entity.Post;
import com.ctrl.forum.loopview.HomeAutoSwitchPicHolder;
import com.ctrl.forum.ui.activity.Invitation.InvitationPinterestDetailActivity;
import com.ctrl.forum.ui.activity.LoginActivity;
import com.ctrl.forum.ui.activity.MainActivity;
import com.ctrl.forum.ui.activity.mine.MineFindFlotActivity;
import com.ctrl.forum.ui.activity.plot.PlotAddInvitationActivity;
import com.ctrl.forum.ui.activity.plot.PlotRimServeActivity;
import com.ctrl.forum.ui.activity.plot.PlotSearchResultActivity;
import com.ctrl.forum.ui.activity.store.StoreCommodityDetailActivity;
import com.ctrl.forum.ui.activity.store.StoreShopListVerticalStyleActivity;
import com.ctrl.forum.ui.adapter.PlotListViewFriendStyleAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

/**
 * 小区 fragment
 * Created by jason on 2016/4/7.
 */
public class PlotFragment extends ToolBarFragment implements View.OnClickListener, XListView.IXListViewListener ,PlatformActionListener{
    @InjectView(R.id.lv_content)
    PullToRefreshListView lv_content;
    @InjectView(R.id.tv_plot_name)
    TextView tv_plot_name; //小区名2
    @InjectView(R.id.rim_post)
    TextView rim_post; //发帖
    @InjectView(R.id.rim_serve)
    TextView rim_serve; //周边服务
    @InjectView(R.id.iv_back)
    ImageView iv_back;
    /*@InjectView(R.id.rl_search)
    RelativeLayout rl_search;*/
   /* @InjectView(R.id.et_search)
    EditText et_search;*/
    @InjectView(R.id.iv_search)
    ImageView iv_search;

    private PlotListViewFriendStyleAdapter invitationListViewFriendStyleAdapter;

    private List<Post> posts;
    private String communityId;
    private PlotDao plotDao;
    private int PAGE_NUM =1;
    private String str="";
    private FrameLayout frameLayout;
    private HomeAutoSwitchPicHolder mAutoSwitchPicHolder;

    private ArrayList<String> mData;
    private List<Banner> listBanner;
    private InvitationDao idao;
    private PopupWindow mPopupWindow;

    //123
    private PopupWindow popupWindow;
    private PopupWindow popupWindow_share;
    private ShareDialog shareDialog;
    private Map<Integer,Boolean> isAdd = new HashMap<>();
    private Map<Integer,Integer> text = new HashMap<>();
    private View footView;
    private TextView endText;
    private ProgressBar progressBar;
    private TextView textView;
    private RelativeLayout rl_footer;
    private Boolean isFromLoad= false;
    private Boolean isDetail = true;

    public static PlotFragment newInstance() {
        PlotFragment fragment = new PlotFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plot, container, false);
        ButterKnife.inject(this, view);
       // rl_search.setVisibility(View.GONE);
        idao = new InvitationDao(this);

        ShareSDK.initSDK(getActivity());

        checkActivity();
        initView();

        //listview增加头部布局
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        View headview = getActivity().getLayoutInflater().inflate(R.layout.item_plot_header_view, lv_content, false);
        frameLayout = (FrameLayout) headview.findViewById(R.id.framelayout);
        headview.setLayoutParams(layoutParams);
        lv_content.getRefreshableView().addHeaderView(headview);

        //listview增加尾部布局
        lv_content.onRefreshComplete();
        footView= LayoutInflater.from(getActivity()).inflate(R.layout.load_more, null);
        progressBar = (ProgressBar) footView.findViewById(R.id.secondBar);
        textView = (TextView) footView.findViewById(R.id.tv_load_more);
        rl_footer = (RelativeLayout) footView.findViewById(R.id.rl_footer);

        /*endText = (TextView) footView.findViewById(R.id.tv_text);
        endText.setVisibility(View.GONE);
        endText.setTextColor(getResources().getColor(R.color.text_my));*/
        lv_content.getRefreshableView().addFooterView(footView);

        invitationListViewFriendStyleAdapter = new PlotListViewFriendStyleAdapter(getActivity());
        lv_content.setAdapter(invitationListViewFriendStyleAdapter);
        invitationListViewFriendStyleAdapter.setOnLove(this);

        invitationListViewFriendStyleAdapter.setOnShare(this);
        invitationListViewFriendStyleAdapter.setOnMoreDialog(this);

        communityId = Arad.preferences.getString("communityId");
        plotDao = new PlotDao(this);

        initData();

        lv_content.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        lv_content.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                idao.requestPostRotaingBanner("B_COMMUNITY_TOP");
                rl_footer.setVisibility(View.GONE);
                if (posts != null) {
                    posts.clear();
                }
                if (Arad.preferences.getString("memberId") != null && !Arad.preferences.getString("memberId").equals("")) {
                    PAGE_NUM = 1;
                    plotDao.queryCommunityPostList(Arad.preferences.getString("memberId"), communityId, PAGE_NUM + "", Constant.PAGE_SIZE + "");
                } else {
                    lv_content.onRefreshComplete();
                }
            }
        });

        lv_content.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                rl_footer.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                textView.setText("加载更多。。。");
                if (posts != null && posts.size()>0) {
                    PAGE_NUM += 1;
                    if (Arad.preferences.getString("memberId") != null && !Arad.preferences.getString("memberId").equals("")) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                plotDao.queryCommunityPostList(Arad.preferences.getString("memberId"), communityId, PAGE_NUM + "", Constant.PAGE_SIZE + "");
                            }
                        }, 500);
                        isFromLoad = true;
                    }
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rl_footer.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            textView.setText("该小区暂无数据,请到别处看看");
                        }
                    }, 500);
                }
            }
        });

        /*lv_content.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                endText.setVisibility(View.GONE);
                if (posts != null) {
                    PAGE_NUM += 1;
                    if (Arad.preferences.getString("memberId") != null && !Arad.preferences.getString("memberId").equals("")) {
                        plotDao.queryCommunityPostList(Arad.preferences.getString("memberId"), communityId, PAGE_NUM + "", Constant.PAGE_SIZE + "");
                    }
                } else {
                    lv_content.onRefreshComplete();
                }
            }
        });*/

        //跳转界面
        lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                isDetail = false;
                if (posts!=null) {
                    if (position == lv_content.getRefreshableView().getHeaderViewsCount() + posts.size()) {
                        return;
                    }
                    if (posts.get(position - 2) != null) {
                        if (posts.get(position - 2).getContentType() != null) {
                            String type = posts.get(position - 2).getContentType();
                            switch (type) {
                                case "0":
                                    intent = new Intent(getActivity(), InvitationPinterestDetailActivity.class);
                                    intent.putExtra("id", posts.get(position - 2).getId());
                                    intent.putExtra("reportid", posts.get(position - 2).getReporterId());
                                    getActivity().startActivity(intent);
                                    AnimUtil.intentSlidIn(getActivity());
                                    break;
                                case "1":
                                    Uri uri = Uri.parse(posts.get(position - 2).getArticleLink());
                                    intent = new Intent(Intent.ACTION_VIEW, uri);
                                    getActivity().startActivity(intent);
                                    AnimUtil.intentSlidIn(getActivity());
                                    break;
                                case "2":
                                    intent = new Intent(getActivity(), StoreCommodityDetailActivity.class);
                                    intent.putExtra("id", posts.get(position - 2).getLinkItemId());
                                    getActivity().startActivity(intent);
                                    break;
                                case "3":
                                    intent = new Intent(getActivity(), StoreShopListVerticalStyleActivity.class);
                                    intent.putExtra("id", posts.get(position - 2).getLinkItemId());
                                    getActivity().startActivity(intent);
                                    AnimUtil.intentSlidIn(getActivity());
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
            }
        });

        invitationListViewFriendStyleAdapter.setOnItemClickListener(new PlotListViewFriendStyleAdapter.OnItemClickListener() {
            @Override
            public void onItemZanClick(PlotListViewFriendStyleAdapter.ViewHolder v) {
                int position = v.getPosition();
                int tex = posts.get(position).getPraiseNum();
                if (isAdd.get(position) == null) {
                    if (posts.get(position).getPraiseState().equals("0")) {
                        isAdd.put(position, true);
                    } else {
                        isAdd.put(position, false);
                    }
                }

                if (text.get(position) == null) {
                    text.put(position, tex);
                }

                if (posts.get(position).getPraiseState() != null) {
                    if (isAdd.get(position)) {
                        idao.requesZambia("add", posts.get(position).getId(), Arad.preferences.getString("memberId")
                                , posts.get(position).getTitle(), posts.get(position).getContent());
                        v.tv_friend_style_zan_num.setText((text.get(position) + 1) + "");
                        text.put(position, text.get(position) + 1);
                        v.iv_friend_style_zan_num.setImageResource(R.mipmap.zan_blue_shixin);
                        //MessageUtils.showShortToast(getActivity(), "点赞成功");
                        isAdd.put(position, false);
                    } else {
                        idao.requesZambia("reduce", posts.get(position).getId(), Arad.preferences.getString("memberId")
                                , posts.get(position).getTitle(), posts.get(position).getContent());
                        //MessageUtils.showShortToast(getActivity(), "取消点赞");
                        v.tv_friend_style_zan_num.setText((text.get(position) - 1) + "");
                        text.put(position, text.get(position) - 1);
                        v.iv_friend_style_zan_num.setImageResource(R.mipmap.zan_blue);
                        isAdd.put(position, true);
                    }
                }
            }
        });

        idao.requestPostRotaingBanner("B_COMMUNITY_TOP");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MainActivity.isFrist) {
            if (isDetail) {
                if (!Arad.preferences.getString("memberId").equals("")) {
                    rl_footer.setVisibility(View.GONE);
                    isFromLoad = false;
                    communityId = Arad.preferences.getString("communityId");
                    tv_plot_name.setText(Arad.preferences.getString("communityName"));
                    if (posts != null) {
                        posts.clear();
                        invitationListViewFriendStyleAdapter.notifyDataSetChanged();
                    }
                    PAGE_NUM = 1;
                    plotDao.queryCommunityPostList(Arad.preferences.getString("memberId"),
                            communityId,
                            PAGE_NUM + "", Constant.PAGE_SIZE + "");
                }
            }
        }
    }

    /**
     * 轮播图
     */
    private void setLoopView() {
        // 1.创建轮播的holder
        mAutoSwitchPicHolder = new HomeAutoSwitchPicHolder(getActivity());
        // 2.得到轮播图的视图view
        View autoPlayPicView = mAutoSwitchPicHolder.getRootView();
        // 把轮播图的视图添加到主界面中
        frameLayout.addView(autoPlayPicView);
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

    /*
* 举报请求
* */
    public void requestJuBao(String postId,String reportId,PopupWindow popupWindow){
        idao.requePostReport(postId, "", reportId, Arad.preferences.getString("memberId"));
        mPopupWindow=popupWindow;
    }
    /*
 * 屏蔽作者请求
 * */
    public void requeMemberBlackListAdd(String reportId,PopupWindow popupWindow){
        idao.requeMemberBlackListAdd(Arad.preferences.getString("memberId"), reportId);
        mPopupWindow=popupWindow;
    }

    private void showShareDialog(View v) {
        View contentView = LayoutInflater.from(this.getActivity()).inflate(R.layout.share_dialog, null);

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

    //更多的弹出框
    private void showMoreDialog(View v,int position, final Post post) {

        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.popupwindow_friend_style_more, null);
        final TextView tv_friend_style_jubao = (TextView)contentView.findViewById(R.id.tv_friend_style_jubao);//举报
        TextView tv_friend_style_pinbi_zuozhe = (TextView)contentView.findViewById(R.id.tv_friend_style_pinbi_zuozhe);//屏蔽作者
        TextView tv_friend_style_cancel = (TextView)contentView.findViewById(R.id.tv_friend_style_cancel);//取消
        tv_friend_style_pinbi_zuozhe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requeMemberBlackListAdd(post.getReporterId(), popupWindow);
            }
        });
        tv_friend_style_jubao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestJuBao(post.getId(), post.getReporterId(), popupWindow);
            }
        });

        popupWindow = new PopupWindow(contentView,
                getResources().getDisplayMetrics().widthPixels,
                getResources().getDisplayMetrics().heightPixels, true);
        // 设置SelectPicPopupWindow弹出窗体可点击
        popupWindow.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        popupWindow.setAnimationStyle(R.style.AnimBottom);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(dw);

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
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        // 设置好参数之后再show
        popupWindow.showAtLocation(getActivity().findViewById(R.id.framelayout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 120);

    }

    //检查依附的activity
    private void checkActivity() {
        Bundle bundle = getArguments();
        str = bundle.getString("str");
        if (str.equals("我")){
           //iv_back.setImageDrawable(getResources().getDrawable(R.mipmap.white_arrow_left_none));
            iv_back.setVisibility(View.VISIBLE);
            iv_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
        }
    }

    private void initView() {
        if (Arad.preferences.getString("communityName").equals("")){
            tv_plot_name.setText("选择小区");
        }else{
            tv_plot_name.setText(Arad.preferences.getString("communityName"));
        }

        tv_plot_name.setOnClickListener(this);
        rim_post.setOnClickListener(this);
        rim_serve.setOnClickListener(this);
        iv_search.setOnClickListener(this);
    }

    private void initData() {
        if (Arad.preferences.getString("memberId")!=null && !Arad.preferences.getString("memberId").equals("")) {
            if(communityId==null || communityId.equals("")){
                MessageUtils.showShortToast(getActivity(),"当前无小区，请选择加入小区！");
            }else {
                plotDao.queryCommunityPostList(Arad.preferences.getString("memberId"), communityId, PAGE_NUM + "", Constant.PAGE_SIZE + "");
            }
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        lv_content.onRefreshComplete();
        if (requestCode==6){
            posts = plotDao.getPlotPost();
            if (posts!=null){
                invitationListViewFriendStyleAdapter.setList(posts);
            }
        }
        if (requestCode == 11) {
            MessageUtils.showShortToast(this.getActivity(), "举报成功");
            mPopupWindow.dismiss();
        }
        if (requestCode == 10) {
            MessageUtils.showShortToast(this.getActivity(), "屏蔽作者成功");
            mPopupWindow.dismiss();
        }

        if (requestCode==19){
           listBanner = idao.getListBanner();
            if (listBanner!=null){
                frameLayout.setVisibility(View.VISIBLE);
               setLoopView();
            }else{
                frameLayout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        lv_content.onRefreshComplete();
        if(errorNo.equals("006")){
            if(posts!=null && posts.size()>0 && isFromLoad){
                progressBar.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
                textView.setText("已经到底了，请到别处看看");
            }
            if (posts==null || posts.size()==0){
                rl_footer.setVisibility(View.GONE);
               /* progressBar.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);*/
            }
        }
    }

    @Override
    public void onClick(View v) {
        Object id = v.getTag();
        switch (v.getId()){
            case R.id.tv_plot_name:
                if (Arad.preferences.getString("memberId").equals("")){
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }else {
                    isDetail = true;
                    Intent intent = new Intent(getActivity(),MineFindFlotActivity.class);
                    startActivityForResult(intent,1203);
                }
                break;
            case R.id.rim_post:
                if (Arad.preferences.getString("memberId").equals("")){
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }else {
                    isDetail = true;
                    Intent intent = new Intent(getActivity(),PlotAddInvitationActivity.class);
                    startActivityForResult(intent,7788);
                }
                break;
            case R.id.rim_serve:
                if (Arad.preferences.getString("memberId").equals("")){
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }else {
                    isDetail = true;
                    startActivity(new Intent(getActivity(), PlotRimServeActivity.class));
                }
                break;
            case R.id.rl_friend_style_share:
                //showShareDialog(this.getView());
                int position = (int) id;
                clickShare(posts.get(position));
                break;
            case R.id.rl_friend_style_more:
                int position1 = (int)id;
                showMoreDialog(v, position1, posts.get(position1));
                break;
            case R.id.iv_search:
                if (Arad.preferences.getString("memberId").equals("")){
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }else {
                    isDetail = true;
                    startActivity(new Intent(getActivity(), PlotSearchResultActivity.class));
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       if (resultCode==getActivity().RESULT_OK){
          /* if (requestCode==1203){
               rl_footer.setVisibility(View.GONE);
               isFromLoad = false;
               communityId = Arad.preferences.getString("communityId");
               tv_plot_name.setText(Arad.preferences.getString("communityName"));
               if (posts != null) {
                   posts.clear();
                   invitationListViewFriendStyleAdapter.notifyDataSetChanged();
               }
               PAGE_NUM=1;
               plotDao.queryCommunityPostList(Arad.preferences.getString("memberId"),
                       communityId,
                       PAGE_NUM + "", Constant.PAGE_SIZE + "");
           }*/
           if (requestCode==7788){
               rl_footer.setVisibility(View.GONE);
               isFromLoad = false;
               if (posts != null) {
                   posts.clear();
                   invitationListViewFriendStyleAdapter.notifyDataSetChanged();
               }
               PAGE_NUM=1;
               plotDao.queryCommunityPostList(Arad.preferences.getString("memberId"),
                       communityId,
                       PAGE_NUM + "", Constant.PAGE_SIZE + "");
           }
       }else {
           return;
       }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String setTitle(Post post){
        if (post.getTitle()==null || post.getTitle().equals("")){
            return "莱州人";
        }else{
            return post.getTitle();
        }
    }

    public void setBimp(Platform.ShareParams sp,Post post){
        if (post.getPostImgList()==null || post.getPostImgList().size()==0){
            Log.e("bimap==========","1235");
            //设为图片路径
            sp.setImagePath(Constant.SHARE_IMAGE_PATH);
            //sp.setImageData(BitmapFactory.decodeResource(getActivity().getResources(), R.mipmap.logo));
        }else{
            sp.setImageUrl(post.getPostImgList().get(0).getImg());//网络图片rul
        }
    }

    public void clickShare(final Post post){
        shareDialog = new ShareDialog(getActivity());
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
               /* sp.setTitle("烟台项目");
                sp.setText("欢迎加入");*/

                sp.setTitle(setTitle(post));
                sp.setText(post.getContent());
                setBimp(sp,post);
                sp.setTitleUrl(Constant.SHARE_INVITION_URL + post.getId());
                //3、非常重要：获取平台对象
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
                qq.setPlatformActionListener(PlotFragment.this); // 设置分享事件回调
                // 执行分享
                qq.share(sp);
            }

        });

        shareDialog.setWeixinButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Platform.ShareParams sp = new Platform.ShareParams();
                sp.setShareType(Platform.SHARE_WEBPAGE);//非常重要：一定要设置分享属性
               /* sp.setTitle("烟台项目");  //分享标题
                sp.setText("欢迎加入");   //分享文本
                sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul*/
                //sp.setUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");   //网友点进链接后，可以看到分享的详情

                sp.setText(post.getContent());
                sp.setTitle(setTitle(post));
                setBimp(sp, post);//设置标题与图片
                sp.setUrl(Constant.SHARE_INVITION_URL + post.getId());

                //3、非常重要：获取平台对象
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                wechat.setPlatformActionListener(PlotFragment.this); // 设置分享事件回调
                // 执行分享
                wechat.share(sp);
            }
        });
        shareDialog.setSinaWeiBoButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //2、设置分享内容
                Platform.ShareParams sp = new Platform.ShareParams();
                //sp.setText("我是新浪微博分享文本，啦啦啦~http://uestcbmi.com/"); //分享文本
                //sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
                //sp.setUrl(Constant.SHARE_INVITION_URL + post.getId());
                //sp.setText("我是新浪微博分享文本，啦啦啦~"+Constant.SHARE_INVITION_URL+post.getId());   //分享文本

                setBimp(sp, post);//设置标题与图片
                sp.setText(setTitle(post)+Constant.SHARE_INVITION_URL + post.getId());
                //3、非常重要：获取平台对象
                Platform sinaWeibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                sinaWeibo.setPlatformActionListener(PlotFragment.this); // 设置分享事件回调
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
                /*sp.setTitle("我是朋友圈分享标题");  //分享标题
                sp.setText("我是朋友圈分享文本，啦啦啦~http://uestcbmi.com/");   //分享文本
                sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
                sp.setUrl("http://sharesdk.cn");   //网友点进链接后，可以看到分享的详情*/

                setBimp(sp, post);//设置标题与图片
                sp.setText(setTitle(post)+Constant.SHARE_INVITION_URL + post.getId());
                //3、非常重要：获取平台对象
                Platform wechatMoments = ShareSDK.getPlatform(WechatMoments.NAME);
                wechatMoments.setPlatformActionListener(PlotFragment.this); // 设置分享事件回调
                // 执行分享
                wechatMoments.share(sp);
            }
        });
        shareDialog.setTecentWeiBoButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //2、设置分享内容
                Platform.ShareParams sp = new Platform.ShareParams();
               /* sp.setTitle("我是腾讯微博分享标题");  //分享标题
                sp.setText("我是腾讯微博分享文本，啦啦啦~http://uestcbmi.com/");   //分享文本
                sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
                sp.setUrl("http://sharesdk.cn");   //网友点进链接后，可以看到分享的详情*/
               // sp.setUrl(Constant.SHARE_INVITION_URL+post.getId());
                //sp.setText("我是腾讯微博分享文本，啦啦啦~"+Constant.SHARE_INVITION_URL+post.getId());   //分享文本

                setBimp(sp, post);//设置标题与图片
                sp.setText(setTitle(post)+Constant.SHARE_INVITION_URL + post.getId());
                //3、非常重要：获取平台对象
                Platform tecentWeibo = ShareSDK.getPlatform(TencentWeibo.NAME);
                tecentWeibo.setPlatformActionListener(PlotFragment.this); // 设置分享事件回调
                // 执行分享
                tecentWeibo.share(sp);
            }
        });

        shareDialog.setEmailButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //2、设置分享内容
                Platform.ShareParams sp = new Platform.ShareParams();
               /* sp.setTitle("我是邮件分享标题");  //分享标题
                sp.setText("我是邮件分享文本，啦啦啦~http://uestcbmi.com/");   //分享文本
                sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
                sp.setUrl("http://sharesdk.cn");   //网友点进链接后，可以看到分享的详情*/

                setBimp(sp, post);//设置标题与图片
                sp.setText(setTitle(post)+Constant.SHARE_INVITION_URL + post.getId());
                //3、非常重要：获取平台对象
                Platform emailName = ShareSDK.getPlatform(Email.NAME);
                emailName.setPlatformActionListener(PlotFragment.this); // 设置分享事件回调
                // 执行分享
                emailName.share(sp);
            }
        });

        shareDialog.setDuanXinButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //2、设置分享内容
                Platform.ShareParams sp = new Platform.ShareParams();
                /*sp.setTitle("我是短信分享标题");  //分享标题
                sp.setText("我是短信分享文本，啦啦啦~http://uestcbmi.com/");   //分享文本
                sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
                      sp.setUrl("http://sharesdk.cn");   //网友点进链接后，可以看到分享的详情*/

                setBimp(sp, post);//设置标题与图片
                sp.setText(setTitle(post)+Constant.SHARE_INVITION_URL + post.getId());
                //3、非常重要：获取平台对象
                Platform shortMessage = ShareSDK.getPlatform(ShortMessage.NAME);
                shortMessage.setPlatformActionListener(PlotFragment.this); // 设置分享事件回调
                // 执行分享
                shortMessage.share(sp);
            }
        });
    }

    @Override
    public void onRefresh() {}

    @Override
    public void onLoadMore() {}

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        if (platform.getName().equals(QQ.NAME)) {// 判断成功的平台是不是QQ
            handler.sendEmptyMessage(1);
        } else if (platform.getName().equals(Wechat.NAME)) {//判断成功的平台是不是微信
            handler.sendEmptyMessage(2);
        }else if(platform.getName().equals(SinaWeibo.NAME)){//判断成功的平台是不是新浪微博
            handler.sendEmptyMessage(3);
        }else if(platform.getName().equals(WechatMoments.NAME)){//判断成功平台是不是微信朋友圈
            handler.sendEmptyMessage(4);
        }else if(platform.getName().equals(TencentWeibo.NAME)){//判断成功平台是不是腾讯微博
            handler.sendEmptyMessage(5);
        }else if(platform.getName().equals(Email.NAME)){//判断成功平台是不是邮件
            handler.sendEmptyMessage(6);
        }else if(platform.getName().equals(ShortMessage.NAME)){//判断成功平台是不是短信
            handler.sendEmptyMessage(7);
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

    Handler handler = new Handler() {

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
                    Toast.makeText(getActivity(), "分享失败啊", Toast.LENGTH_LONG).show();
                    break;
                case 9:
                    Toast.makeText(getActivity(), "已取消", Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
            }
        }

    };

}
