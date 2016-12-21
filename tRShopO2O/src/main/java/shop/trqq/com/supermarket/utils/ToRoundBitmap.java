package shop.trqq.com.supermarket.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;

/**
 *
 */
public class ToRoundBitmap {

    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        //Բ��ͼƬ���
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        //�����εı߳�
        int r = 0;
        //ȡ��̱����߳�
        if(width > height) {
            r = height;
        } else {
            r = width;
        }
        //����һ��bitmap
        Bitmap backgroundBmp = Bitmap.createBitmap(width,
                height, Bitmap.Config.ARGB_8888);
        //newһ��Canvas����backgroundBmp�ϻ�ͼ
        Canvas canvas = new Canvas(backgroundBmp);
        Paint paint = new Paint();
        //���ñ�Ե�⻬��ȥ�����
        paint.setAntiAlias(true);
        //�����ȣ���������
        RectF rect = new RectF(0, 0, r, r);
        //ͨ���ƶ���rect��һ��Բ�Ǿ��Σ���Բ��X�᷽��İ뾶����Y�᷽��İ뾶ʱ��
        //�Ҷ�����r/2ʱ����������Բ�Ǿ��ξ���Բ��
        canvas.drawRoundRect(rect, r/2, r/2, paint);
        //���õ�����ͼ���ཻʱ��ģʽ��SRC_INΪȡSRCͼ���ཻ�Ĳ��֣�����Ľ���ȥ��
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //canvas��bitmap����backgroundBmp��
        canvas.drawBitmap(bitmap, null, rect, paint);
        //�����Ѿ��滭�õ�backgroundBmp
        return backgroundBmp;
    }
}
