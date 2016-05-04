package com.ctrl.forum.ui.activity.store;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.cart.animutils.GoodsAnimUtil;
import com.ctrl.forum.cart.datasave.DemoData;
import com.ctrl.forum.cart.datasave.GoodsDataBaseInterface;
import com.ctrl.forum.cart.datasave.OperateGoodsDataBase;
import com.ctrl.forum.customview.PinnedHeaderListView;
import com.ctrl.forum.entity.FoodModel;
import com.ctrl.forum.entity.FoodTypeModel;
import com.ctrl.forum.ui.adapter.FoodAdapter;
import com.ctrl.forum.ui.adapter.TitleAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/*
* 商城店铺垂直样式 activity
* */

public class StoreShopListVerticalStyleActivitycopy extends AppToolBarActivity implements View.OnClickListener{

    private static final String TAG = "StoreShopListVerticalStyleActivity";
    private boolean isChange;
    /**
     * 菜品数据集
     */
    public List<FoodModel> foodList;
    /**
     * 菜品类型数据集
     */
    public List<FoodTypeModel> foodTypeList;
    /**
     * 菜品类型位置集
     *
     */
    private List<Integer> foodTpyePositionList;

    private ListView lv_listTitle;
    private PinnedHeaderListView lv_Content;
    private FoodAdapter foodAdapter;
    private TitleAdapter titleAdapter;



    public static int SELECTPOSITION = 0;

    /**
     * 数据操作接口
     */
    GoodsDataBaseInterface mGoodsDataBaseInterface = null;
    /**
     * 购物车布局
     */
    @InjectView(R.id.m_list_car_lay)
    RelativeLayout mCarLay;

    /**
     * 商品总价
     */
    @InjectView(R.id.m_list_all_price)
    TextView mListAllPrice;
    /**
     * 物品总数量
     */
    @InjectView(R.id.m_list_num)
    TextView mListAllNum;



    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    FoodTypeModel foodTypeModel = (FoodTypeModel) msg.obj;
                    int typePosition = foodTypeModel.getTypePosition();
                    titleAdapter.setPos(typePosition);
                    titleAdapter.notifyDataSetChanged();
                    break;

                default:
                    break;
            }
        }
    };
    private int itemPos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_shop_vertical_style);
        // 隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initFoodData();
        initView();
        initHttp();
    }

    private void initHttp() {
        titleAdapter.setOnItemClickListener(new TitleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
              SELECTPOSITION=position;
            }

            @Override
            public void onItemLongClick(View v, int position) {

            }
        });

    }

    private void initFoodData() {
        foodList = new ArrayList<FoodModel>();
        foodTypeList = new ArrayList<FoodTypeModel>();
        foodTpyePositionList = new ArrayList<Integer>();

        int itemPosition = 0;// 每个item在list中的位置
        for (char i = 0; i < 13; i++) {
            String type = String.valueOf((char) ('A' + i)); // 菜品类型
            foodTypeList.add(new FoodTypeModel(type, false, itemPosition, i));
            foodTpyePositionList.add(itemPosition);

            // 给菜品类型添加菜品
            for (int j = 0; j < 1; j++) {
                FoodModel foodModel = new FoodModel("松子玉米" + type + " - " + j);
                itemPosition++;
                foodList.add(foodModel);
            }

        }


    }

    private void initView() {
        mGoodsDataBaseInterface = OperateGoodsDataBase.getInstance();
        //清空数据库缓存
        mGoodsDataBaseInterface.deleteAll(this);
      //  mTitle.setText("列表菜单");

/*
        lv_listTitle = (ListView) findViewById(R.id.lv_business_shop_food_orderfoods_foodTypes);
        lv_Content = (PinnedHeaderListView) findViewById(R.id.lv_business_shop_food_orderfoods_foods);*/

        foodAdapter = new FoodAdapter(this, foodList, foodTypeList,
                foodTpyePositionList);
        foodAdapter.setOnItemClickListener(new FoodAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FoodAdapter.ViewHolder holder) {

            }

            @Override
            public void onItemLongClick(FoodAdapter.ViewHolder holder) {

            }

            @Override
            public void onItemJiaClick(FoodAdapter.ViewHolder holder) {
                String numText = holder.item_menu_content_number.getText().toString().trim();
                /** 点击加号之前还没有数据的时候 */
                if (numText.isEmpty() || numText.equals("0")) {
                    Log.e("TAG", "点击获取信息：SELECTPOSITION--" + SELECTPOSITION + "  DemoData.ListMenu_GOODSID[position]--" + DemoData.ListMenu_GOODSID[itemPos]);
                    holder.item_menu_content_number.setVisibility(View.VISIBLE);
                    holder.item_menu_content_number.setText(mGoodsDataBaseInterface.saveGoodsNumber(StoreShopListVerticalStyleActivitycopy.this, SELECTPOSITION, DemoData.ListMenu_GOODSID[(int)holder.item_menu_content_jia.getTag()], "1", DemoData.ListMenu_PPRICE[itemPos]) + "");
                    holder.item_menu_content_number.setVisibility(View.VISIBLE);
                }/** 点击加号之前有数据的时候 */
                else {
                    holder.item_menu_content_number.setText(mGoodsDataBaseInterface.saveGoodsNumber(StoreShopListVerticalStyleActivitycopy.this, SELECTPOSITION, DemoData.ListMenu_GOODSID[(int)holder.item_menu_content_jia.getTag()], String.valueOf(Integer.parseInt(numText) + 1), DemoData.ListMenu_PPRICE[(int)holder.item_menu_content_jia.getTag()]) + "");
                }
                /** 动画 */
              /*  GoodsAnimUtil.setAnim(StoreShopListVerticalStyleActivity.this, holder.item_menu_content_jia, mCarLay);
                GoodsAnimUtil.setOnEndAnimListener(new onEndAnim());*/
                /** 统计购物总数和购物总价 */
            }

            @Override
            public void onItemJianClick(FoodAdapter.ViewHolder holder) {
                String numText = holder.item_menu_content_number.getText().toString().trim();
                holder.item_menu_content_number.setText(mGoodsDataBaseInterface.saveGoodsNumber(StoreShopListVerticalStyleActivitycopy.this, SELECTPOSITION, DemoData.ListMenu_GOODSID[(int)holder.item_menu_content_jia.getTag()], String.valueOf(Integer.parseInt(numText) - 1), DemoData.ListMenu_PPRICE[(int)holder.item_menu_content_jia.getTag()]) + "");
                numText = holder.item_menu_content_number.getText().toString().trim();
                /** 减完之后  数据为0 */
                if (numText.equals("0")) {
                    holder.item_menu_content_number.setVisibility(View.GONE);
                    holder.item_menu_content_jian.setVisibility(View.GONE);
                }
                setAll();

            }
        });



        lv_Content.setAdapter(foodAdapter);
        lv_Content.setOnScrollListener(foodAdapter);
        lv_Content.setPinnedHeaderView(LayoutInflater.from(this).inflate(
                R.layout.listview_head, lv_Content, false));

        lv_Content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });

        titleAdapter = new TitleAdapter(this, foodTypeList, lv_Content);
        lv_listTitle.setAdapter(titleAdapter);

        foodAdapter.setOnPinneChangeListener(new FoodAdapter.OnPinneChangeListener() {

            @Override
            public void onChange(FoodTypeModel foodTypeModel) {
                //这个方法会一直走 所以用一个boolean值限制一下
                if (isChange) {
                    Message message = handler.obtainMessage(1, foodTypeModel);
                    handler.sendMessage(message);
                }

            }

            @Override
            public void onMyScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        isChange = true;

                        break;

                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        isChange = false;
                        break;
                }

            }
        });




        lv_listTitle.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                SELECTPOSITION=arg2;

                FoodTypeModel model = titleAdapter.getItem(arg2);
                itemPos = model.getItemPosition();
             //   Log.i(TAG, "itemPos---------------------" + itemPos);
                lv_Content.setSelection(itemPos);
                titleAdapter.setPos(arg2);
                titleAdapter.notifyDataSetChanged();
                setAll();

            }

        });
    }


    /**
     * 动画结束后，更新所有数量和所有价格
     */
    class onEndAnim implements GoodsAnimUtil.OnEndAnimListener {
        @Override
        public void onEndAnim() {
            setAll();
        }
    }
    /**
     * 点击加号和减号的时候设置总数和总价格
     */
    private void setAll() {
        //设置所有购物数量
        if (mGoodsDataBaseInterface.getSecondGoodsNumberAll(this, SELECTPOSITION) == 0) {
            mListAllNum.setVisibility(View.GONE);
            mListAllPrice.setText("￥0");
            mListAllNum.setText("0");
        } else {
            mListAllPrice.setText("￥" + mGoodsDataBaseInterface.getSecondGoodsPriceAll(this, SELECTPOSITION) + "");
            mListAllNum.setText(mGoodsDataBaseInterface.getSecondGoodsNumberAll(this, SELECTPOSITION) + "");
            mListAllNum.setVisibility(View.VISIBLE);
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
        }


    }


    @Override
    public String setupToolBarTitle() {
        return "小贝商品";
    } 

    @Override
    public boolean setupToolBarLeftButton(ImageView leftButton) {
        leftButton.setImageResource(R.mipmap.jiantou_left_white);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        return true;
    }

    @Override
    public boolean setupToolBarRightText(TextView mRightText) {
        mRightText.setText("店铺详情");
        mRightText.setTextColor(Color.WHITE);
        return true;
    }
}
