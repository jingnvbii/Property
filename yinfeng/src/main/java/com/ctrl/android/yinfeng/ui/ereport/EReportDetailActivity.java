package com.ctrl.android.yinfeng.ui.ereport;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.ctrl.android.yinfeng.R;
import com.ctrl.android.yinfeng.base.AppToolBarActivity;
import com.ctrl.android.yinfeng.dao.ReportDao;
import com.ctrl.android.yinfeng.utils.TimeUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
/*
* 事件上报详情 activity
* */

public class EReportDetailActivity extends AppToolBarActivity implements View.OnClickListener{

    @InjectView(R.id.tv_ereport_title)//标题
    TextView tv_ereport_title;
    @InjectView(R.id.tv_ereport_time)//时间
    TextView tv_ereport_time;
    @InjectView(R.id.tv_ereport_content)//内容
    TextView tv_ereport_content;
    @InjectView(R.id.iv_ereport1)//图片1
    ImageView iv_ereport1;
    @InjectView(R.id.iv_ereport2)//图片2
    ImageView iv_ereport2;
    @InjectView(R.id.iv_ereport3)//图片3
    ImageView iv_ereport3;
    @InjectView(R.id.iv_ereport4)//图片4
    ImageView iv_ereport4;
    private ReportDao rdao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ereport_detail);
        ButterKnife.inject(this);
        init();
    }

    private void init() {
        rdao=new ReportDao(this);
        rdao.requestReportDetail(getIntent().getStringExtra("eventReportId"));
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);

        if(1==requestCode){
            tv_ereport_title.setText(rdao.getReport().getTitle());
            tv_ereport_time.setText(TimeUtil.date(Long.parseLong(rdao.getReport().getCreateTime())));
            tv_ereport_content.setText(rdao.getReport().getContent());

           // Log.d("demo","size: " + rdao.getReportPicList().size());

            if(rdao.getReportPicList()!=null) {
               // Log.d("url",rdao.getReportPicList().get(0).getZipImg());
                if(rdao.getReportPicList().size()>=1) {
                    Arad.imageLoader.load(rdao.getReportPicList().get(0).getZipImg().equals("") ||
                                    rdao.getReportPicList().get(0).getZipImg() == null ? "aa" :
                                    rdao.getReportPicList().get(0).getZipImg()
                    ).placeholder(R.mipmap.default_image).into(iv_ereport1);
                }
                if(rdao.getReportPicList().size()>=2) {
                    iv_ereport2.setVisibility(View.VISIBLE);
                    Arad.imageLoader.load(rdao.getReportPicList().get(1).getZipImg().equals("") ||
                                    rdao.getReportPicList().get(1).getZipImg() == null ? "aa" :
                                    rdao.getReportPicList().get(1).getZipImg()
                    ).placeholder(R.mipmap.default_image).into(iv_ereport2);
                }
                if(rdao.getReportPicList().size()>=3) {
                    iv_ereport3.setVisibility(View.VISIBLE);
                    Arad.imageLoader.load(rdao.getReportPicList().get(2).getZipImg().equals("") ||
                                    rdao.getReportPicList().get(2).getZipImg() == null ? "aa" :
                                    rdao.getReportPicList().get(2).getZipImg()
                    ).placeholder(R.mipmap.default_image).into(iv_ereport3);
                }
                if(rdao.getReportPicList().size()>=4) {
                    iv_ereport4.setVisibility(View.VISIBLE);
                    Arad.imageLoader.load(rdao.getReportPicList().get(3).getZipImg().equals("") ||
                                    rdao.getReportPicList().get(3).getZipImg() == null ? "aa" :
                                    rdao.getReportPicList().get(3).getZipImg()
                    ).placeholder(R.mipmap.default_image).into(iv_ereport4);
                }
                if(rdao.getReportPicList().size()==0) {
                    Arad.imageLoader.load(R.mipmap.default_image
                    ).placeholder(R.mipmap.default_image).into(iv_ereport1);
                }


            }
        }
    }

    @Override
    public String setupToolBarTitle() {
        return "事件上报";
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
    public void onClick(View v) {
       /* if(iv_ereport==v) {
            Intent intent = new Intent(EReportDetailActivity.this, ImageZoomActivity.class);
            List<Img> listObj = rdao.getReportPicList();
            ArrayList<String> listStr = new ArrayList<>();
            if (listObj!=null) {
                for (int i = 0; i < listObj.size(); i++) {
                    listStr.add(listObj.get(i).getOriginalImg());
                }
                intent.putExtra("imageList", (Serializable) listStr);
                startActivity(intent);
            }
        }*/
    }
}
