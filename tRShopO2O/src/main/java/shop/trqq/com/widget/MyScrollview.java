package shop.trqq.com.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.ScrollView;

/*
 * ��д����ViewPager��ScrollView*/
public class MyScrollview extends ScrollView {
    // �������뼰����
    private float xDistance, yDistance, xLast, yLast;

    public MyScrollview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    /**
     * �ܹ�����ViewPager��ScrollView
     *
     * @Description: �����ViewPager��ScrollView�еĻ�����������
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
                // �жϽǶ�,���Ը��������Լ�ȥ�����ٽ�ֵ����Ϊ�ж����ݼ��ɡ�
                if (xDistance > yDistance) {
                    return false;
                }
        }

        return super.onInterceptTouchEvent(ev);
    }

    /**
     * �ж�ScrollView�Ƿ�������ײ�!�����ײ�����ȡ���ؼ������¼� ע������: 1
     * ScrollView.getChildAt(0).getMeasuredHeight()��ʾ:
     * ScrollView��ռ�ĸ߶�.��ScrollView���ݵĸ߶�.������һ ��������Ҫ������ſɼ�,�ⲿ�ֵĸ߶�Ҳ��������
     * ScrollView.getChildAt(0).getMeasuredHeight()��
     *
     * 2 view.getScrollY��ʾ: ScrollView�����Ѿ�����ȥ�ĸ߶�
     *
     * 3 view.getHeight()��ʾ: ScrollView�Ŀɼ��߶� ����
     * protected int computeVerticalScrollRange () ����������ͼ�Ŀɹ�����Χ��������Ԫ�صĸ߶ȡ� ��
     * ����ֵ �ɴ�ֱ�����������������д�ֱ��Χ��ȱʡ�ķ�Χ�ǵ�ǰ��ͼ�Ļ�ͼ�߶ȡ�
     *
     */
    /*@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		//int scrollViewMeasuredHeight = getChildAt(0).getMeasuredHeight();
		// �����ײ�����ȡ���ؼ������¼�
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
    //�ӿڷ�ʽ
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
    //SDK9��ʼ @SuppressLint("NewApi")���ý���������android lint���������ڷ����л�Ҫ�жϰ汾����ͬ�Ĳ���.
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