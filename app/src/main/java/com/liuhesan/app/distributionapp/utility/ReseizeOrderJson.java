package com.liuhesan.app.distributionapp.utility;

import com.liuhesan.app.distributionapp.bean.Order;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tao on 2016/12/22.
 */

public class ReseizeOrderJson {
    private static List<Order> orders;

    public static List<Order> getData(String msg) {
        orders = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(msg);
            Order order = new Order();
            String act = jsonObject.optString("act");
            order.setAct(act);
            JSONObject data = jsonObject.optJSONObject("data");
            String sn = data.optString("sn");
            order.setSn(sn);
            String orderid = data.optString("orderid");
            order.setOrderid(orderid);
            String poi_name = data.optString("poi_name");
            order.setPoi_name(poi_name);
            String poi_addr = data.optString("poi_addr");
            order.setPoi_addr(poi_addr);
            String poi_mob = data.optString("poi_mob");
            order.setPoi_mob(poi_mob);
            double poi_lat = data.optDouble("poi_lat");
            order.setPoi_lat(poi_lat);
            double poi_lng = data.optDouble("poi_lng");
            order.setPoi_lng(poi_lng);
            String price = data.optString("price");
            order.setPrice(price);
            int paytype = data.optInt("paytype");
            order.setPaytype(paytype);
            String name = data.optString("name");
            order.setName(name);
            String addr = data.optString("addr");
            order.setAddr(addr);
            String mob = data.optString("mob");
            order.setMob(mob);
            double lat = data.optDouble("lat");
            order.setLat(lat);
            double lng = data.optDouble("lng");
            order.setLng(lng);
            orders.add(order);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return orders;
    }
}
