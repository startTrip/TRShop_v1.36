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

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.vlonjatg.progressactivity.ProgressActivity;

import java.util.ArrayList;
import java.util.List;

import shop.trqq.com.R;
import shop.trqq.com.supermarket.adapters.AddressInfoAdapter;
import shop.trqq.com.util.ToastUtils;

public class AddressFromMapActivity extends AppCompatActivity implements BDLocationListener, BaiduMap.OnMapStatusChangeListener, OnGetGeoCoderResultListener, OnGetPoiSearchResultListener {

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
        mListView = (ListView) findViewById(R.id.address_list);
        keyWordView = (EditText)findViewById(R.id.search_tv);
        mProgressActivity = (ProgressActivity)findViewById(R.id.address_progress);

    }


    private void initData() {

        mContext = AddressFromMapActivity.this;
        if (mMapView != null) {
            mMapView.showScaleControl(false);
            mMapView.showZoomControls(false);
            mBaiduMap = mMapView.getMap();
        }
        // ���������ҵ�λ��
        mBaiduMap.setMyLocationEnabled(true);
        // ��ʼ������ģ�飬ע���¼�����
        mSearch = GeoCoder.newInstance();

        // POI��ʼ������ģ�飬ע�������¼�����
        mPoiSearch = PoiSearch.newInstance();

        // �·���ַ�б�
        mListData = new ArrayList<>();
        mAddressInfoAdapter = new AddressInfoAdapter(this,mListData);
        mListView.setAdapter(mAddressInfoAdapter);

    }

    // ��������
    private void setData() {

        mBaiduMap.getUiSettings().setCompassEnabled(false);

        mLocationClient = new LocationClient(this);

        mLocationClient.registerLocationListener(this);

        mOption = new LocationClientOption();

        mOption.setOpenGps(false);


        mOption.setScanSpan(10000);

        mOption.setCoorType("bd09ll");

        // �ڶ�λ��ʱ�򣬷��ص�ַ��Ϣ
        mOption.setIsNeedAddress(true);

        mOption.setIsNeedLocationDescribe(true);//��ѡ��Ĭ��false�������Ƿ���Ҫλ�����廯�����������BDLocation.getLocationDescribe��õ�����������ڡ��ڱ����찲�Ÿ�����
        mOption.setIsNeedLocationPoiList(true);//��ѡ��Ĭ��false�������Ƿ���ҪPOI�����������BDLocation.getPoiList��õ�
        mOption.setIgnoreKillProcess(false);//��ѡ��Ĭ��true����λSDK�ڲ���һ��SERVICE�����ŵ��˶������̣������Ƿ���stop��ʱ��ɱ��������̣�Ĭ�ϲ�ɱ��
        mOption.SetIgnoreCacheException(false);//��ѡ��Ĭ��false�������Ƿ��ռ�CRASH��Ϣ��Ĭ���ռ�
        mOption.setEnableSimulateGps(false);//��ѡ��Ĭ��false�������Ƿ���Ҫ����GPS��������Ĭ����Ҫ

        // ���ö�λģʽ
        mOption.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);

        mLocationClient.setLocOption(mOption);
        mLocationClient.start();
    }

    // ��λ�ɹ��Ļص�����
    @Override
    public void onReceiveLocation(BDLocation bdLocation) {

        int locType = bdLocation.getLocType();

        switch (locType) {
            case BDLocation.TypeGpsLocation:
            case BDLocation.TypeNetWorkLocation:
            case BDLocation.TypeOffLineLocation:

                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(bdLocation.getRadius())
                        // �˴����ÿ����߻�ȡ���ķ�����Ϣ��˳ʱ��0-360
                        .direction(100).latitude(bdLocation.getLatitude())
                        .longitude(bdLocation.getLongitude()).build();
                mBaiduMap.setMyLocationData(locData);

                if (isFirstLoc) {//�״ζ�λ
                    isFirstLoc = false;

                    mCity = bdLocation.getCity();

                    // �õ���λ��γ��
                    double latitude = bdLocation.getLatitude();
                    // �õ�λ�õľ���
                    double longitude = bdLocation.getLongitude();

                    LatLng latLng = new LatLng(latitude, longitude);

                    // �����λ���Լ���λ�ã���λ�ɹ� ���µ�ͼ
                    MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(latLng, 18);
                    mBaiduMap.animateMapStatus(update);
                    Log.d("addressfromMapActivity","��λ�ɹ�");

                    searchNeayBy(latLng);
//                    mSearch.reverseGeoCode(new ReverseGeoCodeOption()
//                            .location(latLng));
                }
                break;

            default:
                ToastUtils.showMessage(mContext,"��λʧ��������");
                break;
        }
    }


    private void setListener() {

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.putExtra("address",mListData.get(i));
                setResult(RESULT_OK,intent);
                finish();
                overridePendingTransition(R.anim.push_left_in,R.anim.push_right_out);
            }
        });

        // ��ͼ״̬�ı�ļ�����
        mBaiduMap.setOnMapStatusChangeListener(this);
        // ����������
        mSearch.setOnGetGeoCodeResultListener(this);
        // Poi��������
        mPoiSearch.setOnGetPoiSearchResultListener(this);

        keyWordView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,AddressSearchActivity.class);
                if (mCity != null) {
                    intent.putExtra("city",mCity);
                    intent.putExtra("center",mCenter);
                }
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_in,R.anim.push_left_out);
            }
        });
    }


    // ����ͼ״̬�ı�ʱ��ʼ����
    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus) {

    }

    @Override
    public void onMapStatusChange(MapStatus mapStatus) {

    }

    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {

        mCenter = mapStatus.bound.getCenter();

        Log.d("addressfromMapActivity","��ͼ�ı����");
//        mSearch.reverseGeoCode(new ReverseGeoCodeOption()
//                .location(center));

//         Poi ������������
        searchNeayBy(mCenter);
    }

    /**
     * �����ܱ�
     * @param center
     */
    private void searchNeayBy(LatLng center){

        PoiNearbySearchOption poiNearbySearchOption = new PoiNearbySearchOption();

        poiNearbySearchOption.keyword("С��");
        poiNearbySearchOption.sortType(PoiSortType.distance_from_near_to_far);
        poiNearbySearchOption.location(center);
        poiNearbySearchOption.radius(2000);  // �����뾶����λ����
        poiNearbySearchOption.pageCapacity(20);  // Ĭ��ÿҳ10��

        mProgressActivity.showLoading();
        mPoiSearch.searchNearby(poiNearbySearchOption);  // ���𸽽���������
    }


    /*
	 * �����ܱߵ���λ�ý��
	 */
    @Override
    public void onGetPoiResult(PoiResult result) {
        // ��鵽����Ժ���ʾ����
        if(mProgressActivity.isLoading()){
            mProgressActivity.showContent();
        }

        if(result.error == SearchResult.ERRORNO.NETWORK_ERROR){
            // ��ʾ�����쳣����
            showNetWorkError();
        }

        Log.d("addressfromMapActivity","2");
        // ��ȡPOI�������
        if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {// û���ҵ��������

            return;
        }

        if (result.error == SearchResult.ERRORNO.NO_ERROR) {// ���������������
            if(result != null){
                if(result.getAllPoi()!= null && result.getAllPoi().size()>0){
                    List<PoiInfo> allPoi = result.getAllPoi();
                    for (PoiInfo poiInfo : allPoi) {
                        String name = poiInfo.name;
                        String address = poiInfo.address;
                        LatLng location = poiInfo.location;
                        Log.d("addressfromMapActivity","name"+name+"|address"+address);
                    }
                    mAddressInfoAdapter.addDatas(result.getAllPoi());
                }
            }
        }
    }

    // ��ʾ�����쳣����
    private void showNetWorkError() {
        Drawable errorDrawable = ContextCompat.getDrawable(mContext, R.drawable.wifi_off);
        mProgressActivity.showError(errorDrawable,"�����ֻ����粻̫˳��Ŷ",
                "","���¼���",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mProgressActivity.showLoading();
                        // ���¼��ص�ͼ
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
        // �˳�ʱ���ٶ�λ
        mLocationClient.stop();
        // �رն�λͼ��
        mPoiSearch.destroy();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

    //��������
    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {

            return;
        }

    }

    //��������
    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        // δ�ҵ����
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {

            return;
        }else {

            List<PoiInfo> poiList = result.getPoiList();
            Log.d("addressfromMapActivity", "onGetReverseGeoCodeResult:"+poiList.size());
            if (poiList.size()!=0) {
                // ������ݲ�����
                mAddressInfoAdapter.addDatas(poiList);
            }
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.relocation:
                setReLocation();
                break;
        }
    }

    // ���¶�λ�Լ���λ�ò��� ��ͼ���Լ���λ��Ϊ����
    private void setReLocation() {

        isFirstLoc = true;   // �������¶�λ
        mMapView.setClickable(false);// ���õײ��mapview�ػ����¼�

        if (mOption.isOpenGps()) {
            mOption.setOpenGps(false);
        }
        mLocationClient.setLocOption(mOption);
        mLocationClient.start();// ���¶�λһ��
    }

    public void imageClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                // �����ؼ�����
                finish();
                overridePendingTransition(R.anim.push_left_in,R.anim.push_right_out);
                break;
        }
    }
}
