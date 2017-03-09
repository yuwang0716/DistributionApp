package com.liuhesan.app.distributionapp.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liuhesan.app.distributionapp.R;
import com.liuhesan.app.distributionapp.ui.personcenter.DetailsActivity;
import com.liuhesan.app.distributionapp.ui.personcenter.NotificationActivity;
import com.liuhesan.app.distributionapp.utility.API;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Tao on 2016/12/10.
 * 
 */

public class TotalFragment extends Fragment implements View.OnClickListener {
    private final static String TAG = "TotalFragment";
    private View view;
    private Toolbar mToolbar;
    private TextView ranking,tv_todayorders,tv_cancelorders, tv_todayincome,tv_todayloss,tv_totalincome,
            tv_punctuality,tv_mile,tv_complete;
    private SwipeRefreshLayout refreshLayout;
    private LinearLayout tv_today_orders_details,tv_cancel_details,tv_todayincome_details,tv_todayloss_details,tv_totalincome_details,tv_mile_details;
    private String todayorder,cancelorder,todayincome,todayloss,totalincome,totalmile,punctuality,takegoodspercent;
    private Activity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_total, container, false);
        initView();
        initData();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                initData();
                refreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    private void initData() {
        OkGo.post(API.BASEURL+"deliver/totalOrder")
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        s = s.toString().replace("null","0");
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            int errno = jsonObject.optInt("errno");
                            if (errno == 200){
                                JSONObject data = jsonObject.optJSONObject("data");
                                JSONObject current = data.optJSONObject("current");
                                JSONObject count = data.optJSONObject("count");
                                todayorder = current.optString("cd");
                                cancelorder = data.optString("fe");
                                todayincome = current.optString("cr");
                                if (todayincome.equals("null"))
                                    todayincome = "0.00";
                                todayloss = data.optString("fr");
                                totalmile = count.optString("countc");
                                totalincome = count.optString("countr");
                                if (todayloss.equals("null"))
                                    todayloss = "0.00";
                                if (totalincome.equals("null"))
                                    totalincome = "0.00";
                                if (totalmile.equals("null"))
                                    totalmile = "0.00";
                                punctuality = data.optString("ok");
                                takegoodspercent = data.optString("timelapse");

                                //今日订单
                                String str_todayorder ="<font color='#FF0000'>"+todayorder+"</font>单";
                                tv_todayorders.setText(Html.fromHtml(str_todayorder));

                                //取消订单
                                String str_cancel_orders ="<font color='#FF0000'>"+cancelorder+"</font>单";
                                tv_cancelorders.setText(Html.fromHtml(str_cancel_orders));

                                //今日收入
                                String str_todayincome ="<font color='#FF0000'>"+todayincome+"</font>元";
                                tv_todayincome.setText(Html.fromHtml(str_todayincome));

                                //今日损失
                                String str_todayloss = "<font color='#FF0000'>"+todayloss+"</font>元";
                                tv_todayloss.setText(Html.fromHtml(str_todayloss));

                                //累计收入
                                String str_totalincome ="<font color='#FF0000'>"+totalincome+"</font>元";
                                tv_totalincome.setText(Html.fromHtml(str_totalincome));

                                //累计里程
                                String str_mile ="<font color='#FF0000'>"+totalmile+"</font>km";
                                tv_mile.setText(Html.fromHtml(str_mile));

                                //工作效率
                                tv_punctuality.setText("准时率\n\r"+punctuality);
                                tv_complete.setText("完成率\n\r"+takegoodspercent);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void initView() {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolbar.inflateMenu(R.menu.total_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                startActivity(new Intent(activity, NotificationActivity.class));
                return true;
            }
        });
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        refreshLayout.setColorSchemeResources(R.color.colorText_get);
        tv_todayorders = (TextView)view.findViewById(R.id.today_orders);
        tv_today_orders_details = (LinearLayout) view.findViewById(R.id.today_orders_details);
        tv_cancelorders = (TextView)view.findViewById(R.id.cancel_orders);
        tv_cancel_details = (LinearLayout) view.findViewById(R.id.cancel_orders_details);
        tv_todayincome = (TextView)view.findViewById(R.id.todayincome);
        tv_todayincome_details = (LinearLayout) view.findViewById(R.id.todayincome_details);
        tv_todayloss = (TextView) view.findViewById(R.id.todayloss);
        tv_todayloss_details = (LinearLayout) view.findViewById(R.id.todayloss_details);
        tv_totalincome = (TextView) view.findViewById(R.id.totalincome);
        tv_totalincome_details = (LinearLayout) view.findViewById(R.id.totalincome_details);
        tv_mile = (TextView) view.findViewById(R.id.mile);
        tv_mile_details = (LinearLayout) view.findViewById(R.id.mile_details);
        tv_punctuality = (TextView) view.findViewById(R.id.punctuality);
        tv_complete = (TextView) view.findViewById(R.id.complete);

        tv_today_orders_details.setOnClickListener(this);
        tv_cancel_details.setOnClickListener(this);
        tv_todayincome_details.setOnClickListener(this);
        tv_todayloss_details.setOnClickListener(this);
        tv_totalincome_details.setOnClickListener(this);
        tv_mile_details.setOnClickListener(this);
        tv_punctuality.setOnClickListener(this);
        tv_complete.setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(activity, DetailsActivity.class);
        switch (v.getId()){
            case R.id.today_orders_details:
                intent.putExtra("url",API.BASEURL+"deliver/corder");
                intent.putExtra("act","");
                intent.putExtra("title","今日订单");
                break;
            case R.id.cancel_orders_details:
                intent.putExtra("url",API.BASEURL+"deliver/offorder");
                intent.putExtra("act","");
                intent.putExtra("title","取消订单");
                break;
            case R.id.todayincome_details:
                intent.putExtra("url",API.BASEURL+"deliver/index");
                intent.putExtra("act","1");
                intent.putExtra("title","今日收入");
                break;
            case R.id.todayloss_details:
                intent.putExtra("url",API.BASEURL+"deliver/indexloss");
                intent.putExtra("act","2");
                intent.putExtra("title","今日损失");
                break;
            case R.id.totalincome_details:
                intent.putExtra("url",API.BASEURL+"deliver/reward");
                intent.putExtra("act","");
                intent.putExtra("title","累计收入");
                break;
            case R.id.mile_details:
                intent.putExtra("url",API.BASEURL+"deliver/mileage");
                intent.putExtra("act","");
                intent.putExtra("title","累计里程");
                break;
            case R.id.punctuality:
                intent.putExtra("url",API.BASEURL+"deliver/efficiency");
                intent.putExtra("act","");
                intent.putExtra("title","准时率");
                break;
            case R.id.complete:
                intent.putExtra("url",API.BASEURL+"deliver/getAllOrder");
                intent.putExtra("act","");
                intent.putExtra("title","完成率");
                break;
        }
        startActivity(intent);
    }
}
