package com.liuhesan.app.distributionapp.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.liuhesan.app.distributionapp.R;
import com.liuhesan.app.distributionapp.adapter.GotOrderAdapter;
import com.liuhesan.app.distributionapp.bean.Order;
import com.liuhesan.app.distributionapp.utility.API;
import com.liuhesan.app.distributionapp.utility.TakingOrderJson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Tao on 2016/12/10.
 */

public class TakingGoodsFragment extends Fragment {
    private final static String TAG = "TakingGoodsFragment";
    private View view;
    private ListView mListView;
    private List<Order> orders;
    private GotOrderAdapter gotOrderAdapter;
    private MaterialRefreshLayout refreshLayout;
    private SharedPreferences sharedPreferences;
    private IntentFilter intentFilter;
    private CancelReceive cancelReceive;
    private Map<String, Integer> map;
    private LocalBroadcastManager localBroadcastManager;
    private Activity activity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order_takinggoods, container, false);
        localBroadcastManager = LocalBroadcastManager.getInstance(activity);
        map = new HashMap<>();
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.liuhesan.app.distributionapp.cancelOrder");
        cancelReceive = new CancelReceive();
        localBroadcastManager.registerReceiver(cancelReceive, intentFilter);
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
        mListView = (ListView) view.findViewById(R.id.takinggoods_listview);
        mListView.setVerticalScrollBarEnabled(false);
        refreshLayout = (MaterialRefreshLayout) view.findViewById(R.id.refresh);
        int[] colors = {Color.parseColor("#28AAE3")};
        refreshLayout.setProgressColors(colors);
        orders = new ArrayList<>();
        sharedPreferences = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
    }

    private void initData() {
        OkGo.post(API.BASEURL + "deliver/getOrder")
                .tag(this)
                .params("token", sharedPreferences.getString("token", ""))
                .params("status", "2")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                       s = s.toString().replace("null","0");
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            int errno = jsonObject.optInt("errno");
                            if (errno == 200) {
                                JSONObject data = jsonObject.optJSONObject("data");
                                if (data != null) {
                                    JSONArray list = data.optJSONArray("list");
                                    if (list != null) {
                                        if (gotOrderAdapter == null) {
                                            orders = TakingOrderJson.getData(list);
                                            gotOrderAdapter = new GotOrderAdapter(orders, getContext());
                                            mListView.setAdapter(gotOrderAdapter);
                                            for (int i = 0; i < orders.size(); i++) {
                                                map.put(orders.get(i).getOrderid(),i);
                                            }
                                        } else {
                                            orders.clear();
                                            orders = TakingOrderJson.getData(list);
                                            gotOrderAdapter = new GotOrderAdapter(orders, getContext());
                                            mListView.setAdapter(gotOrderAdapter);
                                            for (int i = 0; i < orders.size(); i++) {
                                                map.put(orders.get(i).getOrderid(),i);
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        localBroadcastManager.unregisterReceiver(cancelReceive);
    }

    class CancelReceive extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String orderid = intent.getStringExtra("orderid");
            if (map.containsKey(orderid)){
                int num = map.get(orderid);
                orders.remove(num);
                gotOrderAdapter.notifyDataSetChanged();
            }
        }
    }
}
