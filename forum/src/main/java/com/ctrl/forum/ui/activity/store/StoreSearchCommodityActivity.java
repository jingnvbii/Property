package com.ctrl.forum.ui.activity.store;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.AnimUtil;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.cart.animutils.GoodsAnimUtil;
import com.ctrl.forum.cart.datasave.GoodsBean;
import com.ctrl.forum.cart.datasave.OperateGoodsDataBase;
import com.ctrl.forum.cart.datasave.OperateGoodsDataBaseStatic;
import com.ctrl.forum.dao.MallDao;
import com.ctrl.forum.entity.Merchant;
import com.ctrl.forum.entity.Product2;
import com.ctrl.forum.ui.adapter.CartPopupWindowListViewAdapter;
import com.ctrl.forum.ui.adapter.StoreSearchGridViewAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/*
* 商城商品搜索 activity
* */

public class StoreSearchCommodityActivity extends AppToolBarActivity implements View.OnClickListener{
    @InjectView(R.id.iv_back)//返回按钮
    ImageView iv_back;
    @InjectView(R.id.et_search)//搜索输入框
    EditText et_search;
    @InjectView(R.id.tv_search)//搜索按钮
    TextView tv_search;
    @InjectView(R.id.tv_choose)//切换按钮
    TextView tv_choose;
    @InjectView(R.id.tv_xiaoliang)//销量优先
            RadioButton tv_xiaoliang;
    @InjectView(R.id.tv_pinjia_commodity)//评价优先
            RadioButton tv_pinjia_commodity;
    @InjectView(R.id.gridview)//网格列表
    PullToRefreshGridView gridView;

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

    /**
     * 购物车布局
     */
    @InjectView(R.id.m_list_car_lay)
    RelativeLayout mCarLay;

    @InjectView(R.id.m_list_car_vertical_style)//购物车图片
            ImageView m_list_car_vertical_style;
    @InjectView(R.id.m_list_submit_vertical_style)//提交订单按钮
            Button m_list_submit_vertical_style;

    private List<Merchant> list;
    private StoreSearchGridViewAdapter adapter;
    private PopupWindow popupWindow;
    private int choose=1;
    private MallDao mdao;
    private int PAGE_NUM=1;
    private OperateGoodsDataBase mGoodsDataBaseInterface;
    private StoreSearchCommodityActivity mContext;
    public static int SELECTPOSITION = 0;//一级列表下标值
    private List<Product2> listProduct;
    private TextView m_list_num_popup;
    private TextView m_list_all_price_popup;
    private ArrayList<GoodsBean> listGoodsBean;
    private List<Product2> listProduct2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_commodity_search);
        ButterKnife.inject(this);
        // 隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mContext=this;
        initView();
        initData();
    }

    private void initView() {
        tv_choose.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        tv_search.setOnClickListener(this);
        tv_pinjia_commodity.setOnClickListener(this);
        tv_xiaoliang.setOnClickListener(this);

        m_list_car_vertical_style.setOnClickListener(this);
        m_list_submit_vertical_style.setOnClickListener(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        et_search.setText("");
        gridView.setAdapter(adapter);
        setAll();
    }

    private void initData() {
        tv_choose.setText("商品");
        et_search.setText(getIntent().getStringExtra("keyword"));
        //光标移到行尾
        et_search.setSelection(et_search.getText().length());
        mGoodsDataBaseInterface = OperateGoodsDataBase.getInstance();
        //清空数据库缓存
        mGoodsDataBaseInterface.deleteAll(mContext);
        tv_choose.setText("商品");
        mdao=new MallDao(this);
        if(getIntent().getFlags()==11){
             mdao.requestQueryCompanyProducts(getIntent().getStringExtra("companyId"),getIntent().getStringExtra("keyword"),String.valueOf(PAGE_NUM), String.valueOf(Constant.PAGE_SIZE));
        }else {
            mdao.requestSearchCompanysOrProductByKeyword("0", "", " ",
                    "1", getIntent().getStringExtra("keyword"), Arad.preferences.getString("memberId"), String.valueOf(PAGE_NUM), String.valueOf(Constant.PAGE_SIZE)
            );
        }
/*        mdao.requestSearchCompanysOrProductByKeyword("0", Arad.preferences.getString("latitude"), Arad.preferences.getString("longitude"),
                "1", getIntent().getStringExtra("keyword"), Arad.preferences.getString("memberId"), String.valueOf(PAGE_NUM), String.valueOf(Constant.PAGE_SIZE)
        );*/
        adapter=new StoreSearchGridViewAdapter(this);
        gridView.getRefreshableView().setNumColumns(2);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(StoreSearchCommodityActivity.this, StoreCommodityDetailActivity.class);
                intent.putExtra("id", listProduct.get(position).getId());
                intent.putExtra("name", listProduct.get(position).getName());
                //  startActivityForResult(intent, 500);
                startActivity(intent);
                AnimUtil.intentSlidIn(StoreSearchCommodityActivity.this);
            }
        });

        adapter.setOnItemClickListener(new StoreSearchGridViewAdapter.OnItemClickListener() {
            @Override
            public void onItemJiaClick(StoreSearchGridViewAdapter.ViewHolder holder) {
                String nums = holder.item_menu_content_number.getText().toString().trim();
                if (nums.isEmpty() || nums.equals("0")) {
                    holder.item_menu_content_jian.setVisibility(View.VISIBLE);
                    //  Log.i("tag", "foodList-id--" + foodList.get(holder.getPosition()).getId());
                    //    Log.i("tag", "price---" + foodList.get(holder.getPosition()).getSellingPrice());
                    //  Log.i("tag", "price---" + foodList.get(holder.getPosition()).getName());

                    //  holder.item_menu_content_number.setText(mGoodsDataBaseInterface.saveGoodsNumber(mContext, SELECTPOSITION, foodList.get(holder.getPosition()).getId(), "1", foodList.get(holder.getPosition()).getSellingPrice()) + "");
                    holder.item_menu_content_number.setText(mGoodsDataBaseInterface.saveGoodsNumber(mContext, SELECTPOSITION,
                            listProduct.get(holder.getPosition()).getId(), "1",
                            Float.parseFloat(listProduct.get(holder.getPosition()).getSellingPrice() + ""),
                            listProduct.get(holder.getPosition()).getName() + "", listProduct.get(holder.getPosition()).getStock() + "") + "");
                    holder.item_menu_content_number.setVisibility(View.VISIBLE);
                }// 点击加号之前有数据的时候
                else {
                    if ((Integer.parseInt(nums) + 1) > Integer.parseInt(listProduct.get(holder.getPosition()).getStock())) {
                        MessageUtils.showShortToast(StoreSearchCommodityActivity.this, "库存不足");
                        return;
                    }
                    holder.item_menu_content_number.setText(mGoodsDataBaseInterface.saveGoodsNumber(mContext, SELECTPOSITION,
                            listProduct.get(holder.getPosition()).getId(), String.valueOf(Integer.parseInt(nums) + 1),
                            Float.parseFloat(listProduct.get(holder.getPosition()).getSellingPrice() + ""),
                            listProduct.get(holder.getPosition()).getName() + "", listProduct.get(holder.getPosition()).getStock() + "") + "");
                    // holder.item_menu_content_number.setText(mGoodsDataBaseInterface.saveGoodsNumber(mContext, SELECTPOSITION, foodList.get(holder.getPosition()).getId(), String.valueOf(Integer.parseInt(nums) + 1), foodList.get(holder.getPosition()).getSellingPrice()) + "");
                }


                //动画
                GoodsAnimUtil.setAnim(StoreSearchCommodityActivity.this, holder.item_menu_content_jia, mCarLay);
                GoodsAnimUtil.setOnEndAnimListener(new onEndAnim());
                // 统计购物总数和购物总价
            }

            @Override
            public void onItemJianClick(StoreSearchGridViewAdapter.ViewHolder holder) {
                String nums = holder.item_menu_content_number.getText().toString().trim();
                //  holder.item_menu_content_number.setText(mGoodsDataBaseInterface.saveGoodsNumber(mContext, SELECTPOSITION, foodList.get(holder.getPosition()).getId(), String.valueOf(Integer.parseInt(nums) - 1), foodList.get(holder.getPosition()).getSellingPrice()) + "");
                holder.item_menu_content_number.setText(mGoodsDataBaseInterface.saveGoodsNumber(mContext, SELECTPOSITION,
                        listProduct.get(holder.getPosition()).getId(),
                        String.valueOf(Integer.parseInt(nums) - 1),
                        Float.parseFloat(listProduct.get(holder.getPosition()).getSellingPrice())
                        ,
                        listProduct.get(holder.getPosition()).getName(), listProduct.get(holder.getPosition()).getStock()) + "");
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

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if(requestCode==6){
          //  MessageUtils.showShortToast(this,"根据关键字获取商品成功");
            listProduct=mdao.getListProduct();
            adapter.setList(listProduct);
        }
        if(requestCode==10){
            listProduct=mdao.getListProduct();
            adapter.setList(listProduct);
        }
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


    @Override
    public void onClick(View v) {
        switch (v.getId()){
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
                    MessageUtils.showShortToast(StoreSearchCommodityActivity.this,"购物车还是空的！");
                }else {
                    Intent intent=new Intent(StoreSearchCommodityActivity.this,StoreOrderDetailActivity.class);
                    intent.putExtra("companyId",getIntent().getStringExtra("id"));
                    startActivity(intent);
                    AnimUtil.intentSlidIn(StoreSearchCommodityActivity.this);
                }

                break;

            case R.id.tv_choose:
                showPopupWidow();
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_search:
                if(TextUtils.isEmpty(et_search.getText().toString())){
                    MessageUtils.showShortToast(this,"搜索内容不能为空");
                    return;
                }
                if(choose==1){
                    mdao.requestSearchCompanysOrProductByKeyword("0", Arad.preferences.getString("latitude"), Arad.preferences.getString("longitude"),
                            "1", getIntent().getStringExtra("keyword"), Arad.preferences.getString("memberId"), String.valueOf(PAGE_NUM), String.valueOf(Constant.PAGE_SIZE)
                    );
                }
                if(choose==2){
                    Intent intent=new Intent(StoreSearchCommodityActivity.this,StoreSearchShopActivity.class);
                    intent.putExtra("keyword",et_search.getText().toString().trim());
                    startActivity(intent);
                    AnimUtil.intentSlidIn(StoreSearchCommodityActivity.this);
                }
                break;
            case R.id.tv_pinjia_commodity:
                mdao.requestSearchCompanysOrProductByKeyword("1", Arad.preferences.getString("latitude"), Arad.preferences.getString("longitude"),
                        "1", getIntent().getStringExtra("keyword"), Arad.preferences.getString("memberId"), String.valueOf(PAGE_NUM), String.valueOf(Constant.PAGE_SIZE)
                );
                break;
            case R.id.tv_xiaoliang:
                mdao.requestSearchCompanysOrProductByKeyword("0", Arad.preferences.getString("latitude"), Arad.preferences.getString("longitude"),
                        "1", getIntent().getStringExtra("keyword"), Arad.preferences.getString("memberId"), String.valueOf(PAGE_NUM), String.valueOf(Constant.PAGE_SIZE)
                );
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
        TextView tv_cart_popup_delete = (TextView) contentView.findViewById(R.id.tv_cart_popup_delete);
        listGoodsBean= OperateGoodsDataBaseStatic.getSecondGoodsTypeList(mContext);
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
                    if ((Integer.parseInt(nums) + 1) > Integer.parseInt(listProduct.get(v.getPosition()).getStock())) {
                        MessageUtils.showShortToast(StoreSearchCommodityActivity.this, "库存不足");
                        return;
                    }
                    // v.tv_popup_lv_number.setText(mGoodsDataBaseInterface.saveGoodsNumber(mContext, SELECTPOSITION,listGoodsBean.get(v.getPosition()).getGoodsid(), String.valueOf(Integer.parseInt(nums) + 1), listGoodsBean.get(v.getPosition()).getGoodsprice()) + "");
                    v.tv_popup_lv_number.setText(mGoodsDataBaseInterface.saveGoodsNumber(mContext, SELECTPOSITION, listGoodsBean.get(v.getPosition()).getGoodsid(), String.valueOf(Integer.parseInt(nums) + 1),
                            listGoodsBean.get(v.getPosition()).getGoodsprice(),
                            listGoodsBean.get(v.getPosition()).getGoodsname(), listGoodsBean.get(v.getPosition()).getStock()) + "");
                    adapter.notifyDataSetChanged();
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
                adapter.notifyDataSetChanged();
                nums = v.tv_popup_lv_number.getText().toString().trim();

                // 减完之后  数据为0
                if (nums.equals("0")) {
                    //  listGoodsBean.remove(v.getPosition());
                    //  mCartPopupWindowListViewAdapter.setName(listNameStr);
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
                        adapter.notifyDataSetChanged();
                        setAll();
                        popupWindow.dismiss();
                    }
                }).show();


    }

    private void showPopupWidow() {
        View contentView= LayoutInflater.from(this).inflate(R.layout.popupwindow_store_choose,null);
        TextView tv_dianpu_pup = (TextView) contentView.findViewById(R.id.tv_dianpu_pup);
        final TextView tv_shangpin_pup = (TextView) contentView.findViewById(R.id.tv_shangpin_pup);

        tv_dianpu_pup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_choose.setText("店铺");
                et_search.setText("");
                choose = 2;
                popupWindow.dismiss();
            }
        });
        tv_shangpin_pup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_choose.setText("商品");
                choose = 1;
                popupWindow.dismiss();

            }
        });

        popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT, true);
     /*   // 设置SelectPicPopupWindow的View
        popupWindow.setContentView(contentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);*/
        // 设置SelectPicPopupWindow弹出窗体可点击
        popupWindow.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
      //  popupWindow.setAnimationStyle(R.style.AnimBottom);
        // 实例化一个ColorDrawable颜色为半透明
      //  ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.mipmap.choose_bg));

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
        popupWindow.showAsDropDown(tv_choose,0,20);

    }


}
