package shop.trqq.com.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import shop.trqq.com.R;
import shop.trqq.com.ui.Base.BaseFragmentActivity;
import shop.trqq.com.ui.Fragment.store_ClassFragment;
import shop.trqq.com.ui.Fragment.store_listFragment;
import shop.trqq.com.widget.PagerSlidingTabStrip;

/**
 * �����б�
 */
public class StoreActivity extends BaseFragmentActivity {
    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    // DisplayMetrics dm;
    private store_listFragment store_listfrag;
    private store_ClassFragment store_classfrag;
    private String[] titles = {"���еĵ���", "���̷���"};
    private String is_strategic_alliance;
    private TextView mHeadTitleTextView;
    private ImageView mImageBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        is_strategic_alliance = getIntent().getStringExtra(
                "is_strategic_alliance");
        initView();
    }

    /**
     * ��ʼ����������ͼ
     */
    private void initTitleBarView() {
        mImageBack = (ImageView) findViewById(R.id.title_back);
        mImageBack.setVisibility(View.VISIBLE);
        mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mHeadTitleTextView = (TextView) findViewById(R.id.head_title_textView);
        mHeadTitleTextView.setText("�����б�");
        if ("1".equals(is_strategic_alliance)) {
            mHeadTitleTextView.setText("ս�������̼�");
        }
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
        pager.setOffscreenPageLimit(2);
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
            switch (position) {
                case 0:
                    if (store_listfrag == null) {
                        store_listfrag = new store_listFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("is_strategic_alliance",
                                is_strategic_alliance);
                        store_listfrag.setArguments(bundle);
                    }
                    return store_listfrag;
                case 1:
                    if (store_classfrag == null) {
                        store_classfrag = new store_ClassFragment();
                    }
                    return store_classfrag;
                default:
                    return null;
            }
        }
    }
}
