package com.ctrl.android.yinfeng.ui.adressbook.tree;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ctrl.android.yinfeng.R;

import java.util.ArrayList;

public class TreeListAdapter extends BaseAdapter{

	private LayoutInflater layoutInflater;
	private Context context;
	private ArrayList<TreeItem> list;

	public TreeListAdapter(Context cxt){
		this.context = cxt;
		layoutInflater = LayoutInflater.from(context);
	}

	public void setList(ArrayList<TreeItem> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
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
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.tree_list_item, null);

			holder.category_1_layout = (LinearLayout) convertView.findViewById(R.id.category_1_layout);
			holder.category_1_im = (ImageView) convertView.findViewById(R.id.category_1_im);
			holder.category_1_tv = (TextView) convertView.findViewById(R.id.category_1_tv);

			holder.category_2_layout = (LinearLayout) convertView.findViewById(R.id.category_2_layout);
			holder.category_2_im = (ImageView) convertView.findViewById(R.id.category_2_im);
			holder.category_2_tv = (TextView) convertView.findViewById(R.id.category_2_tv);

			holder.category_3_layout = (LinearLayout) convertView.findViewById(R.id.category_3_layout);
			holder.category_3_im = (ImageView) convertView.findViewById(R.id.category_3_im);
			holder.category_3_tv = (TextView) convertView.findViewById(R.id.category_3_tv);
			holder.grade_3_tv = (TextView) convertView.findViewById(R.id.grade_3_tv);
			holder.iv_telephone = (ImageView) convertView.findViewById(R.id.iv_telephone);

			holder.category_4_layout = (LinearLayout) convertView.findViewById(R.id.category_4_layout);
			holder.category_4_im = (ImageView) convertView.findViewById(R.id.category_4_im);
			holder.category_4_tv = (TextView) convertView.findViewById(R.id.category_4_tv);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final TreeItem item = list.get(position);

		if(item.getCategory() == 1){
			if(item.isShow()){
				holder.category_1_layout.setVisibility(View.VISIBLE);
				holder.category_2_layout.setVisibility(View.GONE);
				holder.category_3_layout.setVisibility(View.GONE);
				holder.category_4_layout.setVisibility(View.GONE);

				holder.category_1_tv.setText(item.getName());

				if(item.isShowNext()){
					holder.category_1_im.setImageResource(R.mipmap.tongxunlu_xiala);
				} else {
					holder.category_1_im.setImageResource(R.mipmap.tongxunlu_right);
				}

			}

		} else if(item.getCategory() == 2){
			if(item.isShow()){
				holder.category_1_layout.setVisibility(View.GONE);
				holder.category_2_layout.setVisibility(View.VISIBLE);
				holder.category_3_layout.setVisibility(View.GONE);
				holder.category_4_layout.setVisibility(View.GONE);

				holder.category_2_tv.setText(item.getName());

				if(item.isShowNext()){
					holder.category_2_im.setImageResource(R.mipmap.tongxunlu_xiala);
				} else {
					holder.category_2_im.setImageResource(R.mipmap.tongxunlu_right);
				}

			}

		} else if(item.getCategory() == 3){
			if(item.isShow()){
				holder.category_1_layout.setVisibility(View.GONE);
				holder.category_2_layout.setVisibility(View.GONE);
				holder.category_3_layout.setVisibility(View.VISIBLE);
				holder.category_4_layout.setVisibility(View.GONE);

				holder.category_3_tv.setText(item.getName());
				holder.iv_telephone.setImageResource(R.mipmap.iconfont_dianhua);

				//联系人职位级别（0：经理、1：主管、2：普通员工）
			/*	if(item.getContactorGrade().equals("0")){
					holder.grade_3_tv.setText("经理");
				}else if(item.getContactorGrade().equals("1")){
					holder.grade_3_tv.setText("主管");
				}else if(item.getContactorGrade().equals("2")){
					holder.grade_3_tv.setText("普通员工");
				}else {
					holder.grade_3_tv.setText("员工");

				}*/
				if(item.isShowNext()){
					holder.category_3_im.setImageResource(R.mipmap.hotline_default_head_img);
				} else {
					holder.category_3_im.setImageResource(R.mipmap.hotline_default_head_img);
				}

			}

		} else if(item.getCategory() == 4){
			if(item.isShow()){
				holder.category_1_layout.setVisibility(View.GONE);
				holder.category_2_layout.setVisibility(View.GONE);
				holder.category_3_layout.setVisibility(View.GONE);
				holder.category_4_layout.setVisibility(View.VISIBLE);

				holder.category_4_tv.setText(item.getName());

			}

		}

			//holder.title.setText(appLists.get(position).get("name").toString());

		return convertView;
	}

	public class ViewHolder {

		private LinearLayout category_1_layout;
		private ImageView category_1_im;
		private TextView category_1_tv;

		private LinearLayout category_2_layout;
		private ImageView category_2_im;
		private TextView category_2_tv;

		private LinearLayout category_3_layout;
		private ImageView category_3_im;
		private TextView category_3_tv;
		private TextView grade_3_tv;
		private ImageView iv_telephone;

		private LinearLayout category_4_layout;
		private ImageView category_4_im;
		private TextView category_4_tv;

	}

}
