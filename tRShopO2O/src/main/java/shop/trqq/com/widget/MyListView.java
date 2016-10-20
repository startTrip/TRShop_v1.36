package shop.trqq.com.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/*
 * 重写解决ScrollView显示不全*/
public class MyListView extends ListView {
    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    /**
     * 重写该方法，达到使ListView适应ScrollView的效果
     * 这样做的话，ListView的回收机制失去了意义，数据一多就悲剧了
     * 最好把想一起滚动的东西放入ListView，Header、Footer、Item都行！
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}