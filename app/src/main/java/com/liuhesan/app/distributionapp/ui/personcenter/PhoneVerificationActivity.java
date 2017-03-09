package com.liuhesan.app.distributionapp.ui.personcenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.liuhesan.app.distributionapp.R;
import com.liuhesan.app.distributionapp.utility.API;
import com.liuhesan.app.distributionapp.utility.AppManager;
import com.liuhesan.app.distributionapp.utility.HookViewClickUtil;
import com.liuhesan.app.distributionapp.utility.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

public class PhoneVerificationActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "PhoneVerification";
    private ImageButton back;
    private EditText phonenum,code;
    private Button verification;
    private Button  send_code;
    private String authcode;
    private LocalBroadcastManager localBroadcastManager;
    private Intent intent;
    private CountDownTimer countDownTimer;
    private PopupWindow popupwindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);
        AppManager.getAppManager().addActivity(PhoneVerificationActivity.this);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                HookViewClickUtil.hookView(verification);
            }
        });
        initView();
    }

    private void initView() {
        back = (ImageButton) findViewById(R.id.phone_verification_back);
        phonenum = (EditText) findViewById(R.id.phonenum);
        code = (EditText) findViewById(R.id.verification_code);
        send_code = (Button) findViewById(R.id.send_code);
        verification = (Button) findViewById(R.id.verification);
        back.setOnClickListener(this);
        verification.setOnClickListener(this);
        countDownTimer = new CountDownTimer(60*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                send_code.setText((millisUntilFinished/1000)+"秒");
            }

            @Override
            public void onFinish() {
                send_code.setText("重新发送");
                send_code.setClickable(true);
                countDownTimer.cancel();
            }
        };
        send_code.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_code:
                if (TextUtils.isEmpty(phonenum.getText().toString())){
                    ToastUtil.showToast(PhoneVerificationActivity.this,"手机号或验证码不能为空");
                }else {
                    if (send_code.isClickable()){
                        sendSMS();
                        send_code.setClickable(false);
                        countDownTimer.start();
                    }

                }
                break;
            case R.id.verification:
                    if (!TextUtils.isEmpty(phonenum.getText()) && !TextUtils.isEmpty(code.getText())){
                        if (code.getText().toString().equals(authcode)){
                            View customView = View.inflate(PhoneVerificationActivity.this,R.layout.popview_logining,null);
                            popupwindow = new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                            popupwindow.showAtLocation(v, Gravity.CENTER,0,0);
                            OkGo.post(API.BASEURL + "deliver/authMobile/")
                                    .tag(this)
                                    .params("mobile", phonenum.getText().toString().trim())
                                    .params("do","doauth")
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onSuccess(String s, Call call, Response response) {
                                            Log.e(TAG, s+"onSuccess: " );
                                            try {
                                                JSONObject jsonObject = new JSONObject(s);
                                                int errno = jsonObject.optInt("errno");
                                                if (errno == 200){
                                                    Toast.makeText(PhoneVerificationActivity.this,"手机号认证成功",Toast.LENGTH_SHORT).show();
                                                    AppManager.getAppManager().finishActivity(CompleteDetailsActivity.class);
                                                    AppManager.getAppManager().finishActivity(PhoneVerificationActivity.class);
                                                    intent = new Intent("com.liuhesan.app.distributionapp.PHONEVERIFICATION");
                                                    intent.putExtra("phoneVerification",true);
                                                    localBroadcastManager.sendBroadcast(intent);
                                                    SharedPreferences sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
                                                    SharedPreferences.Editor edit = sharedPreferences.edit();
                                                    edit.putInt("isauth",1);
                                                    edit.commit();
                                                }
                                                if (errno == 301){
                                                    Toast.makeText(PhoneVerificationActivity.this,jsonObject.optString("errmsg"),Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }finally {
                                                popupwindow.dismiss();
                                            }
                                        }
                                    });
                        }else {
                            Toast.makeText(PhoneVerificationActivity.this,"验证码输入错误",Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        Toast.makeText(PhoneVerificationActivity.this,"手机号或验证码不能为空",Toast.LENGTH_SHORT).show();
                    }
                break;
            case R.id.phone_verification_back:
                finish();
                break;
        }
    }
    private void sendSMS(){
        if (!TextUtils.isEmpty(phonenum.getText())) {

            OkGo.post(API.BASEURL + "deliver/authMobile/")
                    .tag(this)
                    .params("mobile", phonenum.getText().toString().trim())
                    .params("do", "auth")
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                int errno = jsonObject.optInt("errno");
                                if (errno ==200){
                                    JSONObject data = jsonObject.optJSONObject("data");
                                    authcode = data.optString("authcode");
                                }
                                if (errno == 303){
                                    Toast.makeText(PhoneVerificationActivity.this,jsonObject.optString("errmsg"),Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();

                            }
                        }
                    });
        }
    }
}
