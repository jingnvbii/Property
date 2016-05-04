package com.ctrl.android.yinfeng.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ctrl.android.yinfeng.R;
import com.ctrl.android.yinfeng.entity.Notice;
import com.ctrl.android.yinfeng.utils.TimeUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 公告列表 adapter
 * Created by Eric on 2015/10/13.
 */
public class NoticeListAdapter extends BaseAdapter{

    private Activity mActivity;
    private List<Notice> list;
    private Notice notice;

    public NoticeListAdapter(Activity mActivity){
        this.mActivity = mActivity;
    }

    public void setList(List<Notice> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if(convertView == null){
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.j_notice_list_item,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

       // final Map<String,String> map = list.get(position);
        notice=list.get(position);
        holder.notice_title.setText(notice.getNoticeTitle());
        holder.notice_time.setText(TimeUtil.date(Long.parseLong(notice.getCreateTime())));
        if(notice.getStatus().equals("1")){
            holder.notice_status_flg.setVisibility(View.GONE);
           // holder.notice_status_flg.setBackgroundResource(R.drawable.notice_nosign);
        } else if(notice.getStatus().equals("0")){
           /* Drawable drawable = mActivity.getResources().getDrawable(R.drawable.notice_point);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
            holder.notice_status_flg.setCompoundDrawables(drawable, null, null, null);//画在左边
            holder.notice_status_flg.setText("未签收");
            holder.notice_status_flg.setBackgroundResource(R.drawable.notice_sign);*/
        }

        if(notice.getIsTop().equals("0")){
             holder.iv_ding.setVisibility(View.VISIBLE);
        }else {
            holder.iv_ding.setVisibility(View.GONE);

        }

        return convertView;
    }


    static class ViewHolder {
        @InjectView(R.id.notice_title)//公告标题
        TextView notice_title;
        @InjectView(R.id.notice_time)//公告发布时间
        TextView notice_time;
        @InjectView(R.id.notice_status_flg)//公告签收情况
        TextView notice_status_flg;
        @InjectView(R.id.iv_ding)//顶
        ImageView   iv_ding;


        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }




}
