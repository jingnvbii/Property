package com.ctrl.forum.ui.activity.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 我的收藏
 */
public class MineCollectActivity extends AppToolBarActivity {
    @InjectView(R.id.text)//�ı�
            LinearLayout text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_collect);
        ButterKnife.inject(this);

        ((TextView)text.getChildAt(0)).setTextColor(getResources().getColor(R.color.red_bg));
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
    public String setupToolBarTitle() {return getResources().getString(R.string.my_collect);}

    public void onClick(View view){
        for (int i=0;i<3;i++){
            ((TextView)text.getChildAt(i)).setTextColor(getResources().getColor(R.color.rim));
        }
        ((TextView)view).setTextColor(getResources().getColor(R.color.red_bg));
    }
}
