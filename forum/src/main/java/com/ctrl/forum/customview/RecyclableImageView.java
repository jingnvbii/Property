package com.ctrl.forum.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 右上角的数字（3种模式）
 */
public class RecyclableImageView extends ImageView {
	public RecyclableImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public RecyclableImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public RecyclableImageView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		setImageDrawable(null);
	}


}
