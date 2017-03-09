package com.liuhesan.app.distributionapp.fragment;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.liuhesan.app.distributionapp.R;
import com.liuhesan.app.distributionapp.adapter.ReseizeOrderAdapter;
import com.liuhesan.app.distributionapp.bean.Order;
import com.liuhesan.app.distributionapp.service.LocationService;
import com.liuhesan.app.distributionapp.ui.personcenter.LoginActivity;
import com.liuhesan.app.distributionapp.utility.AppManager;
import com.liuhesan.app.distributionapp.utility.ReseizeOrderJson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Tao on 2016/12/10.
 */

public class ReseizeOrderFragment extends Fragment {
    private final static String TAG = "ReseizeOrderFragment";
    private View view;
    private RecyclerView mRecyclerView;
    private List<Order> orders;
    private ReseizeOrderAdapter reseizeOrderAdapter;
    private Set set;
    private Map<String, List<Order>> map;
    private Activity activity;
    private boolean workstate;
    private IntentFilter intentFilter, intentFilter_msg,intentFilter_notification;
    private WorkstateReceive workstateReceive;
    private MessageReceive messageReceive;
    private NotificationReceive  notificationReceive;
    private LocalBroadcastManager localBroadcastManager;
    private boolean isSound, isShake;
    private List<Order> data;
    private boolean isRegisterMessageReceive;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order_reseizeorder, container, false);
        localBroadcastManager = LocalBroadcastManager.getInstance(activity);
        initView();
        //推送接收注册
        intentFilter_msg = new IntentFilter();
        intentFilter_msg.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        intentFilter_msg.addAction("com.liuhesan.app.distributionapp.JpushMessage");
        messageReceive = new MessageReceive();
        localBroadcastManager.registerReceiver(messageReceive , intentFilter_msg);

        //工作状态接收注册
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.liuhesan.app.distributionapp.WorkState");
        workstateReceive = new WorkstateReceive();
        localBroadcastManager.registerReceiver(workstateReceive, intentFilter);

        //通知接收注册
        intentFilter_notification = new IntentFilter();
        intentFilter_notification.addAction("com.liuhesan.app.distributionapp.Notification");
        notificationReceive = new NotificationReceive();
        localBroadcastManager.registerReceiver(notificationReceive, intentFilter_notification);

        SharedPreferences sharedPreferences = activity.getSharedPreferences("login",Context.MODE_PRIVATE);
        if (sharedPreferences.getInt("status",0) == 1){
            workstate = true;
        }
        isSound = sharedPreferences.getBoolean("isSound", false);
        isShake = sharedPreferences.getBoolean("isShake", false);
        initWorkstate();
        return view;
    }

    private void initWorkstate() {
        if (workstate) {
            if (!isRegisterMessageReceive) {
                localBroadcastManager.registerReceiver(messageReceive, intentFilter_msg);
                isRegisterMessageReceive = true;
            }
        } else {
            if (isRegisterMessageReceive) {
                localBroadcastManager.unregisterReceiver(messageReceive);
                isRegisterMessageReceive = false;
            }
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;

    }

    private void initView() {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.reseizeorder_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        orders = new ArrayList<>();
        set = new HashSet();
        map = new HashMap<>();

    }

    private void initData() {
        if (reseizeOrderAdapter == null) {
            reseizeOrderAdapter = new ReseizeOrderAdapter(orders, getContext());
            mRecyclerView.setAdapter(reseizeOrderAdapter);
        }
    }

    private void setCostomMsg(List<Order> data) {
        if (data.get(0).getAct().toString().trim().equals("n")) {
            if (set.add(data.get(0).getSn())) {
                Intent intent_order = new Intent("com.liuhesan.app.distributionapp.ISREDDOTNEWS");
                intent_order.putExtra("isreddotoder", true);
                localBroadcastManager.sendBroadcast(intent_order);
                map.put(data.get(0).getSn(), data);
                if (reseizeOrderAdapter != null) {
                    orders.addAll(0, map.get(data.get(0).getSn()));
                    reseizeOrderAdapter.notifyItemInserted(0);
                    reseizeOrderAdapter.notifyItemRangeChanged(0, orders.size());
                } else {
                    orders = map.get(data.get(0).getSn());
                }
                Intent intent = new Intent(activity,com.liuhesan.app.distributionapp.ui.personcenter.MainActivity.class);
                intent.putExtra("notification","notification");
                PendingIntent pi = PendingIntent.getActivity(getContext(), 0, intent, 0);
                NotificationManager manager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notification = new NotificationCompat.Builder(getContext())
                        .setContentTitle("有一笔新订单!!")
                        //  .setContentText(data.get(0).getName())
                        .setWhen(System.currentTimeMillis())
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.logo))
                        .setContentIntent(pi)
                        .setAutoCancel(true)
                        .setSmallIcon(R.mipmap.logo)
                        .build();
                int i = 0;
                NotificationCompat.Builder builder = new NotificationCompat.Builder(activity);
                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                if (isSound) {
                    notification.sound = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.new_order1);
                    notification.flags |= Notification.FLAG_INSISTENT;
                } else {
                    builder.build().sound = null;
                }
                if (isShake) {
                    notification.vibrate = new long[]{0,1000,1000,1000};
                } else {
                    notification.vibrate = null;
                }
                manager.notify(i++, notification);
            }
        }


        initData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        localBroadcastManager.unregisterReceiver(workstateReceive);
        localBroadcastManager.unregisterReceiver(notificationReceive);
        if (isRegisterMessageReceive)
            localBroadcastManager.unregisterReceiver(messageReceive);
    }

    class MessageReceive extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, intent.getStringExtra("message") + "Message: ");
            //极光推送
            String msg = intent.getStringExtra("message");
            if (!TextUtils.isEmpty(msg)) {
                msg = msg.toString().replace("null","0");
                data = ReseizeOrderJson.getData(msg);
                if (workstate) {
                    setCostomMsg(data);
                }

                //取消订单操作
                if (data.get(0).getAct().toString().trim().equals("c")) {
                    if (map.containsKey(data.get(0).getSn())) {
                        int position = 0 - orders.indexOf(map.get(data.get(0).getSn())) - 1;
                        orders.remove(position);
                        reseizeOrderAdapter.notifyItemRemoved(position);
                        set.remove(data.get(0).getSn());
                    }

                }
                if (data.get(0).getAct().toString().trim().equals("cc")){
                    Intent intent_cancel = new Intent("com.liuhesan.app.distributionapp.cancelOrder");
                    intent_cancel.putExtra("orderid",data.get(0).getOrderid());
                    localBroadcastManager.sendBroadcast(intent_cancel);
                }
                //新消息操作
                if (data.get(0).getAct().toString().trim().equals("m")) {
                    Intent intent_new = new Intent("com.liuhesan.app.distributionapp.ISREDDOTNEWS");
                    intent_new.putExtra("isreddotnews", true);
                    localBroadcastManager.sendBroadcast(intent_new);
                }
                //移动端排斥
                if (data.get(0).getAct().toString().trim().equals("t")) {
                    try {
                        JSONObject jsonObject = new JSONObject(msg);
                        JSONObject data_mass = jsonObject.optJSONObject("data");
                        String mass = data_mass.optString("mass");
                        SharedPreferences sharedPreferences = activity.getSharedPreferences("login", Context.MODE_PRIVATE);
                        String phoneid = sharedPreferences.getString("phoneid", "");
                        if (!mass.equals(phoneid)) {
                            AppManager.getAppManager().finishAllActivity();
                            sharedPreferences.edit().clear().commit();
                            startActivity(new Intent(activity, LoginActivity.class));
                            Toast.makeText(activity, "你的账号在其他地方登录，非本人登录，请及时修改密码", Toast.LENGTH_SHORT).show();
                            activity.stopService(new Intent(activity, LocationService.class));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    class WorkstateReceive extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            //工作状态
            workstate = intent.getBooleanExtra("workstate", false);
            Log.i(TAG, workstate + "onReceive: ");
            initWorkstate();

        }
    }
    class NotificationReceive extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //通知管理
            isSound = intent.getBooleanExtra("isSound", false);
            isShake = intent.getBooleanExtra("isShake", false);
        }
    }
}