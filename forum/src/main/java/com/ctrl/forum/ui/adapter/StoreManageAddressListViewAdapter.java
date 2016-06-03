package com.ctrl.forum.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.arad.utils.AnimUtil;
import com.ctrl.forum.R;
import com.ctrl.forum.entity.Address;
import com.ctrl.forum.ui.activity.store.StoreEditAddressActivity;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 商城管理地址列表 adapter
 * Created by jason on 2016/4/8.
 */
public class StoreManageAddressListViewAdapter extends BaseAdapter{
    private Context mcontext;
    private List<Address> list;

    public StoreManageAddressListViewAdapter(Context context) {
               this.mcontext=context;
    }

    public void setList(List<Address> list) {
        this.list = list;
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
            convertView= LayoutInflater.from(mcontext).inflate(R.layout.item_store_manage_address,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }
        final Address mAddress=list.get(position);
        holder.tv_manage_address_name.setText(mAddress.getReceiveName());
        holder.tv_manage_address_tel.setText(mAddress.getMobile());
        holder.tv_manage_address_locate.setText(mAddress.getAddressBase()+mAddress.getAddressDetail());
        if(mAddress.getIsDefault().equals("1")){
            holder.cb_manage_address.setChecked(true);
        }else {
            holder.cb_manage_address.setChecked(false);
        }
        holder.iv_manage_address_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(mcontext, StoreEditAddressActivity.class);
                intent.putExtra("address",mAddress);
                mcontext.startActivity(intent);
                AnimUtil.intentSlidIn((Activity)mcontext);
            }
        });
        return convertView;
    }

 public  static class ViewHolder{
        @InjectView(R.id.cb_manage_address)//单选按钮
                CheckBox cb_manage_address;
        @InjectView(R.id.tv_manage_address_name)//姓名
                TextView tv_manage_address_name;
        @InjectView(R.id.tv_manage_address_tel)//电话
                TextView tv_manage_address_tel;
        @InjectView(R.id.tv_manage_address_locate)//地址
                TextView tv_manage_address_locate;
        @InjectView(R.id.iv_manage_address_edit)//编辑
            public  ImageView iv_manage_address_edit;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
