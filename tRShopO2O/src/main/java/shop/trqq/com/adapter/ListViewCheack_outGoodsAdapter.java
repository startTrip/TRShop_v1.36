package shop.trqq.com.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import shop.trqq.com.R;
import shop.trqq.com.bean.Goods_listBean;
import shop.trqq.com.util.ImageLoadUtils;
import shop.trqq.com.util.YkLog;

/**
 * 信息列表的适配器
 *
 * @author 星河
 * @created 2015-02-10
 */
public class ListViewCheack_outGoodsAdapter extends
        CommonAdapter<Goods_listBean> {

    private static final String TAG = "ListViewNewsAdapter";
    Float freightNum = null;
    private Handler handler;

    /**
     * 新闻列表适配器构造方法
     *
     * @param mGoodsBeanList
     * @param context
     */
    public ListViewCheack_outGoodsAdapter(Context context, Handler handler) {
        super(context, new ArrayList<Goods_listBean>(), R.layout.list_favorites);
        this.handler = handler;
        YkLog.i(TAG, "商品列表适配器构造方法");
    }

    // getview里面的一个方法
    @Override
    public void convert(ViewHolder holder, Goods_listBean Bean) {
        LinearLayout listLayout = (LinearLayout) holder
                .getView(R.id.list_layout);
        ImageView goodsImg = (ImageView) holder.getView(R.id.favorites_img);
        TextView goodsTitle = (TextView) holder.getView(R.id.favorites_title);
        TextView goodsprice = (TextView) holder.getView(R.id.favorites_price);
        TextView goodsnum = (TextView) holder.getView(R.id.favorites_good_num);
        ImageLoader.getInstance().displayImage(Bean.getGoods_image_url(),
                goodsImg, ImageLoadUtils.getoptions(),
                ImageLoadUtils.getAnimateFirstDisplayListener());
        goodsTitle.setText(Bean.getGoods_name());
        goodsprice.setText("单价(元) ￥:" + Bean.getGoods_price());
        goodsnum.setText("数量: " + Bean.getGoods_num());
        goodsnum.setVisibility(View.VISIBLE);
        // 常量.equals(变量) .因为变量.equals(常量),当变量为null的时候会有NullPointerException
        if ("1".equals(Bean.getIs_fcode())) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putBoolean("Is_fcode", true);
            msg.setData(bundle);
            msg.what = 4;
            handler.sendMessage(msg);
        }
        // freightNum=freightNum+Float.parseFloat(Bean.getGoods_freight());
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        // TODO Auto-generated method stub

    }

}
