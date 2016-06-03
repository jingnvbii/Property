package com.ctrl.forum.ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.beanu.arad.Arad;
import com.ctrl.forum.R;
import com.ctrl.forum.entity.Image2;

import java.util.List;

/**
 * 图片切换viewpager adapter
 * Created by apple on 2014-12-15.
 */
public class CommdityImageViewPagerAdapter extends PagerAdapter {
    private final List<Image2> listImage;
    private List<View>views;

    public CommdityImageViewPagerAdapter(List<View>views,List<Image2>listImage){
      this.views=views;
      this.listImage=listImage;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
       View view=views.get(position);
        ImageView imageView=(ImageView)view.findViewById(R.id.icon);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        Arad.imageLoader.load(listImage.get(position).getImg()).placeholder(R.mipmap.default_error).into(imageView);
        container.addView(view);
        return views.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
       container.removeView(views.get(position));
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
}
