package com.liuhesan.app.distributionapp.adapter;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RidePath;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.liuhesan.app.distributionapp.R;
import com.liuhesan.app.distributionapp.bean.Order;
import com.liuhesan.app.distributionapp.ui.personcenter.OrderDetailsActivity;
import com.liuhesan.app.distributionapp.utility.API;
import com.liuhesan.app.distributionapp.utility.Distance;
import com.liuhesan.app.distributionapp.utility.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Response;

import static com.liuhesan.app.distributionapp.R.id.distance;
import static com.lzy.okgo.OkGo.getContext;

/**
 * Created by Tao on 2016/12/20.
 */

public class DistributionOrderAdapter extends BaseAdapter {
    private List<Order> orders;
    private Context mContext;
    private static final String TAG = "DistributionOrder";

    private int progress;
    private int progress_out;

    //定义hashMap 用来存放之前创建的每一项item
    HashMap<Integer, View> map = new HashMap<Integer, View>();

    public DistributionOrderAdapter(List<Order> orders, Context mContext) {
        this.orders = orders;
        this.mContext = mContext;

    }

    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public Object getItem(int position) {
        return orders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder mViewHolder;
        if (map.get(position) == null) {
            mViewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_distributing, null);
            mViewHolder.mProgressBar = (ProgressBar) convertView.findViewById(R.id.distributing_progressbar);
           mViewHolder.intime = (TextView) convertView.findViewById(R.id.intime);
            mViewHolder.outtime = (TextView) convertView.findViewById(R.id.outtime);
            mViewHolder.countdown = (TextView) convertView.findViewById(R.id.countdown);
            mViewHolder.distance = (TextView) convertView.findViewById(distance);
            mViewHolder.details = (TextView) convertView.findViewById(R.id.details);
            mViewHolder.shop_name = (TextView) convertView.findViewById(R.id.shop_name);
            mViewHolder.sn = (TextView) convertView.findViewById(R.id.sn);
            mViewHolder.address_get = (TextView) convertView.findViewById(R.id.address_get);
            mViewHolder.customer_name = (TextView) convertView.findViewById(R.id.customer_name);
            mViewHolder.address_send = (TextView) convertView.findViewById(R.id.address_send);
            mViewHolder.total = (TextView) convertView.findViewById(R.id.total);
            mViewHolder.paystate = (TextView) convertView.findViewById(R.id.paystate);
            mViewHolder.paystatetwo = (TextView) convertView.findViewById(R.id.paystatetwo);
            mViewHolder.distributed = (Button) convertView.findViewById(R.id.distributed);
            convertView.setTag(mViewHolder);
            map.put(position, convertView);
        } else {
            convertView = map.get(position);
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        final SharedPreferences sharedPreferences = mContext.getSharedPreferences("login", Context.MODE_PRIVATE);
        //倒计时
         final Timer timer = new Timer();
        final int timeout = sharedPreferences.getInt("timeout", 0) * 60;
        mViewHolder.intime.setText(timeout/60+"分钟");
        mViewHolder.mProgressBar.setMax(timeout/60);
        mViewHolder.mProgressBar.setProgress(timeout/60);
        if (orders.get(position).getPoi_name() != null) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Activity activity = (Activity) mContext;
                    activity.runOnUiThread(new Runnable() {      // UI thread
                        @Override
                        public void run() {
                            progress = mViewHolder.mProgressBar.getProgress();
                            progress = timeout - ((int) (System.currentTimeMillis()/1000 - orders.get(position).getGetOrderTime()));
                            mViewHolder.mProgressBar.setProgress(progress/60);
                            mViewHolder.intime.setText(progress/60+"分钟");
                        }
                    });
                }
            },0, 60*1000);
            //超时计时
            progress = timeout - ((int) (System.currentTimeMillis()/1000 - orders.get(position).getGetOrderTime()));
            if (progress < 0) {
                mViewHolder.outtime.setVisibility(View.VISIBLE);
                mViewHolder.mProgressBar.setVisibility(View.GONE);
                mViewHolder.intime.setVisibility(View.GONE);
                mViewHolder.countdown.setVisibility(View.GONE);

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Activity activity = (Activity) mContext;
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mViewHolder.outtime.setText("超时时间： "+(0-progress)/60+"分钟");
                            }
                        });

                    }
                }, 0, 60*1000);
                //超时提醒
                int i = 1;
                if ((0 - progress) / (60 * i) > 0) {
                    Intent intent = new Intent(mContext,com.liuhesan.app.distributionapp.ui.personcenter.MainActivity.class);
                    intent.putExtra("timeout","timeout");
                    PendingIntent pi = PendingIntent.getActivity(getContext(), 0, intent, 0);
                    NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                    Notification notification = new NotificationCompat.Builder(getContext())
                            .setContentTitle("订单超时提醒")
                            .setContentText("你有订单超时了！！！")
                            .setWhen(System.currentTimeMillis())
                            .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.logo))
                            .setContentIntent(pi)
                            .setAutoCancel(true)
                            .setSmallIcon(R.mipmap.logo)
                            .setVibrate(new long[]{0, 1000, 1000, 1000})
                            .build();
                    manager.notify(0, notification);
                    i++;
                }
            }
        }


        //骑手与取送距离

        double latitude = Double.parseDouble(sharedPreferences.getString("latitude", "0.0"));
        double longitude = Double.parseDouble(sharedPreferences.getString("longitude", "0.0"));

        final DecimalFormat df = new DecimalFormat("0.00");
        LatLonPoint point_me = new LatLonPoint(latitude, longitude);
        final LatLonPoint point_shop = new LatLonPoint(orders.get(position).getPoi_lat(), orders.get(position).getPoi_lng());
        final LatLonPoint point_client = new LatLonPoint(orders.get(position).getLat(), orders.get(position).getLng());
        Distance.getDistance(point_me, point_shop, mContext, new RouteSearch.OnRouteSearchListener() {
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
            public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {
                RideRouteResult mRideRouteResult = rideRouteResult;
                final RidePath ridePath = mRideRouteResult.getPaths()
                        .get(0);
                float dis = ridePath.getDistance();
                final String distance_shop = df.format(dis/1000);
                Distance.getDistance(point_shop, point_client, mContext, new RouteSearch.OnRouteSearchListener() {
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
                    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {
                        RideRouteResult mRideRouteResult = rideRouteResult;
                        final RidePath ridePath = mRideRouteResult.getPaths()
                                .get(0);
                        float dis = ridePath.getDistance();
                        String distance_client = df.format(dis/1000);
                        String str_distance = "我\t\t<font color='#28AAE3'>-" + distance_shop + "km-</font>\t\t取\t\t<font color='#28AAE3'>-" + distance_client + "km-</font>\t\t送";
                        mViewHolder.distance.setText(Html.fromHtml(str_distance));
                        mViewHolder.distributed.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                OkGo.post(API.BASEURL + "deliver/completedOrder/")
                                        .tag(this)
                                        .params("order_id", orders.get(position).getOrderid())
                                        .params("distance",distance)
                                        .params("price",orders.get(position).getPrice())
                                        .params("timeout",timeout)
                                        .execute(new StringCallback() {
                                            @Override
                                            public void onSuccess(String s, Call call, Response response) {
                                                try {
                                                    JSONObject jsonObject = new JSONObject(s);
                                                    int errno = jsonObject.optInt("errno");
                                                    String errmsg = jsonObject.optString("errmsg");
                                                    if (errno == 200) {
                                                        timer.cancel();
                                                        ToastUtil.showToast(mContext, errmsg);
                                                        orders.remove(position);
                                                        notifyDataSetChanged();
                                                    } else {
                                                        ToastUtil.showToast(mContext, errmsg);
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                            }
                        });
                    }
                });
            }
        });

        mViewHolder.shop_name.setText(orders.get(position).getPoi_name());
        mViewHolder.sn.setText("#"+orders.get(position).getSn());
        mViewHolder.address_get.setText(orders.get(position).getPoi_addr());
        mViewHolder.customer_name.setText(orders.get(position).getName());
        mViewHolder.address_send.setText(orders.get(position).getAddr());
        String str_total = null;
        if (orders.get(position).getPaytype() == 2) {
            str_total = "订单金额\t\t<font color='#F08C3F'>" + orders.get(position).getPrice() + "元</font>";
            mViewHolder.paystate.setVisibility(View.VISIBLE);
            mViewHolder.paystatetwo.setVisibility(View.GONE);
        } else {
            str_total = "订单金额\t\t<font color='#28AAE3'>" + orders.get(position).getPrice() + "元</font>";
            mViewHolder.paystatetwo.setVisibility(View.VISIBLE);
            mViewHolder.paystate.setVisibility(View.GONE);
        }
        mViewHolder.total.setText(Html.fromHtml(str_total));

        mViewHolder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OrderDetailsActivity.class);
                intent.putExtra("orderid",orders.get(position).getOrderid());
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }


    class ViewHolder {
        TextView distance, details, shop_name, sn, address_get, customer_name, address_send, total,
                paystate, paystatetwo,intime,outtime,countdown;
        Button distributed;
        ProgressBar mProgressBar;
    }
}
