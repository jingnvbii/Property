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

import com.beanu.arad.Arad;
import com.ctrl.forum.R;
import com.ctrl.forum.cart.datasave.OperateGoodsDataBaseStatic;
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
	private int num=0;
	
	private Context context;
	private List<FoodModel> foodList;//��Ʒ����
	private List<FoodTypeModel> foodTypeList;//��Ʒ���ͼ���
	private List<Integer> foodTpyePositionList;//��Ʒ����λ�ü���
	
	private int mLocationPosition = -1;
	private LayoutInflater inflater;
	private OnItemClickListener mOnItemClickListener;
	private OperateGoodsDataBaseStatic mGoodsDataBaseInterface=null;

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

	//定义接口
	public interface OnItemClickListener{
		void onItemJiaClick(ViewHolder v);
		void onItemJianClick(ViewHolder v);
	}

	public void setOnItemClickListener(OnItemClickListener listener){
		this.mOnItemClickListener = listener ;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final int section = getSectionForPosition(position);
		ViewHolder holder=null;
		if(convertView==null){
			convertView=inflater.inflate(R.layout.listview_item,parent,false);
			holder=new ViewHolder(convertView);
			convertView.setTag(holder);
		}else {
			holder=(ViewHolder)convertView.getTag();
		}
		if (getPositionForSection(section) == position) {
			holder.friends_item_header_parent.setVisibility(View.VISIBLE);
			holder.mHeaderText.setText(foodTypeList.get(section).getFoodTypeName());
		} else {
			holder.friends_item_header_parent.setVisibility(View.GONE);
		}
		FoodModel foodModel=foodList.get(position);
		/** 获取存储的商品数量 */
		if (mGoodsDataBaseInterface.getSecondGoodsNumber(context, StoreShopListVerticalStyleActivity.SELECTPOSITION, foodModel.getId()) == 0) {
			holder.item_menu_content_number.setText("");
			holder.item_menu_content_jian.setVisibility(View.GONE);
			holder.item_menu_content_number.setVisibility(View.GONE);
		} else {
			holder.item_menu_content_number.setText("" + mGoodsDataBaseInterface.getSecondGoodsNumber(context, StoreShopListVerticalStyleActivity.SELECTPOSITION , foodModel.getId()));
			holder.item_menu_content_number.setVisibility(View.VISIBLE);
			holder.item_menu_content_jian.setVisibility(View.VISIBLE);
		}

		holder.tv_shop_name.setText(foodModel.getName());
		holder.tv_numbers.setText(foodModel.getSalesVolume());
		holder.tv_vertical_price.setText(foodModel.getSellingPrice()+"/份");
		Arad.imageLoader.load(foodModel.getListImgUrl()).placeholder(R.mipmap.default_error).into(holder.iv_style_img);
		holder.setPosition(position);
		setOnListtener(holder);
		return convertView;
	}

	//触发
	protected void setOnListtener(final ViewHolder holder){
		if(mOnItemClickListener != null){

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

public static class ViewHolder{
		@InjectView(R.id.friends_item_header_text)
		TextView mHeaderText;   //类型名
		@InjectView(R.id.tv_shop_name)
		TextView tv_shop_name;   //商品名
		@InjectView(R.id.tv_numbers)
		TextView tv_numbers;    //销量
		@InjectView(R.id.tv_vertical_price)
		TextView tv_vertical_price;    //价格
		@InjectView(R.id.item_vertical_numbers)
		public TextView item_menu_content_number;    //数量
		@InjectView(R.id.iv_style_img)
		ImageView iv_style_img;    //商品图片
		@InjectView(R.id.item_vertical_jian)
		public ImageView item_menu_content_jian;    //减号
		@InjectView(R.id.item_vertical_jia)
		public ImageView item_menu_content_jia;    //加号
		@InjectView(R.id.friends_item_header_parent)
		LinearLayout friends_item_header_parent;
	private int position;

	public void setPosition(int position){
			this.position=position;
		}
	public int getPosition(){
		return position;
	}
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
