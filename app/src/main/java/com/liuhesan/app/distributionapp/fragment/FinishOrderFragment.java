package com.liuhesan.app.distributionapp.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnSureLisener;
import com.codbking.widget.bean.DateType;
import com.liuhesan.app.distributionapp.R;
import com.liuhesan.app.distributionapp.adapter.FinishOrderAdapter;
import com.liuhesan.app.distributionapp.bean.Order;
import com.liuhesan.app.distributionapp.utility.API;
import com.liuhesan.app.distributionapp.utility.TakingOrderJson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Tao on 2016/12/17.
 */
public class FinishOrderFragment extends Fragment implements View.OnClickListener {
    private final static String TAG = "FinishOrderFragment";
    private View view;
    private TextView tv_startdate, tv_enddate;
    private Button all, outtime, intime;
    private RecyclerView mRecyclerView;
    private FinishOrderAdapter finishOrderAdapter;
    private List<Order> orders;
    private List<Order> getOrders;
    private int flag = 3;
    int i = 1;
    private Activity activity;
    private LinearLayoutManager mLayoutManager;
    private int lastVisibleItem;
    private int page = 1;
    private int flag_outTime = 0;
    private Set set;
    private TextView end;
    private ImageView more_null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order_more_finishorder, container, false);
        set = new HashSet();
        orders = new ArrayList<Order>();
        getOrders = new ArrayList<Order>();
        initView();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    private void initView() {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("login", Context.MODE_PRIVATE);
        more_null = (ImageView) view.findViewById(R.id.more_null);
        tv_startdate = (TextView) view.findViewById(R.id.date_start);
        tv_enddate = (TextView) view.findViewById(R.id.date_end);
        tv_startdate.setText(sharedPreferences.getString("cdate",""));
        tv_enddate.setText(sharedPreferences.getString("cdate",""));
        getAllData(1);
        all = (Button) view.findViewById(R.id.all);
        outtime = (Button) view.findViewById(R.id.outtime);
        intime = (Button) view.findViewById(R.id.intime);
        end = (TextView) view.findViewById(R.id.finishorder_end);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.more_finishorder_recyclerview);
        mLayoutManager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 2 >= mLayoutManager.getItemCount()){
                    switch (flag_outTime){
                        case 0:
                            getAllData(++page);
                            break;
                        case 1:
                            getData(++page,1);
                            break;
                        case 2:
                            getData(++page,0);
                            break;
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });
        tv_startdate.setOnClickListener(this);
        tv_enddate.setOnClickListener(this);
        all.setOnClickListener(this);
        outtime.setOnClickListener(this);
        intime.setOnClickListener(this);
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
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
            case R.id.all:
                set.clear();
                flag = 3;
                if (orders != null && finishOrderAdapter != null){
                    orders.clear();
                    finishOrderAdapter.notifyDataSetChanged();
                }
                page = 1;
                getAllData(page);
                all.setBackground(activity.getResources().getDrawable(R.drawable.ordertypebutton));
                outtime.setBackground(activity.getResources().getDrawable(R.drawable.odertypetwobutton));
                intime.setBackground(activity.getResources().getDrawable(R.drawable.odertypetwobutton));
                all.setTextColor(getResources().getColor(R.color.colorText_title));
                outtime.setTextColor(getResources().getColor(R.color.colorPrimary));
                intime.setTextColor(getResources().getColor(R.color.colorPrimary));
                flag_outTime = 0;
                break;
            case R.id.outtime:
                set.clear();
                flag = 1;
                if (orders != null&& finishOrderAdapter != null){
                    orders.clear();
                    finishOrderAdapter.notifyDataSetChanged();
                }
                page = 1;
                getData(page,1);
                outtime.setBackground(activity.getResources().getDrawable(R.drawable.ordertypebutton));
                all.setBackground(activity.getResources().getDrawable(R.drawable.odertypetwobutton));
                intime.setBackground(activity.getResources().getDrawable(R.drawable.odertypetwobutton));
                outtime.setTextColor(getResources().getColor(R.color.colorText_title));
                all.setTextColor(getResources().getColor(R.color.colorPrimary));
                intime.setTextColor(getResources().getColor(R.color.colorPrimary));
                flag_outTime = 1;
                break;
            case R.id.intime:
                set.clear();
                flag = 0;
                if (orders != null&& finishOrderAdapter != null){
                    orders.clear();
                    finishOrderAdapter.notifyDataSetChanged();
                }
                page = 1;
                getData(page,0);
                intime.setBackground(activity.getResources().getDrawable(R.drawable.ordertypebutton));
                outtime.setBackground(activity.getResources().getDrawable(R.drawable.odertypetwobutton));
                all.setBackground(activity.getResources().getDrawable(R.drawable.odertypetwobutton));
                intime.setTextColor(getResources().getColor(R.color.colorText_title));
                outtime.setTextColor(getResources().getColor(R.color.colorPrimary));
                all.setTextColor(getResources().getColor(R.color.colorPrimary));
                flag_outTime = 2;
                break;
        }
    }

   void getData(int page,int time){
       if (!TextUtils.isEmpty(tv_startdate.getText())&& !TextUtils.isEmpty(tv_enddate.getText())){
           OkGo.post(API.BASEURL+"deliver/getOrder")
                   .params("page",page)
                   .params("pagesize",2)
                   .params("status",8)
                   .params("timeout",time)
                   .params("sdate",tv_startdate.getText().toString().trim())
                   .params("edate",tv_enddate.getText().toString().trim())
                   .execute(new StringCallback() {
                       @Override
                       public void onSuccess(String s, Call call, Response response) {
                           s = s.toString().replace("null","0");
                           Log.e(TAG,s+ "onSuccess: ");
                           try {
                               JSONObject jsonObject = new JSONObject(s);
                               int errno = jsonObject.optInt("errno");
                               if (errno == 200) {
                                   JSONObject data = jsonObject.optJSONObject("data");
                                   JSONArray list = data.optJSONArray("list");
                                   if (list != null) {
                                                if (finishOrderAdapter == null) {
                                                    orders.clear();
                                                    orders = TakingOrderJson.getData(list);
                                                    if (orders.size() == 0){
                                                        more_null.setVisibility(View.VISIBLE);
                                                    }else {
                                                        more_null.setVisibility(View.GONE);
                                                    }
                                                    Log.i(TAG, TakingOrderJson.getData(list)+"1onSuccess: "+list);
                                                    finishOrderAdapter = new FinishOrderAdapter(orders, getContext());
                                                    mRecyclerView.setAdapter(finishOrderAdapter);
                                                } else {
                                                    getOrders.clear();
                                                    getOrders = TakingOrderJson.getData(list);
                                                    if (getOrders.size() == 0){
                                                        more_null.setVisibility(View.VISIBLE);
                                                    }else {
                                                        more_null.setVisibility(View.GONE);
                                                    }
                                                    if (getOrders.size() == 0){
                                                        end.setVisibility(View.VISIBLE);
                                                    }else {
                                                        if (set.add(getOrders)) {
                                                            orders.addAll(getOrders);
                                                            finishOrderAdapter.notifyDataSetChanged();
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
    }
    void getAllData(int page){
        if (!TextUtils.isEmpty(tv_startdate.getText())&& !TextUtils.isEmpty(tv_enddate.getText())){
            OkGo.post(API.BASEURL+"deliver/getOrder")
                    .params("page",page)
                    .params("pagesize",10)
                    .params("status",8)
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
                                        if (finishOrderAdapter == null) {

                                            orders.clear();
                                            orders = TakingOrderJson.getData(list);
                                            finishOrderAdapter = new FinishOrderAdapter(orders, getContext());
                                            mRecyclerView.setAdapter(finishOrderAdapter);
                                            if (orders.size() == 0){
                                                more_null.setVisibility(View.VISIBLE);
                                            }else {
                                                more_null.setVisibility(View.GONE);
                                            }
                                        } else {

                                            getOrders.clear();
                                            getOrders = TakingOrderJson.getData(list);
                                            if (getOrders.size() == 0){
                                                more_null.setVisibility(View.VISIBLE);
                                            }else {
                                                more_null.setVisibility(View.GONE);
                                            }
                                            if (set.add(getOrders)) {
                                                orders.addAll(getOrders);
                                                finishOrderAdapter.notifyDataSetChanged();
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
    }
}
