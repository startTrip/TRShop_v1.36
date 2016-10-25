package shop.trqq.com.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import shop.trqq.com.R;
import shop.trqq.com.ui.Base.BaseFragmentActivity;
import shop.trqq.com.ui.Fragment.CommentsFragment;
import shop.trqq.com.ui.Fragment.product_detail_Fragment;
import shop.trqq.com.ui.Fragment.product_info_Fragment;
import shop.trqq.com.widget.FragmentPagerAdapter;
import shop.trqq.com.widget.MutipleTouchVerticalViewPager;
import shop.trqq.com.widget.VerticalPagerSlidingTabStrip;

/**
 *  商品信息 ViewPager 展示不同的 Fragment
 */
public class Goods_DetaillActivity extends BaseFragmentActivity {


    VerticalPagerSlidingTabStrip tabs;
    MutipleTouchVerticalViewPager pager;
    DisplayMetrics dm;
    //商品基本信息
    product_detail_Fragment afrag;
    //商品图文详情
    product_info_Fragment bfrag;
    //商品评论
    CommentsFragment cfrag;
    String[] titles = {"商品详情", "图文详情", "商品评论"};
    String goods_id;
    TextView mHeadTitleTextView;
    DisplayMetrics metrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        goods_id = getIntent().getStringExtra("goods_id");
        //布局是RelativeLayout，底部菜单用了android:layout_alignParentBottom="true" 解决底部菜单挡键盘
        int mode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;
        getWindow().setSoftInputMode(mode);

        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        initView();
    }

    /**
     * 初始化标题栏视图
     */
    private void initTitleBarView() {
        mHeadTitleTextView = (TextView) findViewById(R.id.head_title_textView);
        mHeadTitleTextView.setText("商品信息");
    }

    public DisplayMetrics getMetrics() {
        return metrics;
    }

    private void initView() {
        setContentView(R.layout.activity_goods_detail);
        dm = getResources().getDisplayMetrics();
        // 封装的 ViewPager
        pager = (MutipleTouchVerticalViewPager) findViewById(R.id.pager);
        tabs = (VerticalPagerSlidingTabStrip) findViewById(R.id.tabs);
        pager.setAdapter(new MyAdapter(getSupportFragmentManager(), titles));
        tabs.setViewPager(pager);
        initTitleBarView();
        //设置viewpager保留多少个显示界面
        pager.setOffscreenPageLimit(3);
      ;
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

        /**
         *  根据不同的position 返回不同的 Fragment
         * @param position
         * @return
         */
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (afrag == null) {
                        // 商品详情列表
                        afrag = new product_detail_Fragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("goods_id", goods_id);
                        afrag.setArguments(bundle);
                    }
                    return afrag;
                case 1:
                    if (bfrag == null) {
                        bfrag = new product_info_Fragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("goods_id", goods_id);
                        bundle.putString("mobile_body", "");
                        bfrag.setArguments(bundle);
                    }
                    return bfrag;
                case 2:
                    if (cfrag == null) {
                        cfrag = new CommentsFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("goods_id", goods_id);
                        cfrag.setArguments(bundle);
                    }
                    return cfrag;
                default:
                    return null;
            }
        }
    }
}
