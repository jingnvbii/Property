package com.ctrl.forum.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ctrl.forum.R;
import com.ctrl.forum.entity.AssessYi;
import com.ctrl.forum.ui.adapter.MineAssessYiAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 已评价订单
 */
public class MineAssessYiFragment extends Fragment {
    private ListView lv_content;
    private MineAssessYiAdapter assessYiAdapter;
    private List<AssessYi> assessYis;

    public static MineAssessYiFragment newInstance() {
        MineAssessYiFragment fragment = new MineAssessYiFragment();
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
        View view = inflater.inflate(R.layout.fragment_mine_notificationi, container, false);
        lv_content = (ListView) view.findViewById(R.id.lv_content);
        initData();
        assessYiAdapter = new MineAssessYiAdapter(getActivity());
        assessYiAdapter.setMessages(assessYis);
        lv_content.setAdapter(assessYiAdapter);
        return view;
    }

    private void initData() {
        assessYis = new ArrayList<>();
        for(int i=0;i<7;i++){
            AssessYi assess = new AssessYi();
            assess.setShop_name(i+"");
            assessYis.add(assess);
        }
    }

}
