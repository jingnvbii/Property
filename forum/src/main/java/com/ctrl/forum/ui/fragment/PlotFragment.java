package com.ctrl.forum.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarFragment;
import com.ctrl.forum.R;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.dao.PlotDao;
import com.ctrl.forum.entity.Post;
import com.ctrl.forum.ui.activity.mine.MineFindFlotActivity;
import com.ctrl.forum.ui.activity.plot.PlotRimServeActivity;
import com.ctrl.forum.ui.activity.plot.PlotSearchResultActivity;
import com.ctrl.forum.ui.adapter.InvitationListViewFriendStyleAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 小区 fragment
 * Created by jason on 2016/4/7.
 */
public class PlotFragment extends ToolBarFragment implements View.OnClickListener{
    @InjectView(R.id.lv_content)
    PullToRefreshListView lv_content;
    @InjectView(R.id.tv_plot_name)
    TextView tv_plot_name; //小区名
    @InjectView(R.id.rim_post)
    TextView rim_post; //发帖
    @InjectView(R.id.rim_serve)
    TextView rim_serve; //周边服务
    @InjectView(R.id.et_search)
    EditText et_search;
    @InjectView(R.id.iv_back)
    ImageView iv_back;
    @InjectView(R.id.tv_sign)
    TextView tv_sign;

    private InvitationListViewFriendStyleAdapter invitationListViewFriendStyleAdapter;
    private List<Post> posts;
    private String communityId;
    private PlotDao plotDao;
    private int PAGE_NUM =1;
    private String str="";

    public static PlotFragment newInstance() {
        PlotFragment fragment = new PlotFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plot, container, false);
        ButterKnife.inject(this, view);

        checkActivity();

        initView();
        invitationListViewFriendStyleAdapter = new InvitationListViewFriendStyleAdapter(getActivity());
        lv_content.setAdapter(invitationListViewFriendStyleAdapter);

        initData();

        lv_content.setMode(PullToRefreshBase.Mode.BOTH);
        lv_content.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (posts != null) {
                    posts.clear();
                    PAGE_NUM = 1;
                    invitationListViewFriendStyleAdapter = new InvitationListViewFriendStyleAdapter(getActivity());
                    lv_content.setAdapter(invitationListViewFriendStyleAdapter);
                }
                plotDao.initCommunity(Arad.preferences.getString("memberId"), communityId, PAGE_NUM + "", Constant.PAGE_SIZE + "");
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (posts != null) {
                    PAGE_NUM += 1;
                    plotDao.initCommunity(Arad.preferences.getString("memberId"), communityId, PAGE_NUM + "", Constant.PAGE_SIZE + "");
                } else {
                    lv_content.onRefreshComplete();
                }
            }
        });

        //listview增加头部布局
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        View headview = getActivity().getLayoutInflater().inflate(R.layout.item_plot_header_view, lv_content, false);
        headview.setLayoutParams(layoutParams);
        lv_content.getRefreshableView().addHeaderView(headview);

        //为输入框注册键盘监听事件
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    //隐藏软键盘
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }
                    if (!et_search.getText().toString().equals("")) {
                        Intent intent = new Intent(getActivity(), PlotSearchResultActivity.class);
                        intent.putExtra("keyWord", et_search.getText().toString());
                        et_search.setText("");
                        startActivity(intent);
                    }
                    return true;
                }
                return false;
            }
        });

        return view;
    }

    //检查依附的activity
    private void checkActivity() {
        Bundle bundle = getArguments();
        str = bundle.getString("str");
        if (str.equals("我")){
            tv_sign.setText("我");
            iv_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
        }

    }

    private void initView() {
        tv_plot_name.setText(Arad.preferences.getString("communityName"));

        tv_plot_name.setOnClickListener(this);
        rim_post.setOnClickListener(this);
        rim_serve.setOnClickListener(this);

    }

    private void initData() {
        communityId = Arad.preferences.getString("communityId");
        plotDao = new PlotDao(this);

       /* posts = new ArrayList<>();
        for (int i = 0; i<9;i++){
            Post post = new Post();
            post.setId(i+"");
            posts.add(post);
        }
        invitationListViewFriendStyleAdapter.setList(posts);*/
        plotDao.initCommunity(Arad.preferences.getString("memberId"),communityId,PAGE_NUM+"", Constant.PAGE_SIZE+"");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        lv_content.onRefreshComplete();
        if (requestCode==2){
            posts = plotDao.getPostList();
            if (posts!=null){
                invitationListViewFriendStyleAdapter.setList(posts);
            }
        }
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        lv_content.onRefreshComplete();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_plot_name:
                startActivity(new Intent(getActivity(),MineFindFlotActivity.class));
                break;
            case R.id.rim_post:

                break;
            case R.id.rim_serve:
                startActivity(new Intent(getActivity(), PlotRimServeActivity.class));
                break;
        }
    }
}
