package com.ctrl.forum.ui.activity.Invitation;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.photo.zoom.PhotoView;
import com.ctrl.forum.photo.zoom.ViewPagerFixed;

import java.util.ArrayList;


/**
 * 这个是用于瀑布流影集样式进行图片浏览时的界面
 *
 * @author king
 * @QQ:595163260
 * @version 2014年10月18日  下午11:47:53
 */
public class InvitationPinerestGalleyActivity extends AppToolBarActivity implements View.OnClickListener{
	//获取前一个activity传过来的position
	private int position;
	//当前的位置
	private int location = 0;

	private ArrayList<View> listViews = null;
	private ViewPagerFixed pager;
	private MyPageAdapter adapter;
	private TextView tv_tel;//电话连接
	private PopupWindow popupWindow;
	private TextView tv_titile;

	Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 1:
					int arg0=(int)msg.obj;
					tv_titile.setText((arg0+1)+"/"+listViews.size());

					break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invitation_pinerest_gallery);// 切屏到主界面
		initView();

		// 为发送按钮设置文字
		pager = (ViewPagerFixed) findViewById(R.id.gallery01);
		pager.setOnPageChangeListener(pageChangeListener);
		listViews=new ArrayList<>();
		for(int i=0;i<10;i++){
			ImageView view=new ImageView(this);
			view.setImageResource(R.mipmap.fanbingbing);
			view.setScaleType(ImageView.ScaleType.FIT_XY);
			listViews.add(view);
		}
		tv_titile.setText(1+"/"+listViews.size());
		adapter = new MyPageAdapter(listViews);
		pager.setAdapter(adapter);
	}

	private void initView() {
		tv_tel=(TextView)findViewById(R.id.tv_tel);
		tv_titile=(TextView)findViewById(R.id.tv_titile);
		tv_tel.setOnClickListener(this);
	}

	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		public void onPageSelected(int arg0) {
			location = arg0;
			Toast.makeText(InvitationPinerestGalleyActivity.this,""+arg0,Toast.LENGTH_SHORT).show();
			Message message = myHandler.obtainMessage();
			message.what=1;
			message.obj=arg0;
			myHandler.sendMessage(message);
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		public void onPageScrollStateChanged(int arg0) {

		}
	};
	
	private void initListViews(Bitmap bm) {
		if (listViews == null)
			listViews = new ArrayList<View>();
		PhotoView img = new PhotoView(this);
		img.setBackgroundColor(0xff000000);
		img.setImageBitmap(bm);
		img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		listViews.add(img);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.tv_tel:
				showPupupWindow();
				break;
		}
	}

	private void showPupupWindow() {
		View contentView= LayoutInflater.from(this).inflate(R.layout.popupwindow_tel,null);
		popupWindow = new PopupWindow(contentView,
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
		// 设置SelectPicPopupWindow弹出窗体可点击
		popupWindow.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		popupWindow.setAnimationStyle(R.style.AnimBottom);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x00000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		// 我觉得这里是API的一个bug
		popupWindow.setBackgroundDrawable(dw);

		contentView.setFocusable(true);
		contentView.setFocusableInTouchMode(true);
		popupWindow.setTouchable(true);
         /*
        * 设置popupwindow 点击自身消失
        * */
		contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
			}
		});

		popupWindow.setOutsideTouchable(true);

		popupWindow.setTouchInterceptor(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				Log.i("mengdd", "onTouch : ");

				return false;
				// 这里如果返回true的话，touch事件将被拦截
				// 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
			}
		});
		// 设置好参数之后再show
		popupWindow.showAtLocation(InvitationPinerestGalleyActivity.this.findViewById(R.id.framelayout), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);

	}


	class MyPageAdapter extends PagerAdapter {

		private ArrayList<View> listViews;

		private int size;
		public MyPageAdapter(ArrayList<View> listViews) {
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public void setListViews(ArrayList<View> listViews) {
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public int getCount() {
			return size;
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPagerFixed) arg0).removeView(listViews.get(arg1 % size));
		}

		public void finishUpdate(View arg0) {
		}

		public Object instantiateItem(View arg0, final int arg1) {
			try {
				((ViewPagerFixed) arg0).addView(listViews.get(arg1 % size), 0);

				listViews.get(arg1%size).setOnLongClickListener(new View.OnLongClickListener() {
					@Override
					public boolean onLongClick(View v) {
						Toast.makeText(InvitationPinerestGalleyActivity.this,"点击了第"+arg1 % size+"张图片",Toast.LENGTH_SHORT).show();
						//ImageUtil.saveImageToGallery(this,);
                         showDialog();
						return true;
					}
				});


			} catch (Exception e) {
			}
			return listViews.get(arg1 % size);
		}

		private void showDialog() {
		  AlertDialog.Builder builder=  new AlertDialog.Builder(InvitationPinerestGalleyActivity.this);
			builder.setMessage("保存到本地");
			builder.setPositiveButton("确定", null);
			builder.show();
		}



		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}




}
