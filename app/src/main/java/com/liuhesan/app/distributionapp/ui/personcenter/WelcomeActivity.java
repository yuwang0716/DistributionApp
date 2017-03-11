package com.liuhesan.app.distributionapp.ui.personcenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.liuhesan.app.distributionapp.R;
import com.liuhesan.app.distributionapp.utility.AppManager;
import com.liuhesan.app.distributionapp.utility.SharedPreferencesUtil;

import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

public class WelcomeActivity extends AppCompatActivity {
    private static final String TAG = "WelcomeActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 判断是否是第一次开启应用
      boolean isFirstOpen = SharedPreferencesUtil.getBoolean(this, SharedPreferencesUtil.FIRST_OPEN, true);

        // 如果是第一次启动，则先进入功能引导页
        if (isFirstOpen) {
            Intent intent = new Intent(this, WelcomeGuideActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        setContentView(R.layout.activity_welcome);
        AppManager.getAppManager().addActivity(WelcomeActivity.this);
        SharedPreferences sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        if (TextUtils.isEmpty(username)){
            startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
            finish();
        }else {
            URL url= null;//取得资源对象
            try {
                url = new URL("http://www.bjtime.cn");
                URLConnection uc=url.openConnection();//生成连接对象
                uc.connect(); //发出连接
                long ld=uc.getDate(); //取得网站日期时间
                Date date=new Date(ld);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putString("cdate",date.getYear()+"-"+date.getMonth()+"-"+date.getDay());
				edit.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }

            startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
            finish();
        }
    }

}
