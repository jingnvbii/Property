package com.ctrl.forum.ui.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.beanu.arad.Arad;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.dao.ReplyCommentDao;
import com.ctrl.forum.entity.ObtainMyReply;
import com.ctrl.forum.ui.activity.Invitation.InvitationDetailFromPlatformActivity;
import com.ctrl.forum.ui.activity.Invitation.InvitationPinterestDetailActivity;
import com.ctrl.forum.ui.adapter.MineCommentListAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

/**
 * 我的评论--个人中心
 */
public class MineCommentActivity extends AppToolBarActivity {
    private List<ObtainMyReply> comments;
    private MineCommentListAdapter mineCommentListAdapter;
    private PullToRefreshListView lv_comment;
    private int PAGE_NUM=1;
    private ReplyCommentDao replyCommentDao;
    private String id;
    static String title;
    private String memberId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_comment);
        lv_comment = (PullToRefreshListView) findViewById(R.id.lv_comment);

        lv_comment.setMode(PullToRefreshBase.Mode.BOTH);
        mineCommentListAdapter = new MineCommentListAdapter(this);
        lv_comment.setAdapter(mineCommentListAdapter);

        initData();
        lv_comment.setMode(PullToRefreshBase.Mode.BOTH);
        lv_comment.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (comments != null) {
                    comments.clear();
                    PAGE_NUM = 1;
                }
                replyCommentDao.obtainMyReply(memberId, PAGE_NUM, Constant.PAGE_SIZE);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (comments != null) {
                    PAGE_NUM += 1;
                    replyCommentDao.obtainMyReply(memberId, PAGE_NUM, Constant.PAGE_SIZE);
                } else {
                    lv_comment.onRefreshComplete();
                }
            }
        });
        lv_comment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (comments.get(position-1).getSourceType()!=null){
                    String type = comments.get(position-1).getSourceType();
                    if (type.equals("0")){
                        Intent intent = new Intent(getApplicationContext(), InvitationDetailFromPlatformActivity.class);
                        intent.putExtra("id", comments.get(position-1).getPostId());
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(getApplicationContext(), InvitationPinterestDetailActivity.class);
                        intent.putExtra("id", comments.get(position-1).getPostId());
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private void initData() {
        replyCommentDao = new ReplyCommentDao(this);
        if (getIntent().getStringExtra("id")==null || getIntent().getStringExtra("id").equals("")){
            memberId = Arad.preferences.getString("memberId");
            title = getResources().getString(R.string.my_comment);
        }else{
            memberId = getIntent().getStringExtra("id");
            title = "他的评论";
        }
        replyCommentDao.obtainMyReply(memberId, PAGE_NUM, Constant.PAGE_SIZE);
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
    public String setupToolBarTitle() {return title;}

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        lv_comment.onRefreshComplete();
        if (requestCode==3){
            comments = replyCommentDao.getObtainMyReplies();
            if (comments!=null){
                mineCommentListAdapter.setObtainMyReplies(comments);
            }
        }
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        lv_comment.onRefreshComplete();
    }
}
