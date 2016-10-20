package com.roamer.slidelistview;

import android.content.Context;
import android.util.AttributeSet;

/*
 * 重写解决ScrollView显示不全*/
public class MySlideListView extends SlideListView {

	public MySlideListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MySlideListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MySlideListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	/**
	 * 重写该方法，达到使ListView适应ScrollView的效果
	 */
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}