package shop.trqq.com.supermarket.activitys;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.baidu.mapapi.utils.DistanceUtil;
import com.vlonjatg.progressactivity.ProgressActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import shop.trqq.com.AppContext;
import shop.trqq.com.R;
import shop.trqq.com.supermarket.adapters.AddressInfoAdapter;
import shop.trqq.com.util.ToastUtils;

public class AddressFromMapActivity extends AppCompatActivity implements BDLocationListener, BaiduMap.OnMapStatusChangeListener, OnGetGeoCoderResultListener, OnGetPoiSearchResultListener {

    private static final String TAG = AddressFromMapActivity.class.getSimpleName();
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private LocationClientOption mOption;
    private boolean isFirstLoc = true;
    private Context mContext;
    private GeoCoder mSearch;
    private PoiSearch mPoiSearch;
    private ImageView mRelocation;
    private ArrayList<PoiInfo> mListData;
    private AddressInfoAdapter mAddressInfoAdapter;
    private ListView mListView;
    private EditText keyWordView;

    private String mCity;
    private ProgressActivity mProgressActivity;
    private LatLng mCenter;
    private PoiInfo mPoiInfo;
    private String mArea;
    private ImageView mMarketReLocation;
    private LatLng mMarketLatLng;
    private String mProvince;
    private boolean mHasSelected;
    private TextView mDistance;
    private boolean mHasLatlng;
    private LatLng mMyLatlng;
    private String mFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_from_map);
        initView();
        initData();

        setData();
        setListener();
    }

    private void initView() {

        mMapView = (MapView) findViewById(R.id.address_map);
        mRelocation = (ImageView)findViewById(R.id.relocation);

        mDistance = (TextView) findViewById(R.id.distance);
        mMarketReLocation = (ImageView) findViewById(R.id.market_location);
        mListView = (ListView) findViewById(R.id.address_list);
        keyWordView = (EditText)findViewById(R.id.search_tv);
        mProgressActivity = (ProgressActivity)findViewById(R.id.address_progress);

    }


    private void initData() {

        mContext = AddressFromMapActivity.this;

        Intent intent = getIntent();
        mFrom = intent.getStringExtra("from");
        // 用户是否手动选择了地址
        mHasSelected = intent.getBooleanExtra("hasSelected", false);
        mArea = intent.getStringExtra("area");
        // 用户时候已经选过了
        mHasLatlng = intent.getBooleanExtra("hasLatlng", false);
        mMyLatlng= intent.getParcelableExtra("latlng");

        Log.d(TAG,"没有经纬度"+mArea+"|"+mHasSelected+"|已经有了经纬度"+mHasLatlng);

        if (mMapView != null) {
            mMapView.showScaleControl(false);
            mMapView.showZoomControls(false);
            mBaiduMap = mMapView.getMap();
        }
        // 可以设置我的位置
        mBaiduMap.setMyLocationEnabled(true);
        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();

        // POI初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();

        mLocationClient = new LocationClient(this);

        // 初始化定位参数
        initLocationOption();

        // 下方地址列表
        mListData = new ArrayList<>();
        mAddressInfoAdapter = new AddressInfoAdapter(this,mListData);
        mListView.setAdapter(mAddressInfoAdapter);

        // 超市的位置
        mMarketLatLng = new LatLng(AppContext.marketLatitude,AppContext.marketLongitude);
        Marker marker = addMarker(AppContext.marketLatitude,AppContext.marketLongitude);
        marker.setTitle("万能居超市");
    }

    // 初始化定位的参数
    private void initLocationOption() {

        mOption = new LocationClientOption();

        mOption.setCoorType("bd09ll");
        // 在定位的时候，返回地址信息
        mOption.setIsNeedAddress(true);
        mOption.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        mOption.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mOption.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        mOption.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        mOption.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        // 设置定位模式
        mOption.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);

    }

    // 添加超市覆盖物
    private Marker addMarker(Double marketLatitude, Double marketLongitude) {

        MarkerOptions options = new MarkerOptions();

        options.position(new LatLng(marketLatitude,marketLongitude));
            // 超市的位置
        options.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_location_box));

        return (Marker) mBaiduMap.addOverlay(options);
    }


    // 设置数据
    private void setData() {

        mBaiduMap.getUiSettings().setCompassEnabled(false);

        if (mHasSelected) {   // 如果用户手动选择了地址
            Log.d(TAG, "手动选择");

            String[] split = mArea.split(" ");
            int length = split.length;
            String city = split[length - 2];
            String address = split[length - 1];
            mCity = city;
            if (mHasLatlng) {
                // 点击定位到自己的位置，定位成功 更新地图
                MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(mMyLatlng, 16);
                mBaiduMap.animateMapStatus(update);
                setSearchData(mMyLatlng);
            } else {
                // 反编码并定位到用户选择的地方
                GeoCodeOption geoCodeOption = new GeoCodeOption().city(city).address(address);
                // 编码得到经纬度
                mSearch.geocode(geoCodeOption);

            }
        } else {
            Log.d(TAG, "开始定位");
            // 开启定位
            startLocation();
        }

    }
        //  开始定位
    private void startLocation() {

        mLocationClient.setLocOption(mOption);
        mLocationClient.start();
    }

    // 定位成功的回调方法
    @Override
    public void onReceiveLocation(BDLocation bdLocation) {

        mLocationClient.stop();

        int locType = bdLocation.getLocType();

        switch (locType) {
            case BDLocation.TypeGpsLocation:
            case BDLocation.TypeNetWorkLocation:
            case BDLocation.TypeOffLineLocation:

                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(bdLocation.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(100).latitude(bdLocation.getLatitude())
                        .longitude(bdLocation.getLongitude()).build();
                mBaiduMap.setMyLocationData(locData);

                if (isFirstLoc) {//首次定位
                    isFirstLoc = false;

                    mCity = bdLocation.getCity();
                    mProvince = bdLocation.getProvince();
                    // 得到定位的纬度
                    double latitude = bdLocation.getLatitude();
                    // 得到位置的经度
                    double longitude = bdLocation.getLongitude();

                    LatLng latLng = new LatLng(latitude, longitude);

                    // 点击定位到自己的位置，定位成功 更新地图
                    MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(latLng, 18);
                    mBaiduMap.animateMapStatus(update);
                    Log.d(TAG,"定位成功");

                    // 设置搜索的数据
                    setSearchData(latLng);
//                    mSearch.reverseGeoCode(new ReverseGeoCodeOption()
//                            .location(latLng));
                }
                break;

            default:
                ToastUtils.showMessage(mContext,"定位失败请重试");
                break;
        }
    }

   // 设置监听器
    private void setListener() {

        // 点击定位到超市 ImageView 定位到超市的位置
        mMarketReLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCity = "湛江市";
                MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(mMarketLatLng,16);
                mBaiduMap.animateMapStatus(update);
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mPoiInfo = mListData.get(i);
                // 反编码得到详细的 地址信息回传
                mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(mPoiInfo.location));
            }
        });

        // 地图状态改变的监听器
        mBaiduMap.setOnMapStatusChangeListener(this);
        // 地理编码监听
        mSearch.setOnGetGeoCodeResultListener(this);
        // Poi检索监听
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        // 设置监听
        mLocationClient.registerLocationListener(this);

        keyWordView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,AddressSearchActivity.class);
                if (mFrom != null) {
                    intent.putExtra("from",mFrom);
                }
                if (mCity != null) {        // 城市
                    intent.putExtra("city",mCity);
                    intent.putExtra("center",mCenter);
                }
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_in,R.anim.push_left_out);
            }
        });
    }


    // 当地图状态改变时开始调用
    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus) {

    }

    @Override
    public void onMapStatusChange(MapStatus mapStatus) {

    }

    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {

        Log.d(TAG, "地图改变");
        mCenter = mapStatus.bound.getCenter();

        // 设置搜索的数据
        setSearchData(mCenter);
    }

    // 设置搜索的数据
    private void setSearchData(LatLng center) {
        // 设置距离超市的距离
        setDistance(center);
//         Poi 检索搜索附近
        searchNeayBy(center);

    }

    // 设置距离超市的距离
    private void setDistance(LatLng latLng) {
        double size = DistanceUtil.getDistance(mMarketLatLng, latLng);
        String distance = null;
        if(size>=0){
            if (size < 1000) {
                distance = (int) size + "m";
            } else {
                size = size / 1000;
                DecimalFormat df = new DecimalFormat("0.00");
                distance = df.format(size) + " km";
            }
            mDistance.setText(" "+distance);
        }

    }
    /**
     * 搜索周边
     * @param center
     */
    private void searchNeayBy(LatLng center){

        PoiNearbySearchOption poiNearbySearchOption = new PoiNearbySearchOption();

        poiNearbySearchOption.keyword("小区");
        poiNearbySearchOption.sortType(PoiSortType.distance_from_near_to_far);
        poiNearbySearchOption.location(center);
        poiNearbySearchOption.radius(1000);  // 检索半径，单位是米
        poiNearbySearchOption.pageCapacity(20);  // 默认每页10条

        mProgressActivity.showLoading();
        mPoiSearch.searchNearby(poiNearbySearchOption);  // 发起附近检索请求
    }

    /*
	 * 接受周边地理位置结果
	 */
    @Override
    public void onGetPoiResult(PoiResult result) {
        // 检查到结果以后显示内容
        if(mProgressActivity.isLoading()){
            mProgressActivity.showContent();
        }

        if(result.error == SearchResult.ERRORNO.NETWORK_ERROR){
            // 显示网络异常错误
            showNetWorkError();
        }

        // 获取POI检索结果
        if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {// 没有找到检索结果
            return;
        }

        if (result.error == SearchResult.ERRORNO.NO_ERROR) {// 检索结果正常返回
            if(result != null){
                if(result.getAllPoi()!= null && result.getAllPoi().size()>0){
                    List<PoiInfo> allPoi = result.getAllPoi();
                    for (PoiInfo poiInfo : allPoi) {
                        String name = poiInfo.name;
                        String address = poiInfo.address;
                    }
                    mAddressInfoAdapter.addDatas(result.getAllPoi());

                }
            }
        }
    }

    // 显示网络异常错误
    private void showNetWorkError() {
        Drawable errorDrawable = ContextCompat.getDrawable(mContext, R.drawable.wifi_off);
        mProgressActivity.showError(errorDrawable,"您的手机网络不太顺畅哦",
                "","重新加载",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mProgressActivity.showLoading();
                        // 重新加载地图
                        setReLocation();
                    }
                });
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult result) {

    }


    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (mLocationClient != null) {
            // 退出时销毁定位
            mLocationClient.stop();
        }
        // 关闭定位图层
        mPoiSearch.destroy();
        mSearch.destroy();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

    //正向搜索
    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {

        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {

            return;
        }
        Log.d(TAG, "编码成功");
        LatLng location = result.getLocation();

        MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(location,14);
        mBaiduMap.animateMapStatus(update);

        // 设置搜索的数据
        setSearchData(location);
    }

    //反向搜索根据经纬度去的到具体地址信息
    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        // 未找到结果
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {

            return;
        }
        ReverseGeoCodeResult.AddressComponent addressDetail = result.getAddressDetail();
        String address1 = result.getAddress();
        if (mPoiInfo != null) {
            Intent intent = new Intent();
            intent.putExtra("poiInfo", mPoiInfo);
            intent.putExtra("address", addressDetail);
            setResult(RESULT_OK, intent);
            finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
        }
    }

    public void click(View view) {
        switch (view.getId()) {
            case R.id.relocation:
                setReLocation();
                break;
        }
    }

    // 重新定位自己的位置并且 地图以自己的位置为中心
    private void setReLocation() {

        isFirstLoc = true;   // 可以重新定位
        mMapView.setClickable(false);// 不让底层的mapview截获点击事件

        mLocationClient.setLocOption(mOption);
        mLocationClient.start();// 重新定位一下
    }

    public void imageClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                // 按返回键返回
                finish();
                overridePendingTransition(R.anim.push_left_in,R.anim.push_right_out);
                break;
        }
    }
}
