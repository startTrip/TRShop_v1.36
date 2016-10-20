package shop.trqq.com.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 解决商品详情界面多点触摸bug,会导致pointerIndex out of range的异常
 */
public class MutipleTouchVerticalViewPager extends VerticalViewPager {

    public MutipleTouchVerticalViewPager(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public MutipleTouchVerticalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private boolean mIsDisallowIntercept = false;

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        // keep the info about if the innerViews do
        // requestDisallowInterceptTouchEvent
        mIsDisallowIntercept = disallowIntercept;
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // the incorrect array size will only happen in the multi-touch
        // scenario.
        if (ev.getPointerCount() > 1 && mIsDisallowIntercept) {
            requestDisallowInterceptTouchEvent(false);
            boolean handled = super.dispatchTouchEvent(ev);
            requestDisallowInterceptTouchEvent(true);
            return handled;
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }
}
