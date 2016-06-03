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
import com.ctrl.forum.dao.ReplyCommentDao;
import com.ctrl.forum.entity.Assess;
import com.ctrl.forum.ui.adapter.MineAssessYiAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

/**
 * 已评价订单
 */
public class MineAssessYiFragment extends ToolBarFragment {
    private PullToRefreshListView lv_content;
    private MineAssessYiAdapter assessYiAdapter;
    private List<Assess> assessYis;
    private ReplyCommentDao rcdao;

    public static MineAssessYiFragment newInstance() {
        MineAssessYiFragment fragment = new MineAssessYiFragment();
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
        lv_content = (PullToRefreshListView) view.findViewById(R.id.lv_content);
        initData();
        assessYiAdapter = new MineAssessYiAdapter(getActivity());
        lv_content.setAdapter(assessYiAdapter);

        lv_content.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (assessYis!=null){
                    assessYis.clear();
                    rcdao.queryOrderEvaluation(Arad.preferences.getString("memberId"), "1");
                }else{
                    lv_content.onRefreshComplete();
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (assessYis!=null){
                    assessYis.clear();
                    rcdao.queryOrderEvaluation(Arad.preferences.getString("memberId"), "1");
                }else{
                    lv_content.onRefreshComplete();
                }
            }
        });

        return view;
    }

    private void initData() {
          /*  //默认的数据
            assessYis = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                Assess assess = new Assess();
                assessYis.add(assess);}*/
            //网上获取的数据
       rcdao = new ReplyCommentDao(this);
        rcdao.queryOrderEvaluation(Arad.preferences.getString("memberId"),"1");
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        lv_content.onRefreshComplete();
        if (requestCode==1){
            MessageUtils.showShortToast(getActivity(),"获取已评价订单成功");
            assessYis = rcdao.getAssesses();
            if (assessYis!=null){
                assessYiAdapter.setMessages(assessYis);
            }
        }
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        lv_content.onRefreshComplete();
        MessageUtils.showShortToast(getActivity(),"获取已评价订单失败!");
    }
}
