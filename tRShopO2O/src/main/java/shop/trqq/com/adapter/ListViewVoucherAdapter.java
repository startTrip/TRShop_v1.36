package shop.trqq.com.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import shop.trqq.com.R;
import shop.trqq.com.bean.VoucherListBean;
import shop.trqq.com.util.ImageLoadUtils;
import shop.trqq.com.util.YkLog;

/**
 * 信息列表的适配器
 *
 * @author 星河
 * @created 2015-02-10
 */
public class ListViewVoucherAdapter extends CommonAdapter<VoucherListBean> {

    private static final String TAG = "ListViewGoodsAdapter";

    /**
     * 代金卷列表适配器构造方法
     *
     * @param context
     */
    public ListViewVoucherAdapter(Context context) {
        super(context, new ArrayList<VoucherListBean>(), R.layout.list_voucher);
        YkLog.i(TAG, "代金卷列表适配器构造方法");
    }

    // getview里面的一个方法
    @Override
    public void convert(ViewHolder holder, VoucherListBean Bean) {
        ImageView voucher_img = (ImageView) holder.getView(R.id.voucher_img);
        TextView voucher_title = (TextView) holder.getView(R.id.voucher_title);
        TextView voucher_price = (TextView) holder.getView(R.id.voucher_price);
        TextView voucher_desc = (TextView) holder.getView(R.id.voucher_desc);
        TextView voucher_store_name = (TextView) holder
                .getView(R.id.voucher_store_name);
        TextView voucher_end_date = (TextView) holder
                .getView(R.id.voucher_end_date);
        ImageLoader.getInstance().displayImage(Bean.getVoucher_t_customimg(),
                voucher_img, ImageLoadUtils.getoptions(),
                ImageLoadUtils.getAnimateFirstDisplayListener());
        voucher_title.setText(Bean.getVoucher_title());
        voucher_price.setText("￥:" + Bean.getVoucher_price());
        voucher_desc.setText("（购物满" + Bean.getVoucher_limit() + "元可用）");
        voucher_store_name.setText("店铺:" + Bean.getStore_name());
        long time = Long.parseLong(Bean.getVoucher_end_date());
        voucher_end_date.setText("有效期至:" + getDateToString(time * 1000));
    }

    /* 时间戳转换成字符串 */
    public static String getDateToString(long time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日");
        return sf.format(d);
    }

}
