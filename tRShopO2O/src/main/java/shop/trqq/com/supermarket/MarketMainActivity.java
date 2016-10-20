package shop.trqq.com.supermarket;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;

import java.util.ArrayList;

import shop.trqq.com.R;
import shop.trqq.com.supermarket.fragments.MarketClassifyFragment;
import shop.trqq.com.supermarket.fragments.MarketGoCartFragment;
import shop.trqq.com.supermarket.fragments.MarketHomeFragment;
import shop.trqq.com.supermarket.fragments.MarketLbsFragment;

public class MarketMainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private RadioGroup mRadioGroup;
    private ArrayList<Fragment> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.market_activity_main);

        // 初始化视图
        initView();
        // 初始化数据
        initData();

        // 设置监听器
        setListener();
    }

    private void setListener() {

        mRadioGroup.setOnCheckedChangeListener(this);
    }

    private void initData() {

        mList = new ArrayList<>();

        mList.add(new MarketHomeFragment());
        mList.add(new MarketClassifyFragment());
        mList.add(new MarketGoCartFragment());
        mList.add(new MarketLbsFragment());

        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // 将4个 Fragment添加到容器中,并隐藏
        for (Fragment fragment : mList) {
            transaction.add(R.id.main_container,fragment);
            transaction.hide(fragment);
        }
        transaction.show(mList.get(0));
        mRadioGroup.check(R.id.main_tab_home);
        transaction.commit();
    }

    private void initView() {

        mRadioGroup = (RadioGroup) findViewById(R.id.main_tab_bar);
    }

    // RadioGroup 的监听事件,用于与Fragment切换的联动
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        // 用索引记录 选中的是哪一个 RadioButton
        int index = 0;
        switch (checkedId) {
            case R.id.main_tab_home:
                index = 0;
                break;
            case R.id.main_tab_classify:
                index = 1;
                break;
            case R.id.main_tab_gocart:
                index = 2;
                break;
            case R.id.main_tab_personal:
                index = 3;
                break;
        }

        // 切换Fragment
        switchFragment(index);
    }

    /**
     * 切换Fragment的方法,用于显示和隐藏Fragment
     * @param index
     */
    private void switchFragment(int index) {

        int size = mList.size();

        if(index>=0 && index<size){
            FragmentManager fragmentManager = getSupportFragmentManager();

            FragmentTransaction transaction = fragmentManager.beginTransaction();

            for (int i = 0; i < size; i++) {
                Fragment fragment = mList.get(i);
                if(i==index){
                    transaction.show(fragment);
                }else {
                    transaction.hide(fragment);
                }
            }
            transaction.commit();
        }
    }

    // HomeFragment 中点击 更多 时显示更多页面
    public void showClassifyFragment() {
        mRadioGroup.check(R.id.main_tab_classify);
    }
}
