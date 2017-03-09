package com.liuhesan.app.distributionapp.ui.personcenter;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.liuhesan.app.distributionapp.R;
import com.liuhesan.app.distributionapp.utility.API;
import com.liuhesan.app.distributionapp.utility.AppManager;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

public class ForgetPwdActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "ForgetPwdActivity";
    private EditText et_phone,et_code;
    private Button next;
    private String code;
    private Button  send_code;
    private ImageButton back;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        AppManager.getAppManager().addActivity(ForgetPwdActivity.this);
        initView();
    }

    private void initView() {
        back = (ImageButton) findViewById(R.id.forget_pwd_back);
        et_phone = (EditText) findViewById(R.id.forget_pwd_phone);
        et_code = (EditText) findViewById(R.id.forget_pwd_code);
        send_code = (Button) findViewById(R.id.send_code);
        next = (Button) findViewById(R.id.forget_pwd_next);
        back.setOnClickListener(this);
        next.setOnClickListener(this);
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
        switch (v.getId()){
            case R.id.forget_pwd_back:
                finish();
                break;
            case R.id.send_code:
                if (!TextUtils.isEmpty(et_phone.getText())){
                    if (send_code.isClickable()){
                        sendSMS();
                        send_code.setClickable(false);
                        countDownTimer.start();
                    }

                }else {
                    Toast.makeText(ForgetPwdActivity.this,"手机号不能为空",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.forget_pwd_next:
                if (!TextUtils.isEmpty(et_phone.getText()) && !TextUtils.isEmpty(et_code.getText())){
                    Log.i(TAG, code+"onClick: ");
                        if (et_code.getText().toString().trim().equals(code)){
                            Intent intent = new Intent(ForgetPwdActivity.this, NewPwdActivity.class);
                            intent.putExtra("phone",et_phone.getText().toString().trim());
                            startActivity(intent);
                        }else {
                            Toast.makeText(ForgetPwdActivity.this,"验证码不正确",Toast.LENGTH_SHORT).show();
                        }
                }else {
                    Toast.makeText(ForgetPwdActivity.this,"手机号或验证码不能为空",Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    private void sendSMS() {
        OkGo.post(API.BASEURL+"deliver/authMobile/")
                .params("mobile",et_phone.getText().toString().trim())
                .params("do","auth")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            int errno = jsonObject.getInt("errno");
                            if (errno == 200){
                                JSONObject data = jsonObject.optJSONObject("data");
                                code = data.optString("authcode");
                                Log.i(TAG, code+"onClick: ");
                            }
                            if (errno == 303){
                                send_code.setText("获取验证码");
                                send_code.setClickable(true);
                                countDownTimer.cancel();
                                Toast.makeText(ForgetPwdActivity.this,jsonObject.optString("errmsg"),Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
