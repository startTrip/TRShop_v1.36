package shop.trqq.com.ui;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vlonjatg.progressactivity.ProgressActivity;

import java.util.ArrayList;
import java.util.List;

import shop.trqq.com.AppConfig;
import shop.trqq.com.R;
import shop.trqq.com.adapter.MsgAdapter;
import shop.trqq.com.bean.MsgBean;
import shop.trqq.com.ui.Base.BaseActivity;
import shop.trqq.com.ui.Base.UIHelper;
import shop.trqq.com.util.YkLog;

/**
 * ϵͳ��Ϣ����
 */
public class SystemMsgActivity extends BaseActivity {
    private ListView msgListView;
    private TextView mHeadTitleTextView;
    private TextView mHeadRightTextView;
    /*	private Button send;
        private Button send2;
        private Button send3;*/
    // ��Ѷ���ش�������
    private ProgressActivity progressActivity;
    private MsgAdapter adapter;
    private Context mContext;
    private Gson gson;
    private List<MsgBean> msgList = new ArrayList<MsgBean>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_systemmsg);
        mContext = this;
        clearNotification(mContext);
        AppConfig.setSharedPreferences(mContext, "msgnum", 0);
        gson = new Gson();
        initTitleBarView();
        progressActivity = (ProgressActivity) findViewById(R.id.sysmsg_progress);
        progressActivity.showLoading();
        msgListView = (ListView) findViewById(R.id.msg_list_view);
        adapter = new MsgAdapter(mContext);
        msgListView.setAdapter(adapter);
        // initMsg();
        loadLocalMSGData();

        // inputText=(EditText)findViewById(R.id.input_text);
        /*send = (Button) findViewById(R.id.send);
		send2 = (Button) findViewById(R.id.button1);
		send3 = (Button) findViewById(R.id.button2);*/
/*
		send.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				AppConfig.setSharedPreferences(mContext, "msginfo", "");
				msgList = new ArrayList<MsgBean>();
				adapter.setData(msgList);
				adapter.notifyDataSetChanged();
				
				 * String content=inputText.getText().toString();
				 * if(!"".equals(content)){ Msg msg=new Msg(content,
				 * Msg.TYPE_SENT); msgList.add(msg);
				 * adapter.notifyDataSetChanged();
				 * msgListView.setSelection(msgList.size());
				 * inputText.setText(""); }
				 
			}
		});

		send2.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// usePopup(send2);
				openRichMediaList();
			}
		});
		send3.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				useDialog(send3);
			}
		});*/
    }

    /**
     * ��ʼ����������ͼ
     */
    private void initTitleBarView() {
        mHeadTitleTextView = (TextView) findViewById(R.id.head_title_textView);
        mHeadRightTextView = (TextView) findViewById(R.id.head_right_textView);
        mHeadTitleTextView.setText("ϵͳ��Ϣ");
        mHeadRightTextView.setText("���");
        mHeadRightTextView.setVisibility(View.VISIBLE);
        mHeadRightTextView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                AppConfig.setSharedPreferences(mContext, "msginfo", "");
                Drawable emptyDrawable = getResources().getDrawable(
                        R.drawable.ic_empty);
                progressActivity.showEmpty(emptyDrawable, "��ʱû��������Ϣ", "");
                //AppConfig.setSharedPreferences(mContext, "historyList", "");
            }
        });
    }

    private void usePopup(final Button anchor) {
        // �ο��� http://www.cnblogs.com/sw926/p/3230659.html
        LayoutInflater mInflater = LayoutInflater.from(this);
        ViewGroup rootView = (ViewGroup) mInflater.inflate(R.layout.menu, null);
        rootView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        final PopupWindow popup = new PopupWindow(this);
        // setContentView֮ǰһ��Ҫ���ÿ�ߣ�������ʾ
        popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        // ȥ��Ĭ�ϵı���
        popup.setBackgroundDrawable(new ColorDrawable(getResources().getColor(
                android.R.color.transparent)));
        popup.setContentView(rootView);
        // ����հ״���ʱ��PopupWindow����ʧ
        popup.setTouchable(true);
        popup.setOutsideTouchable(true);
        // ���focusableΪfalse����һ��Activity����һ��PopupWindow�������ؼ�������PopupWindowû�н��㣬��ֱ���˳�Activity�����focusableΪtrue��PopupWindow���������еĴ���������������PopupWindows����
        popup.setFocusable(true);
        // ���㵯��λ��
        int[] xy = calcPopupXY(rootView, anchor);
        // �����κ�gravity��ʹ�þ��Ե�(x,y)����
        popup.showAtLocation((View) anchor.getParent(), Gravity.NO_GRAVITY,
                xy[0], xy[1]);
    }

    private void useDialog(final Button anchor) {
        LayoutInflater mInflater = LayoutInflater.from(this);
        ViewGroup rootView = (ViewGroup) mInflater.inflate(R.layout.menu, null);
        rootView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        Dialog dialog = new Dialog(this);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        // ȥ��Ĭ�ϵı���,��������������
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(
                        android.R.color.transparent)));
        // dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // http://stackoverflow.com/questions/12348405/dialog-is-bigger-than-expected-when-using-relativelayout
        // dialogĬ�϶�����title��
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ�����⣬�����Ӱ��߶ȼ��㣬һ��Ҫ��setContentView֮ǰ���ã�����������һ������theme�Ĺ��캯����Ŀ����
        dialog.setContentView(rootView);

        // ���㵯��λ��
        int[] xy = calcPopupXY(rootView, anchor);
        // gravity��Ĭ��ֵΪGravity.CENTER,��Gravity.CENTER_HORIZONTAL |
        // Gravity.CENTER_VERTICAL.
        // �ο�: http://www.cnblogs.com/angeldevil/archive/2012/03/31/2426242.html
        dialog.getWindow().setGravity(Gravity.LEFT | Gravity.TOP);
        params.x = xy[0];
        params.y = xy[1];
        TextView suggest = (TextView) rootView.findViewById(R.id.menu_suggest);
        TextView about = (TextView) rootView.findViewById(R.id.menu_about);
        suggest.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                UIHelper.showSuggest(mContext);
            }
        });
        about.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                UIHelper.showAbout(mContext);
            }
        });
        dialog.show();
    }

    private int[] calcPopupXY(View rootView, View anchor) {
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        rootView.measure(w, h);
        int popupWidth = rootView.getMeasuredWidth();
        int popupHeight = rootView.getMeasuredHeight();
        Rect anchorRect = getViewAbsoluteLocation(anchor);
        int x = anchorRect.left + (anchorRect.right - anchorRect.left) / 2
                - popupWidth / 2;
        int y = anchorRect.top - popupHeight;
        return new int[]{x, y};
    }

    public static Rect getViewAbsoluteLocation(View view) {
        if (view == null) {
            return new Rect();
        }
        // ��ȡView�������Ļ������
        int[] location = new int[2];
        view.getLocationOnScreen(location);// ���ǻ�ȡ�������Ļ�ľ������꣬��view.getLocationInWindow(location);
        // �ǻ�ȡwindow�ϵ�������꣬������ֻ��һ��window�����ߵȼ�
        // ��ȡView�Ŀ��
        int width = view.getMeasuredWidth();
        int height = view.getMeasuredHeight();
        // ��ȡView��Rect
        Rect rect = new Rect();
        rect.left = location[0];
        rect.top = location[1];
        rect.right = rect.left + width;
        rect.bottom = rect.top + height;
        return rect;
    }

    private void initMsg() {
        MsgBean msg1 = new MsgBean();
        msg1.setContent("Hi Peter");
        msg1.setType(MsgBean.TYPE_RECEIVED);
        msgList.add(msg1);
        MsgBean msg2 = new MsgBean();
        msg2.setContent("Hello world");
        msg2.setType(MsgBean.TYPE_SENT);
        msgList.add(msg2);
        MsgBean msg3 = new MsgBean();
        msg3.setContent("��������Ļ���");
        msg3.setType(MsgBean.TYPE_RECEIVED);
        msgList.add(msg3);
        adapter.setData(msgList);
        adapter.notifyDataSetChanged();
    }

    private void loadLocalMSGData() {
        try {

            String json = AppConfig.getSharedPreferences(mContext).getString(
                    "msginfo", "");

            if (json.equals("")) {
                YkLog.t("SystemMsgActivity", "��ʱû��ϵͳ��Ϣ");
                Drawable emptyDrawable = getResources().getDrawable(
                        R.drawable.ic_empty);
                progressActivity.showEmpty(emptyDrawable, "��ʱû��������Ϣ", "");
					/*MsgBean oneMsg=new MsgBean();
					oneMsg.setTitle("������������ɳ���� ����ˮ�� �ֻ���Ϯ 55Ԫ/��� ��Ʒ");
					SimpleDateFormat formatter=new SimpleDateFormat("yyyy��MM��dd�� HH:mm");       
					Date curDate=new Date(System.currentTimeMillis());//��ȡ��ǰʱ��       
					String date=formatter.format(curDate); 
					oneMsg.setDate(date);
					oneMsg.setContent("������������ɳ���� Ǭ¡�ʵ۶�˵�ó�");
					oneMsg.setImage("http://shop.trqq.com/data/upload/mobile/special/s0/s0_04984084248124233.jpg");
					oneMsg.setType(1);
					oneMsg.setClicktype("goods");
					oneMsg.setClicktypedata("2689");
					msgList.add(oneMsg);		
					String msgString = gson.toJson(msgList, new TypeToken<List<MsgBean>>() {
					}.getType());
					// ����ϵͳ��Ϣ
					AppConfig.setSharedPreferences(mContext, "msginfo", msgString);*/
            } else {
                progressActivity.showContent();
                msgList = gson.fromJson(json, new TypeToken<List<MsgBean>>() {
                }.getType());

                YkLog.t("test:", msgList.get(0).getContent() + "|"
                        + msgList.get(0).getType());
                adapter.setData(msgList);
                adapter.notifyDataSetChanged();
                msgListView.setSelection(adapter.getCount());
            }
        } catch (Exception e) {
            // ��ָ���쳣������Ҫ���³�ʼ��
            e.printStackTrace();
            msgList = new ArrayList<MsgBean>();
        }
    }

    // ɾ��֪ͨ
    private void clearNotification(Context context) {
        // ������ɾ��֮ǰ���Ƕ����֪ͨ
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
    }

    // �򿪸�ý���б����
    private void openRichMediaList() {
        // Push: �򿪸�ý����Ϣ�б�
        Intent sendIntent = new Intent();
        sendIntent.setClassName(getBaseContext(),
                "com.baidu.android.pushservice.richmedia.MediaListActivity");
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        SystemMsgActivity.this.startActivity(sendIntent);
    }
}