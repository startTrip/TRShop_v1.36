package com.baidu.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.android.pushservice.PushMessageReceiver;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import shop.trqq.com.AppConfig;
import shop.trqq.com.R;
import shop.trqq.com.bean.MsgBean;
import shop.trqq.com.ui.SystemMsgActivity;
import shop.trqq.com.util.YkLog;

/*
 * Push消息处理receiver。请编写您需要的回调函数， 一般来说： onBind是必须的，用来处理startWork返回值；
 *onMessage用来接收透传消息； onSetTags、onDelTags、onListTags是tag相关操作的回调；
 *onNotificationClicked在通知被点击时回调； onUnbind是stopWork接口的返回值回调

 * 返回值中的errorCode，解释如下：
 *0 - Success
 *10001 - Network Problem
 *10101  Integrate Check Error
 *30600 - Internal Server Error
 *30601 - Method Not Allowed
 *30602 - Request Params Not Valid
 *30603 - Authentication Failed
 *30604 - Quota Use Up Payment Required
 *30605 -Data Required Not Found
 *30606 - Request Time Expires Timeout
 *30607 - Channel Token Timeout
 *30608 - Bind Relation Not Found
 *30609 - Bind Number Too Many

 * 当您遇到以上返回错误时，如果解释不了您的问题，请用同一请求的返回值requestId和errorCode联系我们追查问题。
 *
 */

public class MyPushMessageReceiver extends PushMessageReceiver {
    /**
     * TAG to Log
     */
    public static final String TAG = MyPushMessageReceiver.class
            .getSimpleName();
    private final static String ACTION_GENERAL_SEND = "com.baidu.push.trshop";

    /**
     * 调用PushManager.startWork后，sdk将对push
     * server发起绑定请求，这个过程是异步的。绑定请求的结果通过onBind返回。 如果您需要用单播推送，需要把这里获取的channel
     * id和user id上传到应用server中，再调用server接口用channel id和user id给单个手机或者用户推送。
     *
     * @param context   BroadcastReceiver的执行Context
     * @param errorCode 绑定接口返回值，0 - 成功
     * @param appid     应用id。errorCode非0时为null
     * @param userId    应用user id。errorCode非0时为null
     * @param channelId 应用channel id。errorCode非0时为null
     * @param requestId 向服务端发起的请求id。在追查问题时有用；
     * @return none
     */
    @Override
    public void onBind(Context context, int errorCode, String appid,
                       String userId, String channelId, String requestId) {
        String responseString = "onBind errorCode=" + errorCode + " appid="
                + appid + " userId=" + userId + " channelId=" + channelId
                + " requestId=" + requestId;
        YkLog.t(TAG, responseString);

        if (errorCode == 0) {
            // 绑定成功
        }
        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        updateContent(context, responseString);
    }

    /**
     * 接收透传消息的函数。
     *
     * @param context             上下文
     * @param message             推送的消息
     * @param customContentString 自定义内容,为空或者json字符串
     */
    @Override
    public void onMessage(Context context, String message,
                          String customContentString) {
        String messageString = "透传消息 message=\"" + message
                + "\" customContentString=" + customContentString;
        Log.d(TAG, messageString);

        // 自定义内容获取方式，mykey和myvalue对应透传消息推送时自定义内容中设置的键和值
        if (!TextUtils.isEmpty(customContentString)) {
            JSONObject customJson = null;
            try {
                customJson = new JSONObject(customContentString);
                String myvalue = null;
                if (!customJson.isNull("mykey")) {
                    myvalue = customJson.getString("mykey");
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        updateContent(context, messageString);
        loadLocalMSGData(context, message);

		/*
         * Intent intent = new Intent(context, xitonginfoActivity.class);
		 * intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		 * context.startActivity(intent);
		 */

    }

    // 加载本地透传消息并保存
    private void loadLocalMSGData(Context context, String message) {
        String json = AppConfig.getSharedPreferences(context).getString(
                "msginfo", "");
        Gson gson = new Gson();
        List<MsgBean> msglist = new ArrayList<MsgBean>();
        MsgBean tmpBean = new MsgBean();
        try {
            msglist = gson.fromJson(json, new TypeToken<List<MsgBean>>() {
            }.getType());
            if (json.equals("")) {
                msglist = new ArrayList<MsgBean>();
            }
        } catch (Exception e) {
            // 空指针异常，所以要重新初始化
            e.printStackTrace();
            msglist = new ArrayList<MsgBean>();
        }

        tmpBean = gson.fromJson(message, MsgBean.class);
        msglist.add(tmpBean);
        String msgString = gson.toJson(msglist, new TypeToken<List<MsgBean>>() {
        }.getType());
        // 保存系统消息
        AppConfig.setSharedPreferences(context, "msginfo", msgString);
        // 读取未读系统消息数量并增加
        int num = AppConfig.getSharedPreferences(context).getInt("msgnum", 0);
        num++;
        AppConfig.setSharedPreferences(context, "msgnum", num);
        Intent i = new Intent(ACTION_GENERAL_SEND);
        i.putExtra("msgnum", num);
        context.sendBroadcast(i);
        //是否接收自定义透传消息
        if (AppConfig.getSharedPreferences(context).getBoolean(
                "Notification_switch", true)) {
            showNotification(context, tmpBean.getTitle(), tmpBean.getContent());
        }
    }

    /**
     * 在状态栏显示通知
     */
    private void showNotification(Context context, String title, String Text) {
        // 创建一个NotificationManager的引用
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(android.content.Context.NOTIFICATION_SERVICE);

        // 定义Notification的各种属性
        Notification notification = new Notification(R.drawable.ic_launcher,
                title, System.currentTimeMillis());
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
        notification.defaults = Notification.DEFAULT_LIGHTS;
        // 叠加效果常量
        // notification.defaults=Notification.DEFAULT_LIGHTS|Notification.DEFAULT_SOUND;
        notification.ledARGB = Color.BLUE;
        notification.ledOnMS = 5000; // 闪光时间，毫秒

        // 设置通知的事件消息
        CharSequence contentTitle = title; // 通知栏标题
        CharSequence contentText = Text; // 通知栏内容
        Intent notificationIntent = new Intent(context, SystemMsgActivity.class); // 点击该通知后要跳转的Activity
        PendingIntent contentItent = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);
        //6.0弃用
        notification.setLatestEventInfo(context, contentTitle, contentText,
                contentItent);
        //API16 开始使用
//		 Notification noti = new Notification.Builder(context)
//         .setContentTitle(contentTitle)
//         .setContentText(contentText)
//         .setSmallIcon(R.drawable.ic_launcher)
//         .setContentIntent(contentItent)
//         .build();
        // 把Notification传递给NotificationManager标记ID为0
        notificationManager.notify(0, notification);
    }

    /**
     * 接收通知点击的函数。
     *
     * @param context             上下文
     * @param title               推送的通知的标题
     * @param description         推送的通知的描述
     * @param customContentString 自定义内容，为空或者json字符串
     */
    @Override
    public void onNotificationClicked(Context context, String title,
                                      String description, String customContentString) {
        String notifyString = "通知点击 title=\"" + title + "\" description=\""
                + description + "\" customContent=" + customContentString;
        Log.d(TAG, notifyString);

        // 自定义内容获取方式，mykey和myvalue对应通知推送时自定义内容中设置的键和值
        if (!TextUtils.isEmpty(customContentString)) {
            JSONObject customJson = null;
            try {
                customJson = new JSONObject(customContentString);
                String myvalue = null;
                if (!customJson.isNull("mykey")) {
                    myvalue = customJson.getString("mykey");
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        updateContent(context, notifyString);
    }

    /**
     * 接收通知到达的函数。
     *
     * @param context             上下文
     * @param title               推送的通知的标题
     * @param description         推送的通知的描述
     * @param customContentString 自定义内容，为空或者json字符串
     */

    @Override
    public void onNotificationArrived(Context context, String title,
                                      String description, String customContentString) {

        String notifyString = "onNotificationArrived  title=\"" + title
                + "\" description=\"" + description + "\" customContent="
                + customContentString;
        Log.d(TAG, notifyString);

        // 自定义内容获取方式，mykey和myvalue对应通知推送时自定义内容中设置的键和值
        if (!TextUtils.isEmpty(customContentString)) {
            JSONObject customJson = null;
            try {
                customJson = new JSONObject(customContentString);
                String myvalue = null;
                if (!customJson.isNull("mykey")) {
                    myvalue = customJson.getString("mykey");
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        // 你可以參考 onNotificationClicked中的提示从自定义内容获取具体值
        updateContent(context, notifyString);
    }

    /**
     * setTags() 的回调函数。
     *
     * @param context     上下文
     * @param errorCode   错误码。0表示某些tag已经设置成功；非0表示所有tag的设置均失败。
     * @param successTags 设置成功的tag
     * @param failTags    设置失败的tag
     * @param requestId   分配给对云推送的请求的id
     */
    @Override
    public void onSetTags(Context context, int errorCode,
                          List<String> sucessTags, List<String> failTags, String requestId) {
        String responseString = "onSetTags errorCode=" + errorCode
                + " sucessTags=" + sucessTags + " failTags=" + failTags
                + " requestId=" + requestId;
        Log.d(TAG, responseString);

        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        updateContent(context, responseString);
    }

    /**
     * delTags() 的回调函数。
     *
     * @param context     上下文
     * @param errorCode   错误码。0表示某些tag已经删除成功；非0表示所有tag均删除失败。
     * @param successTags 成功删除的tag
     * @param failTags    删除失败的tag
     * @param requestId   分配给对云推送的请求的id
     */
    @Override
    public void onDelTags(Context context, int errorCode,
                          List<String> sucessTags, List<String> failTags, String requestId) {
        String responseString = "onDelTags errorCode=" + errorCode
                + " sucessTags=" + sucessTags + " failTags=" + failTags
                + " requestId=" + requestId;
        Log.d(TAG, responseString);

        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        updateContent(context, responseString);
    }

    /**
     * listTags() 的回调函数。
     *
     * @param context   上下文
     * @param errorCode 错误码。0表示列举tag成功；非0表示失败。
     * @param tags      当前应用设置的所有tag。
     * @param requestId 分配给对云推送的请求的id
     */
    @Override
    public void onListTags(Context context, int errorCode, List<String> tags,
                           String requestId) {
        String responseString = "onListTags errorCode=" + errorCode + " tags="
                + tags;
        Log.d(TAG, responseString);

        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        updateContent(context, responseString);
    }

    /**
     * PushManager.stopWork() 的回调函数。
     *
     * @param context   上下文
     * @param errorCode 错误码。0表示从云推送解绑定成功；非0表示失败。
     * @param requestId 分配给对云推送的请求的id
     */
    @Override
    public void onUnbind(Context context, int errorCode, String requestId) {
        String responseString = "onUnbind errorCode=" + errorCode
                + " requestId = " + requestId;
        Log.d(TAG, responseString);

        if (errorCode == 0) {
            // 解绑定成功
        }
        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        updateContent(context, responseString);
    }

    private void updateContent(Context context, String content) {
        Log.d(TAG, "updateContent");
		/*
		 * String logText = "" + Utils.logStringCache;
		 * 
		 * if (!logText.equals("")) { logText += "\n"; }
		 * 
		 * SimpleDateFormat sDateFormat = new SimpleDateFormat("HH-mm-ss");
		 * logText += sDateFormat.format(new Date()) + ": "; logText += content;
		 * 
		 * Utils.logStringCache = logText;
		 */

		/*
		 * Intent intent = new Intent();
		 * intent.setClass(context.getApplicationContext(),
		 * PushDemoActivity.class);
		 * intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		 * context.getApplicationContext().startActivity(intent);
		 */
    }

}
