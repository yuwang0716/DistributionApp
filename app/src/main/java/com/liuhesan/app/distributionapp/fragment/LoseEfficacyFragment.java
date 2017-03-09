package com.liuhesan.app.distributionapp.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnSureLisener;
import com.codbking.widget.bean.DateType;
import com.liuhesan.app.distributionapp.R;
import com.liuhesan.app.distributionapp.adapter.LoseOrderAdapter;
import com.liuhesan.app.distributionapp.bean.Order;
import com.liuhesan.app.distributionapp.utility.API;
import com.liuhesan.app.distributionapp.utility.TakingOrderJson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Tao on 2016/12/17.
 */
public class LoseEfficacyFragment extends Fragment implements View.OnClickListener {
    private final static String TAG = "LoseEfficacyFragment";
    private View view;
    private TextView tv_startdate,tv_enddate;
    private ListView listView;
    private LoseOrderAdapter loseOrderAdapter;
    private List<Order> orders;
    private List<Order> getOrders;
    private Activity activity;
    private Button query;
    private boolean isLoading;
    private int page=1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order_more_loseefficacy, container, false);
        initView();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }
    private void initView() {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("login",Context.MODE_PRIVATE);
        tv_startdate = (TextView) view.findViewById(R.id.date_start);
        tv_enddate = (TextView) view.findViewById(R.id.date_end);
        query = (Button) view.findViewById(R.id.order_more_loseefficacy_query);
        tv_startdate.setText(sharedPreferences.getString("cdate",""));
        tv_enddate.setText(sharedPreferences.getString("cdate",""));
        getData(page);
        listView = (ListView) view.findViewById(R.id.more_loseorder_listview);
        listView.setVerticalScrollBarEnabled(false);
        tv_startdate.setOnClickListener(this);
        tv_enddate.setOnClickListener(this);
        query.setOnClickListener(this);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (view.getLastVisiblePosition() == view.getCount() - 1 ){
                    getData(++page);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        DatePickDialog dialog = new DatePickDialog(activity);
        switch (v.getId()) {
            case R.id.date_start:
                dialog.setYearLimt(5);
                dialog.setTitle("开始时间");
                dialog.setType(DateType.TYPE_YMD);
                dialog.setMessageFormat("yyyy-MM-dd");
                dialog.setOnChangeLisener(null);
                dialog.setOnSureLisener(new OnSureLisener() {
                    @Override
                    public void onSure(Date date) {
                        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                        String str=sdf.format(date);
                        tv_startdate.setText(str);
                    }
                });
                dialog.show();
                break;
            case R.id.date_end://结束时间
                dialog.setYearLimt(5);
                dialog.setTitle("结束时间");
                dialog.setType(DateType.TYPE_YMD);
                dialog.setMessageFormat("yyyy-MM-dd");
                dialog.setOnChangeLisener(null);
                dialog.setOnSureLisener(new OnSureLisener() {
                    @Override
                    public void onSure(Date date) {
                        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                        String str=sdf.format(date);
                        tv_enddate.setText(str);
                    }
                });
                dialog.show();
                break;
            case R.id.order_more_loseefficacy_query:
                orders.clear();
                loseOrderAdapter.notifyDataSetChanged();
                page = 1;
                getData(page);
                break;
        }
    }
    void getData(int page){
        if (!TextUtils.isEmpty(tv_startdate.getText())&& !TextUtils.isEmpty(tv_enddate.getText())){
            OkGo.post(API.BASEURL+"deliver/getOrder")
                    .params("page",page)
                    .params("pagesize",10)
                    .params("status",4)
                    .params("sdate",tv_startdate.getText().toString().trim())
                    .params("edate",tv_enddate.getText().toString().trim())
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            s = s.toString().replace("null","0");
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                int errno = jsonObject.optInt("errno");
                                if (errno == 200) {
                                    JSONObject data = jsonObject.optJSONObject("data");
                                    JSONArray list = data.optJSONArray("list");
                                    if (list != null) {
                                        if (loseOrderAdapter == null) {
                                            orders = TakingOrderJson.getData(list);
                                            Log.i(TAG, TakingOrderJson.getData(list)+"1onSuccess: "+list);
                                           loseOrderAdapter = new LoseOrderAdapter(orders, getContext());
                                            listView.setAdapter(loseOrderAdapter);
                                        } else {
                                            getOrders = TakingOrderJson.getData(list);
                                            orders.addAll(getOrders);
                                            loseOrderAdapter.notifyDataSetChanged();
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
}
