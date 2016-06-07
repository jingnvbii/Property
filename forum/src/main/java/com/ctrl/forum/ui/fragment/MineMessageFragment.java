package com.ctrl.forum.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarFragment;
import com.ctrl.forum.R;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.dao.ReplyCommentDao;
import com.ctrl.forum.entity.Message;
import com.ctrl.forum.ui.adapter.MineMessageListAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

/**
 * 消息-全部
 */
public class MineMessageFragment extends ToolBarFragment {
    private List<Message> messages;
    private PullToRefreshListView lv_content;
    private MineMessageListAdapter mineMessageListAdapter;
    private int PAGE_NUM=1;
    private ReplyCommentDao rdao;

    public static MineMessageFragment newInstance() {
        MineMessageFragment fragment = new MineMessageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine_list, container, false);
        initData();
        lv_content = (PullToRefreshListView) view.findViewById(R.id.lv_content);
        mineMessageListAdapter = new MineMessageListAdapter(getActivity());
        lv_content.setAdapter(mineMessageListAdapter);

        lv_content.setMode(PullToRefreshBase.Mode.BOTH);
        lv_content.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (messages != null) {
                    messages.clear();
                    PAGE_NUM = 1;
                    mineMessageListAdapter = new MineMessageListAdapter(getActivity());
                    lv_content.setAdapter(mineMessageListAdapter);
                }
                rdao.queryMessageList(Arad.preferences.getString("memberId"),PAGE_NUM, Constant.PAGE_SIZE);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (messages != null) {
                    PAGE_NUM += 1;
                    rdao.queryMessageList(Arad.preferences.getString("memberId"),PAGE_NUM, Constant.PAGE_SIZE);
                } else {
                    lv_content.onRefreshComplete();
                }
            }
        });
        return view;

    }

    private void initData() {
        rdao = new ReplyCommentDao(this);
        rdao.queryMessageList(Arad.preferences.getString("memberId"),PAGE_NUM, Constant.PAGE_SIZE);
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if (requestCode==4){
            messages = rdao.getMessages();
            if (messages!=null){
                mineMessageListAdapter.setMessages(messages);
            }

        }
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        lv_content.onRefreshComplete();
    }
}
