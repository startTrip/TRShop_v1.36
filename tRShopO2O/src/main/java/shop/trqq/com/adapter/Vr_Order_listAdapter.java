package shop.trqq.com.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import shop.trqq.com.R;
import shop.trqq.com.UserManager;
import shop.trqq.com.bean.Vr_Order_listBean;
import shop.trqq.com.ui.Base.UIHelper;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.ImageLoadUtils;
import shop.trqq.com.util.ToastUtils;
import shop.trqq.com.util.YkLog;

public class Vr_Order_listAdapter extends CommonAdapter<Vr_Order_listBean> {
    private static final String TAG = "OrderAdapter";
    public int flag;
    private String state_desc;
    private Handler handler;
    private String filter;

    public Vr_Order_listAdapter(Handler handler, Context context, String filter) {
        super(context, new ArrayList<Vr_Order_listBean>(),
                R.layout.list_vr_ordel);
        YkLog.i(TAG, "订单适配器构造方法");
        this.filter = filter;
        this.handler = handler;

    }

    @Override
    public void convert(ViewHolder holder, Vr_Order_listBean bean) {
        // TODO Auto-generated method stub
        // loadOrderListDATA();
        final Resources resource = (Resources) mContext.getResources();
        LinearLayout listLayout = (LinearLayout) holder
                .getView(R.id.order_list_layout);
        LinearLayout order_time_layout = (LinearLayout) holder
                .getView(R.id.order_time_layout);
        TextView time = (TextView) holder.getView(R.id.trade_item_time);
        TextView store_name = (TextView) holder.getView(R.id.trade_store_name);
        TextView trade_number = (TextView) holder.getView(R.id.trade_number);
        // LinearLayout body = (LinearLayout)
        // holder.getView(R.id.trade_item_body);
        // TextView fee = (TextView) holder.getView(R.id.trade_item_fee);
        TextView order_state_text = (TextView) holder
                .getView(R.id.trade_item_state);
        /* TextView score = (TextView) holder.getView(R.id.trade_item_score); */
        TextView total = (TextView) holder.getView(R.id.trade_item_total);
        // Button check = (Button) holder.getView(R.id.trade_item_check);
        Button ok = (Button) holder.getView(R.id.trade_item_ok);
        time.setText(":  "
                + getDateToString(Long.parseLong(bean.getAdd_time()) * 1000));
        store_name.setText(":  " + bean.getStore_name());
        trade_number.setText(":  " + bean.getOrder_sn());
        order_state_text.setText(bean.getOrder_state_text());
        // fee.setText(":  " + bean.getShipping_fee());
        total.setText(bean.getOrder_amount());

		/*
		 * View view = LayoutInflater.from(ListContext).inflate(
		 * R.layout.trade_body, null);
		 */
        ImageView image = (ImageView) holder.getView(R.id.trade_body_image);
        TextView text = (TextView) holder.getView(R.id.trade_body_text);
        // TextView total = (TextView)
        // view.findViewById(R.id.trade_body_total);
        LinearLayout trade_item_body = (LinearLayout) holder.getView(R.id.trade_item_body);
        TextView num = (TextView) holder.getView(R.id.trade_body_num);
        ImageLoader.getInstance().displayImage(bean.getGoods_image_url(),
                image, ImageLoadUtils.getoptions());
        text.setText(bean.getGoods_name());
        // total.setText(cartInfoList.get(i).getCartProductPrice());
        num.setText("X " + bean.getGoods_num());
        final String good_id = bean.getGoods_id();
        trade_item_body.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                UIHelper.showGoods_Detaill(mContext, good_id);
                Message msg = new Message();
                msg.what = 2;
                handler.sendMessage(msg);
            }
        });
        state_desc = bean.getState_desc();
        ok.setVisibility(View.GONE);
        final String order_id = bean.getOrder_id();
        if (bean.getIf_cancel()) {
            ok.setBackgroundResource(R.color.red_text_color);
            ok.setText("取消订单");
            ok.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Order_Cancel(order_id);
                }
            });
            ok.setVisibility(View.VISIBLE);
        }
        try {
            FrameLayout trade_item_Layout = (FrameLayout) holder
                    .getView(R.id.trade_item_Layout);
            trade_item_Layout.setVisibility(View.GONE);
            order_time_layout.setBackgroundColor(mContext.getResources()
                    .getColor(R.color.gray));
            if (bean.getIf_pay()) {
                order_time_layout.setBackgroundColor(mContext.getResources()
                        .getColor(R.color.green));
                Button pay = (Button) holder.getView(R.id.vr_order_item_ok);
                final String pay_sn = bean.getOrder_sn();
                final String pay_amount = bean.getOrder_amount();
                pay.setText("订单支付(" + bean.getOrder_amount() + ")元");
                pay.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Payment_List(pay_sn, pay_amount);
                    }
                });
                // total.setText(bean.getPay_amount());
                trade_item_Layout.setVisibility(View.VISIBLE);
            } else if (bean.getOrder_state().equals("20")) {
				/*
				 * order_time_layout.setBackgroundColor(mContext
				 * .getResources().getColor(R.color.gray));
				 */
                Button pay = (Button) holder.getView(R.id.vr_order_item_ok);
                pay.setText("查看兑换码");
                pay.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        UIHelper.showIndate_Code(mContext, order_id);
                        Message msg = new Message();
                        msg.what = 2;
                        handler.sendMessage(msg);
                    }
                });
                // total.setText(bean.getPay_amount());
                trade_item_Layout.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // listLayout.setVisibility(View.VISIBLE);
    }

    // 订单取消
    private void Order_Cancel(String order_id) {
        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        requestParams.add("order_id", order_id);
        HttpUtil.post(HttpUtil.URL_MEMBER_VR_ORDER_CANCEL, requestParams,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          byte[] responseBody) {
                        try {
                            String jsonString = new String(responseBody);
                            YkLog.t("check_password", jsonString);
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
                                    ToastUtils.showMessage(mContext, "订单已经取消");
                                    Message msg = new Message();
                                    msg.what = 1;
                                    handler.sendMessage(msg);
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

    // 兑换码列表
    private void Indate_code_list(String order_id) {
        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        requestParams.add("order_id", order_id);
        HttpUtil.post(HttpUtil.URL_MEMBER_VR_ODER, requestParams,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          byte[] responseBody) {
                        try {
                            String jsonString = new String(responseBody);
                            YkLog.t("code_list", jsonString);
                            JSONObject jsonObject = new JSONObject(jsonString)
                                    .getJSONObject("datas");
                            try {

                                String errStr = jsonObject.getString("error");
                                if (errStr != null) {
                                    ToastUtils.showMessage(mContext, errStr);
                                }
                            } catch (Exception e) {
                                String code_list = jsonObject
                                        .getString("code_list");
                                if (code_list.equals("[]")) {
                                    ToastUtils.showMessage(mContext,
                                            "没有可用的兑换码列表");
                                } else {
                                    ToastUtils.showMessage(mContext,
                                            jsonObject.toString());
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

    // 支付列表
    private void Payment_List(final String pay_sn, final String pay_amount) {
        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
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
                                String payment_list = jsonObject
                                        .getString("payment_list");
                                if (payment_list.equals("[]")) {
                                    ToastUtils.showMessage(mContext,
                                            "没有可用的支付方式");
                                } else if (payment_list.contains("alipay")) {
                                    UIHelper.showPayment_wap(mContext, pay_sn, "v");
									/*final String[] mItems = { "支付宝", "泰付宝",
											"消费积分兑换", "产品积分兑换" };
									// appcompat主题代替系统主题，避免系统版本不支持
									AlertDialog.Builder builder = new AlertDialog.Builder(
											new ContextThemeWrapper(
													mContext,
													android.support.v7.appcompat.R.style.Theme_AppCompat_Light_DialogWhenLarge));
									builder.setTitle("选择支付方式");
									builder.setItems(
											mItems,
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int which) {
													// 点击后弹出窗口选择了第几项
													switch (which) {
													case 0:
														UIHelper.showPayment_wap(
																mContext,
																pay_sn,"r");
														break;
													case 1:
														UIHelper.showTaiPayment_wap(
																mContext,
																pay_sn,
																pay_amount,
																"taifubao");
														break;
													case 2:
														UIHelper.showTaiPayment_wap(
																mContext,
																pay_sn,
																pay_amount,
																"consumepay");
														break;
													case 3:
														UIHelper.showTaiPayment_wap(
																mContext,
																pay_sn,
																pay_amount,
																"productpay");
														break;
													default:
														break;
													}
												}
											});
									builder.create().show();*/
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
        requestParams.add("pay_sn", pay_sn);
        HttpUtil.post(HttpUtil.URL_ORDER_PAYMENT, requestParams,
                new AsyncHttpResponseHandler() {
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
                                ToastUtils.showMessage(mContext,
                                        jsonObject.toString());
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
