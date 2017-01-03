package com.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;
import shop.trqq.com.supermarket.utils.SystemUtils;
import shop.trqq.com.ui.MainTabActivity;
import shop.trqq.com.ui.OrderActivity;

/**
 * �Զ��������
 *
 * ������������ Receiver����
 * 1) Ĭ���û����������
 * 2) ���ղ����Զ�����Ϣ
 */
public class JPushReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		Log.d(TAG, "[JPushReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.d(TAG, "[JPushReceiver] ����Registration Id : " + regId);
			//send the Registration Id to your server...
			// �� registration Id  ����������
		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
			Log.d(TAG, "[JPushReceiver] ���յ������������Զ�����: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
			processCustomMessage(context, bundle);
		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
			Log.d(TAG, "[JPushReceiver] ���յ�����������֪ͨ");
			int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			Log.d(TAG, "[JPushReceiver] ���յ�����������֪ͨ��ID: " + notifactionId);

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
			Log.d(TAG, "[JPushReceiver] �û��������֪ͨ");
			int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);

			if (SystemUtils.isPackageRunning(context, "shop.trqq.com")){
				//�ȴ������棬Ȼ��򿪶���ҳ���Activity
				Intent i = new Intent(context, OrderActivity.class);
				// ֪ͨ�ͻ�ȷ���ջ�
				i.putExtra("filter","");
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(i);
			}else {
				//�ȴ������棬Ȼ��򿪶���ҳ���Activity
				Intent i = new Intent(context, MainTabActivity.class);
				// ֪ͨ�ͻ�ȷ���ջ�
				i.putExtra("type","1");
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(i);
			}

			JPushInterface.clearNotificationById(context, notifactionId);
		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
			Log.d(TAG, "[JPushReceiver] �û��յ���RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
			//��������� JPushInterface.EXTRA_EXTRA �����ݴ�����룬������µ�Activity�� ��һ����ҳ��..

		} else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
			boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
			Log.w(TAG, "[JPushReceiver]" + intent.getAction() +" connected state change to "+connected);
		} else {
			Log.d(TAG, "[JPushReceiver] Unhandled intent - " + intent.getAction());
		}
	}

	// ��ӡ���е� intent extra ����
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
					Log.i(TAG, "This message has no Extra data");
					continue;
				}
				try {
					JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
					Iterator<String> it =  json.keys();

					while (it.hasNext()) {
						String myKey = it.next().toString();
						sb.append("\nkey:" + key + ", value: [" +
								myKey + " - " +json.optString(myKey) + "]");
					}
				} catch (JSONException e) {
					Log.e(TAG, "Get message extra JSON error!");
				}

			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

	//send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle) {

	}
}
