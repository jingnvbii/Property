package com.ctrl.forum.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.beanu.arad.base.ToolBarFragment;
import com.ctrl.forum.R;
import com.ctrl.forum.entity.Message;
import com.ctrl.forum.ui.adapter.MineMessageListAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息-全部
 */
public class MineMessageFragment extends ToolBarFragment {
    private List<Message> messages;
    private PullToRefreshListView lv_content;
    private MineMessageListAdapter mineMessageListAdapter;
    private int PAGE_NUM=1;

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
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (messages != null) {
                    PAGE_NUM += 1;
                } else {
                    lv_content.onRefreshComplete();
                }
            }
        });
        return view;

    }

    //初始化数据
    private void initData() {
        messages = new ArrayList<>();
        for (int i=0;i<3;i++){
            Message message = new Message();
            message.setName(getResources().getString(R.string.system_notification));
            message.setData(getResources().getString(R.string.content_data));
            message.setContent(getResources().getString(R.string.message_contetn));
            messages.add(message);
        }
        Message message = new Message();
        message.setName(getResources().getString(R.string.system_notification));
        message.setData(getResources().getString(R.string.content_data));
        message.setContent(getResources().getString(R.string.message_contetn) + getResources().getString(R.string.message_contetn)+getResources().getString(R.string.message_contetn));
        messages.add(message);

    }

}
