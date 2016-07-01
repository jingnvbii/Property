package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.entity.PostReply;
import com.ctrl.forum.face.FaceConversionUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 商城首页列表 adapter
 * Created by jason on 2016/4/8.
 */
public class FriendStyleRelpyAdapter extends BaseAdapter{
    private Context mcontext;
    private List<PostReply> list;

    public FriendStyleRelpyAdapter(Context context) {
               this.mcontext=context;
    }

    public void setList(List<PostReply> list) {
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
            convertView= LayoutInflater.from(mcontext).inflate(R.layout.item_friend_style_reply,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }
        PostReply merchant=list.get(position);
        holder.tv_reply_name.setText(merchant.getMemberName()+":");
        SpannableString spannableString2 = FaceConversionUtil.getInstace().getExpressionString(mcontext, merchant.getReplyContent());
        holder.tv_reply_content.setText(spannableString2);
        return convertView;
    }

    static class ViewHolder{
       /* @InjectView(R.id.iv_grid_item)//图片
                ImageView iv_grid_item;*/
        @InjectView(R.id.tv_reply_name)//文字
                TextView  tv_reply_name;
        @InjectView(R.id.tv_reply_content)//文字
                TextView  tv_reply_content;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
