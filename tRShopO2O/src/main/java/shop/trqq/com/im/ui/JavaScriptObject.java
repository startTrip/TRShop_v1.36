package shop.trqq.com.im.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.google.gson.Gson;

import org.litepal.crud.DataSupport;
import org.myjson.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.greenrobot.event.EventBus;
import shop.trqq.com.AppManager;
import shop.trqq.com.R;
import shop.trqq.com.event.EventIM_UnRead;
import shop.trqq.com.im.bean.UserBean;
import shop.trqq.com.im.bean.getmsgBean;
import shop.trqq.com.util.YkLog;

public class JavaScriptObject {
    private Context mContxt;
    private Gson gson;
    private WebView webView;
    private IMMsgActivity mIMMsgActivity;
    private final static String ACTION_GENERAL_SEND = "com.trqq.shop.im.getmsg";
    //private final static String ACTION_GENERAL_SEND_USER = "com.trqq.shop.im.unreadnum";

    public JavaScriptObject(Context mContxt, WebView webView) {
        this.mContxt = mContxt;
        gson = new Gson();
        this.webView = webView;
    }

    @JavascriptInterface
    public void get_state(String name) {
        YkLog.i("调用get_state:", name);
    }

    @JavascriptInterface
    public void get_msg(String name) {
        YkLog.i("调用get_msg:", name);
        setmsgBean(new JSONObject(name));
        Intent i = new Intent(ACTION_GENERAL_SEND);
        i.putExtra("get_msg", name);
        mContxt.sendBroadcast(i);
    }

    @JavascriptInterface
    public void del_msg(String name) {
        YkLog.i("调用del_msg:", name);
    }

    @JavascriptInterface
    public void connect(String name) {
        YkLog.i("调用connect:", name);
    }

    // 取getmsgBean
    @JavascriptInterface
    private void setmsgBean(JSONObject jsonObject) {
        int i = 0;
        // 无序
        Iterator keyIter = jsonObject.keys();
        List<getmsgBean> list = new ArrayList<getmsgBean>();
        getmsgBean lastbean = new getmsgBean();
        List<UserBean> mUserList = new ArrayList<UserBean>();
        while (keyIter.hasNext()) {
            try {
                boolean isUpdate = true;
                getmsgBean bean = new getmsgBean();
                String id = (String) keyIter.next();
                // bean.setSpecID(id);
                bean = gson
                        .fromJson(jsonObject.optString(id), getmsgBean.class);
                list.add(bean);
                /*
				 * User userbean=bean.getUser(); userbean.save(); YkLog.t("信息",
				 * bean.getUser().getAvatar());
				 */
                i++;
                int maxid = Integer.parseInt(id);
                JSONObject u_list = new JSONObject();
                u_list.put("max_id", maxid);
                u_list.put("f_id", bean.getF_id());
                final String u_listStr = u_list.toString();
                // new WebView(context);不执行，要在UI线程findViewById
                webView.post(new Runnable() {
                    public void run() {
                        webView.loadUrl("javascript:node_del_msg(" + u_listStr
                                + ")");
                    }
                });


                // 如果当前Activity是IMMsgActivity
                if ("IMMsgActivity".equals(AppManager.getAppManager()
                        .currentActivityString())) {
                    mIMMsgActivity = (IMMsgActivity) AppManager.getAppManager()
                            .currentActivity();
                    // 是否正在和F_id聊天，是就不发送通知
                    // YkLog.t(mIMMsgActivity.getT_id()+"|", list.get(list.size() -
                    // 1).getF_id()+"");
                    if (list.get(list.size() - 1).getF_id() == Integer
                            .parseInt(mIMMsgActivity.getT_id())) {
                        isUpdate = false;
                    }
                }//是否更新
                if (isUpdate) {
                    List<UserBean> mUserBeanList = DataSupport.where(
                            "u_id = ?", "" + bean.getF_id()).find(UserBean.class);

                    UserBean mUserBean = new UserBean();
                    if (mUserBeanList.size() == 0) {
                        //表里没有该用户生成表
                        mUserBean.setU_id(bean.getF_id());
                        mUserBean.setUnreadnum(1);
                        mUserBean.save();
                    } else {
                        //更新数据库
                        mUserBean.setUnreadnum(mUserBeanList.get(0).getUnreadnum() + 1);
                        mUserBean.updateAll("u_id = ?", "" + bean.getF_id());
                    }
					/*Intent mIntent = new Intent(ACTION_GENERAL_SEND_USER);
					mIntent.putExtra("u_id", bean.getF_id());
					mIntent.putExtra("unReadNum", mUserBean.getUnreadnum());
					mContxt.sendBroadcast(mIntent);*/
                    mUserList.add(mUserBean);
                    // 发布事件，在后台线程发的事件
                    EventBus.getDefault().post(new EventIM_UnRead(bean.getF_id(), mUserBean.getUnreadnum()));
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        DataSupport.saveAll(list);

        if (list.size() > 0) {
            // YkLog.t("AppManager.getAppManager().currentActivityString())",
            // AppManager.getAppManager().currentActivityString());
            // 如果当前Activity是IMMsgActivity
            if ("IMMsgActivity".equals(AppManager.getAppManager()
                    .currentActivityString())) {
                mIMMsgActivity = (IMMsgActivity) AppManager.getAppManager()
                        .currentActivity();
                // 是否正在和F_id聊天，是就不发送通知
                // YkLog.t(mIMMsgActivity.getT_id()+"|", list.get(list.size() -
                // 1).getF_id()+"");
                if (list.get(list.size() - 1).getF_id() == Integer
                        .parseInt(mIMMsgActivity.getT_id())) {
                    return;
                }
            }
            showNotification(mContxt, list.get(list.size() - 1));
        }
    }

    /**
     * 在状态栏显示通知
     */
    @SuppressWarnings("deprecation")
    private void showNotification(Context context, getmsgBean bean) {
        // 创建一个NotificationManager的引用
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(android.content.Context.NOTIFICATION_SERVICE);

        // 定义Notification的各种属性
        Notification notification = new Notification(R.drawable.ic_launcher,
                bean.getF_name(), System.currentTimeMillis());
        // FLAG_AUTO_CANCEL 该通知能被状态栏的清除按钮给清除掉
        // FLAG_NO_CLEAR 该通知不能被状态栏的清除按钮给清除掉
        // FLAG_ONGOING_EVENT 通知放置在正在运行
        // FLAG_INSISTENT 是否一直进行，比如音乐一直播放，知道用户响应
        // notification.flags |= Notification.FLAG_ONGOING_EVENT; //
        // 将此通知放到通知栏的"Ongoing"即"正在运行"组中
        notification.flags |= Notification.FLAG_AUTO_CANCEL; // 表明在点击了通知栏中的"清除通知"后，此通知不清除，经常与FLAG_ONGOING_EVENT一起使用
        notification.flags |= Notification.FLAG_SHOW_LIGHTS;
        // DEFAULT_ALL 使用所有默认值，比如声音，震动，闪屏等等
        // DEFAULT_LIGHTS 使用默认闪光提示
        // DEFAULT_SOUNDS 使用默认提示声音
        // DEFAULT_VIBRATE 使用默认手机震动，需加上<uses-permission
        // android:name="android.permission.VIBRATE" />权限
        notification.defaults = Notification.DEFAULT_ALL;
        // 叠加效果常量
        // notification.defaults=Notification.DEFAULT_LIGHTS|Notification.DEFAULT_SOUND;
        notification.ledARGB = Color.BLUE;
        notification.ledOnMS = 5000; // 闪光时间，毫秒

        // 设置通知的事件消息
        CharSequence contentTitle = "收到：" + bean.getF_name() + "的新消息"; // 通知栏标题
        CharSequence contentText = bean.getT_msg(); // 通知栏内容

        Intent notificationIntent = new Intent(context, IMMsgActivity.class); // 点击该通知后要跳转的Activity
        String t_id = String.valueOf(bean.getF_id());
        notificationIntent.putExtra("t_id", t_id);
        notificationIntent.putExtra("t_name", bean.getF_name());
        // 这个意图一旦产生，就会自动清除栈顶的活动，即是说，上一个被打开的活动会被终结掉，于是就实现了没有两个相同的活动被同时打开
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentItent = PendingIntent.getActivity(context, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setLatestEventInfo(context, contentTitle, contentText,
                contentItent);
//		Notification noti = new Notification.Builder(context)
//        .setContentTitle(contentTitle)
//        .setContentText(contentText)
//        .setSmallIcon(R.drawable.ic_launcher)
//        .setContentIntent(contentItent)
//        .build();
        // 把Notification传递给NotificationManager标记ID为1
        notificationManager.notify(1, notification);
    }

}
