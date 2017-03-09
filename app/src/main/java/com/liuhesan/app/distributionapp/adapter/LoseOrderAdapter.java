package com.liuhesan.app.distributionapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.liuhesan.app.distributionapp.R;
import com.liuhesan.app.distributionapp.bean.Order;
import com.liuhesan.app.distributionapp.ui.personcenter.CompletedOrderDetailsActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Tao on 2016/12/20.
 */

public class LoseOrderAdapter extends BaseAdapter {
    private List<Order> orders;
    private Context mContext;
    private static final String TAG = "LoseOrderAdapter";

    //定义hashMap 用来存放之前创建的每一项item
    HashMap<Integer, View> map = new HashMap<Integer, View>();

    public LoseOrderAdapter(List<Order> orders, Context mContext) {
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder mViewHolder;
        if (map.get(position) == null) {
            mViewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_loseorder, null);
           mViewHolder.intime = (TextView) convertView.findViewById(R.id.intime);
            mViewHolder.outtime = (TextView) convertView.findViewById(R.id.outtime);
            mViewHolder.countdown = (TextView) convertView.findViewById(R.id.countdown);
            mViewHolder.details = (TextView) convertView.findViewById(R.id.details);
            mViewHolder.shop_name = (TextView) convertView.findViewById(R.id.shop_name);
            mViewHolder.sn = (TextView) convertView.findViewById(R.id.sn);
            mViewHolder.address_get = (TextView) convertView.findViewById(R.id.address_get);
            mViewHolder.customer_name = (TextView) convertView.findViewById(R.id.customer_name);
            mViewHolder.address_send = (TextView) convertView.findViewById(R.id.address_send);
            mViewHolder.total = (TextView) convertView.findViewById(R.id.total);
            mViewHolder.paystate = (TextView) convertView.findViewById(R.id.paystate);
            mViewHolder.paystatetwo = (TextView) convertView.findViewById(R.id.paystatetwo);
            mViewHolder.order_detailstime = (TextView) convertView.findViewById(R.id.order_detailstime);
            mViewHolder.finishorder_icon = (ImageView) convertView.findViewById(R.id.loseorder_icon);
            mViewHolder.reason = (TextView) convertView.findViewById(R.id.loseorder_reason);
            convertView.setTag(mViewHolder);
            map.put(position, convertView);
        } else {
            convertView = map.get(position);
            mViewHolder = (ViewHolder) convertView.getTag();
        }

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
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(orders.get(position).getStartTime()*1000);
        String startTime = formatter.format(calendar.getTime());
        mViewHolder.order_detailstime.setText("下单时间\t"+startTime+"\n失效原因");
        mViewHolder.reason.setText("\t"+orders.get(position).getReason());
        mViewHolder.finishorder_icon.setBackground(mContext.getResources().getDrawable(R.mipmap.loseorder));
        mViewHolder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CompletedOrderDetailsActivity.class);
                intent.putExtra("orderid",orders.get(position).getOrderid());
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }


    class ViewHolder {
        TextView  details, shop_name, sn, address_get, customer_name, address_send, total,
                paystate, paystatetwo,intime,outtime,countdown,order_detailstime,reason;
        ImageView finishorder_icon;
    }
}
