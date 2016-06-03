package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.beanu.arad.Arad;
import com.ctrl.forum.R;
import com.ctrl.forum.entity.Qualification2;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 店铺详情 资质 adapter
 * Created by jason on 2016/4/8.
 */
public class StoreZiZhiGridViewAdapter extends BaseAdapter{
    private Context mcontext;
    private List<Qualification2>kindList;

    public StoreZiZhiGridViewAdapter(Context context) {
               this.mcontext=context;
    }

    public void setList(List<Qualification2> list) {
        this.kindList = list;
        notifyDataSetChanged();
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
            convertView= LayoutInflater.from(mcontext).inflate(R.layout.item_store_zi_zhi_gridview,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }
        Qualification2 mQualification=kindList.get(position);
        Arad.imageLoader.load(mQualification.getImg()).placeholder(R.mipmap.default_error).into(holder.iv_zizhi);

        return convertView;
    }

    static class ViewHolder{
        @InjectView(R.id.iv_zizhi)//商家logo
                ImageView iv_zizhi;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
