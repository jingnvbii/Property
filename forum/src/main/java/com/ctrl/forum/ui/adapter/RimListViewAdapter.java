package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.ctrl.forum.R;
import com.ctrl.forum.entity.RimServeCategory;
import com.ctrl.forum.entity.RimServeCategorySecond;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 周边服务-listview
 * Created by Administrator on 2016/5/16.
 */
public class RimListViewAdapter extends BaseAdapter{
    private List<RimServeCategory> data;
    private List<RimServeCategorySecond> grid;
    private Context context;
    private RimGridViewAdapter rimGridViewAdapter;

    public RimListViewAdapter(Context context){
        this.context = context;
    }

    public void setRimGridViewAdapter(RimGridViewAdapter rimGridViewAdapter) {
        this.rimGridViewAdapter = rimGridViewAdapter;
    }

    public void setData(List<RimServeCategory> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data==null?0:data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder3 holder3 = null;
        if(convertView==null) {
            convertView= LayoutInflater.from(context).inflate(R.layout.item_rim_list,parent,false);
            holder3=new ViewHolder3(convertView);
            convertView.setTag(holder3);
        }else {
            holder3=(ViewHolder3)convertView.getTag();
        }

        if (data!=null){
            if (data.get(position).getCategoryIcon()!=null && !data.get(position).getCategoryIcon().equals("")) {
                Arad.imageLoader.load(data.get(position).getCategoryIcon()).
                        placeholder(context.getResources().getDrawable(R.mipmap.hot)).
                        into(holder3.iv_pic);
            }
            holder3.tv_title.setText(data.get(position).getName());
            holder3.gv_hot.setAdapter(rimGridViewAdapter);
            grid = data.get(position).getAroundservicecategorylist();
            if (grid!=null){
                if (grid.size()%3==0){
                    //直接给gridView赋值
                    rimGridViewAdapter.setData(grid);
                }if (grid.size()%3==1){
                    RimServeCategorySecond da = new RimServeCategorySecond();
                    grid.add(da);
                    RimServeCategorySecond da1 = new RimServeCategorySecond();
                    grid.add(da1);
                    rimGridViewAdapter.setData(grid);
                }
                if (grid.size()%3==2){
                    RimServeCategorySecond da = new RimServeCategorySecond();
                    grid.add(da);
                    rimGridViewAdapter.setData(grid);}
            }
        }
        return convertView;
    }

    class ViewHolder3{
        @InjectView(R.id.iv_pic)
        ImageView iv_pic;
        @InjectView(R.id.tv_title)
        TextView tv_title;
        @InjectView(R.id.gv_hot)
        GridView gv_hot;
        ViewHolder3(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
