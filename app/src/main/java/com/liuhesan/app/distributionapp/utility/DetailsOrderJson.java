package com.liuhesan.app.distributionapp.utility;

import com.liuhesan.app.distributionapp.bean.DetailsOrder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tao on 2017/1/3.
 */

public class DetailsOrderJson {
    private static DetailsOrder detailsOrder;
    public static DetailsOrder getData(JSONObject data){
        detailsOrder = new DetailsOrder();
        String wm_poi_name = data.optString("wm_poi_name");
        detailsOrder.setWm_poi_name(wm_poi_name);
        String wm_poi_address = data.optString("wm_poi_address");
        detailsOrder.setWm_poi_address(wm_poi_address);
        String orderid = data.optString("orderid");
        detailsOrder.setOrderid(orderid);
        String wm_poi_phone = data.optString("wm_poi_phone");
        detailsOrder.setWm_poi_phone(wm_poi_phone);
        String sn = data.optString("sn");
        detailsOrder.setSn(sn);
        double longitude = data.optDouble("longitude");
        detailsOrder.setLongitude(longitude);
        double latitude = data.optDouble("latitude");
        detailsOrder.setLatitude(latitude);
        String recipient_name = data.optString("recipient_name");
        detailsOrder.setRecipient_name(recipient_name);
        String recipient_address = data.optString("recipient_address");
        detailsOrder.setRecipient_address(recipient_address);
        String recipient_phone = data.optString("recipient_phone");
        detailsOrder.setRecipient_phone(recipient_phone);
        String order_send_time = data.optString("order_send_time");
        detailsOrder.setOrder_send_time(order_send_time);
        long time = data.optLong("order_receive_time");
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String times = format.format(new Date(time * 1000L));
        detailsOrder.setOrder_receive_time(times);
        int pay_type = data.optInt("pay_type");
        detailsOrder.setPay_type(pay_type);
        String order_meal_fee = data.optString("order_meal_fee");
        detailsOrder.setOrder_meal_fee(order_meal_fee);
        String shipping_fee = data.optString("shipping_fee");
        detailsOrder.setShipping_fee(shipping_fee);
        String shop_other_discount = data.optString("shop_other_discount");
        detailsOrder.setShop_other_discount(shop_other_discount);
        String total_price = data.optString("total_price");
        detailsOrder.setTotal_price(total_price);
        String caution = data.optString("caution");
        detailsOrder.setCaution(caution);
        JSONObject xy = data.optJSONObject("xy");
        double wm_poi_latitude = xy.optDouble("latitude");
        double wm_poi_longitude = xy.optDouble("longitude");
        detailsOrder.setWm_poi_latitude(wm_poi_latitude);
        detailsOrder.setWm_poi_longitude(wm_poi_longitude);
        JSONArray goods = data.optJSONArray("goods");
        List<Map<String,String>> maps = new ArrayList<>();
        for (int i = 0; i < goods.length(); i++) {
            JSONObject jsonObject = goods.optJSONObject(i);
            Map<String,String> map = new HashMap<>();
            String name = jsonObject.optString("name");
            String number = jsonObject.optString("number");
            String price = jsonObject.optString("price");
            map.put("name",name);
            map.put("number","×"+number);
            map.put("price",price);
            maps.add(map);
        }
        detailsOrder.setGoods(maps);
        return detailsOrder;
    }
}
