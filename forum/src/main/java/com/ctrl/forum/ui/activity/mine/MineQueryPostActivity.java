package com.ctrl.forum.ui.activity.mine;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.beanu.arad.Arad;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.dao.InvitationDao;
import com.ctrl.forum.entity.Post;
import com.ctrl.forum.ui.adapter.MineQueryPostAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 个人中心---我的发帖
 */
public class MineQueryPostActivity extends AppToolBarActivity {
    @InjectView(R.id.lv_content)
    PullToRefreshListView lv_content;

    private List<Post> posts;
    private int PAGE_NUM =1;
    private MineQueryPostAdapter mineQueryPostAdapter;
    private InvitationDao invitationDao;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_query_post);
        ButterKnife.inject(this);

        invitationDao = new InvitationDao(this);
        invitationDao.queryMyPostList(Arad.preferences.getString("memberId"),PAGE_NUM+"",Constant.PAGE_SIZE+"");
        mineQueryPostAdapter = new MineQueryPostAdapter(this);
        lv_content.setAdapter(mineQueryPostAdapter);
        activity = this;

        lv_content.setMode(PullToRefreshBase.Mode.BOTH);
        lv_content.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (posts != null) {
                    posts.clear();
                    PAGE_NUM = 1;
                    mineQueryPostAdapter = new MineQueryPostAdapter(activity);
                    lv_content.setAdapter(mineQueryPostAdapter);
                }
                invitationDao.queryMyPostList(Arad.preferences.getString("memberId"), PAGE_NUM + "", Constant.PAGE_SIZE + "");

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (posts != null) {
                    PAGE_NUM += 1;
                    invitationDao.queryMyPostList(Arad.preferences.getString("memberId"), PAGE_NUM + "", Constant.PAGE_SIZE + "");
                } else {
                    lv_content.onRefreshComplete();
                }
            }
        });
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
    public String setupToolBarTitle() {return getResources().getString(R.string.my_post);}

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        lv_content.onRefreshComplete();
        if (requestCode==18){
            posts = invitationDao.getMinePost();
            if (posts!=null){
                mineQueryPostAdapter.setList(posts);
            }
        }
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        lv_content.onRefreshComplete();
    }

}
