package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.entity.CashCoupons;
import com.ctrl.forum.utils.TimeUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 店铺详情 现金券  adapter
 * Created by jason on 2016/4/8.
 */
public class StoreXianjinQuanListViewAdapter extends BaseAdapter{
    private Context mcontext;
    private List<CashCoupons>kindList;

    public StoreXianjinQuanListViewAdapter(Context context) {
               this.mcontext=context;
    }

    public void setList(List<CashCoupons> list) {
        this.kindList = list;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return kindList==null?0:kindList.size();
    }

    @Override
    public Object getItem(int position) {
        return kindList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(mcontext).inflate(R.layout.item_store_xian_jin_quan,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }
        CashCoupons mCashCoupons=kindList.get(position);
        holder.tv_name.setText(mCashCoupons.getName());
        holder.tv_xianjinquan_money.setText("￥"+mCashCoupons.getAmount()+"元");
        holder.tv_xianjinquan_remark.setText(mCashCoupons.getRemark());
        holder.tv_use_time.setText("使用期限:"+ TimeUtils.formatPhotoDate(Long.parseLong(mCashCoupons.getValidityStartTime()))+" —— "+TimeUtils.formatPhotoDate(Long.parseLong(mCashCoupons.getValidityEndTime())));
        return convertView;
    }

    static class ViewHolder{
        @InjectView(R.id.tv_name)//标题
                TextView tv_name;
        @InjectView(R.id.tv_use_time)//标题
                TextView tv_use_time;
        @InjectView(R.id.tv_xianjinquan_remark)//现金券说明
                TextView tv_xianjinquan_remark;
        @InjectView(R.id.tv_xianjinquan_money)//现金券金额
                TextView tv_xianjinquan_money;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
