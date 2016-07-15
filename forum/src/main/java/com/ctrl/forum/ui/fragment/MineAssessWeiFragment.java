package com.ctrl.forum.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarFragment;
import com.ctrl.forum.R;
import com.ctrl.forum.dao.ReplyCommentDao;
import com.ctrl.forum.entity.Assess;
import com.ctrl.forum.ui.adapter.MineAssessWeiAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

/**
 * 待评价订单
 */
public class MineAssessWeiFragment extends ToolBarFragment {
    private PullToRefreshListView lv_content;
    private MineAssessWeiAdapter assessWeiAdapter;
    private List<Assess> assessWeis;
    private ReplyCommentDao rcdao;

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
        View view =inflater.inflate(R.layout.fragment_mine_list, container, false);
        lv_content = (PullToRefreshListView) view.findViewById(R.id.lv_content);
        initData();
        assessWeiAdapter = new MineAssessWeiAdapter(getActivity());
        lv_content.setAdapter(assessWeiAdapter);

        lv_content.setMode(PullToRefreshBase.Mode.BOTH);
        lv_content.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (assessWeis != null) {
                    assessWeis.clear();
                    rcdao.queryOrderEvaluation(Arad.preferences.getString("memberId"), "0");
                } else {
                    lv_content.onRefreshComplete();
                }
            }
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (assessWeis != null) {
                    assessWeis.clear();
                    rcdao.queryOrderEvaluation(Arad.preferences.getString("memberId"), "0");
                } else {
                    lv_content.onRefreshComplete();
                }
            }
        });
        return view;
    }

    private void initData() {
       /* assessWeis = new ArrayList<>();
        for(int i=0;i<7;i++){
            Assess assessWei = new Assess();
            assessWeis.add(assessWei);
        }
*/
        rcdao = new ReplyCommentDao(this);
        rcdao.queryOrderEvaluation(Arad.preferences.getString("memberId"), "0");
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        lv_content.onRefreshComplete();
        if (requestCode==1){
            assessWeis = rcdao.getAssesses();
            if (assessWeis!=null){
                assessWeiAdapter.setMessages(assessWeis);
            }
        }
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        lv_content.onRefreshComplete();
    }
}
