package com.ctrl.forum.ui.activity.rim;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarActivity;
import com.ctrl.forum.R;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.customview.FlowLayoutView;
import com.ctrl.forum.dao.SearchDao;
import com.ctrl.forum.entity.SearchHistory;
import com.ctrl.forum.ui.adapter.RimSearchHistoryAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 周边搜索
 */
public class RimSearchActivity extends ToolBarActivity implements View.OnClickListener{
    @InjectView(R.id.ed_search)
    EditText ed_search;
    @InjectView(R.id.iv_back)
    ImageView iv_back;
    @InjectView(R.id.flowlayout)
    FlowLayoutView flowlayout;
    @InjectView(R.id.rl_history)
    ListView rl_history;

    private SearchDao searchDao;
    private List<SearchHistory> hotSearch;
    private List<SearchHistory> searchHistory;
    private TextView view;
    private String memberId;
    private RimSearchHistoryAdapter rimSearchHistoryAdapter;
    private int PAGE_NUM=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rim_search);
        ButterKnife.inject(this);

        initView();
        initData();

        rl_history.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), RimSearchReaultActivity.class);
                intent.putExtra("keyWord", searchHistory.get(position - 1).getKeyword());
                startActivity(intent);
            }
        });

        //为输入框注册键盘监听事件
      ed_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
          @Override
          public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
              if (actionId == EditorInfo.IME_ACTION_GO) {
                  //隐藏软键盘
                  InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                  if (imm.isActive()) {
                      imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                  }

                  if (!ed_search.getText().toString().equals("")) {
                      Intent intent = new Intent(getApplicationContext(), RimSearchReaultActivity.class);
                      intent.putExtra("keyWord", ed_search.getText().toString());
                      ed_search.setText("");
                      startActivity(intent);
                  }

                  return true;
              }
              return false;
          }
      });
    }

    //初始化数据
    private void initData() {
        searchDao = new SearchDao(this);

        memberId = Arad.preferences.getString("memberId");
        searchDao.getSearchHistoryList(memberId, "3", PAGE_NUM + "", Constant.PAGE_SIZE + "");

        rimSearchHistoryAdapter = new RimSearchHistoryAdapter(this);
        rl_history.setAdapter(rimSearchHistoryAdapter);
    }

    //初始化控件
    private void initView() {
        iv_back.setOnClickListener(this);
        flowlayout.setOnClickListener(this);
    }

    //流式布局
    private void initChildViews() {
        // TODO Auto-generated method stub
        MarginLayoutParams lp = new MarginLayoutParams(
                LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        //lp.leftMargin = 20;
        lp.topMargin = 20;
       // lp.bottomMargin = 10;
        lp.rightMargin = 20;
        for(int i = 0; i < hotSearch.size(); i++){
            view = new TextView(this);
            view.setText(hotSearch.get(i).getKeyword());
            //view.setText(mNames[i]);
            view.setTextSize(16);
            view.setTextColor(getResources().getColor(R.color.rim));
            view.setBackgroundDrawable(getResources().getDrawable(R.drawable.rim_search_flow_text));
            view.setClickable(true);
            final int finalI = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), RimSearchReaultActivity.class);
                    intent.putExtra("keyWord", hotSearch.get(finalI).getKeyword());
                    startActivity(intent);
                }
            });
            flowlayout.addView(view,lp);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.flowlayout:
                break;
        }
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if (requestCode==0){
            searchHistory = searchDao.getSearchHistory();
            if (hotSearch!=null){
                hotSearch.clear();
                flowlayout.removeAllViews();
            }
            if (searchHistory!=null){
                rimSearchHistoryAdapter.setData(searchHistory);
            }
            hotSearch = searchDao.getHotSearch();
            if (hotSearch!=null){
                initChildViews();
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (searchHistory!=null){
            searchHistory.clear();
        }
        searchDao.getSearchHistoryList(memberId, "3", PAGE_NUM + "", Constant.PAGE_SIZE + "");
    }

}
