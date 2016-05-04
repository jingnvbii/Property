package com.ctrl.android.yinfeng.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarFragment;
import com.beanu.arad.utils.AnimUtil;
import com.ctrl.android.yinfeng.R;
import com.ctrl.android.yinfeng.dao.JobDao;
import com.ctrl.android.yinfeng.entity.Job;
import com.ctrl.android.yinfeng.ui.adapter.MyJobAdapter;
import com.ctrl.android.yinfeng.ui.job.JobEndActivity;
import com.ctrl.android.yinfeng.ui.job.JobPendingActivity;
import com.ctrl.android.yinfeng.ui.job.JobProcessedActivity;
import com.ctrl.android.yinfeng.ui.job.JobProcessingActivity;
import com.ctrl.android.yinfeng.ui.job.MyJobActivity;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by jason on 2015/10/26.
 */
public class JobFragment extends ToolBarFragment {
    @InjectView(R.id.pull_to_refresh_listView_repairs)
    PullToRefreshListView pull_to_refresh_listView_repairs;//可刷新的列表
    private ListView mListView;
    private MyJobAdapter myRepairsAdapter;
    private String progressState; //处理状态
    private List<Job> listMap;//
    private JobDao dao;
    private int currentpage=1;
    private View rootView;
    private int bol=1;
    private List<Job> mJobList;


    public static JobFragment newInstance(String state){
        JobFragment fragment=new JobFragment();
        fragment.progressState=state;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.j_fragment_my_repairs,container,false);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser&&bol==1) {
            //相当于Fragment的onResume
           dao=new JobDao(this);
           dao.requestRepairList(Arad.preferences.getString("communityId"),Arad.preferences.getString("staffId"), progressState, String.valueOf(currentpage));

        } else {
            //相当于Fragment的onPause
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
        mListView=pull_to_refresh_listView_repairs.getRefreshableView();
        myRepairsAdapter=new MyJobAdapter(getActivity(),progressState);
        mListView.setAdapter(myRepairsAdapter);
        mListView.setDivider(null);
        mListView.setDividerHeight(15);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (progressState.equals("0") && dao.getRepairList().size() > 0) {
                    Intent intent = new Intent(getActivity(), JobPendingActivity.class);
                    intent.putExtra("orderType", dao.getRepairList().get(position - 1).getOrderType());
                    intent.putExtra("repairDemandId", dao.getRepairList().get(position - 1).getId());
                    startActivityForResult(intent, 666);
                    AnimUtil.intentSlidIn(getActivity());
                }
                if (progressState.equals("1") && dao.getRepairList().size() > 0) {
                    if (dao.getRepairList().get(position - 1).getIsFeedback().equals("0")) {
                        Intent intent = new Intent(getActivity(), JobProcessingActivity.class);
                        intent.putExtra("progressState", progressState);
                        intent.putExtra("repairDemandId", dao.getRepairList().get(position - 1).getId());
                        startActivityForResult(intent, 331);
                        AnimUtil.intentSlidIn(getActivity());
                    } else {
                        Intent intent = new Intent(getActivity(), JobProcessedActivity.class);
                        intent.putExtra("progressState", progressState);
                        intent.putExtra("repairDemandId1", dao.getRepairList().get(position - 1).getId());
                        intent.addFlags(999);
                        startActivity(intent);
                        AnimUtil.intentSlidIn(getActivity());
                    }
                }
                if (progressState.equals("2") && dao.getRepairList().size() > 0) {
                    Intent intent = new Intent(getActivity(), JobProcessedActivity.class);
                    intent.putExtra("progressState", progressState);
                    intent.putExtra("repairDemandId", dao.getRepairList().get(position - 1).getId());
                    startActivity(intent);
                    AnimUtil.intentSlidIn(getActivity());
                }
                if (progressState.equals("3") && dao.getRepairList().size() > 0) {
                    Intent intent = new Intent(getActivity(), JobEndActivity.class);
                    intent.putExtra("repairDemandId", dao.getRepairList().get(position - 1).getId());
                    startActivity(intent);
                    AnimUtil.intentSlidIn(getActivity());

                }
            }
        });
        pull_to_refresh_listView_repairs.setMode(PullToRefreshBase.Mode.BOTH);
        pull_to_refresh_listView_repairs.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                currentpage = 1;
                dao.getRepairList().clear();
                dao.requestRepairList(Arad.preferences.getString("communityId"), Arad.preferences.getString("staffId"), progressState, String.valueOf(currentpage));

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                currentpage += 1;
                dao.requestRepairList(Arad.preferences.getString("communityId"), Arad.preferences.getString("staffId"), progressState, String.valueOf(currentpage));
            }
        });


    }


    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        pull_to_refresh_listView_repairs.onRefreshComplete();
        if(errorNo.equals("002")){
            //
        }
    }


    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        pull_to_refresh_listView_repairs.onRefreshComplete();
        bol=0;
        if(requestCode==0){
            mJobList = dao.getRepairList();
            myRepairsAdapter.setList(mJobList);

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==666&&resultCode==667){
            MyJobActivity activity=(MyJobActivity)getActivity();
            activity.getAdapter().reLoad();
        }
        if(requestCode==331&&resultCode==333){
            MyJobActivity activity=(MyJobActivity)getActivity();
            activity.getAdapter().reLoad();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        pull_to_refresh_listView_repairs.onRefreshComplete();
    }
}
