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
import com.ctrl.forum.cart.datasave.GoodsDataBaseInterface;
import com.ctrl.forum.cart.datasave.OperateGoodsDataBase;
import com.ctrl.forum.entity.Product2;
import com.ctrl.forum.ui.activity.store.StoreSearchCommodityActivity;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 商品搜索  adapter
 * Created by jason on 2016/4/8.
 */
public class StoreSearchGridViewAdapter extends BaseAdapter{
    private Context mcontext;
    private List<Product2>kindList;
    private OnItemClickListener mOnItemClickListener;
    private GoodsDataBaseInterface mGoodsDataBaseInterface=null;

    public StoreSearchGridViewAdapter(Context context) {
               this.mcontext=context;
    }

    public void setList(List<Product2> list) {
        this.kindList = list;
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
            convertView= LayoutInflater.from(mcontext).inflate(R.layout.item_store_search,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }
        Product2 kind=kindList.get(position);
         mGoodsDataBaseInterface = OperateGoodsDataBase.getInstance();
            /** 获取存储的商品数量 */
            if (mGoodsDataBaseInterface.getSecondGoodsNumber(mcontext, StoreSearchCommodityActivity.SELECTPOSITION, kind.getId()) == 0) {
                holder.item_menu_content_number.setText("");
                holder.item_menu_content_jian.setVisibility(View.GONE);
                holder.item_menu_content_number.setVisibility(View.GONE);
            } else {
                holder.item_menu_content_number.setText("" + mGoodsDataBaseInterface.getSecondGoodsNumber(mcontext, StoreSearchCommodityActivity.SELECTPOSITION, kind.getId()));
                holder.item_menu_content_number.setVisibility(View.VISIBLE);
                holder.item_menu_content_jian.setVisibility(View.VISIBLE);
            }
        holder.tv_store_name.setText(kind.getName());
        holder.tv_xiaoliang.setText("月销 ："+kind.getSalesVolume());
        holder.tv_price.setText(kind.getSellingPrice()+"/件");
        Arad.imageLoader.load(kind.getListImgUrl()).placeholder(R.mipmap.default_error).into(holder.iv_store_image);
        holder.setPosition(position);
        setOnListtener(holder);
        return convertView;
    }


    //触发
    protected void setOnListtener(final ViewHolder holder){
        if(mOnItemClickListener != null){

            holder.item_menu_content_jia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemJiaClick(holder);
                }
            });
            holder.item_menu_content_jian.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemJianClick(holder);
                }
            });
        }
    }

  public   static class ViewHolder{
        @InjectView(R.id.tv_store_name)//商品名称
                TextView  tv_store_name;
        @InjectView(R.id.tv_xiaoliang)//商品销量
                TextView  tv_xiaoliang;
        @InjectView(R.id.tv_price)//商品价格
                TextView  tv_price;
        @InjectView(R.id.iv_store_image)//商品图片
                ImageView iv_store_image;
      @InjectView(R.id.item_commedity_numbers)
        public TextView item_menu_content_number;    //数量
        @InjectView(R.id.item_commedity_jian)
        public ImageView item_menu_content_jian;    //减号
        @InjectView(R.id.item_commedity_jia)
        public ImageView item_menu_content_jia;    //加号
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
