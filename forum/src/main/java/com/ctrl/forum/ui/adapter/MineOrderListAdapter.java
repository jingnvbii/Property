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
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.entity.MemeberOrder;
import com.ctrl.forum.ui.activity.mine.OrderPingjiaActivity;
import com.ctrl.forum.ui.activity.store.StorePaymentOrderActivity;
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
    private View.OnClickListener onCancle;
    private View.OnClickListener onGoods;
    private int type;

    public MineOrderListAdapter(Context context) {
        this.context = context;
        this.orders = new ArrayList<>();
    }

    public void setOrders(List<MemeberOrder> orders) {
        this.orders = orders;
        notifyDataSetChanged();
    }

    public void setOnGoods(View.OnClickListener onGoods) {
        this.onGoods = onGoods;
    }

    public void setOnCancle(View.OnClickListener onCancle) {
        this.onCancle = onCancle;
    }

    public void setOnDelete(View.OnClickListener onDelete) {
        this.onDelete = onDelete;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        String id = orders.get(position).getId();
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_mine_nopayment,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else {
            holder=(ViewHolder)convertView.getTag();
        }

        if (orders!=null && orders.get(position)!=null){
            type = Integer.valueOf(orders.get(position).getState());
            switch (type){ //1-订单被用户取消 2-订单被系统取消 3-未付款 4-商家未发货 5-用户未签收 6-用户已签收
                case 1: //1-订单被用户取消
                    holder.bt_left.setVisibility(View.GONE);
                    holder.bt_right.setText("已取消");
                    holder.bt_right.setBackgroundColor(context.getResources().getColor(R.color.gray_store));
                    holder.bt_right.setTextColor(context.getResources().getColor(R.color.text_black1));
                    holder.bt_right.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    holder.iv_delete.setOnClickListener(onDelete);
                    holder.iv_delete.setTag(id);
                    break;
                case 2: //2-订单被系统取消
                    holder.bt_left.setVisibility(View.GONE);
                    holder.bt_right.setText("已取消");
                    holder.bt_right.setBackgroundColor(context.getResources().getColor(R.color.gray_store));
                    holder.bt_right.setTextColor(context.getResources().getColor(R.color.text_black1));
                    holder.bt_right.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    holder.iv_delete.setOnClickListener(onDelete);
                    holder.iv_delete.setTag(id);
                    break;
                case 6://已签收
                    holder.bt_left.setVisibility(View.GONE);
                    holder.iv_delete.setOnClickListener(onDelete);
                    holder.iv_delete.setTag(id);
                    holder.bt_right.setBackgroundColor(context.getResources().getColor(R.color.red_bg));
                    holder.bt_right.setTextColor(context.getResources().getColor(R.color.white));
                    String state = orders.get(position).getEvaluationState();
                    if (state.equals("0")){
                        holder.bt_right.setText("去评价");
                        holder.bt_right.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //跳转至评价订单界面
                                Intent intent = new Intent(context, OrderPingjiaActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("id",orders.get(position).getId());
                                intent.putExtra("companyId",orders.get(position).getCompanyId());
                                context.startActivity(intent);
                            }
                        });
                    }else{
                        holder.bt_right.setText("已完成");
                        holder.bt_right.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                    }
                    convertView.setTag(holder);
                    break;
                case 3://未付款
                    holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MessageUtils.showShortToast(context,"订单未完成,不可删除");
                        }
                    });
                    holder.bt_right.setBackgroundColor(context.getResources().getColor(R.color.red_bg));
                    holder.bt_right.setTextColor(context.getResources().getColor(R.color.white));
                    holder.bt_left.setVisibility(View.VISIBLE);
                    holder.bt_left.setText("取消订单");
                    holder.bt_right.setText("去支付");
                    holder.bt_left.setOnClickListener(onCancle);
                    holder.bt_left.setTag(orders.get(position).getId());
                    holder.bt_right.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, StorePaymentOrderActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("orderId", orders.get(position).getId());
                            intent.putExtra("productsTotal", Double.valueOf(orders.get(position).getTotalCost()));
                            intent.putExtra("orderNum", orders.get(position).getOrderNum());
                            intent.putExtra("redenvelope", orders.get(position).getCouponMoney());
                            context.startActivity(intent);
                        }
                    });
                    break;
                case 4: //未发货
                    holder.bt_right.setBackgroundColor(context.getResources().getColor(R.color.red_bg));
                    holder.bt_right.setTextColor(context.getResources().getColor(R.color.white));
                    holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MessageUtils.showShortToast(context, "订单未完成,不可删除");
                        }
                    });
                    holder.bt_left.setVisibility(View.GONE);
                    holder.bt_right.setText("待发货");
                    holder.bt_right.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    break;
                case 5: //未签收
                    holder.bt_right.setBackgroundColor(context.getResources().getColor(R.color.red_bg));
                    holder.bt_right.setTextColor(context.getResources().getColor(R.color.white));
                    holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MessageUtils.showShortToast(context,"订单未完成,不可删除");
                        }
                    });
                    holder.bt_left.setVisibility(View.GONE);
                    holder.bt_right.setText("确认收货");
                    holder.bt_right.setOnClickListener(onGoods);
                    holder.bt_right.setTag(orders.get(position).getId());
                    break;
                default:
                    break;
            }

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
        @InjectView(R.id.bt_left)
        Button  bt_left;
        @InjectView(R.id.bt_right)
        Button  bt_right;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
