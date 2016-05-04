package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.entity.Invitation_listview;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 帖子评论  adapter
 * Created by jason on 2016/4/8.
 */
public class InvitationCommentListViewAdapter extends BaseAdapter{
    private Context mcontext;
    private List<Invitation_listview>kindList;

    public InvitationCommentListViewAdapter(Context context) {
               this.mcontext=context;
    }

    public void setList(List<Invitation_listview> list) {
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
            convertView= LayoutInflater.from(mcontext).inflate(R.layout.item_invitation_comment_listview_text,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }
        Invitation_listview invitation_listview = kindList.get(position);
        holder.tv_name.setText(invitation_listview.getName());
        return convertView;
    }

    static class ViewHolder{
        @InjectView(R.id.tv_name)//标题
                TextView tv_name;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
