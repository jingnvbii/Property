package com.ctrl.forum.ui.activity.Invitation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.AnimUtil;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.customview.PLA_AdapterView;
import com.ctrl.forum.customview.XListView;
import com.ctrl.forum.dao.InvitationDao;
import com.ctrl.forum.dao.SearchDao;
import com.ctrl.forum.entity.HotSearch;
import com.ctrl.forum.entity.Post;
import com.ctrl.forum.entity.SearchHistory;
import com.ctrl.forum.ui.activity.store.StoreCommodityDetailActivity;
import com.ctrl.forum.ui.activity.store.StoreShopListVerticalStyleActivity;
import com.ctrl.forum.ui.adapter.InvitationListViewAdapter;
import com.ctrl.forum.ui.adapter.InvitationListViewBlockStyleAdapter;
import com.ctrl.forum.ui.adapter.InvitationListViewFriendStyleAdapter;
import com.ctrl.forum.ui.adapter.InvitationListViewPinterestStyleAdapter;
import com.ctrl.forum.ui.adapter.InvitationSearchGridViewAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 *
 *帖子搜索 activity
 * Created by jason on 2016/4/12.
 */
public class InvitationSearchActivity extends AppToolBarActivity implements View.OnClickListener,XListView.IXListViewListener{
    private static  int PAGE_NUM =1 ;
    @InjectView(R.id.gridview_invitation_search)//热门搜索
            GridView gridview_invitation_search;
    @InjectView(R.id.et_invitation_search)//搜索输入
    EditText et_invitation_search;
    @InjectView(R.id.tv_invitation_search)//搜索
    TextView tv_invitation_search;
    @InjectView(R.id.lv_invitation_history_search)//历史记录
    ListView lv_invitation_history_search;
    @InjectView(R.id.tv_delete_invitation_history)//清空历史记录
    TextView tv_delete_invitation_history;
    @InjectView(R.id.iv_toolbar_left)//清空历史记录
            ImageView iv_toolbar_left;
    @InjectView(R.id.ll_search)
    LinearLayout ll_search;
    @InjectView(R.id.ll_linear_layout)
    LinearLayout ll_linear_layout;
    @InjectView(R.id.xlv_pinerest_style)
    XListView xlv_pinerest_style;

    @InjectView(R.id.lv_pull)
    PullToRefreshListView lv_pull;

    private SearchDao sdao;
    private List<SearchHistory> listHistorySearch;
    private List<HotSearch> listHotSearch;
    private InvitationSearchGridViewAdapter mInvitationSearchGridViewAdapter;
    private ArrayAdapter mListHistorySearchAdapter;
    private String styleType;
    private InvitationDao idao;
    private String channelId;

    private InvitationListViewAdapter invitationListViewAdapter;
    private InvitationListViewBlockStyleAdapter mInvitationListViewBlockStyleAdapter;
    private InvitationListViewFriendStyleAdapter mInvitationListViewFriendStyleAdapter;
    private InvitationListViewPinterestStyleAdapter mInvitationListViewPinterestStyleAdapter;
    private List<Post> listPost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_search);
        ButterKnife.inject(this);
        // 隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
        initData();
    }

    private void initView() {
        tv_invitation_search.setOnClickListener(this);
        tv_delete_invitation_history.setOnClickListener(this);
        iv_toolbar_left.setOnClickListener(this);
      // styleType=getIntent().getStringExtra("styleType");
        styleType="1";
        channelId=getIntent().getStringExtra("channelId");
        et_invitation_search.addTextChangedListener(watcher);

    }

    private TextWatcher watcher = new TextWatcher() {
        //文字变化时
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            if(s.length()==0){
                ll_search.setVisibility(View.VISIBLE);
                lv_pull.setVisibility(View.GONE);
                ll_linear_layout.setVisibility(View.GONE);
            }
        }
        //文字变化前

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
        }

        //文字变化后
        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        et_invitation_search.setSelection(et_invitation_search.getText().length());
    }

    private void initData() {
        sdao=new SearchDao(this);
        idao=new InvitationDao(this);
        sdao.requestSearchHistory(Arad.preferences.getString("memberId"),"0","","");
        mInvitationSearchGridViewAdapter=new InvitationSearchGridViewAdapter(this);
        gridview_invitation_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                et_invitation_search.setText(listHotSearch.get(position).getKeyword());
                et_invitation_search.setSelection(et_invitation_search.getText().length());
            }
        });
        lv_invitation_history_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                et_invitation_search.setText(listHistorySearch.get(position).getKeyword());
                et_invitation_search.setSelection(et_invitation_search.getText().length());
            }
        });

        mInvitationListViewPinterestStyleAdapter=new InvitationListViewPinterestStyleAdapter(this);
        invitationListViewAdapter=new InvitationListViewAdapter(this);
        mInvitationListViewBlockStyleAdapter=new InvitationListViewBlockStyleAdapter(this);
        mInvitationListViewFriendStyleAdapter=new InvitationListViewFriendStyleAdapter(this);

        if(styleType.equals("1")){
            lv_pull.setAdapter(invitationListViewAdapter);
        }else if(styleType.equals("2")){
            lv_pull.setAdapter(mInvitationListViewBlockStyleAdapter);
        }else if(styleType.equals("4")){
            lv_pull.setAdapter(mInvitationListViewFriendStyleAdapter);
        }else if(styleType.equals("3")) {
              xlv_pinerest_style.setAdapter(mInvitationListViewPinterestStyleAdapter);
        }else {

        }

        xlv_pinerest_style.setFocusable(false);
        xlv_pinerest_style.setOnItemClickListener(new PLA_AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(PLA_AdapterView<?> parent, View view, int position, long id) {
               // int nowPos = position - xlv_pinerest_style.getHeaderViewsCount();
                Intent intent = null;
                String contentType = listPost.get(position-1).getContentType();
                switch (contentType) {
                    case "0":
                        intent = new Intent(InvitationSearchActivity.this, InvitationPinerestGalleyActivity.class);
                        intent.putExtra("id", listPost.get(position-1).getId());
                        startActivityForResult(intent, 202);
                        AnimUtil.intentSlidIn(InvitationSearchActivity.this);
                        break;
                    case "1":
                        Uri uri = Uri.parse(listPost.get(position-1).getArticleLink());
                        intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                        AnimUtil.intentSlidIn(InvitationSearchActivity.this);
                        break;
                    case "2":
                        intent = new Intent(InvitationSearchActivity.this, StoreCommodityDetailActivity.class);
                        intent.putExtra("id", listPost.get(position-1).getLinkItemId());
                       startActivity(intent);
                        AnimUtil.intentSlidIn(InvitationSearchActivity.this);
                        break;
                    case "3":
                        intent = new Intent(InvitationSearchActivity.this, StoreShopListVerticalStyleActivity.class);
                        intent.putExtra("id", listPost.get(position-1).getLinkItemId());
                        startActivity(intent);
                        AnimUtil.intentSlidIn(InvitationSearchActivity.this);
                        break;
                }


            }
        });

        xlv_pinerest_style.setPullLoadEnable(true);
        xlv_pinerest_style.setXListViewListener(this);

        lv_pull.setMode(PullToRefreshBase.Mode.BOTH);
        lv_pull.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (listPost != null)
                    listPost.clear();
                PAGE_NUM = 1;
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        idao.requestPostListByCategory(Arad.preferences.getString("memberId"), channelId, "0", et_invitation_search.getText().toString().trim(), "", PAGE_NUM, Constant.PAGE_SIZE);
                    }

                }, 500);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM += 1;
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        idao.requestPostListByCategory(Arad.preferences.getString("memberId"), channelId, "0", et_invitation_search.getText().toString().trim(), "", PAGE_NUM, Constant.PAGE_SIZE);
                    }
                }, 500);

            }
        });
        lv_pull.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              //  newPosition = position - lv.getHeaderViewsCount();
                Intent intent = null;
                String type = listPost.get(position-1).getSourceType();
                String contentType=listPost.get(position-1).getContentType();
                if(styleType.equals("4")){
                    switch (contentType){
                        case "0":
                            intent = new Intent(InvitationSearchActivity.this, InvitationDetailActivity.class);
                            intent.putExtra("id", listPost.get(position-1).getId());
                            intent.putExtra("reportid", listPost.get(position-1).getReporterId());
                            startActivity(intent);
                            AnimUtil.intentSlidIn(InvitationSearchActivity.this);
                            break;
                        case "1":
                            Uri uri = Uri.parse(listPost.get(position-1).getArticleLink());
                            intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                            AnimUtil.intentSlidIn(InvitationSearchActivity.this);
                            break;
                        case "2":
                            intent=new Intent(InvitationSearchActivity.this, StoreCommodityDetailActivity.class);
                            intent.putExtra("id",listPost.get(position-1).getLinkItemId());
                            startActivity(intent);
                            AnimUtil.intentSlidIn(InvitationSearchActivity.this);
                            break;
                        case "3":
                            intent=new Intent(InvitationSearchActivity.this, StoreShopListVerticalStyleActivity.class);
                            intent.putExtra("id",listPost.get(position-1).getLinkItemId());
                           startActivity(intent);
                            AnimUtil.intentSlidIn(InvitationSearchActivity.this);
                            break;
                    }

                }else {
                    switch (contentType){
                        case "0":
                            switch (type) {
                                case "0"://平台
                                    intent = new Intent(InvitationSearchActivity.this, InvitationDetailFromPlatformActivity.class);
                                    intent.putExtra("id", listPost.get(position-1).getId());
                                    startActivity(intent);
                                    AnimUtil.intentSlidIn(InvitationSearchActivity.this);

                                    break;
                                case "1"://app
                                    intent = new Intent(InvitationSearchActivity.this, InvitationPinterestDetailActivity.class);
                                    intent.putExtra("id", listPost.get(position-1).getId());
                                    startActivity(intent);
                                    AnimUtil.intentSlidIn(InvitationSearchActivity.this);
                                    break;
                            }
                            break;
                        case "1":
                            Uri uri = Uri.parse(listPost.get(position-1).getArticleLink());
                            intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                            AnimUtil.intentSlidIn(InvitationSearchActivity.this);
                            break;
                        case "2":
                            intent=new Intent(InvitationSearchActivity.this, StoreCommodityDetailActivity.class);
                            intent.putExtra("id",listPost.get(position-1).getLinkItemId());
                           startActivity(intent);
                            AnimUtil.intentSlidIn(InvitationSearchActivity.this);
                            break;
                        case "3":
                            intent=new Intent(InvitationSearchActivity.this, StoreShopListVerticalStyleActivity.class);
                            intent.putExtra("id",listPost.get(position-1).getLinkItemId());
                           startActivity(intent);
                            AnimUtil.intentSlidIn(InvitationSearchActivity.this);
                            break;
                    }

                }

            }
        });


    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        showProgress(false);
        lv_pull.onRefreshComplete();
        xlv_pinerest_style.stopLoadMore();
        xlv_pinerest_style.stopRefresh();
        ll_search.setVisibility(View.GONE);
       /* ll_linear_layout.setVisibility(View.GONE);
        lv_pull.setVisibility(View.GONE);*/
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        lv_pull.onRefreshComplete();
        xlv_pinerest_style.stopLoadMore();
        xlv_pinerest_style.stopRefresh();
        if (requestCode == 1) {
            ll_search.setVisibility(View.GONE);
            listPost=idao.getListPost();
            if(styleType.equals("3")){
                lv_pull.setVisibility(View.GONE);
                ll_linear_layout.setVisibility(View.VISIBLE);
              //  xlv_pinerest_style.setVisibility(View.VISIBLE);
                mInvitationListViewPinterestStyleAdapter.setList(listPost);
            }else {
                ll_linear_layout.setVisibility(View.GONE);
                lv_pull.setVisibility(View.VISIBLE);
                if(styleType.equals("1")){
                   invitationListViewAdapter.setList(listPost);
                }else if(styleType.equals("2")){
                   mInvitationListViewBlockStyleAdapter.setList(listPost);
                }else if(styleType.equals("4")){
                   mInvitationListViewFriendStyleAdapter.setList(listPost);
                }else {
                    //
                }
            }
            showProgress(false);
        }

        if(requestCode==1000){
            MessageUtils.showShortToast(this, "清空历史记录成功");
            if(listHistorySearch!=null)
            listHistorySearch.clear();
            lv_invitation_history_search.setAdapter(mListHistorySearchAdapter);

        }
        if(requestCode==999){
            listHistorySearch=sdao.getListSearchHistory();
            listHotSearch=sdao.getListHotSearch();

            ArrayList<String> listHistorySearchStr = new ArrayList<>();
            for(int i=0;i<listHistorySearch.size();i++){
                listHistorySearchStr.add(listHistorySearch.get(i).getKeyword());
            }

            mListHistorySearchAdapter=new ArrayAdapter(this,R.layout.spinner_layout,listHistorySearchStr);
            mInvitationSearchGridViewAdapter.setList(listHotSearch);
            gridview_invitation_search.setAdapter(mInvitationSearchGridViewAdapter);
            lv_invitation_history_search.setAdapter(mListHistorySearchAdapter);
        }
    }

    @Override
    public boolean setupToolBarLeftText(TextView mLeftText) {
        mLeftText.setText("取消");
        mLeftText.setTextColor(getResources().getColor(R.color.text_blue));
        mLeftText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        return true;
    }

    @Override
    public String setupToolBarTitle() {
        TextView tv_title = getmTitle();
        tv_title.setTextColor(getResources().getColor(R.color.text_black));
        return "所在位置";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_toolbar_left:
                onBackPressed();
                break;
            case R.id.tv_invitation_search:
                if(TextUtils.isEmpty(et_invitation_search.getText().toString().trim())){
                    MessageUtils.showShortToast(InvitationSearchActivity.this,"搜索关键字为空");
                    return;
                }
               /* Intent intent =new Intent();
                intent.putExtra("keyword",et_invitation_search.getText().toString().trim());
                setResult(2222,intent);
                finish();*/
                showProgress(true);
                idao.requestPostListByCategory(Arad.preferences.getString("memberId"), channelId, "0", et_invitation_search.getText().toString().trim(), "", PAGE_NUM, Constant.PAGE_SIZE);
                break;
            case R.id.tv_delete_invitation_history:
                sdao.requestDeleteSearchHistory(Arad.preferences.getString("memberId"),"0");
                break;
        }

    }

    @Override
    public void onRefresh() {
        if (listPost != null)
            listPost.clear();
        PAGE_NUM = 1;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                idao.requestPostListByCategory(Arad.preferences.getString("memberId"), channelId, "0", et_invitation_search.getText().toString().trim(), "", PAGE_NUM, Constant.PAGE_SIZE);
            }

        }, 500);
    }

    @Override
    public void onLoadMore() {
        PAGE_NUM += 1;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                idao.requestPostListByCategory(Arad.preferences.getString("memberId"), channelId, "0", et_invitation_search.getText().toString().trim(), "", PAGE_NUM, Constant.PAGE_SIZE);
            }
        }, 500);
    }
}
