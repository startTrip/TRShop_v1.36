package shop.trqq.com.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import shop.trqq.com.R;
import shop.trqq.com.ui.Base.BaseFragmentActivity;
import shop.trqq.com.ui.Fragment.store_listFragment;


/**
 * �����б�
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
        // �ӵ�Activity��
        fragmentTransaction.replace(R.id.fragment, fragment);
        // �ӵ���̨��ջ�У�����һ�����Ļ���������ذ�ť���˵�Activity���棬û�еĻ���ֱ���˳�Activity
        // ����Ĳ����Ǵ�Fragment��Tag���൱��id
        // fragmentTransaction.addToBackStack("fragmentcart");
        // ��ס�ύ
        fragmentTransaction.commitAllowingStateLoss();

    }
}
