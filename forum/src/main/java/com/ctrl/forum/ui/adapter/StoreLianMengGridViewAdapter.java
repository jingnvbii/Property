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
import com.ctrl.forum.entity.CompanyUnion;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 店铺详情 联盟商家 adapter
 * Created by jason on 2016/4/8.
 */
public class StoreLianMengGridViewAdapter extends BaseAdapter{
    private Context mcontext;
    private List<CompanyUnion>kindList;

    public StoreLianMengGridViewAdapter(Context context) {
               this.mcontext=context;
    }

    public void setList(List<CompanyUnion> list) {
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
            convertView= LayoutInflater.from(mcontext).inflate(R.layout.item_store_lian_meng_gridview,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }
        CompanyUnion mCompanyUnion=kindList.get(position);
        holder.tv_lianmeng_name.setText(mCompanyUnion.gettCompanysLis().get(0).getName());
        Arad.imageLoader.load(mCompanyUnion.gettCompanysLis().get(0).getImg()).placeholder(R.mipmap.default_error).into(holder.iv_title_photo);
        return convertView;
    }

    static class ViewHolder{
        @InjectView(R.id.tv_lianmeng_name)//商家名称
                TextView tv_lianmeng_name;
        @InjectView(R.id.iv_title_photo)//商家logo
                ImageView iv_title_photo;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
