package com.ctrl.forum.ui.activity.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.entity.Blacklist;
import com.ctrl.forum.ui.adapter.MineBlacklistAdapter;

import java.util.ArrayList;
import java.util.List;

public class MineBlacklistActivity extends AppToolBarActivity implements View.OnClickListener{
    private List<Blacklist> blacklists;
    private MineBlacklistAdapter blacklistAdapter;
    private ListView lv_blacklist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_blacklist);
        lv_blacklist = (ListView) findViewById(R.id.lv_blacklist);

        initData();
        blacklistAdapter = new MineBlacklistAdapter(blacklists,this);
        lv_blacklist.setAdapter(blacklistAdapter);
        blacklistAdapter.setOnButton(this);
    }

    private void initData() {
        blacklists = new ArrayList<>();
        for (int i = 0;i<5;i++){
            Blacklist blacklist = new Blacklist();
            blacklist.setName(getResources().getString(R.string.comment_name));
            blacklists.add(blacklist);
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
