package com.liuhesan.app.distributionapp.adapter;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Tao on 2017/1/10.
 */

public class ReseizeOrderAdapter extends RecyclerView.Adapter<ReseizeOrderAdapter.ViewHolder> {
    private List<Order> orders;
    private Context mContext;
    private static final String TAG = "ReseizeOrderAdapter";

    public ReseizeOrderAdapter(List<Order> orders, Context mContext) {
        this.orders = orders;
        this.mContext = mContext;
    }

    @Override
    public ReseizeOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_reseize, null);
        return new ViewHolder(view);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final ReseizeOrderAdapter.ViewHolder holder, final int position) {
        //骑手与取送距离
        final SharedPreferences sharedPreferences = mContext.getSharedPreferences("login",Context.MODE_PRIVATE);
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
                        holder.distance.setText(Html.fromHtml(str_distance));
                    }
                });
            }
        });

        holder.shop_name.setText(orders.get(position).getPoi_name());
        holder.sn.setText("#"+orders.get(position).getSn());
        holder.address_get.setText(orders.get(position).getPoi_addr());
        holder.customer_name.setText(orders.get(position).getName());
        holder.address_send.setText(orders.get(position).getAddr());
        String str_total=null;
        if (orders.get(position).getPaytype() == 2){
            str_total = "订单金额\t\t<font color='#F08C3F'>"+orders.get(position).getPrice()+"元</font>";
            holder.paystate.setVisibility(View.VISIBLE);
            holder.paystatetwo.setVisibility(View.GONE);
        }else {
            str_total = "订单金额\t\t<font color='#28AAE3'>"+orders.get(position).getPrice()+"元</font>";
            holder.paystatetwo.setVisibility(View.VISIBLE);
            holder.paystate.setVisibility(View.GONE);
        }
        holder.total.setText(Html.fromHtml(str_total));

        holder.order_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OrderDetailsActivity.class);
                intent.putExtra("orderid",orders.get(position).getOrderid());
                mContext.startActivity(intent);
            }
        });
        //订单操作
        final int timeout = sharedPreferences.getInt("timeout", 0);
        holder.queryOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    OkGo.post(API.BASEURL+"deliver/queryOrder/")
                            .tag(this)
                            .params("order_id",orders.get(position).getOrderid())
                            .params("sendtime",timeout)
                            .params("deliver",sharedPreferences.getString("deliver",""))
                            .params("mobile",sharedPreferences.getString("mobile",""))
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    orders.remove(position);
                                    notifyDataSetChanged();
                                    try {
                                        JSONObject jsonObject = new JSONObject(s);
                                        String errmsg = jsonObject.optString("errmsg");
                                        ToastUtil.showToast(mContext, errmsg);
                                        NotificationManager manger = (NotificationManager)mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
                                        manger.cancelAll();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
            }
        });
        holder.refuseOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //拒单理由
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("拒单将会影响您的评分、等级，请谨慎拒单，请输入您的拒单理由：");
                View view = LayoutInflater.from(mContext).inflate(R.layout.item_reason, null);
                builder.setView(view);
                final EditText et_reason = (EditText)view.findViewById(R.id.reason);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String reason = et_reason.getText().toString().trim();
                        OkGo.post(API.BASEURL+"deliver/refuseOrder/")
                                .tag(this)
                                .params("order_id",orders.get(position).getOrderid())
                                .params("reason",reason)
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(String s, Call call, Response response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(s);
                                            int errno = jsonObject.optInt("errno");
                                            String errmsg = jsonObject.optString("errmsg");
                                            if (errno == 200){
                                                ToastUtil.showToast(mContext,errmsg);
                                                orders.remove(position);
                                                notifyItemRemoved(position);
                                                notifyItemRangeChanged(position,getItemCount());
                                            }else {
                                                ToastUtil.showToast(mContext,errmsg);
                                            }
                                            NotificationManager manger = (NotificationManager)mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
                                            manger.cancelAll();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return orders == null ? 0:orders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView distance,details,shop_name,sn,address_get,customer_name,address_send,total,
                paystate,paystatetwo,order_details;
        Button refuseOrder,queryOrder;
        public ViewHolder(View itemView) {
            super(itemView);
            distance = (TextView) itemView.findViewById(R.id.distance);
            details = (TextView) itemView.findViewById(R.id.details);
            shop_name = (TextView) itemView.findViewById(R.id.shop_name);
            sn = (TextView) itemView.findViewById(R.id.sn);
            address_get = (TextView) itemView.findViewById(R.id.address_get);
            customer_name = (TextView) itemView.findViewById(R.id.customer_name);
            address_send = (TextView) itemView.findViewById(R.id.address_send);
            total = (TextView) itemView.findViewById(R.id.total);
            paystate = (TextView) itemView.findViewById(R.id.paystate);
            paystatetwo = (TextView) itemView.findViewById(R.id.paystatetwo);
            queryOrder = (Button) itemView.findViewById(R.id.reseizeorder_queryOrder);
            refuseOrder = (Button) itemView.findViewById(R.id.reseizeorder_refuseorder);
            order_details = (TextView) itemView.findViewById(R.id.details);

        }
    }
}
