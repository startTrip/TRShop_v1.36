package shop.trqq.com;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import shop.trqq.com.bean.GoodsBean;
import shop.trqq.com.bean.UserInfoBean;
import shop.trqq.com.im.bean.UserBean;
import shop.trqq.com.im.bean.getmsgBean;
import shop.trqq.com.util.AESCipher;
import shop.trqq.com.util.MD5Util;
import shop.trqq.com.util.YkLog;

/**
 * 用户信息管理类
 *
 * @author
 * @version 1.0
 * @created 2015-04-15
 */
public class UserManager {

    private static final String TAG = "UserManager";

    // 全局Context
    private Context mContext;
    // Gson工具
    private static Gson gson;

    private static UserInfoBean userInfo;

    private static List<GoodsBean> historyList;
    private static List<String> serchList;
    // 加密seed别问我是怎样生成的
    private static String seed = "fghjklertyuiovbnmkjhgfvdcsxlkjhgfdskjhgfdsascvbnmertyuio";

    public UserManager(Context context) {
        this.mContext = context;
        // 初始化Gson工具
        gson = new Gson();
        // 商品历史信息
        historyList = new ArrayList<GoodsBean>();
        // 搜索历史信息
        serchList = new ArrayList<String>();
        // 初始化用户信息
        initUserInfo();
        loadLocalhistoryListData();
        loadLocalSearchListData();
    }

    // 得到本地配置信息
    private void initUserInfo() {
        YkLog.i(TAG, "初始化用户信息");
        String userInfoJson = AppConfig.getSharedPreferences(mContext)
                .getString(AppConfig.USER_INFO, "");
        json2Bean(mContext, userInfoJson);
        userInfo.setUserIcon(getavator(mContext));
    }

    public static UserInfoBean getUserInfo() {
        return userInfo;
    }

    /**
     * 是否登陆
     *
     * @return
     */
    public static boolean isLogin() {
        return userInfo.isLogin();
    }

    public static void setUserInfo(Boolean isLogin) {
        UserManager.userInfo.setLogin(isLogin);
    }

    /**
     * 获得用户ID
     *
     * @return
     */
    public static String getUserId() {
        return userInfo.getUserId() == null ? "" : userInfo.getUserId();
    }

    // 获取浏览记录
    public static List<GoodsBean> getHistoryList() {
        return historyList == null ? new ArrayList<GoodsBean>() : historyList;
    }

    // 更新浏览记录
    public static void updateHistoryList(Context context, GoodsBean goodsBean) {
        historyList = getHistoryList();
        for (int i = 0; i < historyList.size(); i++) {
            if (historyList.get(i).getGoods_id()
                    .equals(goodsBean.getGoods_id()))
                historyList.remove(i);
        }
        historyList.add(0, goodsBean);
        if (historyList.size() == 21)
            historyList.remove(20);
        setHistoryList(context, historyList);
    }

    // 保存浏览记录
    public static void setHistoryList(Context context,
                                      List<GoodsBean> historyList) {
        UserManager.historyList = historyList;
        String json = gson.toJson(historyList);
        AppConfig.setSharedPreferences(context, "historyList", json);
    }

    // 加载本地浏览记录
    private void loadLocalhistoryListData() {
        try {
            // 加载本地数据
            String json = AppConfig.getSharedPreferences(mContext).getString(
                    "historyList", "");
            historyList = gson.fromJson(json, new TypeToken<List<GoodsBean>>() {
            }.getType());
        } catch (Exception e) {
            // 空指针异常，所以要重新初始化
            historyList = new ArrayList<GoodsBean>();
        }
    }

    // 获取搜索记录
    public static List<String> getSearchList() {
        return serchList == null ? new ArrayList<String>() : serchList;
    }

    // 保存搜索记录
    public static void setSearchList(Context context, List<String> List) {
        serchList = List;
        String json = gson.toJson(serchList);
        AppConfig.setSharedPreferences(context, "serchList", json);
    }

    // 加载本地搜索记录
    private void loadLocalSearchListData() {
        try {
            // 加载本地数据
            String json = AppConfig.getSharedPreferences(mContext).getString(
                    "serchList", "");
            serchList = gson.fromJson(json, new TypeToken<List<String>>() {
            }.getType());
        } catch (Exception e) {
            // 空指针异常，所以要重新初始化
            serchList = new ArrayList<String>();
        }
    }

    /**
     * json格式转成用户信息加密并保存到本地配置中
     *
     * @param context
     * @param json
     * @return
     */
    public static boolean jsonToBean(Context context, String json) {
        // json字符串ToBean
        userInfo = gson.fromJson(json, UserInfoBean.class);
        YkLog.e("userInfo","登录前"+userInfo.toString());
        if (userInfo == null) {
            userInfo = new UserInfoBean();
            userInfo.setLogin(false);
            return false;
        }
        if (!userInfo.getNickname().equals("")) {
            userInfo.setLogin(true);
            String key;
            YkLog.e("userInfo",userInfo.toString());
            try {
                // AES加密key
                key = AESCipher.encrypt(getAesKey(context), userInfo.getKey());
                // 得到没有加密的key
                String tempkey = userInfo.getKey();
                // 带有加密信息的key生成 json 格式的字符串(使用户看不见)
                userInfo.setKey(key);
                json = gson.toJson(userInfo, UserInfoBean.class);
                // 将没有加密的key 重新设置上去
                userInfo.setKey(tempkey);
                // TestUtils.println_out("key", json);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // 将用户信息 json 字符串保存到 sharePreferences 中
            AppConfig.setSharedPreferences(context, AppConfig.USER_INFO, json);
            return true;
        } else
            return false;
    }

    /**
     * json格式转成用户信息解密并保存到本地配置中
     *
     * @param context
     * @param json
     * @return
     */
    public static boolean json2Bean(Context context, String json) {
        // json字符串ToBean
        userInfo = gson.fromJson(json, UserInfoBean.class);
        if (userInfo == null) {
            userInfo = new UserInfoBean();
            userInfo.setLogin(false);
            return false;
        }
        if (!userInfo.getNickname().equals("")) {
            userInfo.setLogin(true);
            String key;
            try {
                // AES解密key
                key = AESCipher.decrypt(getAesKey(context), userInfo.getKey());
                userInfo.setKey(key);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return true;
        } else
            return false;
    }

    // 获取AesKey
    private static String getAesKey(Context context) {
        // 获取ANDROID_ID
        String ANDROID_ID = Settings.System.getString(
                context.getContentResolver(), Settings.System.ANDROID_ID);
        // MD5加密ANDROID_ID+seed
        return MD5Util.getMD5String(ANDROID_ID + seed);
    }

    // 保存头像
    public static void setavator(Context context, String avator) {
        AppConfig.setSharedPreferences(context, "avator", avator);
        userInfo.setUserIcon(avator);
    }

    // 获取头像
    public static String getavator(Context context) {
        String avator = AppConfig.getSharedPreferences(context).getString(
                "avator", "");
        return avator;
    }

    // 清除信息
    public static void cleanInfo(Context context) {
        AppConfig.setSharedPreferences(context, AppConfig.USER_INFO, "");
        AppConfig.setSharedPreferences(context, "avator", "");
        AppConfig.setSharedPreferences(context, "historyList", "");
        DataSupport.deleteAll(getmsgBean.class);
        DataSupport.deleteAll(UserBean.class);
        userInfo = new UserInfoBean();
    }
}