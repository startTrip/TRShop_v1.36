package shop.trqq.com.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import shop.trqq.com.R;
import shop.trqq.com.bean.StoreClassBean;
import shop.trqq.com.util.YkLog;

/**
 * �����б��������
 *
 * @author �Ǻ�
 * @created 2015-02-10
 */
public class ListViewStoreClassAdapter extends CommonAdapter<StoreClassBean> {

    private static final String TAG = "ListViewStoreAdapter";

    /**
     * @param context
     */

    public ListViewStoreClassAdapter(Context context) {
        super(context, new ArrayList<StoreClassBean>(), R.layout.list_store);
        YkLog.i(TAG, "�����б����������췽��");
    }

    @Override
    public void convert(ViewHolder holder, StoreClassBean Bean) {
        // TODO Auto-generated method stub
        LinearLayout mLinearLayout = (LinearLayout) holder
                .getView(R.id.list_StoreLayout);
        TextView name = (TextView) holder.getView(R.id.textStoreName);
        TextView address = (TextView) holder.getView(R.id.textStoreAddress);
        address.setVisibility(View.GONE);
        name.setPadding(0, 18, 0, 18);
        name.setTextSize(20);

        name.setText(Bean.getSc_name());
        //Ӱ��android5.0 ���ˮ����Ч
/*		final String Store_id = Bean.getSc_id();
        final String Store_Name = Bean.getSc_name();
		mLinearLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UIHelper.showStore_list2(mContext,"0",Store_id,Store_Name);
			}
		});*/
    }

}
