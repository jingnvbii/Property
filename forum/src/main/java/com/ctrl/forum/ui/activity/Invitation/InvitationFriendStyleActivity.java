package com.ctrl.forum.ui.activity.Invitation;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.entity.Invitation_listview;
import com.ctrl.forum.ui.adapter.InvitationFriendStyleListViewAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 *
 *帖子列表  朋友圈样式 activity
 * Created by jason on 2016/4/12.
 */
public class InvitationFriendStyleActivity extends AppToolBarActivity{
    @InjectView(R.id.lv_frind)//下拉列表
    PullToRefreshListView lv_friend;
    private ArrayList<Invitation_listview> kindList;
    private InvitationFriendStyleListViewAdapter friendAdapter;
    private ListView lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_friend_style);
        ButterKnife.inject(this);
        // 隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        initView();
        initData();
        friendAdapter=new InvitationFriendStyleListViewAdapter(this);
        friendAdapter.setList(kindList);
        lv=lv_friend.getRefreshableView();
        lv.setDivider(new ColorDrawable(Color.alpha(0)));
        lv.setDividerHeight(30);
        lv_friend.setAdapter(friendAdapter);




    }

    private void initView() {



    }

    private void initData() {
        kindList= new ArrayList<Invitation_listview>();
        for (int i=0;i<10;i++){
            Invitation_listview lv=new Invitation_listview();
            lv.setName("我是路人"+i);
            kindList.add(lv);
        }
    }

    @Override
    public boolean setupToolBarLeftButton(ImageView leftButton) {
        leftButton.setImageResource(R.mipmap.jiantou_left);
        leftButton.setOnClickListener(new View.OnClickListener() {
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
        return "帖子列表";
    }

    @Override
    public boolean setupToolBarRightButton(ImageView rightButton) {
        rightButton.setImageResource(R.mipmap.edit);
        return true;
    }
}
