package shop.trqq.com.ui.Base;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import shop.trqq.com.AppContext;
import shop.trqq.com.R;
import shop.trqq.com.UserManager;
import shop.trqq.com.bean.AddressBean;
import shop.trqq.com.im.ui.IMMsgActivity;
import shop.trqq.com.im.ui.MainIMActivity;
import shop.trqq.com.supermarket.MarketMainActivity;
import shop.trqq.com.supermarket.activitys.MarketGoCartActivity;
import shop.trqq.com.ui.AboutActivity;
import shop.trqq.com.ui.CartActivity;
import shop.trqq.com.ui.CheckOutActivity;
import shop.trqq.com.ui.FavoritesListActivity;
import shop.trqq.com.ui.GoodsActivity;
import shop.trqq.com.ui.Goods_DetaillActivity;
import shop.trqq.com.ui.HistoryListActivity;
import shop.trqq.com.ui.Indate_code_listActivity;
import shop.trqq.com.ui.MainTabActivity;
import shop.trqq.com.ui.OrderActivity;
import shop.trqq.com.ui.Payment_wapActivity;
import shop.trqq.com.ui.PersonalActivity;
import shop.trqq.com.ui.RegisterActivity;
import shop.trqq.com.ui.SearchActivity;
import shop.trqq.com.ui.SettingsActivity;
import shop.trqq.com.ui.Shipping_StatusActivity;
import shop.trqq.com.ui.SpecialActivity;
import shop.trqq.com.ui.StoreActivity;
import shop.trqq.com.ui.StoreProductActivity;
import shop.trqq.com.ui.Store_FavoritesActivity;
import shop.trqq.com.ui.Store_ListActivity;
import shop.trqq.com.ui.SuggestActivity;
import shop.trqq.com.ui.SystemMsgActivity;
import shop.trqq.com.ui.TaiPayment_wapActivity;
import shop.trqq.com.ui.Voucher_ListActivity;
import shop.trqq.com.ui.Vr_buy1Activity;
import shop.trqq.com.ui.address_editeActivity;
import shop.trqq.com.ui.address_listActivity;
import shop.trqq.com.ui.address_newActivity;
import shop.trqq.com.ui.invoice_listActivity;
import shop.trqq.com.ui.product_infoActivity;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.RSAUtils;
import shop.trqq.com.util.ToastUtils;
import shop.trqq.com.util.YkLog;

/**
 * 界面跳转封装类
 */
public class UIHelper {
    private final static String TAG = "UIHelper";

    /**
     * 显示主界面
     *
     * @param context
     */
    public static void showMain(Context context) {
        YkLog.i(TAG, "跳转到主界面");
        Intent intent = new Intent(context, MainTabActivity.class);
        context.startActivity(intent);
    }

    /**
     * 显示商品界面
     *
     * @param context
     */
    public static void showGoods_Detaill(Context context, String id) {
        YkLog.i(TAG, "跳转到商品界面");
        Intent intent = new Intent(context, Goods_DetaillActivity.class);
        intent.putExtra("goods_id", id);
        context.startActivity(intent);
    }

    // 跳转到超市界面
    public static void showMarket(Context context) {
        Intent intent = new Intent(context, MarketMainActivity.class);

        context.startActivity(intent);
    }

    // 跳转到超市购物车界面
    public static void showMarketCart(Context context) {
        Intent intent = new Intent(context, MarketGoCartActivity.class);
        context.startActivity(intent);
    }

    // 跳转到注册登录界面
    public static void showPersonalActivity(Context context) {
        Intent intent = new Intent(context, PersonalActivity.class);
        context.startActivity(intent);
    }

    /**
     * 显示商品列表界面
     *
     * @param context
     */
    public static void showShop(Context context, String keyword, String gc_id,
                                String store_id, String brand) {
        YkLog.i(TAG, "跳转到商品列表界面");
        Intent intent = new Intent(context, GoodsActivity.class);
        intent.putExtra("keyword", keyword);
        intent.putExtra("gc_id", gc_id);
        intent.putExtra("store_id", store_id);
        intent.putExtra("brand", brand);
        context.startActivity(intent);
    }


    /**
     * 显示商品详情界面
     *
     * @param context
     */
    public static void showProductinfo(Context context, String id,
                                       String mobile_body) {
        YkLog.i(TAG, "跳转到商品详情界面");
        Intent intent = new Intent(context, product_infoActivity.class);
        intent.putExtra("goods_id", id);
        intent.putExtra("mobile_body", mobile_body);
        context.startActivity(intent);
    }

    /**
     * 显示购物车界面
     *
     * @param context
     */
    public static void showCart(Context context) {
        YkLog.i(TAG, "跳转到购物车界面");
        Intent intent = new Intent(context, CartActivity.class);
        context.startActivity(intent);
    }

    /**
     * 显示搜索界面
     *
     * @param context
     */
    public static void showSearch(Context context) {
        YkLog.i(TAG, "跳转到搜索界面");
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    /**
     * 显示订单界面
     *
     * @param context
     */
    public static void showCheckOut(Context context) {
        YkLog.i(TAG, "跳转到订单界面");
        Intent intent = new Intent(context, CheckOutActivity.class);
        context.startActivity(intent);
    }

    /**
     * 显示地址列表界面
     *
     * @param context
     */
    public static void showAddressList(Context context, String freight_hash) {
        YkLog.i(TAG, "跳转到地址列表界面");
        Intent intent = new Intent(context, address_listActivity.class);
        intent.putExtra("freight_hash", freight_hash);
        context.startActivity(intent);
    }

    /**
     * 显示添加地址界面
     *
     * @param context
     */
    public static void showAddressNew(Context context) {
        YkLog.i(TAG, "跳转到添加地址界面");
        Intent intent = new Intent(context, address_newActivity.class);
        context.startActivity(intent);
    }

    /**
     * 显示修改地址界面
     *
     * @param context
     */
    public static void showAddressEdite(Context context, AddressBean bean) {
        YkLog.i(TAG, "跳转到修改地址界面");
        Intent intent = new Intent(context, address_editeActivity.class);
        intent.putExtra("AddressBean", bean);
        context.startActivity(intent);
    }

    /**
     * 显示发票列表界面
     *
     * @param context
     */
    public static void showInvoiceList(Context context) {
        YkLog.i(TAG, "跳转到地址列表界面");
        Intent intent = new Intent(context, invoice_listActivity.class);
        // intent.putExtra("freight_hash", freight_hash);
        context.startActivity(intent);
    }

    /**
     * 显示订单界面
     *
     * @param context
     */
    public static void showOrder(Context context, String filter) {
        YkLog.i(TAG, "跳转到订单界面");
        Intent intent = new Intent(context, OrderActivity.class);
        intent.putExtra("filter", filter);
        context.startActivity(intent);
    }

    /**
     * 显示代金卷界面
     *
     * @param context
     */
    public static void showVoucher(Context context) {
        YkLog.i(TAG, "跳转到代金卷界面");
        Intent intent = new Intent(context, Voucher_ListActivity.class);
        context.startActivity(intent);
    }

    /**
     * 显示收藏的店铺列表界面
     *
     * @param context
     */
    public static void showFavoritesStore(Context context) {
        YkLog.i(TAG, "跳转到收藏的店铺列表界面");
        Intent intent = new Intent(context, Store_FavoritesActivity.class);
        context.startActivity(intent);
    }

    /**
     * 显示注册界面
     *
     * @param context
     */
    public static void showRegister(Context context) {
        YkLog.i(TAG, "跳转到注册界面");
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    /**
     * 关注商品界面
     *
     * @param context
     */
    public static void showFavorites(Context context) {
        YkLog.i(TAG, "跳转到关注商品界面");
        Intent intent = new Intent(context, FavoritesListActivity.class);
        context.startActivity(intent);
    }

    /**
     * 关注浏览记录界面
     *
     * @param context
     */
    public static void showHistory(Context context) {
        YkLog.i(TAG, "跳转到浏览记录界面");
        Intent intent = new Intent(context, HistoryListActivity.class);
        context.startActivity(intent);
    }

    // 建议反馈界面

    public static void showSuggest(Context context) {
        YkLog.i(TAG, "跳转到建议反馈界面");
        Intent intent = new Intent(context, SuggestActivity.class);
        context.startActivity(intent);
    }

    // 专题界面
    public static void showSpecial(Context context, String special_id) {
        YkLog.i(TAG, "跳转到专题界面");
        Intent intent = new Intent(context, SpecialActivity.class);
        intent.putExtra("special_id", special_id);
        context.startActivity(intent);
    }

    // 虚拟购买界面
    public static void showVrBuy1(Context context, String goods_id,
                                  String quantity) {
        YkLog.i(TAG, "跳转到虚拟购买界面");
        Intent intent = new Intent(context, Vr_buy1Activity.class);
        intent.putExtra("goods_id", goods_id);
        intent.putExtra("quantity", quantity);
        context.startActivity(intent);
    }

    // WAP支付
    public static void showPayment_wap(Context context, String pay_sn, String order_type) {
        YkLog.i(TAG, "跳转到WAP支付界面");
        Intent intent = new Intent(context, Payment_wapActivity.class);
        intent.putExtra("pay_sn", pay_sn);
        intent.putExtra("order_type", order_type);
        context.startActivity(intent);
    }

    // 泰付宝支付
    public static void showTaiPayment_wap(Context context, String pay_sn,
                                          String pay_amount, String payment_code) {
        YkLog.i(TAG, "跳转到WAP支付界面");
        Intent intent = new Intent(context, TaiPayment_wapActivity.class);
        intent.putExtra("pay_sn", pay_sn);
        intent.putExtra("pay_amount", pay_amount);
        intent.putExtra("payment_code", payment_code);
        context.startActivity(intent);
    }

    // 物流信息
    public static void showShipping_Status(Context context, String order_id) {
        YkLog.i(TAG, "跳转到物流信息界面");
        Intent intent = new Intent(context, Shipping_StatusActivity.class);
        intent.putExtra("order_id", order_id);
        context.startActivity(intent);
    }

    // 虚拟订单可用码列表
    public static void showIndate_Code(Context context, String order_id) {
        YkLog.i(TAG, "跳转到虚拟订单可用码列表界面");
        Intent intent = new Intent(context, Indate_code_listActivity.class);
        intent.putExtra("order_id", order_id);
        context.startActivity(intent);
    }

    // 系统消息
    public static void showSystemMsg(Context context) {
        YkLog.i(TAG, "跳转到系统消息界面");
        Intent intent = new Intent(context, SystemMsgActivity.class);
        context.startActivity(intent);
    }

    // 关于界面
    public static void showAbout(Context context) {
        YkLog.i(TAG, "跳转到关于界面");
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }

    // 设置界面
    public static void showSetting(Context context) {
        YkLog.i(TAG, "跳转到设置界面");
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }

    // IM主界面
    public static void showMainIM(Context context) {
        YkLog.i(TAG, "跳转到IM主界面");
        Intent intent = new Intent(context, MainIMActivity.class);
        context.startActivity(intent);
    }

    /**
     * 全部店铺界面
     *
     * @param context
     */
    public static void showStore_list(Context context, String is_strategic_alliance) {
        YkLog.i(TAG, "跳转到全部店铺界面1");
        Intent intent = new Intent(context, StoreActivity.class);
        intent.putExtra("is_strategic_alliance", is_strategic_alliance);
        context.startActivity(intent);
    }

    /**
     * 全部店铺列表界面
     *
     * @param context
     */
    public static void showStore_list2(Context context, String is_sa, String sc_id, String sc_name) {
        YkLog.i(TAG, "跳转到全部店铺界面2");
        Intent intent = new Intent(context, Store_ListActivity.class);
        intent.putExtra("is_strategic_alliance", is_sa);
        intent.putExtra("sc_id", sc_id);
        intent.putExtra("sc_name", sc_name);
        context.startActivity(intent);
    }

    // 店铺首页
    public static void showStore(Context context, String store_id) {
        YkLog.i(TAG, "跳转到店铺首页");
        Intent intent = new Intent(context, StoreProductActivity.class);
        intent.putExtra("store_id", store_id);
        context.startActivity(intent);
    }

    // IM聊天界面
    public static void showIMChat(Context context, String t_id, String t_name) {
        YkLog.i(TAG, "跳转到 IM聊天界面");
        Intent intent = new Intent(context, IMMsgActivity.class);
        intent.putExtra("t_id", t_id);
        intent.putExtra("t_name", t_name);
        context.startActivity(intent);
    }

    /**
     * 登录时候用于显示登录对话框
     *
     * @param context
     */
    public static void showLoginDialog(final Context context) {
        final Dialog dialog = new Dialog(context, R.style.LoginDialog);
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_login);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        final EditText mLoginUserIdEditText = (EditText) window
                .findViewById(R.id.login_userid_editText);
        final EditText mLoginPasswordEditText = (EditText) window
                .findViewById(R.id.login_password_editText);
        Button mRegisterButton = (Button) window
                .findViewById(R.id.register_button);
        Button mLoginButton = (Button) window.findViewById(R.id.login_button);

        mLoginButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String loginId = mLoginUserIdEditText.getText().toString();
                String password = mLoginPasswordEditText.getText().toString();
                if (loginId.equals("")) {
                    ToastUtils.showMessage(context, "账号不能为空");
                    return;
                }
                if (password.equals("")) {
                    ToastUtils.showMessage(context, "密码不能为空");
                    return;
                }

                // int loginType = UserManager.getUserInfo().getLoginType();
                // 获取手机串号
                TelephonyManager tm = (TelephonyManager) context
                        .getSystemService(Context.TELEPHONY_SERVICE);
                String imei = tm.getDeviceId();
				/* 密钥内容 base64 code */
                String PUCLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDKLCrEWbKaIglL5RO2mztLrVi2"
                        + "IqAa+yQc1nk8f4hPOLTNMfVEtXvE/A47aYUEiKG9Wo9nRCQhU2qsZSJSefwvtiXG"
                        + "eBrTcYNmvANW64f4jvmIZAbcUYUI75g5Nv0q7KmFSb/3bsruP48jjmzupa2n2PHG"
                        + "GQ0nlrQmUE99xkauEQIDAQAB";

                // requestParams.add("loginType", String.valueOf(loginType));
                // requestParams.add("loginId", loginId);

                String passwordencrypt = password;
                try {

                    // 加密 （非对称加密）通过公钥加密
                    passwordencrypt = RSAUtils.encryptByPublic(password);
                    YkLog.i("password",passwordencrypt);
                    // passwordencrypt = Base64.encode(encryptByte);
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                RequestParams requestParams = new RequestParams();
                requestParams.add("username", loginId);
                requestParams.add("password", passwordencrypt);
                // requestParams.add("imei", imei);
                requestParams.add("client", "android");
                // System.out.println(requestParams.toString());
                HttpUtil.post(HttpUtil.URL_LOGIN, requestParams,
                        new AsyncHttpResponseHandler() {
                            ProgressDialog pd;

                            @Override
                            public void onStart() {
                                super.onStart();
                                dialog.hide();
                                pd = ProgressDialog.show(context, null,
                                        "正在登陆...", true, true,
                                        new OnCancelListener() {
                                            @Override
                                            public void onCancel(
                                                    DialogInterface dialog) {
                                                sendCancelMessage();
                                            }
                                        });
                                pd.setCanceledOnTouchOutside(false);
                            }

                            @Override
                            public void onSuccess(int statusCode,
                                                  Header[] headers, byte[] responseBody) {
                                try {
                                    String jsonStr = new String(responseBody);
                                    JSONObject jsonObject = new JSONObject(
                                            jsonStr).getJSONObject("datas");
                                    try {
                                        String errStr = jsonObject
                                                .getString("error");
                                        if (errStr != null) {
                                            ToastUtils.showMessage(context,
                                                    errStr);
                                            if (dialog.isShowing())
                                                dialog.show();
                                        }
                                    } catch (Exception e) {
                                        String userInfo = new JSONObject(
                                                jsonStr).getString("datas");
                                        YkLog.t("Login", jsonStr);
                                        // 将数据插入本地配置，成功就跳转到主界面
                                        if (UserManager.jsonToBean(context,
                                                userInfo)) {
//                                                UIHelper.showMain(context);
                                            if (context.getClass().equals(PersonalActivity.class)){
                                                ((PersonalActivity)context).finish();
                                            }else {
                                                UIHelper.showMain(context);
                                            }
                                            dialog.dismiss();
                                            UserManager.setUserInfo(true);
                                            ToastUtils.showMessage(context,
                                                    R.string.login_success);
                                            //加载IM
                                            AppContext mAppContext = (AppContext) context
                                                    .getApplicationContext();
                                            mAppContext.initSocketIOWebView();
                                        } else {
                                            ToastUtils.showMessage(context,
                                                    R.string.request_failed);
                                            if (dialog.isShowing())
                                                dialog.show();
                                        }
                                    }
                                } catch (Exception e) {
                                    ToastUtils.showMessage(context,
                                            R.string.request_failed);
                                    if (dialog.isShowing())
                                        dialog.show();
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode,
                                                  Header[] headers, byte[] responseBody,
                                                  Throwable error) {
                                if (dialog.isShowing())
                                    dialog.show();
                            }

                            @Override
                            public void onCancel() {
                                super.onCancel();
                                dialog.show();
                            }

                            @Override
                            public void onFinish() {
                                super.onFinish();
                                pd.dismiss();
                            }
                        });

            }
        });

        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showRegister(context);
                dialog.dismiss();
            }
        });
    }


}
