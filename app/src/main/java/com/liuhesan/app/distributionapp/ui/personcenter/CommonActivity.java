package com.liuhesan.app.distributionapp.ui.personcenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.liuhesan.app.distributionapp.R;
import com.liuhesan.app.distributionapp.utility.AppManager;

public class CommonActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "CommonActivity";
    private CheckBox news,sound,shake;
    private ImageButton back;
    private RelativeLayout rl_sound,rl_shake;
    private LocalBroadcastManager localBroadcastManager;
    private Intent intent;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor edit;
    private boolean isSound,isShake;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);
        AppManager.getAppManager().addActivity(CommonActivity.this);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        intent = new Intent("com.liuhesan.app.distributionapp.Notification");
        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        edit = sharedPreferences.edit();
        initView();
    }

    private void initView() {
        back = (ImageButton) findViewById(R.id.common_back);
        news = (CheckBox) findViewById(R.id.common_news);
        sound = (CheckBox) findViewById(R.id.common_sound);
        shake = (CheckBox) findViewById(R.id.common_shake);
        rl_sound = (RelativeLayout) findViewById(R.id.rl_sound);
        rl_shake = (RelativeLayout) findViewById(R.id.rl_shake);

        isSound = sharedPreferences.getBoolean("isSound",false);
        isShake =  sharedPreferences.getBoolean("isShake",false);

        if (isShake || isShake){
            news.setChecked(true);
            rl_sound.setVisibility(View.VISIBLE);
            rl_shake.setVisibility(View.VISIBLE);
            sound.setChecked(isSound);
            shake.setChecked(isShake);
        }
        if (!isSound && !isShake){
            news.setChecked(false);
            rl_sound.setVisibility(View.GONE);
            rl_shake.setVisibility(View.GONE);
            sound.setChecked(isSound);
            shake.setChecked(isShake);
        }
        back.setOnClickListener(this);
        news.setOnClickListener(this);
        sound.setOnClickListener(this);
        shake.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.common_back:
                finish();
                break;
            case R.id.common_news:
                if (news.isChecked()) {
                    rl_sound.setVisibility(View.VISIBLE);
                    rl_shake.setVisibility(View.VISIBLE);
                    edit.commit();
                }else {
                    rl_sound.setVisibility(View.GONE);
                    rl_shake.setVisibility(View.GONE);
                    edit.putBoolean("isSound",false);
                    edit.putBoolean("isShake",false);
                    edit.commit();
                }
                break;
            case R.id.common_sound:
                if (news.isChecked()) {
                    if (sound.isChecked()) {
                        intent.putExtra("isSound",true);
                        localBroadcastManager.sendBroadcast(intent);
                        edit.putBoolean("isSound",true);
                        edit.commit();
                    }else {
                        intent.putExtra("isSound",false);
                        localBroadcastManager.sendBroadcast(intent);
                        edit.putBoolean("isSound",false);
                        edit.commit();
                    }
                }
                break;
            case R.id.common_shake:
                if (news.isChecked()) {
                    if (shake.isChecked()) {
                        intent.putExtra("isShake",true);
                        localBroadcastManager.sendBroadcast(intent);
                        edit.putBoolean("isShake",true);
                        edit.commit();
                    }else {
                        intent.putExtra("isShake",false);
                        localBroadcastManager.sendBroadcast(intent);
                        edit.putBoolean("isShake",false);
                        edit.commit();
                    }
                }
                break;
        }
    }
}
