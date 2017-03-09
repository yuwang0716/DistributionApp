package com.liuhesan.app.distributionapp.utility;

import com.liuhesan.app.distributionapp.bean.Order;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tao on 2016/12/22.
 * 字段声明
 sn 订单当天流水号
 orderid订单展示ID
 poi_name商家名称
 poi_addr 商家地址
 poi_mob 商家电话
 poi_lng 商家经度
 poi_lat 商家纬度
 addr 收件人地址
 mob 收件人电话
 Name 收件人姓名
 Prices 商品总价格
 Paytype 支付方式
 lat送餐地址纬度
 lng送餐地址经度
 Shopid 店铺编号
 otime 用户下单时间（时间戳）
 ctime 订单完成时间
 Outtime 超时时间
 Timeout 超时状态
 currtime 取餐时间
 */

public class TakingOrderJson {
    private static List<Order> orders;
    public static List<Order>  getData(JSONArray list){
        orders = new ArrayList<>();
            for (int i = 0; i < list.length() ; i++) {
                Order order = new Order();
                JSONObject jsonObject = list.optJSONObject(i);
                String sn = jsonObject.optString("sn");
                order.setSn(sn);
                String orderid = jsonObject.optString("orderid");
                order.setOrderid(orderid);
                String poi_name = jsonObject.optString("poi_name");
                order.setPoi_name(poi_name);
                String poi_addr = jsonObject.optString("poi_addr");
                order.setPoi_addr(poi_addr);
                String name = jsonObject.optString("name");
                order.setName(name);
                String addr = jsonObject.optString("addr");
                order.setAddr(addr);
                double poi_lat = jsonObject.optDouble("poi_lat");
                order.setPoi_lat(poi_lat);
                double poi_lng = jsonObject.optDouble("poi_lng");
                order.setPoi_lng(poi_lng);
                double lat = jsonObject.optDouble("lat");
                order.setLat(lat);
                double lng = jsonObject.optDouble("lng");
                order.setLng(lng);
                String poi_mob = jsonObject.optString("poi_mob");
                order.setPoi_mob(poi_mob);
                String mob =jsonObject.optString("mob");
                order.setMob(mob);
                int paytype =jsonObject.optInt("paytype");
                order.setPaytype(paytype);
                String price = jsonObject.optString("price");
                order.setPrice(price);
                long startTime= jsonObject.optLong("otime");
                order.setStartTime(startTime);
                long endTime = jsonObject.optLong("ctime");
                order.setEndTime(endTime);
                long outtime = jsonObject.optLong("outtime");
                order.setOutTime(outtime);
                int timeout = jsonObject.optInt("timeout");
                if (timeout == 0){
                    order.setOutTime(false);
                }if (timeout == 1){
                    order.setOutTime(true);
                }
                String reason = jsonObject.optString("reason");
                order.setReason(reason);
                long currtime = jsonObject.optLong("currtime");
                order.setGetOrderTime(currtime);
                orders.add(order);
            }
        return orders;
    }
}
