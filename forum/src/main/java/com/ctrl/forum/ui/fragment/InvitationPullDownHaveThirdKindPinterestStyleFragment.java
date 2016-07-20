package com.ctrl.forum.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarFragment;
import com.beanu.arad.utils.AnimUtil;
import com.ctrl.forum.R;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.customview.PLA_AdapterView;
import com.ctrl.forum.customview.XListView;
import com.ctrl.forum.dao.InvitationDao;
import com.ctrl.forum.entity.Banner;
import com.ctrl.forum.entity.Category;
import com.ctrl.forum.entity.Post;
import com.ctrl.forum.loopview.HomeAutoSwitchPicHolder;
import com.ctrl.forum.ui.activity.Invitation.InvitationPinerestGalleyActivity;
import com.ctrl.forum.ui.activity.Invitation.InvitationPullDownActivity;
import com.ctrl.forum.ui.activity.Invitation.InvitationSearchActivity;
import com.ctrl.forum.ui.activity.LoginActivity;
import com.ctrl.forum.ui.activity.store.StoreCommodityDetailActivity;
import com.ctrl.forum.ui.activity.store.StoreShopListVerticalStyleActivity;
import com.ctrl.forum.ui.adapter.InvitationListViewPinterestStyleAdapter;
import com.ctrl.forum.ui.adapter.InvitationPullDownGridViewAdapter;
import com.ctrl.forum.ui.adapter.JasonViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 帖子下拉列表有三级分类 fragment
 * Created by Administrator on 2015/11/30.
 */
public class InvitationPullDownHaveThirdKindPinterestStyleFragment extends ToolBarFragment implements XListView.IXListViewListener {
    private static Context mContext;
    @InjectView(R.id.xlv_pinerest_style)
    XListView xlv_pinerest_style;
    private GridView gridView1;


    private int width;
    DisplayMetrics dm;
    private int NUM = 4; // 每行显示个数
    private int hSpacing = 20;// 水平间距
    private String id;
    private View headview;
    private HorizontalScrollView horizontalScrollView;
    private LinearLayout ll;
    private FrameLayout framelayout;
    private InvitationDao idao;
    private int bol = 1;
    private int PAGE_NUM = 1;
    private List<Post> listPost;
    private List<Category> listCategroy3;
    private InvitationListViewPinterestStyleAdapter mInvitationListViewPinterestStyleAdapter;
    private InvitationPullDownGridViewAdapter adapter;
    private String thirdKindId = null;
    private List<Post> listPost2;
    private int Position;
    private HomeAutoSwitchPicHolder mAutoSwitchPicHolder;
    private ArrayList<String> mData;
    private TextView tv_search;
    private boolean isFirst=true;
    private List<Banner> listBanner;
    private String keyword;
    private String showAll;
    private String firstId;


    public static InvitationPullDownHaveThirdKindPinterestStyleFragment newInstance(Context context,String id,String thirdKindId,String keyword,String showAll,String firstId) {
        InvitationPullDownHaveThirdKindPinterestStyleFragment fragment = new InvitationPullDownHaveThirdKindPinterestStyleFragment();
        fragment.id = id;
        mContext=context;
        fragment.thirdKindId = thirdKindId;
        fragment.keyword = keyword;
        fragment.showAll = showAll;
        fragment.firstId = firstId;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInvitationListViewPinterestStyleAdapter = new InvitationListViewPinterestStyleAdapter(getActivity());
        width = getResources().getDisplayMetrics().widthPixels;
        idao = new InvitationDao(this);
        adapter = new InvitationPullDownGridViewAdapter(getActivity());
    }



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(!InvitationPullDownActivity.isFromSelcet){
            thirdKindId=null;
        }
        if(!InvitationPullDownActivity.isFromSearch){
            keyword=null;
            //thirdKindId=null;
        }
        if (isVisibleToUser&&bol==1 ) {
            idao.requestPostRotaingBanner("B_POST_MIDDLE");
            if(listCategroy3!=null)listCategroy3.clear();
            if(keyword!=null){
                if(listPost!=null){
                    listPost.clear();
                }
                idao.requestPostRotaingBanner("B_POST_MIDDLE");
                idao.requesPostCategory(id, "2", "0");
                idao.requestPostListByCategory(Arad.preferences.getString("memberId"), thirdKindId, "0", keyword,"", PAGE_NUM, Constant.PAGE_SIZE);
            }else if(thirdKindId!=null) {
                idao.requesPostCategory(id, "2", "0");
                idao.requestPostListByCategory(Arad.preferences.getString("memberId"), thirdKindId, "0", "","", PAGE_NUM, Constant.PAGE_SIZE);
            }else if(showAll.equals("1")) {
                idao.requestPostRotaingBanner("B_POST_MIDDLE");
                idao.requestPostListByCategory(Arad.preferences.getString("memberId"),firstId, "0", "","", PAGE_NUM, Constant.PAGE_SIZE);
            }else if(thirdKindId==null){
                idao.requesPostCategory(id, "2", "0");
                idao.requestPostListByCategory(Arad.preferences.getString("memberId"), id, "0", "","", PAGE_NUM, Constant.PAGE_SIZE);
            }else {
                //
            }
        }

    }

    public void request(String id) {
        bol = 1;
        thirdKindId = id;
        idao.requestPostListByCategory(Arad.preferences.getString("memberId"), thirdKindId, "0", "","", PAGE_NUM, Constant.PAGE_SIZE);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invitation_pull_down_have_third_kind_pinterest_style, container, false);
        ButterKnife.inject(this, view);
        headview = getActivity().getLayoutInflater().inflate(R.layout.fragment_invitation_header, null);
        xlv_pinerest_style.addHeaderView(headview);
        xlv_pinerest_style.setFocusable(false);
        xlv_pinerest_style.setOnItemClickListener(new PLA_AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(PLA_AdapterView<?> parent, View view, int position, long id) {
                int nowPos = position - xlv_pinerest_style.getHeaderViewsCount();
                Intent intent=null;
                String contentType=listPost.get(nowPos).getContentType();
                switch (contentType){
                    case "0":
                        intent = new Intent(getActivity(), InvitationPinerestGalleyActivity.class);
                        intent.putExtra("id", listPost.get(nowPos).getId());
                        startActivityForResult(intent, 202);
                        AnimUtil.intentSlidIn(getActivity());
                        break;
                    case "1":
                        Uri uri = Uri.parse(listPost.get(nowPos).getArticleLink());
                        intent = new Intent(Intent.ACTION_VIEW, uri);
                        getActivity().startActivity(intent);
                        AnimUtil.intentSlidIn(getActivity());
                        break;
                    case "2":
                        intent=new Intent(getActivity(), StoreCommodityDetailActivity.class);
                        intent.putExtra("id",listPost.get(nowPos).getLinkItemId());
                        getActivity().startActivity(intent);
                        AnimUtil.intentSlidIn(getActivity());
                        break;
                    case "3":
                        intent=new Intent(getActivity(), StoreShopListVerticalStyleActivity.class);
                        intent.putExtra("id",listPost.get(nowPos).getLinkItemId());
                        getActivity().startActivity(intent);
                        AnimUtil.intentSlidIn(getActivity());
                        break;
                }


            }
        });
        initData();
        getScreenDen();
        xlv_pinerest_style.setPullLoadEnable(true);
        xlv_pinerest_style.setXListViewListener(this);
        // setValue();


        return view;
    }



    private void initData() {
        gridView1 = (GridView) headview.findViewById(R.id.gridView_pull_down);
        //   horizontalScrollView = (HorizontalScrollView) headview.findViewById(R.id.scrollView);
        ll = (LinearLayout) headview.findViewById(R.id.ll);
        gridView1 = (GridView) headview.findViewById(R.id.gridView_pull_down);
       framelayout = (FrameLayout) headview.findViewById(R.id.framelayout);
        tv_search = (TextView) headview.findViewById(R.id.tv_search);
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listPost != null) {
                    listPost.clear();
                }
                bol = 1;
                Position = position;
                thirdKindId = listCategroy3.get(position).getId();
                idao.requestPostListByCategory(Arad.preferences.getString("memberId"), thirdKindId, "0", "", "", PAGE_NUM, Constant.PAGE_SIZE);
            }
        });

        tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Arad.preferences.getString("memberId") == null || Arad.preferences.getString("memberId").equals("")) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    AnimUtil.intentSlidOut(getActivity());
                    return;
                }
                InvitationPullDownActivity.isFromSearch = true;
                bol = 1;
                Intent intent = new Intent(getActivity(), InvitationSearchActivity.class);
                startActivityForResult(intent, 1112);
                AnimUtil.intentSlidIn(getActivity());
            }
        });

    }



    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        xlv_pinerest_style.stopLoadMore();
        xlv_pinerest_style.stopRefresh();
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
            listPost = idao.getListPost();
            if(listCategroy3==null){
             //   framelayout.setVisibility(View.VISIBLE);
                gridView1.setVisibility(View.GONE);
            }else {
                framelayout.setVisibility(View.GONE);
                gridView1.setVisibility(View.VISIBLE);
            }
            mInvitationListViewPinterestStyleAdapter.setList(listPost);
            InvitationPullDownActivity.isFromSelcet=false;
            InvitationPullDownActivity.isFromSearch=false;
        }
        if (requestCode == 2) {
            bol = 0;
            framelayout.setVisibility(View.GONE);
            gridView1.setVisibility(View.VISIBLE);
            listCategroy3 = idao.getListCategory();
            adapter.setList(listCategroy3);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1112&&resultCode==2222){
            keyword = data.getStringExtra("keyword");
            InvitationPullDownActivity activity = (InvitationPullDownActivity) getActivity();
            activity.setKeyword(keyword);
            activity.getAdapter().reLoad();
        }
        if(requestCode==202&&resultCode==203){
            InvitationPullDownActivity activity=(InvitationPullDownActivity)mContext;
            JasonViewPagerAdapter adapter1 = activity.getAdapter();
            adapter1.reLoad();
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
        xlv_pinerest_style.stopLoadMore();
        xlv_pinerest_style.stopRefresh();
        if(errorNo.equals("006")){
            framelayout.setVisibility(View.GONE);
        }
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
                bol = 1;
                Position = position;
                idao.requestPostListByCategory(Arad.preferences.getString("memberId"), listCategroy3.get(position).getId(), "0", "", "",PAGE_NUM, Constant.PAGE_SIZE);
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
        if(listBanner!=null&&listBanner.size()==0){
            framelayout.setVisibility(View.GONE);
        }
        xlv_pinerest_style.setAdapter(mInvitationListViewPinterestStyleAdapter);
        gridView1.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        ll.setVisibility(View.VISIBLE);
        PAGE_NUM = 1;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(listPost!=null){
                    listPost.clear();
                }

                if(thirdKindId!=null){
                    idao.requestPostListByCategory(Arad.preferences.getString("memberId"), thirdKindId, "0", "", "", PAGE_NUM, Constant.PAGE_SIZE);
                }
                if (listCategroy3 != null) {
                    idao.requestPostListByCategory(Arad.preferences.getString("memberId"),listCategroy3.get(Position).getId(), "0", "", "",PAGE_NUM, Constant.PAGE_SIZE);
                }else if(showAll.equals("1")){
                    idao.requestPostListByCategory(Arad.preferences.getString("memberId"),firstId, "0", keyword, "",PAGE_NUM, Constant.PAGE_SIZE);
                }else {
                    idao.requestPostListByCategory(Arad.preferences.getString("memberId"), id, "0", keyword, "", PAGE_NUM, Constant.PAGE_SIZE);
                }
            }
        }, 500);

    }

    @Override
    public void onLoadMore() {
        ll.setVisibility(View.VISIBLE);
        PAGE_NUM += 1;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(thirdKindId!=null){
                    idao.requestPostListByCategory(Arad.preferences.getString("memberId"),thirdKindId, "0", "", "",PAGE_NUM, Constant.PAGE_SIZE);
                }
                if (listCategroy3 != null) {
                    idao.requestPostListByCategory(Arad.preferences.getString("memberId"),listCategroy3.get(Position).getId(), "0", "", "",PAGE_NUM, Constant.PAGE_SIZE);
                }else if(showAll.equals("1")){
                    idao.requestPostListByCategory(Arad.preferences.getString("memberId"),firstId, "0", keyword, "",PAGE_NUM, Constant.PAGE_SIZE);
                }else {
                    idao.requestPostListByCategory(Arad.preferences.getString("memberId"), id, "0", keyword, "", PAGE_NUM, Constant.PAGE_SIZE);
                }
            }
        }, 500);
    }
}
