package com.ctrl.forum.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarFragment;
import com.beanu.arad.utils.AnimUtil;
import com.ctrl.forum.R;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.dao.CollectDao;
import com.ctrl.forum.entity.CollectionPost;
import com.ctrl.forum.ui.activity.Invitation.InvitationDetailFromPlatformActivity;
import com.ctrl.forum.ui.activity.Invitation.InvitationPinterestDetailActivity;
import com.ctrl.forum.ui.activity.store.StoreCommodityDetailActivity;
import com.ctrl.forum.ui.activity.store.StoreShopListVerticalStyleActivity;
import com.ctrl.forum.ui.adapter.MinePostCollectListAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

/**
 * 我的收藏-帖子收藏
 * Created by Administrator on 2016/5/13.
 */
public class MineInvitationCollectFragment extends ToolBarFragment{
    private List<CollectionPost> collectionPosts;
    private MinePostCollectListAdapter minePostCollectListAdapter;
    private PullToRefreshListView lv_content;
    private CollectDao collectDao;
    private int PAGE_NUM=1;

    public static MineInvitationCollectFragment newInstance() {
        MineInvitationCollectFragment fragment = new MineInvitationCollectFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mine_list, container, false);
        lv_content = (PullToRefreshListView) view.findViewById(R.id.lv_content);
        minePostCollectListAdapter = new MinePostCollectListAdapter(getActivity());
        lv_content.setAdapter(minePostCollectListAdapter);

        initData();

        lv_content.setMode(PullToRefreshBase.Mode.BOTH);
        lv_content.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (collectionPosts != null) {
                    collectionPosts.clear();
                    PAGE_NUM = 1;
                }
                collectDao.getCollectPostList(Arad.preferences.getString("memberId"), PAGE_NUM + "", Constant.PAGE_SIZE + "");
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (collectionPosts != null) {
                    PAGE_NUM += 1;
                    collectDao.getCollectPostList(Arad.preferences.getString("memberId"), PAGE_NUM + "", Constant.PAGE_SIZE + "");
                } else {
                    lv_content.onRefreshComplete();
                }
            }
        });

        //点击跳转
        lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                if (collectionPosts.get(position-1)!=null) {
                    if (collectionPosts.get(position-1).getContentType()!=null){
                        String type = collectionPosts.get(position-1).getContentType();
                        switch (type){
                            case "0":
                                String iType = collectionPosts.get(position-1).getSourceType();
                                if (iType.equals("0")){
                                    intent = new Intent(getActivity(), InvitationDetailFromPlatformActivity.class);
                                    intent.putExtra("id", collectionPosts.get(position-1).getPostId());
                                    startActivity(intent);
                                }else{
                                    intent = new Intent(getActivity(), InvitationPinterestDetailActivity.class);
                                    intent.putExtra("id", collectionPosts.get(position-1).getPostId());
                                    startActivity(intent);
                                }
                                break;
                            case "1":
                                Uri uri = Uri.parse(collectionPosts.get(position - 1).getArticleLink());
                                intent = new Intent(Intent.ACTION_VIEW, uri);
                                getActivity().startActivity(intent);
                                AnimUtil.intentSlidIn(getActivity());
                                break;
                            case "2":
                                intent = new Intent(getActivity(), StoreCommodityDetailActivity.class);
                                intent.putExtra("id", collectionPosts.get(position-1).getLinkItemId());
                                startActivity(intent);
                                break;
                            case "3":
                                intent = new Intent(getActivity(), StoreShopListVerticalStyleActivity.class);
                                intent.putExtra("id",collectionPosts.get(position-1).getLinkItemId());
                                getActivity().startActivity(intent);
                                AnimUtil.intentSlidIn(getActivity());
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        });

        return view;
    }

    private void initData() {
        collectDao = new CollectDao(this);
        collectDao.getCollectPostList(Arad.preferences.getString("memberId"), PAGE_NUM + "", Constant.PAGE_SIZE + "");

    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        lv_content.onRefreshComplete();
        if(requestCode == 2){
            collectionPosts = collectDao.getCollectionPost();
            if (collectionPosts!=null){
                minePostCollectListAdapter.setCollectionPosts(collectionPosts);
            }
        }
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        lv_content.onRefreshComplete();
    }
}
