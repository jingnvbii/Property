package com.ctrl.forum.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarFragment;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.customview.GridViewForScrollView;
import com.ctrl.forum.dao.InvitationDao;
import com.ctrl.forum.entity.Category2;
import com.ctrl.forum.entity.Merchant;
import com.ctrl.forum.entity.Post;
import com.ctrl.forum.entity.ThirdKind;
import com.ctrl.forum.ui.adapter.InvitationListViewAdapter;
import com.ctrl.forum.ui.adapter.InvitationPullDownGridViewAdapter;
import com.ctrl.forum.ui.adapter.testAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

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
    private GridViewForScrollView gridView1;

    private int width;
    DisplayMetrics dm;
    private int NUM = 4; // 每行显示个数
    private int hSpacing = 20;// 水平间距
    private testAdapter mAdapter;
    private List<Category2> mCategory2List;
    private InvitationListViewAdapter invitationListViewAdapter;
    private InvitationDao idao;
    private String channelId;
    private int PAGE_NUM=1;
    private List<Post> listPost;


    public static InvitationPullDownHaveThirdKindFragment newInstance(List<Category2>list,String channelId) {
        InvitationPullDownHaveThirdKindFragment fragment = new InvitationPullDownHaveThirdKindFragment();
        fragment.mCategory2List=list;
        fragment.channelId=channelId;
        return fragment;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invitation_pull_down_have_third_kind, container, false);
        ButterKnife.inject(this, view);
        invitationListViewAdapter=new InvitationListViewAdapter(getActivity());
        width = getResources().getDisplayMetrics().widthPixels;
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        View headview =getActivity().getLayoutInflater().inflate(R.layout.fragment_invitation_header, lv_invitation_pull_down_have_third_kind, false);
        headview.setLayoutParams(layoutParams);
        ListView lv = lv_invitation_pull_down_have_third_kind.getRefreshableView();
        gridView1=(GridViewForScrollView)headview.findViewById(R.id.gridView1);
        initData();
        getScreenDen();
        setValue();
        lv.addHeaderView(headview);
        return view;
    }

    private void initData() {
        idao = new InvitationDao(this);
        idao.requestPostListByCategory(Arad.preferences.getString("memberId"), channelId, "0", PAGE_NUM, Constant.PAGE_SIZE);
        list=new ArrayList<>();

        for(int i=0;i<20;i++){
            Merchant invitation=new Merchant();
            invitation.setName("汪峰"+i+"便利店");
            list.add(invitation);
        }


        kindList=new ArrayList<>();
        for(int i=0;i<8;i++){
            ThirdKind kind=new ThirdKind();
            kind.setKindName("频道:"+i);
            kindList.add(kind);
        }
    }

    private void setValue() {
        InvitationPullDownGridViewAdapter adapter = new InvitationPullDownGridViewAdapter(getActivity());
        adapter.setList(mCategory2List);
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

            }
        });

    }

    private void getScreenDen() {
        dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if (requestCode == 1) {
            MessageUtils.showShortToast(getActivity(), "获取帖子列表成功");
            listPost = idao.getListPost();
            invitationListViewAdapter.setList(listPost);

            Log.i("tag", "listPost---" + listPost.size());
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lv_invitation_pull_down_have_third_kind.setAdapter(invitationListViewAdapter);
    }
}
