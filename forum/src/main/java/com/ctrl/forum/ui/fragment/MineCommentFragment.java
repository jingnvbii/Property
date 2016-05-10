package com.ctrl.forum.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarFragment;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.dao.ReplyCommentDao;
import com.ctrl.forum.entity.ReplyForMe;
import com.ctrl.forum.ui.adapter.MineMesCommentListAdapter;

import java.util.List;

/**
 * 消息-评论
 */
public class MineCommentFragment extends ToolBarFragment {

    private List<ReplyForMe> replyForMes;
    private MineMesCommentListAdapter mineMesCommentListAdapter;
    private ListView lv_content;
    private ReplyCommentDao rcdao;

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
        initData();

        lv_content = (ListView) view.findViewById(R.id.lv_content);
        mineMesCommentListAdapter = new MineMesCommentListAdapter(getActivity(),replyForMes);
        lv_content.setAdapter(mineMesCommentListAdapter);
        return view;
    }

    private void initData() {
      rcdao = new ReplyCommentDao(this);
        MessageUtils.showShortToast(getActivity(),Arad.preferences.getString("memberId"));
        rcdao.obtainReply(Arad.preferences.getString("memberId"), Constant.PAGE_NUM,Constant.PAGE_SIZE);
        replyForMes = rcdao.getReplyForMes();
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if(requestCode == 0){
            MessageUtils.showShortToast(getActivity(),"查看评论成功");
        }
    }
}
