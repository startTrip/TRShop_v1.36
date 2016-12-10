package shop.trqq.com.supermarket.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.utils.DistanceUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import shop.trqq.com.AppConfig;
import shop.trqq.com.AppContext;
import shop.trqq.com.R;

/**
 * 定位超市和自己的位置，描述超市配送区域和自己与超市的距离
 */
public class MarketLbsFragment extends Fragment implements BDLocationListener, BaiduMap.OnMarkerClickListener {

    private static final String TAG = MarketLbsFragment.class.getSimpleName();
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private TextView mMyAddress;
    private ImageView mReLocaation;
    private GeoCoder mSearch;
    private ImageView mMarketReLocation;
    private LatLng mMarketLatLng;
    private LatLng mLatLng;
    private LocationClientOption mOption;
    private Double mMarketLatitude;
    private Double mMarketLongitude;

    public MarketLbsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_market_lbs, container, false);
        initView(view);

        initData();

        setData();

        // 设置监听器
        setListener();
        return view;
    }

    private void initView(View view) {

        mMapView = (MapView) view.findViewById(R.id.baidu_mapview);
        mMyAddress = (TextView) view.findViewById(R.id.market_myaddress);

        mReLocaation = (ImageView)view.findViewById(R.id.market_relocation);

        mMarketReLocation = (ImageView)view.findViewById(R.id.market_location);
    }

    // 初始化数据
    private void initData() {
        if (mMapView != null) {
            mBaiduMap = mMapView.getMap();

        }
        mBaiduMap.setMyLocationEnabled(true);

        mMarketLatitude = AppContext.marketLatitude;
        mMarketLongitude = AppContext.marketLongitude;
        mMarketLatLng = new LatLng(mMarketLatitude,mMarketLongitude);

        mSearch = GeoCoder.newInstance();
        // 添加并标注超市的位置
        setMarketLocation();
    }

    // 设置数据
    private void setData() {

        mBaiduMap.getUiSettings().setCompassEnabled(false);//不显示指南针

        mLocationClient = new LocationClient(getActivity());

        mLocationClient.registerLocationListener(this);

        mOption = new LocationClientOption();

        // 设置打开GPS
        mOption.setOpenGps(true);

        mOption.setCoorType("bd09ll");

        // 在定位的时候，返回地址信息
        mOption.setIsNeedAddress(true);

        mOption.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        mOption.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mOption.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        mOption.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        mOption.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        // 设置定位模式
        mOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);

        mLocationClient.setLocOption(mOption);
        mLocationClient.start();
    }


    // 设置监听器
    private void setListener() {

        // 点击定位ImageView 定位到自己的位置
        mReLocaation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  setReLocation();
            }
        });
        // 点击定位到超市 ImageView 定位到超市的位置
        mMarketReLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(mMarketLatLng,14);
                mBaiduMap.animateMapStatus(update);
            }
        });
        mBaiduMap.setOnMarkerClickListener(this);
    }

    // 重新定位自己的位置并且 地图以自己的位置为中心
    private void setReLocation() {

        mMapView.setClickable(false);// 不让底层的mapview截获点击事件

        if (mOption.isOpenGps()) {
            Log.d(TAG, "setReLocation: ");
            mOption.setOpenGps(false);
        }
        mLocationClient.setLocOption(mOption);
        mLocationClient.start();// 重新定位一下
    }



    private Marker currentLocationMarker;

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {

        mLocationClient.stop();
        int locType = bdLocation.getLocType();

        Log.d(TAG, "onReceiveLocation: "+locType);

        switch (locType) {
            case BDLocation.TypeGpsLocation:
            case BDLocation.TypeNetWorkLocation:
            case BDLocation.TypeOffLineLocation:

                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(bdLocation.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(360).latitude(bdLocation.getLatitude())
                        .longitude(bdLocation.getLongitude()).build();
                mBaiduMap.setMyLocationData(locData);

                String city = bdLocation.getCity();
                Intent intent = new Intent("city");
                intent.putExtra("city",city);
                getActivity().sendBroadcast(intent);


                String addrStr = bdLocation.getAddrStr();
                Log.d(TAG, "onReceiveLocation: "+addrStr);

                // 得到定位的纬度
                double latitude = bdLocation.getLatitude();
                // 得到位置的经度
                double longitude = bdLocation.getLongitude();


                LatLng latLng = new LatLng(latitude, longitude);

                setDistance(latLng);

                if (currentLocationMarker == null) {
                    // 添加标注物
                    currentLocationMarker = addMarker(latitude,longitude,1);
                    currentLocationMarker.setTitle("我的位置");

                }else {
                    // 移动标注物
                    currentLocationMarker.setPosition(latLng);
                }
                // 点击定位到自己的位置，定位成功 更新地图
                MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(latLng,17);
                mBaiduMap.animateMapStatus(update);

                break;

            default:
                Log.d(TAG, "onReceiveLocation: "+"定位失败");
                mMyAddress.setText("定位失败");
                break;
        }
    }

    private void setDistance(LatLng latLng) {

        double size = DistanceUtil.getDistance(mMarketLatLng, latLng);

        // 将距离存储下来
        AppConfig.setSharedPreferences(getContext(),"distance",(float)size);

        String distance = null;
        if(size>=0){

            if (size < 1000) {

                distance = (int) size + "m";
            } else {
                size = size / 1000;
                DecimalFormat df = new DecimalFormat("0.00");
                distance = df.format(size) + " km";
            }
            mMyAddress.setText(" 距超市 "+distance);
        }

    }
    private void setMarketLocation() {
        // 设定万能居超市的地址

        Marker marker = addMarker(mMarketLatitude,mMarketLongitude, 0);
        marker.setTitle("万能居超市");

        //定义地图状态
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(mMarketLatLng,13));

        //添加多边形区域
        addRange();
    }

    // 添加矩形区域
    private void addRange() {
        // 添加多边形
        LatLng pt1 = new LatLng(21.28313,110.403193);
        LatLng pt2 = new LatLng(21.315897,110.385442);
        LatLng pt3 = new LatLng(21.3229,110.375956);
        LatLng pt4 = new LatLng(21.313203,110.361727);
        LatLng pt5 = new LatLng(21.300272,110.353678);
        LatLng pt6 = new LatLng(21.281951,110.34606);
        LatLng pt7 = new LatLng(21.275485,110.339305);
        LatLng pt8 = new LatLng(21.275619,110.339305);
        LatLng pt9 = new LatLng(21.274542,110.335065);
        LatLng pt10 = new LatLng(21.27023,110.337077);
        LatLng pt11 = new LatLng(21.26282,110.344839);
        LatLng pt12 = new LatLng(21.254196,110.353606);
        LatLng pt13 = new LatLng(21.236948,110.376459);
        LatLng pt14 = new LatLng(21.233175,110.405205);
        LatLng pt15 = new LatLng(21.24409,110.431364);
        List<LatLng> pts = new ArrayList<LatLng>();
        pts.add(pt1);
        pts.add(pt2);
        pts.add(pt3);
        pts.add(pt4);
        pts.add(pt5);
        pts.add(pt6);
        pts.add(pt7);
        pts.add(pt8);
        pts.add(pt9);
        pts.add(pt10);
        pts.add(pt11);
        pts.add(pt12);
        pts.add(pt13);
        pts.add(pt14);
        pts.add(pt15);
        OverlayOptions ooPolygon = new PolygonOptions().points(pts)
                .stroke(new Stroke(1, 0xAAFF0000)).fillColor(0x30ffff00);

        mBaiduMap.addOverlay(ooPolygon);
    }

    /**
     * 添加 标注
     * @param latitude
     * @param longitude
     * @param i   0 代表超市的位置， 1 代表自己的位置
     * @return
     */
    private Marker addMarker(double latitude, double longitude,int i) {

        MarkerOptions options = new MarkerOptions();

        options.position(new LatLng(latitude,longitude));
        if(i==0){
            // 超市的位置
            options.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_location_box));

        }else if(i==1){
            // 我的位置
            options.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_location_fresh));
        }
        return (Marker) mBaiduMap.addOverlay(options);
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
       
        final String title = marker.getTitle();

        if (title != null) {

            final View view = LayoutInflater.from(getActivity()).inflate(R.layout.market_address, null);
            final TextView name= (TextView) view.findViewById(R.id.title);
            final TextView info= (TextView) view.findViewById(R.id.title_info);
            // 根据 经纬度得到地址信息
            mSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
                @Override
                public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

                }

                @Override
                public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                    name.setText(title);

                    if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                        //没有找到检索结果
                        info.setText("");
                    }else {
                        info.setText(result.getAddress());
                    }
                    //获取反向地理编码结果
                    InfoWindow infoWindow = new InfoWindow(view,marker.getPosition(),-100);
                    mBaiduMap.showInfoWindow(infoWindow);
                }
            });

            mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(marker.getPosition()));
        }

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {

        mLocationClient.unRegisterLocationListener(this);

        mLocationClient.stop();
        mMapView.onDestroy();

        mSearch.destroy();
        mMapView = null;
        super.onDestroy();
    }
}
