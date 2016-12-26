package shop.trqq.com.ui.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.vlonjatg.progressactivity.ProgressActivity;
import com.zxing.activity.CaptureActivity;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import shop.trqq.com.R;
import shop.trqq.com.bean.GoodClassBean;
import shop.trqq.com.ui.Base.UIHelper;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.ToastUtils;

public class Fragment_Categroy extends Fragment {
    private View rootView;// 缓存Fragment view
    private Context mContext;
    private String toolsList[];
    private TextView toolsTextViews[];
    private View views[];
    private LayoutInflater inflater;
    private ScrollView scrollView;
    private int scrllViewWidth = 0, scrollViewMiddle = 0;
    private ViewPager shop_pager;
    private int currentItem = 0;
    private ShopAdapter shopAdapter;
    private ViewGroup parent;
    private ArrayList<GoodClassBean> typeList;
    private Gson gson;
    // 加载进度
    private ProgressActivity progressActivity;
    // 标题栏标题
    private TextView mHeadTitleTextView;
    private EditText mHeadEditText;
    private ImageView mHeadRightImg;
    private LinearLayout mRightLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        mContext = getActivity();
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_categroy, container, false);
            scrollView = (ScrollView) rootView
                    .findViewById(R.id.tools_scrlllview);
            progressActivity = (ProgressActivity) rootView
                    .findViewById(R.id.Categroy_progress);
            shopAdapter = new ShopAdapter(getChildFragmentManager());
            gson = new Gson();
            typeList = new ArrayList<GoodClassBean>();
            initTitleBarView();
            loadOnlineTYPEListData();

        }
        // 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        return rootView;
    }

    /**
     * 初始化标题栏视图
     */
    private void initTitleBarView() {
        mHeadRightImg = (ImageView) rootView
                .findViewById(R.id.head_right_imageView);
        // mHeadTitleTextView = (TextView)
        // rootView.findViewById(R.id.head_title_textView);
        mRightLayout = (LinearLayout) rootView
                .findViewById(R.id.head_right_img_linearLayout);
        mHeadEditText = (EditText) rootView.findViewById(R.id.head_search_edit);

        mHeadEditText.setVisibility(View.VISIBLE);
        mHeadRightImg.setImageResource(R.drawable.barcode_normal);
        // mHeadRightImg.setVisibility(View.VISIBLE);
        mRightLayout.setVisibility(View.VISIBLE);
        mHeadEditText.setFocusable(false);
        mHeadEditText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                UIHelper.showSearch(mContext);
            }
        });
        mHeadRightImg.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent openCameraIntent = new Intent(getActivity(),
                        CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
            }
        });
        LinearLayout tLinearLayout=(LinearLayout) rootView
                .findViewById(R.id.header_relativelayout);
//        SystemBarHelper.immersiveStatusBar(getActivity(),0);
//        SystemBarHelper.setHeightAndPadding(getActivity(), tLinearLayout);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            // ToastUtils.showMessage(getActivity(), scanResult);
            try {
                String a[] = scanResult.split("goods_id=");
                UIHelper.showGoods_Detaill(mContext, a[1]);
            } catch (Exception e) {
                ToastUtils.showMessage(getActivity(), "扫描出错，请确认是泰润官方商品二维码");
            }

        }
    }

    /**
     * 动态生成显示items中的textview
     */
    private void showToolsView() {

        LinearLayout toolsLayout = (LinearLayout) rootView
                .findViewById(R.id.tools);
        toolsTextViews = new TextView[typeList.size()];
        views = new View[typeList.size()];

        for (int i = 0; i < typeList.size(); i++) {
            View view = inflater.inflate(R.layout.item_b_top_nav_layout, null);
            view.setId(i);
            view.setOnClickListener(toolsItemListener);
            TextView textView = (TextView) view.findViewById(R.id.text);
            textView.setText(typeList.get(i).getGc_name());
            toolsLayout.addView(view);
            toolsTextViews[i] = textView;
            views[i] = view;
        }
        changeTextColor(0);
        progressActivity.showContent();
    }

    private void loadOnlineTYPEListData() {
        progressActivity.showLoading();
        RequestParams requestParams = new RequestParams();
        HttpUtil.get(HttpUtil.URL_GOODSCLASS, requestParams,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          byte[] responseBody) {
                        try {
                            String jsonString = new String(responseBody);
                            JSONObject jsonObjects = new JSONObject(jsonString)
                                    .getJSONObject("datas");

                            String type_list = jsonObjects
                                    .getString("class_list");
                            //System.out.println(type_list);
                            typeList = gson.fromJson(type_list,
                                    new TypeToken<List<GoodClassBean>>() {
                                    }.getType());
                            showToolsView();
                            initPager();
                        } catch (Exception e) {
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {

                        try {
                            if (isAdded()) {
                                Drawable errorDrawable = ContextCompat.getDrawable(mContext, R.drawable.wifi_off);
                                progressActivity.showError(errorDrawable, "网络开了小差",
                                        "连接不上网络，请确认一下您的网络开关，或者服务器网络正忙，请稍后再试",
                                        "重新连接", new OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                // TODO Auto-generated method stub
                                                progressActivity.showLoading();
                                                typeList.clear();
                                                loadOnlineTYPEListData();
                                            }
                                        });
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    private View.OnClickListener toolsItemListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            shop_pager.setCurrentItem(v.getId());

        }
    };

    /**
     * initPager<br/>
     * 初始化ViewPager控件相关内容
     */
    private void initPager() {
        shop_pager = (ViewPager) rootView.findViewById(R.id.goods_pager);
        shop_pager.setAdapter(shopAdapter);
        shop_pager.setOnPageChangeListener(onPageChangeListener);
    }

    /**
     * OnPageChangeListener<br/>
     * 监听ViewPager选项卡变化事的事件
     */

    private OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
        @Override
        public void onPageSelected(int arg0) {
            if (shop_pager.getCurrentItem() != arg0)
                shop_pager.setCurrentItem(arg0);
            if (currentItem != arg0) {
                changeTextColor(arg0);
                changeTextLocation(arg0);
            }

            currentItem = arg0;
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };

    /**
     * ViewPager 加载选项卡
     *
     * @author Administrator
     */
    private class ShopAdapter extends FragmentPagerAdapter {
        public ShopAdapter(FragmentManager fm) {
            super(fm);
        }

        Fragment_typelist typelistView;

        @Override
        public Fragment getItem(int position) {
            //第二级分类
            typelistView = new Fragment_typelist();
            Bundle bundle = new Bundle();
            bundle.putString("gc_id", typeList
                    .get(position).getGc_id());
            typelistView.setArguments(bundle);
            /*
			 * Fragment fragment =new Fragment_pro_type(); Bundle bundle=new
			 * Bundle(); String str=typeList.get(arg0).getGc_name();
			 * bundle.putString("typename",str); fragment.setArguments(bundle);
			 */

            return typelistView;
        }

        @Override
        public int getCount() {
            return typeList.size();
        }
    }

    /**
     * 改变textView的颜色
     *
     * @param id
     */
    private void changeTextColor(int id) {
        for (int i = 0; i < toolsTextViews.length; i++) {
            if (i != id) {
                toolsTextViews[i]
                        .setBackgroundResource(android.R.color.transparent);
                toolsTextViews[i].setTextColor(0xff000000);
            }
        }
        toolsTextViews[id].setBackgroundResource(android.R.color.white);
        toolsTextViews[id].setTextColor(0xffff5d5e);
    }

    /**
     * 改变栏目位置
     *
     * @param clickPosition
     */
    private void changeTextLocation(int clickPosition) {

        int x = (views[clickPosition].getTop() - getScrollViewMiddle() + (getViewheight(views[clickPosition]) / 2));
        scrollView.smoothScrollTo(0, x);
    }

    /**
     * 返回scrollview的中间位置
     *
     * @return
     */
    private int getScrollViewMiddle() {
        if (scrollViewMiddle == 0)
            scrollViewMiddle = getScrollViewheight() / 2;
        return scrollViewMiddle;
    }

    /**
     * 返回ScrollView的宽度
     *
     * @return
     */
    private int getScrollViewheight() {
        if (scrllViewWidth == 0)
            scrllViewWidth = scrollView.getBottom() - scrollView.getTop();
        return scrllViewWidth;
    }

    /**
     * 返回view的宽度
     *
     * @param view
     * @return
     */
    private int getViewheight(View view) {
        return view.getBottom() - view.getTop();
    }
}