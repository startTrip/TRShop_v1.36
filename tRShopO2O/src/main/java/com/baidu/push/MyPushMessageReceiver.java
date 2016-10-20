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
 * Push��Ϣ����receiver�����д����Ҫ�Ļص������� һ����˵�� onBind�Ǳ���ģ���������startWork����ֵ��
 *onMessage��������͸����Ϣ�� onSetTags��onDelTags��onListTags��tag��ز����Ļص���
 *onNotificationClicked��֪ͨ�����ʱ�ص��� onUnbind��stopWork�ӿڵķ���ֵ�ص�

 * ����ֵ�е�errorCode���������£�
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

 * �����������Ϸ��ش���ʱ��������Ͳ����������⣬����ͬһ����ķ���ֵrequestId��errorCode��ϵ����׷�����⡣
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
     * ����PushManager.startWork��sdk����push
     * server�������������������첽�ġ�������Ľ��ͨ��onBind���ء� �������Ҫ�õ������ͣ���Ҫ�������ȡ��channel
     * id��user id�ϴ���Ӧ��server�У��ٵ���server�ӿ���channel id��user id�������ֻ������û����͡�
     *
     * @param context   BroadcastReceiver��ִ��Context
     * @param errorCode �󶨽ӿڷ���ֵ��0 - �ɹ�
     * @param appid     Ӧ��id��errorCode��0ʱΪnull
     * @param userId    Ӧ��user id��errorCode��0ʱΪnull
     * @param channelId Ӧ��channel id��errorCode��0ʱΪnull
     * @param requestId �����˷��������id����׷������ʱ���ã�
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
            // �󶨳ɹ�
        }
        // Demo���½���չʾ���룬Ӧ��������������Լ��Ĵ����߼�
        updateContent(context, responseString);
    }

    /**
     * ����͸����Ϣ�ĺ�����
     *
     * @param context             ������
     * @param message             ���͵���Ϣ
     * @param customContentString �Զ�������,Ϊ�ջ���json�ַ���
     */
    @Override
    public void onMessage(Context context, String message,
                          String customContentString) {
        String messageString = "͸����Ϣ message=\"" + message
                + "\" customContentString=" + customContentString;
        Log.d(TAG, messageString);

        // �Զ������ݻ�ȡ��ʽ��mykey��myvalue��Ӧ͸����Ϣ����ʱ�Զ������������õļ���ֵ
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

        // Demo���½���չʾ���룬Ӧ��������������Լ��Ĵ����߼�
        updateContent(context, messageString);
        loadLocalMSGData(context, message);

		/*
         * Intent intent = new Intent(context, xitonginfoActivity.class);
		 * intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		 * context.startActivity(intent);
		 */

    }

    // ���ر���͸����Ϣ������
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
            // ��ָ���쳣������Ҫ���³�ʼ��
            e.printStackTrace();
            msglist = new ArrayList<MsgBean>();
        }

        tmpBean = gson.fromJson(message, MsgBean.class);
        msglist.add(tmpBean);
        String msgString = gson.toJson(msglist, new TypeToken<List<MsgBean>>() {
        }.getType());
        // ����ϵͳ��Ϣ
        AppConfig.setSharedPreferences(context, "msginfo", msgString);
        // ��ȡδ��ϵͳ��Ϣ����������
        int num = AppConfig.getSharedPreferences(context).getInt("msgnum", 0);
        num++;
        AppConfig.setSharedPreferences(context, "msgnum", num);
        Intent i = new Intent(ACTION_GENERAL_SEND);
        i.putExtra("msgnum", num);
        context.sendBroadcast(i);
        //�Ƿ�����Զ���͸����Ϣ
        if (AppConfig.getSharedPreferences(context).getBoolean(
                "Notification_switch", true)) {
            showNotification(context, tmpBean.getTitle(), tmpBean.getContent());
        }
    }

    /**
     * ��״̬����ʾ֪ͨ
     */
    private void showNotification(Context context, String title, String Text) {
        // ����һ��NotificationManager������
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(android.content.Context.NOTIFICATION_SERVICE);

        // ����Notification�ĸ�������
        Notification notification = new Notification(R.drawable.ic_launcher,
                title, System.currentTimeMillis());
        // FLAG_AUTO_CANCEL ��֪ͨ�ܱ�״̬���������ť�������
        // FLAG_NO_CLEAR ��֪ͨ���ܱ�״̬���������ť�������
        // FLAG_ONGOING_EVENT ֪ͨ��������������
        // FLAG_INSISTENT �Ƿ�һֱ���У���������һֱ���ţ�֪���û���Ӧ
        // notification.flags |= Notification.FLAG_ONGOING_EVENT; //
        // ����֪ͨ�ŵ�֪ͨ����"Ongoing"��"��������"����
        notification.flags |= Notification.FLAG_AUTO_CANCEL; // �����ڵ����֪ͨ���е�"���֪ͨ"�󣬴�֪ͨ�������������FLAG_ONGOING_EVENTһ��ʹ��
        notification.flags |= Notification.FLAG_SHOW_LIGHTS;
        // DEFAULT_ALL ʹ������Ĭ��ֵ�������������𶯣������ȵ�
        // DEFAULT_LIGHTS ʹ��Ĭ��������ʾ
        // DEFAULT_SOUNDS ʹ��Ĭ����ʾ����
        // DEFAULT_VIBRATE ʹ��Ĭ���ֻ��𶯣������<uses-permission
        // android:name="android.permission.VIBRATE" />Ȩ��
        notification.defaults = Notification.DEFAULT_LIGHTS;
        // ����Ч������
        // notification.defaults=Notification.DEFAULT_LIGHTS|Notification.DEFAULT_SOUND;
        notification.ledARGB = Color.BLUE;
        notification.ledOnMS = 5000; // ����ʱ�䣬����

        // ����֪ͨ���¼���Ϣ
        CharSequence contentTitle = title; // ֪ͨ������
        CharSequence contentText = Text; // ֪ͨ������
        Intent notificationIntent = new Intent(context, SystemMsgActivity.class); // �����֪ͨ��Ҫ��ת��Activity
        PendingIntent contentItent = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);
        //6.0����
        notification.setLatestEventInfo(context, contentTitle, contentText,
                contentItent);
        //API16 ��ʼʹ��
//		 Notification noti = new Notification.Builder(context)
//         .setContentTitle(contentTitle)
//         .setContentText(contentText)
//         .setSmallIcon(R.drawable.ic_launcher)
//         .setContentIntent(contentItent)
//         .build();
        // ��Notification���ݸ�NotificationManager���IDΪ0
        notificationManager.notify(0, notification);
    }

    /**
     * ����֪ͨ����ĺ�����
     *
     * @param context             ������
     * @param title               ���͵�֪ͨ�ı���
     * @param description         ���͵�֪ͨ������
     * @param customContentString �Զ������ݣ�Ϊ�ջ���json�ַ���
     */
    @Override
    public void onNotificationClicked(Context context, String title,
                                      String description, String customContentString) {
        String notifyString = "֪ͨ��� title=\"" + title + "\" description=\""
                + description + "\" customContent=" + customContentString;
        Log.d(TAG, notifyString);

        // �Զ������ݻ�ȡ��ʽ��mykey��myvalue��Ӧ֪ͨ����ʱ�Զ������������õļ���ֵ
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

        // Demo���½���չʾ���룬Ӧ��������������Լ��Ĵ����߼�
        updateContent(context, notifyString);
    }

    /**
     * ����֪ͨ����ĺ�����
     *
     * @param context             ������
     * @param title               ���͵�֪ͨ�ı���
     * @param description         ���͵�֪ͨ������
     * @param customContentString �Զ������ݣ�Ϊ�ջ���json�ַ���
     */

    @Override
    public void onNotificationArrived(Context context, String title,
                                      String description, String customContentString) {

        String notifyString = "onNotificationArrived  title=\"" + title
                + "\" description=\"" + description + "\" customContent="
                + customContentString;
        Log.d(TAG, notifyString);

        // �Զ������ݻ�ȡ��ʽ��mykey��myvalue��Ӧ֪ͨ����ʱ�Զ������������õļ���ֵ
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
        // Demo���½���չʾ���룬Ӧ��������������Լ��Ĵ����߼�
        // ����ԅ��� onNotificationClicked�е���ʾ���Զ������ݻ�ȡ����ֵ
        updateContent(context, notifyString);
    }

    /**
     * setTags() �Ļص�������
     *
     * @param context     ������
     * @param errorCode   �����롣0��ʾĳЩtag�Ѿ����óɹ�����0��ʾ����tag�����þ�ʧ�ܡ�
     * @param successTags ���óɹ���tag
     * @param failTags    ����ʧ�ܵ�tag
     * @param requestId   ������������͵������id
     */
    @Override
    public void onSetTags(Context context, int errorCode,
                          List<String> sucessTags, List<String> failTags, String requestId) {
        String responseString = "onSetTags errorCode=" + errorCode
                + " sucessTags=" + sucessTags + " failTags=" + failTags
                + " requestId=" + requestId;
        Log.d(TAG, responseString);

        // Demo���½���չʾ���룬Ӧ��������������Լ��Ĵ����߼�
        updateContent(context, responseString);
    }

    /**
     * delTags() �Ļص�������
     *
     * @param context     ������
     * @param errorCode   �����롣0��ʾĳЩtag�Ѿ�ɾ���ɹ�����0��ʾ����tag��ɾ��ʧ�ܡ�
     * @param successTags �ɹ�ɾ����tag
     * @param failTags    ɾ��ʧ�ܵ�tag
     * @param requestId   ������������͵������id
     */
    @Override
    public void onDelTags(Context context, int errorCode,
                          List<String> sucessTags, List<String> failTags, String requestId) {
        String responseString = "onDelTags errorCode=" + errorCode
                + " sucessTags=" + sucessTags + " failTags=" + failTags
                + " requestId=" + requestId;
        Log.d(TAG, responseString);

        // Demo���½���չʾ���룬Ӧ��������������Լ��Ĵ����߼�
        updateContent(context, responseString);
    }

    /**
     * listTags() �Ļص�������
     *
     * @param context   ������
     * @param errorCode �����롣0��ʾ�о�tag�ɹ�����0��ʾʧ�ܡ�
     * @param tags      ��ǰӦ�����õ�����tag��
     * @param requestId ������������͵������id
     */
    @Override
    public void onListTags(Context context, int errorCode, List<String> tags,
                           String requestId) {
        String responseString = "onListTags errorCode=" + errorCode + " tags="
                + tags;
        Log.d(TAG, responseString);

        // Demo���½���չʾ���룬Ӧ��������������Լ��Ĵ����߼�
        updateContent(context, responseString);
    }

    /**
     * PushManager.stopWork() �Ļص�������
     *
     * @param context   ������
     * @param errorCode �����롣0��ʾ�������ͽ�󶨳ɹ�����0��ʾʧ�ܡ�
     * @param requestId ������������͵������id
     */
    @Override
    public void onUnbind(Context context, int errorCode, String requestId) {
        String responseString = "onUnbind errorCode=" + errorCode
                + " requestId = " + requestId;
        Log.d(TAG, responseString);

        if (errorCode == 0) {
            // ��󶨳ɹ�
        }
        // Demo���½���չʾ���룬Ӧ��������������Լ��Ĵ����߼�
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
