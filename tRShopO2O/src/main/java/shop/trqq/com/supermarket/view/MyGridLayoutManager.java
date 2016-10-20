package shop.trqq.com.supermarket.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Project: TRShop_v1.36
 * Author: wm
 * Data:   2016/9/19
 */

/**
 *
 */
public class MyGridLayoutManager extends GridLayoutManager {

    public MyGridLayoutManager(Context context, int spanCount) {

         super(context, spanCount);
    }

    public MyGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {

        int height = 0;

        int childCount = getItemCount();
        for (int i = 0; i < childCount; i++) {
            View child = recycler.getViewForPosition(i);
            measureChild(child, widthSpec, heightSpec);
            if (i % getSpanCount() == 0) {
                int measuredHeight = child.getMeasuredHeight();
                height += measuredHeight;
            }
        }

        setMeasuredDimension(View.MeasureSpec.getSize(widthSpec), height);
    }
}
