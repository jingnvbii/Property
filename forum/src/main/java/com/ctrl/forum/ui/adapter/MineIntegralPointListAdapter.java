package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.entity.RedeemHistory;
import com.ctrl.forum.utils.DateUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 兑换商品积分记录
 * Created by Administrator on 2016/5/12.
 */
public class MineIntegralPointListAdapter extends BaseAdapter{
    private List<RedeemHistory> redeemHistories;
    private Context context;

    public MineIntegralPointListAdapter(Context context) {
        this.context = context;
    }

    public void setExchaneProducts(List<RedeemHistory> redeemHistories) {
        this.redeemHistories = redeemHistories;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return redeemHistories!=null?redeemHistories.size():0;
    }

    @Override
    public Object getItem(int position) {
        return redeemHistories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_mine_remark_history,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (redeemHistories!=null && redeemHistories.get(position)!=null){
            viewHolder.tv_day.setText(DateUtil.getStringByFormat(redeemHistories.get(position).getCreateTime(), "yyyy-MM-dd   HH:mm:ss"));
            viewHolder.tv_fen.setText(redeemHistories.get(position).getPoint()+"");

            int point = redeemHistories.get(position).getPoint();
            if (point>0){
                viewHolder.tv_fen.setText("+"+point);
            }else{
                viewHolder.tv_fen.setText(point+"");
            }
            String doType  = redeemHistories.get(position).getDoType();
            switch (doType){
                case "1":
                    viewHolder.tv_content.setText("商城消费奖励"+point+"积分");
                    break;
                case "2":
                    viewHolder.tv_content.setText("积分兑换"+point+"积分");
                    break;
                case "3":
                    viewHolder.tv_content.setText("每日签到奖励"+point+"积分");
                    break;
                case "4":
                    viewHolder.tv_content.setText("连续签到30天奖励"+point+"积分");
                    break;
                case "5":
                    viewHolder.tv_content.setText("连续签到100天奖励"+point+"积分");
                    break;
                case "6":
                    viewHolder.tv_content.setText("连续签到200天奖励"+point+"积分");
                    break;
                case "7":
                    viewHolder.tv_content.setText("绑定手机号奖励"+point+"积分");
                    break;
                case "8":
                    viewHolder.tv_content.setText("完整填写个人资料奖励"+point+"积分");
                    break;
                case "9":
                    viewHolder.tv_content.setText("回别人帖奖励"+point+"积分");
                    break;
                case "10":
                    viewHolder.tv_content.setText("被管理员拉黑"+point+"积分");
                    break;
                case "11":
                    viewHolder.tv_content.setText("被拉黑设备"+point+"积分");
                    break;
                default:
                    viewHolder.tv_content.setText("其它");
                    break;
            }
        }
        return convertView;
    }
    class ViewHolder{
        @InjectView(R.id.tv_content)
        TextView tv_content;
        @InjectView(R.id.tv_day)
        TextView tv_day;
        @InjectView(R.id.tv_fen)
        TextView tv_fen;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
