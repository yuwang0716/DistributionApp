package com.liuhesan.app.distributionapp.bean;

import java.util.List;
import java.util.Map;

/**
 * Created by Tao on 2017/1/3.
 */

public class DetailsOrder {
    private String wm_poi_name;
    private String wm_poi_address;
    private String wm_poi_phone;
    private String sn;
    private double longitude;
    private double latitude;
    private double wm_poi_longitude;
    private double wm_poi_latitude;
    private String recipient_name;
    private String recipient_address;
    private String recipient_phone;
    private String order_send_time;
    private String order_receive_time;
    private String orderid;
    private int pay_type;
    private String order_meal_fee;
    private String shipping_fee;
    private String shop_other_discount;
    private String total_price;
    private String caution;
    private List<Map<String,String>> goods;

    public double getWm_poi_longitude() {
        return wm_poi_longitude;
    }

    public void setWm_poi_longitude(double wm_poi_longitude) {
        this.wm_poi_longitude = wm_poi_longitude;
    }

    public double getWm_poi_latitude() {
        return wm_poi_latitude;
    }

    public void setWm_poi_latitude(double wm_poi_latitude) {
        this.wm_poi_latitude = wm_poi_latitude;
    }

    public String getWm_poi_name() {
        return wm_poi_name;
    }

    public void setWm_poi_name(String wm_poi_name) {
        this.wm_poi_name = wm_poi_name;
    }

    public String getWm_poi_address() {
        return wm_poi_address;
    }

    public void setWm_poi_address(String wm_poi_address) {
        this.wm_poi_address = wm_poi_address;
    }

    public String getWm_poi_phone() {
        return wm_poi_phone;
    }

    public void setWm_poi_phone(String wm_poi_phone) {
        this.wm_poi_phone = wm_poi_phone;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getRecipient_name() {
        return recipient_name;
    }

    public void setRecipient_name(String recipient_name) {
        this.recipient_name = recipient_name;
    }

    public String getRecipient_address() {
        return recipient_address;
    }

    public void setRecipient_address(String recipient_address) {
        this.recipient_address = recipient_address;
    }

    public String getRecipient_phone() {
        return recipient_phone;
    }

    public void setRecipient_phone(String recipient_phone) {
        this.recipient_phone = recipient_phone;
    }

    public String getOrder_send_time() {
        return order_send_time;
    }

    public void setOrder_send_time(String order_send_time) {
        this.order_send_time = order_send_time;
    }

    public String getOrder_receive_time() {
        return order_receive_time;
    }

    public void setOrder_receive_time(String order_receive_time) {
        this.order_receive_time = order_receive_time;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public int getPay_type() {
        return pay_type;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }

    public String getOrder_meal_fee() {
        return order_meal_fee;
    }

    public void setOrder_meal_fee(String order_meal_fee) {
        this.order_meal_fee = order_meal_fee;
    }

    public String getShipping_fee() {
        return shipping_fee;
    }

    public void setShipping_fee(String shipping_fee) {
        this.shipping_fee = shipping_fee;
    }

    public String getShop_other_discount() {
        return shop_other_discount;
    }

    public void setShop_other_discount(String shop_other_discount) {
        this.shop_other_discount = shop_other_discount;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getCaution() {
        return caution;
    }

    public void setCaution(String caution) {
        this.caution = caution;
    }

    public List<Map<String, String>> getGoods() {
        return goods;
    }

    public void setGoods(List<Map<String, String>> goods) {
        this.goods = goods;
    }
}
