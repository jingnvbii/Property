package com.ctrl.forum.customview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.beanu.arad.Arad;
import com.ctrl.forum.R;
import com.ctrl.forum.customview.photoview.HackyViewPager;
import com.ctrl.forum.customview.photoview.PhotoView;
import com.ctrl.forum.customview.photoview.PhotoViewAttacher;

import java.util.ArrayList;

/**
 * 图片方法放大的  activity
 * Created by Eric on 2015/10/8.
 */
public class ImageZoomActivity extends Activity{

    private ViewPager mViewPager;
    private ArrayList<String> imageList;
    private int position;

    boolean num=false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.image_zoom_viewpager);
        mViewPager = (HackyViewPager)findViewById(R.id.view_pager);
        setContentView(mViewPager);
        Bundle bundle = getIntent().getExtras();
        imageList = (ArrayList<String>)bundle.getSerializable("imageList");
        for(int i = 0 ; i < imageList.size() ; i ++){
            Log.d("demo","imageListZoom : " + imageList.get(i));
        }
        position = getIntent().getIntExtra("position",0);
        mViewPager.setAdapter(new SamplePagerAdapter());
        mViewPager.setCurrentItem(position);

    }


    class SamplePagerAdapter extends PagerAdapter {

        private boolean isFistOpen = true;

        public SamplePagerAdapter(){

        }

        @Override
        public int getCount() {
            return imageList == null ? 0 : imageList.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            photoView.setImageResource(R.mipmap.default_error);
            photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    finish();
                    //onBackPressed();
                }
            });
            if (isFistOpen) {
                isFistOpen = false;
                Arad.imageLoader.load(imageList.get(position))
                        .placeholder(R.mipmap.default_error).into(photoView);
//                Arad.imageLoader.display(Constant. + mPaths1.get(pst).getMaxImgPath(), photoView, R.drawable.icon_default);
            } else {
                Arad.imageLoader.load(imageList.get(position))
                        .placeholder(R.mipmap.default_error).into(photoView);
//                Arad.imageLoader.display(Constant.IMGPATH + mPaths1.get(position).getMaxImgPath(), photoView, R.drawable.icon_default);
            }
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    public boolean isViewPagerActive(){
        return (mViewPager != null && mViewPager instanceof HackyViewPager);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        if(isViewPagerActive()){

        }
        super.onSaveInstanceState(outState);
    }
}














