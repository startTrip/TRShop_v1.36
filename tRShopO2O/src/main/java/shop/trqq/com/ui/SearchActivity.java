package shop.trqq.com.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import shop.trqq.com.R;
import shop.trqq.com.ui.Base.BaseFragmentActivity;
import shop.trqq.com.ui.Fragment.Fragment_Search;

/*
 * ����Activity
 * */

public class SearchActivity extends BaseFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();
            Fragment_Search fragment = new Fragment_Search();
            // �ӵ�Activity��
            fragmentTransaction.replace(R.id.fragment, fragment);
            // �ӵ���̨��ջ�У�����һ�����Ļ���������ذ�ť���˵�Activity���棬û�еĻ���ֱ���˳�Activity
            // ����Ĳ����Ǵ�Fragment��Tag���൱��id
            // fragmentTransaction.addToBackStack("fragmentcart");
            // ��ס�ύ http://blog.jobbole.com/66117/
            fragmentTransaction.commitAllowingStateLoss();
        }
    }
}
