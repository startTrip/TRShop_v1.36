package shop.trqq.com.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import shop.trqq.com.R;
import shop.trqq.com.UserManager;
import shop.trqq.com.bean.OrderGroupHomeListBean;
import shop.trqq.com.supermarket.activitys.BinderCardActivity;
import shop.trqq.com.ui.Base.UIHelper;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.ToastUtils;
import shop.trqq.com.util.YkLog;
import shop.trqq.com.widget.MyListView;

/**
 * ÎÒµÄ¶©µ¥µêÆÌÊÊÅäÆ÷
 *
 * @author Weiss
 */
public class OrderAdapter extends CommonAdapter<OrderGroupHomeListBean> {
    private static final String TAG = "OrderAdapter";
    private int flag;
    private Handler handler;
    private String filter;

    public void setOnClickAliPay(onClickAliPay onClickAliPay) {
        mOnClickAliPay = onClickAliPay;
    }

    public interface onClickAliPay{
        void clickAlipay(String pay_sn, String pay_amount);
    }

    private onClickAliPay mOnClickAliPay;

    public OrderAdapter(Handler handler, Context context, String filter) {
        super(context, new ArrayList<OrderGroupHomeListBean>(),
                R.layout.list_order);

        this.handler = handler;
        this.filter = filter;

    }

    @Override
    public void convert(ViewHolder holder, OrderGroupHomeListBean bean) {
        // TODO Auto-generated method stub
        LinearLayout order_time_layout = (LinearLayout) holder
                .getView(R.id.order_time_layout);
        TextView time = (TextView) holder.getView(R.id.trade_item_time);
        /*
		 * LinearLayout body = (LinearLayout)
		 * holder.getView(R.id.trade_item_body); TextView fee = (TextView)
		 * holder.getView(R.id.trade_item_fee); TextView red_paper = (TextView)
		 * holder .getView(R.id.trade_item_redPaper); TextView score =
		 * (TextView) holder.getView(R.id.trade_item_score);
		 */
        // TextView total = (TextView) holder.getView(R.id.trade_item_total);
        MyListView mListView = (MyListView) holder.getView(R.id.listView_order);
        Order_CellAdapter mOrder_cellAdapter = new Order_CellAdapter(handler,
                mContext, filter);
        mOrder_cellAdapter.setData(bean.getOrder_list());
        mListView.setAdapter(mOrder_cellAdapter);
        // Button check = (Button) holder.getView(R.id.trade_item_check);
        time.setText(":  "
                + getDateToString(Long.parseLong(bean.getAdd_time()) * 1000));
        try {
            LinearLayout trade_item_Layout = (LinearLayout) holder
                    .getView(R.id.trade_item_Layout);
            trade_item_Layout.setVisibility(View.GONE);
            order_time_layout.setBackgroundColor(mContext.getResources()
                    .getColor(R.color.gray));
            if (bean.getPay_amount() != null) {
                if (Float.parseFloat(bean.getPay_amount()) > 0) {
                    order_time_layout.setBackgroundColor(mContext
                            .getResources().getColor(R.color.green));
                    Button ok = (Button) holder.getView(R.id.trade_item_ok);
                    ok.setText("¶©µ¥Ö§¸¶(" + bean.getPay_amount() + ")Ôª");

                    // Ö§ï¿½ï¿½ï¿½ï¿½ï¿?
                    final String pay_sn = bean.getPay_sn();
                    // Ö§ï¿½ï¿½ï¿½ï¿½ï¿½Ü½ï¿½ï¿?
                    final String pay_amount = bean.getPay_amount();
                    ok.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            //  TODO :ï¿½ï¿½ï¿½Ö§ï¿½ï¿?
                            // ï¿½ï¿½ï¿½Ö§ï¿½ï¿?
                            // ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½Ö§ï¿½ï¿½ï¿½ï¿½ï¿½?
                            Payment_List(pay_sn, pay_amount);
//                            mOnClickAliPay.clickAlipay(pay_sn,pay_amount);
                        }
                    });
                    // total.setText(bean.getPay_amount());
                    trade_item_Layout.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     *  ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½Ö§ï¿½ï¿½ï¿½Ãµï¿?Ö§ï¿½ï¿½ï¿½Ğ±ï¿½
     * @param pay_sn   ï¿½ï¿½ï¿½ï¿½Ö§ï¿½ï¿½ï¿½ï¿½ï¿?
     * @param pay_amount   ï¿½ï¿½ï¿½ï¿½Ö§ï¿½ï¿½ï¿½ï¿½ï¿?
     */
    private void Payment_List(final String pay_sn, final String pay_amount) {

        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);         // ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½
        HttpUtil.post(HttpUtil.URL_ORDER_PAYMENT_LIST, requestParams,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(final int statusCode, Header[] headers,
                                          byte[] responseBody) {
                        try {
                            String jsonString = new String(responseBody);
                            YkLog.t("Payment_List", jsonString);
                            JSONObject jsonObject = new JSONObject(jsonString)
                                    .getJSONObject("datas");
                            try {

                                String errStr = jsonObject.getString("error");
                                if (errStr != null) {
                                    ToastUtils.showMessage(mContext, errStr);
                                }
                            } catch (Exception e) {
                                // {"code":200,"datas":{"payment_list":["alipay","wxpay"]}}
                                String payment_list = jsonObject
                                        .getString("payment_list");
                                if (payment_list.equals("[]")) {
                                    ToastUtils.showMessage(mContext,
                                            "Ã»ÓĞ¿ÉÓÃµÄÖ§¸¶·½Ê½");
                                } else if (payment_list.contains("taifubao")) {
                                    final String[] mItems = {"Ì©¸¶±¦",
                                            "Ïû·Ñ»ı·Ö¶Ò»»", "²úÆ·»ı·Ö¶Ò»»", "Í¨ÓÃ»ı·Ö¶Ò»»","¹ºÎï¿¨"};

//                                            "Ì©ï¿½ï¿½ï¿½ï¿½",
//                                            "ï¿½ï¿½Ñ»ï¿½Ö¶Ò»ï¿½", "ï¿½ï¿½Æ·ï¿½ï¿½Ö¶Ò»ï¿?, "Í¨ï¿½Ã»ï¿½Ö¶Ò»ï¿?};
                                    // appcompatï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ÏµÍ³ï¿½ï¿½ï¿½â£¬ï¿½ï¿½ï¿½ï¿½ÏµÍ³ï¿½æ±¾ï¿½ï¿½Ö§ï¿½ï¿?
                                    AlertDialog.Builder builder = new AlertDialog.Builder(
                                            new ContextThemeWrapper(
                                                    mContext,
                                                    android.support.v7.appcompat.R.style.Base_V21_Theme_AppCompat_Light_Dialog));
//													android.support.v7.appcompat.R.style.Theme_AppCompat_Light_DialogWhenLarge));
                                    //builder.setTitle("Ñ¡ï¿½ï¿½Ö§ï¿½ï¿½ï¿½ï¿½Ê½");

                                    builder.setItems(
                                            mItems,
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(
                                                        DialogInterface dialog,
                                                        int which) {
                                                    // ï¿½ï¿½ï¿½ï¿½óµ¯³ï¿½ï¿½ï¿½ï¿½ï¿½Ñ¡ï¿½ï¿½ï¿½ËµÚ¼ï¿½ï¿½ï¿?
                                                    switch (which) {
//                                                        // æ”¯ä»˜å®æš‚æ—¶ä¸èƒ½ç”¨
//                                                        case 0:
////                                                             ï¿½ï¿½×ªï¿½ï¿½Ö§ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½
//                                                            UIHelper.showPayment_wap(
//                                                                    mContext,
//                                                                    pay_sn, "r");
////                                                            mOnClickAliPay.clickAlipay(pay_sn, pay_amount);
//                                                            break;
                                                        case 0:
                                                            // ï¿½ï¿½×ªï¿½ï¿½Ì©ï¿½ï¿½ï¿½ï¿½
                                                            UIHelper.showTaiPayment_wap(
                                                                    mContext,
                                                                    pay_sn,
                                                                    pay_amount,
                                                                    "taifubao");
                                                            break;

                                                        // ï¿½ï¿½Ñ»ï¿½ï¿½
                                                        case 1:
                                                            UIHelper.showTaiPayment_wap(
                                                                    mContext,
                                                                    pay_sn,
                                                                    pay_amount,
                                                                    "consumepay");
                                                            break;
                                                        case 2:
                                                            // ï¿½ï¿½Æ·ï¿½ï¿½ï¿?
                                                            UIHelper.showTaiPayment_wap(
                                                                    mContext,
                                                                    pay_sn,
                                                                    pay_amount,
                                                                    "productpay");
                                                            break;
                                                        case 3:
                                                            // ï¿½ï¿½Í¨ï¿½ï¿½ï¿?
                                                            UIHelper.showTaiPayment_wap(
                                                                    mContext,
                                                                    pay_sn,
                                                                    pay_amount,
                                                                    "tyongpay");
                                                            break;
                                                        case 4:
                                                            Intent intent = new Intent(mContext,BinderCardActivity.class);
                                                            mContext.startActivity(intent);
                                                        default:
                                                            break;
                                                    }
                                                }
                                            });
                                    builder.create().show();
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

    // Ö§ï¿½ï¿½
    private void Pay(String pay_sn) {
        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        String uri = HttpUtil.URL_ORDER_PAYMENT + "&pay_sn=" + pay_sn;
        HttpUtil.post(uri, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                try {
                    String jsonString = new String(responseBody);
                    YkLog.t("Pay", jsonString);
                    JSONObject jsonObject = new JSONObject(jsonString)
                            .getJSONObject("datas");
                    try {

                        String errStr = jsonObject.getString("error");
                        if (errStr != null) {
                            ToastUtils.showMessage(mContext, errStr);
                        }
                    } catch (Exception e) {
                        ToastUtils.showMessage(mContext, jsonObject.toString());
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

    /* Ê±ï¿½ï¿½ï¿½×ªï¿½ï¿½ï¿½ï¿½ï¿½Ö·ï¿?*/
    public static String getDateToString(long time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sf.format(d);
    }

}
