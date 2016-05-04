package com.ctrl.android.property.eric.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ctrl.android.property.R;
import com.ctrl.android.property.eric.ui.pay.HousePayHistoryActivity;
import com.ctrl.android.property.eric.util.N;
import com.ctrl.android.property.eric.util.S;

import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 缴费记录的 adapter
 * Created by Eric on 2015/10/20
 */
public class HousePayHistoryAdapter extends BaseAdapter{

    private HousePayHistoryActivity mActivity;
    private List<Map<String,String>> listMap;

    public HousePayHistoryAdapter(HousePayHistoryActivity mActivity){
        this.mActivity = mActivity;
    }

    public void setList(List<Map<String,String>> listMap) {
        this.listMap = listMap;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return listMap == null ? 0 : listMap.size();
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
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.house_pay_history_item,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        final Map<String,String> map = listMap.get(position);

        holder.house_pay_history_time.setText(S.getStr(map.get("time")));
        holder.house_pay_history_item.setText(S.getStr(map.get("name")));
        holder.house_pay_history_amount.setText(N.toPriceFormate(S.getStr(map.get("amount"))));

        return convertView;
    }

    static class ViewHolder {

        @InjectView(R.id.house_pay_history_time)//缴费时间
        TextView house_pay_history_time;
        @InjectView(R.id.house_pay_history_item)//缴费项目
        TextView house_pay_history_item;
        @InjectView(R.id.house_pay_history_amount)//缴费金额
        TextView house_pay_history_amount;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

}
