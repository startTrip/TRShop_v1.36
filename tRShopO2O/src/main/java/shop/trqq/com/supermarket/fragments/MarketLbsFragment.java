package shop.trqq.com.supermarket.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.Address;
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

import java.util.ArrayList;
import java.util.List;

import shop.trqq.com.R;
import shop.trqq.com.util.YkLog;

/**
 * ��λ���к��Լ���λ�ã�������������������Լ��볬�еľ���
 */
public class MarketLbsFragment extends Fragment implements BDLocationListener, BaiduMap.OnMarkerClickListener {

    private static final String TAG = MarketLbsFragment.class.getSimpleName();
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private TextView mMyAddress;
    private ImageView mReLocaation;
    private boolean isClickLoc;
    private GeoCoder mSearch;
    private ImageView mMarketReLocation;
    private LatLng mMarketLatLng;
    private LatLng mLatLng;

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

        // ���ü�����
        setListener();
        return view;
    }

    // �������
    private void setData() {

        mBaiduMap.getUiSettings().setCompassEnabled(false);//����ʾָ����

        mLocationClient = new LocationClient(getActivity());

        mLocationClient.registerLocationListener(this);

        LocationClientOption option = new LocationClientOption();

        // ���ô�GPS
        option.setOpenGps(true);

        option.setCoorType("bd09ll");

        // �ڶ�λ��ʱ�򣬷��ص�ַ��Ϣ
        option.setIsNeedAddress(true);

        // ���ö�λģʽ
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);

        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    // ��Ӿ�������
    private void addRange() {
        // ��Ӷ����
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
                .stroke(new Stroke(1, 0xAAFF0000)).fillColor(0xAAffff00);

        mBaiduMap.addOverlay(ooPolygon);
    }

    // ���ü�����
    private void setListener() {

        // �����λImageView ��λ���Լ���λ��
        mReLocaation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YkLog.i("congxin","congxindinwei");
                  setReLocation();
            }
        });
        // �����λ������ ImageView ��λ�����е�λ��
        mMarketReLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng latLng = new LatLng(21.281833,110.396756);
                // �����λ���Լ���λ�ã���λ�ɹ� ���µ�ͼ
                MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(latLng,16);
                mBaiduMap.animateMapStatus(update);
            }
        });
        mBaiduMap.setOnMarkerClickListener(this);
    }

    // ���¶�λ�Լ���λ�ò��� ��ͼ���Լ���λ��Ϊ����
    private void setReLocation() {

        mMapView.setClickable(false);// ���õײ��mapview�ػ����¼�
        isClickLoc = true;// �����ʼ��λ
        mLocationClient.start();// ���¶�λһ��
    }
    // ��ʼ�����
    private void initData() {
        if (mMapView != null) {
            mBaiduMap = mMapView.getMap();

        }
        mSearch = GeoCoder.newInstance();
    }

    private void initView(View view) {

        mMapView = (MapView) view.findViewById(R.id.baidu_mapview);
        mMyAddress = (TextView) view.findViewById(R.id.market_myaddress);

        mReLocaation = (ImageView)view.findViewById(R.id.market_relocation);

        mMarketReLocation = (ImageView)view.findViewById(R.id.market_location);
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

                String address1 = bdLocation.getAddrStr();
                Address address = bdLocation.getAddress();
                String city = address.city;
                String district = address.district;
                String street = address.street;
                String streetNumber = address.streetNumber;

                //��λ�ɹ����õ� �Ϸ��� Title��
                if (address1 != null && address!=null) {

                    mMyAddress.setText(city+district+street);
                }

                // �õ���λ��γ��
                double latitude = bdLocation.getLatitude();
                // �õ�λ�õľ���
                double longitude = bdLocation.getLongitude();

                YkLog.i("jinweidu",latitude+"/"+longitude);

                mLatLng = new LatLng(latitude, longitude);

                setDistance(mLatLng);
                // �������λ���Լ���λ��
                if(isClickLoc){
                    // �����λ���Լ���λ�ã���λ�ɹ� ���µ�ͼ
                    MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(mLatLng,16);
                    mBaiduMap.animateMapStatus(update);

                    isClickLoc = false;
                }

                if (currentLocationMarker == null) {
                    // ��ӱ�ע��
                    currentLocationMarker = addMarker(latitude,longitude,1);
                    currentLocationMarker.setTitle("�ҵ�λ��");

                    // ��Ӳ���ע���е�λ��
                    initMarketLocation();

                }else {
                    // �ƶ���ע��
                    currentLocationMarker.setPosition(mLatLng);
                }
                break;

            default:
                Log.d(TAG, "onReceiveLocation: "+"��λʧ��");
                mMyAddress.setText("��λʧ��");
                break;
        }
    }

    private void setDistance(LatLng latLng) {

        double distance = DistanceUtil.getDistance(mMarketLatLng, latLng)/1000;

        if(distance>=0){
        String result = String.format("%.1f",distance);

        Log.d("result",result);

            mMyAddress.setText("�೬�� "+ result +" km");
        }
    }
    private void initMarketLocation() {
        // �趨���ܾӳ��еĵ�ַ
        mMarketLatLng = new LatLng(21.281833,110.396756);
        Marker marker = addMarker(21.281833, 110.39675,0);
        marker.setTitle("���ܾӳ���");

        //�����ͼ״̬
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(mMarketLatLng,13));

        // ��Ӷ��������
        addRange();
    }

    /**
     * ��� ��ע
     * @param latitude
     * @param longitude
     * @param i   0 ��?�е�λ�ã� 1 ����Լ���λ��
     * @return
     */
    private Marker addMarker(double latitude, double longitude,int i) {

        MarkerOptions options = new MarkerOptions();

        options.position(new LatLng(latitude,longitude));
        if(i==0){
            // ���е�λ��
            options.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_location_box));
        }else if(i==1){
            // �ҵ�λ��
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
            // ��� ��γ�ȵõ���ַ��Ϣ
            mSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
                @Override
                public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

                }

                @Override
                public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                    name.setText(title);
                    if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                        //û���ҵ��������
                        info.setText("");
                    }else {
                        info.setText(result.getAddress());
                    }
                    //��ȡ������������
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

        super.onDestroy();
    }
}
