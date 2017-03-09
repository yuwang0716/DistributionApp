package com.liuhesan.app.distributionapp.bean;

/**
 * Created by Tao on 2016/12/20.
 */

public class Order {

    private String act;
    private String sn;
    private String orderid;
    private String poi_name;
    private String poi_addr;
    private String poi_mob;
    private double poi_lat;
    private double poi_lng;
    private String price;
    private int paytype;
    private String name;
    private String addr;
    private String mob;
    private double lat;
    private double lng;
    private long outTime;
    private long startTime;
    private long endTime;
    private boolean isOutTime;
    private String reason;
    private long getOrderTime;

    public long getGetOrderTime() {
        return getOrderTime;
    }

    public void setGetOrderTime(long getOrderTime) {
        this.getOrderTime = getOrderTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isOutTime() {
        return isOutTime;
    }

    public void setOutTime(boolean outTime) {
        isOutTime = outTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getAct() {
        return act;
    }

    public void setAct(String act) {
        this.act = act;
    }

    public long getOutTime() {
        return outTime;
    }

    public void setOutTime(long outTime) {
        this.outTime = outTime;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getPoi_name() {
        return poi_name;
    }

    public void setPoi_name(String poi_name) {
        this.poi_name = poi_name;
    }

    public String getPoi_addr() {
        return poi_addr;
    }

    public void setPoi_addr(String poi_addr) {
        this.poi_addr = poi_addr;
    }

    public String getPoi_mob() {
        return poi_mob;
    }

    public void setPoi_mob(String poi_mob) {
        this.poi_mob = poi_mob;
    }

    public double getPoi_lat() {
        return poi_lat;
    }

    public void setPoi_lat(double poi_lat) {
        this.poi_lat = poi_lat;
    }

    public double getPoi_lng() {
        return poi_lng;
    }

    public void setPoi_lng(double poi_lng) {
        this.poi_lng = poi_lng;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getPaytype() {
        return paytype;
    }

    public void setPaytype(int paytype) {
        this.paytype = paytype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

}
