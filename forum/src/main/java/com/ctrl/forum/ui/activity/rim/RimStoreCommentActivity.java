package com.ctrl.forum.ui.activity.rim;

import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.base.RimCommentListItemType;
import com.ctrl.forum.entity.CommentOne;
import com.ctrl.forum.entity.CommentReplay;
import com.ctrl.forum.entity.CommentShop;
import com.ctrl.forum.entity.CommentThree;
import com.ctrl.forum.ui.adapter.RimCommentListAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 评论页面
 */
public class RimStoreCommentActivity extends AppToolBarActivity implements View.OnClickListener{
    @InjectView(R.id.hsv_pic)  //显示图片
    HorizontalScrollView hsv_pic;
    @InjectView(R.id.lv_pic)   //表情.照片.拍摄
    LinearLayout lv_pic;
    @InjectView(R.id.iv_add)
    ImageView iv_add;       //加图片
    @InjectView(R.id.lv_content)
    ListView lv_content;
    @InjectView(R.id.tv_huitie)
    TextView tv_huitie;

    private List<RimCommentListItemType> list = null;
    private RimCommentListAdapter rimCommentListAdapter;
    private Random random = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rim_store_comment);
        ButterKnife.inject(this);
        initView();
        initData();
        rimCommentListAdapter = new RimCommentListAdapter(this);
        rimCommentListAdapter.setList(list);
        lv_content.setAdapter(rimCommentListAdapter);
    }

    private void initData() {
            random = new Random();
            list=new ArrayList<RimCommentListItemType>();
            for (int i = 0; i < 2; i++) {
                int type = random.nextInt(3);
                switch (type) {
                    case 0: //三张图片
                        CommentThree commentThree = new CommentThree();
                        commentThree.setName(i+"");
                        list.add(commentThree);
                        break;
                    case 1://一张图片
                        CommentOne commentOne = new CommentOne();
                        commentOne.setName(i+"");
                        list.add(commentOne);
                        break;
                    case 2://回复某人
                        CommentReplay commentReplay = new CommentReplay();
                        commentReplay.setName(i+"");
                        list.add(commentReplay);
                        break;
                    case 3://直接回复商铺
                        CommentShop commentShop = new CommentShop();
                        commentShop.setName(i+"");
                        list.add(commentShop);
                        break;
                    default:
                        break;
                }
            }
    }

    private void initView() {

        hsv_pic.setVisibility(View.GONE);
        lv_pic.setVisibility(View.GONE);

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
        if (v==tv_huitie){
            rimCommentListAdapter.setList(list);
        }
    }
}
