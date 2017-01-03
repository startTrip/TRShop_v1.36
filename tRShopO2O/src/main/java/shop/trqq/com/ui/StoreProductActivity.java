package shop.trqq.com.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.vlonjatg.progressactivity.ProgressActivity;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import cn.lightsky.infiniteindicator.InfiniteIndicatorLayout;
import cn.lightsky.infiniteindicator.slideview.BaseSliderView;
import cn.lightsky.infiniteindicator.slideview.BaseSliderView.OnSliderClickListener;
import cn.lightsky.infiniteindicator.slideview.DefaultSliderView;
import shop.trqq.com.R;
import shop.trqq.com.UserManager;
import shop.trqq.com.adapter.GridviewGoodsAdapter;
import shop.trqq.com.bean.GoodsBean;
import shop.trqq.com.bean.Mb_SlidersBean;
import shop.trqq.com.ui.Base.BaseActivity;
import shop.trqq.com.ui.Base.UIHelper;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.ImageLoadUtils;
import shop.trqq.com.util.ToastUtils;
import shop.trqq.com.util.YkLog;
import shop.trqq.com.widget.MyGridView;

/**
 * �����������
 */
public class StoreProductActivity extends BaseActivity {
    // ����������
    private TextView mHeadTitleTextView;
    private Context mContext;
    private Gson gson;
    private MyGridView recommended_goods_list;
    private MyGridView new_goods_list;
    private TextView new_goods_list_title;
    private TextView recommended_goods_list_title;
    private List<GoodsBean> newGoodsList;
    private List<GoodsBean> recommendedGoodsList;
    private GridviewGoodsAdapter newGoodsAdapter;
    private GridviewGoodsAdapter recommendedGoodsAdapter;
    private TextView goods_all_text;
    private TextView storename;
    private ImageView storeIcon;
    private ImageView storebg;
    private TextView new_nogoods;
    private TextView recommended_nogoods;
    private TextView store_focus;
    private TextView store_share;
    private TextView store_tel;
    private TextView store_QQ;
    private String store_name, store_id, store_phone, store_qq, member_id, member_name;
    private Boolean isFavoritesFlag = false;
    // ���ؽ���Activity
    private ProgressActivity progressActivity;

    private InfiniteIndicatorLayout indicator;

    // ����������Activity��������³�Ա����
    private final UMSocialService mController = UMServiceFactory
            .getUMSocialService("com.umeng.share");
    private ImageView mImageBack;
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_store);
        mContext = this;
        gson = new Gson();
        Intent intent = getIntent();
        store_id = intent.getStringExtra("store_id");
        recommendedGoodsList = new ArrayList<GoodsBean>();
        newGoodsList = new ArrayList<GoodsBean>();
        mHeadTitleTextView = (TextView) findViewById(R.id.head_title_textView);
        mHeadTitleTextView.setTextSize(18);

        mImageBack = (ImageView) findViewById(R.id.title_back);
        mImageBack.setVisibility(View.VISIBLE);
        mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        initViews();
    }

    private void initViews() {
        progressActivity = (ProgressActivity) findViewById(R.id.StoreProduct_progress);
        indicator = (InfiniteIndicatorLayout) findViewById(R.id.indicator_store_image);
        recommended_goods_list = (MyGridView) findViewById(R.id.recommended_goods_list_GridView);
        new_goods_list = (MyGridView) findViewById(R.id.new_goods_list_GridView);
        goods_all_text = (TextView) findViewById(R.id.goods_all_text);
        new_nogoods = (TextView) findViewById(R.id.new_goods_list_nogoods);
        recommended_nogoods = (TextView) findViewById(R.id.recommended_goods_list_nogoods);
        // ��������
        storename = (TextView) findViewById(R.id.storename);
        // ����ͼƬ
        storeIcon = (ImageView) findViewById(R.id.storeIcon);
        // ����ͼƬ
        storebg = (ImageView) findViewById(R.id.storeBg);
        // �����ղ�
        store_focus = (TextView) findViewById(R.id.store_Favorites);
        store_share = (TextView) findViewById(R.id.store_share);
        store_tel = (TextView) findViewById(R.id.store_tel);
        store_QQ = (TextView) findViewById(R.id.store_QQ);
        // �Ƽ���Ʒ������
        recommendedGoodsAdapter = new GridviewGoodsAdapter(mContext,
                GridviewGoodsAdapter.TYPE_STOREGOODS);
        // ��Ʒ�Ƽ���Ʒ������ �� ������һ������
        newGoodsAdapter = new GridviewGoodsAdapter(mContext,
                GridviewGoodsAdapter.TYPE_STOREGOODS);

        recommended_goods_list.setAdapter(recommendedGoodsAdapter);
        new_goods_list.setAdapter(newGoodsAdapter);
        // �������������Ʒ
        loadOnlineStoreData();

        if (UserManager.isLogin()) {
            isFavorites();
        }

        // ������ظ�����Ʒ ��ť
        goods_all_text.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                UIHelper.showShop(mContext, "", "", store_id, "");
            }
        });
        store_focus.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (isFavoritesFlag) {
                    delFavoritesData();
                } else {
                    addFavoritesData();
                }
            }
        });

		/*
         * // ���÷�������
		 * mController.setShareContent("̩���̳�,���й����Լ����̳ǣ�http://shop.trqq.com/wap/"
		 * ); // ���÷���ͼƬ, ����2ΪͼƬ��url��ַ mController .setShareMedia(new
		 * UMImage(StoreProductActivity.this,
		 * "http://shop.trqq.com/data/upload/shop/common/04995400822795037.jpg"
		 * ));
		 */
        // ͨ��ĳ����ť���������������������
        mController.getConfig().removePlatform(SHARE_MEDIA.RENREN,
                SHARE_MEDIA.DOUBAN);
        store_share.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mController.openShare(StoreProductActivity.this, false);
            }
        });

        store_tel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (store_phone.length() >= 5 && store_phone != null) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + store_phone));
                    startActivity(intent);
                }
            }
        });

    }

    /**
     * ����wpa�Ự
     */
/*	private void onClickStartWPA(String qquin) {
                        if (!"".equals(qquin)) {
                        	Tencent mTencent=Tencent.createInstance("222222", mContext);
                    int ret = mTencent.startWPAConversation(mContext, qquin, "");
                            if (ret != 0) {
                                Toast.makeText(getApplicationContext(),
                                        "start WPA conversation failed. error:" + ret,
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                        	
      
	}*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** ʹ��SSO��Ȩ����������´��� */
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
                requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    /**
     * @return
     * @�������� : ���΢��ƽ̨����
     */
    private void addWXPlatform() {
        // �ر�ע�⣺����ʱ��Ҫ������ǩ����apk�ļ����ԣ����򽫲�����������΢��Ҫ��Ӧǩ��MD5
        // ע�⣺��΢����Ȩ��ʱ�򣬱��봫��appSecret
        // wx967daebe835fbeac������΢�ſ���ƽ̨ע��Ӧ�õ�AppID, ������Ҫ�滻����ע���AppID
        String appId = "wxe33f85af5890c21d";
        String appSecret = "d4624c36b6795d1d99dcf0547af5443d";
        // ���΢��ƽ̨
        UMWXHandler wxHandler = new UMWXHandler(StoreProductActivity.this,
                appId, appSecret);
        wxHandler.addToSocialSDK();

        // ֧��΢������Ȧ
        UMWXHandler wxCircleHandler = new UMWXHandler(
                StoreProductActivity.this, appId, appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
    }

    private void addQQQZonePlatform() {
        String appId = "1104863114";
        String appKey = "8zATFnyrYyqY78cg";
        // ���QQ֧��, ��������QQ�������ݵ�target url
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(
                StoreProductActivity.this, appId, appKey);
        qqSsoHandler.setTargetUrl("http://shop.trqq.com/wap/");
        qqSsoHandler.addToSocialSDK();

        // ���QZoneƽ̨
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(
                StoreProductActivity.this, appId, appKey);
        qZoneSsoHandler.addToSocialSDK();
    }

    /**
     * ���ݲ�ͬ��ƽ̨���ò�ͬ�ķ�������</br>
     */
    private void setShareContent() {
        String storeurl = "http://shop.trqq.com/wap/tmpl/product_store.html?store_id="
                + store_id;
        UMImage urlImage = new UMImage(StoreProductActivity.this,
                "http://shop.trqq.com/data/upload/shop/common/04995400822795037.jpg");
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareContent("�ҷ���̩���̳ǵ�" + store_name
                + "�ǳ�������ֵ���ղء�http://shop.trqq.com/wap/");
        weixinContent.setTitle("̩���̳ǵ��̣�" + store_name);
        weixinContent.setTargetUrl(storeurl);
        weixinContent.setShareMedia(urlImage);
        mController.setShareMedia(weixinContent);

        // ��������Ȧ���������
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent("�ҷ���̩���̳ǵ�" + store_name
                + "�ǳ�������ֵ���ղء�http://shop.trqq.com/wap/");
        circleMedia.setTitle("̩���̳ǵ��̣�" + store_name);
        circleMedia.setShareMedia(urlImage);
        // circleMedia.setShareMedia(uMusic);
        // circleMedia.setShareMedia(video);
        circleMedia.setTargetUrl(storeurl);
        mController.setShareMedia(circleMedia);
        // ����QQ�ռ��������
        QZoneShareContent qzone = new QZoneShareContent();
        qzone.setShareContent("�ҷ���̩���̳ǵ�" + store_name
                + "�ǳ�������ֵ���ղء�http://shop.trqq.com/wap/");
        qzone.setTargetUrl(storeurl);
        qzone.setTitle("̩���̳ǵ��̣�" + store_name);
        qzone.setShareMedia(urlImage);
        // qzone.setShareMedia(uMusic);
        mController.setShareMedia(qzone);

		/*
		 * video.setThumb(new UMImage(getActivity(),
		 * BitmapFactory.decodeResource( getResources(), R.drawable.device)));
		 */

        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setShareContent("�ҷ���̩���̳ǵ�" + store_name
                + "�ǳ�������ֵ���ղء�http://shop.trqq.com/wap/");
        qqShareContent.setTitle("̩���̳ǵ��̣�" + store_name);
        qqShareContent.setShareMedia(urlImage);
        qqShareContent.setTargetUrl(storeurl);
        mController.setShareMedia(qqShareContent);
    }

    private void loadOnlineStoreData() {
        // ���ص�������
        progressActivity.showLoading();
        RequestParams requestParams = new RequestParams();
        requestParams.add("store_id", store_id + "");
        String uri = HttpUtil.URL_STORE_DETAIL;
        HttpUtil.get(uri, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                try {
                    String jsonString = new String(responseBody);
                    YkLog.i("storeProduct",jsonString);
                    try {
                        YkLog.t("StoreProductActivity", jsonString);
                        JSONObject jsonObjects = new JSONObject(jsonString)
                                .getJSONObject("datas");
                        JSONObject goods_list_info = jsonObjects
                                .getJSONObject("goods_list_info");
                        JSONObject goods_store = goods_list_info
                                .getJSONObject("goods_store");
                        store_name = goods_store.getString("store_name");
                        store_phone = goods_store.getString("store_phone");
                        store_qq = goods_store.getString("store_qq");
                        member_id = goods_store.getString("store_member_id");
                        member_name = goods_store.getString("store_member_name");
                        mHeadTitleTextView.setText(store_name);
                        storename.setText(store_name);
                        String imageUrl = goods_store.getString("store_label");
                        ImageLoader
                                .getInstance()
                                .displayImage(
                                        imageUrl,
                                        storeIcon,
                                        ImageLoadUtils.getoptions(),
                                        ImageLoadUtils
                                                .getAnimateFirstDisplayListener());
                        // �Ƽ�
                        String recommended_goods_list = goods_list_info
                                .getString("recommended_goods_list");
                        if (recommended_goods_list.equals("null")) {
                            recommended_nogoods.setVisibility(View.VISIBLE);
                        } else {
                            recommendedGoodsList = gson.fromJson(
                                    recommended_goods_list,
                                    new TypeToken<List<GoodsBean>>() {
                                    }.getType());

                            recommendedGoodsAdapter
                                    .setData(recommendedGoodsList);
                            recommendedGoodsAdapter.notifyDataSetChanged();
                        }
                        // ��Ʒ
                        String new_goods_list = goods_list_info
                                .getString("new_goods_list");
                        if (new_goods_list.equals("null")) {
                            new_nogoods.setVisibility(View.VISIBLE);
                        } else {
                            newGoodsList = gson.fromJson(new_goods_list,
                                    new TypeToken<List<GoodsBean>>() {
                                    }.getType());

                            newGoodsAdapter.setData(newGoodsList);
                            newGoodsAdapter.notifyDataSetChanged();
                        }
                        if (store_phone.length() >= 5 && store_phone != null) {
                            Drawable tmpdrawableTop = getResources()
                                    .getDrawable(R.drawable.icon_tel);
                            store_tel.setCompoundDrawablesWithIntrinsicBounds(
                                    null, tmpdrawableTop, null, null);
                        }
/*						if (store_qq.length() >= 5 && store_qq != null) {
							Drawable tmpdrawableTop = getResources()
									.getDrawable(R.drawable.icon_qq);
							store_QQ.setCompoundDrawablesWithIntrinsicBounds(
									null, tmpdrawableTop, null, null);
						}*/
                        store_QQ.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                //�ĳ��Լ�IM��ϵ��ʽ
                                if (member_id.length() >= 1 && member_id != null) {
                                    if (UserManager.isLogin()) {
                                        UIHelper.showIMChat(mContext, member_id,
                                                member_name);
                                    } else {
                                        ToastUtils.showMessage(mContext, "���¼");
                                    }
                                }
								/*
								 * ע�⣺������ʱ��
								 * 
								 * API 11֮ǰ�� android.text.ClipboardManager API 11֮��
								 * android.content.ClipboardManager
								 */
                                // �õ������������
				/*				if (store_qq.length() >= 5 && store_qq != null) {
									ClipboardManager cmb = (ClipboardManager) mContext
											.getSystemService(Context.CLIPBOARD_SERVICE);
									cmb.setText(store_qq);
									ToastUtils.showMessageLong(mContext, "�Ѿ������˿ͷ�QQ��"
											+ store_qq + "\n�����а壬���ֶ���Ӻ���");
								}*/
                            }
                        });
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        ToastUtils.showMessage(mContext, "�õ����Ѿ��ر�");
                        Drawable emptyDrawable = getResources().getDrawable(
                                R.drawable.ic_empty);
                        progressActivity
                                .showEmpty(emptyDrawable, "�õ����Ѿ��ر�", "");
                        Handler handler = new Handler();
                        Runnable finishThread = new Runnable() {
                            public void run() {
                                finish();
                            }
                        };
                        handler.postDelayed(finishThread, 2000);
                    }
                    addWXPlatform();
                    addQQQZonePlatform();
                    setShareContent();
                    // gson��������
                    JsonObject jsonObject = new JsonParser().parse(jsonString)
                            .getAsJsonObject();
                    JsonObject data = jsonObject.getAsJsonObject("datas");
                    JsonObject goodslist_info = data
                            .getAsJsonObject("goods_list_info");
                    JsonObject goodsstore = goodslist_info
                            .getAsJsonObject("goods_store");
                    if (goodsstore.get("mb_sliders").isJsonObject()) {
                        JsonObject mb_sliders = goodsstore
                                .getAsJsonObject("mb_sliders");
                        for (Entry<String, JsonElement> entry : mb_sliders
                                .entrySet()) {
                            Mb_SlidersBean bean = new Mb_SlidersBean();
                            bean = gson.fromJson(entry.getValue(),
                                    Mb_SlidersBean.class);
                            final DefaultSliderView textSliderView = new DefaultSliderView(
                                    mContext);

                            textSliderView.getBundle().putSerializable("extra",
                                    bean);
                            // ���ز���ר��
                            String uri = bean.getImgUrl();
                            //uri = uri.replace("127.0.0.1", "172.25.25.1");
                            textSliderView
                                    .image(uri)
                                    .setScaleType(BaseSliderView.ScaleType.Fit)
                                    .setOnSliderClickListener(
                                            new OnSliderClickListener() {
                                                @Override
                                                public void onSliderClick(
                                                        BaseSliderView slider) {
                                                    // TODO Auto-generated
                                                    // method
                                                    // stub
                                                    Mb_SlidersBean bean = (Mb_SlidersBean) textSliderView
                                                            .getBundle()
                                                            .getSerializable(
                                                                    "extra");
                                                    clickimg(bean.getType(),
                                                            bean.getLink());
                                                }
                                            });
                            indicator.addSlider(textSliderView);
                        }
                        indicator
                                .setIndicatorPosition(InfiniteIndicatorLayout.IndicatorPosition.Center_Bottom);
                        indicator.startAutoScroll();
                        indicator.setVisibility(View.VISIBLE);
                    }
                    if (goodsstore.has("mb_title_img")) {
                        String mb_title_img = goodsstore.get("mb_title_img")
                                .getAsString();
                        ImageLoader
                                .getInstance()
                                .displayImage(
                                        mb_title_img,
                                        storebg,
                                        ImageLoadUtils.getoptions(),
                                        ImageLoadUtils
                                                .getAnimateFirstDisplayListener());
                    }
                    progressActivity.showContent();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                mHeadTitleTextView.setText("���翪��С��");
                Drawable errorDrawable = getResources().getDrawable(
                        R.drawable.wifi_off);
                progressActivity.showError(errorDrawable, "���翪��С��",
                        "���Ӳ������磬��ȷ��һ���������翪�أ����߷�����������æ�����Ժ�����", "��������",
                        new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                progressActivity.showLoading();
                                newGoodsList.clear();
                                recommendedGoodsList.clear();
                                loadOnlineStoreData();
                            }
                        });
            }

            @Override
            public void onFinish() {
                super.onFinish();
                // ���ܳɹ�����ʧ�ܣ���Ҫ���������رյ�

            }

        });
    }

    private void clickimg(String type, String data) {
        if (type.equals("1")) {
            try {
                String a[] = data.split("goods_id=");
                UIHelper.showGoods_Detaill(mContext, a[1]);
            } catch (Exception e) {
                if ("".equals(data) || "http://shop.trqq.com/".equals(data) || "http://shop.trqq.com".equals(data)) {
//					ToastUtils.showMessage(mContext, "�ף������Ӳ���Ҫ��ת");	
                } else {
                    ToastUtils.showMessage(mContext, "��ת������ȷ����̩��ٷ���Ʒ");
                }
            }
        } else if (type.equals("2")) {
            UIHelper.showGoods_Detaill(mContext, data);
        }
    }

    // ��ӵ��ղؼ�
    private void addFavoritesData() {
        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        requestParams.add("store_id", store_id);
        HttpUtil.post(HttpUtil.URL_STORE_FAVORITES_ADD, requestParams,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          byte[] responseBody) {
                        try {
                            String jsonString = new String(responseBody);
                            // TestUtils.println_err(TAG,jsonString);
                            try {
                                JSONObject jsonObject = new JSONObject(
                                        jsonString).getJSONObject("datas");
                                String errStr = jsonObject.getString("error");
                                if (errStr != null) {
                                    ToastUtils.showMessage(mContext, errStr);
                                }
                            } catch (Exception e) {
                                if (new JSONObject(jsonString).getString(
                                        "datas").equals("1")) {
                                    ToastUtils.showMessage(mContext, "�ɹ��ղ�");
                                    Drawable focusdrawableTop = getResources()
                                            .getDrawable(
                                                    R.drawable.nearby_focus_on);
                                    store_focus
                                            .setCompoundDrawablesWithIntrinsicBounds(
                                                    null, focusdrawableTop,
                                                    null, null);
                                    store_focus.setText("�ѹ�ע");
                                    isFavoritesFlag = true;
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {
                        // TODO Auto-generated method stub
                        ToastUtils.showMessage(getApplicationContext(),
                                R.string.get_informationData_failure);
                    }
                });
    }

    // ɾ���ղ�
    private void delFavoritesData() {
        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        requestParams.add("store_id", store_id);
        HttpUtil.post(HttpUtil.URL_STORE_FAVORITES_DEL, requestParams,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          byte[] responseBody) {
                        try {
                            String jsonString = new String(responseBody);
                            // TestUtils.println_err(TAG,jsonString);
                            try {
                                JSONObject jsonObject = new JSONObject(
                                        jsonString).getJSONObject("datas");
                                String errStr = jsonObject.getString("error");
                                if (errStr != null) {
                                    ToastUtils.showMessage(mContext, errStr);
                                }
                            } catch (Exception e) {
                                if (new JSONObject(jsonString).getString(
                                        "datas").equals("1")) {
                                    ToastUtils.showMessage(mContext, "�ɹ�ȡ���ղ�");
                                    Drawable focusdrawableTop = getResources()
                                            .getDrawable(
                                                    R.drawable.nearby_focus_off);
                                    store_focus
                                            .setCompoundDrawablesWithIntrinsicBounds(
                                                    null, focusdrawableTop,
                                                    null, null);
                                    store_focus.setText("��ע");
                                    isFavoritesFlag = false;
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {
                        // TODO Auto-generated method stub
                        ToastUtils.showMessage(mContext,
                                R.string.get_informationData_failure);
                    }
                });
    }

    // �Ƿ��Ѿ��ղ�
    private void isFavorites() {
        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        requestParams.add("store_id", store_id);
        HttpUtil.post(HttpUtil.URL_STORE_FAVORITES_FLAG, requestParams,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          byte[] responseBody) {
                        try {
                            String jsonString = new String(responseBody);
                            try {
                                JSONObject jsonObject = new JSONObject(
                                        jsonString).getJSONObject("datas");
                                isFavoritesFlag = false;
                                String errStr = jsonObject.getString("error");
                                if (errStr != null) {
                                    // ToastUtils.showMessage(mContext, errStr);
                                }
                            } catch (Exception e) {
                                if (new JSONObject(jsonString).getString(
                                        "datas").equals("1")) {
                                    // ToastUtils.showMessage(mContext, "�Ѿ���ע");
                                    Drawable focusdrawableTop = getResources()
                                            .getDrawable(
                                                    R.drawable.nearby_focus_on);
                                    store_focus
                                            .setCompoundDrawablesWithIntrinsicBounds(
                                                    null, focusdrawableTop,
                                                    null, null);
                                    store_focus.setText("�ѹ�ע");
                                    isFavoritesFlag = true;
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {
                        // TODO Auto-generated method stub
                        ToastUtils.showMessage(getApplicationContext(),
                                R.string.get_informationData_failure);
                    }
                });
    }
}
