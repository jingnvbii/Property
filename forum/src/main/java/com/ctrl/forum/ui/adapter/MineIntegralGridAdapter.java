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
import com.ctrl.forum.entity.IntegralProduct;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 积分
 * Created by Administrator on 2016/5/11.
 */
public class MineIntegralGridAdapter extends BaseAdapter{
    private List<IntegralProduct> product;
    private Context context;
    public MineIntegralGridAdapter( Context context){
        this.context = context;
    }

    public void setData(List<IntegralProduct> product) {
        this.product = product;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return product==null?0:product.size();
    }

    @Override
    public Object getItem(int position) {
        return product.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_mine_integraproduct,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }
        if (product!=null) {
            holder.tv_name.setText(product.get(position).getName());
            holder.tv_money.setText(product.get(position).getNeedPoint());
            String url = product.get(position).getListImgUrl();
            Arad.imageLoader.load(url).placeholder(context.getResources().getDrawable(R.mipmap.shop_item)).into(holder.iv_pic);
        }

        return convertView;
    }

    class ViewHolder{
        @InjectView(R.id.iv_pic)
        ImageView iv_pic;
        @InjectView(R.id.tv_name)
        TextView tv_name;
        @InjectView(R.id.tv_money)
        TextView tv_money;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
