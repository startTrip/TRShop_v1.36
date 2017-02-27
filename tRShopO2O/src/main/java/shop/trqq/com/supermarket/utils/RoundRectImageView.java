package shop.trqq.com.supermarket.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * Project: TRShop_v1.36
 * Author: wm
 * Data:   2017/2/24
 */

// 实现圆角矩形
public class RoundRectImageView extends ImageView {


    private Paint paint;

    public RoundRectImageView(Context context) {
        this(context,null);
    }

    public RoundRectImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint  = new Paint();
        int color = 0xff424242;
        paint.setColor(color);
        paint.setAntiAlias(true);
    }


    /**
     * 绘制圆角矩形图片
     */
    @Override
    protected void onDraw(Canvas canvas) {

        Drawable drawable = getDrawable();
        if (null != drawable) {
            Bitmap bitmap = drawableToBitmap(drawable);
            Bitmap b = getRoundBitmap(bitmap, 14);
            final Rect rectSrc = new Rect(0, 0, b.getWidth(), b.getHeight());
            final Rect rectDest = new Rect(0,0,getWidth(),getHeight());
            paint.reset();
            canvas.drawBitmap(b, rectSrc, rectDest, paint);

        } else {
            super.onDraw(canvas);
        }
    }

    // drawable 转换成 Bitmap
    public  Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap;
        int width = Math.max(drawable.getIntrinsicWidth(), 2);
        int height = Math.max(drawable.getIntrinsicHeight(), 2);
        try {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("log", "Failed to create bitmap from drawable!");
            bitmap = null;
        }

        return bitmap;
    }


    /**
     * 获取圆角矩形图片方法
     * @param bitmap
     * @param roundPx,一般设置成14
     * @return Bitmap
     * @author caizhiming
     */
    private Bitmap getRoundBitmap(Bitmap bitmap, int roundPx) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);


        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);

        int x = bitmap.getWidth();

        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;

    }

}




