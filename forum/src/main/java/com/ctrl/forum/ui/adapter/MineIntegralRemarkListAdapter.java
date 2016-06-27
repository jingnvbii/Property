package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.ctrl.forum.R;
import com.ctrl.forum.entity.ExchaneProduct;
import com.ctrl.forum.utils.DateUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 兑换商品积分记录
 * Created by Administrator on 2016/5/12.
 */
public class MineIntegralRemarkListAdapter extends BaseAdapter{
    private List<ExchaneProduct> exchaneProducts;
    private Context context;

    public MineIntegralRemarkListAdapter(Context context) {
        this.context = context;
    }

    public void setExchaneProducts(List<ExchaneProduct> exchaneProducts) {
        this.exchaneProducts = exchaneProducts;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return exchaneProducts!=null?exchaneProducts.size():0;
    }

    @Override
    public Object getItem(int position) {
        return exchaneProducts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_mine_point_history,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (exchaneProducts!=null && exchaneProducts.get(position)!=null){
            viewHolder.tv_name.setText(exchaneProducts.get(position).getProductName());
            viewHolder.tv_money.setText(exchaneProducts.get(position).getPoint());
            viewHolder.tv_time.setText(DateUtil.getStringByFormat(exchaneProducts.get(position).getCreateTime(), "yyyy-MM-dd"));
            Arad.imageLoader.load(exchaneProducts.get(position).getProductImg()).
                    placeholder(context.getResources().getDrawable(R.mipmap.image_default)).into(viewHolder.iv_pic);
        }

        return convertView;
    }

    class ViewHolder{
        @InjectView(R.id.iv_pic)
        ImageView iv_pic;   //图片
        @InjectView(R.id.tv_name)
        TextView tv_name;   //商品名称
        @InjectView(R.id.tv_money)
        TextView tv_money;
        @InjectView(R.id.tv_time)
        TextView tv_time;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
