package shop.trqq.com.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.roamer.slidelistview.SlideBaseAdapter;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;

import shop.trqq.com.R;
import shop.trqq.com.UserManager;
import shop.trqq.com.bean.invoice_listBean;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.ToastUtils;
import shop.trqq.com.util.YkLog;

/**
 * 地址列表的适配器
 *
 * @author 星河
 * @created 2015-02-10
 */
public class ListViewInvoiceAdapter extends SlideBaseAdapter {

    private static final String TAG = "ListViewAddressAdapter";

    Context ListContext;
    private ArrayList<invoice_listBean> mData;

    /**
     * @param context
     */

    public ListViewInvoiceAdapter(Context context,
                                  ArrayList<invoice_listBean> data) {
        super(context);
        mData = data;
        ListContext = context;
        YkLog.i(TAG, "信息列表适配器构造方法");
    }

    @Override
    public int getFrontViewId(int position) {
        return R.layout.address_cell;
    }

    @Override
    public int getLeftBackViewId(int position) {
        return 0;
    }

    @Override
    public int getRightBackViewId(int position) {
        // TODO Auto-generated method stub
        return R.layout.list_address_backview;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setData(ArrayList<invoice_listBean> data) {
        if (data != null)
            this.mData = data;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = createConvertView(position);
            holder = new ViewHolder();
            holder.username = (TextView) convertView
                    .findViewById(R.id.textAddressName);
            holder.address = (TextView) convertView
                    .findViewById(R.id.textAddressAddress);
            holder.delete = (Button) convertView
                    .findViewById(R.id.address_back_del);
            holder.edit = (Button) convertView
                    .findViewById(R.id.address_back_edit);
            holder.edit.setVisibility(View.GONE);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.username.setText(mData.get(position).getInv_title());
        holder.address.setText(mData.get(position).getInv_content());
        if (holder.delete != null) {
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InvoiceDetele(mData.get(position).getInv_id(), position);
                }
            });
        }

        return convertView;

    }

    private void InvoiceDetele(String inv_id, final int position) {
        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        requestParams.add("inv_id", inv_id);
        HttpUtil.post(HttpUtil.URL_INVOICE_DELETE, requestParams,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          byte[] responseBody) {
                        try {
                            String jsonString = new String(responseBody);
                            System.err.println(TAG + "：" + jsonString);
                            try {
                                JSONObject jsonObject = new JSONObject(
                                        jsonString).getJSONObject("datas");
                                String errStr = jsonObject.getString("error");
                                if (errStr != null) {
                                    ToastUtils.showMessage(ListContext, errStr);
                                }
                            } catch (Exception e) {
                                if (new JSONObject(jsonString).getString(
                                        "datas").equals("1")) {
                                    ToastUtils.showMessage(ListContext, "删除成功");
                                    mData.remove(position);
                                    notifyDataSetChanged();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {
                        // TODO Auto-generated method stub
                        ToastUtils.showMessage(ListContext, R.string.get_informationData_failure);
                    }
                });
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        // TODO Auto-generated method stub

    }

    class ViewHolder {
        private TextView username;
        private TextView address;
        private Button delete;
        private Button edit;
    }

}
