package com.ctrl.forum.ui.viewpage;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.beanu.arad.Arad;
import com.ctrl.forum.R;
import com.ctrl.forum.entity.CycleVpEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * ImageView创建工厂
 */
public class ViewFactory {
	/**
	 * 轮播广告图
	 */
	public static void initialize(final Context context,View vhdf,final CycleViewPager cycleViewPager,List<CycleVpEntity> cycentiy) {
		//vhdf = getLayoutInflater().inflate(R.layout.viewpage, null);
		//cycleViewPager=(CycleViewPager)getFragmentManager().findFragmentById(R.id.fragment_cycle_viewpager_content);
		
		List<CycleVpEntity> infos = new ArrayList<CycleVpEntity>();
		List<ImageView> views = new ArrayList<ImageView>();
		for(int i = 0; i < cycentiy.size(); i ++){
			CycleVpEntity info = new CycleVpEntity();
			info.setIurl(cycentiy.get(i).getIurl());
			info.setTitle(cycentiy.get(i).getTitle());
			info.setCurl(cycentiy.get(i).getCurl());
			infos.add(info);
		}
		// 将最后一个ImageView添加进来
		views.add(ViewFactory.getImageView(context, infos.get(infos.size() - 1).getIurl()));
		for (int i = 0; i < infos.size(); i++) {
			views.add(ViewFactory.getImageView(context, infos.get(i).getIurl()));
		}
		// 将第一个ImageView添加进来
		views.add(ViewFactory.getImageView(context, infos.get(0).getIurl()));
		// 设置循环，在调用setData方法前调用
		cycleViewPager.setCycle(true);
		// 在加载数据前设置是否循环
		cycleViewPager.setData(views, infos, new CycleViewPager.ImageCycleViewListener() {
			
			@Override
			public void onImageClick(CycleVpEntity info, int postion, View imageView) {
				// TODO Auto-generated method stub
				if (cycleViewPager.isCycle()) {
					postion = postion - 1;
					/*new AlertDialog(context).builder()
					.setMsg("position:"+postion)
					.setNegativeButton("确定", new OnClickListener() {
						@Override
						public void onClick(View v) {

						}
					}).show();*/
				}
			}
		});
		//设置轮播
		cycleViewPager.setWheel(true);
		// 设置轮播时间，默认5000ms
		cycleViewPager.setTime(2000);
		//设置圆点指示图标组居中显示，默认靠右
		cycleViewPager.setIndicatorCenter();
	}
	
	
	
	
	
	
	
	
	

	/**
	 * 获取ImageView视图的同时加载显示url
	 * 
	 * @return
	 */
	public static ImageView getImageView(Context context, String url) {
		ImageView imageView = (ImageView)LayoutInflater.from(context).inflate(
				R.layout.view_banner, null);
		Arad.imageLoader.load(url).placeholder(R.mipmap.weixin).into(imageView);
		return imageView;
	}
}
