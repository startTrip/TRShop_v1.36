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
    // ��Ӧ���˳����֪ͨ���ò���ImageLoadUtils��
    private DisplayImageOptions options;

    public MsgAdapter(Context context) {
        super(context, new ArrayList<MsgBean>(), R.layout.msg_item);
        // �ж�ImageLoader�Ƿ��ʼ��
        if (!ImageLoader.getInstance().isInited()) {
            initImageLoader(mContext);
        }

        // ͼƬ���ع���
        options = new DisplayImageOptions.Builder()
                // ����ͼƬ�������ڼ���ʾ��ͼƬ
                .showImageOnLoading(R.color.img_loading)
                // ����ͼƬUriΪ�ջ��Ǵ����ʱ����ʾ��ͼƬ
                .showImageForEmptyUri(R.color.img_loading)
                // ����ͼƬ����/��������д���ʱ����ʾ��ͼƬ
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
                ToastUtils.showMessage(mContext, "��ת������ȷ����̩��ٷ���Ʒ��Ʒ");
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
