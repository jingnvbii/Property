package com.ctrl.forum.ui.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.AndroidUtil;
import com.ctrl.forum.R;
import com.ctrl.forum.entity.Post;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Transformation;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/8/2.
 */
public class MasonryAdapter extends RecyclerView.Adapter<MasonryAdapter.MasonryView>{
    private  Activity context;
    private List<Post> products;
    HashMap<Integer, Integer> indexMap = new HashMap<Integer, Integer>();
  //  HashMap<Integer, Bitmap> bitmapMap = new HashMap<Integer, Bitmap>();
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;


    public MasonryAdapter(Activity context) {
        this.context=context;
    }
    
    public void setList(List<Post>postList){
        this.products=postList;
        notifyDataSetChanged();
       // notifyItemRangeInserted();
    }

    public void addDatas(List<Post>postList) {
        postList.addAll(postList);
        notifyDataSetChanged();
    }


    private View mHeaderView;

    private OnItemClickListener mOnItemClickListener;//声明接口
    public interface OnItemClickListener {
        void ItemClickListener(View view, int postion);

        void ItemLongClickListener(View view, int postion);
    }

    public void setOnClickListener(OnItemClickListener onitemclicklistener) {
        this.mOnItemClickListener = onitemclicklistener;
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    @Override
    public int getItemViewType(int position) {
        if(mHeaderView == null) return TYPE_NORMAL;
        if(position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }

   /* @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mHeaderView != null && viewType == TYPE_HEADER) return new Holder(mHeaderView);
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pinterest, parent, false);
        return new Holder(layout);
    }*/
    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }



    @Override
    public MasonryView onCreateViewHolder(ViewGroup viewGroup, int i) {
        if(mHeaderView != null && i == TYPE_HEADER) return new MasonryView(mHeaderView);
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_pinterest, viewGroup, false);
        return new MasonryView(view);
    }

  /*  @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if(manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) == TYPE_HEADER
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(MasonryView holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if(lp != null
                && lp instanceof StaggeredGridLayoutManager.LayoutParams
                && holder.getLayoutPosition() == 0) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
    }
    }*/

    @Override
    public void onBindViewHolder(final MasonryView masonryView, final int position) {
       // resizeItemView(masonryView.iv_pinerest_style_image,getScaleType(position));
       // if(getItemViewType(position1) == TYPE_HEADER) return;
      //  final int position = getRealPosition(masonryView);

        if(indexMap.get(position)!=null&&indexMap.get(position)>0){
          //  masonryView.rl_content.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams params = masonryView.iv_pinerest_style_image.getLayoutParams();
            params.height=indexMap.get(position);
            masonryView.iv_pinerest_style_image.setLayoutParams(params);
            //  masonryView.iv_pinerest_style_image.setImageBitmap(bitmapMap.get(position));
            //  return;
        }
        Post post=products.get(position);
        Transformation transformation = new Transformation() {

            @Override
            public Bitmap transform(Bitmap source) {
                int targetWidth = (AndroidUtil.getDeviceWidth(context)-48)/2;
                int targetHeight = 300;
                if (source.getWidth() == 0 || source.getHeight() == 0) {
                    indexMap.put(position,source.getHeight());
                //    bitmapMap.put(position,source);
                    return source;
                }

                if (source.getWidth() > source.getHeight()) {//横向长图1
                    if (source.getHeight() < targetHeight && source.getWidth() <= 400) {
                        indexMap.put(position, source.getHeight());
                      //  bitmapMap.put(position,source);
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
                            indexMap.put(position,result.getHeight());
                         //   bitmapMap.put(position,result);
                            return result;
                        } else {
                            indexMap.put(position,source.getHeight());
                          //  bitmapMap.put(position,source);
                            return source;
                        }
                    }
                } else {//竖向长图
                    //如果图片小于设置的宽度，则返回原图
                    if (source.getWidth() < targetWidth && source.getHeight() <= 600) {
                        indexMap.put(position,source.getHeight());
                     //   bitmapMap.put(position,source);
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
                            indexMap.put(position,result.getHeight());
                          //  bitmapMap.put(position,result);
                            return result;
                        } else {
                            indexMap.put(position,source.getHeight());
                          //  bitmapMap.put(position,source);
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

        masonryView.tv_pinerest_style_title.setText(post.getTitle());
        masonryView.tv_pinerest_style_zan.setText(post.getPraiseNum() + "");
        if(post.getPostImgList()!=null){
            masonryView.iv_pinerest_style_image.setVisibility(View.VISIBLE);
            masonryView.rl_content.setVisibility(View.GONE);
            masonryView.tv_pinerest_style_imagenum.setText(post.getPostImgList().size() + " 图");

        if(post.getPostImgList()!=null&&!post.getPostImgList().get(0).getImg().equals("")){
           // Log.i("tag","position==="+position);
         //  Log.i("tag","url==="+products.get(position).getPostImgList().get(0).getImg());

            Arad.imageLoader.load(post.getPostImgList().get(0).getImg()).transform(transformation).into(masonryView.iv_pinerest_style_image, new Callback() {
                @Override
                public void onSuccess() {
                    masonryView.rl_content.setVisibility(View.VISIBLE);
                   /* ((ViewGroup.MarginLayoutParams)masonryView.iv_pinerest_style_image.getLayoutParams()).setMargins(10,10,10,10);
                    ((ViewGroup.MarginLayoutParams)masonryView.rl_content.getLayoutParams()).setMargins(10, 10, 10, 10);*/
                }

                @Override
                public void onError() {

                }
            });

    }
        }else {
            masonryView.iv_pinerest_style_image.setVisibility(View.GONE);
            masonryView.rl_content.setVisibility(View.GONE);
        }

        if (mOnItemClickListener != null) {
            masonryView.iv_pinerest_style_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = masonryView.getLayoutPosition();//得到当前点击item的位置pos
                    mOnItemClickListener.ItemClickListener(masonryView.itemView, pos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(products==null){return 0;}
        else {
            return mHeaderView==null?products.size():products.size()+1;
        }
      //
    }


/*
    private float getScaleType(int position) {
        if (!indexMap.containsKey(position)) {
            float scaleType;
            if (hasHeader()) {
                if (position == 1) {
                    scaleType = SIZE_SCALE_01;
                } else if (position == 2) {
                    scaleType = SIZE_SCALE_02;
                } else {
                    scaleType = MeasureUtil.getRandomInt() % 2 == 0 ? SIZE_SCALE_01 : SIZE_SCALE_02;
                }
            } else {
                if (position == 0) {
                    scaleType = SIZE_SCALE_01;
                } else if (position == 1) {
                    scaleType = SIZE_SCALE_02;
                } else {
                    scaleType = MeasureUtil.getRandomInt() % 2 == 0 ? SIZE_SCALE_01 : SIZE_SCALE_02;
                }
            }
            indexMap.put(position, scaleType);
        }

        return indexMap.get(position);
    }

    private boolean hasHeader() {
        return false;
    }

    private void resizeItemView(ImageView frontCoverImage, float scaleType) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) frontCoverImage.getLayoutParams();
        params.width = AndroidUtil.getDeviceWidth(context) / 2;
        params.height = (int) (params.width / scaleType) - AndroidUtil.dp2px(context, 8);
        frontCoverImage.setLayoutParams(params);
    }*/

 public static class MasonryView extends  RecyclerView.ViewHolder{
                ImageView iv_pinerest_style_image;
                TextView tv_pinerest_style_title;
                TextView tv_pinerest_style_imagenum;
                TextView tv_pinerest_style_zan;
                RelativeLayout rl_content;
        public MasonryView( View itemView){
            super(itemView);
            iv_pinerest_style_image= (ImageView) itemView.findViewById(R.id.iv_pinerest_style_image);
            tv_pinerest_style_title= (TextView) itemView.findViewById(R.id.tv_pinerest_style_title);
            tv_pinerest_style_imagenum= (TextView) itemView.findViewById(R.id.tv_pinerest_style_imagenum);
            tv_pinerest_style_zan= (TextView) itemView.findViewById(R.id.tv_pinerest_style_zan);
            rl_content= (RelativeLayout) itemView.findViewById(R.id.rl_content);

        }

    }

}