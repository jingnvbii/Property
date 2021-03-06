package com.ctrl.forum.HorzitalGridView.adapter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.ctrl.forum.R;
import com.ctrl.forum.entity.MallKind;
import com.ctrl.forum.ui.fragment.StroeFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于GridView装载数据的适配器
 * 
 * @author xxs
 * 
 */
public class App2Adapter extends BaseAdapter implements OnItemClickListener {
	private StroeFragment fragment;
	private List<MallKind> mList;// 定义一个list对象
	private Context mContext;// 上下文
	public static final int APP_PAGE_SIZE = 10;// 每一页装载数据的大小
	private PackageManager pm;// 定义一个PackageManager对象

	private int page;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	/**
	 * 构造方法
	 *
	 * @param context
	 *            上下文
	 * @param list
	 *            所有APP的集合
	 * @param page
	 *            当前页
	 */
	public App2Adapter(Context context, List<MallKind> list, int page,StroeFragment fragment) {
		mContext = context;
		pm = context.getPackageManager();
		this.page = page;
		mList = new ArrayList<MallKind>();
		this.fragment=fragment;
		// 根据当前页计算装载的应用，每页只装载16个
		int i = page * APP_PAGE_SIZE;// 当前页的起始位置
		int iEnd = i + APP_PAGE_SIZE;// 所有数据的结束位置
		while ((i < list.size()) && (i < iEnd)) {
			mList.add(list.get(i));
			i++;
		}
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.app_item, parent, false);
		}
		MallKind appInfo = mList.get(position);
		ImageView appicon = (ImageView) convertView
				.findViewById(R.id.ivAppIcon);
		TextView appname = (TextView) convertView.findViewById(R.id.tvAppName);
		//appicon.setImageResource(R.mipmap.ic_launcher);
		if(appInfo.getKindIcon()!=null&&!appInfo.getKindIcon().equals("")) {
			Arad.imageLoader.load(appInfo.getKindIcon()).placeholder(R.mipmap.default_error).into(appicon);
		}else {
			appicon.setImageResource(R.mipmap.default_error);
		}
		appname.setText(appInfo.getKindName());
		return convertView;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		int index = this.getPage() * APP_PAGE_SIZE + arg2;

		for (int i = 0; i < APP_PAGE_SIZE; i++) {
			if (i == arg2) {
				fragment.setGridViewItemClick(arg2,mList.get(arg2).getId(),mList.get(arg2).getKindName());
				//MessageUtils.showShortToast(mContext,"fsdfds");
			} else {
				/*arg0.getChildAt(i).setBackgroundColor(Color.argb(0, 0, 0, 0));*/

			}
		}

	}

}
