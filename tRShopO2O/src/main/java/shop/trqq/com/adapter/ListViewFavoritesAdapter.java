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
import shop.trqq.com.bean.GoodsBean;
import shop.trqq.com.ui.Base.UIHelper;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.ImageLoadUtils;
import shop.trqq.com.util.ToastUtils;
import shop.trqq.com.util.YkLog;
import shop.trqq.com.widget.DialogTool;

/**
 * ��ע��Ʒ�б��������
 *
 * @author �Ǻ�
 * @created 2015-02-10
 */
public class ListViewFavoritesAdapter extends CommonAdapter<GoodsBean> {

    private static final String TAG = "ListViewFavoritesAdapter";

    /**
     * ��ע��Ʒ�б����������췽��
     *
     * @param context
     */
    public ListViewFavoritesAdapter(Context context) {
        super(context, new ArrayList<GoodsBean>(), R.layout.list_favorites);
        YkLog.i(TAG, "��ע��Ʒ�б����������췽��");
    }


    // getview�����һ������
    @Override
    public void convert(ViewHolder holder, GoodsBean goodsBean) {
        LinearLayout listLayout = (LinearLayout) holder
                .getView(R.id.list_layout);
        ImageView goodsImg = (ImageView) holder.getView(R.id.favorites_img);
        TextView goodsTitle = (TextView) holder.getView(R.id.favorites_title);
        TextView goodsprice = (TextView) holder.getView(R.id.favorites_price);
        ImageLoader.getInstance().displayImage(goodsBean.getGoods_image_url(),
                goodsImg, ImageLoadUtils.getoptions(),
                ImageLoadUtils.getAnimateFirstDisplayListener());
        goodsTitle.setText(goodsBean.getGoods_name());
        goodsprice.setText("��:" + goodsBean.getGoods_price());
        final String goods_id = goodsBean.getGoods_id();
        listLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.showGoods_Detaill(mContext, goods_id);
            }
        });
        final int dPosition = holder.getmPosition();
        listLayout.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                DialogTool.createNormalDialog2(mContext,
                        "ȷ���Ƴ���ע����Ʒ��",
                        "ȷ��",
                        "ȡ��",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                delFaavoritesData(goods_id, dPosition);
                            }},
                        null).show();
/*                AlertDialog.Builder builder = new AlertDialog.Builder(
                        new ContextThemeWrapper(
                                mContext,
                                android.support.v7.appcompat.R.style.Theme_AppCompat_Light_DialogWhenLarge));
                builder.setTitle("�Ƿ�ɾ����ע")
                        .setMessage("ȷ���Ƴ���ע����Ʒ��")
                        .setNegativeButton("ȷ��", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                delFaavoritesData(goods_id, dPosition);
                            }
                        })
                        .setPositiveButton("ȡ��", null)
                        .show();*/

                return false;
            }
        });
    }

    // ɾ���ղؼ�
    private void delFaavoritesData(String fav_id, final int dPosition) {
        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        requestParams.add("fav_id", fav_id);
        HttpUtil.post(HttpUtil.URL_FAVORITES_DELETE, requestParams,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          byte[] responseBody) {
                        try {
                            String jsonString = new String(responseBody);
                            YkLog.t(TAG, jsonString + "/" + dPosition);
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
