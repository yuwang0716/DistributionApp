package com.liuhesan.app.distributionapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.liuhesan.app.distributionapp.R;
import com.liuhesan.app.distributionapp.bean.Order;
import com.liuhesan.app.distributionapp.utility.API;
import com.liuhesan.app.distributionapp.ui.personcenter.OrderDetailsActivity;
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
 * Created by Tao on 2016/12/20.
 */

public class GotOrderAdapter extends BaseAdapter {
    private List<Order> orders;
    private Context mContext;
    private String token;
    private static final String TAG = "GotOrderAdapter";
    public GotOrderAdapter(List<Order> orders, Context mContext) {
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
        ViewHolder mViewHolder ;
        if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_takinggoods, null);
            mViewHolder = new ViewHolder();

            mViewHolder.distance = (TextView) convertView.findViewById(R.id.distance);
            mViewHolder.details = (TextView) convertView.findViewById(R.id.details);
            mViewHolder.shop_name = (TextView) convertView.findViewById(R.id.shop_name);
            mViewHolder.sn = (TextView) convertView.findViewById(R.id.sn);
            mViewHolder.address_get = (TextView) convertView.findViewById(R.id.address_get);
            mViewHolder.customer_name = (TextView) convertView.findViewById(R.id.customer_name);
            mViewHolder.address_send = (TextView) convertView.findViewById(R.id.address_send);
            mViewHolder.total = (TextView) convertView.findViewById(R.id.total);
            mViewHolder.paystate = (TextView) convertView.findViewById(R.id.paystate);
            mViewHolder.paystatetwo = (TextView) convertView.findViewById(R.id.paystatetwo);
            mViewHolder.cancelOrder = (Button) convertView.findViewById(R.id.takinggoods_cancelder);
            mViewHolder.gotOrder = (Button) convertView.findViewById(R.id.takinggoods_gotOrder);
            mViewHolder.order_details = (TextView) convertView.findViewById(R.id.details);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        //骑手与取送距离
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("login",Context.MODE_PRIVATE);
        double latitude = Double.parseDouble(sharedPreferences.getString("latitude", "0.0"));
        double longitude = Double.parseDouble(sharedPreferences.getString("longitude", "0.0"));
        LatLng latLng_me = new LatLng(latitude, longitude);
        LatLng latLng_shop= new LatLng(orders.get(position).getPoi_lat(), orders.get(position).getPoi_lng());
        LatLng latLng_client = new LatLng(orders.get(position).getLat(), orders.get(position).getLng());
        DecimalFormat df = new DecimalFormat("0.00");
        String distance_shop = df.format(AMapUtils.calculateLineDistance(latLng_me, latLng_shop)/1000);
        String distance_user = df.format(AMapUtils.calculateLineDistance(latLng_shop, latLng_client)/1000);
        String str_distance = "我\t\t<font color='#28AAE3'>-" + distance_shop + "m-</font>\t\t取\t\t<font color='#28AAE3'>-" + distance_user + "m-</font>\t\t送";
        mViewHolder.distance.setText(Html.fromHtml(str_distance));
        mViewHolder.shop_name.setText(orders.get(position).getPoi_name());
        mViewHolder.sn.setText("#"+orders.get(position).getSn());
        mViewHolder.address_get.setText(orders.get(position).getPoi_addr());
        mViewHolder.customer_name.setText(orders.get(position).getName());
        mViewHolder.address_send.setText(orders.get(position).getAddr());
        String str_total=null;
        if (orders.get(position).getPaytype() == 2){
            str_total = "订单金额\t\t<font color='#F08C3F'>"+orders.get(position).getPrice()+"元</font>";
            mViewHolder.paystate.setVisibility(View.VISIBLE);
            mViewHolder.paystatetwo.setVisibility(View.GONE);
        }else {
            str_total = "订单金额\t\t<font color='#28AAE3'>"+orders.get(position).getPrice()+"元</font>";
            mViewHolder.paystatetwo.setVisibility(View.VISIBLE);
            mViewHolder.paystate.setVisibility(View.GONE);
        }
        mViewHolder.total.setText(Html.fromHtml(str_total));
        mViewHolder.order_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OrderDetailsActivity.class);
                intent.putExtra("orderid",orders.get(position).getOrderid());
                mContext.startActivity(intent);
            }
        });
        //订单操作
        mViewHolder.cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消订单理由
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("取消订单将会影响您的完成率，请谨慎取消订单，请输入您的拒单理由：");
                View view = LayoutInflater.from(mContext).inflate(R.layout.item_reason, null);
                builder.setView(view);
                final EditText et_reason = (EditText)view.findViewById(R.id.reason);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (TextUtils.isEmpty(et_reason.getText())){
                            ToastUtil.showToast(mContext,"理由不能为空");
                        }else {
                            String reason = et_reason.getText().toString().trim();

                            OkGo.post(API.BASEURL+"deliver/releaseOrder/")
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
                                                    notifyDataSetChanged();
                                                }else {
                                                    ToastUtil.showToast(mContext,errmsg);
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                        }

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
        mViewHolder.gotOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkGo.post(API.BASEURL+"deliver/fetchOrder/")
                        .tag(this)
                        .params("token",token)
                        .params("order_id",orders.get(position).getOrderid())
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                Log.i(TAG, s+"fetchOrderSuccess: ");
                                try {
                                    JSONObject jsonObject = new JSONObject(s);
                                    int errno = jsonObject.optInt("errno");
                                    String errmsg = jsonObject.optString("errmsg");
                                    if (errno == 200){
                                        ToastUtil.showToast(mContext,errmsg);
                                        orders.remove(position);
                                        notifyDataSetChanged();
                                    }else {
                                        Toast.makeText(mContext,errmsg,Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        });

        return convertView;
    }


    class ViewHolder {
       TextView distance,details,shop_name,sn,address_get,customer_name,address_send,total,
               paystate,paystatetwo,order_details;
        Button cancelOrder,gotOrder;
    }
}
