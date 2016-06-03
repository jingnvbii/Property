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
import com.ctrl.forum.entity.ProductCollect;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 商品收藏 adapter
 * Created by jason on 2016/4/8.
 */
public class ProductCollectFragmentAdapter extends BaseAdapter{
    private Context mcontext;
    private List<ProductCollect> list;

    public ProductCollectFragmentAdapter(Context context) {
               this.mcontext=context;
               list = new ArrayList<>();
    }

    public void setList(List<ProductCollect> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return list==null?0:list.size();

    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(mcontext).inflate(R.layout.item_product_collect_fragment,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }
        if (list!=null && list.get(position)!=null){
            ProductCollect product=list.get(position);
            holder.tv_name.setText(product.getName());
            holder.tv_sale.setText("月销量:"+product.getSalesVolume()+"份");
            holder.tv_money.setText(product.getSellingPrice()+"/份");
            Arad.imageLoader.load(product.getListImgUrl()).into(holder.iv_title_photo);
        }
        return convertView;
    }

    static class ViewHolder{
        @InjectView(R.id.iv_title_photo)
                ImageView iv_title_photo;
        @InjectView(R.id.tv_name)
                TextView  tv_name;
        @InjectView(R.id.tv_sale)
        TextView  tv_sale;  //销量
        @InjectView(R.id.tv_money)
        TextView  tv_money;  //价格
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
