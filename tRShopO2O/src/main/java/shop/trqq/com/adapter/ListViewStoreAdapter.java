package shop.trqq.com.adapter;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import shop.trqq.com.R;
import shop.trqq.com.bean.StoreListBean;
import shop.trqq.com.util.YkLog;

/**
 * �����б��������
 *
 * @author �Ǻ�
 * @created 2015-02-10
 */
public class ListViewStoreAdapter extends CommonAdapter<StoreListBean> {

    private static final String TAG = "ListViewStoreAdapter";

    /**
     * @param context
     */

    public ListViewStoreAdapter(Context context) {
        super(context, new ArrayList<StoreListBean>(), R.layout.list_store);
        YkLog.i(TAG, "�����б����������췽��");
    }

    @Override
    public void convert(ViewHolder holder, StoreListBean Bean) {
        // TODO Auto-generated method stub
        LinearLayout mLinearLayout = (LinearLayout) holder
                .getView(R.id.list_StoreLayout);
        TextView username = (TextView) holder.getView(R.id.textStoreName);
        TextView address = (TextView) holder.getView(R.id.textStoreAddress);
        username.setText(Bean.getStore_name());
        address.setText(Bean.getStore_address());
        final String Store_id = Bean.getStore_id();
/*		mLinearLayout.setOnClickListener(new OnClickListener() {
            @Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UIHelper.showStore(mContext, Store_id);
			}
		});*/
    }

}
