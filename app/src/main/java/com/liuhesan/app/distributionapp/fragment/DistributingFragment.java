package com.liuhesan.app.distributionapp.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.liuhesan.app.distributionapp.R;
import com.liuhesan.app.distributionapp.adapter.DistributionOrderAdapter;
import com.liuhesan.app.distributionapp.bean.Order;
import com.liuhesan.app.distributionapp.utility.API;
import com.liuhesan.app.distributionapp.utility.ReseizeOrderJson;
import com.liuhesan.app.distributionapp.utility.TakingOrderJson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Tao on 2016/12/10.
 */

public class DistributingFragment extends Fragment {
    private final static  String TAG = "DistributingFragment";
    private View view;
    private ListView mListView;
    private List<Order> orders;
    private List<Order> getOrders;
    private DistributionOrderAdapter distributionOrderAdapter;
    private MaterialRefreshLayout refreshLayout;
    private SharedPreferences sharedPreferences;
    private Activity activity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order_distributing, container, false);
        initView();
        initData();
        refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                initData();
                materialRefreshLayout.finishRefresh();
            }
        });
        refreshLayout.autoRefresh();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    private void initView() {
        mListView = (ListView) view.findViewById(R.id.distributing_listview);
        mListView.setVerticalScrollBarEnabled(false);
        refreshLayout = (MaterialRefreshLayout) view.findViewById(R.id.distributing_refresh);
        int[] colors = {Color.parseColor("#28AAE3")};
        refreshLayout.setProgressColors(colors);
        sharedPreferences = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);

    }
    private void initData() {
        OkGo.post(API.BASEURL+"deliver/getOrder")
                .tag(this)
                .params("token",sharedPreferences.getString("token",""))
                .params("status","3")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        s = s.toString().replace("null","0");
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            int errno = jsonObject.optInt("errno");
                            orders =  ReseizeOrderJson.getData(s);
                            if (errno == 200) {
                                JSONObject data = jsonObject.optJSONObject("data");
                                JSONArray list = data.optJSONArray("list");
                                if (list != null) {
                                    if (distributionOrderAdapter == null) {
                                        orders = TakingOrderJson.getData(list);
                                        Log.i(TAG, TakingOrderJson.getData(list)+"1onSuccess: "+list);
                                        distributionOrderAdapter = new DistributionOrderAdapter(orders, getContext());
                                        mListView.setAdapter(distributionOrderAdapter);
                                    } else {
                                        getOrders = TakingOrderJson.getData(list);
                                       distributionOrderAdapter = new DistributionOrderAdapter(getOrders, getContext());
                                        mListView.setAdapter(distributionOrderAdapter);
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }
}
