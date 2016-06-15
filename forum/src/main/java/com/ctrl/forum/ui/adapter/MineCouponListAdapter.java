package com.ctrl.forum.ui.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.entity.Redenvelope;
import com.ctrl.forum.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 优惠劵
 * Created by Administrator on 2016/5/3.
 */
public class MineCouponListAdapter extends BaseAdapter{
    private List<Redenvelope> redenvelopes;
    private Context context;
    private int resources;
    private String amount = "";

    public MineCouponListAdapter(Context context,int resources) {
        this.context = context;
        this.resources = resources;
        redenvelopes = new ArrayList<>();
    }

    public void setMessages(List<Redenvelope> redenvelopes) {
        this.redenvelopes.addAll(redenvelopes);
        notifyDataSetChanged();
    }

    public void setAmount(String amount) {
        this.amount = amount;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return redenvelopes!= null?redenvelopes.size():0;
    }

    @Override
    public Object getItem(int position) {return redenvelopes.get(position);}

    @Override
    public long getItemId(int position) {return position;}

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(resources,null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }

        if (redenvelopes!=null && redenvelopes.get(position)!=null){
            //为控件赋值
            holder.tv_money.setText(redenvelopes.get(position).getAmount());
            holder.tv_man.setText("全品类满"+redenvelopes.get(position).getLeastMoney()+"使用");
            holder.tv_time.setText(DateUtil.getStringByFormat(redenvelopes.get(position).getDeadlineTime(),"yyyy-mm-dd"));
            holder.tv_zhi.setText(redenvelopes.get(position).getUseRule());
        }

        if (!amount.equals("")) {
            float amounts = Integer.parseInt(amount);
            float leastMoney =Float.valueOf(redenvelopes.get(position).getLeastMoney());
            //float leastMoney = Integer.parseInt(redenvelopes.get(position).getLeastMoney());
            if (amounts>leastMoney | amounts==leastMoney) {
                holder.rl_bg.setBackground(context.getResources().getDrawable(R.mipmap.juan_hui));
                holder.tv_man.setTextColor(context.getResources().getColor(R.color.red_juan));
                holder.tv_money.setTextColor(context.getResources().getColor(R.color.red_juan));
                holder.tv_time.setTextColor(context.getResources().getColor(R.color.red_juan));
                holder.tv_zhi.setTextColor(context.getResources().getColor(R.color.red_juan));
                holder.tv_line.setBackgroundColor(context.getResources().getColor(R.color.red_juan));
            } else {
                holder.rl_bg.setBackground(context.getResources().getDrawable(R.mipmap.hui_use));
                holder.tv_man.setTextColor(context.getResources().getColor(R.color.hui_use));
                holder.tv_money.setTextColor(context.getResources().getColor(R.color.hui_use));
                holder.tv_time.setTextColor(context.getResources().getColor(R.color.hui_use));
                holder.tv_zhi.setTextColor(context.getResources().getColor(R.color.hui_use));
                holder.tv_line.setBackgroundColor(context.getResources().getColor(R.color.hui_use));
            }
        }

        return convertView;
    }

    class ViewHolder{
        @InjectView(R.id.tv_money)
        TextView tv_money;
        @InjectView(R.id.tv_man)
        TextView tv_man;
        @InjectView(R.id.tv_time)
        TextView tv_time;
        @InjectView(R.id.tv_zhi)
        TextView tv_zhi;
        @InjectView(R.id.rl_bg)
        RelativeLayout rl_bg;
        @InjectView(R.id.tv_line)
                TextView tv_line;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
