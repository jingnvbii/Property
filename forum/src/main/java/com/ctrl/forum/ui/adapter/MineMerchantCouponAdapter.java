package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.entity.CouponsPackag;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 现金包
 * Created by Administrator on 2016/4/22.
 */
public class MineMerchantCouponAdapter extends BaseAdapter{
    private Context context;
    private List<CouponsPackag> list;
    private View.OnClickListener onButton;
    public static EditText et_text;

    public void setOnButton(View.OnClickListener onButton) {this.onButton = onButton;}
    public MineMerchantCouponAdapter(Context context) {this.context = context;}

    public void setList(List<CouponsPackag> list) {
        this.list = list;
    }

    @Override
    public int getCount() {return list!=null?list.size():0;}

    @Override
    public Object getItem(int position) {return list.get(position);}

    @Override
    public long getItemId(int position) {return position;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_mine_coupon_give,parent,false);
            holder=new ViewHolder(convertView);
            holder.bt_ok.setOnClickListener(onButton);
            et_text = (EditText) convertView.findViewById(R.id.et_phone);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }

        if (list!=null && list.get(position)!=null){
            holder.tv_name.setText(list.get(position).getName());
        }

        holder.bt_ok.setTag(position);
        return convertView;
    }

    class ViewHolder{
        @InjectView(R.id.tv_name)
        TextView tv_name;
        @InjectView(R.id.bt_ok)
        Button bt_ok;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
