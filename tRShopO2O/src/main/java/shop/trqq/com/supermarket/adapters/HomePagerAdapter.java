package shop.trqq.com.supermarket.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import shop.trqq.com.bean.Mb_SlidersBean;
import shop.trqq.com.ui.Base.UIHelper;
import shop.trqq.com.util.YkLog;

/**
 * Project: TRShop_v1.36
 * Author: wm
 * Data:   2016/9/18
 */
public class HomePagerAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {

    private final float mDensity;
    private Context mContext;
    private ArrayList<ImageView> mList;
    private ArrayList<Mb_SlidersBean> mDataBeen;
    private ViewPager mViewPager;

    private onBannerImageClickListener mOnBannerImageClickListener;
    private final int mWidthPixels;

    public interface onBannerImageClickListener{
        void onBannerImageClick(int i);
    }

    public void setOnBannerImageClickListener(onBannerImageClickListener onBannerImageClickListener) {
        mOnBannerImageClickListener = onBannerImageClickListener;
    }

    public HomePagerAdapter(Context context, ArrayList<Mb_SlidersBean> topData, ViewPager topPager) {
        mContext = context;
        if (topData != null) {
            mDataBeen = topData;
        } else {
            mDataBeen = new ArrayList<>();
        }
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        mWidthPixels = displayMetrics.widthPixels;
        mDensity = displayMetrics.density;

        mList = new ArrayList<>();
        if(mDataBeen.size()==1){
            ImageView imageView = new ImageView(mContext);
            mList.add(imageView);
        }else if(mDataBeen.size()>1){

            for (int i = 0; i < mDataBeen.size() + 2; i++) {
                ImageView imageView = new ImageView(mContext);
                mList.add(imageView);
            }
        }
        mViewPager = topPager;
        mViewPager.addOnPageChangeListener(this);
    }

    @Override
    public int getCount() {

        return mDataBeen == null ? 0 : mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        Log.d("AAA",position+"");
        ImageView imageView = mList.get(position);

        int size = mDataBeen.size();
        if (size > 0) {

            Mb_SlidersBean dataBean;
            if (position == 0) {
                dataBean = mDataBeen.get(size - 1);
            } else if (position == mList.size() - 1) {
                dataBean = mDataBeen.get(0);
            } else {
                dataBean = mDataBeen.get(position - 1);
            }
            String img_url = dataBean.getImgUrl();

            if (!TextUtils.isEmpty(img_url)) {

                Picasso.with(mContext).load(img_url)
                        .config(Bitmap.Config.RGB_565)
                        .resize(mWidthPixels, (int) (180*mDensity))
                        .centerInside()
                        .into(imageView);
            }

            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {

                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:

                            // 按下s
                            if (mOnBannerImageClickListener != null) {
                                mOnBannerImageClickListener.onBannerImageClick(0);
                            }
                            break;
                        case MotionEvent.ACTION_MOVE:
                            break;
                        case MotionEvent.ACTION_UP:
                            if (mOnBannerImageClickListener != null) {
                                mOnBannerImageClickListener.onBannerImageClick(1);
                            }
                            break;
                    }
                    return false;
                }
            });

            // 图片的点击事件 监听
            imageView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int i = 0;
                    if(mList.size()==1){
                        i = 0;
                    }else {
                        i = mList.indexOf(v)-1;
                    }
                    YkLog.e("click","点击了"+i);
                    Mb_SlidersBean mb_slidersBean = mDataBeen.get(i);
                    String type = mb_slidersBean.getType();
                    String link = mb_slidersBean.getLink();
                    clickimg(type,link);
                }
            });
            container.addView(imageView);
        }
        return imageView;
    }

    private void clickimg(String type, String data) {
        if (type.equals("1")) {
            if(data.contains("goods_id=")){
                String a[] = data.split("goods_id=");
                UIHelper.showGoods_Detaill(mContext, a[1]);
            }else if(data.contains("store_id=")&&data.contains("stc_id=")){
                String[] split = data.split("stc_id=");
                UIHelper.showMarketGoodList(mContext,"126",split[1],"");
            }
        } else if (type.equals("2")) {
            UIHelper.showGoods_Detaill(mContext, data);
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }



    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position < 1) {
            mViewPager.setCurrentItem(mDataBeen.size(), false); //首位之前跳转到末尾
        } else if (position > mDataBeen.size()) {

            mViewPager.setCurrentItem(1, false);    // 末位之后跳转到首位
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
