package com.ctrl.forum.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarFragment;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.dao.CouponsDao;
import com.ctrl.forum.entity.Redenvelope;
import com.ctrl.forum.ui.adapter.MineCouponListAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 优惠劵(未使用)
 */
public class MineCouponFragment extends ToolBarFragment {
    private List<Redenvelope> redenvelopes ;
    private PullToRefreshListView lv_content;
    private MineCouponListAdapter couponListAdapter;
    private CouponsDao cdao;
    private int PAGE_NUM = 1;
    private int resources;
    private String amount;
    private List<Redenvelope> redenvelopeList = new ArrayList<>();

    public static MineCouponFragment newInstance() {
        MineCouponFragment fragment = new MineCouponFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MineCouponFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_mine_list, container, false);
        lv_content = (PullToRefreshListView) v.findViewById(R.id.lv_content);
        initData();

        lv_content.setMode(PullToRefreshBase.Mode.BOTH);
        lv_content.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (redenvelopes != null) {
                    redenvelopes.clear();
                    PAGE_NUM = 1;
                    couponListAdapter = new MineCouponListAdapter(getActivity(), resources);
                    lv_content.setAdapter(couponListAdapter);
                }
                cdao.getMemberRedenvelope("1", "0", Arad.preferences.getString("memberId"), PAGE_NUM + "", Constant.PAGE_SIZE + "");
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (redenvelopes != null) {
                    PAGE_NUM += 1;
                    cdao.getMemberRedenvelope("1", "0", Arad.preferences.getString("memberId"), PAGE_NUM + "", Constant.PAGE_SIZE + "");
                } else {
                    lv_content.onRefreshComplete();
                }
            }
        });

        lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (redenvelopeList!=null && redenvelopeList.get(position-1)!=null){
                    Float amounts = Float.valueOf(amount);
                    Float leastMoney = Float.valueOf(redenvelopeList.get(position-1).getLeastMoney());
                    if (amounts>leastMoney | amounts == leastMoney){
                        Intent intent=new Intent();
                        intent.putExtra("amount",redenvelopeList.get(position-1).getAmount());
                        intent.putExtra("id",redenvelopeList.get(position-1).getId());
                        getActivity().setResult(Activity.RESULT_OK,intent);
                        getActivity().finish();
                    }else{
                         MessageUtils.showShortToast(getActivity(),"您的金额不满"+leastMoney+"元，该劵不可使用！");
                    }
                }
            }
        });

        return v;
    }

    private void initData() {
        resources = R.layout.item_mine_hui;
        couponListAdapter = new MineCouponListAdapter(getActivity(),resources);
        lv_content.setAdapter(couponListAdapter);

        cdao = new CouponsDao(this);
        cdao.getMemberRedenvelope("1", "0", Arad.preferences.getString("memberId"), PAGE_NUM + "", Constant.PAGE_SIZE + "");

        amount = getActivity().getIntent().getStringExtra("amount");
        couponListAdapter.setAmount(amount);
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        lv_content.onRefreshComplete();
        if (requestCode==1){
            redenvelopes = cdao.getRedenvelopes();
            if (redenvelopes!=null) {
                couponListAdapter.setMessages(redenvelopes);
                redenvelopeList.addAll(redenvelopes);
            }
        }
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        lv_content.onRefreshComplete();
    }

}
