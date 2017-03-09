package com.liuhesan.app.distributionapp.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.liuhesan.app.distributionapp.R;
import com.liuhesan.app.distributionapp.service.LocationService;
import com.liuhesan.app.distributionapp.utility.API;
import com.liuhesan.app.distributionapp.utility.HookViewClickUtil;
import com.liuhesan.app.distributionapp.utility.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;
import shanyao.tabpagerindictor.TabPageIndicator;

/**
 * Created by Tao on 2016/12/10.
 */

public class OrderFragment extends Fragment implements View.OnClickListener {
    private final static String TAG = "OrderFragment";
    private View view;
    private Toolbar mToolbar;
    private TabPageIndicator indicator;
    private ViewPager viewPager;
    private BasePagerAdapter adapter;
    private ImageButton bt_wrokstate;
    private PopupWindow popupwindow;
    private Activity activity;
    private RadioButton work,rest;
    private TextView tv_work,tv_rest;
    private boolean workstate;
    private SharedPreferences sharedPreferences;
    private LinearLayout ll_work,ll_rest;
    private LocalBroadcastManager localBroadcastManager;
    private Intent intent;
    private IntentFilter intentFilter;
    private NotificationReceive notificationReceive;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order, container, false);
        localBroadcastManager = LocalBroadcastManager.getInstance(activity);
        intentFilter = new IntentFilter();
        intentFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        intentFilter.addAction("com.liuhesan.app.distributionapp.news");
        notificationReceive = new NotificationReceive();
        localBroadcastManager.registerReceiver(notificationReceive, intentFilter);
        activity.getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                HookViewClickUtil.hookView(bt_wrokstate);
            }
        });
        initView();
        return view;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initView() {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        //后期加上二维码传递信息
       /* mToolbar.inflateMenu(R.menu.order_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(getContext(), "扫描", Toast.LENGTH_SHORT).show();
                return true;
            }
        });*/
        indicator = (TabPageIndicator) view.findViewById(R.id.indicator);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        adapter = new BasePagerAdapter(getChildFragmentManager());
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(adapter);
        indicator.setViewPager(viewPager);
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                MaterialRefreshLayout refreshLayout;
                if (position == 1){
                    refreshLayout = (MaterialRefreshLayout) view.findViewById(R.id.refresh);
                    refreshLayout.autoRefresh();
                }
                if (position == 2){
                    refreshLayout = (MaterialRefreshLayout) view.findViewById(R.id.distributing_refresh);
                    refreshLayout.autoRefresh();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setTabPagerIndicator();
        //工作状态
        sharedPreferences = activity.getSharedPreferences("login", Context.MODE_PRIVATE);
        bt_wrokstate = (ImageButton) view.findViewById(R.id.order_workstate);
        intent = new Intent("com.liuhesan.app.distributionapp.WorkState");
        if (sharedPreferences.getInt("status",0) == 1){

                bt_wrokstate.setBackground(activity.getResources().getDrawable(R.mipmap.toolbar_workstate));
                workstate = true;
                activity.startService(new Intent(activity, LocationService.class));
                intent.putExtra("workstate",true);
                localBroadcastManager.sendBroadcast(intent);

        }else {
            bt_wrokstate.setBackground(activity.getResources().getDrawable(R.mipmap.toolbar_reststate));
            workstate = false;
            activity.stopService(new Intent(activity, LocationService.class));
            intent.putExtra("workstate",false);
            localBroadcastManager.sendBroadcast(intent);
        }
        bt_wrokstate.setOnClickListener(this);
    }

    private void setTabPagerIndicator() {
        indicator.setIndicatorMode(TabPageIndicator.IndicatorMode.MODE_WEIGHT_NOEXPAND_SAME);// 设置模式，一定要先设置模式
        indicator.setIndicatorColor(Color.parseColor("#28AAE3"));// 设置底部导航线的颜色
        indicator.setDividerColor(Color.parseColor("#ffffff"));// 设置分割线的颜色
        indicator.setDividerPadding(10);//设置
        indicator.setTextColorSelected(Color.parseColor("#28AAE3"));// 设置tab标题选中的颜色
        indicator.setTextColor(Color.parseColor("#979696"));// 设置tab标题未被选中的颜色
        indicator.setBackgroundColor(Color.parseColor("#ffffff"));
        indicator.setTextSize((int) (getResources().getDimension(R.dimen.x28)));// 设置字体大小
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.order_workstate:
                if (popupwindow != null&&popupwindow.isShowing()) {
                    popupwindow.dismiss();
                    return;
                } else {
                    initmPopupWindowView();
                    popupwindow.showAsDropDown(v, 0, 10);
                }
                break;
            case R.id.ll_workstate_work:
                if (sharedPreferences.getInt("isauth",0) == 1){
                tv_work.setTextColor(Color.parseColor("#28AAE3"));
                tv_rest.setTextColor(Color.BLACK);
                work.setChecked(true);
                rest.setChecked(false);
                workstate = true;
                bt_wrokstate.setBackground(activity.getResources().getDrawable(R.mipmap.toolbar_workstate));
                OkGo.post(API.BASEURL + "deliver/workStatus/")
                        .tag(this)
                        .params("status", 1)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(s);
                                    int errno = jsonObject.optInt("errno");
                                    if (errno == 200){
                                        activity.startService(new Intent(activity, LocationService.class));
                                        SharedPreferences.Editor edit = sharedPreferences.edit();
                                        edit.putInt("status",1);
                                        edit.commit();
                                        intent.putExtra("workstate",true);
                                        localBroadcastManager.sendBroadcast(intent);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                }else {
                    ToastUtil.showToast(activity,"手机认证后才可以接单");
                }
                break;
            case R.id.ll_workstate_rest:
                tv_rest.setTextColor(Color.parseColor("#28AAE3"));
                tv_work.setTextColor(Color.BLACK);
                work.setChecked(false);
                rest.setChecked(true);
                workstate = false;
                bt_wrokstate.setBackground(activity.getResources().getDrawable(R.mipmap.toolbar_reststate));
                OkGo.post(API.BASEURL+"deliver/workStatus/")
                        .tag(this)
                        .params("status",0)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(s);
                                    int errno = jsonObject.optInt("errno");
                                    if (errno == 200){
                                        activity.stopService(new Intent(activity, LocationService.class));
                                        sharedPreferences = activity.getSharedPreferences("login", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor edit = sharedPreferences.edit();
                                        edit.putInt("status",0);
                                        edit.commit();
                                        intent.putExtra("workstate",false);
                                        localBroadcastManager.sendBroadcast(intent);
                                        Log.i(TAG, sharedPreferences.getInt("status",0)+"onSuccess: ");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                break;
        }

    }

    private void initmPopupWindowView() {
        View customView = View.inflate(activity,R.layout.popview_item,null);
        popupwindow = new PopupWindow(customView,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        //popupwindow.setAnimationStyle(R.style.AnimationFade);
        popupwindow.setOutsideTouchable(true);
        customView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (popupwindow != null && popupwindow.isShowing()) {
                    popupwindow.dismiss();
                    popupwindow = null;
                }

                return false;
            }
        });
        ll_work = (LinearLayout) customView.findViewById(R.id.ll_workstate_work);
        ll_rest = (LinearLayout) customView.findViewById(R.id.ll_workstate_rest);
        work = (RadioButton) customView.findViewById(R.id.rb_workstate_work);
        rest = (RadioButton) customView.findViewById(R.id.rb_workstate_rest);
        tv_work = (TextView) customView.findViewById(R.id.tv_workstate_work);
        tv_rest = (TextView) customView.findViewById(R.id.tv_workstate_rest);
        if (workstate){
            work.setChecked(true);
            tv_work.setTextColor(Color.parseColor("#28AAE3"));
            tv_rest.setTextColor(Color.BLACK);
        }else {
            rest.setChecked(true);
            tv_rest.setTextColor(Color.parseColor("#28AAE3"));
            tv_work.setTextColor(Color.BLACK);
        }
        ll_work.setOnClickListener(this);
        ll_rest.setOnClickListener(this);
    }

    private class BasePagerAdapter extends FragmentPagerAdapter {
        String[] titles;

        public BasePagerAdapter(FragmentManager fm) {
            super(fm);
            this.titles = getResources().getStringArray(R.array.order_titles);
        }
        @Override
        public Fragment getItem(int position) {
            List<Fragment> fragments = new ArrayList<>();
            ReseizeOrderFragment reseizeOrderFragment = new ReseizeOrderFragment();
            TakingGoodsFragment takingGoodsFragment = new TakingGoodsFragment();
            DistributingFragment distributingFragment = new DistributingFragment();
            MoreFragment moreFragment = new MoreFragment();
            fragments.add(reseizeOrderFragment);
            fragments.add(takingGoodsFragment);
            fragments.add(distributingFragment);
            fragments.add(moreFragment);

            FragmentFactory fragmentFactory = new FragmentFactory(fragments);

            return fragmentFactory.createForNoExpand(position);
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        localBroadcastManager.unregisterReceiver(notificationReceive);
    }

    class NotificationReceive extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
           String notification = intent.getStringExtra("notification");
           String timeout = intent.getStringExtra("timeout");
            if (!TextUtils.isEmpty(notification)){
                if (notification.equals("notification")){
                    viewPager.setCurrentItem(0);
                }
            }
            if (!TextUtils.isEmpty(timeout)){
                if (timeout.equals("timeout")){
                    viewPager.setCurrentItem(2);
                }
            }
        }
    }

}
