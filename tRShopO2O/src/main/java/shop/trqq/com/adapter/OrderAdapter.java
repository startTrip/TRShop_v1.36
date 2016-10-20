package shop.trqq.com.adapter;

import android.content.Context;
import android.content.DialogInterface;
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

import shop.trqq.com.R;
import shop.trqq.com.UserManager;
import shop.trqq.com.bean.OrderGroupHomeListBean;
import shop.trqq.com.ui.Base.UIHelper;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.ToastUtils;
import shop.trqq.com.util.YkLog;
import shop.trqq.com.widget.MyListView;

/**
 * 我的订单店铺适配器
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
        void clickAlipay(String pay_sn);
    }

    private onClickAliPay mOnClickAliPay;

    public OrderAdapter(Handler handler, Context context, String filter) {
        super(context, new ArrayList<OrderGroupHomeListBean>(),
                R.layout.list_order);
        YkLog.i(TAG, "订单适配器构造方法");
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
                    ok.setText("订单支付(" + bean.getPay_amount() + ")元");

                    // 支付编号
                    final String pay_sn = bean.getPay_sn();
                    // 支付的总金额
                    final String pay_amount = bean.getPay_amount();
                    ok.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            //  TODO :点击支付
                            // 点击支付
                            // ！！！！！！！！！！点击订单支付按钮
                            Payment_List(pay_sn, pay_amount);
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
     *  点击订单支付得到 支付列表
     * @param pay_sn   订单支付编号
     * @param pay_amount   订单支付金额
     */
    private void Payment_List(final String pay_sn, final String pay_amount) {

        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);         // 访问令牌
        HttpUtil.post(HttpUtil.URL_ORDER_PAYMENT_LIST, requestParams,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
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
                                            "没有可用的支付方式");
                                } else if (payment_list.contains("alipay")) {
                                    final String[] mItems = {"支付宝"};

//                                            "泰付宝",
//                                            "消费积分兑换", "产品积分兑换", "通用积分兑换"};
                                    // appcompat主题代替系统主题，避免系统版本不支持
                                    AlertDialog.Builder builder = new AlertDialog.Builder(
                                            new ContextThemeWrapper(
                                                    mContext,
                                                    android.support.v7.appcompat.R.style.Base_V21_Theme_AppCompat_Light_Dialog));
//													android.support.v7.appcompat.R.style.Theme_AppCompat_Light_DialogWhenLarge));
                                    //builder.setTitle("选择支付方式");

                                    builder.setItems(
                                            mItems,
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(
                                                        DialogInterface dialog,
                                                        int which) {
                                                    // 点击后弹出窗口选择了第几项
                                                    switch (which) {
                                                        case 0:
//                                                             跳转到支付宝界面
//                                                            UIHelper.showPayment_wap(
//                                                                    mContext,
//                                                                    pay_sn, "r");
                                                            mOnClickAliPay.clickAlipay(pay_sn);
                                                            break;
                                                        case 1:
                                                            // 跳转到泰付宝
                                                            UIHelper.showTaiPayment_wap(
                                                                    mContext,
                                                                    pay_sn,
                                                                    pay_amount,
                                                                    "taifubao");
                                                            break;

                                                        // 消费积分
                                                        case 2:
                                                            UIHelper.showTaiPayment_wap(
                                                                    mContext,
                                                                    pay_sn,
                                                                    pay_amount,
                                                                    "consumepay");
                                                            break;
                                                        case 3:
                                                            // 产品积分
                                                            UIHelper.showTaiPayment_wap(
                                                                    mContext,
                                                                    pay_sn,
                                                                    pay_amount,
                                                                    "productpay");
                                                            break;
                                                        case 4:
                                                            // 普通积分
                                                            UIHelper.showTaiPayment_wap(
                                                                    mContext,
                                                                    pay_sn,
                                                                    pay_amount,
                                                                    "tyongpay");
                                                            break;
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

    // 支付
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

    /* 时间戳转换成字符串 */
    public static String getDateToString(long time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sf.format(d);
    }

}
