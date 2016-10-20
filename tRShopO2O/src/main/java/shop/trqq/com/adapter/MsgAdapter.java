package shop.trqq.com.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import shop.trqq.com.R;
import shop.trqq.com.bean.MsgBean;
import shop.trqq.com.ui.Base.UIHelper;
import shop.trqq.com.util.ToastUtils;

public class MsgAdapter extends CommonAdapter<MsgBean> {
    // 当应用退出点击通知调用不了ImageLoadUtils类
    private DisplayImageOptions options;

    public MsgAdapter(Context context) {
        super(context, new ArrayList<MsgBean>(), R.layout.msg_item);
        // 判断ImageLoader是否初始化
        if (!ImageLoader.getInstance().isInited()) {
            initImageLoader(mContext);
        }

        // 图片加载工具
        options = new DisplayImageOptions.Builder()
                // 设置图片在下载期间显示的图片
                .showImageOnLoading(R.color.img_loading)
                // 设置图片Uri为空或是错误的时候显示的图片
                .showImageForEmptyUri(R.color.img_loading)
                // 设置图片加载/解码过程中错误时候显示的图片
                .showImageOnFail(R.color.img_loading).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true).build();
    }

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(
                context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        // config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
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

    @Override
    public void convert(ViewHolder holder, final MsgBean Bean) {
        LinearLayout leftLayout = (LinearLayout) holder
                .getView(R.id.left_layout);
        LinearLayout rightLayout = (LinearLayout) holder
                .getView(R.id.right_layout);
        TextView left_title = (TextView) holder.getView(R.id.left_title);
        TextView left_date = (TextView) holder.getView(R.id.left_date);
        TextView left_content = (TextView) holder.getView(R.id.left_content);
        ImageView left_image = (ImageView) holder.getView(R.id.left_image);
        TextView rightMsg = (TextView) holder.getView(R.id.right_msg);
        if (Bean.getType() == Bean.TYPE_RECEIVED) {
            leftLayout.setVisibility(View.VISIBLE);
            rightLayout.setVisibility(View.GONE);
            left_content.setText(Bean.getContent());
            left_title.setText(Bean.getTitle());
            // OrderAdapter.getDateToString(Long.parseLong(Bean.getDate()))
            left_date.setText(Bean.getDate());
            ImageLoader.getInstance().displayImage(Bean.getImage(), left_image,
                    options, new AnimateFirstDisplayListener());
            // System.err.println(Bean.getClicktype());
            leftLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickimg(Bean.getClicktype(), Bean.getClicktypedata());

                }
            });
        } else if (Bean.getType() == Bean.TYPE_SENT) {
            rightLayout.setVisibility(View.VISIBLE);
            leftLayout.setVisibility(View.GONE);

            rightMsg.setText(Bean.getContent());
        }
        // System.err.println(Bean.getContent()+"|"+Bean.getType());
    }

    private void clickimg(String type, String data) {
        if (type.equals("url")) {
            try {
                String a[] = data.split("goods_id=");
                UIHelper.showGoods_Detaill(mContext, a[1]);
            } catch (Exception e) {
                ToastUtils.showMessage(mContext, "跳转出错，请确认是泰润官方商品商品");
            }
        } else if (type.equals("keyword")) {
            UIHelper.showShop(mContext, data, "", "", "");
        } else if (type.equals("goods")) {
            UIHelper.showGoods_Detaill(mContext, data);
        } else if (type.equals("special")) {
            UIHelper.showSpecial(mContext, data);
        }
    }

}
