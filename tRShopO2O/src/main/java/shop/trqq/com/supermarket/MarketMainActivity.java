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

        // ��ʼ����ͼ
        initView();
        // ��ʼ������
        initData();

        // ���ü�����
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

        // ��4�� Fragment��ӵ�������,������
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

    // RadioGroup �ļ����¼�,������Fragment�л�������
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        // ��������¼ ѡ�е�����һ�� RadioButton
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

        // �л�Fragment
        switchFragment(index);
    }

    /**
     * �л�Fragment�ķ���,������ʾ������Fragment
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

    // HomeFragment �е�� ���� ʱ��ʾ����ҳ��
    public void showClassifyFragment() {
        mRadioGroup.check(R.id.main_tab_classify);
    }
}
