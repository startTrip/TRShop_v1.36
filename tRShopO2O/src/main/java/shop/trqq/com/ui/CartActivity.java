package shop.trqq.com.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import shop.trqq.com.R;
import shop.trqq.com.ui.Base.BaseFragmentActivity;
import shop.trqq.com.ui.Fragment.Fragment_Cart;

/**
 * ���ﳵ����
 */
public class CartActivity extends BaseFragmentActivity {
    Fragment_Cart fragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        fragment = new Fragment_Cart();
        // �ӵ�Activity��
        // fragmentTransaction.replace(R.id.fragment,fragment);
        fragmentManager.beginTransaction().add(R.id.fragment, fragment)
                .commitAllowingStateLoss();
        // �ӵ���̨��ջ�У�����һ�����Ļ���������ذ�ť���˵�Activity���棬û�еĻ���ֱ���˳�Activity
        // ����Ĳ����Ǵ�Fragment��Tag���൱��id
        // fragmentTransaction.addToBackStack("fragmentcart");
        // ��ס�ύ
        // fragmentTransaction.commit();

    }
}
