package shop.trqq.com.supermarket;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    private int currntTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.market_activity_main);

        // ��ʼ����ͼ
        initView();
        // ��ʼ������
        initData(savedInstanceState);

        // ���ü�����
        setListener();
    }

    private void setListener() {

        mRadioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("CURRENTTAB", currntTab);
    }

    private void initData(Bundle savedInstanceState) {

        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // ��һ�γ�ʼ�� ��Fragment ��ӵ� ��������
        mList = new ArrayList<>();
        if(savedInstanceState==null){

            mList.add(new MarketHomeFragment());
            mList.add(new MarketClassifyFragment());
            mList.add(new MarketGoCartFragment());
            mList.add(new MarketLbsFragment());

            // ��4�� Fragment��ӵ�������,������
            for (int i = 0; i < mList.size(); i++) {
                transaction.add(R.id.main_container,mList.get(i),i+"");
                transaction.hide(mList.get(i));
            }
            currntTab = 0;
            transaction.show(mList.get(0));
            setCurrentTab(0);
        }else {
            for (int i = 0; i < 4; i++) {
                Fragment fragment = fragmentManager.findFragmentByTag(i + "");
                mList.add(fragment);
                transaction.hide(fragment);
            }
            // �õ������tab���л�������ȥ
            int index = savedInstanceState.getInt("CURRENTTAB");
            Fragment fragment = fragmentManager.findFragmentByTag(index + "");
            transaction.show(fragment);
            setCurrentTab(index);

            Log.d("market","mlist|"+mList.size()+"...."+"index"+index);
        }
        transaction.commit();
    }

    // ���õ�ǰ��Tab
    private void setCurrentTab(int index) {
        switch (index) {
            case 0:
                mRadioGroup.check(R.id.main_tab_home);
                break;
            case 1:
                mRadioGroup.check(R.id.main_tab_classify);
                break;
            case 2:
                mRadioGroup.check(R.id.main_tab_gocart);
                break;
            case 3:
                mRadioGroup.check(R.id.main_tab_personal);
                break;
            default:
                break;
        }
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
        // ��¼��ǰ�� tab;
        currntTab = index;
        Log.d("market","index"+index);
        // �л�Fragment
        switchFragment(index);
    }

    /**
     * �л�Fragment�ķ���,������ʾ������Fragment
     * @param index
     */
    private void switchFragment(int index) {

        int size = mList.size();
        Log.d("market","mlist|"+mList.size());
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
