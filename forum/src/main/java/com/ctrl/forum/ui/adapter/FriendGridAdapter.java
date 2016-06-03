package com.ctrl.forum.ui.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.beanu.arad.Arad;
import com.ctrl.forum.R;
import com.ctrl.forum.entity.PostImage;
import com.ctrl.forum.utils.SysUtils;

import java.util.List;

/*
* 朋友圈图片adapater
* */

public class FriendGridAdapter extends BaseAdapter {

	public Activity context;
	public List<PostImage> list;
	public Bitmap bitmaps[];
	private int wh;
	
	public FriendGridAdapter(Activity context,List<PostImage> data) {
		this.context=context;
		this.wh=(SysUtils.getScreenWidth(context)-SysUtils.Dp2Px(context, 99))/3;
		this.list=data;
		
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
	

	@Override
	public View getView(final int position, View view, ViewGroup arg2) {
		Holder holder;
		if (view==null) {
			view=LayoutInflater.from(context).inflate(R.layout.item_gridview, null);
			holder=new Holder();
			holder.imageView=(ImageView) view.findViewById(R.id.imageView);
			holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			view.setTag(holder);
		}else {
			holder= (Holder) view.getTag();
		}

		Arad.imageLoader.load(list.get(position).getImg()).placeholder(R.mipmap.default_error).into(holder.imageView);
		AbsListView.LayoutParams param = new AbsListView.LayoutParams(wh,wh);
        view.setLayoutParams(param);
		return view;
	}
	
	class Holder{
		ImageView imageView;
	}
	
}
