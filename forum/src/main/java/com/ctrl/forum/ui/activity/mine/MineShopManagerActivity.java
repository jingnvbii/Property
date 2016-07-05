package com.ctrl.forum.ui.activity.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarActivity;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.customview.MinePinnedHeaderListView;
import com.ctrl.forum.dao.MallDao;
import com.ctrl.forum.dao.MineStoreDao;
import com.ctrl.forum.entity.Product2;
import com.ctrl.forum.entity.ProductCategroy;
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
    private Map<String,List<Product2>> rightStr ;
    private TextView tv_line;
    private MineStoreDao sdao;
    private MallDao mallDao;
    private List<ProductCategroy> leftStr; //商品分类

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
                        LinearLayout ll = (LinearLayout) left_listView.getChildAt(i);

                        TextView tv = (TextView) ll.getChildAt(1);
                        tv.setTextColor(getResources().getColor(R.color.red_bg));
                        TextView tv1 = (TextView) ll.getChildAt(0);
                        tv1.setBackgroundColor(getResources().getColor(R.color.red_bg));
                    } else {
                        left_listView.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.gray_store));

                        LinearLayout ll = (LinearLayout) left_listView.getChildAt(i);
                        TextView tv = (TextView) ll.getChildAt(1);
                        tv.setTextColor(getResources().getColor(R.color.defaultTextColor));
                        TextView tv1 = (TextView) ll.getChildAt(0);
                        tv1.setBackgroundColor(getResources().getColor(R.color.gray_store));
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
                            LinearLayout ll = (LinearLayout) left_listView.getChildAt(i);

                            TextView tv = (TextView) ll.getChildAt(1);
                            tv.setTextColor(getResources().getColor(R.color.red_bg));
                            TextView tv1 = (TextView) ll.getChildAt(0);
                            tv1.setBackgroundColor(getResources().getColor(R.color.red_bg));
                        } else {
                            left_listView.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.gray_store));
                            LinearLayout ll = (LinearLayout) left_listView.getChildAt(i);
                            TextView tv = (TextView) ll.getChildAt(1);
                            tv.setTextColor(getResources().getColor(R.color.defaultTextColor));
                            TextView tv1 = (TextView) ll.getChildAt(0);
                            tv1.setBackgroundColor(getResources().getColor(R.color.gray_store));
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
    }

    private void initData() {
        sdao = new MineStoreDao(this);
        mallDao = new MallDao(this);
        mallDao.requestProductCategroy(Arad.preferences.getString("companyId"),"");
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

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if (requestCode==9){
            leftStr = mallDao.getListProductCategroy();
            rightStr = new HashMap<>();
            if (leftStr!=null){
                tv_line.setBackgroundColor(getResources().getColor(R.color.line_gray));

                List<String> str = new ArrayList<>();
                for (int i= 0 ;i<leftStr.size();i++){
                    ProductCategroy productCategory = leftStr.get(i);
                    str.add(productCategory.getName());
                    rightStr.put(leftStr.get(i).getId(),leftStr.get(i).getProductList());
                }
                left_listView.setAdapter(new ArrayAdapter<>(this,
                        R.layout.item_mine_list_left, R.id.tv_left_content, str));
                sectionedAdapter.setLeftStr(leftStr);
                sectionedAdapter.setRightStr(rightStr);
            }
        }
        if (requestCode==3){
            if (type.equals("1")){
                MessageUtils.showShortToast(this, "商品上架成功");
                Arad.preferences.putBoolean(storeId,true);
            }else{
                MessageUtils.showShortToast(this, "商品下架成功");
                Arad.preferences.putBoolean(storeId, false);
            }
            Arad.preferences.flush();
        }
    }

    public static String type = "";
    public static String storeId = "";

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Object id = buttonView.getTag();
        storeId = id.toString();
        if (isChecked){
            sdao.UpdateProduct(storeId, "1");
            type = "1";
        }else{
            sdao.UpdateProduct(storeId, "0");
            type = "0";
        }
    }

}
