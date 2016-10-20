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

    private View rootView;// 缓存Fragment view
    private Context mContext;
    // 加载进度Activity
    private ProgressActivity progressActivity;
    // 标题栏标题
    private EditText mHeadEditText;
    private TextView mHeadRightText;
    private List<String> keywordsList = new ArrayList<String>();
    // 隆富,内衣,孕之彩,纳米,女装,秋装,冬装
    private String[] keywords = {"隆富", "内衣", "孕之彩", "纳米", "女装",//
            "秋装", "冬装"};
//    private KeywordsFlow keywordsFlow;

    private TagContainerLayout mTagContainerLayout,search_layout;
    private LinearLayout search_history;
    List<String> list1;
    private TextView search_voice;
    // 语音听写对象
    private SpeechRecognizer mIat;
    // 语音听写UI
    private RecognizerDialog mIatDialog;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    private SharedPreferences mSharedPreferences;
    // 引擎类型
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
                        .setMessage("你将会删除“" + text + "”搜索记录")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mTagContainerLayout.removeTag(position);
                                list1.remove(position);
                                UserManager.setSearchList(mContext, list1);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
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
        //热门搜索
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
        // 初始化识别无UI识别对象
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(getActivity(), mInitListener);

        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
        // 1.使用带UI接口时，请将assets下文件拷贝到项目中；
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
        // 添加
        loadOnlinekeyword();
        // Bugly test
        // CrashReport.testJavaCrash();
        return rootView;
    }

    /**
     * 初始化标题栏视图
     */
    private void initTitleBarView() {
        mHeadRightText = (TextView) rootView
                .findViewById(R.id.head_right_textView);
        mHeadEditText = (EditText) rootView.findViewById(R.id.head_search_edit);
        mHeadEditText.setVisibility(View.VISIBLE);
        mHeadRightText.setText("搜索");
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
                //重新布局
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
     * 随机生成热词搜索
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
     * 加载网络热词
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
                                    "搜索暂时没数据", "");
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {
                        Drawable errorDrawable =ContextCompat.getDrawable(mContext, R.drawable.wifi_off);
                        progressActivity.showError(errorDrawable, "网络开了小差",
                                "连接不上网络，请确认一下您的网络开关，或者服务器网络正忙，请稍后再试", "重新连接",
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
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            YkLog.t(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
//                showTip("初始化失败，错误码：" + code);
            }
        }
    };
    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            YkLog.t("RecognizerDialogListener", results.getResultString());
            printResult(results);
//            String result=parseData(results.getResultString());
//            mHeadEditText.setText(result);
        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
//            ToastUtils.showMessage(getActivity(),"识别错误");
        }

    };
    /**
     * 开始语音识别
     *
     */
    public void startVoice() {

        // 2.设置听写参数
        mIatDialog.setParameter(SpeechConstant.DOMAIN, "iat");
        mIatDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mIatDialog.setParameter(SpeechConstant.ACCENT, "mandarin");

        mIatDialog.setListener(mRecognizerDialogListener);

        mIatDialog.show();
    }

    /**
     * 解析语音数据
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
        // 读取json结果中的sn字段
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
     * 参数设置
     *
     * @return
     */
    public void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
//            mIat.setParameter(SpeechConstant.ACCENT, lag);


        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS,"4000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "0");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/iat.wav");
    }
}