package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.ctrl.forum.R;
import com.ctrl.forum.customview.StorkPicView;
import com.ctrl.forum.entity.Product2;
import com.ctrl.forum.entity.ProductCategroy;

import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 商品管理
 */
public class MineShopManagerAdapter extends SectionedBaseAdapter {
	
	private Context mContext;
    private List<ProductCategroy> leftStr;
    private Map<String,List<Product2>> rightStr;
    private CompoundButton.OnCheckedChangeListener onCheckBox;
	
	public MineShopManagerAdapter(Context context){
		this.mContext = context;
	}

    public void setLeftStr(List<ProductCategroy> leftStr) {
        this.leftStr = leftStr;
        notifyDataSetChanged();
    }

    public void setRightStr( Map<String,List<Product2>> rightStr) {
        this.rightStr = rightStr;
        notifyDataSetChanged();
    }

    public void setOnCheckBox(CompoundButton.OnCheckedChangeListener onCheckBox) {
        this.onCheckBox = onCheckBox;
    }

    public CompoundButton.OnCheckedChangeListener getOnCheckBox() {
        return onCheckBox;
    }

    @Override
    public Object getItem(int section, int position) {
        //return rightStr.get(section).get(position);
        return rightStr.get(leftStr.get(section).getId()).get(position);
    }

    @Override
    public long getItemId(int section, int position) {
        return position;
    }

    @Override
    public int getSectionCount() {
        return leftStr!=null?leftStr.size():0;
    }

    @Override
    public int getCountForSection(int section) {
        //return rightStr!=null?rightStr.get(section)!=null?rightStr.get(section).size():0:0;
        return rightStr!=null?rightStr.get(leftStr.get(section).getId())!=null?rightStr.get(leftStr.get(section).getId()).size():0:0;
    }

    @Override
    public View getItemView(final int section, final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_mine_list_right,parent,false);
            holder = new ViewHolder(convertView);
            holder.iv_checkBox.setOnCheckedChangeListener(onCheckBox);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        if (leftStr!=null&&rightStr!=null){
            holder.iv_right_pic.setColour(mContext.getResources().getColor(R.color.c_gray));
            holder.iv_right_pic.setBorderWidth(3);
            List<Product2> cProducts =rightStr.get(leftStr.get(section).getId());
            if (cProducts!=null) {
                Arad.imageLoader.load(cProducts.get(position).getListImgUrl()).into(holder.iv_right_pic);
                holder.tv_store_name.setText(cProducts.get(position).getName());
                holder.tv_dan_price.setText(cProducts.get(position).getSellingPrice()+"元/份");
                holder.tv_sale_num.setText(cProducts.get(position).getSalesVolume());
            }
            String storeId = cProducts.get(position).getId();//商品的id
            holder.iv_checkBox.setTag(storeId);
        }
        return convertView;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        LinearLayout layout = null;
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (LinearLayout) inflator.inflate(R.layout.item_mine_list_header, null);
        } else {
            layout = (LinearLayout) convertView;
        }
        layout.setClickable(false);
        ProductCategroy productCategory = leftStr.get(section);
        ((TextView) layout.findViewById(R.id.tv_header)).setText(productCategory.getName());
        return layout;
    }

    class ViewHolder{
        @InjectView(R.id.tv_store_name)
        TextView  tv_store_name;
        @InjectView(R.id.tv_sale_num)
        TextView  tv_sale_num;
        @InjectView(R.id.tv_dan_price)
        TextView  tv_dan_price;
        @InjectView(R.id.iv_right_pic)
        StorkPicView iv_right_pic;
        @InjectView(R.id.iv_checkBox)
        CheckBox iv_checkBox;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

}
