package com.ctrl.forum.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.beanu.arad.base.ToolBarFragment;
import com.ctrl.forum.R;
import com.ctrl.forum.entity.MesComment;
import com.ctrl.forum.ui.adapter.MineMesCommentListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Use the {@link MineCommentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MineCommentFragment extends ToolBarFragment {

    private List<MesComment> mesComments;
    private MineMesCommentListAdapter mineMesCommentListAdapter;
    private ListView lv_content;

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
        View view = inflater.inflate(R.layout.fragment_mine_notificationi, container, false);

        initView();
        initData(); //�����

        lv_content = (ListView) view.findViewById(R.id.lv_content);
        mineMesCommentListAdapter = new MineMesCommentListAdapter(getActivity(),mesComments);
        lv_content.setAdapter(mineMesCommentListAdapter);
        return view;
    }
    //��ʼ�����
    private void initData() {
       mesComments = new ArrayList<>();
        for (int i=0;i<5;i++){
            MesComment mesComment = new MesComment();
            mesComment.setVip_name("name"+i);
            mesComments.add(mesComment);
        }
    }

    private void initView() {

    }


}
