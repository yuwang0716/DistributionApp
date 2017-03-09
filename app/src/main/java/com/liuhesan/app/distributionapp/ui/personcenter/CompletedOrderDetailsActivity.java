package com.liuhesan.app.distributionapp.ui.personcenter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.liuhesan.app.distributionapp.R;
import com.liuhesan.app.distributionapp.adapter.DetailsOrderAdapter;
import com.liuhesan.app.distributionapp.bean.DetailsOrder;
import com.liuhesan.app.distributionapp.utility.API;
import com.liuhesan.app.distributionapp.utility.DetailsOrderJson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import okhttp3.Call;
import okhttp3.Response;

public class CompletedOrderDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private final static  String TAG ="OrderDetailsActivity";
    private String orderid;
    private TextView tv_details_distance,tv_shop_name,tv_sn,tv_address_get,tv_customer_name,
            tv_address_send, tv_send_time,tv_order_time,tv_orderid,tv_meal_fee,tv_ship_fee,
            tv_favorable,tv_total,tv_caution,pay_state,pay_state_two;
    private ListView goods;
    private ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completedorder_details);
        Intent intent = getIntent();
        orderid = intent.getStringExtra("orderid");
        Log.i(TAG,orderid+"onSuccess: ");
        initView();
        initData();
    }

    private void initView() {
        back = (ImageButton) findViewById(R.id.order_details_back);
        tv_details_distance = (TextView) findViewById(R.id.order_details_distance);
        tv_shop_name = (TextView) findViewById(R.id.shop_name);
        tv_sn = (TextView) findViewById(R.id.sn);
        tv_address_get = (TextView) findViewById(R.id.address_get);
        tv_customer_name = (TextView) findViewById(R.id.customer_name);
        tv_address_send = (TextView) findViewById(R.id.address_send);
        tv_send_time = (TextView) findViewById(R.id.send_time);
        tv_order_time = (TextView) findViewById(R.id.order_time);
        tv_orderid = (TextView) findViewById(R.id.orderid);
        tv_meal_fee = (TextView) findViewById(R.id.meal_fee);
        tv_ship_fee = (TextView) findViewById(R.id.ship_fee);
        tv_favorable = (TextView) findViewById(R.id.favorable);
        tv_total = (TextView) findViewById(R.id.total);
        tv_caution = (TextView) findViewById(R.id.caution);
        goods = (ListView) findViewById(R.id.goods);
        pay_state = (TextView) findViewById(R.id.pay_state);
        pay_state_two = (TextView) findViewById(R.id.pay_statetwo);
        back.setOnClickListener(this);
    }

    private void initData() {
        OkGo.post(API.BASEURL+"deliver/orderArt")
                .params("order_id",orderid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        s = s.toString().replace("null","0");
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            int errno = jsonObject.optInt("errno");
                            if (errno == 200){
                                JSONObject data = jsonObject.optJSONObject("data");
                                final DetailsOrder detailsOrder = DetailsOrderJson.getData(data);
                                tv_shop_name.setText(detailsOrder.getWm_poi_name());
                                tv_sn.setText("#"+detailsOrder.getSn());
                                tv_address_get.setText(detailsOrder.getWm_poi_address());
                                tv_customer_name.setText(detailsOrder.getRecipient_name());
                                tv_address_send.setText(detailsOrder.getRecipient_address());
                                tv_send_time.setText(detailsOrder.getOrder_send_time());
                                tv_order_time.setText(detailsOrder.getOrder_receive_time());
                                tv_orderid.setText(detailsOrder.getOrderid());
                                tv_meal_fee.setText(detailsOrder.getOrder_meal_fee());
                                tv_ship_fee.setText(detailsOrder.getShipping_fee());
                                tv_favorable.setText(detailsOrder.getShop_other_discount());
                                tv_total.setText(detailsOrder.getTotal_price());
                                tv_caution.setText(detailsOrder.getCaution());
                                if (detailsOrder.getPay_type() == 2){
                                    pay_state.setVisibility(View.VISIBLE);
                                    pay_state_two.setVisibility(View.GONE);
                                }else {
                                    pay_state.setVisibility(View.GONE);
                                    pay_state_two.setVisibility(View.VISIBLE);
                                }
                                DetailsOrderAdapter adapter = new DetailsOrderAdapter(detailsOrder.getGoods(),CompletedOrderDetailsActivity.this);
                                goods.setAdapter(adapter);
                                //取送距离
                                LatLng latLng_shop = new LatLng(detailsOrder.getWm_poi_latitude(), detailsOrder.getWm_poi_longitude());
                                LatLng latLng_client = new LatLng(detailsOrder.getLatitude(),detailsOrder.getLongitude());
                                DecimalFormat df = new DecimalFormat("0.00");
                                String distance = df.format(AMapUtils.calculateLineDistance(latLng_shop, latLng_client)/1000);
                                String str_distance = "商家<font color='#28AAE3'>-" + distance + "km-</font>客户";
                                tv_details_distance.setText(Html.fromHtml(str_distance));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
