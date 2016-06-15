package com.ctrl.forum.ui.activity.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;

public class MineDetailActivity extends AppToolBarActivity implements View.OnClickListener{
    private RelativeLayout rl_post;
    private RelativeLayout rl_title;
    private RelativeLayout rl_comment;
    private RelativeLayout rl_fa_title;
    private TextView tv_message;
    private TextView tv_blacklist;
    private ImageView iv_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_detail);
        initView();
    }

    private void initView() {
        rl_post = (RelativeLayout) findViewById(R.id.rl_post);
        rl_title = (RelativeLayout) findViewById(R.id.rl_title);
        rl_comment = (RelativeLayout) findViewById(R.id.rl_comment);
        rl_fa_title = (RelativeLayout) findViewById(R.id.rl_fa_title);
        tv_message = (TextView) findViewById(R.id.tv_message);
        tv_blacklist = (TextView) findViewById(R.id.tv_blacklist);
        iv_phone = (ImageView) findViewById(R.id.iv_phone);

        rl_post.setOnClickListener(this);
        rl_title.setOnClickListener(this);
        rl_comment.setOnClickListener(this);
        rl_fa_title.setOnClickListener(this);
        tv_message.setOnClickListener(this);
        tv_blacklist.setOnClickListener(this);
        iv_phone.setOnClickListener(this);
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
    public String setupToolBarTitle() {return getResources().getString(R.string.detail);}


    @Override
    public void onClick(View v) {

    }
}
