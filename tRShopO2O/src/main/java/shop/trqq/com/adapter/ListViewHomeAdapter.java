package shop.trqq.com.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import cn.lightsky.infiniteindicator.InfiniteIndicatorLayout;
import cn.lightsky.infiniteindicator.slideview.BaseSliderView;
import cn.lightsky.infiniteindicator.slideview.BaseSliderView.OnSliderClickListener;
import cn.lightsky.infiniteindicator.slideview.DefaultSliderView;
import cn.lightsky.infiniteindicator.slideview.ThreeSliderView;
import shop.trqq.com.R;
import shop.trqq.com.UserManager;
import shop.trqq.com.bean.GoodsBean;
import shop.trqq.com.bean.Home1Bean;
import shop.trqq.com.bean.HomeBean;
import shop.trqq.com.ui.Base.UIHelper;
import shop.trqq.com.util.ImageLoadUtils;
import shop.trqq.com.util.ToastUtils;
import shop.trqq.com.util.YkLog;
import shop.trqq.com.widget.MyGridView;

/**
 * 首页样式列表的适配器
 *
 * @author Weiss
 * @created 2015-02-10
 */
public class ListViewHomeAdapter extends MultiItemCommonAdapter<HomeBean>
        implements OnClickListener {

    private static final String TAG = "ListViewHomeAdapter";
    private Handler mHandler;

    // private AutoScrollViewPager viewPager;
    // private CirclePageIndicator indicator;
    private InfiniteIndicatorLayout indicator;
    private int index;

    private Boolean haveAdv_list;// 存在首页广告轮播
    private Boolean isHome;// 是否首页，否为专题
    private static final int TYPE_ADV_LIST = 8; // 广告轮播样式
    private static final int TYPE_HOME1 = 1; // HOME1样式
    private static final int TYPE_HOME2 = 2;// HOME2样式
    private static final int TYPE_HOME3 = 3;// HOME3样式
    private static final int TYPE_HOME4 = 4;// HOME4样式
    private static final int TYPE_GOODS = 5; // 商品样式
    private static final int TYPE_GOODS1 = 6;// 打折样式
    private static final int TYPE_GOODS2 = 7;// 抢购样式
    private static final int TYPE_OTHER = 9;// 其它样式
    private static final int TYPE_OTHER2 = 10;// 其它样式

    /**
     * 新闻列表适配器构造方法
     *
     * @param context
     */
    public ListViewHomeAdapter(Context context, Handler Handler, Boolean isHome) {
        super(context, new ArrayList<HomeBean>(),
                new MultiItemTypeSupport<HomeBean>() {
                    @Override
                    public int getLayoutId(int position, HomeBean bean) {
                        if (bean.getAdv_list() != null) {
                            return R.layout.list_home_adv_list;
                        } else if (bean.getHome1() != null) {
                            return R.layout.list_home_1;
                        } else if (bean.getHome2() != null) {
                            return R.layout.list_home_2;
                        } else if (bean.getHome3() != null) {
                            return R.layout.list_home_3;
                        } else if (bean.getHome4() != null) {
                            return R.layout.list_home_4;
                        } else if (bean.getGoods() != null) {
                            return R.layout.list_home_good;
                        } else if (bean.getGoods1() != null) {
                            return R.layout.list_home_good;
                        } else if (bean.getGoods2() != null) {
                            return R.layout.list_home_good;
                        }
                        else if (bean.getOther() != null) {
                            return R.layout.list_home_other;
                        }
                        return R.layout.list_home_other2;
                    }

                    @Override
                    public int getViewTypeCount() {
                        return 11;
                    }

                    @Override
                    public int getItemViewType(int postion, HomeBean bean) {
                        if (bean.getAdv_list() != null) {
                            return TYPE_ADV_LIST;
                        } else if (bean.getHome1() != null) {
                            return TYPE_HOME1;
                        } else if (bean.getHome2() != null) {
                            return TYPE_HOME2;
                        } else if (bean.getHome3() != null) {
                            return TYPE_HOME3;
                        } else if (bean.getHome4() != null) {
                            return TYPE_HOME4;
                        } else if (bean.getGoods() != null) {
                            return TYPE_GOODS;
                        } else if (bean.getGoods1() != null) {
                            return TYPE_GOODS1;
                        } else if (bean.getGoods2() != null) {
                            return TYPE_GOODS2;
                        }
                        else if (bean.getOther() != null) {
                            return TYPE_OTHER;
                        }
                        return TYPE_OTHER2;
                    }
                });
        YkLog.i(TAG, "信息列表适配器构造方法");
        mHandler = Handler;
        haveAdv_list = false;
        this.isHome = isHome;
    }

    /**
     * 点击跳转到商品详情页面
     * @param type  类型
     * @param data  商品的ID
     */
    private void clickimg(String type, String data) {
        if (type.equals("url")) {
            try {
                String a[] = data.split("goods_id=");
                UIHelper.showGoods_Detaill(mContext, a[1]);
            } catch (Exception e) {
                if ("".equals(data) || "http://shop.trqq.com/".equals(data) || "http://shop.trqq.com".equals(data)) {
//				ToastUtils.showMessage(mContext, "亲！此链接不需要跳转");	
                } else {
                    ToastUtils.showMessage(mContext, "跳转出错，请确认是泰润官方商品");
                }
            }
        } else if (type.equals("keyword")) {
            UIHelper.showShop(mContext, data, "", "", "");
        } else if (type.equals("goods")) {
            UIHelper.showGoods_Detaill(mContext, data);
        } else if (type.equals("special")) {
            UIHelper.showSpecial(mContext, data);
        }
    }

    public void stopAd() {
        if (haveAdv_list) {
            indicator.stopAutoScroll();
        }
    }

    public void startAd() {
        if (haveAdv_list) {
            indicator.startAutoScroll();
        }
    }

    // @Override
    // public int getItemViewType(int position) {
    // if (mData.get(position).getAdv_list() != null) {
    // return TYPE_ADV_LIST;
    // } else if (mData.get(position).getHome1() != null) {
    // return TYPE_HOME1;
    // } else if (mData.get(position).getHome2() != null) {
    // return TYPE_HOME2;
    // } else if (mData.get(position).getHome3() != null) {
    // return TYPE_HOME3;
    // } else if (mData.get(position).getHome4() != null) {
    // return TYPE_HOME4;
    // } else if (mData.get(position).getGoods() != null) {
    // return TYPE_GOODS;
    // } else if (mData.get(position).getGoods1() != null) {
    // return TYPE_GOODS1;
    // } else if (mData.get(position).getGoods2() != null) {
    // return TYPE_GOODS2;
    // }
    // return super.getItemViewType(position);// return 0;
    // }
    //
    // // 设置一共多少样式
    // @Override
    // public int getViewTypeCount() {
    // // TODO Auto-generated method stub
    // return 9;
    // }

    // getview里面的一个方法
    @Override
    public void convert(ViewHolder holder, final HomeBean homeBean) {
        final int mPosition = holder.getmPosition();
        int type = getItemViewType(mPosition);
        switch (type) {
            case TYPE_ADV_LIST:
                indicator = (InfiniteIndicatorLayout) holder
                        .getView(R.id.indicator_default_circle);
                if (homeBean.getAdv_list().getItem().size() == 0) {
                    indicator.setVisibility(View.GONE);
                } else {
                    haveAdv_list = true;
                    indicator.removeAllSliders();
                    for (Home1Bean bean : homeBean.getAdv_list().getItem()) {
                        final DefaultSliderView textSliderView = new DefaultSliderView(
                                mContext);

                        textSliderView.getBundle().putSerializable("extra", bean);

                        String uri = bean.getImage();
                        // 本地测试专用
                        uri = uri.replace("localhost", "192.168.191.1");
                        textSliderView
                                .image(uri)
                                .setScaleType(BaseSliderView.ScaleType.Fit)
                                .setOnSliderClickListener(
                                        new OnSliderClickListener() {
                                            @Override
                                            public void onSliderClick(
                                                    BaseSliderView slider) {
                                                // TODO Auto-generated method stub
                                                Home1Bean bean = (Home1Bean) textSliderView
                                                        .getBundle()
                                                        .getSerializable("extra");
                                                clickimg(bean.getType(),
                                                        bean.getData());
                                            }
                                        });
                        indicator.addSlider(textSliderView);
                    }
                    indicator
                            .setIndicatorPosition(InfiniteIndicatorLayout.IndicatorPosition.Center_Bottom);
                    indicator.startAutoScroll();
                    indicator.setVisibility(View.VISIBLE);
                }
                if (isHome) {
                    LinearLayout home_product_indicator = (LinearLayout) holder
                            .getView(R.id.home_product_indicator);
                    ImageButton index1Btn = (ImageButton) holder
                            .getView(R.id.index_1_btn);
                    ImageButton index2Btn = (ImageButton) holder
                            .getView(R.id.index_2_btn);
                    ImageButton index3Btn = (ImageButton) holder
                            .getView(R.id.index_3_btn);
                    ImageButton index4Btn = (ImageButton) holder
                            .getView(R.id.index_4_btn);
                    ImageButton index5Btn = (ImageButton) holder
                            .getView(R.id.index_5_btn);
                    ImageButton index6Btn = (ImageButton) holder
                            .getView(R.id.index_6_btn);
                    ImageButton index7Btn = (ImageButton) holder
                            .getView(R.id.index_7_btn);
                    ImageButton index8Btn = (ImageButton) holder
                            .getView(R.id.index_8_btn);
                    index1Btn.setOnClickListener(this);
                    index2Btn.setOnClickListener(this);
                    index3Btn.setOnClickListener(this);
                    index4Btn.setOnClickListener(this);
                    index5Btn.setOnClickListener(this);
                    index6Btn.setOnClickListener(this);
                    index7Btn.setOnClickListener(this);
                    index8Btn.setOnClickListener(this);
                    home_product_indicator.setVisibility(View.VISIBLE);
                }
                break;
            case TYPE_HOME1:
                TextView home1title = (TextView) holder.getView(R.id.home1_title);
                ImageView home1Img = (ImageView) holder.getView(R.id.home1_image);
                if (homeBean.getHome1().getTitle().equals(""))
                    home1title.setVisibility(View.GONE);
                home1title.setText(homeBean.getHome1().getTitle());
                ImageLoader.getInstance().displayImage(
                        homeBean.getHome1().getImage(), home1Img,
                        ImageLoadUtils.getoptions(),
                        ImageLoadUtils.getAnimateFirstDisplayListener());
                home1Img.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickimg(homeBean.getHome1().getType(), homeBean.getHome1()
                                .getData());
                    }
                });
                break;
            case TYPE_HOME2:
                ImageView square_image = (ImageView) holder
                        .getView(R.id.square_image);
                ImageView rectangle1_image = (ImageView) holder
                        .getView(R.id.rectangle1_image);
                ImageView rectangle2_image = (ImageView) holder
                        .getView(R.id.rectangle2_image);
                TextView home2title = (TextView) holder.getView(R.id.home2_title);
                if (homeBean.getHome2().getTitle().equals(""))
                    home2title.setVisibility(View.GONE);
                home2title.setText(homeBean.getHome2().getTitle());
                ImageLoader.getInstance().displayImage(
                        homeBean.getHome2().getSquare_image(), square_image,
                        ImageLoadUtils.getoptions(),
                        ImageLoadUtils.getAnimateFirstDisplayListener());
                ImageLoader.getInstance().displayImage(
                        homeBean.getHome2().getRectangle1_image(),
                        rectangle1_image, ImageLoadUtils.getoptions(),
                        ImageLoadUtils.getAnimateFirstDisplayListener());
                ImageLoader.getInstance().displayImage(
                        homeBean.getHome2().getRectangle2_image(),
                        rectangle2_image, ImageLoadUtils.getoptions(),
                        ImageLoadUtils.getAnimateFirstDisplayListener());
                square_image.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickimg(homeBean.getHome2().getSquare_type(), homeBean
                                .getHome2().getSquare_data());
                    }
                });
                rectangle1_image.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickimg(homeBean.getHome2().getRectangle1_type(), homeBean
                                .getHome2().getRectangle1_data());
                    }
                });
                rectangle2_image.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickimg(homeBean.getHome2().getRectangle2_type(), homeBean
                                .getHome2().getRectangle2_data());
                    }
                });
                break;
            case TYPE_HOME4:
                ImageView home4_square_image = (ImageView) holder
                        .getView(R.id.home4_square_image);
                ImageView home4_rectangle2_image = (ImageView) holder
                        .getView(R.id.home4_rectangle2_image);
                ImageView home4_rectangle1_image = (ImageView) holder
                        .getView(R.id.home4_rectangle1_image);
                TextView home4title = (TextView) holder.getView(R.id.home4_title);
                if (homeBean.getHome4().getTitle().equals(""))
                    home4title.setVisibility(View.GONE);
                home4title.setText(homeBean.getHome4().getTitle());
                ImageLoader.getInstance().displayImage(
                        homeBean.getHome4().getSquare_image(), home4_square_image,
                        ImageLoadUtils.getoptions(),
                        ImageLoadUtils.getAnimateFirstDisplayListener());

                ImageLoader.getInstance().displayImage(
                        homeBean.getHome4().getRectangle1_image(),
                        home4_rectangle1_image, ImageLoadUtils.getoptions(),
                        ImageLoadUtils.getAnimateFirstDisplayListener());

                ImageLoader.getInstance().displayImage(
                        homeBean.getHome4().getRectangle2_image(),
                        home4_rectangle2_image, ImageLoadUtils.getoptions(),
                        ImageLoadUtils.getAnimateFirstDisplayListener());
                home4_square_image.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickimg(homeBean.getHome4().getSquare_type(), homeBean
                                .getHome4().getSquare_data());
                    }
                });
                home4_rectangle1_image.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickimg(homeBean.getHome4().getRectangle1_type(), homeBean
                                .getHome4().getRectangle1_data());
                    }
                });
                home4_rectangle2_image.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickimg(homeBean.getHome4().getRectangle2_type(), homeBean
                                .getHome4().getRectangle2_data());
                    }
                });
                break;
            case TYPE_HOME3:
                TextView home3title = (TextView) holder.getView(R.id.home3_title);
                MyGridView home3GridView = (MyGridView) holder
                        .getView(R.id.home3_GridView);
                home3title.setText(homeBean.getHome3().getTitle());
                if (homeBean.getHome3().getTitle().equals(""))
                    home3title.setVisibility(View.GONE);
                GridviewHome3Adapter home3Adapter = new GridviewHome3Adapter(
                        mContext);
                home3Adapter.setData(homeBean.getHome3().getItem());
                home3GridView.setAdapter(home3Adapter);
                break;
            case TYPE_GOODS:
                TextView goodstitle = (TextView) holder
                        .getView(R.id.home_goods_title);
                MyGridView goodsGridView = (MyGridView) holder
                        .getView(R.id.home_goods_GridView);
                goodstitle.setText(homeBean.getGoods().getTitle());
                if (homeBean.getGoods().getTitle().equals(""))
                    goodstitle.setVisibility(View.GONE);
                GridviewGoodsAdapter goodsAdapter = new GridviewGoodsAdapter(
                        mContext, GridviewGoodsAdapter.TYPE_GOODS);
                goodsAdapter.setData(homeBean.getGoods().getItem());
                goodsGridView.setAdapter(goodsAdapter);
                break;
            case TYPE_GOODS1:// 打折
                TextView goods1title = (TextView) holder
                        .getView(R.id.home_goods_title);
                MyGridView goods1GridView = (MyGridView) holder
                        .getView(R.id.home_goods_GridView);
                goods1title.setText(homeBean.getGoods1().getTitle());
                if (homeBean.getGoods1().getTitle().equals(""))
                    goods1title.setVisibility(View.GONE);
                GridviewGoodsAdapter goods1Adapter = new GridviewGoodsAdapter(
                        mContext, GridviewGoodsAdapter.TYPE_GOODS1);
                goods1Adapter.setData(homeBean.getGoods1().getItem());
                goods1GridView.setAdapter(goods1Adapter);
                break;
            case TYPE_GOODS2:// 抢购
                TextView goods2title = (TextView) holder
                        .getView(R.id.home_goods_title);
                MyGridView goods2GridView = (MyGridView) holder
                        .getView(R.id.home_goods_GridView);
                goods2title.setText(homeBean.getGoods2().getTitle());
                if (homeBean.getGoods2().getTitle().equals(""))
                    goods2title.setVisibility(View.GONE);
                GridviewGoodsAdapter goods2Adapter = new GridviewGoodsAdapter(
                        mContext, GridviewGoodsAdapter.TYPE_GOODS2);
                goods2Adapter.setData(homeBean.getGoods2().getItem());
                goods2GridView.setAdapter(goods2Adapter);
                break;
            case TYPE_OTHER://销售排行
                TextView other_title = (TextView) holder.getView(R.id.other_title);
                other_title.setText(homeBean.getOther().getTitle());
                InfiniteIndicatorLayout saleIndicator = (InfiniteIndicatorLayout) holder
                        .getView(R.id.indicator_sale_circle);
                saleIndicator.removeAllSliders();
                List<GoodsBean> other = homeBean.getOther().getItem();
                //正式要改
                //String pic_host = "http://192.168.191.1/late_shop/data/upload/";
                String pic_host = "http://shop.trqq.com/data/upload/";
                for (int i = 0; i < other.size() / 3; i++) {
                    //LinkedHashMap排序，HashMap顺序乱的
                    final LinkedHashMap<String, String> url_maps = new LinkedHashMap<String, String>();
                    url_maps.put(other.get(0 + i * 3).getGoods_id(),
                            pic_host + other.get(0 + i * 3).getGoods_pic());
                    url_maps.put(other.get(1 + i * 3).getGoods_id(),
                            pic_host + other.get(1 + i * 3).getGoods_pic());
                    url_maps.put(other.get(2 + i * 3).getGoods_id(),
                            pic_host + other.get(2 + i * 3).getGoods_pic());
                    final ThreeSliderView threeSliderView = new ThreeSliderView(
                            mContext, url_maps);
                    threeSliderView.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            List<String> good_id_list = new ArrayList<String>();
                            for (String key : url_maps.keySet()) {
                                good_id_list.add(key);
                            }
                            switch (v.getId()) {
                                case R.id.slider_image1:
                                    clickimg("goods", good_id_list.get(0));
                                    break;
                                case R.id.slider_image2:
                                    clickimg("goods", good_id_list.get(1));
                                    break;
                                case R.id.slider_image3:
                                    clickimg("goods", good_id_list.get(2));
                                    break;
                                default:
                                    break;
                            }

                        }
                    }).setScaleType(BaseSliderView.ScaleType.Fit);
                    saleIndicator.addSlider(threeSliderView);
                }
                saleIndicator
                        .setIndicatorPosition(InfiniteIndicatorLayout.IndicatorPosition.Center_Bottom);
                saleIndicator.setInterval(5000);
                saleIndicator.startAutoScroll();
                break;
            case TYPE_OTHER2://新品推荐 相关的内容

                TextView other2_title = (TextView) holder.getView(R.id.other2_title);
                other2_title.setText(homeBean.getOther2().getTitle());
                ImageView other2_image1 = (ImageView) holder
                        .getView(R.id.other2_image1);
                ImageView other2_image2 = (ImageView) holder
                        .getView(R.id.other2_image2);
                ImageView other2_image3 = (ImageView) holder
                        .getView(R.id.other2_image3);
                final List<GoodsBean> other2 = homeBean.getOther2().getItem();
                String pic2_host = "http://shop.trqq.com/data/upload/";
                ImageLoader.getInstance().displayImage(
                        pic2_host+other2.get(0).getGoods_pic(), other2_image1,
                        ImageLoadUtils.getoptions(),
                        ImageLoadUtils.getAnimateFirstDisplayListener());

                ImageLoader.getInstance().displayImage(
                        pic2_host+other2.get(1).getGoods_pic(),
                        other2_image2, ImageLoadUtils.getoptions(),
                        ImageLoadUtils.getAnimateFirstDisplayListener());

                ImageLoader.getInstance().displayImage(
                        pic2_host+other2.get(2).getGoods_pic(),
                        other2_image3, ImageLoadUtils.getoptions(),
                        ImageLoadUtils.getAnimateFirstDisplayListener());
                other2_image1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickimg("goods",other2.get(0).getGoods_id());
                    }
                });
                other2_image2.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickimg("goods",other2.get(1).getGoods_id());
                    }
                });
                other2_image3.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickimg("goods",other2.get(2).getGoods_id());
                    }
                });
                break;
        }
    }


    // 上方 泰润商城中超市、酒店、餐厅等模块的点击事件
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.index_1_btn:
//                UIHelper.showShop(mContext, "", "", "", "375");
                // 跳转到超市商铺界面
//                UIHelper.showStore(mContext, "126");
                UIHelper.showMarket(mContext);
                break;
            case R.id.index_2_btn:
                UIHelper.showShop(mContext, "", "", "", "376");
                break;
            case R.id.index_3_btn:
                UIHelper.showShop(mContext, "", "", "", "377");
                break;
            case R.id.index_4_btn:
                UIHelper.showStore_list2(mContext, "1", "", "");
                break;
            case R.id.index_5_btn:
                UIHelper.showStore_list(mContext, "0");
                break;
            case R.id.index_6_btn:
                UIHelper.showShop(mContext, "", "", "", "");
                break;
            case R.id.index_7_btn:
//                UIHelper.showAbout(mContext);
                UIHelper.showStore(mContext, "2");
                break;
            case R.id.index_8_btn:
                if (UserManager.isLogin()) {
                    UIHelper.showSuggest(mContext);
                } else {
                    ToastUtils.showMessage(mContext, "请登录");
                }
                break;
            default:
                break;
        }
    }

}
