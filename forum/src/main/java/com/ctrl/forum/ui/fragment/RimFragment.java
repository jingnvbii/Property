package com.ctrl.forum.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.beanu.arad.base.ToolBarFragment;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.dao.RimDao;
import com.ctrl.forum.entity.RimServeCategory;
import com.ctrl.forum.ui.activity.rim.RimCollectServeActivity;
import com.ctrl.forum.ui.activity.rim.RimSearchActivity;
import com.ctrl.forum.ui.adapter.RimGridViewAdapter;
import com.ctrl.forum.ui.adapter.RimListViewAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 周边fragment
 * Created by jason on 2016/4/7.
 */
public class  RimFragment extends ToolBarFragment implements View.OnClickListener{

    @InjectView(R.id.lv_content)
    ListView lv_content; //分类的内容

    private RimGridViewAdapter rimGridViewAdapter;
    private RimListViewAdapter rimListViewAdapter;
    private List<RimServeCategory> category;
    private RimDao rdao;

    public static RimFragment newInstance() {
        RimFragment fragment = new RimFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rim, container, false);
        ButterKnife.inject(this, view);

        initView();

        rimListViewAdapter.setRimGridViewAdapter(rimGridViewAdapter);
        lv_content.setAdapter(rimListViewAdapter);
        rimListViewAdapter.setOnCollect(this);
        rimListViewAdapter.setOnSearch(this);

        return view;
    }

    private void initView() {
        rimGridViewAdapter  = new RimGridViewAdapter(getActivity());
        rimListViewAdapter = new RimListViewAdapter(getActivity());

        rdao = new RimDao(this);
        rdao.getAroundServiceCategory();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_collect:
                startActivity(new Intent(getActivity(),RimCollectServeActivity.class));
                break;
            case R.id.rl_search:
                startActivity(new Intent(getActivity(),RimSearchActivity.class));
                break;
        }
    }



    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if (requestCode==1){
            MessageUtils.showShortToast(getActivity(),"获取成功");
            category = rdao.getRimServeCategory();
            if (category!=null){
                rimListViewAdapter.setData(category);
            }
        }
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
            MessageUtils.showShortToast(getActivity(), errorMessage);
    }

}
