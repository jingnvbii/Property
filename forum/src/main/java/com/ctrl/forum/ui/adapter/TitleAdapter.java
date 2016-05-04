package com.ctrl.forum.ui.adapter;

import android.content.Context;
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


	private OnItemClickListener mOnItemClickListener;
	//定义接口
	public interface OnItemClickListener{
		void onItemClick(View v, int position);
		void onItemLongClick(View v, int position);
	}

	public void setOnItemClickListener(OnItemClickListener listener){
		this.mOnItemClickListener = listener ;
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
			vh.tv_line_red = (TextView) convertView.findViewById(R.id.tv_line_red);
			vh.ll_title_item = (LinearLayout) convertView.findViewById(R.id.ll_title_item);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		FoodTypeModel model = getItem(position); 

		if (pos==position) {
			vh.tv_line_red.setVisibility(View.VISIBLE);
			vh.title.setTextColor(context.getResources().getColor(R.color.text_red));
			vh.ll_title_item.setBackgroundResource(R.color.white);
		} else {
			vh.tv_line_red.setVisibility(View.GONE);
			vh.title.setTextColor(context.getResources().getColor(R.color.text_black));
			vh.ll_title_item.setBackgroundResource(R.color.gray_store);

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

		setOnListtener(vh);
		return convertView;
	}

	//触发
	protected void setOnListtener(final ViewHolder holder){
		if(mOnItemClickListener != null){
			holder.ll_title_item.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					int layoutPosition = pos;
					mOnItemClickListener.onItemClick(holder.ll_title_item,layoutPosition);
				}
			});
			holder.ll_title_item.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					int layoutPosition = pos;
					mOnItemClickListener.onItemLongClick(holder.ll_title_item,layoutPosition);
					return true;
				}
			});
		}
	}

	class ViewHolder {
		public TextView title;
		public TextView tv_line_red;
		public LinearLayout ll_title_item;
	}

}
