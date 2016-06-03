package com.ctrl.forum.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.beanu.arad.base.ToolBarFragment;
import com.ctrl.forum.R;
import com.ctrl.forum.customview.XListView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 帖子下拉列表瀑布流样式 fragment
 * Created by Administrator on 2015/11/30.
 */
public class InvitationPullDownNoThirdKindPinterestStyleFragment extends ToolBarFragment {
    @InjectView(R.id.invitation_list_no_third_kind)
    XListView invitation_list_no_third_kind;
    private FrameLayout framelayout;


    public static InvitationPullDownNoThirdKindPinterestStyleFragment newInstance() {
        InvitationPullDownNoThirdKindPinterestStyleFragment fragment = new InvitationPullDownNoThirdKindPinterestStyleFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invitation_pull_down_no_third_kind_pinterest_style, container, false);
        ButterKnife.inject(this, view);
      //  AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        View headview =getActivity().getLayoutInflater().inflate(R.layout.fragment_invitation_header_second,null);
       // headview.setLayoutParams(layoutParams);
        framelayout=(FrameLayout)headview.findViewById(R.id.framelayout);
        invitation_list_no_third_kind.addHeaderView(headview);
        //调用轮播图
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
