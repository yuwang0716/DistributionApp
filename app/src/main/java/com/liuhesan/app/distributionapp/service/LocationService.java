package com.liuhesan.app.distributionapp.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.liuhesan.app.distributionapp.utility.API;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 类说明：后台服务定位
 */
public class LocationService extends Service {

    private String TAG = LocationService.class.getSimpleName();

    private static AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private double latitude;
    private double longitude;
    private Timer timer;
    private TimerTask timerTask;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startLocation();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {

        stopLocation();
        mLocationClient.onDestroy();
        super.onDestroy();
    }

    /**
     * 启动定位
     */
    void startLocation() {
        stopLocation();

        if (null == mLocationClient) {
            mLocationClient = new AMapLocationClient(this.getApplicationContext());
        }

        mLocationOption = new AMapLocationClientOption();
        // 使用连续
        mLocationOption.setOnceLocation(false);
        // 每10秒定位一次
        mLocationOption.setInterval(1000);
        mLocationOption.setHttpTimeOut(30000);
        // 地址信息
        mLocationOption.setNeedAddress(true);
        mLocationOption.setLocationCacheEnable(false);
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.setLocationListener(locationListener);
        mLocationClient.startLocation();
        timer = new Timer();
        timerTask = new TimerTask() {

            @Override
            public void run() {
                if (latitude != 0 && longitude != 0) {
                    OkGo.post(API.BASEURL + "deliver/logSteps/")
                            .tag(this)
                            .params("latitude", latitude)
                            .params("longitude", longitude)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    Log.e(TAG,latitude+ "保存\t"+longitude +"\n返回\t"+s);
                                }
                            });
                }
            }
        };
    }

    /**
     * 停止定位
     */
    public static void stopLocation() {
        if (null != mLocationClient) {
            mLocationClient.stopLocation();
        }
    }

    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (null != aMapLocation) {
                if (aMapLocation.getErrorCode() == 0) {
                        latitude = aMapLocation.getLatitude();
                        longitude = aMapLocation.getLongitude();
                        DecimalFormat df = new DecimalFormat("0.0000");
                        latitude = Double.parseDouble(df.format(latitude));
                        longitude = Double.parseDouble(df.format(longitude));
                        SharedPreferences sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = sharedPreferences.edit();
                        edit.putString("latitude", Double.toString(latitude));
                        edit.putString("longitude", Double.toString(longitude));
                        edit.commit();
                        Log.e(TAG, latitude+"坐标\t "+ longitude+"\n误差\t"+aMapLocation.getAccuracy());
                        timer.schedule(timerTask,1000,10000);

                } else {
                    Log.e("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());

                }

            }

        }
    };

}
