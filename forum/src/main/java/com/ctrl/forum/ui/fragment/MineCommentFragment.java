package com.ctrl.forum.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarFragment;
import com.ctrl.forum.R;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.dao.ReplyCommentDao;
import com.ctrl.forum.entity.ReplyForMe;
import com.ctrl.forum.ui.activity.Invitation.InvitationDetailActivity;
import com.ctrl.forum.ui.adapter.MineMesCommentListAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

/**
 * 消息-评论
 */
public class MineCommentFragment extends ToolBarFragment {

    private List<ReplyForMe> replyForMes;
    private MineMesCommentListAdapter mineMesCommentListAdapter;
    private PullToRefreshListView lv_content;
    private ReplyCommentDao rcdao;
    private int PAGE_NUM=1;

    public static MineCommentFragment newInstance() {
        MineCommentFragment fragment = new MineCommentFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mine_list, container, false);

        lv_content = (PullToRefreshListView) view.findViewById(R.id.lv_content);
        initData();

        lv_content.setMode(PullToRefreshBase.Mode.BOTH);
        lv_content.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (replyForMes != null) {
                    replyForMes.clear();
                    PAGE_NUM = 1;
                }
                rcdao.obtainReply(Arad.preferences.getString("memberId"), PAGE_NUM, Constant.PAGE_SIZE);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM += 1;
                rcdao.obtainReply(Arad.preferences.getString("memberId"), PAGE_NUM, Constant.PAGE_SIZE);
            }
        });

        lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), InvitationDetailActivity.class);
                intent.putExtra("id", replyForMes.get(position-1).getPostId());
                intent.putExtra("reportid",replyForMes.get(position-1).getReporterId());
                startActivity(intent);
            }
        });

        return view;
    }

    private void initData() {
      rcdao = new ReplyCommentDao(this);
        mineMesCommentListAdapter = new MineMesCommentListAdapter(getActivity());
        lv_content.setAdapter(mineMesCommentListAdapter);
        rcdao.obtainReply(Arad.preferences.getString("memberId"),PAGE_NUM,Constant.PAGE_SIZE);
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        lv_content.onRefreshComplete();
        if(requestCode == 0){
            replyForMes = rcdao.getReplyForMes();
            if (replyForMes.size()>0){
                mineMesCommentListAdapter.setReplyForMes(replyForMes);
            }
        }
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        lv_content.onRefreshComplete();
    }


}
