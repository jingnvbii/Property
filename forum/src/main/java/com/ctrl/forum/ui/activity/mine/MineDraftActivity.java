package com.ctrl.forum.ui.activity.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarActivity;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.ListItemTypeInterf;
import com.ctrl.forum.dao.EditDao;
import com.ctrl.forum.entity.Drafts;
import com.ctrl.forum.entity.DraftsPostList;
import com.ctrl.forum.ui.adapter.MineDraftsListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MineDraftActivity extends ToolBarActivity implements View.OnClickListener{
    @InjectView(R.id.lv_content)
    ListView lv_content;

    private EditDao editDao;
    private List<ListItemTypeInterf> datas;
    private List<DraftsPostList> draftsPostLists;
    private List<Drafts> drafts;
    private MineDraftsListAdapter mineDraftsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_draft);
        ButterKnife.inject(this);
        initView();
        initData();

    }

    private void initView() {
        datas = new ArrayList<>();
        mineDraftsListAdapter = new MineDraftsListAdapter(this);
        lv_content.setAdapter(mineDraftsListAdapter);
        mineDraftsListAdapter.setOnEdit(this);
        mineDraftsListAdapter.setOnDelete(this);
    }

    private void initData() {
        editDao = new EditDao(this);
        editDao.getDraftsList(Arad.preferences.getString("memberId"));
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if (requestCode==4){
            draftsPostLists = editDao.getDraftsPostLists();
            if (draftsPostLists!=null){
                for (int i =0;i<draftsPostLists.size();i++){
                    datas.add(draftsPostLists.get(i));
                    drafts = draftsPostLists.get(i).getDraftsList();
                    for (int j=0;j<drafts.size();j++){
                        datas.add(drafts.get(j));
                    }
                }
                mineDraftsListAdapter.setList(datas);
            }
        }
    }

    @Override
    public boolean setupToolBarLeftButton(ImageView leftButton) {
        leftButton.setImageResource(R.mipmap.white_arrow_left_none);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        return true;
    }

    @Override
    public String setupToolBarTitle() {return getResources().getString(R.string.my_drafts);}


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_edit:
                MessageUtils.showShortToast(this,"编辑");
                break;
            case R.id.bt_delete:
                MessageUtils.showShortToast(this,"删除");
                break;
        }
    }
}