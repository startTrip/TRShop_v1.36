package shop.trqq.com.util;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HttpUtil {

    // 正式的 shop.trqq.com
    // 开发环境下的 shop.wushi3.com
    public static final String HOST = "http://shop.trqq.com/mobile/index.php";
    //public static final String HOST ="http://192.168.191.1/late_shop/mobile/index.php";

    // 购物卡注册和绑定接口
    public static final String URL_REGISTER_GET_CODE = HOST+"?act=login&op=send_auth_code_wap&type=mobile&phone_num=";

    public static final String URL_BINDER_CARD= HOST + "?act=shopcard_interface&op=card_info";

    // 超市的首页接口
    public static final String URL_MARKET_HOME = HOST+"?act=wnj_show_store&op=index&store_id=126";

    // 超市中的分类接口
    public static final String URL_MARKET_CLASSIFY=HOST + "/mobile/index.php?act=wnj_store_goods_class&op=index&store_id=126";

    public static final String URL_MARKET_GOODS_LIST = HOST +"?act=wnj_show_store&op=goods_all";

    // 申请 免单的接口
    public static final String URL_APPLY_FREE = HOST + "?act=wnj_order_fee&op=apply_order_fee";
    // 请求地图上面 id 的接口
    public static final String URL_ADDRESS_ID= HOST + "?act=wnj_area&op=get_area_id";


    //public static final String HOST  "http://192.168.23.1/haoshop/mobile/index.php";
    public static final String URL_SHOP = HOST + "?act=shop&op=index";
    public static final String URL_SHOP_CLASS = HOST + "?act=shop_class&op=index";

    public static final String URL_STORE = HOST
            + "?act=show_store&op=goods_all";

    // 店铺商品列表 URL
    public static final String URL_STORE_DETAIL = HOST
            + "?act=show_store&op=index";
    public static final String URL_STORE_FAVORITES_LIST = HOST
            + "?act=member_favorites_store&op=favorites_list";
    public static final String URL_STORE_FAVORITES_ADD = HOST
            + "?act=member_favorites_store&op=favorites_add";
    public static final String URL_STORE_FAVORITES_DEL = HOST
            + "?act=member_favorites_store&op=favorites_del";
    public static final String URL_STORE_FAVORITES_FLAG = HOST
            + "?act=member_favorites_store&op=isfavorites";
    public static final String URL_COMMENTS_LIST = HOST
            + "?act=buycomments&op=comments_list";
    public static final String URL_ADDRESS_ADD = HOST
            + "?act=member_address&op=address_add";
    public static final String URL_ADDRESS_DETAILS = HOST
            + "?act=member_address&op=address_info";
    public static final String URL_ADDRESS_DETELE = HOST
            + "?act=member_address&op=address_del";
    public static final String URL_ADDRESS_EDIT = HOST
            + "?act=member_address&op=address_edit";
    public static final String URL_ADDRESS_LIST = HOST
            + "?act=member_address&op=address_list";
    public static final String URL_ADD_CART = HOST
            + "?act=member_cart&op=cart_add";
    public static final String URL_ADD_FAVORITES = HOST
            + "?act=member_favorites&op=favorites_add";
    public static final String URL_BUY_STEP1 = HOST
            + "?act=member_buy&op=buy_step1";
    public static final String URL_BUY_STEP2 = HOST
            + "?act=member_buy&op=buy_step2";
    public static final String URL_CART_DETELE = HOST
            + "?act=member_cart&op=cart_del";
    public static final String URL_CART_EDIT_QUANTITY = HOST
            + "?act=member_cart&op=cart_edit_quantity";
    public static final String URL_CART_LIST = HOST
            + "?act=member_cart&op=cart_list";
    public static final String URL_CHECK_PASSWORD = HOST
            + "?act=member_buy&op=check_password";
    public static final String URL_CONTEXTPATH = HOST + "?";
    public static final String URL_FAVORITES_FLAG = HOST
            + "?act=member_favorites&op=isfavorites";
    public static final String URL_FAVORITES_DELETE = HOST
            + "?act=member_favorites&op=favorites_del";
    public static final String URL_FAVORITES_LIST = HOST
            + "?act=member_favorites&op=favorites_list";
    public static final String URL_FEEDBACK_ADD = HOST
            + "?act=member_feedback&op=feedback_add";
    public static final String URL_GET_CITY = HOST
            + "?act=member_address&op=area_list";
    public static final String URL_GOODSCLASS = HOST + "?act=goods_class";
    public static final String URL_GOODSDETAILS = HOST
            + "?act=goods&op=goods_detail";
    public static final String URL_GOODSLIST = HOST
            + "?act=goods&op=goods_list";
    public static final String URL_GOODS_DETAILS_WEB = HOST
            + "?act=goods&op=goods_body";
    public static final String URL_HELP = "http://www.shopnctest.com/b2b2c/2014/demo/mobile/help.html";
    public static final String URL_HOME = HOST + "?act=index&op=index";
    public static final String URL_INVOICE_ADD = HOST
            + "?act=member_invoice&op=invoice_add";
    public static final String URL_INVOICE_CONTEXT_LIST = HOST
            + "?act=member_invoice&op=invoice_content_list";
    public static final String URL_INVOICE_DELETE = HOST
            + "?act=member_invoice&op=invoice_del";
    public static final String URL_INVOICE_LIST = HOST
            + "?act=member_invoice&op=invoice_list";
    public static final String URL_LOGIN = HOST + "?act=login";
    public static final String URL_LOGIN_OUT = HOST + "?act=logout";
    // public static final String URL_MEMBER_CHAT =
    // HOST+"?act=member_chat&op=get_node_info&key=";
    public static final String URL_MEMBER_CHAT = HOST
            + "?act=member_chat&op=get_node_info";
    public static final String URL_MEMBER_CHAT_GET_LOG_INFO = HOST
            + "?act=member_chat&op=get_chat_log";
    public static final String URL_MEMBER_CHAT_GET_USER_INFO = HOST
            + "?act=member_chat&op=get_info";
    public static final String URL_MEMBER_CHAT_GET_USER_LIST = HOST
            + "?act=member_chat&op=get_user_list";
    public static final String URL_MEMBER_CHAT_SEND_MSG = HOST
            + "?act=member_chat&op=send_msg";
    public static final String URL_MEMBER_CHAT_FRIEND_LIST = HOST
            + "?act=member_snsfriend&op=friend_list";
    public static final String URL_MEMBER_CHAT_MEMBER_LIST = HOST
            + "?act=member_snsfriend&op=member_list";
    public static final String URL_MEMBER_VR_BUY1 = HOST
            + "?act=member_vr_buy&op=buy_step1";
    public static final String URL_MEMBER_VR_BUY2 = HOST
            + "?act=member_vr_buy&op=buy_step2";
    public static final String URL_MEMBER_VR_BUY3 = HOST
            + "?act=member_vr_buy&op=buy_step3";
    public static final String URL_MEMBER_VR_ODER = HOST
            + "?act=member_vr_order&op=indate_code_list";
    public static final String URL_MEMBER_VR_ORDER = HOST
            + "?act=member_vr_order&op=order_list";
    public static final String URL_MEMBER_VR_ORDER_CANCEL = HOST
            + "?act=member_vr_order&op=order_cancel";
    public static final String URL_MYSTOIRE = HOST + "?act=member_index";
    public static final String URL_ORDER_CANCEL = HOST
            + "?act=member_order&op=order_cancel";
    public static final String URL_ORDER_LIST = HOST
            + "?act=member_order&op=order_list";

    public static final String URL_ORDER_TAIPAYMENT = HOST
            + "?act=payment_api&op=index";
    public static final String URL_ORDER_PAYMENT = HOST
            + "?act=member_payment&op=pay";
    public static final String URL_ORDER_VRPAYMENT = HOST
            + "?act=member_payment&op=vr_pay";
    public static final String URL_ORDER_PAYMENT_LIST = HOST
            + "?act=member_payment&op=payment_list";
    public static final String URL_ORDER_RECEIVE = HOST
            + "?act=member_order&op=order_receive";
    public static final String URL_QUERY_DELIVER = HOST
            + "?act=member_order&op=search_deliver";

    public static final String URL_REGISTER = HOST + "?act=login&op=register";
    public static final String URL_SPECIAL = HOST + "?act=index&op=special";
    public static final String URL_UPDATE_ADDRESS = HOST
            + "?act=member_buy&op=change_address";
    public static final String URL_VERSION_UPDATE = HOST
            + "?act=index&op=apk_version";
    public static final String URL_VIRTUAL_ORDER_PAYMENT = HOST
            + "?act=member_payment&op=vr_pay";
    public static final String URL_VOUCHER = HOST
            + "?act=member_voucher&op=voucher_list";

    public static final String URL_HOME_RANK = HOST
            + "?act=index&op=index_sort";
    public static final String URL_HOME_NEW = HOST
            + "?act=index&op=index_new";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void cancelRequests(Context context, Boolean mayInterruptIfRunning) {
        client.cancelRequests(context, mayInterruptIfRunning);
    }

    public static void cancelAllRequests(Boolean mayInterruptIfRunning) {
        client.cancelAllRequests(true);
    }


    public static void post(String urlString, AsyncHttpResponseHandler res) {
        client.post(urlString, res);
    }


    public static void post(String urlString, RequestParams params,
                            AsyncHttpResponseHandler res) {
        client.post(urlString, params, res);
    }


    public static void post(String uString, BinaryHttpResponseHandler bHandler) {
        client.post(uString, bHandler);
    }

    public static void get(String urlString, AsyncHttpResponseHandler res) {
        client.get(urlString, res);
    }

    public static void get(String urlString, RequestParams params,
                           AsyncHttpResponseHandler res) {
        client.get(urlString, params, res);
    }

    public static void get(String urlString, RequestParams params,
                           JsonHttpResponseHandler res) {
        client.get(urlString, params, res);
    }

    public static void get(String uString, BinaryHttpResponseHandler bHandler) {
        client.get(uString, bHandler);
    }

    public static AsyncHttpClient getClient() {
        return client;
    }
}