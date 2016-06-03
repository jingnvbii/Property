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
import com.ctrl.forum.entity.Mall;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 商城首页列表 adapter
 * Created by jason on 2016/4/8.
 */
public class StoreFragmentAdapter extends BaseAdapter{
    private Context mcontext;
    private List<Mall> list;

    public StoreFragmentAdapter(Context context) {
               this.mcontext=context;
    }

    public void setList(List<Mall> list) {
        this.list = list;
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
       Mall mall=list.get(position);
        holder.tv_name.setText(mall.getName());
        holder.tv_distance.setText((Double.parseDouble(mall.getDis())) / 1000.0 + " 千米");
        holder.ratingBar.setNumStars(Integer.parseInt(mall.getEvaluatLevel()));
        holder.tv_xianjinquan_name.setText(mall.getCashName());
        if(mall.getState().equals("0")){
            holder.tv_yingyezhong.setText("休息中");
            holder.tv_yingyezhong.setBackgroundResource(R.color.text_gray);
        }
        if(mall.getState().equals("1")){
            holder.tv_yingyezhong.setText("营业中");
            holder.tv_yingyezhong.setBackgroundResource(R.mipmap.tv_blue_bg);
        }
        Arad.imageLoader.load(mall.getImg()).placeholder(R.mipmap.default_error).into(holder.iv_title_photo);
        return convertView;
    }

    static class ViewHolder{
       /* @InjectView(R.id.iv_grid_item)//图片
                ImageView iv_grid_item;*/
        @InjectView(R.id.tv_name)//文字
                TextView  tv_name;
        @InjectView(R.id.iv_title_photo)//文字
                ImageView iv_title_photo;
        @InjectView(R.id.ratingBar)//文字
                RatingBar ratingBar;
        @InjectView(R.id.tv_xianjinquan_name)//现金券名称
                TextView  tv_xianjinquan_name;
        @InjectView(R.id.tv_youhuiquan_name)//优惠券名称
                TextView  tv_youhuiquan_name;
        @InjectView(R.id.tv_distance)//优惠券名称
                TextView  tv_distance;
        @InjectView(R.id.tv_yingyezhong)//优惠券名称
                TextView  tv_yingyezhong;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
