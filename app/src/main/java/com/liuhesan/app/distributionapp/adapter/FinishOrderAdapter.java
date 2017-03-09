package com.liuhesan.app.distributionapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.liuhesan.app.distributionapp.R;
import com.liuhesan.app.distributionapp.bean.Order;
import com.liuhesan.app.distributionapp.ui.personcenter.CompletedOrderDetailsActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Tao on 2017/1/10.
 */

public class FinishOrderAdapter extends RecyclerView.Adapter<FinishOrderAdapter.ViewHolder> {
    private List<Order> orders;
    private Context mContext;
    private static final String TAG = "FinishOrderAdapter";
    public FinishOrderAdapter(List<Order> orders, Context mContext) {
        this.orders = orders;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_finishorder, null);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(FinishOrderAdapter.ViewHolder holder, final int position) {
            holder.shop_name.setText(orders.get(position).getPoi_name());
            holder.sn.setText("#"+orders.get(position).getSn());
            holder.address_get.setText(orders.get(position).getPoi_addr());
            holder.customer_name.setText(orders.get(position).getName());
            holder.address_send.setText(orders.get(position).getAddr());
            String str_total = null;
            if (orders.get(position).getPaytype() == 2) {
                str_total = "订单金额\t\t<font color='#F08C3F'>" + orders.get(position).getPrice() + "元</font>";
                holder.paystate.setVisibility(View.VISIBLE);
                holder.paystatetwo.setVisibility(View.GONE);
            } else {
                str_total = "订单金额\t\t<font color='#28AAE3'>" + orders.get(position).getPrice() + "元</font>";
                holder.paystatetwo.setVisibility(View.VISIBLE);
                holder.paystate.setVisibility(View.GONE);
            }
            holder.total.setText(Html.fromHtml(str_total));
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(orders.get(position).getStartTime()*1000);
            String startTime = formatter.format(calendar.getTime());
            calendar.setTimeInMillis(orders.get(position).getEndTime()*1000);
            String endTime = formatter.format(calendar.getTime());

            if (orders.get(position).isOutTime()){
                holder.finishorder_icon.setBackground(mContext.getResources().getDrawable(R.mipmap.finishorder_outtime));
                long outTime = orders.get(position).getOutTime();
                holder.order_detailstime.setText("下单时间\t"+startTime +"\n"+"完成时间\t"+endTime+"\n"+"超时时间\t"+outTime/60+"分钟");
            }else {
                holder.finishorder_icon.setBackground(mContext.getResources().getDrawable(R.mipmap.finishorder_intime));
                holder.order_detailstime.setText("下单时间\t"+startTime +"\n"+"完成时间\t"+endTime);
            }
            holder.details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, CompletedOrderDetailsActivity.class);
                    intent.putExtra("orderid",orders.get(position).getOrderid());
                    mContext.startActivity(intent);
                }
            });

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView distance, details, shop_name, sn, address_get, customer_name, address_send, total,
                paystate, paystatetwo,intime,outtime,countdown,order_detailstime;
        ImageView finishorder_icon;
        public ViewHolder(View itemView) {
            super(itemView);
            intime = (TextView) itemView.findViewById(R.id.intime);
            outtime = (TextView) itemView.findViewById(R.id.outtime);
            countdown = (TextView) itemView.findViewById(R.id.countdown);
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
            order_detailstime = (TextView) itemView.findViewById(R.id.order_detailstime);
            finishorder_icon = (ImageView) itemView.findViewById(R.id.finishorder_icon);
        }
    }
}
