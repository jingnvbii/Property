package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.ctrl.forum.R;
import com.ctrl.forum.entity.CompanyCollect;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 我的收藏-店铺收藏 adapter
 * Created by jason on 2016/4/8.
 */
public class CompanyCollectFragmentAdapter extends BaseAdapter{
    private Context mcontext;
    private List<CompanyCollect> list;

    public CompanyCollectFragmentAdapter(Context context) {
               this.mcontext=context;
                list = new ArrayList<>();
    }

    public void setList(List<CompanyCollect> list) {
        this.list .addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(mcontext).inflate(R.layout.item_listview_store_fragment_jason,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }

        if (list!=null && list.get(position)!=null){
            CompanyCollect company=list.get(position);
            holder.tv_name.setText(company.getName());
            holder.tv_distance.setText(company.getDis());
            Arad.imageLoader.load(company.getImg()).into(holder.iv_title_photo);
            holder.ratingBar.setRating(Integer.parseInt(company.getEvaluatLevel()));
            holder.ratingBar.setFocusable(false);
            holder.tv_youhuiquan_name.setText(company.getCashName());

            if (company.getCouponEnable().equals("0")){
                holder.tv_xianjinquan_name.setText("不可用");
            }else{
                holder.tv_xianjinquan_name.setText("可用");
            }

            if (company.getPacketEnable().equals("0")){
                holder.tv_youhuiquan_name.setText("不可用");
            }

            String state = company.getState();
            if (!state.equals("")){
                switch (state){
                    case "1":
                        holder.tv_yingyezhong.setText("营业中");
                        holder.tv_yingyezhong.setBackgroundResource(R.mipmap.tv_blue_bg);
                        break;
                    case "0":
                        holder.tv_yingyezhong.setText("休息中");
                        holder.tv_yingyezhong.setBackgroundResource(R.drawable.tv_gray_bg);
                        break;
                }
            }
        }

        return convertView;
    }

    static class ViewHolder{
        @InjectView(R.id.iv_title_photo)
        ImageView iv_title_photo;
        @InjectView(R.id.tv_name)
        TextView  tv_name;
        @InjectView(R.id.ratingBar)
        RatingBar ratingBar;  //评分条
        @InjectView(R.id.tv_yingyezhong)
        TextView  tv_yingyezhong;  //是否营业中
        /*@InjectView(R.id.tv_xianjinquan)
        TextView  tv_xianjinquan; //现金劵
        @InjectView(R.id.tv_xianjinquan_content)
        TextView  tv_xianjinquan_content; //现金劵的金额
        @InjectView(R.id.tv_youhuiquan)
        TextView  tv_youhuiquan;  //优惠劵*/
        @InjectView(R.id.tv_xianjinquan_name)
        TextView  tv_xianjinquan_name;  //现金劵的内容
        @InjectView(R.id.tv_youhuiquan_name)
        TextView  tv_youhuiquan_name;  //优惠劵的内容
        @InjectView(R.id.tv_distance)
        TextView  tv_distance;   //距当前位置的距离

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
