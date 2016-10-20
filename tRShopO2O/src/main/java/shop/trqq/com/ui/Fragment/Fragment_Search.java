package shop.trqq.com.ui.Fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.systembar.SystemBarHelper;
import com.google.gson.Gson;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.vlonjatg.progressactivity.ProgressActivity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView.OnTagClickListener;
import shop.trqq.com.R;
import shop.trqq.com.UserManager;
import shop.trqq.com.bean.VoiceBean;
import shop.trqq.com.search.KeywordsFlow;
import shop.trqq.com.ui.Base.UIHelper;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.JsonParser;
import shop.trqq.com.util.YkLog;

public class Fragment_Search extends Fragment {
    private static String TAG = Fragment_Search.class.getSimpleName();

    private View rootView;// ����Fragment view
    private Context mContext;
    // ���ؽ���Activity
    private ProgressActivity progressActivity;
    // ����������
    private EditText mHeadEditText;
    private TextView mHeadRightText;
    private List<String> keywordsList = new ArrayList<String>();
    // ¡��,����,��֮��,����,Ůװ,��װ,��װ
    private String[] keywords = {"¡��", "����", "��֮��", "����", "Ůװ",//
            "��װ", "��װ"};
//    private KeywordsFlow keywordsFlow;

    private TagContainerLayout mTagContainerLayout,search_layout;
    private LinearLayout search_history;
    List<String> list1;
    private TextView search_voice;
    // ������д����
    private SpeechRecognizer mIat;
    // ������дUI
    private RecognizerDialog mIatDialog;
    // ��HashMap�洢��д���
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    private SharedPreferences mSharedPreferences;
    // ��������
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    // private Button btnIn, btnOut;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        rootView = inflater.inflate(R.layout.fragment_search, container, false);
        initTitleBarView();
        /*
		 * btnIn = (Button) rootView.findViewById(R.id.button1); btnOut =
		 * (Button) rootView.findViewById(R.id.button2);
		 */
        progressActivity = (ProgressActivity) rootView
                .findViewById(R.id.search_progress);
        mTagContainerLayout = (TagContainerLayout) rootView
                .findViewById(R.id.tagcontainerLayout);
        search_layout= (TagContainerLayout) rootView
                .findViewById(R.id.search_layout);
        search_voice= (TextView) rootView
                .findViewById(R.id.search_voice);
        search_history= (LinearLayout) rootView
                .findViewById(R.id.search_history);
        list1 = new ArrayList<String>();
        list1 = UserManager.getSearchList();
        if(list1.size()==0){
            search_history.setVisibility(View.GONE);
        }
        mTagContainerLayout.setOnTagClickListener(new OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                YkLog.t(text);
                UIHelper.showShop(mContext, text, "", "", "");
            }

            @Override
            public void onTagLongClick(final int position, String text) {
                YkLog.t(text);
                AlertDialog dialog = new AlertDialog.Builder(mContext)
                        .setMessage("�㽫��ɾ����" + text + "��������¼")
                        .setPositiveButton("ɾ��", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mTagContainerLayout.removeTag(position);
                                list1.remove(position);
                                UserManager.setSearchList(mContext, list1);
                            }
                        })
                        .setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                dialog.show();
            }
        });
        mTagContainerLayout.setTags(list1);
        //��������
        search_layout.setOnTagClickListener(new OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                YkLog.t(text);
                UIHelper.showShop(mContext, text, "", "", "");
            }

            @Override
            public void onTagLongClick(int position, String text) {

            }
        });
        // ��ʼ��ʶ����UIʶ�����
        // ʹ��SpeechRecognizer���󣬿ɸ��ݻص���Ϣ�Զ�����棻
        mIat = SpeechRecognizer.createRecognizer(getActivity(), mInitListener);

        // ��ʼ����дDialog�����ֻʹ����UI��д���ܣ����贴��SpeechRecognizer
        // ʹ��UI��д���ܣ������sdk�ļ�Ŀ¼�µ�notice.txt,���ò����ļ���ͼƬ��Դ
        // 1.ʹ�ô�UI�ӿ�ʱ���뽫assets���ļ���������Ŀ�У�
        mIatDialog = new RecognizerDialog(getActivity(), mInitListener);
        search_voice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setParam();
                mIatDialog.setListener(mRecognizerDialogListener);
                mIatDialog.show();
//                startVoice();
            }
        });
		/*
		 * btnIn.setOnClickListener(buttonListener);
		 * btnOut.setOnClickListener(buttonListener);
		 */
/*        keywordsFlow = (KeywordsFlow) rootView.findViewById(R.id.frameLayout1);
        keywordsFlow.setDuration(800l);
        keywordsFlow.setOnItemClickListener(buttonListener);*/
        // ���
        loadOnlinekeyword();
        // Bugly test
        // CrashReport.testJavaCrash();
        return rootView;
    }

    /**
     * ��ʼ����������ͼ
     */
    private void initTitleBarView() {
        mHeadRightText = (TextView) rootView
                .findViewById(R.id.head_right_textView);
        mHeadEditText = (EditText) rootView.findViewById(R.id.head_search_edit);
        mHeadEditText.setVisibility(View.VISIBLE);
        mHeadRightText.setText("����");
        mHeadRightText.setVisibility(View.VISIBLE);
        mHeadRightText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String str = mHeadEditText.getText().toString();
                for (int i = 0; i < list1.size(); i++) {
                    if (list1.get(i).equals(str)) {
                        list1.remove(i);
                        mTagContainerLayout.removeTag(i);
                    }
                }
				if (!"".equals(str.trim())) {
                    list1.add(0, str);
                    mTagContainerLayout.addTag(str, 0);
                }
                //���²���
//                keywordsFlow.go2Show(KeywordsFlow.ANIMATION_OUT);
                if (list1.size() == 11) {
                    list1.remove(10);
                    mTagContainerLayout.removeTag(10);
                }
                if(list1.size()>0){
                    search_history.setVisibility(View.VISIBLE);
                }
                UserManager.setSearchList(mContext, list1);
                UIHelper.showShop(mContext, mHeadEditText.getText().toString(),
                        "", "", "");
            }
        });
        LinearLayout tLinearLayout=(LinearLayout) rootView
                .findViewById(R.id.header_relativelayout);
        SystemBarHelper.immersiveStatusBar(getActivity(),0);
        SystemBarHelper.setHeightAndPadding(getActivity(), tLinearLayout);
    }

    /**
     * ��������ȴ�����
     */
    private void feedKeywordsFlow(KeywordsFlow keywordsFlow, List<String> list) {

        List<String> templist = new ArrayList<String>();
        templist.addAll(list);
        Random random = new Random();
        for (int i = 0; i < KeywordsFlow.MAX; i++) {
            int ran = random.nextInt(templist.size());
            String tmp = templist.get(ran);
            if (templist.size() > 1) {
                templist.remove(ran);
            }
            keywordsFlow.feedKeyword(tmp);
        }

    }


    /**
     * ���������ȴ�
     */
    private void loadOnlinekeyword() {
        progressActivity.showLoading();
        RequestParams requestParams = new RequestParams();
        HttpUtil.get(HttpUtil.HOST + "?act=search&op=search", requestParams,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          byte[] responseBody) {
                        try {
                            String jsonString = new String(responseBody);
                            keywordsList = new ArrayList<String>();
                            jsonString = jsonString.substring(0,
                                    jsonString.lastIndexOf(","));
                            YkLog.t("search", jsonString);
                            String a[] = jsonString.split("=>");
                            String keyword[] = a[1].replace("'", "").split(",");
                            // keywords = keyword;
                            for (int i = 0; i < keyword.length; i++) {
                                YkLog.t("keyword", keyword[i]);
                                keywordsList.add(keyword[i]);
                            }
                            search_layout.setTags(keywordsList  );
/*                            feedKeywordsFlow(keywordsFlow, keywordsList);
                            keywordsFlow.go2Show(KeywordsFlow.ANIMATION_IN);*/
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (keywordsList.size() > 0) {
                            progressActivity.showContent();
                        } else {
                            Drawable emptyDrawable = ContextCompat.getDrawable(mContext, R.drawable.ic_empty);
                            progressActivity.showEmpty(emptyDrawable,
                                    "������ʱû����", "");
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {
                        Drawable errorDrawable =ContextCompat.getDrawable(mContext, R.drawable.wifi_off);
                        progressActivity.showError(errorDrawable, "���翪��С��",
                                "���Ӳ������磬��ȷ��һ���������翪�أ����߷�����������æ�����Ժ�����", "��������",
                                new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // TODO Auto-generated method stub
                                        progressActivity.showLoading();
                                        if (!keywordsList.isEmpty()) {
                                            keywordsList.clear();
                                        }
                                        loadOnlinekeyword();
                                    }
                                });
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();

                    }
                });

    }

    private View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (v instanceof TextView) {
                String keyword = ((TextView) v).getText().toString();
                // ToastUtils.showMessage(getActivity(), keyword);
                UIHelper.showShop(getActivity(), keyword, "", "", "");
            }
        }
    };

    /**
     * ��ʼ����������
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            YkLog.t(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
//                showTip("��ʼ��ʧ�ܣ������룺" + code);
            }
        }
    };
    /**
     * ��дUI������
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            YkLog.t("RecognizerDialogListener", results.getResultString());
            printResult(results);
//            String result=parseData(results.getResultString());
//            mHeadEditText.setText(result);
        }

        /**
         * ʶ��ص�����.
         */
        public void onError(SpeechError error) {
//            ToastUtils.showMessage(getActivity(),"ʶ�����");
        }

    };
    /**
     * ��ʼ����ʶ��
     *
     */
    public void startVoice() {

        // 2.������д����
        mIatDialog.setParameter(SpeechConstant.DOMAIN, "iat");
        mIatDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mIatDialog.setParameter(SpeechConstant.ACCENT, "mandarin");

        mIatDialog.setListener(mRecognizerDialogListener);

        mIatDialog.show();
    }

    /**
     * ������������
     *
     * @param resultString
     */
    protected String parseData(String resultString) {
        Gson gson = new Gson();
        VoiceBean bean = gson.fromJson(resultString, VoiceBean.class);
        ArrayList<VoiceBean.WSBean> ws = bean.ws;

        StringBuffer sb = new StringBuffer();
        for (VoiceBean.WSBean wsBean : ws) {
            String text = wsBean.cw.get(0).w;
            sb.append(text);
        }
        return sb.toString();
    }

    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // ��ȡjson����е�sn�ֶ�
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }
        mHeadEditText.setText(resultBuffer.toString());
        mHeadEditText.setSelection(resultBuffer.length());
    }
    /**
     * ��������
     *
     * @return
     */
    public void setParam() {
        // ��ղ���
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // ������д����
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // ���÷��ؽ����ʽ
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

            // ��������
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // ������������
//            mIat.setParameter(SpeechConstant.ACCENT, lag);


        // ��������ǰ�˵�:������ʱʱ�䣬���û��೤ʱ�䲻˵��������ʱ����
        mIat.setParameter(SpeechConstant.VAD_BOS,"4000");

        // ����������˵�:��˵㾲�����ʱ�䣬���û�ֹͣ˵���೤ʱ���ڼ���Ϊ�������룬 �Զ�ֹͣ¼��
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");

        // ���ñ�����,����Ϊ"0"���ؽ���ޱ��,����Ϊ"1"���ؽ���б��
        mIat.setParameter(SpeechConstant.ASR_PTT, "0");

        // ������Ƶ����·����������Ƶ��ʽ֧��pcm��wav������·��Ϊsd����ע��WRITE_EXTERNAL_STORAGEȨ��
        // ע��AUDIO_FORMAT���������Ҫ���°汾������Ч
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/iat.wav");
    }
}