package shop.trqq.com.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/*
 * ��д���ScrollView��ʾ��ȫ*/
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
     * ��д�÷������ﵽʹListView��ӦScrollView��Ч��
     * �������Ļ���ListView�Ļ��ջ���ʧȥ�����壬����һ��ͱ�����
     * ��ð���һ������Ķ�������ListView��Header��Footer��Item���У�
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}