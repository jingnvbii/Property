package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.customview.PinnedHeaderListView;
import com.ctrl.forum.entity.FoodTypeModel;

import java.util.List;

public class TitleAdapter extends BaseAdapter {
	private static final String TAG = "TitleAdapter";

	private Context context;
	private List<FoodTypeModel> foodTypeList;
	private PinnedHeaderListView lv_Content;
	private int pos;

	public TitleAdapter(Context context, List<FoodTypeModel> foodTypeList,PinnedHeaderListView lv_Content) {
		super();
		this.context = context;
		this.foodTypeList = foodTypeList;
		this.lv_Content=lv_Content;
	}
	public void setPos(int pos) {
		this.pos = pos;
	}

	@Override
	public int getCount() {
		return foodTypeList.size();
	}

	@Override
	public FoodTypeModel getItem(int position) {
		return foodTypeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item, null);
			vh.title = (TextView) convertView.findViewById(R.id.title);
			vh.tv_line_red=(TextView)convertView.findViewById(R.id.tv_line_red);
			vh.ll_title_item=(LinearLayout)convertView.findViewById(R.id.ll_title_item);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		FoodTypeModel model = getItem(position); 

		if (pos==position) {
			vh.title.setTextColor(context.getResources().getColor(R.color.text_red));
			vh.tv_line_red.setVisibility(View.VISIBLE);
			vh.ll_title_item.setBackgroundColor(Color.WHITE);
		} else {
			vh.title.setTextColor(context.getResources().getColor(R.color.text_black));
			vh.tv_line_red.setVisibility(View.GONE);
			vh.ll_title_item.setBackgroundColor(Color.parseColor("#efefef"));
		}
		final String foodTypeName=model.getFoodTypeName();
		final int itemPos=model.getItemPosition();
		vh.title.setText(foodTypeName); 
//		vh.title.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) { 
//				changeTextColor(foodTypeName); 
//				
//				lv_Content.setSelection(itemPos);
//			}
//		});
		return convertView;
	}

	class ViewHolder {
		public TextView title;
		public TextView tv_line_red;
		public LinearLayout ll_title_item;
	}

}
