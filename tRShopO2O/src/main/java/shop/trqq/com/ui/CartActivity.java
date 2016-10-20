package shop.trqq.com.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import shop.trqq.com.R;
import shop.trqq.com.ui.Base.BaseFragmentActivity;
import shop.trqq.com.ui.Fragment.Fragment_Cart;

/**
 * 购物车界面
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
        // 加到Activity中
        // fragmentTransaction.replace(R.id.fragment,fragment);
        fragmentManager.beginTransaction().add(R.id.fragment, fragment)
                .commitAllowingStateLoss();
        // 加到后台堆栈中，有下一句代码的话，点击返回按钮是退到Activity界面，没有的话，直接退出Activity
        // 后面的参数是此Fragment的Tag。相当于id
        // fragmentTransaction.addToBackStack("fragmentcart");
        // 记住提交
        // fragmentTransaction.commit();

    }
}
