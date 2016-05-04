package com.ctrl.android.yinfeng.ui.adressbook.tree;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.beanu.arad.utils.AndroidUtil;
import com.ctrl.android.yinfeng.R;
import com.ctrl.android.yinfeng.base.AppToolBarActivity;
import com.ctrl.android.yinfeng.dao.ContactDao;
import com.ctrl.android.yinfeng.entity.Hotline;
import com.ctrl.android.yinfeng.entity.Hotline2;
import com.ctrl.android.yinfeng.entity.Hotline3;
import com.ctrl.android.yinfeng.ui.adressbook.AdressBookSearchActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * listVIew实现树形结构
 * */
public class TreeListActivity extends AppToolBarActivity implements View.OnClickListener{
	@InjectView(R.id.keyword_text)//搜索框
	EditText keyword_text;
	@InjectView(R.id.search_btn)//搜索按钮
	Button search_btn;

	private ListView tree_list_lv;
	private TreeListAdapter treeListAdapter;

	private ArrayList<TreeItem> listTreeItem=new ArrayList<>();
	private List<Hotline>hotlineList=new ArrayList<>();
	private ContactDao cdao;

	private int mPosition = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tree_list_activity);
		/**首次进入该页不让弹出软键盘*/
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		ButterKnife.inject(this);
		//listTreeItem = getList();
        search_btn.setOnClickListener(this);
		tree_list_lv = (ListView)findViewById(R.id.tree_list_lv);
		treeListAdapter = new TreeListAdapter(this);
		cdao=new ContactDao(this);
		cdao.requestContactGroupList("", "");


		tree_list_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				mPosition = position;

				if(listTreeItem.get(position).getCategory() == 1) {
					cdao.requestContact2GroupList(listTreeItem.get(position).getId(), "");
				}else if(listTreeItem.get(position).getCategory()==2){
					cdao.requestContact3GroupList(listTreeItem.get(position).getId(), "");
				}else if(listTreeItem.get(position).getCategory()==3){
					AndroidUtil.dial(TreeListActivity.this,listTreeItem.get(position).getPhone());
				}
		/*		ArrayList<TreeItem> list = new ArrayList<TreeItem>();

				//TreeItem item = listTreeTtem.get(position);
				//Toast.makeText(TreeListActivity.this, item.getName(), Toast.LENGTH_SHORT).show();

				if(listTreeItem.get(position).getCategory() == 1){

					TreeItem item_a = new TreeItem();
					TreeItem item_b = new TreeItem();

					if(!(listTreeItem.get(position).isShowNext())){
						listTreeItem.get(position).setShowNext(true);

						ArrayList<TreeItem> listRequest = new ArrayList<TreeItem>();

						item_a.setId("a");
						item_a.setCategoryOneId(listTreeItem.get(position).getId());
						item_a.setCategoryTwoId("");
						item_a.setCategoryThreeId("");
						item_a.setCategoryFourId("");
						item_a.setFatherId(listTreeItem.get(position).getId());
						item_a.setCategory(2);
						item_a.setName("层级二(获取a)");
						item_a.setShow(true);
						item_a.setShowNext(false);

						item_b.setId("b");
						item_b.setCategoryOneId(listTreeItem.get(position).getId());
						item_b.setCategoryTwoId("");
						item_b.setCategoryThreeId("");
						item_b.setCategoryFourId("");
						item_b.setFatherId(listTreeItem.get(position).getId());
						item_b.setCategory(2);
						item_b.setName("层级二(获取b)");
						item_b.setShow(true);
						item_b.setShowNext(false);

						listRequest.add(item_a);
						listRequest.add(item_b);

						for(int i = 0, l = listTreeItem.size() ; i < l ; i ++){
							if(i == position){
								list.add(listTreeItem.get(i));
								list.addAll(listRequest);
							} else {
								list.add(listTreeItem.get(i));
							}
						}
						listTreeItem.clear();
						listTreeItem = list;

					} else {
						listTreeItem.get(position).setShowNext(false);
						for(int i = 0, l = listTreeItem.size() ; i < l ; i ++){
							if((listTreeItem.get(position).getId()).equals(listTreeItem.get(i).getCategoryOneId())){
								//
							} else {
								list.add(listTreeItem.get(i));
							}
						}
						listTreeItem.clear();
						listTreeItem = list;

					}
				} else if(listTreeItem.get(position).getCategory() == 2){

					TreeItem item_a = new TreeItem();
					TreeItem item_b = new TreeItem();

					if(!(listTreeItem.get(position).isShowNext())){
						listTreeItem.get(position).setShowNext(true);

						ArrayList<TreeItem> listRequest = new ArrayList<TreeItem>();

						item_a.setId("a");
						item_a.setCategoryOneId(listTreeItem.get(position).getCategoryOneId());
						item_a.setCategoryTwoId(listTreeItem.get(position).getId());
						item_a.setCategoryThreeId("");
						item_a.setCategoryFourId("");
						item_a.setFatherId(listTreeItem.get(position).getId());
						item_a.setCategory(3);
						item_a.setName("层级三(获取aa)");
						item_a.setShow(true);
						item_a.setShowNext(false);

						item_b.setId("b");
						item_b.setCategoryOneId(listTreeItem.get(position).getCategoryOneId());
						item_b.setCategoryTwoId(listTreeItem.get(position).getId());
						item_b.setCategoryThreeId("");
						item_b.setCategoryFourId("");
						item_b.setFatherId(listTreeItem.get(position).getId());
						item_b.setCategory(3);
						item_b.setName("层级三(获取bb)");
						item_b.setShow(true);
						item_b.setShowNext(false);

						listRequest.add(item_a);
						listRequest.add(item_b);

						for(int i = 0, l = listTreeItem.size() ; i < l ; i ++){
							if(i == position){
								list.add(listTreeItem.get(i));
								list.addAll(listRequest);
							} else {
								list.add(listTreeItem.get(i));
							}
						}
						listTreeItem.clear();
						listTreeItem = list;

					} else {
						listTreeItem.get(position).setShowNext(false);
						for(int i = 0, l = listTreeItem.size() ; i < l ; i ++){
							if((listTreeItem.get(position).getId()).equals(listTreeItem.get(i).getCategoryTwoId())){
								//
							} else {
								list.add(listTreeItem.get(i));
							}
						}
						listTreeItem.clear();
						listTreeItem = list;

					}
				} else if(listTreeItem.get(position).getCategory() == 3){

					TreeItem item_a = new TreeItem();
					TreeItem item_b = new TreeItem();

					if(!(listTreeItem.get(position).isShowNext())){
						listTreeItem.get(position).setShowNext(true);

						ArrayList<TreeItem> listRequest = new ArrayList<TreeItem>();

						item_a.setId("aaa");
						item_a.setCategoryOneId(listTreeItem.get(position).getCategoryOneId());
						item_a.setCategoryTwoId(listTreeItem.get(position).getCategoryTwoId());
						item_a.setCategoryThreeId(listTreeItem.get(position).getId());
						item_a.setCategoryFourId("");
						item_a.setFatherId(listTreeItem.get(position).getId());
						item_a.setCategory(4);
						item_a.setName("层级四(获取aaa)");
						item_a.setShow(true);
						item_a.setShowNext(false);

						item_b.setId("bbb");
						item_b.setCategoryOneId(listTreeItem.get(position).getCategoryOneId());
						item_b.setCategoryTwoId(listTreeItem.get(position).getCategoryTwoId());
						item_b.setCategoryThreeId(listTreeItem.get(position).getId());
						item_b.setCategoryFourId("");
						item_b.setFatherId(listTreeItem.get(position).getId());
						item_b.setCategory(4);
						item_b.setName("层级四(获取bbb)");
						item_b.setShow(true);
						item_b.setShowNext(false);

						listRequest.add(item_a);
						listRequest.add(item_b);

						for(int i = 0, l = listTreeItem.size() ; i < l ; i ++){
							if(i == position){
								list.add(listTreeItem.get(i));
								list.addAll(listRequest);
							} else {
								list.add(listTreeItem.get(i));
							}
						}
						listTreeItem.clear();
						listTreeItem = list;

					} else {
						listTreeItem.get(position).setShowNext(false);
						for(int i = 0, l = listTreeItem.size() ; i < l ; i ++){
							if((listTreeItem.get(position).getId()).equals(listTreeItem.get(i).getCategoryThreeId())){
								//
							} else {
								list.add(listTreeItem.get(i));
							}
						}
						listTreeItem.clear();
						listTreeItem = list;

					}
				} else if(listTreeItem.get(position).getCategory() == 4){
					Toast.makeText(TreeListActivity.this, "打电话给" + listTreeItem.get(position).getName(), Toast.LENGTH_SHORT).show();
				}

				treeListAdapter.setList(listTreeItem);
				tree_list_lv.setAdapter(treeListAdapter);
				tree_list_lv.deferNotifyDataSetChanged();*/

			}
		});

	}

	@Override
	public void onRequestSuccess(int requestCode) {
		super.onRequestSuccess(requestCode);
		ArrayList<TreeItem> list = new ArrayList<>();
		if(requestCode==0){
			List<Hotline> hotline = cdao.getListContactGroup();
			for(int i=0;i<hotline.size();i++){
				TreeItem item_1 = new TreeItem();
				item_1.setId(hotline.get(i).getId());
				item_1.setCategoryOneId("");
				item_1.setCategoryTwoId("");
				item_1.setCategoryThreeId("");
				item_1.setCategoryFourId("");
				item_1.setFatherId("");
				item_1.setCategory(1);
				item_1.setName(hotline.get(i).getName());
				item_1.setShow(true);
				item_1.setShowNext(false);
				list.add(item_1);

			}
			listTreeItem.clear();
			listTreeItem=list;
			treeListAdapter.setList(listTreeItem);
			tree_list_lv.setAdapter(treeListAdapter);
			tree_list_lv.setDividerHeight(10);

		}

		if(requestCode==1){

			List<Hotline2> hotline2 = cdao.getListContactGroup2();
			ArrayList<TreeItem> listRequest = new ArrayList<>();

			for(int i=0;i<hotline2.size();i++){
				TreeItem item_a = new TreeItem();
				item_a.setId(hotline2.get(i).getId());
				item_a.setCategoryOneId(listTreeItem.get(mPosition).getId());
				item_a.setCategoryTwoId("");
				item_a.setCategoryThreeId("");
				item_a.setCategoryFourId("");
				item_a.setFatherId("");
				item_a.setCategory(2);
				item_a.setName(hotline2.get(i).getName());
				item_a.setShow(true);
				item_a.setShowNext(false);
				listRequest.add(item_a);
			}
			if(!(listTreeItem.get(mPosition).isShowNext())){
				listTreeItem.get(mPosition).setShowNext(true);
				for(int i = 0, l = listTreeItem.size() ; i < l ; i ++){
					if(i == mPosition){
						list.add(listTreeItem.get(i));
						list.addAll(listRequest);
					} else {
						list.add(listTreeItem.get(i));
					}
				}

				listTreeItem.clear();
				listTreeItem = list;

			}  else {
				listTreeItem.get(mPosition).setShowNext(false);
				for(int i = 0, l = listTreeItem.size() ; i < l ; i ++){
					if((listTreeItem.get(mPosition).getId()).equals(listTreeItem.get(i).getCategoryOneId())){
						//
					} else {
						list.add(listTreeItem.get(i));
					}
				}
				listTreeItem.clear();
				listTreeItem = list;

			}




			//listTreeItem.clear();
			//listTreeItem = list;

			treeListAdapter.setList(listTreeItem);
			tree_list_lv.setAdapter(treeListAdapter);
			tree_list_lv.setDividerHeight(10);
			tree_list_lv.deferNotifyDataSetChanged();

		}
	if(requestCode==2){
		List<Hotline3> hotline3 = cdao.getListContactGroup3();
		ArrayList<TreeItem> listRequest3 = new ArrayList<>();

		for(int i=0;i<hotline3.size();i++){
			TreeItem item_a = new TreeItem();
			item_a.setId(hotline3.get(i).getId());
			item_a.setCategoryOneId(listTreeItem.get(mPosition).getCategoryOneId());
			item_a.setCategoryTwoId(listTreeItem.get(mPosition).getId());
			item_a.setCategoryThreeId("");
			item_a.setCategoryFourId("");
			item_a.setFatherId("");
			item_a.setCategory(3);
			item_a.setPhone(hotline3.get(i).getMobile());
			item_a.setContactorGrade(hotline3.get(i).getContactGrade());
			item_a.setName(hotline3.get(i).getContactName());
			item_a.setShow(true);
			item_a.setShowNext(false);
			listRequest3.add(item_a);
		}
		if(!(listTreeItem.get(mPosition).isShowNext())){
			listTreeItem.get(mPosition).setShowNext(true);
			for(int i = 0, l = listTreeItem.size() ; i < l ; i ++){
				if(i == mPosition){
					list.add(listTreeItem.get(i));
					list.addAll(listRequest3);
				} else {
					list.add(listTreeItem.get(i));
				}
			}

			listTreeItem.clear();
			listTreeItem = list;

		}  else {
			listTreeItem.get(mPosition).setShowNext(false);
			for(int i = 0, l = listTreeItem.size() ; i < l ; i ++){
				if((listTreeItem.get(mPosition).getId()).equals(listTreeItem.get(i).getCategoryTwoId())){
					//
				} else {
					list.add(listTreeItem.get(i));
				}
			}
			listTreeItem.clear();
			listTreeItem = list;

		}




		//listTreeItem.clear();
		//listTreeItem = list;

		treeListAdapter.setList(listTreeItem);
		tree_list_lv.setAdapter(treeListAdapter);
		tree_list_lv.setDividerHeight(10);
		tree_list_lv.deferNotifyDataSetChanged();

	}
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case  R.id.search_btn:
			Intent intent1 = new Intent(TreeListActivity.this, AdressBookSearchActivity.class);
			intent1.putExtra("keyWord",keyword_text.getText().toString());
			startActivity(intent1);
			keyword_text.setText("");
			break;

//		case R.id.ad_ListView_btn:
//			//Toast.makeText(this, "IOS弹框", Toast.LENGTH_SHORT).show();
//			Intent intent1 = new Intent(TreeListActivity.this, ListViewWithAdsActivity.class);
//			startActivity(intent1);
//			break;

		default:
			break;
		}
	}

	@Override
	public void finish() {
		super.finish();
	}

	private ArrayList<TreeItem> getList(){
		ArrayList<TreeItem> list = new ArrayList<>();

		TreeItem item_1 = new TreeItem();
		item_1.setId("1");
		item_1.setCategoryOneId("");
		item_1.setCategoryTwoId("");
		item_1.setCategoryThreeId("");
		item_1.setCategoryFourId("");
		item_1.setFatherId("");
		item_1.setCategory(1);
		item_1.setName("层级一");
		item_1.setShow(true);
		item_1.setShowNext(false);
		list.add(item_1);

//		TreeItem item_2 = new TreeItem();
//		item_2.setId("2");
//		item_2.setCategoryOneId("1");
//		item_2.setCategoryTwoId("");
//		item_2.setCategoryThreeId("");
//		item_2.setCategoryFourId("");
//		item_2.setFatherId("1");
//		item_2.setCategory(2);
//		item_2.setName("层级二");
//		item_2.setShow(true);
//		item_2.setShowNext(true);
//		list.add(item_2);
//
//		TreeItem item_3 = new TreeItem();
//		item_3.setId("3");
//		item_3.setCategoryOneId("1");
//		item_3.setCategoryTwoId("2");
//		item_3.setCategoryThreeId("");
//		item_3.setCategoryFourId("");
//		item_3.setFatherId("2");
//		item_3.setCategory(3);
//		item_3.setName("层级三");
//		item_3.setShow(true);
//		item_3.setShowNext(true);
//		list.add(item_3);
//
//		TreeItem item_4 = new TreeItem();
//		item_4.setId("4");
//		item_4.setCategoryOneId("1");
//		item_4.setCategoryTwoId("2");
//		item_4.setCategoryThreeId("3");
//		item_4.setCategoryFourId("");
//		item_4.setFatherId("3");
//		item_4.setCategory(4);
//		item_4.setName("层级四");
//		item_4.setShow(true);
//		item_4.setShowNext(true);
//		list.add(item_4);

		TreeItem item_11 = new TreeItem();
		item_11.setId("11");
		item_11.setCategoryOneId("");
		item_11.setCategoryTwoId("");
		item_11.setCategoryThreeId("");
		item_11.setCategoryFourId("");
		item_11.setFatherId("3");
		item_11.setCategory(1);
		item_11.setName("层级一");
		item_11.setShow(true);
		item_11.setShowNext(false);
		list.add(item_11);

//		TreeItem item_22 = new TreeItem();
//		item_22.setId("22");
//		item_22.setCategoryOneId("11");
//		item_22.setCategoryTwoId("");
//		item_22.setCategoryThreeId("");
//		item_22.setCategoryFourId("");
//		item_22.setFatherId("11");
//		item_22.setCategory(2);
//		item_22.setName("层级二");
//		item_22.setShow(true);
//		item_22.setShowNext(true);
//		list.add(item_22);
//
//		TreeItem item_33 = new TreeItem();
//		item_33.setId("33");
//		item_33.setCategoryOneId("11");
//		item_33.setCategoryTwoId("22");
//		item_33.setCategoryThreeId("");
//		item_33.setCategoryFourId("");
//		item_33.setFatherId("22");
//		item_33.setCategory(3);
//		item_33.setName("层级三");
//		item_33.setShow(false);
//		item_33.setShowNext(true);
//		list.add(item_33);
//
//		TreeItem item_44 = new TreeItem();
//		item_44.setId("44");
//		item_44.setCategoryOneId("11");
//		item_44.setCategoryTwoId("22");
//		item_44.setCategoryThreeId("33");
//		item_44.setCategoryFourId("");
//		item_44.setFatherId("33");
//		item_44.setCategory(4);
//		item_44.setName("层级四");
//		item_44.setShow(false);
//		item_44.setShowNext(true);
//		list.add(item_44);

		TreeItem item_111 = new TreeItem();
		item_111.setId("111");
		item_111.setCategoryOneId("");
		item_111.setCategoryTwoId("");
		item_111.setCategoryThreeId("");
		item_111.setCategoryFourId("");
		item_111.setFatherId("");
		item_111.setCategory(1);
		item_111.setName("层级一");
		item_111.setShow(true);
		item_111.setShowNext(false);
		list.add(item_111);

//		TreeItem item_222 = new TreeItem();
//		item_222.setId("222");
//		item_222.setCategoryOneId("111");
//		item_222.setCategoryTwoId("");
//		item_222.setCategoryThreeId("");
//		item_222.setCategoryFourId("");
//		item_222.setFatherId("111");
//		item_222.setCategory(2);
//		item_222.setName("层级二");
//		item_222.setShow(false);
//		item_222.setShowNext(true);
//		list.add(item_222);
//
//		TreeItem item_333 = new TreeItem();
//		item_333.setId("333");
//		item_333.setCategoryOneId("111");
//		item_333.setCategoryTwoId("222");
//		item_333.setCategoryThreeId("");
//		item_333.setCategoryFourId("");
//		item_333.setFatherId("222");
//		item_333.setCategory(3);
//		item_333.setName("层级三");
//		item_333.setShow(false);
//		item_333.setShowNext(true);
//		list.add(item_333);
//
//		TreeItem item_444 = new TreeItem();
//		item_444.setId("444");
//		item_444.setCategoryOneId("111");
//		item_444.setCategoryTwoId("222");
//		item_444.setCategoryThreeId("333");
//		item_444.setCategoryFourId("");
//		item_444.setFatherId("333");
//		item_444.setCategory(4);
//		item_444.setName("层级四");
//		item_444.setShow(false);
//		item_444.setShowNext(true);
//		list.add(item_444);

		return list;
	}

	@Override
	public String setupToolBarTitle() {
		return "通讯录";
	}

	@Override
	public boolean setupToolBarLeftButton(ImageView leftButton) {
		leftButton.setImageResource(R.mipmap.white_arrow_left_none);
		leftButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//MessageUtils.showShortToast(MallShopActivity.this, "AA");
				onBackPressed();
			}
		});
		return true;
	}
}
