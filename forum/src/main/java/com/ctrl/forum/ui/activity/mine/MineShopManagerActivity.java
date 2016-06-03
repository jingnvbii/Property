package com.ctrl.forum.ui.activity.mine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarActivity;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.customview.MinePinnedHeaderListView;
import com.ctrl.forum.dao.MineStoreDao;
import com.ctrl.forum.entity.CProduct;
import com.ctrl.forum.entity.CProductCategory;
import com.ctrl.forum.ui.adapter.MineShopManagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 店铺管理
 */
public class MineShopManagerActivity extends ToolBarActivity implements CompoundButton.OnCheckedChangeListener{
    private MinePinnedHeaderListView right_listview;
    private MineShopManagerAdapter sectionedAdapter;
    private boolean isScroll = true;
    private ListView left_listView;
    private Map<String,List<CProduct>> rightStr ;
   //private Map<Integer,List<CProduct>> rightStr ;
    private List<CProduct> rightStrs;
    private TextView tv_left_content;
    private TextView tv_line;
    private MineStoreDao sdao;
    private List<CProductCategory> leftStr; //商品分类

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_shop_manager);

        initData();
        initView();
       // getData();

        sectionedAdapter = new MineShopManagerAdapter(this);
        right_listview.setAdapter(sectionedAdapter);
        sectionedAdapter.setOnCheckBox(this);

        left_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long arg3) {
                isScroll = false;
                for (int i = 0; i < left_listView.getChildCount(); i++) {
                    if (i == position) {
                        left_listView.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.white));
                        //TextView textView = (TextView) view.findViewById(R.id.tv_left_content);
                        tv_left_content.setTextColor(getResources().getColor(R.color.red_bg));
                        //textView.setTextColor(getResources().getColor(R.color.red_bg));
                    } else {
                        left_listView.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.gray_store));
                        tv_left_content.setTextColor(getResources().getColor(R.color.defaultTextColor));
                    }
                }

                int rightSection = 0;
                for (int i = 0; i < position; i++) {
                    rightSection += sectionedAdapter.getCountForSection(i) + 1;
                }
                right_listview.setSelection(rightSection);

            }

        });

        right_listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {

            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (isScroll) {
                    for (int i = 0; i < left_listView.getChildCount(); i++) {
                        if (i == sectionedAdapter.getSectionForPosition(firstVisibleItem)) {
                            left_listView.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.white));
                            tv_left_content.setTextColor(getResources().getColor(R.color.red_bg));
                        } else {
                            left_listView.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.gray_store));
                            tv_left_content.setTextColor(getResources().getColor(R.color.defaultTextColor));
                        }
                    }
                } else {
                    isScroll = true;
                }
            }
        });
    }

    private void initView() {
         right_listview = (MinePinnedHeaderListView) findViewById(R.id.lv_right);
         tv_line = (TextView) findViewById(R.id.tv_line);

        left_listView = (ListView) findViewById(R.id.lv_left);

        View leftView = LayoutInflater.from(this).inflate(R.layout.item_mine_list_left,null);
        tv_left_content = (TextView) leftView.findViewById(R.id.tv_left_content);
    }

    private void initData() {
        /*rightStr = new HashMap<>();
        for (int i = 0;i<5;i++){
            List<CProduct> list = new ArrayList<>();
            CProduct cProduct = new CProduct();
            cProduct.setId("i");
         rightStr.put(i+"",list);
        }*/

        sdao = new MineStoreDao(this);
        sdao.getProductCategory(Arad.preferences.getString("companyId"));
    }

    @Override
    public boolean setupToolBarLeftButton(ImageView leftButton) {
        leftButton.setImageResource(R.mipmap.white_arrow_left_none);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        return true;
    }

    @Override
    public String setupToolBarTitle() {return "商品管理";}

    private void getData()
    {
        leftStr = new ArrayList<>();
        CProductCategory cProductCategory = new CProductCategory();
        cProductCategory.setName("日用百货");
        leftStr.add(cProductCategory);
        CProductCategory cProductCategory1 = new CProductCategory();
        cProductCategory.setName("奶类");
        leftStr.add(cProductCategory1);
        CProductCategory cProductCategory2= new CProductCategory();
        cProductCategory.setName("生鲜类");
        leftStr.add(cProductCategory2);
        CProductCategory cProductCategory3 = new CProductCategory();
        cProductCategory.setName("销量排行");
        leftStr.add(cProductCategory3);

        List<String> str1 = new ArrayList<>();
        for (int i= 0 ;i<leftStr.size();i++){
            CProductCategory cProductCategory6 = leftStr.get(i);
            str1.add(cProductCategory6.getName());
            //rightStr赋值
            rightStr.put(leftStr.get(i).getId(),leftStr.get(i).getcProducts());
        }
        left_listView.setAdapter(new ArrayAdapter<>(this,
                R.layout.item_mine_list_left, R.id.tv_left_content, str1));
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if (requestCode==2){
            leftStr = sdao.getProductCategories();
            rightStr = new HashMap<>();
            if (leftStr!=null){
                tv_line.setBackgroundColor(getResources().getColor(R.color.line_gray));

                List<String> str = new ArrayList<>();
                for (int i= 0 ;i<leftStr.size();i++){
                    CProductCategory cProductCategory = leftStr.get(i);
                    str.add(cProductCategory.getName());
                    //rightStr赋值
                    rightStr.put(leftStr.get(i).getId(),leftStr.get(i).getcProducts());
                    //Log.e(leftStr.get(i).getcProducts().toString(),"=================");
                }
                left_listView.setAdapter(new ArrayAdapter<>(this,
                        R.layout.item_mine_list_left, R.id.tv_left_content, str));
                sectionedAdapter.setLeftStr(leftStr);
                sectionedAdapter.setRightStr(rightStr);
            }
        }
        if (requestCode==3){
            MessageUtils.showShortToast(this,"商品操作成功");
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Object id = buttonView.getTag();
        String storeId = id.toString();
        if (isChecked){
            sdao.UpdateProduct(storeId,"1");
            MessageUtils.showShortToast(this, "商品上架");
        }else{
            sdao.UpdateProduct(storeId,"0");
            MessageUtils.showShortToast(this, "商品下架");
        }
    }
}
