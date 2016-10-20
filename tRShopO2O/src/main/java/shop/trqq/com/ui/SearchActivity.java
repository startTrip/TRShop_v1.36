package shop.trqq.com.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import shop.trqq.com.R;
import shop.trqq.com.ui.Base.BaseFragmentActivity;
import shop.trqq.com.ui.Fragment.Fragment_Search;

/*
 * 搜索Activity
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
            // 加到Activity中
            fragmentTransaction.replace(R.id.fragment, fragment);
            // 加到后台堆栈中，有下一句代码的话，点击返回按钮是退到Activity界面，没有的话，直接退出Activity
            // 后面的参数是此Fragment的Tag。相当于id
            // fragmentTransaction.addToBackStack("fragmentcart");
            // 记住提交 http://blog.jobbole.com/66117/
            fragmentTransaction.commitAllowingStateLoss();
        }
    }
}
