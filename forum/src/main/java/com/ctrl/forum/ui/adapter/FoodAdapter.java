package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.cart.datasave.DemoData;
import com.ctrl.forum.cart.datasave.GoodsDataBaseInterface;
import com.ctrl.forum.cart.datasave.OperateGoodsDataBase;
import com.ctrl.forum.customview.PinnedHeaderListView;
import com.ctrl.forum.entity.FoodModel;
import com.ctrl.forum.entity.FoodTypeModel;
import com.ctrl.forum.ui.activity.store.StoreShopListVerticalStyleActivity;

import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class FoodAdapter extends BaseAdapter implements SectionIndexer, PinnedHeaderListView.PinnedHeaderAdapter, OnScrollListener {
	private static final String TAG = "FoodAdapter";
	
	private OnPinneChangeListener changeListener;
	public  boolean isChangeable;
	
	private Context context;
	private List<FoodModel> foodList;//��Ʒ����
	private List<FoodTypeModel> foodTypeList;//��Ʒ���ͼ���
	private List<Integer> foodTpyePositionList;//��Ʒ����λ�ü���
	
	private int mLocationPosition = -1;
	private LayoutInflater inflater;
	private OnItemClickListener mOnItemClickListener;
	private int mPosition=-1;

	public void setChangeable(boolean isChangeable) {
		this.isChangeable = isChangeable;
	}
	public FoodAdapter(Context context, List<FoodModel> foodList, List<FoodTypeModel> foodTypeList, List<Integer> foodTpyePositionList) {
		super();
		this.context = context;
		this.foodList = foodList;
		this.foodTypeList = foodTypeList;
		this.foodTpyePositionList = foodTpyePositionList;
		
		inflater = LayoutInflater.from(context);
		mGoodsDataBaseInterface = OperateGoodsDataBase.getInstance();
	}


	/** 数据操作接口 */
	GoodsDataBaseInterface mGoodsDataBaseInterface = null;

	//定义接口
	public interface OnItemClickListener{
		void onItemClick(ViewHolder holder);
		void onItemLongClick(ViewHolder holder);
		void onItemJiaClick(ViewHolder holder);
		void onItemJianClick(ViewHolder holder);
	}

	public void setOnItemClickListener(OnItemClickListener listener){
		this.mOnItemClickListener = listener ;
	}

    public void setposition(int position){
		this.mPosition=position;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() { 
		return foodList.size();
	}

	@Override
	public FoodModel getItem(int position) { 
		return foodList.get(position);
	}

	@Override
	public long getItemId(int position) { 
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int section = getSectionForPosition(position);
		/*if (convertView == null) {
			convertView = inflater.inflate(R.layout.listview_item, null);
		}
		LinearLayout mHeaderParent = (LinearLayout) convertView.findViewById(R.id.friends_item_header_parent);
		TextView mHeaderText = (TextView) convertView.findViewById(R.id.friends_item_header_text);*/
		ViewHolder holder=null;
		if(convertView==null){
			convertView= LayoutInflater.from(context).inflate(R.layout.listview_item,parent,false);
			holder=new ViewHolder(convertView);
			holder.item_menu_content_jia.setTag(position);

			convertView.setTag(holder);
		}else {
			holder=(ViewHolder)convertView.getTag();
		}


		if (getPositionForSection(section) == position) {
			holder.mHeaderParent.setVisibility(View.VISIBLE);
			holder.mHeaderText.setText(foodTypeList.get(section).getFoodTypeName());
		} else {
			holder.mHeaderParent.setVisibility(View.GONE);
		}
		//TextView textView = (TextView) convertView.findViewById(R.id.tv_shop_name);
		final String foodName=foodList.get(position).getName();
		   /** 获取存储的商品数量 */
		   if (mGoodsDataBaseInterface.getSecondGoodsNumber(context, StoreShopListVerticalStyleActivity.SELECTPOSITION, DemoData.ListMenu_GOODSID[(int)holder.item_menu_content_jia.getTag()]) == 0) {
			   holder.item_menu_content_number.setText("");
			   holder.item_menu_content_number.setVisibility(View.GONE);
			   holder.item_menu_content_jian.setVisibility(View.GONE);
		   } else {
			   holder.item_menu_content_number.setText("" + mGoodsDataBaseInterface.getSecondGoodsNumber(context, StoreShopListVerticalStyleActivity.SELECTPOSITION, DemoData.ListMenu_GOODSID[(int)holder.item_menu_content_jia.getTag()]));
			   holder.item_menu_content_number.setVisibility(View.VISIBLE);
			   holder.item_menu_content_jian.setVisibility(View.VISIBLE);
		   }


		/*textView.setText(foodName);
		textView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(context, foodName, Toast.LENGTH_SHORT).show();

			}
		});*/

		setOnListtener(holder);
		return convertView;
	}
   //触发监听
	private void setOnListtener( final ViewHolder holder) {
		if(mOnItemClickListener != null){
		/*	holder.view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mOnItemClickListener.onItemClick(holder);
				}
			});
			holder.view.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					mOnItemClickListener.onItemLongClick(holder);
					return true;
				}
			});*/
			holder.item_menu_content_jia.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mOnItemClickListener.onItemJiaClick(holder);
				}
			});
			holder.item_menu_content_jian.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mOnItemClickListener.onItemJianClick(holder);
				}
			});
		}

	}


public	static class ViewHolder{

		@InjectView(R.id.friends_item_header_parent)
		        LinearLayout mHeaderParent;
		@InjectView(R.id.friends_item_header_text)//标题
				TextView mHeaderText;
		@InjectView(R.id.item_menu_content_number)//标题
			public 	TextView item_menu_content_number;
		@InjectView(R.id.item_menu_content_jia)//加号
		public	ImageView item_menu_content_jia;
		@InjectView(R.id.item_menu_content_jian)//减号
		public	ImageView item_menu_content_jian;



		ViewHolder(View view) {
			ButterKnife.inject(this, view);
		}
	}


	
	
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) { 
		if(changeListener!=null){
			changeListener.onMyScrollStateChanged(view, scrollState);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) { 
		if (view instanceof PinnedHeaderListView) {
			((PinnedHeaderListView) view).configureHeaderView(firstVisibleItem);
		}
	}

	@Override
	public int getPinnedHeaderState(int position) {
		int realPosition = position;
		if (realPosition < 0 || (mLocationPosition != -1 && mLocationPosition == realPosition)) {
			return PINNED_HEADER_GONE;
		}
		mLocationPosition = -1;
		int section = getSectionForPosition(realPosition);
		int nextSectionPosition = getPositionForSection(section + 1);
		if (nextSectionPosition != -1 && realPosition == nextSectionPosition - 1) {
			return PINNED_HEADER_PUSHED_UP;
		}
		return PINNED_HEADER_VISIBLE;
	}

	@Override
	public void configurePinnedHeader(View header, int position, int alpha) { 
		int realPosition = position;
		int section = getSectionForPosition(realPosition);
//		Log.i(TAG, "section------------------------"+foodTypeList.get(section).getFoodTypeName());
		
		if (isChangeable) {// ֻ����Activity������˴˼�������һ�����⵽���˶��˵�ʱ�򣬲Ŵ�������
			onChange(foodTypeList.get(section));
		}
		 
		
		FoodTypeModel model=(FoodTypeModel) getSections()[section];
		 
		((TextView) header.findViewById(R.id.friends_list_header_text)).setText(model.getFoodTypeName());
		
	}

	@Override
	public Object[] getSections() { 
		return foodTypeList.toArray();
	}

	@Override
	public int getPositionForSection(int section) {
		if (section < 0 || section >= foodTypeList.size()) {
			return -1;
		} 
		return foodTpyePositionList.get(section);
	}

	@Override
	public int getSectionForPosition(int position) {
		if (position < 0 || position >= getCount()) {
			return -1;
		}
		 //ע����������ķ���ֵ�������index<0ʱ������-index-2��ԭ��
        //����Arrays.binarySearch�������������������У��շ������������е���������ڣ��շ��ص�һ������������ĸ���-1
        //���ûŪ���ף����Լ���鿴api
		int index = Arrays.binarySearch(foodTpyePositionList.toArray(), position);
		return index >= 0 ? index : -index - 2;
	}
	
	// ------------------------------------------
	public interface OnPinneChangeListener {
		abstract void onChange(FoodTypeModel foodTypeModel);
		abstract void onMyScrollStateChanged(AbsListView view, int scrollState);
		
	}

	public void setOnPinneChangeListener(OnPinneChangeListener listener) {
		changeListener = listener;
		isChangeable = true;
	}

	private void onChange(FoodTypeModel foodTypeModel) {
		if (changeListener != null) {
			changeListener.onChange(foodTypeModel);
		}
	}

}
