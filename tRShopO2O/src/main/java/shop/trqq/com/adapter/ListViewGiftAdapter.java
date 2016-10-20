package shop.trqq.com.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import shop.trqq.com.R;
import shop.trqq.com.bean.Gift_ArrayBean;
import shop.trqq.com.ui.Base.UIHelper;
import shop.trqq.com.util.YkLog;


/**
 * ��Ϣ�б��������
 *
 * @author �Ǻ�
 * @created 2015-02-10
 */
public class ListViewGiftAdapter extends CommonAdapter<Gift_ArrayBean> {

    private static final String TAG = "ListViewGiftAdapter";

    /**
     * ��Ʒ���������췽��
     *
     * @param context
     */
    public ListViewGiftAdapter(Context context) {
        super(context, new ArrayList<Gift_ArrayBean>(),
                R.layout.list_gift);
        YkLog.i(TAG, "��Ʒ���������췽��");
    }


    // getview�����һ������
    @Override
    public void convert(ViewHolder holder, Gift_ArrayBean Bean) {
        LinearLayout listLayout = (LinearLayout) holder
                .getView(R.id.list_gift_layout);
        TextView gift_name = (TextView) holder.getView(R.id.list_gift_name);
        TextView gift_num = (TextView) holder
                .getView(R.id.list_gift_num);
        gift_name.setText(Bean.getGift_goodsname());
        gift_num.setText(" �� " + Bean.getGift_amount());
        final String goods_id = Bean.getGift_goodsid();
        listLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.showGoods_Detaill(mContext, goods_id);
            }

        });
        listLayout.setVisibility(View.VISIBLE);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        // TODO Auto-generated method stub

    }

}
