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
 * ���ⶩ���������б��������
 *
 * @author �Ǻ�
 * @created 2015-02-10
 */
public class ListViewIndate_CodeAdapter extends CommonAdapter<Indate_CodeBean> {

    private static final String TAG = "ListViewNewsAdapter";

    /**
     * ��ע��Ʒ�б����������췽��
     *
     * @param context
     */
    public ListViewIndate_CodeAdapter(Context context) {
        super(context, new ArrayList<Indate_CodeBean>(),
                R.layout.list_indate_code);
        YkLog.i(TAG, "��ע��Ʒ�б����������췽��");
    }

    // getview�����һ������
    @Override
    public void convert(ViewHolder holder, Indate_CodeBean Bean) {
        TextView vr_code = (TextView) holder.getView(R.id.vr_code);
        TextView vr_indate = (TextView) holder.getView(R.id.vr_indate);
        vr_code.setText(Bean.getVr_code());
        String time = getDateToString(Long.parseLong(Bean.getVr_indate()) * 1000);
        vr_indate.setText("����ʱ�䣺" + time);
    }

    /* ʱ���ת�����ַ��� */
    public static String getDateToString(long time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy��MM��dd�� HH:mm");
        return sf.format(d);
    }
}
