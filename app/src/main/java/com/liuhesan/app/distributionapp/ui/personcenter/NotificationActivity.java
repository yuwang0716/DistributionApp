package com.liuhesan.app.distributionapp.ui.personcenter;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.liuhesan.app.distributionapp.R;
import com.liuhesan.app.distributionapp.fragment.NotificationMerchantFragment;
import com.liuhesan.app.distributionapp.fragment.NotificationSystemFragment;
import com.liuhesan.app.distributionapp.utility.API;
import com.liuhesan.app.distributionapp.utility.AppManager;
import com.liuhesan.app.distributionapp.widget.NoScrollViewPager;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class NotificationActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "NotificationActivity";
    private ImageButton back;
    private Button notification_merchant,notification_system;
    private NoScrollViewPager mViewPager;
    private List<Fragment> fragments;
    private FragmentPagerAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        initView();
        initAdapter();
    }

    private void initAdapter() {
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };
        mViewPager.setAdapter(mAdapter);
    }


    private void initView() {
        back = (ImageButton) findViewById(R.id.notification_back);

        back.setOnClickListener(this);
        notification_merchant = (Button) findViewById(R.id.notification_merchant);
        notification_system = (Button)findViewById(R.id.notification_system);
        mViewPager = (NoScrollViewPager) findViewById(R.id.notification_viewpager);
        mViewPager.setNoScroll(true);
        NotificationSystemFragment notificationSystemFragment = new NotificationSystemFragment();
        NotificationMerchantFragment notificationMerchantFragment = new NotificationMerchantFragment();
        fragments = new ArrayList<>();
        fragments.add(notificationSystemFragment);
        fragments.add(notificationMerchantFragment);
        notification_system.setOnClickListener(this);
        notification_merchant.setOnClickListener(this);
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.notification_system:
                mViewPager.setCurrentItem(0);
                notification_system.setBackground(getDrawable(R.drawable.finishorderbutton));
                notification_merchant.setBackground(getDrawable(R.drawable.loseefficacybutton));
                notification_system.setTextColor(getResources().getColor(R.color.colorText_title));
                notification_merchant.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case R.id.notification_merchant:
                mViewPager.setCurrentItem(1);
                notification_system.setBackground(getDrawable(R.drawable.finishorderbuttontwo));
                notification_merchant.setBackground(getDrawable(R.drawable.loseefficacybuttontwo));
                notification_system.setTextColor(getResources().getColor(R.color.colorPrimary));
                notification_merchant.setTextColor(getResources().getColor(R.color.colorText_title));
                break;
            case R.id.notification_back:
                finish();
                break;
        }
    }
}
