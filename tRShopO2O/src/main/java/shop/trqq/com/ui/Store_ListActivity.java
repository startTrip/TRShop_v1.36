package shop.trqq.com.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import shop.trqq.com.R;
import shop.trqq.com.ui.Base.BaseFragmentActivity;
import shop.trqq.com.ui.Fragment.store_listFragment;


/**
 * 店铺列表
 *
 * @author Weiss
 */
public class Store_ListActivity extends BaseFragmentActivity {
    String sc_id, is_strategic_alliance, sc_name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        sc_id = getIntent().getStringExtra("sc_id");
        sc_name = getIntent().getStringExtra("sc_name");
        is_strategic_alliance = getIntent().getStringExtra("is_strategic_alliance");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        store_listFragment fragment = new store_listFragment();
        Bundle bundle = new Bundle();
        bundle.putString("sc_id", sc_id);
        bundle.putString("sc_name", sc_name);
        bundle.putString("is_strategic_alliance", is_strategic_alliance);
        fragment.setArguments(bundle);
        // 加到Activity中
        fragmentTransaction.replace(R.id.fragment, fragment);
        // 加到后台堆栈中，有下一句代码的话，点击返回按钮是退到Activity界面，没有的话，直接退出Activity
        // 后面的参数是此Fragment的Tag。相当于id
        // fragmentTransaction.addToBackStack("fragmentcart");
        // 记住提交
        fragmentTransaction.commitAllowingStateLoss();

    }
}
