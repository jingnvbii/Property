package com.ctrl.forum.ui.activity.mine;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.entity.Comment;
import com.ctrl.forum.ui.adapter.MineCommentListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的评论
 * 退出时,点两次才会退出
 */
public class MineCommentActivity extends AppToolBarActivity {
    private List<Comment> comments;
    private MineCommentListAdapter mineCommentListAdapter;
    private ListView lv_comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_comment);
        lv_comment = (ListView) findViewById(R.id.lv_comment);
        initData();
        mineCommentListAdapter = new MineCommentListAdapter(comments,this);
        lv_comment.setAdapter(mineCommentListAdapter);
        lv_comment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                finish();
            }
        });
    }

    private void initData() {
        comments = new ArrayList<>();
        for (int i=0;i<6;i++){
            Comment comment = new Comment();
            comment.setName(getResources().getString(R.string.comment_name));
            comment.setComment(getResources().getString(R.string.comment_content));
            comment.setTime(getResources().getString(R.string.comment_time));
            comment.setYear(getResources().getString(R.string.comment_year));
            comments.add(comment);
        }
    }

    @Override
    public boolean setupToolBarLeftButton(ImageView leftButton) {
        leftButton.setImageResource(R.mipmap.white_arrow_left_none);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                Log.e("finish", "kokoko");
            }
        });
        return true;
    }

    @Override
    public String setupToolBarTitle() {return getResources().getString(R.string.my_comment);}



}
