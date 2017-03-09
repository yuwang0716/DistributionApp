package com.liuhesan.app.distributionapp.ui.personcenter;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liuhesan.app.distributionapp.R;
import com.liuhesan.app.distributionapp.service.LocationService;
import com.liuhesan.app.distributionapp.utility.AppManager;
import com.liuhesan.app.distributionapp.utility.DataCleanManager;

public class SystemSettingActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout changPwd;
    private ImageButton back;
    private LinearLayout clearData,common,aboutus;
    private Button exit;
    private TextView tv_clearData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_setting);
        AppManager.getAppManager().addActivity(SystemSettingActivity.this);
        initView();
    }

    private void initView() {
        back = (ImageButton) findViewById(R.id.systemsetting_back);
        changPwd = (LinearLayout) findViewById(R.id.systemsetting_changepwd);
        common = (LinearLayout) findViewById(R.id.systemsetting_common);
        clearData = (LinearLayout) findViewById(R.id.systemsetting_clearcache);
        exit = (Button) findViewById(R.id.systemsetting_exit);
        tv_clearData = (TextView) findViewById(R.id.tv_clearcache);
        aboutus = (LinearLayout) findViewById(R.id.systemsetting_about);
        try {
            tv_clearData.setText(DataCleanManager.getTotalCacheSize(SystemSettingActivity.this));
        } catch (Exception e) {
            e.printStackTrace();
        }
        back.setOnClickListener(this);
        changPwd.setOnClickListener(this);
        common.setOnClickListener(this);
        clearData.setOnClickListener(this);
        exit.setOnClickListener(this);
        aboutus.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.systemsetting_back:
                finish();
                break;
            case R.id.systemsetting_changepwd:
                startActivity(new Intent(SystemSettingActivity.this, ChangePwdActivity.class));
                break;
            case R.id.systemsetting_common:
                startActivity(new Intent(SystemSettingActivity.this,CommonActivity.class));
                break;
            case R.id.systemsetting_clearcache:
                DataCleanManager.clearAllCache(SystemSettingActivity.this);
                tv_clearData.setText("0.0kb");
                break;
            case R.id.systemsetting_exit:
                SharedPreferences sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
                sharedPreferences.edit().clear().commit();
                startActivity(new Intent(SystemSettingActivity.this,LoginActivity.class));
                AppManager.getAppManager().finishAllActivity();
                NotificationManager manger = (NotificationManager)this.getSystemService(NOTIFICATION_SERVICE);
                manger.cancelAll();
                stopService(new Intent(SystemSettingActivity.this,LocationService.class));
                break;
            case R.id.systemsetting_about:
                startActivity(new Intent(SystemSettingActivity.this,AboutUsActivity.class));
                break;
        }
    }
}
