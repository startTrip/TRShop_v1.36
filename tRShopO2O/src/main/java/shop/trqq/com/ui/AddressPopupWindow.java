package shop.trqq.com.ui;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import shop.trqq.com.R;
import shop.trqq.com.UserManager;
import shop.trqq.com.bean.AddressBean;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.YkLog;

/**
 * 地址选择弹窗
 */
public class AddressPopupWindow extends PopupWindow implements OnClickListener,
        OnWheelChangedListener, OnWheelScrollListener {
    private static final String TAG = "AddressPopupWindow";
    private WheelView mViewProvince;// 省
    private WheelView mViewCity;// 市
    private WheelView mViewDistrict;// 区县
    private Button mBtnConfirm;
    /**
     * 当前省的名称
     */
    protected String mCurrentProviceName;
    /**
     * 当前市的名称
     */
    protected String mCurrentCityName;
    /**
     * 当前区的名称
     */
    protected String mCurrentDistrictName = "";

    private Gson gson;
    private List<AddressBean> provinceList;
    private List<AddressBean> cityList;
    private List<AddressBean> districtList;
    private Context mContext;
    private Boolean first = true;
    private String[] mProvinceDatas, cities, areas;
    private String city_id, mArea_id;

    private int mPosition;


    protected void initProvinceDatas() {
        provinceList = new ArrayList<AddressBean>();
        cityList = new ArrayList<AddressBean>();
        districtList = new ArrayList<AddressBean>();
        loadingGetCityData("0", 1);
    }

    /*
     * @Override protected void onCreate(Bundle savedInstanceState) {
     * super.onCreate(savedInstanceState);
     * setContentView(R.layout.activity_address); gson=new Gson();
     * mContext=this; setUpViews();
     *
     * setUpData(); }
     */
    private View rootView; // 总的布局
    private OnOptionsSelectListener optionsSelectListener;

    public AddressPopupWindow(Context context) {
        super(context);
        this.setWidth(LayoutParams.FILL_PARENT);
        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setBackgroundDrawable(new BitmapDrawable());// 这样设置才能点击屏幕外dismiss窗口
        this.setOutsideTouchable(true);
        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        rootView = mLayoutInflater.inflate(R.layout.activity_address, null);
        setContentView(rootView);

        gson = new Gson();
        mContext = context;
        setUpViews();

        setUpData();
        setUpListener();

    }

    public interface OnOptionsSelectListener {
        void onOptionsSelect(String options1, String option2,
                                    String options3);
    }

    public void setOnoptionsSelectListener(
            OnOptionsSelectListener optionsSelectListener) {
        this.optionsSelectListener = optionsSelectListener;
    }

    private void setUpViews() {
        mViewProvince = (WheelView) rootView.findViewById(R.id.id_province);
        mViewCity = (WheelView) rootView.findViewById(R.id.id_city);
        mViewDistrict = (WheelView) rootView.findViewById(R.id.id_district);
        mBtnConfirm = (Button) rootView.findViewById(R.id.btn_confirm);
    }

    private void setUpListener() {
        // 添加change事件
        mViewProvince.addChangingListener(this);
        // 添加change事件
        mViewCity.addChangingListener(this);
        // 添加change事件
        mViewDistrict.addChangingListener(this);

        mViewProvince.addScrollingListener(this);
        mViewCity.addScrollingListener(this);
        mViewDistrict.addScrollingListener(this);
        // 添加onclick事件
        mBtnConfirm.setOnClickListener(this);
    }

    private void setUpData() {
        // 设置可见条目数量
        mViewProvince.setVisibleItems(7);
        mViewCity.setVisibleItems(7);
        mViewDistrict.setVisibleItems(7);
        // String[] load=new String[7];
        String[] load = {"正在加载", "正在加载", "正在加载", "正在加载", "正在加载", "正在加载",
                "正在加载"};
        mProvinceDatas = cities = areas = load;
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(mContext,
                mProvinceDatas));
        mViewCity
                .setViewAdapter(new ArrayWheelAdapter<String>(mContext, cities));
        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(mContext,
                areas));
        initProvinceDatas();

        // loadingGetCityData(provinceList.get(0).getArea_id(),2);
        /*
		 * updateCities(); updateAreas();
		 */

    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub
        if (wheel == mViewProvince) {
            mPosition = newValue;
        }else if(wheel == mViewCity){
            mPosition = newValue;
        }else if (wheel == mViewDistrict){
            mPosition = newValue;
        }
        Log.d(TAG, "onChanged: ");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                if (optionsSelectListener != null) {
                    Log.d(TAG, "onClick: "+mCurrentProviceName + " "
                                    + mCurrentCityName + " " + mCurrentDistrictName +"城市"+
                            city_id +"地区"+mArea_id);
                    optionsSelectListener.onOptionsSelect(mCurrentProviceName + " "
                                    + mCurrentCityName + " " + mCurrentDistrictName,
                            city_id, mArea_id);
                    dismiss();
                }
                return;
			/*
			 * Intent it = new Intent();
			 * it.putExtra("result",mCurrentProviceName
			 * +" "+mCurrentCityName+" "+mCurrentDistrictName);
			 * it.putExtra("city_id",city_id); it.putExtra("area_id",mArea_id);
			 * setResult(Activity.RESULT_OK, it); // showSelectedResult();
			 * finish(); break; default: break;
			 */
        }
    }

    private void showSelectedResult() {
        Toast.makeText(
                mContext,
                "当前选中:" + mCurrentProviceName + "," + mCurrentCityName + ","
                        + mCurrentDistrictName, Toast.LENGTH_SHORT).show();
    }

    private void loadingGetCityData(String area_id, final int type) {

        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        requestParams.add("area_id", area_id);
        HttpUtil.post(HttpUtil.URL_GET_CITY, requestParams,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          byte[] responseBody) {
                        try {

                            String jsonString = new String(responseBody);
                            YkLog.t("loadingGetCityData"
                                    + jsonString);
                            // JSONObject jsonObject = new
                            // JSONObject(jsonString);
                            JSONObject JSONdata = new JSONObject(jsonString)
                                    .getJSONObject("datas");
                            // JSONArray jsonObjects
                            // =JSONdata.getJSONArray("area_list");
                            // String add_list =jsonObjects.toString();
                            String area_list = JSONdata.getString("area_list");
                            YkLog.t("add_list:" + area_list);
                            switch (type) {
                                case 1:
                                    provinceList = gson.fromJson(area_list,
                                            new TypeToken<List<AddressBean>>() {
                                            }.getType());
                                    break;
                                case 2:
                                    cityList = gson.fromJson(area_list,
                                            new TypeToken<List<AddressBean>>() {
                                            }.getType());
                                    break;
                                case 3:
                                    districtList = gson.fromJson(area_list,
                                            new TypeToken<List<AddressBean>>() {
                                            }.getType());
                                    break;
                            }

                            YkLog.t("provinceListname:"
                                    + provinceList.get(
                                    mViewProvince.getCurrentItem())
                                    .getArea_name());
							/*
							 * AppConfig.setSharedPreferences(mContext,
							 * "cartInfoList", cart_list);
							 */
                        } catch (Exception e) {
                            provinceList = new ArrayList<AddressBean>();
                            cityList = new ArrayList<AddressBean>();
                            districtList = new ArrayList<AddressBean>();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        switch (type) {
                            case 1:
                                if (first) {
                                    mCurrentProviceName = provinceList.get(0)
                                            .getArea_name();
                                }
							/*
							 * int pCurrent = mViewProvince.getCurrentItem();
							 * mCurrentProviceName =
							 * provinceList.get(pCurrent).getArea_name();
							 */
                                mProvinceDatas = new String[provinceList.size()];
                                for (int i = 0; i < provinceList.size(); i++) {
                                    mProvinceDatas[i] = provinceList.get(i)
                                            .getArea_name();
                                }

                                mViewProvince
                                        .setViewAdapter(new ArrayWheelAdapter<String>(
                                                mContext, mProvinceDatas));
                                // mViewProvince.invalidate();
                                loadingGetCityData(
                                        provinceList.get(
                                                mViewProvince.getCurrentItem())
                                                .getArea_id(), 2);
                                break;
                            case 2:
                                if (first) {
                                    mCurrentCityName = cityList.get(0)
                                            .getArea_name();
                                    // loadingGetCityData(cityList.get(0).getArea_id(),3);
                                }
                                int pCurrent = mViewProvince.getCurrentItem();
                                mCurrentProviceName = provinceList.get(pCurrent)
                                        .getArea_name();
                                cities = new String[cityList.size()];
                                for (int i = 0; i < cityList.size(); i++) {
                                    cities[i] = cityList.get(i).getArea_name();
                                }
                                if (cityList.size() == 0) {
                                    cities = new String[]{""};
                                }
                                mViewCity
                                        .setViewAdapter(new ArrayWheelAdapter<String>(
                                                mContext, cities));
                                mViewCity.setCurrentItem(0);

                                loadingGetCityData(
                                        cityList.get(mViewCity.getCurrentItem())
                                                .getArea_id(), 3);
                                break;
                            case 3:
                                if (first) {
                                    mCurrentDistrictName = districtList.get(0)
                                            .getArea_name();

                                    first = false;
                                }
                                int cCurrent = mViewCity.getCurrentItem();
                                mCurrentCityName = cityList.get(cCurrent)
                                        .getArea_name();
                                city_id = cityList.get(cCurrent).getArea_id();

                                if (districtList.size() == 0) {
                                    areas = new String[]{""};
                                }else {
                                    areas = new String[districtList.size()];
                                    for (int i = 0; i < districtList.size(); i++) {
                                        areas[i] = districtList.get(i).getArea_name();
                                    }
                                }

                                // System.err.println(areas[0]);
                                mCurrentDistrictName = areas[0];
                                mViewDistrict
                                        .setViewAdapter(new ArrayWheelAdapter<String>(
                                                mContext, areas));
                                mViewDistrict.setCurrentItem(0);

                                if (districtList.size() != 0)
                                    mArea_id = districtList.get(0).getArea_id();
                                // mViewDistrict.invalidate();
                                break;
                        }
                    }
                });
        // return true;
    }

    @Override
    public void onScrollingStarted(WheelView wheel) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onScrollingFinished(WheelView wheel) {
        // TODO Auto-generated method stub
        Log.d(TAG, "onScrollingFinished: ");
        if (wheel == mViewProvince) {
            loadingGetCityData(provinceList.get(mPosition).getArea_id(), 2);
        } else if (wheel == mViewCity) {
            loadingGetCityData(cityList.get(mPosition).getArea_id(), 3);
        } else if (wheel == mViewDistrict) {
            if (districtList.size() != 0){
                mArea_id = districtList.get(mPosition).getArea_id();
                mCurrentDistrictName = districtList.get(mPosition).getArea_name();
            }else {
                mArea_id = "";
            }
        }
    }

}
