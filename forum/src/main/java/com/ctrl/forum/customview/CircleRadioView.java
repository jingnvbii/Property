package com.ctrl.forum.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.ctrl.forum.R;

/**
 * 右上角的数字（底部导航栏【我】）
 */
public class CircleRadioView extends RadioButton {

	private int num = 0;

	public void setNum(int num) {
		this.num = num;
		invalidate();
	}

	public int getNum() {
		return num;
	}

	public CircleRadioView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}

	public CircleRadioView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public CircleRadioView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	private Paint paint = null;
	private TextPaint textPaint = null;

	private void init() {
		paint = new Paint();
		paint.setAntiAlias(true);
		textPaint = new TextPaint();
		textPaint.setTextSize(20);
		textPaint.setColor(Color.WHITE);
		textPaint.setAntiAlias(true);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		if (num > 0) {
			if (num <= 99) {
				paint.setColor(0xffff0000);
				textPaint.setTextSize(getResources().getDimension(R.dimen.text_size_radio));
				canvas.drawCircle(
						getWidth() - textPaint.measureText(num + "") * 0.5f - getResources().getDimension(R.dimen.main_tab_right),
						textPaint.getFontMetrics().bottom * 1.5f +  getPaddingTop(),
						textPaint.getTextSize(),
						paint);
				canvas.drawText(num + "",
						getWidth() - textPaint.measureText(num + "") - getResources().getDimension(R.dimen.main_tab_right),
						textPaint.getFontMetrics().bottom * 3 + getPaddingTop(),
						textPaint);
			} else {
				paint.setColor(0xffff0000);
				textPaint.setTextSize(getResources().getDimension(R.dimen.text_size_radio));
				canvas.drawCircle(getWidth() - textPaint.measureText(99 + "") * 0.5f - getResources().getDimension(R.dimen.main_tab_right),
						textPaint.getFontMetrics().bottom * 1.5f + getPaddingTop(),
						textPaint.getTextSize(),
						paint);
				canvas.drawText(99 + "",
						getWidth() - textPaint.measureText(99 + "") - getResources().getDimension(R.dimen.main_tab_right),
						textPaint.getFontMetrics().bottom * 3 + getPaddingTop(),
						textPaint);
			}
		}
	}

}
