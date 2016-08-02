package com.ctrl.forum.customview;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.AndroidUtil;
import com.ctrl.forum.R;
import com.ctrl.forum.customview.photoview.HackyViewPager;
import com.ctrl.forum.customview.photoview.PhotoView;
import com.ctrl.forum.customview.photoview.PhotoViewAttacher;
import com.squareup.picasso.Transformation;

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
            Transformation transformation = new Transformation() {
                @Override
                public Bitmap transform(Bitmap source) {
                    int targetWidth = (AndroidUtil.getDeviceWidth(ImageZoomActivity.this));
                    int targetHeight = 500;
                    if (source.getWidth() == 0 || source.getHeight() == 0) {
                        return source;
                    }

                    if (source.getWidth() > source.getHeight()) {//横向长图
                        if (source.getHeight() < targetHeight && source.getWidth() <= 400) {
                            return source;
                        } else {
                            //如果图片大小大于等于设置的高度，则按照设置的高度比例来缩放
                            double aspectRatio = (double) source.getWidth() / (double) source.getHeight();
                            int width = (int) (targetHeight * aspectRatio);
                            if (width > 400) { //对横向长图的宽度 进行二次限制
                                width = 400;
                                targetHeight = (int) (width / aspectRatio);// 根据二次限制的宽度，计算最终高度
                            }
                            if (width != 0 && targetHeight != 0) {
                                Bitmap result = Bitmap.createScaledBitmap(source, width, targetHeight, false);
                                if (result != source) {
                                    // Same bitmap is returned if sizes are the same
                                    source.recycle();
                                }
                                return result;
                            } else {
                                return source;
                            }
                        }
                    } else {//竖向长图
                        //如果图片小于设置的宽度，则返回原图
                        if (source.getWidth() < targetWidth && source.getHeight() <= 600) {
                            return source;
                        } else {
                            //如果图片大小大于等于设置的宽度，则按照设置的宽度比例来缩放
                            double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
                            int height = (int) (targetWidth * aspectRatio);
                            if (height > 600) {//对横向长图的高度进行二次限制
                                height = 600;
                                targetWidth = (int) (height / aspectRatio);//根据二次限制的高度，计算最终宽度
                            }
                            if (height != 0 && targetWidth != 0) {
                                Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, height, false);
                                if (result != source) {
                                    // Same bitmap is returned if sizes are the same
                                    source.recycle();
                                }
                                return result;
                            } else {
                                return source;
                            }
                        }
                    }

                }

                @Override
                public String key() {
                    return "transformation" + " desiredWidth";
                }
            };

            if (isFistOpen) {
                isFistOpen = false;
                Arad.imageLoader.load(imageList.get(position))
                        .placeholder(R.mipmap.default_error).transform(transformation).into(photoView);
//                Arad.imageLoader.display(Constant. + mPaths1.get(pst).getMaxImgPath(), photoView, R.drawable.icon_default);
            } else {
                Arad.imageLoader.load(imageList.get(position))
                        .placeholder(R.mipmap.default_error).transform(transformation).into(photoView);
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














