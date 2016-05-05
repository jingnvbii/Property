package com.ctrl.forum.ui.activity.rim;

import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 评论页面
 */
public class RimStoreCommentActivity extends AppToolBarActivity implements View.OnClickListener{
    @InjectView(R.id.hsv_pic)
    HorizontalScrollView hsv_pic;
    @InjectView(R.id.lv_pic)
    LinearLayout lv_pic;
    @InjectView(R.id.iv_add)
    ImageView iv_add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rim_store_comment);
        ButterKnife.inject(this);
        initView();
    }

    private void initView() {
       //Ĭ�������,��ͼƬ,ͼƬ��ʾ�����ص�
        hsv_pic.setVisibility(View.INVISIBLE);
        lv_pic.setVisibility(View.INVISIBLE);

        iv_add.setOnClickListener(this);
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
    public String setupToolBarTitle() {return getResources().getString(R.string.store_comment);}

    @Override
    public void onClick(View v) {
       if (v==iv_add){
           lv_pic.setVisibility(View.VISIBLE);
       }
    }
}
