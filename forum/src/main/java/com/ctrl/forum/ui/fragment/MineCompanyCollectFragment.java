package com.ctrl.forum.ui.fragment;

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
import com.ctrl.forum.dao.CollectDao;
import com.ctrl.forum.entity.CompanyCollect;
import com.ctrl.forum.ui.adapter.CompanyCollectFragmentAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

/**
 * 我的收藏-店铺收藏
 * Created by Administrator on 2016/5/13.
 */
public class MineCompanyCollectFragment extends ToolBarFragment{
    private List<CompanyCollect> companyCollects;
    private CompanyCollectFragmentAdapter companyCollectFragmentAdapter;
    private PullToRefreshListView lv_content;
    private CollectDao cdao;
    private int PAGE_NUM=1;
    private String couponEnable,packetEnable;

    public static MineCompanyCollectFragment newInstance() {
        MineCompanyCollectFragment fragment = new MineCompanyCollectFragment();
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
        View view = inflater.inflate(R.layout.fragment_mine_list, container, false);

        lv_content = (PullToRefreshListView) view.findViewById(R.id.lv_content);
        companyCollectFragmentAdapter = new CompanyCollectFragmentAdapter(getActivity());
        lv_content.setAdapter(companyCollectFragmentAdapter);
        initData();

        lv_content.setMode(PullToRefreshBase.Mode.BOTH);
        lv_content.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (companyCollects != null) {
                    companyCollects.clear();
                    PAGE_NUM = 1;
                }
                cdao.companysCollection(Arad.preferences.getString("memberId"), "100", "100", PAGE_NUM + "", Constant.PAGE_SIZE + "");
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (companyCollects != null) {
                    PAGE_NUM += 1;
                    cdao.companysCollection(Arad.preferences.getString("memberId"), "100", "100", PAGE_NUM + "", Constant.PAGE_SIZE + "");
                } else {
                    lv_content.onRefreshComplete();
                }
            }
        });

        lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (companyCollects!=null) {
                    CompanyCollect company = companyCollects.get(position);
                    couponEnable = company.getCouponEnable();//是否可以使用现金劵,0不可以,1可以
                    packetEnable = company.getPacketEnable();
                    useEnable();
                }
            }
        });

        return view;
    }

    public void useEnable(){
        if (couponEnable.equals("0")){
            MessageUtils.showShortToast(getActivity(),"不可以使用现金劵");
        }
        if(couponEnable.equals("1")){
            MessageUtils.showShortToast(getActivity(), "可以使用现金劵");
        }
        if (packetEnable.equals("0")){
            MessageUtils.showShortToast(getActivity(),"不可以使用优惠劵");
        }
        if (packetEnable.equals("1")){
            MessageUtils.showShortToast(getActivity(),"可以使用优惠劵劵");
        }
    }

    private void initData() {
        cdao = new CollectDao(this);
        //cdao.companysCollection(会员id,纬度,经度,当前页号,每页的数目);
       String latitude =  Arad.preferences.getString("latitude");
        String lontitude =  Arad.preferences.getString("lontitude");
        cdao.companysCollection(Arad.preferences.getString("memberId"), latitude,lontitude, PAGE_NUM + "", Constant.PAGE_SIZE + "");
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        lv_content.onRefreshComplete();
        if(requestCode == 0){
            companyCollects = cdao.getCompanyCollects();
            if (companyCollects!=null){
                companyCollectFragmentAdapter.setList(companyCollects);
            }
        }
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        lv_content.onRefreshComplete();
    }


}
