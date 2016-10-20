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
        YkLog.i("����get_state:", name);
    }

    @JavascriptInterface
    public void get_msg(String name) {
        YkLog.i("����get_msg:", name);
        setmsgBean(new JSONObject(name));
        Intent i = new Intent(ACTION_GENERAL_SEND);
        i.putExtra("get_msg", name);
        mContxt.sendBroadcast(i);
    }

    @JavascriptInterface
    public void del_msg(String name) {
        YkLog.i("����del_msg:", name);
    }

    @JavascriptInterface
    public void connect(String name) {
        YkLog.i("����connect:", name);
    }

    // ȡgetmsgBean
    @JavascriptInterface
    private void setmsgBean(JSONObject jsonObject) {
        int i = 0;
        // ����
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
				 * User userbean=bean.getUser(); userbean.save(); YkLog.t("��Ϣ",
				 * bean.getUser().getAvatar());
				 */
                i++;
                int maxid = Integer.parseInt(id);
                JSONObject u_list = new JSONObject();
                u_list.put("max_id", maxid);
                u_list.put("f_id", bean.getF_id());
                final String u_listStr = u_list.toString();
                // new WebView(context);��ִ�У�Ҫ��UI�߳�findViewById
                webView.post(new Runnable() {
                    public void run() {
                        webView.loadUrl("javascript:node_del_msg(" + u_listStr
                                + ")");
                    }
                });


                // �����ǰActivity��IMMsgActivity
                if ("IMMsgActivity".equals(AppManager.getAppManager()
                        .currentActivityString())) {
                    mIMMsgActivity = (IMMsgActivity) AppManager.getAppManager()
                            .currentActivity();
                    // �Ƿ����ں�F_id���죬�ǾͲ�����֪ͨ
                    // YkLog.t(mIMMsgActivity.getT_id()+"|", list.get(list.size() -
                    // 1).getF_id()+"");
                    if (list.get(list.size() - 1).getF_id() == Integer
                            .parseInt(mIMMsgActivity.getT_id())) {
                        isUpdate = false;
                    }
                }//�Ƿ����
                if (isUpdate) {
                    List<UserBean> mUserBeanList = DataSupport.where(
                            "u_id = ?", "" + bean.getF_id()).find(UserBean.class);

                    UserBean mUserBean = new UserBean();
                    if (mUserBeanList.size() == 0) {
                        //����û�и��û����ɱ�
                        mUserBean.setU_id(bean.getF_id());
                        mUserBean.setUnreadnum(1);
                        mUserBean.save();
                    } else {
                        //�������ݿ�
                        mUserBean.setUnreadnum(mUserBeanList.get(0).getUnreadnum() + 1);
                        mUserBean.updateAll("u_id = ?", "" + bean.getF_id());
                    }
					/*Intent mIntent = new Intent(ACTION_GENERAL_SEND_USER);
					mIntent.putExtra("u_id", bean.getF_id());
					mIntent.putExtra("unReadNum", mUserBean.getUnreadnum());
					mContxt.sendBroadcast(mIntent);*/
                    mUserList.add(mUserBean);
                    // �����¼����ں�̨�̷߳����¼�
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
            // �����ǰActivity��IMMsgActivity
            if ("IMMsgActivity".equals(AppManager.getAppManager()
                    .currentActivityString())) {
                mIMMsgActivity = (IMMsgActivity) AppManager.getAppManager()
                        .currentActivity();
                // �Ƿ����ں�F_id���죬�ǾͲ�����֪ͨ
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
     * ��״̬����ʾ֪ͨ
     */
    @SuppressWarnings("deprecation")
    private void showNotification(Context context, getmsgBean bean) {
        // ����һ��NotificationManager������
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(android.content.Context.NOTIFICATION_SERVICE);

        // ����Notification�ĸ�������
        Notification notification = new Notification(R.drawable.ic_launcher,
                bean.getF_name(), System.currentTimeMillis());
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
        notification.defaults = Notification.DEFAULT_ALL;
        // ����Ч������
        // notification.defaults=Notification.DEFAULT_LIGHTS|Notification.DEFAULT_SOUND;
        notification.ledARGB = Color.BLUE;
        notification.ledOnMS = 5000; // ����ʱ�䣬����

        // ����֪ͨ���¼���Ϣ
        CharSequence contentTitle = "�յ���" + bean.getF_name() + "������Ϣ"; // ֪ͨ������
        CharSequence contentText = bean.getT_msg(); // ֪ͨ������

        Intent notificationIntent = new Intent(context, IMMsgActivity.class); // �����֪ͨ��Ҫ��ת��Activity
        String t_id = String.valueOf(bean.getF_id());
        notificationIntent.putExtra("t_id", t_id);
        notificationIntent.putExtra("t_name", bean.getF_name());
        // �����ͼһ���������ͻ��Զ����ջ���Ļ������˵����һ�����򿪵Ļ�ᱻ�ս�������Ǿ�ʵ����û��������ͬ�Ļ��ͬʱ��
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
        // ��Notification���ݸ�NotificationManager���IDΪ1
        notificationManager.notify(1, notification);
    }

}
