package com.ctrl.android.yinfeng.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarFragment;
import com.beanu.arad.utils.AnimUtil;
import com.ctrl.android.yinfeng.R;
import com.ctrl.android.yinfeng.dao.ReportDao;
import com.ctrl.android.yinfeng.entity.Report;
import com.ctrl.android.yinfeng.ui.adapter.ReportAdapter;
import com.ctrl.android.yinfeng.ui.ereport.EReportDetailActivity;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by jason on 2015/10/26.
 */
public class ReportFragment extends ToolBarFragment {
    @InjectView(R.id.pull_to_refresh_listView_report)
    PullToRefreshListView pull_to_refresh_listView_report;//可刷新的列表
    private ListView mListView;
    private ReportAdapter myReportAdapter;
    private String progressState; //处理状态
    private List<Report> listMap;//
    private int currentpage=1;
    private View rootView;
    private int bol=1;
    private ReportDao dao;
    private String kindId;


    public static ReportFragment newInstance(String state,String kindId){
        ReportFragment fragment=new ReportFragment();
        fragment.progressState=state;
        fragment.kindId=kindId;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       return inflater.inflate(R.layout.report_fragment,container,false);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser&&bol==1) {
            Log.i("Tag","Tag123131");
            //相当于Fragment的onResume
            dao=new ReportDao(this);
            if(kindId.equals("")){
            dao.requestReportList(Arad.preferences.getString("communityId"),
                    Arad.preferences.getString("staffId"),
                    progressState, String.valueOf(currentpage),
                    "");}
            else {
                dao.requestReportList(Arad.preferences.getString("communityId"),
                        Arad.preferences.getString("staffId"),
                        progressState, String.valueOf(currentpage),
                        kindId);
            }
           // dao.requestReportList("1", progressState, "");

        } else {
            //相当于Fragment的onPause
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
        mListView=pull_to_refresh_listView_report.getRefreshableView();
        myReportAdapter=new ReportAdapter(getActivity(),progressState);
        mListView.setAdapter(myReportAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (progressState.equals("") && dao.getReportList().size() > 0) {
                    Intent intent = new Intent(getActivity(), EReportDetailActivity.class);
                    intent.putExtra("eventReportId", dao.getReportList().get(position - 1).getId());
                    startActivity(intent);
                    AnimUtil.intentSlidIn(getActivity());
                }
                if (progressState.equals("0") && dao.getReportList().size() > 0) {
                    Intent intent = new Intent(getActivity(), EReportDetailActivity.class);
                    intent.putExtra("eventReportId", dao.getReportList().get(position - 1).getId());
                    startActivity(intent);
                    AnimUtil.intentSlidIn(getActivity());
                }
                if (progressState.equals("1") && dao.getReportList().size() > 0) {
                    Intent intent = new Intent(getActivity(), EReportDetailActivity.class);
                    intent.putExtra("eventReportId", dao.getReportList().get(position - 1).getId());
                    startActivity(intent);
                    AnimUtil.intentSlidIn(getActivity());
                }
                if (progressState.equals("2") && dao.getReportList().size() > 0) {
                    Intent intent = new Intent(getActivity(), EReportDetailActivity.class);
                    intent.putExtra("eventReportId", dao.getReportList().get(position - 1).getId());
                    startActivity(intent);
                    AnimUtil.intentSlidIn(getActivity());

                }
            }
        });

        pull_to_refresh_listView_report.setMode(PullToRefreshBase.Mode.BOTH);
        pull_to_refresh_listView_report.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                currentpage = 1;
                dao.getReportList().clear();
                if(kindId.equals("")){
                    dao.requestReportList(Arad.preferences.getString("communityId"),
                            Arad.preferences.getString("staffId"),
                            progressState, String.valueOf(currentpage),
                            "");}
                else {
                    dao.requestReportList(Arad.preferences.getString("communityId"),
                            Arad.preferences.getString("staffId"),
                            progressState, String.valueOf(currentpage),
                            kindId);
                }

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                currentpage += 1;
                if(kindId.equals("")){
                    dao.requestReportList(Arad.preferences.getString("communityId"),
                            Arad.preferences.getString("staffId"),
                            progressState, String.valueOf(currentpage),
                            "");}
                else {
                    dao.requestReportList(Arad.preferences.getString("communityId"),
                            Arad.preferences.getString("staffId"),
                            progressState, String.valueOf(currentpage),
                            kindId);
                }
            }
        });


    }


    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        pull_to_refresh_listView_report.onRefreshComplete();
        if(errorNo.equals("002")){
            //
        }
    }


    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        pull_to_refresh_listView_report.onRefreshComplete();
        bol=0;
        if(requestCode==0){
            myReportAdapter.setList(dao.getReportList());
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        pull_to_refresh_listView_report.onRefreshComplete();
    }
    /**
     * 测试 获取数据的方法
     * */
/*    private List<Map<String,String>> getListMap(String str){
        List<Map<String,String>> list = new ArrayList<>();
        for(int i = 0 ; i < 20 ; i ++){
            Map<String,String> map = new HashMap<String,String>();
            map.put("number","投诉编号：123132" +i);
            map.put("type","投诉类型：" +"水暖维修" );
            map.put("time","投诉时间：2015/10/19 ");

            list.add(map);
        }
        return list;
    }*/

}
