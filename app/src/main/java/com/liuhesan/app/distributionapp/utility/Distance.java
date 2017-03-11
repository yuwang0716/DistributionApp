package com.liuhesan.app.distributionapp.utility;

import android.content.Context;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.RouteSearch;

/**
 * Created by Tao on 2017/3/11.
 */

public class Distance {

    public static void getDistance(LatLonPoint mStartPoint, LatLonPoint mEndPoint, Context context, RouteSearch.OnRouteSearchListener searchListener){
         RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(mStartPoint, mEndPoint);
         RouteSearch.RideRouteQuery query = new RouteSearch.RideRouteQuery(fromAndTo, RouteSearch.RidingDefault);
         RouteSearch mRouteSearch = new RouteSearch(context);
         mRouteSearch.calculateRideRouteAsyn(query);
         mRouteSearch.setRouteSearchListener(searchListener);

    }
}

