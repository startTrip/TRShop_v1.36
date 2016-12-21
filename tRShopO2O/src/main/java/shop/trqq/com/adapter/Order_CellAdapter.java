package shop.trqq.com.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import shop.trqq.com.R;
import shop.trqq.com.UserManager;
import shop.trqq.com.bean.Order_goodsBean;
import shop.trqq.com.bean.Order_listBean;
import shop.trqq.com.supermarket.view.MyPopupWindow;
import shop.trqq.com.ui.Base.UIHelper;
import shop.trqq.com.ui.OrderActivity;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.ImageLoadUtils;
import shop.trqq.com.util.ToastUtils;
import shop.trqq.com.util.YkLog;
import shop.trqq.com.widget.DialogTool;

/**
 * 我的订单商品适配器
 *
 * @author Weiss
 */
public class Order_CellAdapter extends CommonAdapter<Order_listBean> {
    private static final String TAG = "OrderAdapter";
    private List<Order_goodsBean> extend_order_goods;
    public int flag;
    private String state_desc;
    private Handler handler;
    private String filter;
    private final MyPopupWindow mMyPopupWindow;
    private Button mFree;
    private TextView mAlertMessage;
    private View mButtonLayout;

    public Order_CellAdapter(Handler handler, Context context, String filter) {
        super(context, new ArrayList<Order_listBean>(), R.layout.order_cell);
        YkLog.i(TAG, "订单适配器构造方法");
        this.filter = filter;
        this.handler = handler;
        mMyPopupWindow = MyPopupWindow.getInstance();
    }

    @Override
    public void convert(ViewHolder holder, final Order_listBean bean) {
        // TODO Auto-generated method stub
        // loadOrderListDATA();
        final Resources resource = (Resources) mContext.getResources();
        LinearLayout listLayout = (LinearLayout) holder
                .getView(R.id.order_list_layout);
        TextView store_name = (TextView) holder.getView(R.id.trade_store_name);
        TextView trade_number = (TextView) holder.getView(R.id.trade_number);
        LinearLayout body = (LinearLayout) holder.getView(R.id.trade_item_body);
        TextView fee = (TextView) holder.getView(R.id.trade_item_fee);
        TextView state_descTextView = (TextView) holder
                .getView(R.id.trade_item_state_desc);
        // TextView score = (TextView) holder.getView(R.id.trade_item_score);
        TextView total = (TextView) holder.getView(R.id.trade_item_total);
        // Button check = (Button) holder.getView(R.id.trade_item_check);
        Button receive = (Button) holder.getView(R.id.trade_item_receive);
        Button cancel = (Button) holder.getView(R.id.trade_item_cancel);
        Button deliver = (Button) holder.getView(R.id.trade_item_deliver);
        mFree = (Button) holder.getView(R.id.trade_item_free);
        mAlertMessage = holder.getView(R.id.alert_message);
        mButtonLayout = holder.getView(R.id.confirm_layout);
        store_name.setText(":  " + bean.getStore_name());
        trade_number.setText(":  " + bean.getOrder_sn());
        fee.setText("运费: ￥" + bean.getShipping_fee());
        float sum = Float.parseFloat(bean.getGoods_amount())
                + Float.parseFloat(bean.getShipping_fee());
        total.setText("合计: ￥" + sum);

        // System.out.println(bean.getExtend_order_goods().size());
        extend_order_goods = bean.getExtend_order_goods();
        body.removeAllViews();
        for (int i = 0; i < extend_order_goods.size(); i++) {
            View view = LayoutInflater.from(mContext).inflate(
                    R.layout.trade_body, null);
            ImageView image = (ImageView) view
                    .findViewById(R.id.trade_body_image);
            TextView text = (TextView) view.findViewById(R.id.trade_body_text);
            // TextView total = (TextView)
            // view.findViewById(R.id.trade_body_total);
            TextView num = (TextView) view.findViewById(R.id.trade_body_num);
            ImageLoader.getInstance().displayImage(
                    extend_order_goods.get(i).getGoods_image_url(), image,
                    ImageLoadUtils.getoptions());
            text.setText(extend_order_goods.get(i).getGoods_name());
            // total.setText(cartInfoList.get(i).getCartProductPrice());
            num.setText("X " + extend_order_goods.get(i).getGoods_num());
            final String good_id = extend_order_goods.get(i).getGoods_id();
            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    UIHelper.showGoods_Detaill(mContext, good_id);
                    Message msg = new Message();
                    msg.what = 2;
                    handler.sendMessage(msg);
                }
            });
            body.addView(view);
        }
        state_desc = bean.getState_desc();
        state_descTextView.setText(state_desc);
        if (!filter.equals("") && !filter.equals(state_desc)) {
            listLayout.setVisibility(View.GONE);
            // return ;
        }
        final String order_id = bean.getOrder_id();

        // 得到免单状态
        String is_free_charge = bean.getIs_free_charge();
        // 得到店铺的 id
        String store_id = bean.getStore_id();

        /*
		 * ok.setText("已取消"); ok.setOnClickListener(null);
		 * ok.setBackgroundResource(R.color.gray_bg_color);
		 */
        mButtonLayout.setVisibility(View.VISIBLE);
        cancel.setVisibility(View.GONE);
        receive.setVisibility(View.GONE);
        deliver.setVisibility(View.GONE);
        mFree.setVisibility(View.GONE);
        // 根据商品发货状态来显示  订单取消，订单确认，查看物流, 申请免单
        if(TextUtils.equals(state_desc,"交易完成")){
            if(TextUtils.equals("126",store_id)){
                // 设置提示的信息    is_free_charge 为申请免单的状态
                setAlertMessage(order_id,is_free_charge);
            }
        }
        if(bean.isIf_cancel()){
            cancel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    DialogTool.createNormalDialog2(mContext,
                            "是否取消订单？",
                            "确定",
                            "取消",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub
                                    // 取消订单
                                    Order_Cancel(order_id);
                                }},
                            null).show();
                }
            });
            cancel.setVisibility(View.VISIBLE);
        }
        if (bean.isIf_deliver()) {//查看物流
            deliver.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    UIHelper.showShipping_Status(mContext, order_id);
                    Message msg = new Message();
                    msg.what = 2;
                    handler.sendMessage(msg);
                }
            });
            deliver.setVisibility(View.VISIBLE);
        }
        if (bean.isIf_receive()) {//确认收货
            // 只有超市才可以申请免单
            if(TextUtils.equals("126",store_id)){
                mFree.setVisibility(View.VISIBLE);
            }
            receive.setVisibility(View.VISIBLE);
            receive.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    DialogTool.createNormalDialog2(mContext,
                            "确定收到货了么？",
                            "确定",
                            "取消",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub
                                    Order_Receive(order_id);
                                }},
                            null).show();
                }
            });
            mFree.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogTool.createNormalDialog2(mContext,
                            "确认收货以后才能申请免单哦",
                            "确定",
                            "",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                }},
                            null).show();
                }
            });
        } else if (bean.isIf_lock()) {
            // cancel.setVisibility(View.GONE);
        }

        // listLayout.setVisibility(View.VISIBLE);
    }

    // 设置提示的信息和显示
    private void setAlertMessage(final String order_id,String is_free_charge) {
        mAlertMessage.setVisibility(View.VISIBLE);   // 设置提示信息显示
        mFree.setVisibility(View.VISIBLE);
        switch (is_free_charge){
            case "0":     // 默认状态,可以提交免单申请
                if(TextUtils.isEmpty(mAlertMessage.getText())){        // 确认订单以后的信息
                    mAlertMessage.setText("万能居超市订单付款后超过三十分钟送达可申请免单");
                }
                mFree.setVisibility(View.VISIBLE);
                mFree.setBackgroundColor(Color.RED);
                mFree.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogTool.createNormalDialog2(mContext,
                                "付款后超过30分钟送达才可以申请成功哦",
                                "申请",
                                "取消",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // 申请免单
                                        applyForFree(order_id);
                                    }},
                                null).show();
                    }
                });
                break;
            case "1":     // 已经申请免单，待审核
                mFree.setEnabled(false);
                mFree.setText("待审核");
                mFree.setBackgroundColor(Color.GRAY);
                mAlertMessage.setText("您已申请免单，待审核");
                break;
            case "2":      // 审核免单通过
                mButtonLayout.setVisibility(View.GONE);
                mFree.setVisibility(View.GONE);
                mAlertMessage.setText("你的免单申请已通过，金额已退回您的账户");
                break;
            case "3":       // 免单审核不通过
                mButtonLayout.setVisibility(View.GONE);
                mFree.setVisibility(View.GONE);
                mAlertMessage.setText("免单审核不通过，不符合免单要求");
                break;
        }

    }

    // 申请免单方法
    private void applyForFree(String order_id) {
        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key",key);
        requestParams.add("apply_status","apply_fee");
        requestParams.add("order_id",order_id);
        HttpUtil.post(HttpUtil.URL_APPLY_FREE, requestParams, new AsyncHttpResponseHandler() {
            ProgressDialog pd;
            @Override
            public void onStart() {
                super.onStart();
                pd = ProgressDialog.show(mContext, null,
                        "正在申请免单...", true, true,
                        new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                sendCancelMessage();
                            }
                        });
                pd.setCanceledOnTouchOutside(false);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String jsonString = new String(responseBody);
                Log.d("free",jsonString);
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONObject jsonObject1 = jsonObject.optJSONObject("datas");
                    String status = jsonObject1.optString("status");
                    String message = jsonObject1.optString("message");
                    if (!TextUtils.isEmpty(status)) {
                        switch (status){
                            case "0":       // 不符合免单申请要求
                            case "1":    // 免单申请提交成功，等待审核
                            case "2":       // 免单申请提交失败，请检查网络
                            case "-1":      // 非万能居超市不支持免单
                                showPayResultSuccess(message);
                                break;
                            case "-2":      // 请确认订单信息以后提交申请

                                break;
                            case "-3":      // 参数提交失败

                                break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                ToastUtils.showMessage(mContext,"请检查网络");
            }
            @Override
            public void onFinish() {
                super.onFinish();
                pd.dismiss();
            }
        });
    }

    // 订单取消
    private void Order_Cancel(String order_id) {
        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        requestParams.add("order_id", order_id);     //
        HttpUtil.post(HttpUtil.URL_ORDER_CANCEL, requestParams,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          byte[] responseBody) {
                        try {
                            String jsonString = new String(responseBody);
                            YkLog.t("URL_ORDER_CANCEL", jsonString);
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

    // 确认收货
    private void Order_Receive(String order_id) {
        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        requestParams.add("order_id", order_id);
        HttpUtil.post(HttpUtil.URL_ORDER_RECEIVE, requestParams,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          byte[] responseBody) {
                        try {
                            String jsonString = new String(responseBody);
                            YkLog.t("Order_Receive", jsonString);
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
                                    ToastUtils
                                            .showMessage(mContext, "订单已经确认收货");
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


    /* 时间戳转换成字符串 */
    public static String getDateToString(long time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sf.format(d);
    }

    /**
     * 支付成功的弹框
     */
    private void showPayResultSuccess(final String message) {

        View viewPop = LayoutInflater.from(mContext).inflate(R.layout.popup_pay_succes, null);
        ((TextView)viewPop.findViewById(R.id.pay_result)).setText(message);
        mMyPopupWindow.showPopupWindowFronCenter((OrderActivity)mContext, viewPop);
        viewPop.findViewById(R.id.tv_pay_know).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        mMyPopupWindow.cancel();
                        Message msg = Message.obtain(handler);
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }
                });
    }
}
