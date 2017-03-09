package com.liuhesan.app.distributionapp.ui.personcenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RidePath;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.liuhesan.app.distributionapp.R;
import com.liuhesan.app.distributionapp.overlay.RideRouteOverlay;
import com.liuhesan.app.distributionapp.utility.AMapUtil;

public class AMapNaviViewActivity extends Activity implements RouteSearch.OnRouteSearchListener, AMap.OnMapClickListener, AMap.OnMarkerClickListener {
    private final static String TAG = "AMapNaviViewActivity";
    private AMap aMap;
    private MapView mapView;
    private Context mContext;
    private RouteSearch mRouteSearch;
    private RideRouteResult mRideRouteResult;
    private LatLonPoint mStartPoint;//起点，39.996678,116.479271
    private LatLonPoint mEndPoint ;//终点，39.997796,116.468939
    private final int ROUTE_TYPE_RIDE = 4;
    private LinearLayout.LayoutParams mParams;
    private RelativeLayout mContainerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amap_navi_view);
        mContainerLayout = (RelativeLayout) findViewById(R.id.activity_main);
        AMapOptions aOptions = new AMapOptions();
        SharedPreferences sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        double latitude_user = Double.parseDouble(sharedPreferences.getString("latitude", "0.0"));
        double longitude_user = Double.parseDouble(sharedPreferences.getString("longitude", "0.0"));
        Log.e(TAG, sharedPreferences.getString("latitude", "0.0")+"坐标\t"+ sharedPreferences.getString("longitude", "0.0"));
        aOptions.camera(new CameraPosition(new LatLng(latitude_user,longitude_user), 14f, 0, 0));
        mapView = new MapView(this, aOptions);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        mContainerLayout.addView(mapView, mParams);
        Intent intent = getIntent();
        double longitude = intent.getDoubleExtra("longitude", 0.0);
        double latitude = intent.getDoubleExtra("latitude", 0.0);
        double longitude_shop = intent.getDoubleExtra("longitudeShop", 0.0);
        double latitude_shop = intent.getDoubleExtra("latitudeShop", 0.0);
        if (longitude_shop != 0.0){
            mStartPoint = new LatLonPoint(latitude_shop,longitude_shop);
            mEndPoint = new LatLonPoint(latitude,longitude);
        }else {
            mStartPoint = new LatLonPoint(latitude_user,longitude_user);
            mEndPoint = new LatLonPoint(latitude,longitude);
        }

        init();
        setfromandtoMarker();
        searchRouteResult(ROUTE_TYPE_RIDE, RouteSearch.RidingDefault);
        mapView.setVisibility(View.VISIBLE);
    }
    private void setfromandtoMarker() {
        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(mStartPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.start)));
        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(mEndPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.end)));
    }
    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap  = mapView.getMap();
        }
        registerListener();
        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(this);
    }
    /**
     * 注册监听
     */
    private void registerListener() {

        aMap.setOnMapClickListener(this);
        aMap.setOnMarkerClickListener(this);

    }
    /**
     * 开始搜索路径规划方案
     */
    public void searchRouteResult(int routeType, int mode) {
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                mStartPoint, mEndPoint);
        if (routeType == ROUTE_TYPE_RIDE) {// 骑行路径规划
            RouteSearch.RideRouteQuery query = new RouteSearch.RideRouteQuery(fromAndTo, mode);
            mRouteSearch.calculateRideRouteAsyn(query);// 异步路径规划骑行模式查询
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }


    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult result, int errorCode) {
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mRideRouteResult = result;
                    final RidePath ridePath = mRideRouteResult.getPaths()
                            .get(0);
                    RideRouteOverlay rideRouteOverlay = new RideRouteOverlay(
                            this, aMap, ridePath,
                            mRideRouteResult.getStartPos(),
                            mRideRouteResult.getTargetPos());
                    rideRouteOverlay.removeFromMap();
                    rideRouteOverlay.addToMap();
                    rideRouteOverlay.zoomToSpan();

                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

}
