package com.liuhesan.app.distributionapp.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.liuhesan.app.distributionapp.R;
import com.liuhesan.app.distributionapp.adapter.NotificationsAdapter;
import com.liuhesan.app.distributionapp.ui.personcenter.NotificationContentActivity;
import com.liuhesan.app.distributionapp.utility.API;
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
 * Created by Tao on 2017/1/6.
 */

public class NotificationMerchantFragment extends Fragment {
    private final static String TAG = "MerchantFragment";
    private View view;
    private ListView mListView;
    private List<Map<String,Object>> datas;
    private NotificationsAdapter mAdapter;
    private Activity mContext;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notificationmerchant, container, false);
        initData();
        initView();
        return view;
    }

    private void initData() {
        datas = new ArrayList<>();
        OkGo.post(API.BASEURL+"deliver/articleList")
                .tag(this)
                .params("page",1)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optInt("errno")==200){
                                JSONArray data = jsonObject.optJSONArray("data");
                                for (int i = 0; i < data.length(); i++) {
                                    Map<String,Object> map = new HashMap();
                                    JSONObject object = data.optJSONObject(i);
                                    String title = object.optString("title");
                                    int id = object.optInt("id");
                                    map.put("title",title);
                                    map.put("id",id);
                                    datas.add(map);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mAdapter = new NotificationsAdapter(datas,mContext,"merchant");
                        mListView.setAdapter(mAdapter);
                    }
                });
    }

    private void initView() {
        mListView = (ListView) view.findViewById(R.id.notification_merchant_listview);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (Activity) context;
    }
}
