package shop.trqq.com.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;

import shop.trqq.com.R;
import shop.trqq.com.UserManager;
import shop.trqq.com.bean.StoreFavoritesListBean;
import shop.trqq.com.ui.Base.UIHelper;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.ImageLoadUtils;
import shop.trqq.com.util.ToastUtils;
import shop.trqq.com.util.YkLog;
import shop.trqq.com.widget.DialogTool;

/**
 * �ղصĵ����б��������
 *
 * @author �Ǻ�
 * @created 2015-02-10
 */
public class ListViewFavoritesStoreAdapter extends
        CommonAdapter<StoreFavoritesListBean> {

    private static final String TAG = "ListViewFavoritesStoreAdapter";

    /**
     * �ղصĵ����б��췽��
     *
     * @param context
     */
    public ListViewFavoritesStoreAdapter(Context context) {
        super(context, new ArrayList<StoreFavoritesListBean>(),
                R.layout.list_voucher);
        YkLog.i(TAG, "�ղصĵ����б��췽��");
    }

    // getview�����һ������
    @Override
    public void convert(ViewHolder holder, StoreFavoritesListBean Bean) {
        LinearLayout voucherlist_layout = (LinearLayout) holder
                .getView(R.id.voucherlist_layout);
        ImageView store_avatar = (ImageView) holder.getView(R.id.voucher_img);
        TextView store_name = (TextView) holder.getView(R.id.voucher_title);
        LinearLayout voucher_priceLayout = (LinearLayout) holder
                .getView(R.id.voucher_priceLayout);
        TextView goods_count = (TextView) holder
                .getView(R.id.voucher_store_name);
        TextView fav_time_text = (TextView) holder
                .getView(R.id.voucher_end_date);
        ImageLoader.getInstance().displayImage(Bean.getStore_avatar_url(),
                store_avatar, ImageLoadUtils.getoptions(),
                ImageLoadUtils.getAnimateFirstDisplayListener());
        store_name.setText(Bean.getStore_name());
        voucher_priceLayout.setVisibility(View.GONE);
        goods_count.setText("��Ʒ����" + Bean.getGoods_count() + " �ղ�����"
                + Bean.getStore_collect());
        fav_time_text.setText("�ղ�ʱ�䣺" + Bean.getFav_time_text());
        final String Store_id = Bean.getStore_id();
        voucherlist_layout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                UIHelper.showStore(mContext, Store_id);
            }
        });
        final int dPosition = holder.getmPosition();
        voucherlist_layout.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                DialogTool.createNormalDialog2(mContext,
                        "ȷ���Ƴ���ע�ĵ�����",
                        "ȷ��",
                        "ȡ��",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                delFavoritesData(Store_id, dPosition);
                            }},
                        null).show();
               /* AlertDialog.Builder builder = new AlertDialog.Builder(
                        new ContextThemeWrapper(
                                mContext,
                                android.support.v7.appcompat.R.style.Theme_AppCompat_Light_DialogWhenLarge));
                builder.setTitle("�Ƿ�ɾ����ע")
                        .setMessage("ȷ���Ƴ���ע�ĵ�����")
                        .setNegativeButton("ȷ��",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // TODO Auto-generated method stub
                                        delFavoritesData(Store_id, dPosition);
                                    }
                                }).setPositiveButton("ȡ��", null).show();*/
                return false;
            }
        });
    }

    // ɾ���ղ�
    private void delFavoritesData(String store_id, final int dPosition) {
        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        requestParams.add("store_id", store_id);
        HttpUtil.post(HttpUtil.URL_STORE_FAVORITES_DEL, requestParams,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          byte[] responseBody) {
                        try {
                            String jsonString = new String(responseBody);
                            // TestUtils.println_err(TAG,jsonString);
                            try {
                                JSONObject jsonObject = new JSONObject(
                                        jsonString).getJSONObject("datas");
                                String errStr = jsonObject.getString("error");
                                if (errStr != null) {
                                    ToastUtils.showMessage(mContext, errStr);
                                }
                            } catch (Exception e) {
                                if (new JSONObject(jsonString).getString(
                                        "datas").equals("1")) {
                                    ToastUtils.showMessage(mContext, "�ɹ�ȡ���ղ�");
                                    mData.remove(dPosition);
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
                        ToastUtils.showMessage(mContext, R.string.get_informationData_failure);
                    }
                });
    }

}
