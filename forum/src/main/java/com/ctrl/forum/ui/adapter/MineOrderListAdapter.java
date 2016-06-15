package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.ctrl.forum.R;
import com.ctrl.forum.entity.MemeberOrder;
import com.ctrl.forum.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 我的订单
 * Created by Administrator on 2016/4/25.
 */
public class MineOrderListAdapter extends BaseAdapter{
    private List<MemeberOrder> orders;
    private Context context;
    private View.OnClickListener onDelete;
    private View.OnClickListener onPay;
    private View.OnClickListener onBuy;
    private View.OnClickListener onPingJia;
    private View.OnClickListener onCancle;

    public MineOrderListAdapter(Context context) {
        this.context = context;
        this.orders = new ArrayList<>();
    }

    public void setOrders(List<MemeberOrder> orders) {
        this.orders = orders;
        Log.e("setOrders===========","1234566");
        notifyDataSetChanged();
    }

    public void setOnCancle(View.OnClickListener onCancle) {
        this.onCancle = onCancle;
    }

    public void setOnBuy(View.OnClickListener onBuy) {
        this.onBuy = onBuy;
    }

    public void setOnDelete(View.OnClickListener onDelete) {
        this.onDelete = onDelete;
    }

    public void setOnPay(View.OnClickListener onPay) {
        this.onPay = onPay;
    }

    public void setOnPingJia(View.OnClickListener onPingJia) {
        this.onPingJia = onPingJia;
    }

    // 它的返回值是listview的item的种类的个数的和。
    @Override
    public int getViewTypeCount() {
        // TODO Auto-generated method stub
        return 6;
    }

    // 获取到具体的list的每一个成员的类型号
    @Override
    public int getItemViewType(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public int getCount() {
        return orders==null?0:orders.size();
    }

    @Override
    public Object getItem(int position) {
        return orders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.e("getView===========","1234566");
        ViewHolder holder = null;
        String id = orders.get(position).getId();
        if(convertView==null){
            String types =orders.get(position).getState();
            switch (types){
                case "6"://已签收
                    convertView= LayoutInflater.from(context).inflate(R.layout.item_mine_payment,parent,false);
                    holder=new ViewHolder(convertView);
                    holder.bt_left = (Button) convertView.findViewById(R.id.bt_left);
                    holder.bt_right = (Button) convertView.findViewById(R.id.bt_right);
                    holder.iv_delete.setOnClickListener(onDelete);
                    holder.iv_delete.setTag(id);
                    holder.bt_left.setOnClickListener(onPingJia);
                    holder.bt_left.setTag(id);
                    holder.bt_right.setOnClickListener(onBuy);
                    holder.bt_right.setTag(id);
                    holder.bt_left.setVisibility(View.VISIBLE);
                    holder.bt_right.setBackgroundColor(context.getResources().getColor(R.color.red_bg));
                    convertView.setTag(holder);
                    break;
                case "3"://未付款
                    convertView= LayoutInflater.from(context).inflate(R.layout.item_mine_nopayment,parent,false);
                    holder=new ViewHolder(convertView);
                    holder.payment = (Button) convertView.findViewById(R.id.payment);
                    holder.cancle = (Button) convertView.findViewById(R.id.cancle);
                    holder.cancle.setOnClickListener(onCancle);
                    holder.payment.setOnClickListener(onPay);
                    holder.iv_delete.setOnClickListener(onDelete);
                    holder.cancle.setTag(id);
                    holder.payment.setTag(id);
                    holder.iv_delete.setTag(id);
                    convertView.setTag(holder);
                    break;
                case "4": //未发货
                    convertView= LayoutInflater.from(context).inflate(R.layout.item_mine_payment,parent,false);
                    holder=new ViewHolder(convertView);
                    holder.bt_left = (Button) convertView.findViewById(R.id.bt_left);
                    holder.bt_right = (Button) convertView.findViewById(R.id.bt_right);
                    holder.iv_delete.setOnClickListener(onDelete);
                    holder.iv_delete.setTag(id);
                    holder.bt_left.setVisibility(View.GONE);
                    holder.bt_right.setBackgroundColor(context.getResources().getColor(R.color.gray));
                    holder.bt_right.setText("未发货");
                    convertView.setTag(holder);
                    break;
                case "5": //未签收
                    convertView= LayoutInflater.from(context).inflate(R.layout.item_mine_payment,parent,false);
                    holder=new ViewHolder(convertView);
                    holder.bt_left = (Button) convertView.findViewById(R.id.bt_left);
                    holder.bt_right = (Button) convertView.findViewById(R.id.bt_right);
                    holder.iv_delete.setOnClickListener(onDelete);
                    holder.iv_delete.setTag(id);
                    holder.bt_left.setVisibility(View.GONE);
                    holder.bt_right.setBackgroundColor(context.getResources().getColor(R.color.gray));
                    holder.bt_right.setText("未签收");
                    convertView.setTag(holder);
                    break;
                default:
                    convertView= LayoutInflater.from(context).inflate(R.layout.item_mine_payment,parent,false);
                    holder=new ViewHolder(convertView);
                    holder.bt_left = (Button) convertView.findViewById(R.id.bt_left);
                    holder.bt_right = (Button) convertView.findViewById(R.id.bt_right);
                    holder.iv_delete.setOnClickListener(onDelete);
                    holder.iv_delete.setTag(id);
                    holder.bt_left.setVisibility(View.GONE);
                    holder.bt_right.setBackgroundColor(context.getResources().getColor(R.color.gray));
                    holder.bt_right.setText("已取消");
                    convertView.setTag(holder);
                    break;
            }
        }else {
            holder=(ViewHolder)convertView.getTag();
        }

        if (orders!=null && orders.get(position)!=null){
            holder.ctrl.setText(orders.get(position).getCompanyname());
            holder.tv_total.setText(orders.get(position).getTotalCost());
            String time = DateUtil.getStringByFormat(orders.get(position).getCreateTime(),"yyyy-MM-dd  hh:mm:ss");
            holder.tv_time.setText(time);
            holder.tv_content.setText("本订单由"+orders.get(position).getCompanyname()+"提供");
            Arad.imageLoader.load(orders.get(position).getImg()).into(holder.iv_head);
        }
        return convertView;
    }

    class ViewHolder{
        Button payment; //付款
        Button cancle;
        Button bt_left;
        Button bt_right;

        @InjectView(R.id.ctrl)//文字
                TextView  ctrl;
        @InjectView(R.id.tv_total)//总价
                TextView  tv_total;
        @InjectView(R.id.tv_time)
                TextView  tv_time;
        @InjectView(R.id.tv_content)
                TextView  tv_content;
        @InjectView(R.id.iv_head)
                ImageView  iv_head;
        @InjectView(R.id.iv_delete)
        ImageView  iv_delete;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
