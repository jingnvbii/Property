package com.ctrl.forum.loopview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.AnimUtil;
import com.ctrl.forum.R;
import com.ctrl.forum.entity.Banner;
import com.ctrl.forum.ui.activity.Invitation.InvitationPinterestDetailActivity;
import com.ctrl.forum.ui.activity.store.StoreCommodityDetailActivity;
import com.ctrl.forum.ui.activity.store.StoreShopListVerticalStyleActivity;

import java.util.List;
/**
 * @类名： HomeAutoSwitchPicHolder
 * @创建者： 史翔宇
 * @创建时间：2015年12月18日
 * @描述： 轮播图控件
 */
public class HomeAutoSwitchPicHolder extends BaseHolder<List<String>>
        implements ViewPager.OnPageChangeListener {

 
    public ViewPager mPager;

    private HomeAutoSwitchPicAdapter adapter;

   
    private LinearLayout mPointContainer;

    private List<String> mPictures;
    private AutoSwitchTask mSwitchTask;
    private int mPosition;
    private List<Banner> listBanner ;

    public HomeAutoSwitchPicHolder(Context context) {
        super(context);
       // this.mContext=context;
    }

    protected View initView() {
        View view = View.inflate(mContext, R.layout.home_autoswitchpic, null);
        mPager=(ViewPager) view.findViewById(R.id.item_home_viewPager);
        mPointContainer=(LinearLayout) view.findViewById(R.id.item_home_point_container);
        return view;
    }


    protected void refreshUI(List<String> data) {
        this.mPictures = data;
        adapter=new HomeAutoSwitchPicAdapter();
        this.mPager.setAdapter(adapter);
        addPointToContainer(data);
        this.mPager.setOnPageChangeListener(this);
        int middle = Integer.MAX_VALUE/1000;
        int extra = middle % mPictures.size();
        this.mPager.setCurrentItem(middle - extra);

        if (mSwitchTask == null)
            mSwitchTask = new AutoSwitchTask();
        //开始轮播
       this.mSwitchTask.start();




    // 给ViewPager设置touch的监听
        mPager.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    	// 希望轮播停止
                        mSwitchTask.stop();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                    	// 希望播放
                        mSwitchTask.start();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }
    /*
    *
    * 设置跳转数据
    * */
    public void  setData(List<Banner>listBanner){
       this.listBanner=listBanner;
    }




    /**
     * 给容器添加点
     * @param data
     */
    protected void addPointToContainer(List<String> data) {
        mPointContainer.removeAllViews();

        for (int i = 0; i < data.size(); i++) {
            View view = new View(UIUtils.getContext());
            view.setBackgroundResource(R.drawable.home_point_normal);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UIUtils.dip2px(6), UIUtils.dip2px(6));
            if (i != 0) {
                params.leftMargin = UIUtils.dip2px(8);
                params.bottomMargin = UIUtils.dip2px(8);
            } else {
                view.setBackgroundResource(R.drawable.home_point_select);
            }

            mPointContainer.addView(view,params);
        }
    }

    /**
     * 轮播任务
     */
    class AutoSwitchTask implements Runnable {

        public void run() {
            int item = mPager.getCurrentItem();
            mPager.setCurrentItem(++item);
            UIUtils.postDelayed(this, 5000);
        }

        public void start() {
            stop();
            UIUtils.postDelayed(this, 5000);
        }

        public void stop() {
            UIUtils.removeCallbacks(this);
        }
    }

    class HomeAutoSwitchPicAdapter extends PagerAdapter {

        @Override
        public int getCount() {
           return mPictures != null ?Integer.MAX_VALUE : 0;
          //return mPictures != null ? mPictures.size() : 0;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position = position % mPictures.size();
            if(position==0){
                mPosition=mPictures.size()-1;
            }else {
                mPosition=position-1;
            }
            ImageView iv = new ImageView(UIUtils.getContext());
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            // 设置网络图片
            if(mPictures.get(position)!=null&&!mPictures.get(position).equals(""))
            Arad.imageLoader.load(mPictures.get(position)).placeholder(R.mipmap.default_error).into(iv);
            container.addView(iv, 0);

            if (listBanner.get(mPosition).getType()!=null) {
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //  MessageUtils.showShortToast(mContext,"dfsdf"+mPosition);
                        String type = listBanner.get(mPosition).getType();
                        Intent intent = null;
                        switch (type) {
                            case "0"://跳商家
                                intent = new Intent(mContext, StoreShopListVerticalStyleActivity.class);
                                intent.putExtra("id", listBanner.get(mPosition).getTargetId());
                                mContext.startActivity(intent);
                                AnimUtil.intentSlidIn((Activity) mContext);
                                break;
                            case "1"://跳商品详情
                                intent = new Intent(mContext, StoreCommodityDetailActivity.class);
                                intent.putExtra("id", listBanner.get(mPosition).getTargetId());
                                mContext.startActivity(intent);
                                AnimUtil.intentSlidIn((Activity) mContext);
                                break;
                            case "2"://跳帖子详情
                                intent = new Intent(mContext, InvitationPinterestDetailActivity.class);
                                intent.putExtra("id", listBanner.get(mPosition).getTargetId());
                                mContext.startActivity(intent);
                                AnimUtil.intentSlidIn((Activity) mContext);
                                break;
                            case "3"://外部链接
                                if (listBanner.get(mPosition).getTargetUrl().length() > 0) {
                                    Uri uri = Uri.parse(listBanner.get(mPosition).getTargetUrl());
                                    intent = new Intent(Intent.ACTION_VIEW, uri);
                                    mContext.startActivity(intent);
                                    AnimUtil.intentSlidIn((Activity) mContext);
                                }
                                break;
                        }


                    }
                });
            }

            return iv;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            container.removeView((View) object);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    	// 页面选中时

      position = position % mPictures.size();

        int count = mPointContainer.getChildCount();
        for (int i = 0; i < count; i++)
        {
            View view = mPointContainer.getChildAt(i);
            view.setBackgroundResource(i == position ? R.drawable.home_point_select
                    : R.drawable.home_point_normal);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}