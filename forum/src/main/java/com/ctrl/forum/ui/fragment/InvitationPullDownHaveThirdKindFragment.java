package com.ctrl.forum.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarFragment;
import com.beanu.arad.utils.AnimUtil;
import com.ctrl.forum.R;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.dao.InvitationDao;
import com.ctrl.forum.entity.Banner;
import com.ctrl.forum.entity.Category;
import com.ctrl.forum.entity.Category2;
import com.ctrl.forum.entity.Merchant;
import com.ctrl.forum.entity.Post;
import com.ctrl.forum.entity.ThirdKind;
import com.ctrl.forum.loopview.HomeAutoSwitchPicHolder;
import com.ctrl.forum.ui.activity.Invitation.InvitationDetailActivity;
import com.ctrl.forum.ui.activity.Invitation.InvitationDetailFromPlatformActivity;
import com.ctrl.forum.ui.activity.Invitation.InvitationPinterestDetailActivity;
import com.ctrl.forum.ui.activity.Invitation.InvitationPullDownActivity;
import com.ctrl.forum.ui.activity.Invitation.InvitationSearchActivity;
import com.ctrl.forum.ui.activity.LoginActivity;
import com.ctrl.forum.ui.activity.store.StoreCommodityDetailActivity;
import com.ctrl.forum.ui.activity.store.StoreShopListVerticalStyleActivity;
import com.ctrl.forum.ui.adapter.InvitationListViewAdapter;
import com.ctrl.forum.ui.adapter.InvitationListViewBlockStyleAdapter;
import com.ctrl.forum.ui.adapter.InvitationListViewFriendStyleAdapter;
import com.ctrl.forum.ui.adapter.InvitationPullDownGridViewAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 帖子下拉列表有三级分类 fragment
 * Created by Administrator on 2015/11/30.
 */
public class InvitationPullDownHaveThirdKindFragment extends ToolBarFragment {
    @InjectView(R.id.lv_invitation_pull_down_have_third_kind)
    PullToRefreshListView lv_invitation_pull_down_have_third_kind;


    private List<Merchant> list;
    private List<ThirdKind> kindList;
    private GridView gridView1;

    private int width;
    DisplayMetrics dm;
    private int NUM = 4; // 每行显示个数
    private int hSpacing = 20;// 水平间距
    private List<Category2> mCategory2List;
    private InvitationListViewAdapter invitationListViewAdapter;
    private InvitationDao idao;
    private String channelId;
    private int PAGE_NUM = 1;
    private List<Post> listPost;
    private List<Category> listCategroy3;
    private FrameLayout framelayout;
    private HorizontalScrollView horizontalScrollView;
    private LinearLayout ll;
    private View headview;
    private int bol = 1;
    private boolean  isFirst=true;
    private String styleType;
    private InvitationListViewBlockStyleAdapter mInvitationListViewBlockStyleAdapter;
    private InvitationListViewFriendStyleAdapter mInvitationListViewFriendStyleAdapter;
    private InvitationPullDownGridViewAdapter gridViewAdapter;
    private String thirdKindId = null;
    private List<Post> listPost2;
    private int Position;
    private HomeAutoSwitchPicHolder mAutoSwitchPicHolder;
    private ArrayList<String> mData;
    private TextView tv_search;
    private int newPosition;
    private List<Banner> listBanner;
    private String keyword;

    private Map<Integer,Boolean> isAdd = new HashMap<>();
    private Map<Integer,Integer> text = new HashMap<>();
    private String showAll;
    private String firstId;


    public static InvitationPullDownHaveThirdKindFragment newInstance(String channelId,String styleType,String thirdKindId,String keyword,String showAll,String firstId) {
        InvitationPullDownHaveThirdKindFragment fragment = new InvitationPullDownHaveThirdKindFragment();
        fragment.channelId = channelId;
        fragment.styleType = styleType;
        fragment.keyword = keyword;
        fragment.thirdKindId = thirdKindId;
        fragment.showAll = showAll;
        fragment.firstId = firstId;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bol=1;
        idao = new InvitationDao(this);
        invitationListViewAdapter = new InvitationListViewAdapter(getActivity());
        mInvitationListViewBlockStyleAdapter = new InvitationListViewBlockStyleAdapter(getActivity());
        mInvitationListViewFriendStyleAdapter = new InvitationListViewFriendStyleAdapter(getActivity());
        width = getResources().getDisplayMetrics().widthPixels;
        gridViewAdapter = new InvitationPullDownGridViewAdapter(getActivity());
        mInvitationListViewFriendStyleAdapter.setOnItemClickListener(new InvitationListViewFriendStyleAdapter.OnItemClickListener() {
            @Override
            public void onItemZanClick(InvitationListViewFriendStyleAdapter.ViewHolder holder) {
                int position = holder.getPosition();
                int tex = listPost.get(position).getPraiseNum();

                if (isAdd.get(position)==null) {
                    if (listPost.get(position).getPraiseState().equals("0")) {
                        isAdd.put(position, true);
                    } else {
                        isAdd.put(position, false);
                    }
                }

                if (text.get(position)==null){
                    text.put(position,tex);
                }

                if (listPost.get(position).getPraiseState()!=null) {
                    if (isAdd.get(position)) {
                        idao.requesZambia("add", listPost.get(position).getId(),Arad.preferences.getString("memberId")
                                ,listPost.get(position).getTitle(),listPost.get(position).getContent());
                        holder.tv_friend_style_zan_num.setText((text.get(position) + 1) + "");
                        text.put(position,text.get(position) + 1);
                        holder.iv_friend_style_zan_num.setImageResource(R.mipmap.zan_blue_shixin);
                       // MessageUtils.showShortToast(getActivity(), "点赞成功");
                        isAdd.put(position,false);
                    } else {
                        idao.requesZambia("reduce", listPost.get(position).getId(), Arad.preferences.getString("memberId")
                                , listPost.get(position).getTitle(), listPost.get(position).getContent());
                       // MessageUtils.showShortToast(getActivity(), "取消点赞");
                        holder.tv_friend_style_zan_num.setText((text.get(position) - 1) + "");
                        text.put(position, text.get(position)-1);
                        holder.iv_friend_style_zan_num.setImageResource(R.mipmap.zan_blue);
                        isAdd.put(position,true);
                    }
                    }
                    }


        });
        }



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(!InvitationPullDownActivity.isFromSelcet){
            thirdKindId=null;
        }
        if(!InvitationPullDownActivity.isFromSearch){
            keyword=null;
           // thirdKindId=null;
        }

        if (isVisibleToUser&&bol==1 ) {
            showProgress(true);
            if(framelayout!=null)
            framelayout.setVisibility(View.GONE);
            if(listCategroy3!=null)listCategroy3.clear();
            if(keyword!=null){
                if(listPost!=null){
                    listPost.clear();
                }
                idao.requestPostRotaingBanner("B_POST_MIDDLE");
                idao.requesPostCategory(channelId, "2", "0");
                idao.requestPostListByCategory(Arad.preferences.getString("memberId"), thirdKindId, "0", keyword,"", PAGE_NUM, Constant.PAGE_SIZE);
            }else if(thirdKindId!=null) {
                idao.requesPostCategory(channelId, "2", "0");
                idao.requestPostListByCategory(Arad.preferences.getString("memberId"), thirdKindId, "0", "","", PAGE_NUM, Constant.PAGE_SIZE);
          
            }else if(showAll.equals("1")) {
               // idao.requesPostCategory(channelId, "2", "0");
                idao.requestPostRotaingBanner("B_POST_MIDDLE");
                idao.requestPostListByCategory(Arad.preferences.getString("memberId"), firstId, "0", "","", PAGE_NUM, Constant.PAGE_SIZE);
            }else if(thirdKindId==null){
                idao.requestPostRotaingBanner("B_POST_MIDDLE");
                idao.requesPostCategory(channelId, "2", "0");
                idao.requestPostListByCategory(Arad.preferences.getString("memberId"), channelId, "0", "","", PAGE_NUM, Constant.PAGE_SIZE);
           }else {
                //
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invitation_pull_down_have_third_kind, container, false);
        ButterKnife.inject(this, view);
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        headview = getActivity().getLayoutInflater().inflate(R.layout.fragment_invitation_header, lv_invitation_pull_down_have_third_kind, false);
        headview.setLayoutParams(layoutParams);
       final ListView lv = lv_invitation_pull_down_have_third_kind.getRefreshableView();
        initData();
        getScreenDen();
        lv.addHeaderView(headview);
        lv.setFocusable(false);

        lv_invitation_pull_down_have_third_kind.setMode(PullToRefreshBase.Mode.BOTH);
        lv_invitation_pull_down_have_third_kind.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                ll.setVisibility(View.VISIBLE);
                if (listPost != null)
                    listPost.clear();
                PAGE_NUM = 1;
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        if(thirdKindId!=null){
                            idao.requestPostListByCategory(Arad.preferences.getString("memberId"), thirdKindId, "0", "", "", PAGE_NUM, Constant.PAGE_SIZE);

                        }else if (listCategroy3 != null) {
                            idao.requestPostListByCategory(Arad.preferences.getString("memberId"), listCategroy3.get(Position).getId(), "0", "", "", PAGE_NUM, Constant.PAGE_SIZE);
                        } else if(showAll.equals("1")){
                            idao.requestPostListByCategory(Arad.preferences.getString("memberId"), firstId, "0", keyword, "", PAGE_NUM, Constant.PAGE_SIZE);
                        }else {
                            idao.requestPostListByCategory(Arad.preferences.getString("memberId"), channelId, "0", keyword, "", PAGE_NUM, Constant.PAGE_SIZE);
                        }
                    }

                }, 500);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                ll.setVisibility(View.VISIBLE);
                PAGE_NUM += 1;
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        if(thirdKindId!=null){
                            idao.requestPostListByCategory(Arad.preferences.getString("memberId"), thirdKindId, "0", "", "", PAGE_NUM, Constant.PAGE_SIZE);
                        }else if (listCategroy3 != null) {
                            idao.requestPostListByCategory(Arad.preferences.getString("memberId"), listCategroy3.get(Position).getId(), "0", "", "", PAGE_NUM, Constant.PAGE_SIZE);
                        } else if(showAll.equals("1")){
                            idao.requestPostListByCategory(Arad.preferences.getString("memberId"), firstId, "0", keyword, "", PAGE_NUM, Constant.PAGE_SIZE);
                        }else {
                            idao.requestPostListByCategory(Arad.preferences.getString("memberId"), channelId, "0", keyword, "", PAGE_NUM, Constant.PAGE_SIZE);
                        }
                    }
                }, 500);

            }
        });
        lv_invitation_pull_down_have_third_kind.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                newPosition = position - lv.getHeaderViewsCount();
                Intent intent = null;
                String type = listPost.get(newPosition).getSourceType();
                String contentType=listPost.get(newPosition).getContentType();
                if(styleType.equals("4")){
                    switch (contentType){
                        case "0":
                            intent = new Intent(getActivity(), InvitationDetailActivity.class);
                            intent.putExtra("id", listPost.get(newPosition).getId());
                            intent.putExtra("reportid", listPost.get(newPosition).getReporterId());
                            startActivity(intent);
                            AnimUtil.intentSlidIn(getActivity());
                            break;
                        case "1":
                            Uri uri = Uri.parse(listPost.get(newPosition).getArticleLink());
                            intent = new Intent(Intent.ACTION_VIEW, uri);
                            getActivity().startActivity(intent);
                            AnimUtil.intentSlidIn(getActivity());
                            break;
                        case "2":
                            intent=new Intent(getActivity(), StoreCommodityDetailActivity.class);
                            intent.putExtra("id",listPost.get(newPosition).getLinkItemId());
                            getActivity().startActivity(intent);
                            AnimUtil.intentSlidIn(getActivity());
                            break;
                        case "3":
                            intent=new Intent(getActivity(), StoreShopListVerticalStyleActivity.class);
                            intent.putExtra("id",listPost.get(newPosition).getLinkItemId());
                            getActivity().startActivity(intent);
                            AnimUtil.intentSlidIn(getActivity());
                            break;
                    }

                }else {
                    switch (contentType){
                        case "0":
                            switch (type) {
                                case "0"://平台
                                    intent = new Intent(getActivity(), InvitationDetailFromPlatformActivity.class);
                                    intent.putExtra("id", listPost.get(newPosition).getId());
                                    startActivity(intent);
                                    AnimUtil.intentSlidIn(getActivity());

                                    break;
                                case "1"://app
                                    intent = new Intent(getActivity(), InvitationPinterestDetailActivity.class);
                                    intent.putExtra("id", listPost.get(newPosition).getId());
                                    startActivity(intent);
                                    AnimUtil.intentSlidIn(getActivity());
                                    break;
                            }
                            break;
                        case "1":
                            Uri uri = Uri.parse(listPost.get(newPosition).getArticleLink());
                            intent = new Intent(Intent.ACTION_VIEW, uri);
                            getActivity().startActivity(intent);
                            AnimUtil.intentSlidIn(getActivity());
                            break;
                        case "2":
                            intent=new Intent(getActivity(), StoreCommodityDetailActivity.class);
                            intent.putExtra("id",listPost.get(newPosition).getLinkItemId());
                            getActivity().startActivity(intent);
                            AnimUtil.intentSlidIn(getActivity());
                            break;
                        case "3":
                            intent=new Intent(getActivity(), StoreShopListVerticalStyleActivity.class);
                            intent.putExtra("id",listPost.get(newPosition).getLinkItemId());
                            getActivity().startActivity(intent);
                            AnimUtil.intentSlidIn(getActivity());
                            break;
                    }

                }

            }
        });
        return view;
    }

    private void initData() {
        //  horizontalScrollView = (HorizontalScrollView) headview.findViewById(R.id.scrollView);
        ll = (LinearLayout) headview.findViewById(R.id.ll);
        gridView1 = (GridView) headview.findViewById(R.id.gridView_pull_down);
        framelayout = (FrameLayout) headview.findViewById(R.id.framelayout);
        tv_search = (TextView) headview.findViewById(R.id.tv_search);
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(listPost!=null){
                    listPost.clear();
                }
                bol = 1;
                gridViewAdapter.setPos(position);
                Position = position;
                thirdKindId=listCategroy3.get(position).getId();
                idao.requestPostListByCategory(Arad.preferences.getString("memberId"), listCategroy3.get(position).getId(), "0", "", "",PAGE_NUM, Constant.PAGE_SIZE);
            }
        });

        tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Arad.preferences.getString("memberId")==null||Arad.preferences.getString("memberId").equals("")){
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    AnimUtil.intentSlidOut(getActivity());
                    return;
                }
                InvitationPullDownActivity.isFromSearch=true;
                bol=1;
                Intent intent = new Intent(getActivity(), InvitationSearchActivity.class);
                startActivityForResult(intent, 1111);
                AnimUtil.intentSlidIn(getActivity());
            }
        });





    }

    private void setValue() {
        InvitationPullDownGridViewAdapter adapter = new InvitationPullDownGridViewAdapter(getActivity());
        adapter.setList(listCategroy3);
        int count = adapter.getCount();
        int columns = (count % 2 == 0) ? count / 2 : count / 2 + 1;
        gridView1.setAdapter(adapter);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(columns * dm.widthPixels / NUM,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        gridView1.setLayoutParams(params);
        gridView1.setColumnWidth(dm.widthPixels / NUM);
        // gridView.setHorizontalSpacing(hSpacing);
        gridView1.setStretchMode(GridView.NO_STRETCH);
        if (count <= 3) {
            gridView1.setNumColumns(count);
        } else {
            gridView1.setNumColumns(columns);
        }
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listPost != null) {
                    listPost.clear();
                }
                bol = 1;
                idao.requestPostListByCategory(Arad.preferences.getString("memberId"), listCategroy3.get(position).getId(), "0", "", "", PAGE_NUM, Constant.PAGE_SIZE);
            }
        });


    }




    @Override
    public void onResume() {
        super.onResume();
    }

    private void getScreenDen() {
        dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        lv_invitation_pull_down_have_third_kind.onRefreshComplete();
        showProgress(false);

        if(requestCode==19){
            listBanner=idao.getListBanner();
            framelayout.setVisibility(View.VISIBLE);
            //设置轮播图
            if(listBanner!=null&&framelayout.getVisibility()==View.VISIBLE) {
                setLoopView();
            }
        }
        if (requestCode == 1) {
            bol = 0;
            if(listCategroy3==null) {
             //   framelayout.setVisibility(View.VISIBLE);
                gridView1.setVisibility(View.GONE);
            }else {
                framelayout.setVisibility(View.GONE);
                gridView1.setVisibility(View.VISIBLE);
            }
            listPost = idao.getListPost();
            switch (styleType) {
                case "1":
                    invitationListViewAdapter.setList(listPost);
                    break;
                case "2":
                    mInvitationListViewBlockStyleAdapter.setList(listPost);
                    break;
                case "4":
                    mInvitationListViewFriendStyleAdapter.setList(listPost);
                    break;
            }
            InvitationPullDownActivity.isFromSelcet=false;
            InvitationPullDownActivity.isFromSearch=false;

        }
        if (requestCode == 2) {
            bol = 0;
            framelayout.setVisibility(View.GONE);
            gridView1.setVisibility(View.VISIBLE);

            listCategroy3 = idao.getListCategory();
            gridViewAdapter.setList(listCategroy3);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1111&&resultCode==2222){
            keyword = data.getStringExtra("keyword");
            InvitationPullDownActivity activity = (InvitationPullDownActivity) getActivity();
            activity.setKeyword(keyword);
            activity.getAdapter().reLoad();
        }
    }

    /*
    * 轮播图
    * */
    private void setLoopView() {
        // 1.创建轮播的holder
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


    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        lv_invitation_pull_down_have_third_kind.onRefreshComplete();
        showProgress(false);
        if(errorNo.equals("006")){
            framelayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridView1.setAdapter(gridViewAdapter);
        if(listBanner!=null&&listBanner.size()==0){
            framelayout.setVisibility(View.GONE);
        }
        if(styleType!=null) {
            switch (styleType) {
                case "1":
                    lv_invitation_pull_down_have_third_kind.setAdapter(invitationListViewAdapter);
                    break;
                case "2":
                    lv_invitation_pull_down_have_third_kind.setAdapter(mInvitationListViewBlockStyleAdapter);
                    break;
                case "4":
                    lv_invitation_pull_down_have_third_kind.setAdapter(mInvitationListViewFriendStyleAdapter);
                    break;
            }
        }


    }



}
