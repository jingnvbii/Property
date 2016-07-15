package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.ctrl.forum.R;
import com.ctrl.forum.entity.Assess;
import com.ctrl.forum.ui.activity.mine.OrderPingjiaActivity;
import com.ctrl.forum.utils.DateUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 待评价订单
 * Created by Administrator on 2016/5/3.
 */
public class MineAssessWeiAdapter extends BaseAdapter {
    private List<Assess> messages;
    private Context context;

    public MineAssessWeiAdapter(Context context) {this.context = context;}

    public void setMessages(List<Assess> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {return messages!=null?messages.size():0;}

    @Override
    public Object getItem(int position) {return messages.get(position);}

    @Override
    public long getItemId(int position) {return position;}

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_mine_assess_wei,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }

        if (messages!=null){
            holder.tv_name.setText(messages.get(position).getCompanyname());
            holder.tv_total.setText(messages.get(position).getTotalCost()+"元");
            holder.tv_data.setText(DateUtil.getStringByFormat(messages.get(position).getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
            Arad.imageLoader.load(messages.get(position).getImg()).
                    placeholder(context.getResources().getDrawable(R.mipmap.image_default)).into(holder.iv_head);

            holder.assess_go.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, OrderPingjiaActivity.class);
                    intent.putExtra("id",messages.get(position).getId());
                    //intent.putExtra("companyId",companyId); //商家id
                    context.startActivity(intent);
                }
            });
        }
        return convertView;
    }

    class ViewHolder{
        @InjectView(R.id.iv_head)
        ImageView iv_head;
        @InjectView(R.id.tv_name)
        TextView tv_name;
        @InjectView(R.id.tv_total)
        TextView tv_total;
        @InjectView(R.id.tv_data)
        TextView tv_data;
        @InjectView(R.id.assess_go)
        Button assess_go;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
