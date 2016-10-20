package shop.trqq.com.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.TextView;

import shop.trqq.com.R;
import shop.trqq.com.ui.Base.BaseFragmentActivity;
import shop.trqq.com.ui.Fragment.VoucherListFragment;
import shop.trqq.com.widget.PagerSlidingTabStrip;

/**
 * ����ȯ
 */
public class Voucher_ListActivity extends BaseFragmentActivity {
    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    // DisplayMetrics dm;
    private VoucherListFragment[] VoucherListfrag = new VoucherListFragment[3];
    private String[] titles = {"δʹ��", "��ʹ��", "�ѹ���"};
    private String goods_id;
    private TextView mHeadTitleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    /**
     * ��ʼ����������ͼ
     */
    private void initTitleBarView() {
        mHeadTitleTextView = (TextView) findViewById(R.id.head_title_textView);
        mHeadTitleTextView.setText("�ҵĴ����");
    }

    private void initView() {
        setContentView(R.layout.activity_voucher_list);
        // dm = getResources().getDisplayMetrics();
        pager = (ViewPager) findViewById(R.id.pager);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager.setAdapter(new MyAdapter(getSupportFragmentManager(), titles));
        tabs.setViewPager(pager);
        initTitleBarView();
        // ����viewpager�������ٸ���ʾ����
        pager.setOffscreenPageLimit(3);
    }

    public class MyAdapter extends FragmentPagerAdapter {
        String[] _titles;

        public MyAdapter(FragmentManager fm, String[] titles) {
            super(fm);
            _titles = titles;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
            return super.instantiateItem(container, position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return _titles[position];
        }

        @Override
        public int getCount() {
            return _titles.length;
        }

        @Override
        public Fragment getItem(int position) {
            if (VoucherListfrag[position] == null) {
                VoucherListfrag[position] = new VoucherListFragment();
                Bundle bundle = new Bundle();
                bundle.putString("voucher_state", String.valueOf(position + 1));
                VoucherListfrag[position].setArguments(bundle);
            }
            return VoucherListfrag[position];
        }
    }
}
