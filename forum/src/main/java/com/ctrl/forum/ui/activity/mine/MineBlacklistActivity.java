package com.ctrl.forum.ui.activity.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.dao.EditDao;
import com.ctrl.forum.entity.Blacklist;
import com.ctrl.forum.ui.adapter.MineBlacklistAdapter;

import java.util.ArrayList;
import java.util.List;

public class MineBlacklistActivity extends AppToolBarActivity implements View.OnClickListener{
    private List<Blacklist> blacklists = new ArrayList<>();
    private EditDao edao;
    private MineBlacklistAdapter blacklistAdapter;
    private ListView lv_blacklist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_blacklist);
        lv_blacklist = (ListView) findViewById(R.id.lv_blacklist);

        initData();
        blacklistAdapter = new MineBlacklistAdapter(this);
        lv_blacklist.setAdapter(blacklistAdapter);
        blacklistAdapter.setOnButton(this);
    }

    private void initData() {
        edao = new EditDao(this);
        edao.getBlackList(Arad.preferences.getString("memberId"), Constant.PAGE_NUM,Constant.PAGE_SIZE);
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if (requestCode==2){
            MessageUtils.showShortToast(this,"获取成功");
            blacklists = edao.getBlacklists();
            blacklistAdapter.setBlacklists(blacklists);
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
    public String setupToolBarTitle() {return getResources().getString(R.string.my_blacklist);}

    @Override
    public void onClick(View v) {
        Object tag = v.getTag();
        switch (v.getId()){
            case R.id.bt_clear:
                int position = (int) tag;
                finish();
                break;
        }
    }
}
