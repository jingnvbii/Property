package com.ctrl.forum.ui.activity.Invitation;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.entity.Invitation_listview;
import com.ctrl.forum.ui.adapter.InvitationCommentListViewAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 *
 *帖子评论 activity
 * Created by jason on 2016/4/12.
 */
public class InvitationCommentActivity extends AppToolBarActivity implements View.OnClickListener{
    @InjectView(R.id.lv_comment)//下拉列表
    PullToRefreshListView lv_comment;

    @InjectView(R.id.btn_yuyin)//语音按钮
    Button btn_yuyin;
    @InjectView(R.id.ll_input_text)//底部编辑框
    LinearLayout ll_input_text;
    @InjectView(R.id.iv_yuyin)//语音切换图标
    ImageView iv_yuyin;

    private InvitationCommentListViewAdapter commentAdapter;
    private List<Invitation_listview> kindList;

    private boolean isBtnVisible=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_comment);
        ButterKnife.inject(this);
        // 隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
        initData();

        commentAdapter=new InvitationCommentListViewAdapter(this);
        commentAdapter.setList(kindList);
        lv_comment.setAdapter(commentAdapter);
    }

    private void initData() {
        kindList= new ArrayList<Invitation_listview>();
        for (int i=0;i<10;i++){
            Invitation_listview lv=new Invitation_listview();
            lv.setName("我是路人"+i);
            kindList.add(lv);
        }
    }

    private void initView() {
        iv_yuyin.setOnClickListener(this);
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
        return "评论";
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_yuyin:
                if(isBtnVisible){
                    ll_input_text.setVisibility(View.GONE);
                    btn_yuyin.setVisibility(View.VISIBLE);
                    isBtnVisible=false;
                    iv_yuyin.setImageResource(R.mipmap.keyboard_input);
                }else {
                    ll_input_text.setVisibility(View.VISIBLE);
                    btn_yuyin.setVisibility(View.GONE);
                    isBtnVisible=true;
                    iv_yuyin.setImageResource(R.mipmap.yuyin);
                }


                break;
        }

    }
}
