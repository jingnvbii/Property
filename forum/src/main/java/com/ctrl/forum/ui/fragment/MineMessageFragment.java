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

import java.util.ArrayList;
import java.util.List;

/**
 * 消息-全部
 */
public class MineMessageFragment extends ToolBarFragment {
    private List<Message> messages;
    private ListView lv_content;
    private MineMessageListAdapter mineMessageListAdapter;

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
        View view = inflater.inflate(R.layout.fragment_mine_notificationi, container, false);
        initData();
        lv_content = (ListView) view.findViewById(R.id.lv_content);
        mineMessageListAdapter = new MineMessageListAdapter(getActivity(),messages);
        lv_content.setAdapter(mineMessageListAdapter);
        return view;

    }

    //��ʼ�����
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
