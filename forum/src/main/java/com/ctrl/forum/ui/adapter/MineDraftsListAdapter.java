package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.base.ListItemTypeInterf;
import com.ctrl.forum.entity.Drafts;
import com.ctrl.forum.entity.DraftsPostList;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 草稿箱
 * Created by jason on 2016/4/8.
 */
public class MineDraftsListAdapter extends BaseAdapter{
    private Context mcontext;
    private List<ListItemTypeInterf> list;
    private View.OnClickListener onEdit;
    private View.OnClickListener onDelete;

    public void setOnDelete(View.OnClickListener onDelete) {
        this.onDelete = onDelete;
    }

    public void setOnEdit(View.OnClickListener onEdit) {
        this.onEdit = onEdit;
    }

    public MineDraftsListAdapter(Context context) {
               this.mcontext=context;
        list = new ArrayList<>();
    }

    public void setList(List<ListItemTypeInterf> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    // 获取到具体的list的每一个成员的类型号
    @Override
    public int getItemViewType(int position) {
        // TODO Auto-generated method stub
        return list.get(position).getType();
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
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        ViewHolder1 viewHolder1 = null;
        ViewHolder2 viewHolder2 = null;
        if (convertView == null) {
            switch (type) {
                case 0:
                    convertView = LayoutInflater.from(mcontext).inflate(
                            R.layout.item_mine_drafts_time, parent, false);
                    viewHolder1 = new ViewHolder1(convertView);
                    convertView.setTag(viewHolder1);
                    break;
                case 1:
                    convertView = LayoutInflater.from(mcontext).inflate(
                            R.layout.item_mine_drafts_content, parent, false);
                    viewHolder2 = new ViewHolder2(convertView);
                    viewHolder2.bt_edit.setOnClickListener(onEdit);
                    viewHolder2.bt_delete.setOnClickListener(onDelete);
                    convertView.setTag(viewHolder2);
                    break;
                default:
                    break;
            }
        } else {
            switch (type) {
                case 0:
                    viewHolder1 = (ViewHolder1) convertView.getTag();
                    break;
                case 1:
                    viewHolder2 = (ViewHolder2) convertView.getTag();
                    break;
                default:
                    break;
            }
        }

        // 设置数据
        switch (type) {
            case 0:
                DraftsPostList draftsPostList = (DraftsPostList) list.get(position);
                viewHolder1.tv_time.setText(draftsPostList.getTime());
                viewHolder1.tv_num.setText("("+draftsPostList.getCount()+"封)");
                break;
            case 1:
                Drafts drafts = (Drafts) list.get(position);
                viewHolder2.tv_title_content.setText(drafts.getTitle());
                viewHolder2.bt_delete.setOnClickListener(onDelete);
                viewHolder2.bt_edit.setOnClickListener(onEdit);
                if (drafts.getStatus()!=null){
                    if (drafts.getStatus().equals("2")){viewHolder2.iv_back.setVisibility(View.VISIBLE);}
                    else {viewHolder2.iv_back.setVisibility(View.GONE);}
                }
                break;
            default:
                break;
        }

        return convertView;
    }

    class ViewHolder1 {
        @InjectView(R.id.tv_time)
        TextView  tv_time;
        @InjectView(R.id.tv_num)
        TextView  tv_num;
        ViewHolder1(View view) {
            ButterKnife.inject(this, view);
        }
    }
     class ViewHolder2{
        @InjectView(R.id.tv_title_content)
        TextView  tv_title_content;
        @InjectView(R.id.bt_edit)
        Button bt_edit;
        @InjectView(R.id.bt_delete)
        Button  bt_delete;
        @InjectView(R.id.iv_back)
        ImageView iv_back;
         ViewHolder2(View view) {
            ButterKnife.inject(this, view);
        }
    }

}
