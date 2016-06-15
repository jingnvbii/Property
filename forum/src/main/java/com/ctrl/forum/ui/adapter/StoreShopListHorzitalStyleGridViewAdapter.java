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
import com.ctrl.forum.cart.datasave.OperateGoodsDataBaseStatic;
import com.ctrl.forum.entity.Product2;
import com.ctrl.forum.ui.activity.store.StoreShopListHorzitalStyleActivity;
import com.ctrl.forum.ui.activity.store.StoreShopListVerticalStyleActivity;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 商城首页列表 adapter
 * Created by jason on 2016/4/8.
 */
public class StoreShopListHorzitalStyleGridViewAdapter extends BaseAdapter{
    private Context mcontext;
    private List<Product2> list;
    private OnItemClickListener mOnItemClickListener;
    private OperateGoodsDataBaseStatic mGoodsDataBaseInterface=null;

    public StoreShopListHorzitalStyleGridViewAdapter(Context context) {
               this.mcontext=context;
    }

    public void setList(List<Product2> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    //定义接口
    public interface OnItemClickListener{
        void onItemJiaClick(ViewHolder v);
        void onItemJianClick(ViewHolder v);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mOnItemClickListener = listener ;
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
            convertView= LayoutInflater.from(mcontext).inflate(R.layout.item_gridview_store_shop_list_horzital_style_fragment,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }
        Product2 merchant=list.get(position);

        /** 获取存储的商品数量 */
        if (mGoodsDataBaseInterface.getSecondGoodsNumber(mcontext, StoreShopListHorzitalStyleActivity.SELECTPOSITION,merchant.getId()) == 0) {
            holder.tv_numbers.setText("");
            holder.iv_subtract.setVisibility(View.GONE);
            holder.tv_numbers.setVisibility(View.GONE);
        } else {
            holder.tv_numbers.setText("" + mGoodsDataBaseInterface.getSecondGoodsNumber(mcontext, StoreShopListVerticalStyleActivity.SELECTPOSITION , merchant.getId()));
            holder.iv_subtract.setVisibility(View.VISIBLE);
            holder.tv_numbers.setVisibility(View.VISIBLE);
        }
        holder.tv_name.setText(merchant.getName());
        if(merchant.getSalesVolume()==null||merchant.getSalesVolume().equals("")){
            holder.tv_xiaoliang.setText("月销：0");

        }else {
            holder.tv_xiaoliang.setText("月销：" + merchant.getSalesVolume());
        }
            holder.tv_price.setText(merchant.getSellingPrice() + "元/份");
        Arad.imageLoader.load(merchant.getListImgUrl()).placeholder(R.mipmap.default_error).into(holder.iv_title_photo);
        holder.setPosition(position);
        setOnListtener(holder);
        return convertView;
    }

    //触发
    protected void setOnListtener(final ViewHolder holder){
        if(mOnItemClickListener != null){

            holder.iv_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemJiaClick(holder);
                }
            });
            holder.iv_subtract.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemJianClick(holder);
                }
            });
        }
    }

 public static class ViewHolder{
       /* @InjectView(R.id.iv_grid_item)//图片
                ImageView iv_grid_item;*/
        @InjectView(R.id.tv_name)//文字
                TextView  tv_name;
        @InjectView(R.id.tv_xiaoliang)//文字
                TextView  tv_xiaoliang;
        @InjectView(R.id.tv_price)//文字
                TextView  tv_price;
        @InjectView(R.id.tv_numbers)//文字
        public  TextView  tv_numbers;
        @InjectView(R.id.iv_title_photo)
                ImageView iv_title_photo;
        @InjectView(R.id.iv_add)
             public ImageView iv_add;
        @InjectView(R.id.iv_subtract)
        public ImageView iv_subtract;
     private int position;

     public void setPosition(int position){
         this.position=position;
     }
     public int getPosition(){
         return position;
     }
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
