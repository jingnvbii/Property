package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.entity.SearchHistory;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 周边服务-搜索历史记录
 * Created by Administrator on 2016/5/16.
 */
public class RimSearchHistoryAdapter extends BaseAdapter{
    private List<SearchHistory> data;
    private Context context;
    private View.OnClickListener onKeyword;

    public RimSearchHistoryAdapter(Context context){
        this.context = context;
    }

    public void setOnKeyword(View.OnClickListener onKeyword) {
        this.onKeyword = onKeyword;
    }

    public void setData(List<SearchHistory> data) {
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
        ViewHolder holder = null;

        if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.rim_search_history_item, parent, false);
                    holder = new ViewHolder(convertView);
                    holder.tv_history_content.setOnClickListener(onKeyword);
                    convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        if (data!=null){
        holder.tv_history_content.setText(data.get(position).getKeyword());}

        return convertView;
    }

        class ViewHolder {
            @InjectView(R.id.tv_history_content)
            TextView tv_history_content;

            ViewHolder(View view) {
                ButterKnife.inject(this, view);
            }
        }
}
