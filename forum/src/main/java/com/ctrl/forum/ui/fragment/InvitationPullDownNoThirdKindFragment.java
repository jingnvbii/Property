package com.ctrl.forum.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarFragment;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.dao.InvitationDao;
import com.ctrl.forum.entity.Post;
import com.ctrl.forum.ui.adapter.InvitationListViewAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 帖子下拉列表无三级分类 fragment
 * Created by Administrator on 2015/11/30.
 */
public class InvitationPullDownNoThirdKindFragment extends ToolBarFragment {
    @InjectView(R.id.listview_invitation_pull_down_no_third_kind)
    PullToRefreshListView listview_invitation_pull_down_no_third_kind;

    private FrameLayout framelayout;
    private InvitationDao idao;
    private String channelId;
   // private List<Post> mPostList;
    private int PAGE_NUM=1;
    private List<Post> listPost;
    private InvitationListViewAdapter invitationListViewAdapter;


    public static InvitationPullDownNoThirdKindFragment newInstance(String channelId) {
        InvitationPullDownNoThirdKindFragment fragment = new InvitationPullDownNoThirdKindFragment();
        fragment.channelId=channelId;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invitation_pull_down_no_third_kind, container, false);
        ButterKnife.inject(this, view);
        invitationListViewAdapter=new InvitationListViewAdapter(getActivity());
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        View headview =getActivity().getLayoutInflater().inflate(R.layout.fragment_invitation_header_second, listview_invitation_pull_down_no_third_kind, false);
        headview.setLayoutParams(layoutParams);
        framelayout=(FrameLayout)headview.findViewById(R.id.framelayout);
        ListView lv = listview_invitation_pull_down_no_third_kind.getRefreshableView();
        lv.addHeaderView(headview);
        //调用轮播图
     //   setLoopView();
        initData();
        return view;
    }

    private void initData() {
        idao = new InvitationDao(this);
        idao.requestPostListByCategory(Arad.preferences.getString("memberId"), channelId, "0","", PAGE_NUM, Constant.PAGE_SIZE);
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
        listview_invitation_pull_down_no_third_kind.setAdapter(invitationListViewAdapter);
    }
}
