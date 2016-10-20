package shop.trqq.com.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
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
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import shop.trqq.com.R;
import shop.trqq.com.UserManager;
import shop.trqq.com.bean.Order_goodsBean;
import shop.trqq.com.bean.Order_listBean;
import shop.trqq.com.ui.Base.UIHelper;
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

    public Order_CellAdapter(Handler handler, Context context, String filter) {
        super(context, new ArrayList<Order_listBean>(), R.layout.order_cell);
        YkLog.i(TAG, "订单适配器构造方法");
        this.filter = filter;
        this.handler = handler;

    }

    @Override
    public void convert(ViewHolder holder, Order_listBean bean) {
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
        /*
		 * ok.setText("已取消"); ok.setOnClickListener(null);
		 * ok.setBackgroundResource(R.color.gray_bg_color);
		 */
        cancel.setVisibility(View.GONE);
        receive.setVisibility(View.GONE);
        deliver.setVisibility(View.GONE);
        // 根据商品发货状态来显示  订单取消，订单确认，查看物流
        if (bean.isIf_cancel()) {

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
            receive.setVisibility(View.VISIBLE);
        } else if (bean.isIf_lock()) {
            // cancel.setVisibility(View.GONE);
        }

        // listLayout.setVisibility(View.VISIBLE);
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

}
