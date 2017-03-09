package com.liuhesan.app.distributionapp.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.liuhesan.app.distributionapp.R;
import com.liuhesan.app.distributionapp.ui.personcenter.CompleteDetailsActivity;
import com.liuhesan.app.distributionapp.ui.personcenter.MyPropertyActivity;
import com.liuhesan.app.distributionapp.ui.personcenter.NotificationActivity;
import com.liuhesan.app.distributionapp.ui.personcenter.SystemSettingActivity;
import com.liuhesan.app.distributionapp.utility.API;
import com.liuhesan.app.distributionapp.utility.MyApplication;
import com.liuhesan.app.distributionapp.widget.CircleImageView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Tao on 2016/12/10.
 */

public class MineFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "MineFragment";
    private  View view;
    private Toolbar toolbar;
    private TextView tv_finishorders,tv_totalmile,tv_totalmoney,mine_workstate,
                    username,tv_phone_verification;
    private static CircleImageView mCircleImageView;
    private Activity mContext;
    private ImageView msgdot;
    private boolean isReddot;
    private LinearLayout notification,myproperty,completeDetails,systemSetting;
    private String url;
    private IntentFilter intentFilter,intentFilter_news,intentFilter_phone;
    private ReseizeReceive localReceive;
    private NewsReceive newsReceive;
    private PhoneReceive phoneReceive;
    private LocalBroadcastManager localBroadcastManager;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine, container, false);
        intentFilter = new IntentFilter();
        localBroadcastManager = LocalBroadcastManager.getInstance(mContext);
        intentFilter.addAction("com.liuhesan.app.distributionapp.MINEFRAGMENT");
        localReceive = new ReseizeReceive();
        localBroadcastManager.registerReceiver(localReceive,intentFilter);

        intentFilter_news = new IntentFilter();
        intentFilter_news.addAction("com.liuhesan.app.distributionapp.ISREDDOTNEWS");
        newsReceive = new NewsReceive();
        localBroadcastManager.registerReceiver(newsReceive,intentFilter_news);

        intentFilter_phone = new IntentFilter();
        intentFilter_phone.addAction("com.liuhesan.app.distributionapp.PHONEVERIFICATION");
        phoneReceive = new PhoneReceive();
        localBroadcastManager.registerReceiver(phoneReceive,intentFilter_phone);
        initView();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (Activity) context;

    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView() {
        SharedPreferences sharedPreferences =  mContext.getSharedPreferences("login",Context.MODE_PRIVATE);

        //设置头像
        mCircleImageView = (CircleImageView) view.findViewById(R.id.circle_image);
        url = sharedPreferences.getString("headportrait","");
        Log.i(TAG, url);
        Glide.with(MineFragment.this).load(url)
                .error(R.mipmap.default_personal_image)
                .placeholder(R.mipmap.default_personal_image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        mCircleImageView.setImageDrawable(resource);
                    }
                });
        mCircleImageView.setOnClickListener(this);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setBackgroundResource(R.mipmap.mine_img_middle);

        username = (TextView) view.findViewById(R.id.username);
        username.setText(sharedPreferences.getString("deliver"," "));
        tv_phone_verification = (TextView) view.findViewById(R.id.mine_phone_verification);
        if ( sharedPreferences.getInt("isauth",0) == 1){
            Drawable drawable= getResources().getDrawable(R.mipmap.mine_phone_verification);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tv_phone_verification.setText("\t手机认证");
            tv_phone_verification.setCompoundDrawables(drawable,null,null,null);
        }else {
            tv_phone_verification.setCompoundDrawables(null,null,null,null);
            tv_phone_verification.setText("手机未认证");
        }

        //数据展示
        tv_finishorders = (TextView) view.findViewById(R.id.finishorders);
        tv_totalmile = (TextView) view.findViewById(R.id.totalmile);
        tv_totalmoney = (TextView) view.findViewById(R.id.totalmoney);
        initData();

        notification = (LinearLayout) view.findViewById(R.id.mine_notification);
        msgdot = (ImageView) view.findViewById(R.id.mine_msgdot);
        myproperty = (LinearLayout) view.findViewById(R.id.mine_myproperty);
        completeDetails = (LinearLayout) view.findViewById(R.id.complete_details);
        systemSetting = (LinearLayout) view.findViewById(R.id.systemsetting);
        notification.setOnClickListener(this);
        myproperty.setOnClickListener(this);
        completeDetails.setOnClickListener(this);
        systemSetting.setOnClickListener(this);
    }

    private void initData() {
        OkGo.post(API.BASEURL+"deliver/totalOrder")
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        s = s.toString().replace("null","0");
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(s);
                            int errno = jsonObject.optInt("errno");
                            if (errno == 200) {
                                JSONObject data = jsonObject.optJSONObject("data");
                                JSONObject count = data.optJSONObject("count");
                                String countOrder = count.optString("counto");
                                String totalincome = count.optString("countr");
                                String totalmile = count.optString("countc");
                                if (countOrder.equals("null"))
                                    countOrder = "0";
                                if (totalincome.equals("null"))
                                    totalincome = "0.00";
                                if (totalmile.equals("null"))
                                    totalmile = "0.00";
                                tv_finishorders.setText(countOrder+"单");
                                tv_totalmile.setText(totalmile+"km");
                                tv_totalmoney.setText(totalincome+"元");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.circle_image:
                startActivity(new Intent(mContext,CompleteDetailsActivity.class));
                break;
            case R.id.mine_notification:
                startActivity(new Intent(mContext,NotificationActivity.class));
                msgdot.setVisibility(View.GONE);
                break;
            case R.id.mine_myproperty:
                startActivity(new Intent(mContext, MyPropertyActivity.class));
                break;
            case R.id.complete_details:
                startActivityForResult(new Intent(MyApplication.getInstance(), CompleteDetailsActivity.class),10);
                break;
            case R.id.systemsetting:
                startActivity(new Intent(MyApplication.getInstance(),SystemSettingActivity.class));
                break;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        localBroadcastManager.unregisterReceiver(localReceive);
        localBroadcastManager.unregisterReceiver(newsReceive);
        localBroadcastManager.unregisterReceiver(phoneReceive);
    }
    //更改头像
    class ReseizeReceive extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            url = intent.getStringExtra("headportrait");
            Glide.with(MineFragment.this).load(url)
                    .placeholder(R.mipmap.default_personal_image)
                    .error(R.mipmap.default_personal_image)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(new SimpleTarget<GlideDrawable>() {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            mCircleImageView.setImageDrawable(resource);
                        }
                    });

        }
    }
    //新消息
    class NewsReceive extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            isReddot =  intent.getBooleanExtra("isreddotnews",false);
            Log.i(TAG, isReddot+"onReceive: ");
            if (isReddot) {
                msgdot.setVisibility(View.VISIBLE);
            }
        }
    }
    //手机认证
    class PhoneReceive extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {


            if (intent.getBooleanExtra("phoneVerification",false)) {
                Drawable drawable= getResources().getDrawable(R.mipmap.mine_phone_verification);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tv_phone_verification.setText("\t手机认证");
                tv_phone_verification.setCompoundDrawables(drawable,null,null,null);
            }else {
                tv_phone_verification.setCompoundDrawables(null,null,null,null);
                tv_phone_verification.setText("手机未认证");
            }
        }
    }
}
