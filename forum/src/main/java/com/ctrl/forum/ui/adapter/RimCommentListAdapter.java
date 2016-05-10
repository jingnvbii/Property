package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ctrl.forum.R;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.base.RimCommentListItemType;
import com.ctrl.forum.entity.CommentOne;
import com.ctrl.forum.entity.CommentReplay;
import com.ctrl.forum.entity.CommentShop;
import com.ctrl.forum.entity.CommentThree;
import java.util.ArrayList;
import java.util.List;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/5/6.
 */
public class RimCommentListAdapter extends BaseAdapter {
    private Context context;
    private List<RimCommentListItemType> list;

    public RimCommentListAdapter(Context context) {
        this.context = context;
        list = new ArrayList<RimCommentListItemType>();
    }

    public void setList(List<RimCommentListItemType> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addList(List<RimCommentListItemType> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    //返回item类型的个数
    @Override
    public int getViewTypeCount() {return Constant.RIM_TYPE_ITEM;}

    // 获取到具体的list的每一个成员的类型号
    @Override
    public int getItemViewType(int position) {return list.get(position).getType();}

    @Override
    public int getCount() {return list.size();}

    @Override
    public Object getItem(int position) {return position;}

    @Override
    public long getItemId(int position) {return 0;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        ViewHolder viewHolder = null;
        ViewHolder1 viewHolder1 = null;
        ViewHolder2 viewHolder2 = null;
        ViewHolder3 viewHolder3 = null;
        if (convertView == null) {
            switch (type) {
                case 0: //评论列表三张图片,返回值为0
                    convertView = LayoutInflater.from(context).inflate(
                            R.layout.item_rimcomment_threepic, parent, false);
                    viewHolder = new ViewHolder(convertView);
                    convertView.setTag(viewHolder);
                    break;
                case 1: //返回值为1,表示一张图片
                    convertView = LayoutInflater.from(context).inflate(
                            R.layout.item_rimcomment_onepic, parent, false);
                    viewHolder1 = new ViewHolder1(convertView);
                    convertView.setTag(viewHolder1);
                    break;
                case 2: //评论..回复某人 返回2
                    convertView = LayoutInflater.from(context).inflate(
                            R.layout.item_rimcomment_replay, parent, false);
                    viewHolder2 = new ViewHolder2(convertView);
                    convertView.setTag(viewHolder2);
                    break;
                case 3: //直接文字评论店铺 返回3
                    convertView = LayoutInflater.from(context).inflate(
                            R.layout.item_rimcomment_comment, parent, false);
                    viewHolder3 = new ViewHolder3(convertView);
                    convertView.setTag(viewHolder3);
                    break;
                default:
                    break;
            }
        } else {
            switch (type) {
                case 0:
                    viewHolder = (ViewHolder) convertView.getTag();
                    break;
                case 1:
                    viewHolder1 = (ViewHolder1) convertView.getTag();
                    break;
                case 2:
                    viewHolder2 = (ViewHolder2) convertView.getTag();
                    break;
                case 3:
                    viewHolder3 = (ViewHolder3) convertView.getTag();
                    break;
                default:
                    break;
            }
        }

        // 设置数据
        switch (type) {
            case 0:
                CommentThree commentThree = (CommentThree) list.get(position);

                break;

            case 1:
                CommentOne commentOne = (CommentOne) list.get(position);

                break;
            case 2:
                CommentReplay commentReplay = (CommentReplay) list.get(position);

                break;
            case 3:
                CommentShop commentShop = (CommentShop) list.get(position);

                break;
            default:
                break;
        }
        return convertView;
    }
    //评论列表三张图片,返回值为0
    class ViewHolder {
        LinearLayout ll_threePic;//存放三张图片的LinearLayout
        @InjectView(R.id.iv_head)
        ImageView iv_head;//头像
        @InjectView(R.id.tv_name)
        TextView tv_name; //本人名
        @InjectView(R.id.iv_grade)
        TextView iv_grade; //等级
        @InjectView(R.id.tv_data)
        TextView tv_data;//时间
        @InjectView(R.id.tv_lz)
        TextView tv_lz;//是否是楼主
        @InjectView(R.id.iv_pic1)
        TextView tv_replay_name;//回复的名字
        ViewHolder(View view) {ButterKnife.inject(this, view);}
    }
    //返回值为1,表示一张图片
    class ViewHolder1 {
        @InjectView(R.id.iv_head)
        ImageView iv_head;//头像
        @InjectView(R.id.tv_name)
        TextView tv_name; //本人名
        @InjectView(R.id.iv_grade)
        TextView iv_grade; //等级
        @InjectView(R.id.tv_data)
        TextView tv_data;//时间
        @InjectView(R.id.tv_lz)
        TextView tv_lz;//是否是楼主
        @InjectView(R.id.iv_pic1)
        TextView tv_replay_name;//回复的名字
        ViewHolder1(View view) {ButterKnife.inject(this, view);}
    }
    //评论..回复某人 返回2
    class ViewHolder2{
        @InjectView(R.id.iv_head)
          ImageView iv_head;//头像
        @InjectView(R.id.tv_name)
          TextView tv_name; //本人名
        @InjectView(R.id.iv_grade)
          TextView iv_grade; //等级
        @InjectView(R.id.tv_data)
        TextView tv_data;//时间
        @InjectView(R.id.tv_lz)
        TextView tv_lz;//是否是楼主
        @InjectView(R.id.tv_replay_name)
        TextView tv_replay_name;//回复的名字
        @InjectView(R.id.tv_replay_content)
        TextView  tv_replay_content;//回复的内容
        ViewHolder2(View view) {ButterKnife.inject(this, view);}
    }
    //评论..回复某人 返回2
    class ViewHolder3{
        @InjectView(R.id.iv_head)
        ImageView iv_head;//头像
        @InjectView(R.id.tv_name)
        TextView tv_name; //本人名
        @InjectView(R.id.iv_grade)
        TextView iv_grade; //等级
        @InjectView(R.id.tv_data)
        TextView tv_data;//时间
        @InjectView(R.id.tv_lz)
        TextView tv_lz;//是否是楼主
        @InjectView(R.id.tv_replay)
        TextView  tv_replay;//回复的内容
        ViewHolder3(View view) {ButterKnife.inject(this, view);}
    }

}
