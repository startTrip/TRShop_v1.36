package shop.trqq.com.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import shop.trqq.com.R;

/**
 * ImageLoad������
 *
 * @version 2012-5-21 ����9:21:01
 */
public class ImageLoadUtils {
    private static AnimateFirstDisplayListener mAnimateFirstDisplayListener;
    // ͼƬ���ع���
    private static DisplayImageOptions options;

    public static DisplayImageOptions getoptions() {
        options = new DisplayImageOptions.Builder()
                // ����ͼƬ�������ڼ���ʾ��ͼƬ
                .showImageOnLoading(R.color.img_loading)
                // ����ͼƬUriΪ�ջ��Ǵ����ʱ����ʾ��ͼƬ
                .showImageForEmptyUri(R.color.img_loading)
                // ����ͼƬ����/��������д���ʱ����ʾ��ͼƬ
                .showImageOnFail(R.color.img_loading).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true).build();
        return options;
    }

    //  ͷ��ͼƬ���ع���
    private static DisplayImageOptions useroptions;

    public static DisplayImageOptions getUserOptions() {
        useroptions = new DisplayImageOptions.Builder()
                // ����ͷ���������ڼ���ʾ��ͼƬ
                .showImageOnLoading(R.drawable.default_user_portrait)
                // ����ͷ��UriΪ�ջ��Ǵ����ʱ����ʾ��ͼƬ
                .showImageForEmptyUri(R.drawable.default_user_portrait)
                // ����ͷ�����/��������д���ʱ����ʾ��ͼƬ
                .showImageOnFail(R.drawable.default_user_portrait).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true).build();
        return useroptions;
    }

    /**
     * ͼƬ���ؼ����¼�
     **/
    public static class AnimateFirstDisplayListener extends
            SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections
                .synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view,
                                      Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500); // ����image���ض���500ms
                    displayedImages.add(imageUri); // ��ͼƬuri��ӵ�������
                }
            }
        }
    }

    public static AnimateFirstDisplayListener getAnimateFirstDisplayListener() {
        mAnimateFirstDisplayListener = new AnimateFirstDisplayListener();
        return mAnimateFirstDisplayListener;
    }

    public static void clearImageLoader(Context context) {
        YkLog.i("ImageLoader", "���ImageLoader");
        ImageLoader.getInstance().clearDiskCache();
        ImageLoader.getInstance().clearMemoryCache();
    }

}