package com.ctrl.forum.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.beanu.arad.base.ToolBarFragment;
import com.beanu.arad.utils.AnimUtil;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.cart.animutils.GoodsAnimUtil;
import com.ctrl.forum.cart.datasave.GoodsBean;
import com.ctrl.forum.cart.datasave.OperateGoodsDataBase;
import com.ctrl.forum.cart.datasave.OperateGoodsDataBaseStatic;
import com.ctrl.forum.entity.ProductCategroy;
import com.ctrl.forum.ui.activity.store.StoreCommodityDetailActivity;
import com.ctrl.forum.ui.activity.store.StoreOrderDetailActivity;
import com.ctrl.forum.ui.activity.store.StoreShopListHorzitalStyleActivity;
import com.ctrl.forum.ui.adapter.CartPopupWindowListViewAdapter;
import com.ctrl.forum.ui.adapter.StoreShopListHorzitalStyleGridViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 商城商品列表 fragment
 * Created by Administrator on 2015/11/30.
 */
public class StoreShopListHorzitalStyleFragment extends ToolBarFragment implements View.OnClickListener{
    @InjectView(R.id.gridview_shore_horzital_style)
    GridView gridView;
    private List<ProductCategroy> aroundCompanies ;
    private StoreShopListHorzitalStyleGridViewAdapter mAdapter;
    private int pos;
    private OperateGoodsDataBase mGoodsDataBaseInterface;
    public static int SELECTPOSITION = 0;//一级列表下标值
    private StoreShopListHorzitalStyleActivity activity;
    private ImageView iv;
    private TextView tv;
    private Button btn;

    private PopupWindow popupWindow;
    private TextView m_list_num_popup;
    private TextView m_list_all_price_popup;
    private ArrayList<GoodsBean> listGoodsBean;
    private TextView tvNum;
    private int bol=0;
    private Button m_list_submit_popup;

    public static StoreShopListHorzitalStyleFragment newInstance(List<ProductCategroy>aroundCompanies,
                                                                 int pos,ImageView iv,TextView tv,Button btn,TextView tvNum) {
        StoreShopListHorzitalStyleFragment fragment = new StoreShopListHorzitalStyleFragment();
        fragment.aroundCompanies = aroundCompanies;
        fragment.iv=iv;
        fragment.tv=tv;
        fragment.btn=btn;
        fragment.tvNum=tvNum;
        fragment.pos=pos;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGoodsDataBaseInterface = OperateGoodsDataBase.getInstance();
        iv.setOnClickListener(this);
        btn.setOnClickListener(this);
        //清空数据库缓存
       // mGoodsDataBaseInterface.deleteAll(getActivity());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_shop_list_horzital_style, container, false);
        ButterKnife.inject(this, view);
        activity=(StoreShopListHorzitalStyleActivity)getActivity();
        mGoodsDataBaseInterface = OperateGoodsDataBase.getInstance();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==111&&resultCode==112){
            setAll();
            activity.getAdapter().reLoad();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new StoreShopListHorzitalStyleGridViewAdapter(getActivity());
        mAdapter.setList(aroundCompanies.get(pos).getProductList());
        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), StoreCommodityDetailActivity.class);
                intent.putExtra("id", aroundCompanies.get(pos).getProductList().get(position).getId());
                startActivityForResult(intent, 111);
                AnimUtil.intentSlidIn(getActivity());
            }
        });

        mAdapter.setOnItemClickListener(new StoreShopListHorzitalStyleGridViewAdapter.OnItemClickListener() {
            @Override
            public void onItemJiaClick(StoreShopListHorzitalStyleGridViewAdapter.ViewHolder holder) {
                String nums = holder.tv_numbers.getText().toString().trim();
                if (nums.isEmpty() || nums.equals("0")) {
                    holder.iv_subtract.setVisibility(View.VISIBLE);
                    holder.tv_numbers.setText(mGoodsDataBaseInterface.saveGoodsNumber(getActivity(), SELECTPOSITION,
                            aroundCompanies.get(pos).getProductList().get(holder.getPosition()).getId(), "1",
                            Float.parseFloat(aroundCompanies.get(pos).getProductList().get(holder.getPosition()).getSellingPrice()),
                            aroundCompanies.get(pos).getProductList().get(holder.getPosition()).getName(),
                            aroundCompanies.get(pos).getProductList().get(holder.getPosition()).getStock()) + "");
                    holder.tv_numbers.setVisibility(View.VISIBLE);
                }// 点击加号之前有数据的时候
                else {
                    if (aroundCompanies.get(pos).getProductList().get(holder.getPosition()).getStock() != null) {
                        if ((Integer.parseInt(nums) + 1) > Integer.parseInt(aroundCompanies.get(pos).getProductList().get(holder.getPosition()).getStock())) {
                            MessageUtils.showShortToast(getActivity(), "库存不足");
                            return;
                        }
                    }
                    holder.tv_numbers.setText(mGoodsDataBaseInterface.saveGoodsNumber(getActivity(), SELECTPOSITION,
                            aroundCompanies.get(pos).getProductList().get(holder.getPosition()).getId(),
                            String.valueOf(Integer.parseInt(nums) + 1),
                            Float.parseFloat(aroundCompanies.get(pos).getProductList().get(holder.getPosition()).getSellingPrice()),
                            aroundCompanies.get(pos).getProductList().get(holder.getPosition()).getName(),
                            aroundCompanies.get(pos).getProductList().get(holder.getPosition()).getStock()) + "");
                }

                activity = (StoreShopListHorzitalStyleActivity) getActivity();

                //动画
                GoodsAnimUtil.setAnim(getActivity(), holder.iv_add, activity.getRel());
                GoodsAnimUtil.setOnEndAnimListener(new onEndAnim());
                // 统计购物总数和购物总价
            }

            @Override
            public void onItemJianClick(StoreShopListHorzitalStyleGridViewAdapter.ViewHolder holder) {
                String nums = holder.tv_numbers.getText().toString().trim();
                //  holder.item_menu_content_number.setText(mGoodsDataBaseInterface.saveGoodsNumber(mContext, SELECTPOSITION, foodList.get(holder.getPosition()).getId(), String.valueOf(Integer.parseInt(nums) - 1), foodList.get(holder.getPosition()).getSellingPrice()) + "");
                holder.tv_numbers.setText(mGoodsDataBaseInterface.saveGoodsNumber(getActivity(), SELECTPOSITION,
                        aroundCompanies.get(pos).getProductList().get(holder.getPosition()).getId(),
                        String.valueOf(Integer.parseInt(nums) - 1),
                        Float.parseFloat(aroundCompanies.get(pos).getProductList().get(holder.getPosition()).getSellingPrice()),
                        aroundCompanies.get(pos).getProductList().get(holder.getPosition()).getName(),
                        aroundCompanies.get(pos).getProductList().get(holder.getPosition()).getStock()) + "");
                nums = holder.tv_numbers.getText().toString().trim();
                // 减完之后  数据为0
                if (nums.equals("0")) {
                    holder.tv_numbers.setVisibility(View.GONE);
                    holder.iv_subtract.setVisibility(View.GONE);
                }
                setAll();
            }
        });

    }


    @Override
    public void onClick(View v) {
        if(iv.getId()==v.getId()){
            if (listGoodsBean != null) {
                listGoodsBean.clear();
            }
            if (!tvNum.getText().toString().equals("0")) {
                showCartPopupWindow(v);
            }
        }

        if(btn.getId()==v.getId()){
            if (tvNum.getText().toString().equals("0")) {
                MessageUtils.showShortToast(getActivity(), "购物车还是空的！");
            } else {
                Intent intent = new Intent(getActivity(), StoreOrderDetailActivity.class);
                intent.putExtra("companyId", getActivity().getIntent().getStringExtra("id"));
                startActivity(intent);
                AnimUtil.intentSlidIn(getActivity());
            }
        }
        if(m_list_submit_popup.getId()==v.getId()){
            Intent intent = new Intent(getActivity(), StoreOrderDetailActivity.class);
            intent.putExtra("companyId", getActivity().getIntent().getStringExtra("id"));
            startActivity(intent);
            AnimUtil.intentSlidIn(getActivity());
            popupWindow.dismiss();
        }
    }

    private void showCartPopupWindow(View v) {
        bol=1;
        View contentView= LayoutInflater.from(getActivity()).inflate(R.layout.popupwindow_cart_vertical_style,null);

        popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT, true);

        final ListView lv_cart_popup=(ListView)contentView.findViewById(R.id.lv_cart_popup);
        m_list_num_popup = (TextView) contentView.findViewById(R.id.m_list_num_popup);
        m_list_all_price_popup = (TextView) contentView.findViewById(R.id.m_list_all_price_popup);
        m_list_submit_popup = (Button) contentView.findViewById(R.id.m_list_submit_popup);
        TextView tv_cart_popup_delete = (TextView) contentView.findViewById(R.id.tv_cart_popup_delete);
        listGoodsBean= OperateGoodsDataBaseStatic.getSecondGoodsTypeList(activity);
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
                setAll();
                activity.getAdapter().reLoad();
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });
        m_list_submit_popup.setOnClickListener(this);

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
        m_list_num_popup.setText(mGoodsDataBaseInterface.getSecondGoodsNumberAll(getActivity(), SELECTPOSITION) + "");
        m_list_all_price_popup.setText("共￥"+mGoodsDataBaseInterface.getSecondGoodsPriceAll(getActivity(), SELECTPOSITION)+"元");



        final CartPopupWindowListViewAdapter mCartPopupWindowListViewAdapter = new CartPopupWindowListViewAdapter(getActivity());

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
                    if(listGoodsBean.get(v.getPosition()).getStock()!=null) {
                        if ((Integer.parseInt(nums) + 1) > Integer.parseInt(listGoodsBean.get(v.getPosition()).getStock())) {
                            MessageUtils.showShortToast(getActivity(), "库存不足");
                            return;
                        }
                    }

                    // v.tv_popup_lv_number.setText(mGoodsDataBaseInterface.saveGoodsNumber(mContext, SELECTPOSITION,listGoodsBean.get(v.getPosition()).getGoodsid(), String.valueOf(Integer.parseInt(nums) + 1), listGoodsBean.get(v.getPosition()).getGoodsprice()) + "");
                    v.tv_popup_lv_number.setText(mGoodsDataBaseInterface.saveGoodsNumber(getActivity(), SELECTPOSITION, listGoodsBean.get(v.getPosition()).getGoodsid(), String.valueOf(Integer.parseInt(nums) + 1),
                            listGoodsBean.get(v.getPosition()).getGoodsprice(),
                            listGoodsBean.get(v.getPosition()).getGoodsname(), listGoodsBean.get(v.getPosition()).getStock()) + "");

                 //   activity.getAdapter().reLoad();
                    setPupupAll();
                }
            }

            @Override
            public void onItemJianClick(CartPopupWindowListViewAdapter.ViewHolder v) {
                String nums = v.tv_popup_lv_number.getText().toString().trim();
                //   v.tv_popup_lv_number.setText(mGoodsDataBaseInterface.saveGoodsNumber(mContext, SELECTPOSITION,listGoodsBean.get(v.getPosition()).getGoodsid(), String.valueOf(Integer.parseInt(nums) - 1), listGoodsBean.get(v.getPosition()).getGoodsprice()) + "");
                 v.tv_popup_lv_number.setText(mGoodsDataBaseInterface.saveGoodsNumber(getActivity(), SELECTPOSITION,
                         listGoodsBean.get(v.getPosition()).getGoodsid(), String.valueOf(Integer.parseInt(nums) - 1),
                         listGoodsBean.get(v.getPosition()).getGoodsprice(),
                         listGoodsBean.get(v.getPosition()).getGoodsname(), listGoodsBean.get(v.getPosition()).getStock()) + "");
                nums = v.tv_popup_lv_number.getText().toString().trim();

                // 减完之后  数据为0
                if (nums.equals("0")) {
                   /* setPupupAll();
                    setAll();
                    activity.getAdapter().reLoad();*/
                    listGoodsBean= OperateGoodsDataBaseStatic.getSecondGoodsTypeList(activity);
                    mCartPopupWindowListViewAdapter.setList(listGoodsBean);
                }

                setAll();
                setPupupAll();

            }
        });

    }

    private void showDeleteDialog() {
        new AlertDialog.Builder(getActivity()).setTitle("提示")
                .setMessage("是否清空购物车内所有商品？")
                .setNegativeButton("取消",null)
                .setPositiveButton("清空", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mGoodsDataBaseInterface.deleteAll(getActivity());
                        setAll();
                        activity.getAdapter().reLoad();
                        popupWindow.dismiss();
                    }
                }).show();


    }

    /**
     * 点击弹窗加号和减号的时候设置总数和总价格
     */
    private void setPupupAll() {

        //设置所有购物数量
        if (mGoodsDataBaseInterface.getSecondGoodsNumberAll(getActivity(), SELECTPOSITION) == 0) {

            popupWindow.dismiss();
            mGoodsDataBaseInterface.deleteAll(getActivity());
            iv.setImageResource(R.mipmap.cart_car_gray);
            btn.setBackgroundResource(R.color.text_gray);
            tvNum.setVisibility(View.GONE);
            tv.setText("共￥0 元");
            tvNum.setText("0");
            activity.getAdapter().reLoad();
        } else {
            m_list_all_price_popup.setText("共￥" + String.valueOf(mGoodsDataBaseInterface.getSecondGoodsPriceAll(getActivity(), SELECTPOSITION))+" 元");
            m_list_num_popup.setText(mGoodsDataBaseInterface.getSecondGoodsNumberAll(getActivity(), SELECTPOSITION) + "");
            m_list_num_popup.setVisibility(View.VISIBLE);
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
    public void setAll() {

        //设置所有购物数量
        if (mGoodsDataBaseInterface.getSecondGoodsNumberAll(getActivity(), SELECTPOSITION) == 0) {
            iv.setImageResource(R.mipmap.cart_car_gray);
            btn.setBackgroundResource(R.color.text_gray);
            tvNum.setVisibility(View.GONE);
            tv.setText("共￥0 元");
            tvNum.setText("0");
        } else {
            iv.setImageResource(R.mipmap.cart_car_orange);
            btn.setBackgroundResource(R.color.text_red);
            tv.setText("共￥" + String.valueOf(mGoodsDataBaseInterface.getSecondGoodsPriceAll(getActivity(), SELECTPOSITION))+" 元");
            tvNum.setText(mGoodsDataBaseInterface.getSecondGoodsNumberAll(getActivity(), SELECTPOSITION) + "");
            tvNum.setVisibility(View.VISIBLE);
        }
    }




}
