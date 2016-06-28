package com.ctrl.forum.ui.activity.store;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.AnimUtil;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.cart.animutils.GoodsAnimUtil;
import com.ctrl.forum.cart.datasave.GoodsBean;
import com.ctrl.forum.cart.datasave.GoodsDataBaseInterface;
import com.ctrl.forum.cart.datasave.OperateGoodsDataBase;
import com.ctrl.forum.cart.datasave.OperateGoodsDataBaseStatic;
import com.ctrl.forum.customview.PinnedHeaderListView;
import com.ctrl.forum.dao.MallDao;
import com.ctrl.forum.entity.Company;
import com.ctrl.forum.entity.FoodModel;
import com.ctrl.forum.entity.FoodTypeModel;
import com.ctrl.forum.entity.ProductCategroy;
import com.ctrl.forum.ui.adapter.CartPopupWindowListViewAdapter;
import com.ctrl.forum.ui.adapter.FoodAdapter;
import com.ctrl.forum.ui.adapter.TitleAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/*
* 商城店铺垂直样式 activity
* */

public class StoreShopListVerticalStyleActivity extends AppToolBarActivity implements View.OnClickListener {

    /**
     * 购物车布局
     */
    @InjectView(R.id.m_list_car_lay)
    RelativeLayout mCarLay;

    @InjectView(R.id.m_list_car_vertical_style)//购物车图片
            ImageView m_list_car_vertical_style;
    @InjectView(R.id.m_list_submit_vertical_style)//提交订单按钮
            Button m_list_submit_vertical_style;

    @InjectView(R.id.iv_style_img)//店铺图片
    ImageView iv_style_img;
    @InjectView(R.id.tv_shop_name)//店铺名称
    TextView tv_shop_name;
    @InjectView(R.id.tv_time)//营业时间
    TextView tv_time;
    @InjectView(R.id.ratingBar)//评价等级
    RatingBar ratingBar;
    @InjectView(R.id.tv_store_information)//店铺公告
    TextView tv_store_information;
    @InjectView(R.id.iv_store_information_close)//关闭公告
    ImageView iv_store_information_close;
    @InjectView(R.id.rl_store_information)//公告布局
    RelativeLayout rl_store_information;

    @InjectView(R.id.et_vertical_style_search)//搜索栏
    EditText et_vertical_style_search;
    @InjectView(R.id.tv_vertical_style_search)//搜索
    TextView tv_vertical_style_search;


    /**
     * 商品总价
     */
    @InjectView(R.id.m_list_all_price_store_vertical)
    TextView mListAllPrice;
    /**
     * 物品总数量
     */
    @InjectView(R.id.m_list_num_store_vertical)
    TextView mListAllNum;

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
     */
    private List<Integer> foodTpyePositionList;

    private ListView lv_listTitle;
    private PinnedHeaderListView lv_Content;
    private FoodAdapter foodAdapter;
    private TitleAdapter titleAdapter;

    public static int SELECTPOSITION = 0;//一级列表下标值
    public static int FIRSTPOSITION = 0;//一级列表下标值
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
                case 2:
                    tv.setText(name);
                    break;

                default:
                    break;
            }
        }
    };
    private MallDao mdao;
    private List<ProductCategroy> listProductCategroy;
    private GoodsDataBaseInterface mGoodsDataBaseInterface;
    private Context mContext;
    private PopupWindow popupWindow;
    private ArrayList<GoodsBean> listGoodsBean;
    private ArrayList<String> listNameStr;
    private TextView m_list_num_popup;
    private TextView m_list_all_price_popup;
    private Company company;
    private Button m_list_submit_popup;
    private String name;
    private TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_shop_vertical_style);
        // 隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.inject(this);
        mContext = this;
        initView();
        initFoodData();
    }

    private void initView() {
        tv_vertical_style_search.setOnClickListener(this);
        et_vertical_style_search.addTextChangedListener(watcher);
        mGoodsDataBaseInterface = OperateGoodsDataBase.getInstance();
        //清空数据库缓存
        mGoodsDataBaseInterface.deleteAll(mContext);
        lv_listTitle = (ListView) findViewById(R.id.lv_business_shop_food_orderfoods_foodTypes);
        lv_Content = (PinnedHeaderListView) findViewById(R.id.lv_business_shop_food_orderfoods_foods);
        m_list_car_vertical_style.setOnClickListener(this);
        m_list_submit_vertical_style.setOnClickListener(this);
        iv_store_information_close.setOnClickListener(this);
/*
        Arad.imageLoader.load(getIntent().getStringExtra("url")).placeholder(R.mipmap.default_error).into(iv_style_img);
        tv_shop_name.setText(getIntent().getStringExtra("name"));
        tv_time.setText("营业时间 " + getIntent().getStringExtra("startTime") + "-" + getIntent().getStringExtra("endTime"));
        if(getIntent().getStringExtra("levlel")!=null)
        ratingBar.setRating(Float.parseFloat(getIntent().getStringExtra("levlel"))/2);*/

        lv_Content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(StoreShopListVerticalStyleActivity.this, StoreCommodityDetailActivity.class);
                intent.putExtra("id", foodList.get(position).getId());
                intent.putExtra("name", foodList.get(position).getName());
                intent.putExtra("categroyName", getCategroyName(foodList.get(position).getId()));
                startActivityForResult(intent, 500);
                AnimUtil.intentSlidIn(StoreShopListVerticalStyleActivity.this);
            }
        });

    }

    private String getCategroyName(String id) {
        String name=null;
        for(int i=0;i<listProductCategroy.size();i++){
            for(int j=0;j<listProductCategroy.get(i).getProductList().size();j++){
                if(id.equals(listProductCategroy.get(i).getProductList().get(j).getId())){
                    name=listProductCategroy.get(i).getName();
                }
            }
        }
        return name;
    }


    private TextWatcher watcher = new TextWatcher() {
        //文字变化时
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            if(s.length()==0){
                tv_vertical_style_search.setVisibility(View.GONE);
            }else {
                tv_vertical_style_search.setVisibility(View.VISIBLE);
            }


        }
        //文字变化前

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
            if(s.length()==0){
                tv_vertical_style_search.setVisibility(View.GONE);
            }else {
                tv_vertical_style_search.setVisibility(View.VISIBLE);
            }

        }

        //文字变化后
        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            if (s.length() == 0&&tv_vertical_style_search.getVisibility()==View.VISIBLE) {
                tv_vertical_style_search.setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==500&&resultCode==112){
            lv_Content.setAdapter(foodAdapter);
            setAll();
        }
    }

    public void initFoodData() {
        mdao = new MallDao(this);
        showProgress(true);
        mdao.requestProductCategroy(getIntent().getStringExtra("id"), "1");
        mdao.requestCompanysDetails(Arad.preferences.getString("memberId"), getIntent().getStringExtra("id"));

        foodList = new ArrayList<FoodModel>();
        foodTypeList = new ArrayList<FoodTypeModel>();
        foodTpyePositionList = new ArrayList<Integer>();

    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        showProgress(false);
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        showProgress(false);
        if (requestCode == 002) {
          //  MessageUtils.showShortToast(this, "获取店铺详情成功");
            showProgress(false);
            company = mdao.getCompany();
            tv_store_information.setText(company.getNotice());
            tv_store_information.requestFocus();
            Arad.imageLoader.load(company.getImg()).placeholder(R.mipmap.default_error).into(iv_style_img);
            tv_shop_name.setText(company.getName());
            if(getIntent().getStringExtra("startTime")!=null&&getIntent().getStringExtra("endTime")!=null) {
                tv_time.setText("营业时间 " + getIntent().getStringExtra("startTime") + "-" + getIntent().getStringExtra("endTime"));
            }else {
                tv_time.setVisibility(View.GONE);
            }
            if(company.getEvaluatLevel()!=null) {
                ratingBar.setRating(Float.parseFloat(company.getEvaluatLevel()) );
            }else {
                ratingBar.setRating(Float.parseFloat("0"));
            }
            name=company.getName();
            handler.sendEmptyMessage(2);
        }



        if (requestCode == 9) {
          //  MessageUtils.showShortToast(this, "获取铺商品分类以及分类下的商品列表成功");
            listProductCategroy = mdao.getListProductCategroy();
            int itemPosition = 0;// 每个item在list中的位置
            for (int i = 0; i < listProductCategroy.size(); i++) {
                foodTypeList.add(new FoodTypeModel(listProductCategroy.get(i).getName(), false, itemPosition, i));
                foodTpyePositionList.add(itemPosition);
                for (int j = 0; j < listProductCategroy.get(i).getProductList().size(); j++) {
                    FoodModel foodModel = new FoodModel();
                    foodModel.setId(listProductCategroy.get(i).getProductList().get(j).getId());
                    foodModel.setName(listProductCategroy.get(i).getProductList().get(j).getName());
                    foodModel.setSalesVolume(listProductCategroy.get(i).getProductList().get(j).getSalesVolume());
                    foodModel.setSellingPrice(listProductCategroy.get(i).getProductList().get(j).getSellingPrice());
                    foodModel.setStock(listProductCategroy.get(i).getProductList().get(j).getStock());
                    foodModel.setListImgUrl(listProductCategroy.get(i).getProductList().get(j).getListImgUrl());
                    itemPosition++;
                    foodList.add(foodModel);
                }
            }
            // Log.i("tag", "foodTypeList activity---" + foodTypeList.size());
            foodAdapter = new FoodAdapter(this, foodList, foodTypeList,
                    foodTpyePositionList);
            lv_Content.setAdapter(foodAdapter);
            lv_Content.setOnScrollListener(foodAdapter);
            lv_Content.setPinnedHeaderView(LayoutInflater.from(this).inflate(
                    R.layout.listview_head, lv_Content, false));

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
                    FoodTypeModel model = titleAdapter.getItem(arg2);
                    Integer itemPos = model.getItemPosition();
                    // Log.i("tag", "itemPos---------------------" + itemPos);
                    SELECTPOSITION = arg2;
                    //  Log.i("tag", "SELECTPOSITION _listview---------------------" + SELECTPOSITION);
                    lv_Content.setSelection(itemPos);
                    titleAdapter.setPos(arg2);
                    titleAdapter.notifyDataSetChanged();

                }

            });
            foodAdapter.setOnItemClickListener(new FoodAdapter.OnItemClickListener() {

                @Override
                public void onItemJiaClick(FoodAdapter.ViewHolder holder) {
                    String nums = holder.item_menu_content_number.getText().toString().trim();
                    if (nums.isEmpty() || nums.equals("0")) {
                        if(foodList.get(holder.getPosition()).getStock().equals("0")){
                            MessageUtils.showShortToast(StoreShopListVerticalStyleActivity.this,"库存不足");
                            return;
                        }
                        holder.item_menu_content_jian.setVisibility(View.VISIBLE);
                      //  Log.i("tag", "foodList-id--" + foodList.get(holder.getPosition()).getId());
                    //    Log.i("tag", "price---" + foodList.get(holder.getPosition()).getSellingPrice());
                    //  Log.i("tag", "price---" + foodList.get(holder.getPosition()).getName());

                      //  holder.item_menu_content_number.setText(mGoodsDataBaseInterface.saveGoodsNumber(mContext, SELECTPOSITION, foodList.get(holder.getPosition()).getId(), "1", foodList.get(holder.getPosition()).getSellingPrice()) + "");
                       holder.item_menu_content_number.setText(mGoodsDataBaseInterface.saveGoodsNumber(mContext, SELECTPOSITION,
                               foodList.get(holder.getPosition()).getId(), "1",
                               Float.parseFloat(foodList.get(holder.getPosition()).getSellingPrice()+"") ,
                               foodList.get(holder.getPosition()).getName() + "", foodList.get(holder.getPosition()).getStock()+"")+"");
                        holder.item_menu_content_number.setVisibility(View.VISIBLE);
                    }// 点击加号之前有数据的时候
                    else {
                        if((Integer.parseInt(nums)+1)>Integer.parseInt(foodList.get(holder.getPosition()).getStock())){
                            MessageUtils.showShortToast(StoreShopListVerticalStyleActivity.this,"库存不足");
                            return;
                        }
                        holder.item_menu_content_number.setText(mGoodsDataBaseInterface.saveGoodsNumber(mContext, SELECTPOSITION,
                                foodList.get(holder.getPosition()).getId(), String.valueOf(Integer.parseInt(nums) + 1),
                                Float.parseFloat(foodList.get(holder.getPosition()).getSellingPrice()+""),
                                foodList.get(holder.getPosition()).getName() + "",foodList.get(holder.getPosition()).getStock()+"")+"");
                        // holder.item_menu_content_number.setText(mGoodsDataBaseInterface.saveGoodsNumber(mContext, SELECTPOSITION, foodList.get(holder.getPosition()).getId(), String.valueOf(Integer.parseInt(nums) + 1), foodList.get(holder.getPosition()).getSellingPrice()) + "");
                    }



                    //动画
                    GoodsAnimUtil.setAnim(StoreShopListVerticalStyleActivity.this, holder.item_menu_content_jia, mCarLay);
                    GoodsAnimUtil.setOnEndAnimListener(new onEndAnim());
                    // 统计购物总数和购物总价
                }

                @Override
                public void onItemJianClick(FoodAdapter.ViewHolder holder) {
                    String nums = holder.item_menu_content_number.getText().toString().trim();
                  //  holder.item_menu_content_number.setText(mGoodsDataBaseInterface.saveGoodsNumber(mContext, SELECTPOSITION, foodList.get(holder.getPosition()).getId(), String.valueOf(Integer.parseInt(nums) - 1), foodList.get(holder.getPosition()).getSellingPrice()) + "");
                    holder.item_menu_content_number.setText(mGoodsDataBaseInterface.saveGoodsNumber(mContext, SELECTPOSITION,
                            foodList.get(holder.getPosition()).getId(),
                            String.valueOf(Integer.parseInt(nums) - 1),
                            Float.parseFloat(foodList.get(holder.getPosition()).getSellingPrice())
                            ,
                            foodList.get(holder.getPosition()).getName(),foodList.get(holder.getPosition()).getStock())+"");
                    nums = holder.item_menu_content_number.getText().toString().trim();
                    // 减完之后  数据为0
                    if (nums.equals("0")) {
                        holder.item_menu_content_number.setVisibility(View.GONE);
                        holder.item_menu_content_jian.setVisibility(View.GONE);
                    }
                    setAll();
                }
            });

        }

    }

    @Override
    public void onClick(View v) {
        Intent intent=null;
        switch (v.getId()){
            case R.id.tv_horzital_style_search:
                 intent=new Intent(this,StoreSearchCommodityActivity.class);
                intent.putExtra("keyword", et_vertical_style_search.getText().toString().trim());
                intent.putExtra("companyId", getIntent().getStringExtra("id"));
                intent.addFlags(11);
                startActivity(intent);
                AnimUtil.intentSlidIn(this);
                break;
            case R.id.m_list_submit_popup:
                intent=new Intent(StoreShopListVerticalStyleActivity.this,StoreOrderDetailActivity.class);
                intent.putExtra("companyId",getIntent().getStringExtra("id"));
                startActivity(intent);
                AnimUtil.intentSlidIn(StoreShopListVerticalStyleActivity.this);
                popupWindow.dismiss();
                break;
            case R.id.iv_store_information_close:
                rl_store_information.setVisibility(View.GONE);
                break;
            case R.id.m_list_car_vertical_style:
                if(listGoodsBean!=null){
                    listGoodsBean.clear();
                }
                if(!mListAllNum.getText().toString().equals("0")) {
                    showCartPopupWindow(v);
                }
                break;
            case R.id.m_list_submit_vertical_style:
                if(mListAllNum.getText().toString().equals("0")) {
                    MessageUtils.showShortToast(StoreShopListVerticalStyleActivity.this,"购物车还是空的！");
                }else {
                    intent=new Intent(StoreShopListVerticalStyleActivity.this,StoreOrderDetailActivity.class);
                    intent.putExtra("companyId",getIntent().getStringExtra("id"));
                    startActivity(intent);
                    AnimUtil.intentSlidIn(StoreShopListVerticalStyleActivity.this);
                }

                break;
        }

    }

    private void showCartPopupWindow(View v) {
        View contentView= LayoutInflater.from(this).inflate(R.layout.popupwindow_cart_vertical_style,null);

        popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT, true);

        final ListView lv_cart_popup=(ListView)contentView.findViewById(R.id.lv_cart_popup);
         m_list_num_popup = (TextView) contentView.findViewById(R.id.m_list_num_popup);
        m_list_all_price_popup = (TextView) contentView.findViewById(R.id.m_list_all_price_popup);
        m_list_submit_popup = (Button) contentView.findViewById(R.id.m_list_submit_popup);
        TextView tv_cart_popup_delete = (TextView) contentView.findViewById(R.id.tv_cart_popup_delete);
        listGoodsBean= OperateGoodsDataBaseStatic.getSecondGoodsTypeList(mContext);
        //获取数据
        setData();
        // 设置SelectPicPopupWindow的View
        popupWindow.setContentView(contentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        popupWindow.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        popupWindow.setAnimationStyle(R.style.AnimBottom);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(dw);

        m_list_submit_popup.setOnClickListener(this);

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
        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
        m_list_num_popup.setText(mGoodsDataBaseInterface.getSecondGoodsNumberAll(mContext, SELECTPOSITION) + "");
        m_list_all_price_popup.setText("共￥"+mGoodsDataBaseInterface.getSecondGoodsPriceAll(mContext, SELECTPOSITION)+"元");



       final CartPopupWindowListViewAdapter mCartPopupWindowListViewAdapter = new CartPopupWindowListViewAdapter(this);

        for(int i=0;i<listGoodsBean.size();i++){
            if(listGoodsBean.get(i).getGoodsnum().equals("0")){
                listGoodsBean.remove(i);
                mCartPopupWindowListViewAdapter.setList(listGoodsBean);
            }
        }
    //    mCartPopupWindowListViewAdapter.setName(listNameStr);
        mCartPopupWindowListViewAdapter.setList(listGoodsBean);
        lv_cart_popup.setAdapter(mCartPopupWindowListViewAdapter);


        tv_cart_popup_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog();

            }
        });

        mCartPopupWindowListViewAdapter.setOnItemClickListener(new CartPopupWindowListViewAdapter.OnItemClickListener() {
            @Override
            public void onItemJiaClick(CartPopupWindowListViewAdapter.ViewHolder v) {
                String nums = v.tv_popup_lv_number.getText().toString().trim();
                if(nums.isEmpty()||nums.equals("0")){
                   v.iv_popup_lv_subtract.setVisibility(View.GONE);
                   v.tv_popup_lv_number.setVisibility(View.GONE);
                }else {
                    if ((Integer.parseInt(nums) + 1) > Integer.parseInt(foodList.get(v.getPosition()).getStock())) {
                        MessageUtils.showShortToast(StoreShopListVerticalStyleActivity.this, "库存不足");
                        return;
                    }
                    // v.tv_popup_lv_number.setText(mGoodsDataBaseInterface.saveGoodsNumber(mContext, SELECTPOSITION,listGoodsBean.get(v.getPosition()).getGoodsid(), String.valueOf(Integer.parseInt(nums) + 1), listGoodsBean.get(v.getPosition()).getGoodsprice()) + "");
                    v.tv_popup_lv_number.setText(mGoodsDataBaseInterface.saveGoodsNumber(mContext, SELECTPOSITION, listGoodsBean.get(v.getPosition()).getGoodsid(), String.valueOf(Integer.parseInt(nums) + 1),
                            listGoodsBean.get(v.getPosition()).getGoodsprice(),
                            listGoodsBean.get(v.getPosition()).getGoodsname(), listGoodsBean.get(v.getPosition()).getStock()) + "");
                    foodAdapter.notifyDataSetChanged();
                    setPupupAll();
                }
            }

            @Override
            public void onItemJianClick(CartPopupWindowListViewAdapter.ViewHolder v) {
                String nums = v.tv_popup_lv_number.getText().toString().trim();
                //   v.tv_popup_lv_number.setText(mGoodsDataBaseInterface.saveGoodsNumber(mContext, SELECTPOSITION,listGoodsBean.get(v.getPosition()).getGoodsid(), String.valueOf(Integer.parseInt(nums) - 1), listGoodsBean.get(v.getPosition()).getGoodsprice()) + "");
                v.tv_popup_lv_number.setText(mGoodsDataBaseInterface.saveGoodsNumber(mContext, SELECTPOSITION,
                        listGoodsBean.get(v.getPosition()).getGoodsid(), String.valueOf(Integer.parseInt(nums) - 1),
                        listGoodsBean.get(v.getPosition()).getGoodsprice() ,
                        listGoodsBean.get(v.getPosition()).getGoodsname(),listGoodsBean.get(v.getPosition()).getStock())+"");
                foodAdapter.notifyDataSetChanged();
                nums = v.tv_popup_lv_number.getText().toString().trim();

                // 减完之后  数据为0
                if (nums.equals("0")) {
                    listGoodsBean= OperateGoodsDataBaseStatic.getSecondGoodsTypeList(mContext);
                    mCartPopupWindowListViewAdapter.setList(listGoodsBean);
                }
                setPupupAll();
                setAll();

            }
        });

    }

    private void showDeleteDialog() {
        new AlertDialog.Builder(this).setTitle("提示")
        .setMessage("是否清空购物车内所有商品？")
                .setNegativeButton("取消",null)
                .setPositiveButton("清空", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mGoodsDataBaseInterface.deleteAll(mContext);
                        foodAdapter.notifyDataSetChanged();
                        setAll();
                        popupWindow.dismiss();
                    }
                }).show();


    }

    private void setData() {

    }

    @Override
    public String setupToolBarTitle() {
        tv=getmTitle();
        tv.setText(name);
        return tv.getText().toString();
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
        mRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StoreShopListVerticalStyleActivity.this,StoreShopDetailActivity.class);
                intent.putExtra("id",getIntent().getStringExtra("id"));
                intent.putExtra("name",name);
                startActivity(intent);
                AnimUtil.intentSlidIn(StoreShopListVerticalStyleActivity.this);
            }
        });
        return true;
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
        if (mGoodsDataBaseInterface.getSecondGoodsNumberAll(mContext, SELECTPOSITION) == 0) {
            m_list_car_vertical_style.setImageResource(R.mipmap.cart_car_gray);
            m_list_submit_vertical_style.setBackgroundResource(R.color.text_gray);
            mListAllNum.setVisibility(View.GONE);
            mListAllPrice.setText("共￥0 元");
            mListAllNum.setText("0");
        } else {
            m_list_car_vertical_style.setImageResource(R.mipmap.cart_car_orange);
            m_list_submit_vertical_style.setBackgroundResource(R.color.text_red);
            mListAllPrice.setText("共￥" + String.valueOf(mGoodsDataBaseInterface.getSecondGoodsPriceAll(mContext, SELECTPOSITION))+" 元");
            mListAllNum.setText(mGoodsDataBaseInterface.getSecondGoodsNumberAll(mContext, SELECTPOSITION) + "");
            mListAllNum.setVisibility(View.VISIBLE);
        }
    }
    /**
     * 点击弹窗加号和减号的时候设置总数和总价格
     */
    private void setPupupAll() {

        //设置所有购物数量
        if (mGoodsDataBaseInterface.getSecondGoodsNumberAll(mContext, SELECTPOSITION) == 0) {
            popupWindow.dismiss();
            mGoodsDataBaseInterface.deleteAll(mContext);
            m_list_car_vertical_style.setImageResource(R.mipmap.cart_car_gray);
            m_list_submit_vertical_style.setBackgroundResource(R.color.text_gray);
            mListAllNum.setVisibility(View.GONE);
            mListAllPrice.setText("共￥0 元");
            mListAllNum.setText("0");
        } else {
            m_list_all_price_popup.setText("共￥" + String.valueOf(mGoodsDataBaseInterface.getSecondGoodsPriceAll(mContext, SELECTPOSITION))+" 元");
            m_list_num_popup.setText(mGoodsDataBaseInterface.getSecondGoodsNumberAll(mContext, SELECTPOSITION) + "");
            m_list_num_popup.setVisibility(View.VISIBLE);
        }
    }


}
