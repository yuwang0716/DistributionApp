package com.liuhesan.app.distributionapp.ui.personcenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;

import com.liuhesan.app.distributionapp.R;
import com.liuhesan.app.distributionapp.utility.API;
import com.liuhesan.app.distributionapp.utility.AppManager;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

public class WorkStateActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "WorkStateActivity";
    private RadioButton start,leave;
    private ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_state);
        AppManager.getAppManager().addActivity(WorkStateActivity.this);
        initView();
    }

    private void initView() {
        start = (RadioButton) findViewById(R.id.workstate_start);
        leave = (RadioButton) findViewById(R.id.workstate_leave);
        back = (ImageButton) findViewById(R.id.workstate_back);
        Intent intent = getIntent();
        String state = intent.getStringExtra("state").trim();
        Log.i(TAG, state+"initView: ");
        if (state.equals("工作")){
            start.setChecked(true);
        }if (state.equals("休息")){
            leave.setChecked(true);
        }
        start.setOnClickListener(this);
        leave.setOnClickListener(this);
        back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        final SharedPreferences sharedPreferences =  getSharedPreferences("login", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        final SharedPreferences.Editor edit = sharedPreferences.edit();
        switch (v.getId()){
            case R.id.workstate_start:
                if (start.isChecked()) {
                    leave.setChecked(false);
                    OkGo.post(API.BASEURL + "deliver/workStatus/")
                            .tag(this)
                            .params("token", token)
                            .params("status", 1)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(s);
                                        int errno = jsonObject.optInt("errno");
                                        if (errno == 200){
                                            //启动定位
                                            edit.putString("state","工作");
                                            edit.commit();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });

                }
                break;
            case R.id.workstate_leave:
                if (leave.isChecked()) {
                    start.setChecked(false);
                        OkGo.post(API.BASEURL+"deliver/workStatus/")
                                .tag(this)
                                .params("token",token)
                                .params("status",0)
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(String s, Call call, Response response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(s);
                                            int errno = jsonObject.optInt("errno");
                                            if (errno == 200){
                                                //启动定位
                                                edit.putString("state","休息");
                                                edit.commit();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                }
                break;
            case R.id.workstate_back:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
