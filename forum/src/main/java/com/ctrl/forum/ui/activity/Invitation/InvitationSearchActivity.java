package com.ctrl.forum.ui.activity.Invitation;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.dao.SearchDao;
import com.ctrl.forum.entity.HotSearch;
import com.ctrl.forum.entity.SearchHistory;
import com.ctrl.forum.ui.adapter.InvitationSearchGridViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 *
 *帖子搜索 activity
 * Created by jason on 2016/4/12.
 */
public class InvitationSearchActivity extends AppToolBarActivity implements View.OnClickListener{
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
    private SearchDao sdao;
    private List<SearchHistory> listHistorySearch;
    private List<HotSearch> listHotSearch;
    private InvitationSearchGridViewAdapter mInvitationSearchGridViewAdapter;
    private ArrayAdapter mListHistorySearchAdapter;
    private String styleType;

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
        styleType=getIntent().getStringExtra("styleType");
    }

    private void initData() {
        sdao=new SearchDao(this);
        sdao.requestSearchHistory(Arad.preferences.getString("memberId"),"0","","");
        mInvitationSearchGridViewAdapter=new InvitationSearchGridViewAdapter(this);
        gridview_invitation_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                et_invitation_search.setText(listHotSearch.get(position).getKeyword());
            }
        });
        lv_invitation_history_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                et_invitation_search.setText(listHistorySearch.get(position).getKeyword());
            }
        });

    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
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
                Intent intent =new Intent();
                intent.putExtra("keyword",et_invitation_search.getText().toString().trim());
                setResult(2222,intent);
                finish();
                break;
            case R.id.tv_delete_invitation_history:
                sdao.requestDeleteSearchHistory(Arad.preferences.getString("memberId"),"0");
                break;
        }

    }
}
