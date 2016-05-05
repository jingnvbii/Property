package com.ctrl.forum.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ctrl.forum.R;
import com.ctrl.forum.entity.AssessWei;
import com.ctrl.forum.ui.adapter.MineAssessWeiAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * �����۶���
 */
public class MineAssessWeiFragment extends Fragment {
    private ListView lv_content;
    private MineAssessWeiAdapter assessWeiAdapter;
    private List<AssessWei> assessWeis;

    public static MineAssessWeiFragment newInstance() {
        MineAssessWeiFragment fragment = new MineAssessWeiFragment();
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
        View view =inflater.inflate(R.layout.fragment_mine_notificationi, container, false);
        lv_content = (ListView) view.findViewById(R.id.lv_content);
        initData();
        assessWeiAdapter = new MineAssessWeiAdapter(getActivity());
        assessWeiAdapter.setMessages(assessWeis);
        lv_content.setAdapter(assessWeiAdapter);
        return view;
    }

    private void initData() {
        assessWeis = new ArrayList<>();
        for(int i=0;i<7;i++){
            AssessWei assessWei = new AssessWei();
            assessWei.setShop_name(i+"");
            assessWeis.add(assessWei);
        }
    }


}
