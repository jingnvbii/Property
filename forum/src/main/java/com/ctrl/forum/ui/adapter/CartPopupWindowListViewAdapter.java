package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.cart.datasave.GoodsBean;
import com.ctrl.forum.cart.datasave.OperateGoodsDataBaseStatic;
import com.ctrl.forum.ui.activity.store.StoreShopListVerticalStyleActivity;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 购物车弹窗列表 adapter
 * Created by jason on 2016/4/8.
 */
public class CartPopupWindowListViewAdapter extends BaseAdapter {
    private Context mcontext;
    private List<GoodsBean> list;
  //  private List<String> listNameStr;

    private OnItemClickListener mOnItemClickListener;
    private OperateGoodsDataBaseStatic mGoodsDataBaseInterface = null;

    public CartPopupWindowListViewAdapter(Context context) {
        this.mcontext = context;
    }

    public void setList(List<GoodsBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    //定义接口
    public interface OnItemClickListener {
        void onItemJiaClick(ViewHolder v);

        void onItemJianClick(ViewHolder v);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
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
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mcontext).inflate(R.layout.item_cart_popup_lv, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        /** 获取存储的商品数量 */
        if (mGoodsDataBaseInterface.getSecondGoodsNumber(mcontext, StoreShopListVerticalStyleActivity.SELECTPOSITION, list.get(position).getGoodsid()) == 0) {
            holder.tv_popup_lv_number.setText("");
            holder.iv_popup_lv_subtract.setVisibility(View.GONE);
            holder.tv_popup_lv_number.setVisibility(View.GONE);
        } else {

            holder.tv_popup_lv_number.setText("" + mGoodsDataBaseInterface.getSecondGoodsNumber(mcontext, StoreShopListVerticalStyleActivity.SELECTPOSITION, list.get(position).getGoodsid()));
            holder.tv_popup_lv_number.setVisibility(View.VISIBLE);
            holder.iv_popup_lv_subtract.setVisibility(View.VISIBLE);
        }

        GoodsBean mGoodsBean = list.get(position);
        holder.setPosition(position);
        holder.tv_popup_lv_price.setText(mGoodsBean.getGoodsprice()+"");
        holder.tv_popup_lv_number.setText(mGoodsBean.getGoodsnum());
        holder.tv_popup_lv_name.setText(mGoodsBean.getGoodsname());

        setOnListtener(holder);
        return convertView;
    }

    //触发
    protected void setOnListtener(final ViewHolder holder) {
        if (mOnItemClickListener != null) {

            holder.iv_popup_lv_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemJiaClick(holder);
                }
            });
            holder.iv_popup_lv_subtract.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemJianClick(holder);
                }
            });
        }
    }

  /*  public void setName(List<String> listNameStr) {
        this.listNameStr = listNameStr;
        notifyDataSetChanged();
    }
*/
    public static class ViewHolder {
        @InjectView(R.id.tv_popup_lv_name)//名称
                TextView tv_popup_lv_name;
        @InjectView(R.id.tv_popup_lv_price)//价格
                TextView tv_popup_lv_price;
        @InjectView(R.id.iv_popup_lv_add)//加号
        public ImageView iv_popup_lv_add;
        @InjectView(R.id.iv_popup_lv_subtract)//减号
        public ImageView iv_popup_lv_subtract;
        @InjectView(R.id.tv_popup_lv_number)//数量
        public TextView tv_popup_lv_number;
     /*   @InjectView(R.id.rl_all)//根布局
      RelativeLayout rl_all;
*/
        private int position;

        public void setPosition(int position) {
            this.position = position;
        }

        public int getPosition() {
            return position;
        }

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
