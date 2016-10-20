package shop.trqq.com.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import shop.trqq.com.AppContext;
import shop.trqq.com.R;
import shop.trqq.com.UserManager;
import shop.trqq.com.ui.Base.BaseActivity;
import shop.trqq.com.ui.Base.UIHelper;
import shop.trqq.com.util.DataCleanManager;
import shop.trqq.com.util.FileUtil;
import shop.trqq.com.util.ToastUtils;
import shop.trqq.com.util.UpdateManager;
import shop.trqq.com.util.YkLog;

/**
 * 设置界面
 *
 * @author Weiss
 * @version 1.0
 * @date 2016-01-15
 */
public class SettingsActivity extends BaseActivity {

    private static final String TAG = "SettingsActivity";
    // 全局Context
    private Context mContext;

    // 标题栏标题
    private TextView mHeadTitleTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        YkLog.i(TAG, "onCreate方法");
        setContentView(R.layout.activity_settings);
        initView();
        // Display the fragment as the main fragment.
        getFragmentManager().beginTransaction().replace(R.id.fragment,
                new DataSyncPreferenceFragment()).commitAllowingStateLoss();
        mContext = this;
    }

    /**
     * 初始化视图
     */
    private void initView() {
        YkLog.i(TAG, "初始化视图");
        initTitleBarView();

    }

    /**
     * 初始化标题栏视图
     */
    private void initTitleBarView() {
        YkLog.i(TAG, "初始化标题栏视图");
        mHeadTitleTextView = (TextView) findViewById(R.id.head_title_textView);
        mHeadTitleTextView.setText(R.string.settings);
    }

    /**
     * This fragment shows data and sync preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class DataSyncPreferenceFragment extends PreferenceFragment {

        private SwitchPreference mSwitchPreference;
        private Preference searchcachePreference;
        private Preference cachePreference;
        private Preference aboutPreference;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.activity_setting);

            mSwitchPreference = (SwitchPreference) findPreference("example_switch");
            cachePreference = (Preference) findPreference("cache");
            searchcachePreference= (Preference) findPreference("searchcache");
            aboutPreference = (Preference) findPreference("about");
            aboutPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {

                @Override
                public boolean onPreferenceClick(Preference preference) {
                    // TODO Auto-generated method stub
                    UIHelper.showAbout(getActivity());
                    return false;
                }
            });
            searchcachePreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    UserManager.setSearchList(getActivity(),new ArrayList<String>());
                        ToastUtils.showMessage(getActivity(),"清除搜索记录成功");
                    return false;
                }
            });
            cachePreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {

                @Override
                public boolean onPreferenceClick(Preference preference) {
                    // TODO Auto-generated method stub
                    //清除内部缓存
                    DataCleanManager.cleanInternalCache(getActivity());
                    //清除外部缓存
                    DataCleanManager.cleanExternalCache(getActivity());
                    DataCleanManager.cleanFiles(getActivity());
                    //清除更新目录
                    DataCleanManager.deleteFolderFile(UpdateManager.getUpdatePath(), false);
                    caculateCacheSize();
                    ToastUtils.showMessage(getActivity(),"成功清除缓存");
                    return true;
                }
            });
            caculateCacheSize();
        }

        /**
         * 计算缓存的大小
         */
        private void caculateCacheSize() {
            long fileSize = 0;
            String cacheSize = "0KB";
            File filesDir = getActivity().getFilesDir();
            File cacheDir = getActivity().getCacheDir();

            fileSize += FileUtil.getDirSize(filesDir);
            YkLog.t("filesDir缓存", "" + fileSize);
            fileSize += FileUtil.getDirSize(cacheDir);
            YkLog.t("...+cacheDir缓存", "" + fileSize);
            // 2.2版本才有将应用缓存转移到sd卡的功能
            if (AppContext.isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
                File externalCacheDir = getActivity()
                        .getExternalCacheDir();
                File externalFilesDir = getActivity()
                        .getExternalFilesDir(Environment.DIRECTORY_DCIM);
                File UpdateFilesDir = new File(UpdateManager.getUpdatePath());
                fileSize += FileUtil.getDirSize(externalCacheDir);
                YkLog.t("...+externalCacheDir缓存", "" + fileSize);
                fileSize += FileUtil.getDirSize(externalFilesDir);
                YkLog.t("...+externalFilesDir缓存", "" + fileSize);
                fileSize += FileUtil.getDirSize(UpdateFilesDir);
                YkLog.t("...+UpdateFilesDir缓存", "" + fileSize);
            }
            if (fileSize > 60960)//清空后目录大小52KB，小于60960忽略不计
                cacheSize = FileUtil.formatFileSize(fileSize);
            cachePreference.setSummary(cacheSize);
        }
    }
}