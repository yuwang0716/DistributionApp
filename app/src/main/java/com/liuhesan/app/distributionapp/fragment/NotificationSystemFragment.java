package com.liuhesan.app.distributionapp.fragment;

import android.app.Activity;
import android.content.Context;
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

public class NotificationSystemFragment extends Fragment {
    private final static String TAG = "SystemFragment";
    private View view;
    private ListView mListView;
    private List<Map<String,Object>> datas;
    private NotificationsAdapter mAdapter;
    private Activity mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notificationsystem, container, false);
        initView();
        initData();
        return view;
    }

    private void initData() {
        datas = new ArrayList<>();
        OkGo.post(API.BASEURL+"deliver/sysList")
                .tag(this)
                .params("page",1)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.i(TAG,s+ "onSuccess: ");
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
                        mAdapter = new NotificationsAdapter(datas,mContext,"system");
                        mListView.setAdapter(mAdapter);
                    }
                });



    }


    private void initView() {
        mListView = (ListView) view.findViewById(R.id.notification_system_listview);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (Activity) context;
    }
}
