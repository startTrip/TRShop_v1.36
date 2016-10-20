package shop.trqq.com.adapter;

import android.content.Context;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import shop.trqq.com.R;
import shop.trqq.com.bean.Indate_CodeBean;
import shop.trqq.com.util.YkLog;

/**
 * 虚拟订单可用码列表的适配器
 *
 * @author 星河
 * @created 2015-02-10
 */
public class ListViewIndate_CodeAdapter extends CommonAdapter<Indate_CodeBean> {

    private static final String TAG = "ListViewNewsAdapter";

    /**
     * 关注商品列表适配器构造方法
     *
     * @param context
     */
    public ListViewIndate_CodeAdapter(Context context) {
        super(context, new ArrayList<Indate_CodeBean>(),
                R.layout.list_indate_code);
        YkLog.i(TAG, "关注商品列表适配器构造方法");
    }

    // getview里面的一个方法
    @Override
    public void convert(ViewHolder holder, Indate_CodeBean Bean) {
        TextView vr_code = (TextView) holder.getView(R.id.vr_code);
        TextView vr_indate = (TextView) holder.getView(R.id.vr_indate);
        vr_code.setText(Bean.getVr_code());
        String time = getDateToString(Long.parseLong(Bean.getVr_indate()) * 1000);
        vr_indate.setText("过期时间：" + time);
    }

    /* 时间戳转换成字符串 */
    public static String getDateToString(long time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        return sf.format(d);
    }
}
