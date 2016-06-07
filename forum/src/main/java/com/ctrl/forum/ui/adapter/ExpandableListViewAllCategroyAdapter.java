package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.customview.GridViewForScrollView;
import com.ctrl.forum.entity.Category;
import com.ctrl.forum.ui.activity.Invitation.InvitationPullDownActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 所有分类展开列表 adapter
 * Created by jason on 2016/4/8.
 */
public class ExpandableListViewAllCategroyAdapter extends BaseExpandableListAdapter implements AdapterView.OnItemClickListener {
    private  Context mContext;
    private LayoutInflater layoutInflater;
    private GridViewForScrollView grid;
    private ArrayAdapter<String> adapter;
    private List<String> listChildStr;
    private List<String> listGroupStr;
    private int GROUP_POSITION;

    public ExpandableListViewAllCategroyAdapter(Context context){
        this.mContext=context;
    }

    private List<Category> list;

    public void setList(List<Category>list){
        this.list=list;
        initData();
        notifyDataSetChanged();
    }

    private void initData() {
        listGroupStr=new ArrayList<>();
        for(int i=0;i<list.size();i++){
            listGroupStr.add(list.get(i).getName());
        }
    }


    @Override
    public int getGroupCount() {
        return list==null?0:list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        /*
        * 必须为1，否则数据重复
        * */
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        // Layout parameters for the ExpandableListView
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView text = new TextView(mContext);
        text.setLayoutParams(lp);
        // Center the text vertically
        text.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        // Set the text starting position
        text.setPadding(20, 10,0, 10);
        text.setTextColor(Color.BLACK);
        text.setTextSize(16);
        text.setText(listGroupStr.get(groupPosition));
        return text;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        listChildStr=new ArrayList<>();
        for(int i=0;i<list.get(groupPosition).getCategorylist().size();i++){
            listChildStr.add(list.get(groupPosition).getCategorylist().get(i).getName());
        }
        layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup item = (ViewGroup)layoutInflater.inflate(R.layout.gridview_child_all_category, null); //载入gridview布局
        grid  = (GridViewForScrollView) item.findViewById(R.id.gridview_child);// 获取girdview的节点
        grid.setNumColumns(6);// 设置每行列数
        grid.setGravity(Gravity.CENTER);// 位置居中
        grid.setVerticalSpacing(10);
        adapter=new ArrayAdapter<String>(mContext,R.layout.child_item,listChildStr);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InvitationPullDownActivity.isFromSelcet=true;
                InvitationPullDownActivity activity=(InvitationPullDownActivity)mContext;
                activity.request(groupPosition,position,list.get(groupPosition).getCategorylist().get(position).getId());
            }
        });
        return item;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
