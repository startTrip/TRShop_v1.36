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
 * �û���Ϣ������
 *
 * @author
 * @version 1.0
 * @created 2015-04-15
 */
public class UserManager {

    private static final String TAG = "UserManager";

    // ȫ��Context
    private Context mContext;
    // Gson����
    private static Gson gson;

    private static UserInfoBean userInfo;

    private static List<GoodsBean> historyList;
    private static List<String> serchList;
    // ����seed���������������ɵ�
    private static String seed = "fghjklertyuiovbnmkjhgfvdcsxlkjhgfdskjhgfdsascvbnmertyuio";

    public UserManager(Context context) {
        this.mContext = context;
        // ��ʼ��Gson����
        gson = new Gson();
        // ��Ʒ��ʷ��Ϣ
        historyList = new ArrayList<GoodsBean>();
        // ������ʷ��Ϣ
        serchList = new ArrayList<String>();
        // ��ʼ���û���Ϣ
        initUserInfo();
        loadLocalhistoryListData();
        loadLocalSearchListData();
    }

    // �õ�����������Ϣ
    private void initUserInfo() {
        YkLog.i(TAG, "��ʼ���û���Ϣ");
        String userInfoJson = AppConfig.getSharedPreferences(mContext)
                .getString(AppConfig.USER_INFO, "");
        json2Bean(mContext, userInfoJson);
        userInfo.setUserIcon(getavator(mContext));
    }

    public static UserInfoBean getUserInfo() {
        return userInfo;
    }

    /**
     * �Ƿ��½
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
     * ����û�ID
     *
     * @return
     */
    public static String getUserId() {
        return userInfo.getUserId() == null ? "" : userInfo.getUserId();
    }

    // ��ȡ�����¼
    public static List<GoodsBean> getHistoryList() {
        return historyList == null ? new ArrayList<GoodsBean>() : historyList;
    }

    // ���������¼
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

    // ���������¼
    public static void setHistoryList(Context context,
                                      List<GoodsBean> historyList) {
        UserManager.historyList = historyList;
        String json = gson.toJson(historyList);
        AppConfig.setSharedPreferences(context, "historyList", json);
    }

    // ���ر��������¼
    private void loadLocalhistoryListData() {
        try {
            // ���ر�������
            String json = AppConfig.getSharedPreferences(mContext).getString(
                    "historyList", "");
            historyList = gson.fromJson(json, new TypeToken<List<GoodsBean>>() {
            }.getType());
        } catch (Exception e) {
            // ��ָ���쳣������Ҫ���³�ʼ��
            historyList = new ArrayList<GoodsBean>();
        }
    }

    // ��ȡ������¼
    public static List<String> getSearchList() {
        return serchList == null ? new ArrayList<String>() : serchList;
    }

    // ����������¼
    public static void setSearchList(Context context, List<String> List) {
        serchList = List;
        String json = gson.toJson(serchList);
        AppConfig.setSharedPreferences(context, "serchList", json);
    }

    // ���ر���������¼
    private void loadLocalSearchListData() {
        try {
            // ���ر�������
            String json = AppConfig.getSharedPreferences(mContext).getString(
                    "serchList", "");
            serchList = gson.fromJson(json, new TypeToken<List<String>>() {
            }.getType());
        } catch (Exception e) {
            // ��ָ���쳣������Ҫ���³�ʼ��
            serchList = new ArrayList<String>();
        }
    }

    /**
     * json��ʽת���û���Ϣ���ܲ����浽����������
     *
     * @param context
     * @param json
     * @return
     */
    public static boolean jsonToBean(Context context, String json) {
        // json�ַ���ToBean
        userInfo = gson.fromJson(json, UserInfoBean.class);
        YkLog.e("userInfo","��¼ǰ"+userInfo.toString());
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
                // AES����key
                key = AESCipher.encrypt(getAesKey(context), userInfo.getKey());
                // �õ�û�м��ܵ�key
                String tempkey = userInfo.getKey();
                // ���м�����Ϣ��key���� json ��ʽ���ַ���(ʹ�û�������)
                userInfo.setKey(key);
                json = gson.toJson(userInfo, UserInfoBean.class);
                // ��û�м��ܵ�key ����������ȥ
                userInfo.setKey(tempkey);
                // TestUtils.println_out("key", json);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // ���û���Ϣ json �ַ������浽 sharePreferences ��
            AppConfig.setSharedPreferences(context, AppConfig.USER_INFO, json);
            return true;
        } else
            return false;
    }

    /**
     * json��ʽת���û���Ϣ���ܲ����浽����������
     *
     * @param context
     * @param json
     * @return
     */
    public static boolean json2Bean(Context context, String json) {
        // json�ַ���ToBean
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
                // AES����key
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

    // ��ȡAesKey
    private static String getAesKey(Context context) {
        // ��ȡANDROID_ID
        String ANDROID_ID = Settings.System.getString(
                context.getContentResolver(), Settings.System.ANDROID_ID);
        // MD5����ANDROID_ID+seed
        return MD5Util.getMD5String(ANDROID_ID + seed);
    }

    // ����ͷ��
    public static void setavator(Context context, String avator) {
        AppConfig.setSharedPreferences(context, "avator", avator);
        userInfo.setUserIcon(avator);
    }

    // ��ȡͷ��
    public static String getavator(Context context) {
        String avator = AppConfig.getSharedPreferences(context).getString(
                "avator", "");
        return avator;
    }

    // �����Ϣ
    public static void cleanInfo(Context context) {
        AppConfig.setSharedPreferences(context, AppConfig.USER_INFO, "");
        AppConfig.setSharedPreferences(context, "avator", "");
        AppConfig.setSharedPreferences(context, "historyList", "");
        DataSupport.deleteAll(getmsgBean.class);
        DataSupport.deleteAll(UserBean.class);
        userInfo = new UserInfoBean();
    }
}