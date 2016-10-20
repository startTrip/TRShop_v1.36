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
 * ��Ϣ�б��������
 *
 * @author �Ǻ�
 * @created 2015-02-10
 */
public class ListViewVoucherAdapter extends CommonAdapter<VoucherListBean> {

    private static final String TAG = "ListViewGoodsAdapter";

    /**
     * ������б����������췽��
     *
     * @param context
     */
    public ListViewVoucherAdapter(Context context) {
        super(context, new ArrayList<VoucherListBean>(), R.layout.list_voucher);
        YkLog.i(TAG, "������б����������췽��");
    }

    // getview�����һ������
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
        voucher_price.setText("��:" + Bean.getVoucher_price());
        voucher_desc.setText("��������" + Bean.getVoucher_limit() + "Ԫ���ã�");
        voucher_store_name.setText("����:" + Bean.getStore_name());
        long time = Long.parseLong(Bean.getVoucher_end_date());
        voucher_end_date.setText("��Ч����:" + getDateToString(time * 1000));
    }

    /* ʱ���ת�����ַ��� */
    public static String getDateToString(long time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy��MM��dd��");
        return sf.format(d);
    }

}
