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
 * ImageLoad工具类
 *
 * @version 2012-5-21 下午9:21:01
 */
public class ImageLoadUtils {
    private static AnimateFirstDisplayListener mAnimateFirstDisplayListener;
    // 图片加载工具
    private static DisplayImageOptions options;

    public static DisplayImageOptions getoptions() {
        options = new DisplayImageOptions.Builder()
                // 设置图片在下载期间显示的图片
                .showImageOnLoading(R.color.img_loading)
                // 设置图片Uri为空或是错误的时候显示的图片
                .showImageForEmptyUri(R.color.img_loading)
                // 设置图片加载/解码过程中错误时候显示的图片
                .showImageOnFail(R.color.img_loading).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true).build();
        return options;
    }

    //  头像图片加载工具
    private static DisplayImageOptions useroptions;

    public static DisplayImageOptions getUserOptions() {
        useroptions = new DisplayImageOptions.Builder()
                // 设置头像在下载期间显示的图片
                .showImageOnLoading(R.drawable.default_user_portrait)
                // 设置头像Uri为空或是错误的时候显示的图片
                .showImageForEmptyUri(R.drawable.default_user_portrait)
                // 设置头像加载/解码过程中错误时候显示的图片
                .showImageOnFail(R.drawable.default_user_portrait).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true).build();
        return useroptions;
    }

    /**
     * 图片加载监听事件
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
                    FadeInBitmapDisplayer.animate(imageView, 500); // 设置image隐藏动画500ms
                    displayedImages.add(imageUri); // 将图片uri添加到集合中
                }
            }
        }
    }

    public static AnimateFirstDisplayListener getAnimateFirstDisplayListener() {
        mAnimateFirstDisplayListener = new AnimateFirstDisplayListener();
        return mAnimateFirstDisplayListener;
    }

    public static void clearImageLoader(Context context) {
        YkLog.i("ImageLoader", "清除ImageLoader");
        ImageLoader.getInstance().clearDiskCache();
        ImageLoader.getInstance().clearMemoryCache();
    }

}