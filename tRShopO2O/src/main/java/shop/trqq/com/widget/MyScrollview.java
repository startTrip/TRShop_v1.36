package shop.trqq.com.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.ScrollView;

/*
 * 重写兼容ViewPager的ScrollView*/
public class MyScrollview extends ScrollView {
    // 滑动距离及坐标
    private float xDistance, yDistance, xLast, yLast;

    public MyScrollview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    /**
     * 能够兼容ViewPager的ScrollView
     *
     * @Description: 解决了ViewPager在ScrollView中的滑动反弹问题
     */
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();

                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;
                // 判断角度,可以根据需求自己去建立临界值来作为判断依据即可。
                if (xDistance > yDistance) {
                    return false;
                }
        }

        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 判断ScrollView是否滚动到底部!滚到底部不夺取父控件滚动事件 注意事项: 1
     * ScrollView.getChildAt(0).getMeasuredHeight()表示:
     * ScrollView所占的高度.即ScrollView内容的高度.常常有一 部分内容要滑动后才可见,这部分的高度也包含在了
     * ScrollView.getChildAt(0).getMeasuredHeight()中
     *
     * 2 view.getScrollY表示: ScrollView顶端已经滑出去的高度
     *
     * 3 view.getHeight()表示: ScrollView的可见高度 　　
     * protected int computeVerticalScrollRange () 　　滚动视图的可滚动范围是所有子元素的高度。 　
     * 返回值 由垂直方向滚动条代表的所有垂直范围，缺省的范围是当前视图的画图高度。
     *
     */
    /*@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		//int scrollViewMeasuredHeight = getChildAt(0).getMeasuredHeight();
		// 滚到底部不夺取父控件触摸事件
		if (t + getHeight() >= computeVerticalScrollRange()) {
			final ViewParent parent = getParent();
			if (parent != null) {
				parent.requestDisallowInterceptTouchEvent(false);
			}
		} else {
			final ViewParent parent = getParent();
			if (parent != null) {
				parent.requestDisallowInterceptTouchEvent(true);
			}
		}
		super.onScrollChanged(l, t, oldl, oldt);
	}*/
    //接口方式
	/*
	private OnBorderListener onBorderListener;
    private View             contentView;
    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        doOnBorderListener();
    }
    public void setOnBorderListener(final OnBorderListener onBorderListener) {
        this.onBorderListener = onBorderListener;
        if (onBorderListener == null) {
            return;
        }

        if (contentView == null) {
            contentView = getChildAt(0);
        }
    }

    *//**
     * OnBorderListener, Called when scroll to top or bottom
     *
     * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-5-22
     *//*
    public static interface OnBorderListener {

        *//**
     * Called when scroll to bottom
     *//*
        public void onBottom();

        */

    /**
     * Called when scroll to top
     *//*
        public void onTop();
    }

    private void doOnBorderListener() {
    	final ViewParent parent = getParent();
        if (contentView != null && contentView.getMeasuredHeight() <= getScrollY() + getHeight()) {
                parent.requestDisallowInterceptTouchEvent(true);           
        } else {
        	 parent.requestDisallowInterceptTouchEvent(false);
        }
    }*/
    //SDK9开始 @SuppressLint("NewApi")作用仅仅是屏蔽android lint错误，所以在方法中还要判断版本做不同的操作.
    @SuppressLint("NewApi")
    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX,
                                  boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        final ViewParent parent = getParent();
        if (scrollY != 0 && clampedY) {
            parent.requestDisallowInterceptTouchEvent(false);
        } else {
            parent.requestDisallowInterceptTouchEvent(true);
        }
    }
}