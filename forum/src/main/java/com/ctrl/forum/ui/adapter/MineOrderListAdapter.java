package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.ctrl.forum.R;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.entity.MemeberOrder;
import com.ctrl.forum.utils.DateUtil;

import java.util.List;

/**
 * 我的订单
 * Created by Administrator on 2016/4/25.
 */
public class MineOrderListAdapter extends BaseAdapter{
    private List<MemeberOrder> orders;
    private Context context;
    private List<Integer> types;
    private View.OnClickListener onDelete;
    private View.OnClickListener onPay;
    private View.OnClickListener onBuy;
    private View.OnClickListener onPingJia;
    private View.OnClickListener onCancle;

    public MineOrderListAdapter(Context context) {
        this.context = context;
    }

    public void setOrders(List<MemeberOrder> orders) {
        this.orders = orders;
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
        return Constant.Order_TYPE_ITEM;
    }

    // 获取到具体的list的每一个成员的类型号
    @Override
    public int getItemViewType(int position) {
        // TODO Auto-generated method stub
        return types.get(position);
    }

    public void setTypes(List<Integer> types) {
        this.types = types;
        notifyDataSetChanged();
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
        ViewHolder holder = null;
        String id = orders.get(position).getId();
        if(convertView==null){
            switch (types.get(position)){
                case 0://已付款
                    convertView= LayoutInflater.from(context).inflate(R.layout.item_mine_payment,parent,false);
                    holder=new ViewHolder();
                    holder.ctrl = (TextView) convertView.findViewById(R.id.ctrl);
                    holder.tv_total = (TextView) convertView.findViewById(R.id.tv_total);
                    holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
                    holder.time = (TextView) convertView.findViewById(R.id.textView24);
                    holder.iv_head = (ImageView) convertView.findViewById(R.id.iv_head);
                    holder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
                    holder.buy_again = (Button) convertView.findViewById(R.id.buy_again);
                    holder.button2 = (Button) convertView.findViewById(R.id.button2);
                    holder.iv_delete.setOnClickListener(onDelete);
                    holder.buy_again.setOnClickListener(onBuy);
                    holder.button2.setOnClickListener(onPingJia);
                    holder.iv_delete.setTag(id);
                    holder.buy_again.setTag(id);
                    holder.button2.setTag(id);
                    convertView.setTag(holder);
                    break;
                case 3://未付款
                    convertView= LayoutInflater.from(context).inflate(R.layout.item_mine_nopayment,parent,false);
                    holder=new ViewHolder();
                    holder.ctrl = (TextView) convertView.findViewById(R.id.ctrl);
                    holder.tv_total = (TextView) convertView.findViewById(R.id.tv_total);
                    holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
                    holder.time = (TextView) convertView.findViewById(R.id.textView24);
                    holder.iv_head = (ImageView) convertView.findViewById(R.id.iv_head);
                    holder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
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
            }
        }else {
            holder=(ViewHolder)convertView.getTag();
        }

        if (orders!=null && orders.get(position)!=null){
            holder.ctrl.setText(orders.get(position).getCompanyname());
            holder.tv_total.setText(orders.get(position).getTotalCost());
            String time = DateUtil.getStringByFormat(orders.get(position).getCreateTime(),"yyyy-MM-dd  hh:mm:ss");
            holder.time.setText(time);
            holder.tv_content.setText("本订单由"+orders.get(position).getCompanyname()+"提供");
            Arad.imageLoader.load(orders.get(position).getImg()).into(holder.iv_head);
        }
        return convertView;
    }

    class ViewHolder{
        TextView ctrl;
        TextView tv_total; //总价
        TextView time;//时间
        TextView tv_content;//由谁提供
        ImageView iv_head;
        Button payment; //付款
        ImageView iv_delete;
        Button buy_again;
        Button button2;
        Button cancle;
    }
}
