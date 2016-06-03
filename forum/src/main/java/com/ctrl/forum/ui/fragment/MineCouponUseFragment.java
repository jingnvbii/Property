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
import com.ctrl.forum.dao.CouponsDao;
import com.ctrl.forum.entity.Redenvelope;
import com.ctrl.forum.ui.adapter.MineCouponListAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

/**
 * 优惠劵 - 已使用
 */
public class MineCouponUseFragment extends ToolBarFragment {
    private List<Redenvelope> redenvelopes;
    private PullToRefreshListView lv_content;
    private MineCouponListAdapter couponListAdapter;
    private CouponsDao cdao;
    private int PAGE_NUM = 1;


    public static MineCouponUseFragment newInstance() {
        MineCouponUseFragment fragment = new MineCouponUseFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MineCouponUseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mine_list, container, false);
        lv_content = (PullToRefreshListView) v.findViewById(R.id.lv_content);
        initData();

        lv_content.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (redenvelopes!=null) {
                    redenvelopes.clear();
                    PAGE_NUM = 1;
                }
                cdao.getMemberRedenvelope("1", "1", Arad.preferences.getString("memberId"), PAGE_NUM + "", Constant.PAGE_SIZE + "");
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (redenvelopes!=null) {
                    PAGE_NUM += 1;
                    cdao.getMemberRedenvelope("1", "1", Arad.preferences.getString("memberId"), Constant.PAGE_NUM + "", Constant.PAGE_SIZE + "");
                } else {
                    lv_content.onRefreshComplete();
                }
            }
        });
        return v;
    }

    private void initData() {
       cdao = new CouponsDao(this);
        cdao.getMemberRedenvelope("1", "1", Arad.preferences.getString("memberId"), Constant.PAGE_NUM + "", Constant.PAGE_SIZE + "");

        int resources = R.layout.item_mine_huiuse;
        couponListAdapter = new MineCouponListAdapter(getActivity(),resources);
        lv_content.setAdapter(couponListAdapter);
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        lv_content.onRefreshComplete();
        if (requestCode==1){
            MessageUtils.showShortToast(getActivity(), "获取优惠劵成功");
            redenvelopes = cdao.getRedenvelopes();
            if (redenvelopes!=null) {
                couponListAdapter.setMessages(redenvelopes);
            }
        }
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        MessageUtils.showShortToast(getActivity(), "获取失败");
        lv_content.onRefreshComplete();
    }
}
